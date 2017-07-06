package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * This server provides access to files inside 
 * directory provided in server properties.
 * If file has extension smsrc it will be
 * treated as smart script.
 * Server allows calling workers
 * defined in workers properties.
 * Server also allows calling
 * any worker that is inside package
 * "hr.fer.zemris.java.webserver.workers",
 * to do this start path with "/ext".
 * Server provides sessions.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.6.2016.)
 */
public class SmartHttpServer {
	
	/**
	 * Single session entry.
	 * Each entry has unique key defining client,
	 * time until this session entry is valid
	 * and map of persistent parameters.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.6.2016.)
	 */
	private static class SessionMapEntry {
		//removed sid from here because it was already in ClientWorker
		/** Milisecond when session becomes unusable. */
		private volatile long validUntil;
		/** Persistent parameters. Parameters saved in session. */
		private Map<String, String> map = new ConcurrentHashMap<>();
	}
	
	/** Server address. */
	private String address;
	/** Server port. */
	private int port;
	/** Max number of {@link ClientWorker}s. */
	private int workerThreads;
	/** Number of seconds until session becomes unusable. */
	private int sessionTimeout;
	/** All supported mime types. */
	private Map<String, String> mimeTypes = new HashMap<>();
	/** Single thread where server takes requests. */
	private ServerThread serverThread;
	/** Pool of thread where requests are processed. */
	private ExecutorService threadPool;
	/** Root path accessible from your web server. */
	private Path documentRoot;
	/** Map of workers. From worker properties file. */
	private Map<String,IWebWorker> workersMap = new HashMap<>();
	/** Map of all sessions. Key is SID of client. */
	private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
	/** Random generator used to generate SID. */
	private Random sessionRandom = new Random();
	
	/** Used to remove old sessions from {@link #sessions}. Checks every 5 minutes. */
	private Runnable sessionCleaner = new Runnable() {
		@Override
		public void run() {
			while(true) {
				try {
					TimeUnit.MINUTES.sleep(5);
				} catch (InterruptedException ignorable) {
				}
				long current = System.currentTimeMillis();
				Iterator<Map.Entry<String, SessionMapEntry>> iterator = sessions.entrySet().iterator();
				while(iterator.hasNext()) {
					Entry<String, SessionMapEntry> entry = iterator.next();
					if (entry.getValue().validUntil < current) {
						iterator.remove();
					}
				}
			}
		}
	};

	/**
	 * From given config file read all properties and initializes:
	 * address, port, number of worker thread, session timeout,
	 * document root, mime types.
	 * 
	 * Will throw {@link RuntimeException} if given properties file is invalid.
	 * 
	 * @param configFileName		server properties file.
	 */
	public SmartHttpServer(String configFileName) {
		Properties serverConfig = getProperties(configFileName);
		
		try {
			address = serverConfig.getProperty("server.address");
			port = Integer.parseInt(serverConfig.getProperty("server.port"));
			workerThreads = Integer.parseInt(serverConfig.getProperty("server.workerThreads"));
			documentRoot = Paths.get(serverConfig.getProperty("server.documentRoot"));
			sessionTimeout = Integer.parseInt(serverConfig.getProperty("session.timeout"));
		
			String mimeFilePath = serverConfig.getProperty("server.mimeConfig");
			fillMimeTypes(mimeFilePath);
			
		} catch (NumberFormatException | InvalidPathException exc) {
			throw new RuntimeException("Invalid property in "+configFileName);
		}
		
		if(address == null) {
			throw new RuntimeException("Address was not specified in given config file.");
		}
		
		loadWorkers(serverConfig.getProperty("server.workers"));
		
		Thread cleaner = new Thread(sessionCleaner);
		cleaner.setDaemon(true);
		cleaner.start();
	}
	
	/**
	 * From given properties file reads workers
	 * and puts them in {@link #workersMap}.
	 * 
	 * @param filePath		config file for workers.
	 */
	private void loadWorkers(String filePath) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			for (String line : lines) {
				if(line.startsWith("/")) line=line.substring(1);
				line = line.trim();
				if(line.isEmpty()) continue;
				if(line.startsWith("#")) continue;				
				int splitIndex = line.indexOf("=");
				if(splitIndex == -1) throw new RuntimeException("Invalid worker: "+line);
				String path = line.substring(0, splitIndex).trim();
				if(workersMap.containsKey(path)) {
					throw new RuntimeException(filePath+" has 2+ paths for "+path);
				}
				String fqcn = line.substring(splitIndex+1).trim();
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				workersMap.put(path, iww);
			}
		} catch (Exception exc) {
			throw new RuntimeException("Unable to load workers from " + filePath);
		}
	}

	/**
	 * Retrieves all mime types from given file path.
	 * Mime types are put in {@link #mimeTypes}.
	 * Will throw {@link RuntimeException} if unable to read file.
	 * 
	 * @param mimeFilePath		where mime types are.
	 */
	private void fillMimeTypes(String mimeFilePath) {
		List<String> lines = null;
		try {
			Path path = Paths.get(mimeFilePath);
			if(Files.notExists(path)) {
				throw new RuntimeException(mimeFilePath + " not found.");
			}
			lines = Files.readAllLines(path);
			
		} catch (InvalidPathException e) {
			throw new RuntimeException(mimeFilePath + " is not valid path.");
		} catch (IOException e) {
			throw new RuntimeException("I/O exception while reading file.");
		} 
		
		if(lines == null) return;
 		
		for(String line : lines) {
			line = line.trim();
			if(line.isEmpty()) continue;
			if(line.startsWith("#")) continue;
			
			int splitIndex = line.indexOf("=");
			if(splitIndex == -1) throw new RuntimeException("Invalid mime type: "+line);
			String left = line.substring(0, splitIndex).trim();
			String right = line.substring(splitIndex+1).trim();
			mimeTypes.put(left, right);
		}
	}
	
	/**
	 * Retrieves properties from given file path.
	 * Will throw {@link RuntimeException} if unable to retrieve.
	 * 
	 * @param filePath	where properties are.
	 * @return properties from given file.
	 */
	private Properties getProperties(String filePath) {
		Properties p = new Properties();
		
		try (FileInputStream inputStream = new FileInputStream(new File(filePath))){
			p.load(inputStream);
		} catch (FileNotFoundException fileError) {
			throw new RuntimeException("File '"+filePath+"' not found.");
		} catch (IOException IOError) {
			throw new RuntimeException("Error while reading from input stream.");
		} catch (Exception exc) {
			throw new RuntimeException("Can not load given property.");
		}
		return p;
	}

	/**
	 * Starts server.
	 * Starts server thread and initializes thread pool.
	 */
	protected synchronized void start() {
		if(serverThread == null || !serverThread.isAlive()) {
			serverThread = new ServerThread();
			serverThread.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Stops server.
	 * Interrupts server thread and shuts down treahd pool.
	 */
	protected synchronized void stop() {
		serverThread.interrupt();
		threadPool.shutdown();
	}

	/**
	 * Thread in which server will take requests.
	 * This thread will not process requests.
	 * Once request is accepted it is submitted
	 * to thread pool.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.6.2016.)
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()){
				serverSocket.bind(
						new InetSocketAddress((InetAddress)null, port)
				);
				
				while(true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
				
			} catch (IOException e) {
				throw new RuntimeException("Unable to create server thread.");
			} 
		}
	}

	/**
	 * This class implements work that server does for client.
	 * Each client request will result in new thread 
	 * of this runnable class.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.6.2016.)
	 */
	private class ClientWorker implements Runnable {
		/** Client socket. */
		private Socket csocket;
		/** Input stream. Read from client. */ 
		private PushbackInputStream istream;
		/** Output stream. Write to client. */
		private OutputStream ostream;
		/** HTML version in request. */
		private String version;
		/** Method in request. */
		private String method;
		/** Parameters provided by client. */
		private Map<String, String> params = new HashMap<String, String>();
		/** Persistent parameters used by client. */
		private Map<String, String> permPrams = null;
		/** Cookies that will be sent. */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/** Session id for this client. */
		private String sid;

		/**
		 * Only initializes client socket. 
		 * 
		 * @param csocket		client socket.
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
				List<String> request = readRequest();
				if(request.isEmpty()) {
					sendError(400, "Invalid header");
					return;
				}
				if(!validteFirstLine(request.get(0))) {
					return;
				}
				
				checkSession(request);
				outputCookies.add(
					new RCCookie("sid", sid, null, getDomain(request), "/", true)
				);
				
				String path = resolvePath(
					request.get(0).split("\\s+")[1]
				);
				
				if(workersMap.containsKey(path)) {
					RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);
					workersMap.get(path).processRequest(rc);
					return;
				}
				if(path.startsWith("ext/")) {
					execute(path.substring(4)); //4 to remove "ext/"
					return;
				}
				Path tempPath = getValidPath(path);
				if(tempPath == null) {
					return;
				}
				
				String extension = null;
				int extensionIndex = path.lastIndexOf(".");
				if(extensionIndex != -1 && extensionIndex < path.length()-1) {
					extension = path.substring(extensionIndex+1);
					
					if (extension.equals("smscr")) {
						executeSmsrc(tempPath);
						return;
					}
				}
				String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");
				
				RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);
				rc.setMimeType(mimeType);
				rc.write(Files.readAllBytes(tempPath));
				
			} catch (IOException exc) {
				//what is smart to do here?
				throw new RuntimeException("Server crashed.");
			} finally {
				try {
					csocket.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to close client socket.");
				}
			}
			
		}

		/**
		 * Executes smsrc file.
		 * 
		 * @param scriptPath	path to the smsrc file.
		 * @throws IOException if unable to read from given path.
		 */
		private void executeSmsrc(Path scriptPath) throws IOException {
			String documentBody = new String(
				Files.readAllBytes(scriptPath), 
				StandardCharsets.UTF_8
			);
			new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(ostream, params, permPrams, outputCookies)
			).execute();
		}

		/**
		 * Returns path that will be valid 
		 * or null if path is not good.
		 * 
		 * @param path		relative path being checked.
		 * @return valid path or null.
		 * 
		 * @throws IOException if unable to send error about why file is invalid.
		 */
		private Path getValidPath(String path) throws IOException {
			Path tempPath = documentRoot.resolve(path);
			if(!tempPath.startsWith(documentRoot)) {
				sendError(403, "Forbidden access");
				return null;
			}
			if(!Files.exists(tempPath) 
					|| !Files.isRegularFile(tempPath) 
					|| !Files.isReadable(tempPath)) {
				sendError(404, "Unable to read file");
				return null;
			}
			return tempPath;
		}

		/**
		 * From given line stores {@link #version} and {@link #method}.
		 * If given line is invalid return false.
		 * 
		 * @param firstLine		first line in header.
		 * @return true if given line is valid, false otherwise.
		 * 
		 * @throws IOException if unable to send error about why line is invalid.
		 */
		private boolean validteFirstLine(String firstLine) throws IOException {
			String[] elements = firstLine.split("\\s+");
			if(elements.length != 3) {
				sendError(400, "Invalid header");
				return false;
			}
			method = elements[0].toUpperCase();
			if(!method.equals("GET")) {
				sendError(400, "Method not allowed");
				return false;
			}
			version = elements[2].toUpperCase();
			if(!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(400, "Version not supported");
				return false;
			}
			return true;
		}

		/**
		 * From request retrieves domain.
		 * 
		 * @param request		client request.
		 * @return domain.
		 */
		private String getDomain(List<String> request) {
			String domain = null;
			for (String line : request) {
				if (line.startsWith("Host: ")) {
					int start = "Host:".length();
					int end = line.lastIndexOf(":");
					domain = line.substring(start, end);
					break;
				}
			}
			return domain;
		}

		/**
		 * Loads session of this client,
		 * or if there is no session
		 * creates new one.
		 * 
		 * @param request		client request.
		 */
		private void checkSession(List<String> request) {
			if (sid == null) {
				loadSid(request);
			}
			SessionMapEntry session = null;
			if(sid != null) {
				session = sessions.get(sid);
			}
			
			if(session != null) {
				long current = System.currentTimeMillis();
				if(session.validUntil < current) {
					sessions.remove(sid);
					session = null;
				}
			}
			if(session == null) {
				sid = generateSID();
				session = new SessionMapEntry();
				sessions.put(sid, session);
			}
			
			session.validUntil = System.currentTimeMillis()
				+ TimeUnit.SECONDS.toMillis(sessionTimeout);
			
			permPrams = session.map;
		}
		
		/**
		 * Loads {@link #sid} from cookies provided in client request.
		 * 
		 * @param request		client request.
		 */
		private void loadSid(List<String> request) {
			for (String line : request) {
				if (line.startsWith("Cookie:")) {
					int index = line.indexOf("sid=\"");
					if (index == -1) continue; 	// not sure if there can be more lines
												// with cookies, break would be better if not
					String sidAndMore = line.substring(index + 5);
					int secondQuote = sidAndMore.indexOf("\"");
					sid = sidAndMore.substring(0, secondQuote);
				}
			}
		}

		/**
		 * Generates random SID.
		 * It will contain 20 A-Z letters.
		 * 
		 * @return random session id.
		 */
		private String generateSID() {
			char[] letters = new char[20];
			for(int i = 0; i < 20; i++) {
				letters[i] = (char)(sessionRandom.nextInt(('Z'-'A')+1) + 'A');
			}
			return new String(letters);
		}

		/**
		 * Executes worker from
		 * hr.fer.zemris.java.webserver.workers.
		 * Given worker name must be a class 
		 * implementing {@link IWebWorker}.
		 * If worker doesn't exists throws
		 * runtime execpetion.
		 * 
		 * @param workerName		name of worker class.
		 */
		private void execute(String workerName) {
			try {
				String fqcn = "hr.fer.zemris.java.webserver.workers."+workerName;
				Class<?> referenceToClass;
				referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject;
				newObject = referenceToClass.newInstance();
				IWebWorker worker = (IWebWorker) newObject;
				RequestContext context = new RequestContext(ostream, params, permPrams, outputCookies);
				worker.processRequest(context);
			} catch (Exception exc) {
				throw new RuntimeException("Unable to execute " + workerName);
			}
		}

		/**
		 * Takes request path and returns real path.
		 * If url had parameters they will be 
		 * stored in {@link #params}.
		 * 
		 * Example:
		 * "abc/def?name=joe&country=usa"
		 * would return "abc/def"
		 * but would also do 
		 * <code>params.put("name","joe")</code>
		 * and <code>params.put("country","usa")</code>
		 * 
		 * @param string	requested path.
		 * @return real path.
		 */
		private String resolvePath(String string) {
			if(string.startsWith("/")) string = string.substring(1);
			int splitIndex = string.indexOf("?");
			if(splitIndex == -1) {
				return string;
			}
			String realPath = string.substring(0, splitIndex);
			String[] urlParams = string.substring(splitIndex+1).split("&");
			for(String p : urlParams) {
				int index = p.indexOf("=");
				if(index == -1) break;
				String left = p.substring(0, index);
				String right = p.substring(index+1);
				params.put(left, right);
			}
			return realPath;
		}

		/**
		 * Send HTML error message to {@link #ostream}.
		 * 
		 * @param statusCode	status code in message.
		 * @param statusText	text describin error.
		 * @throws IOException if can't write to {@link #ostream}.
		 */
		private void sendError(int statusCode, String statusText) throws IOException {

			ostream.write(
					("HTTP/1.1 " + statusCode+" " + statusText+ "\r\n"+
					"Server: simple java server\r\n"+
					"Content-Type: text/plain;charset=UTF-8\r\n"+
					"Content-Length: 0\r\n"+
					"Connection: close\r\n"+
					"\r\n").getBytes(StandardCharsets.US_ASCII)
			);
			ostream.flush();
		}
		
		/**
		 * Reads everything from input and 
		 * retrieves list of every read line.
		 * 
		 * @return list of lines from input.
		 * @throws IOException	if can't read from input stream.
		 */
		private List<String> readRequest() throws IOException {
			List<String> headers = new ArrayList<>();
			String requestStr = new String(
					getBytes(), 
					StandardCharsets.US_ASCII
			);
			String currentLine = null;
			for (String s : requestStr.split("\n")) {
				if (s.isEmpty()) break;
				
				char c = s.charAt(0);
				if (c==9 || c==32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
		
		/**
		 * Used to read all bytes from {@link #istream}.
		 * 
		 * @return		all bytes from input stream.
		 * @throws IOException	if can't read from {@link #istream}.
		 */
		private byte[] getBytes() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
l:			while (true) {
				int b = istream.read();
				if (b == -1) return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0: 
					if (b==13) { state=1; } else if (b==10) state=4;
					break;
				case 1: 
					if (b==10) { state=2; } else state=0;
					break;
				case 2: 
					if (b==13) { state=3; } else state=0;
					break;
				case 3: 
					if (b==10) { break l; } else state=0;
					break;
				case 4: 
					if (b==10) { break l; } else state=0;
					break;
				}
			}
			return bos.toByteArray();
		}
	}
	
	/**
	 * Entry point.
	 * 
	 * @param args		config server file path.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Expected 1 argument: server config file path.");
			System.exit(-1);
		}
		try {
			SmartHttpServer server = new SmartHttpServer(args[0]);
			server.start();
			System.out.println("Server started.");
		} catch(Exception information) {
			System.err.println(information.getMessage());
		}
	}
}
