package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to write output to client.
 * Before first writing it will first write header.
 * Once header is written is it not allowed to
 * change any of properties defined in header.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.6.2016.)
 */
public class RequestContext {
	
	/**
	 * This class represents a single
	 * cookie used when answering to request.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.6.2016.)
	 */
	public static class RCCookie {
		/** Read-only name of this cookie. */
		private final String name;
		/** Read-only value of name for this cookie. */
		private final String value;
		/** Read-only domain of this cookie. */
		private final String domain;
		/** Read-only path of this cookie. */
		private final String path;
		/** Read-only max age of this cookie. */
		private final Integer maxAge;
		/** Flag representing if cookie is for http only. */
		private final boolean httpOnly;
		
		/**
		 * Initializes read-only properties of cookie.
		 * 
		 * @param name		cookie name.
		 * @param value		value of name.
		 * @param maxAge	cookie max age.
		 * @param domain	cookie domain.
		 * @param path 		cookie path.
		 * @param httpOnly	<code>true</code> if only http can use this cookie.
		 * 
		 * @exception IllegalArgumentException if name or value are null.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
			if(name == null || value == null) {
				throw new IllegalArgumentException("Name and value should not be null.");
			}
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.httpOnly = httpOnly;
		}
		
		/**
		 * Initializes read-only properties of cookie.
		 * 
		 * @param name		cookie name.
		 * @param value		value of name.
		 * @param maxAge	cookie max age.
		 * @param domain	cookie domain.
		 * @param path 		cookie path.
		 * 
		 * @exception IllegalArgumentException if name or value are null.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this(name, value, maxAge, domain, path, false);
		}

		/**
		 * Retrieves name used in this cookie.
		 * 
		 * @return name used in this cookie.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Retrieves value of name used in this cookie.
		 * 
		 * @return value of name used in this cookie.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Retrieves domain used in this cookie.
		 * 
		 * @return domain used in this cookie.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Retrieves path used in this cookie.
		 * 
		 * @return path used in this cookie.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Retrieves max age used in this cookie.
		 * 
		 * @return max age used in this cookie.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}

	/** Stream used to write to client. */
	private OutputStream outputStream;		
	
	/** {@code true} after header is generated. */
	private boolean headerGenerated = false;
	/** Charset used to write to client. */
	private Charset charset;				
	/** Encoding used to create charset. */
	private String encoding = "UTF-8";		
	/** Answer status code. */
	private int statusCode = 200;			
	/** Answer status text. */
	private String statusText = "OK";		
	/** MIME type of answer. */
	private String mimeType = "text/html";	
	
	/** Parameters used by client. */
	private Map<String,String> parameters;			
	/** Temporary parameters used by client. */
	private Map<String,String> temporaryParameters;
	/** Persistent parameters used by client. */
	private Map<String,String> persistentParameters;
	/** Cookies that will be added to header. */
	private List<RCCookie> outputCookies;
	
	/**
	 * Initializes stream where {@link #write(byte[])} 
	 * and {@link #write(String)} will write, 
	 * both parameters and persistent parameters and
	 * cookies used in this request.
	 * 
	 * @param outputStream				output stream.
	 * @param parameters				parameters used by client.
	 * @param persistentParameters		persistent parameters used by client.
	 * @param outputCookies				cookies that will be added to header.
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters, 
			Map<String,String> persistentParameters, List<RCCookie> outputCookies) {
		
		if(outputStream == null) {
			throw new IllegalArgumentException("Output stream must not be null!");
		}
		
		this.outputStream = outputStream;
		this.parameters = parameters == null ? 
			new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? 
			new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies == null ? 
			new ArrayList<>() : outputCookies;
		temporaryParameters = new HashMap<>();
		
		this.parameters = Collections.unmodifiableMap(this.parameters); //make map read-only
	}
	
	/**
	 * Adds cookie to this request.
	 * <code>null</code> will not be added.
	 * Can not be changed after header is generated.
	 * 
	 * @param cookie		cookie being added.	
	 */
	public void addRCCookie(RCCookie cookie) {
		checkHeader("cookies");
		if(cookie == null) return;
		outputCookies.add(cookie);
	}
	
	/**
	 * Retrieves value of given parameter.
	 * Returns <code>null</code> if no association exists.
	 * 
	 * @param name		key of parameter.
	 * @return value of parameter.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Retrieves names of all parameters.
	 * Returned set is read-only.
	 * 
	 * @return name of all parameters.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Retrieves value of given persistent parameter.
	 * Returns <code>null</code> if no association exists.
	 * 
	 * @param name		key of persistent parameter.
	 * @return value of persistent parameter.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Retrieves names of all parameters in persistent parameters map.
	 * Set is read-only.
	 * 
	 * @return set of all parameters in persistent parameters.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Stores a value to persistent parameters.
	 * 
	 * @param name		key of persistent parameter.
	 * @param value		value of persistent parameter.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes given persistent parameter.
	 * 
	 * @param name			key of persistent parameter.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Retrieves value from temporary parameters map. 
	 * Returns <code>null</code> if no association exists.
	 * 
	 * @param name		key of temporary parameter.
	 * @return value of temporary parameter.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Retrieves names of all parameters in temporary parameters map.
	 * Set is read-only.
	 * 
	 * @return set of all parameters in temporary parameters.
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Stores a value to temporary parameters.
	 * 
	 * @param name		key of temporary parameter.
	 * @param value		value of temporary parameter.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes a value from temporary parameters.
	 * 
	 * @param name		key of temporary parameter.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Writes given data into output stream 
	 * that was given to in constructor.
	 * 
	 * @param data		data to be written into output stream.
	 * @return this object.
	 * 
	 * @throws IOException if unable to write to output stream.
	 */
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader());
			headerGenerated = true;
		}
		outputStream.write(data);
		outputStream.flush();
		return this;
	}

	/**
	 * Writes given text into output stream 
	 * that was given to in constructor.
	 * 
	 * @param text		text to be written into output stream.
	 * @return this object.
	 * 
	 * @throws IOException if unable to write to output stream.
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			charset = Charset.forName(encoding);
		}
		return write(text.getBytes(charset));
	}
	
	/**
	 * Generates header and retrieves it.
	 * Header is serialized into bytes using code ISO_8859_1.
	 * <p>
	 * Lines are separated by "\r\n".
	 * First line is: "HTTP/1.1 {@link #statusCode} {@link #statusText}".
	 * Second line is: "Content-Type: {@link #mimeType}".
	 * 		If {@link #mimeType} starts with "text/",
	 * 		"; charset={@link #encoding}" is added to the end of second line.
	 * Next follows lines equal to size of {@link #outputCookies}.
	 * 		Those lines consist of {@link RCCookie} properties.
	 * At the of header is added "\r\n".
	 * </p>
	 * 
	 * @return header serialized into bytes.
	 */
	private byte[] generateHeader() {
		StringBuilder header = new StringBuilder();
		header.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n")
			.append("Content-Type: " + mimeType);
		if(mimeType.startsWith("text/")) { 
			header.append("; charset=" + encoding);
		}
		header.append("\r\n");
		
		for(RCCookie cookie : outputCookies) {
			header.append("Set-Cookie: "+cookie.name+"=\""+cookie.value+"\"");
			if(cookie.domain != null) {
				header.append("; Domain="+cookie.domain);
			}
			if(cookie.path != null) {
				header.append("; Path="+cookie.path);
			}
			if(cookie.maxAge != null) {
				header.append("; Max-Age="+cookie.maxAge);
			}
			if(cookie.httpOnly) {
				header.append("; HttpOnly");
			}
			header.append("\r\n");
		}
		
		header.append("\r\n");
		
		return header.toString().getBytes(StandardCharsets.ISO_8859_1);
	}
	
	/**
	 * Checks if header is already generated and written.
	 * If it is, throws {@link RuntimeException}.
	 * If not, nothing is done.
	 * 
	 * @param propertyName		property about to be changed. 
	 */
	private void checkHeader(String propertyName) {
		if(headerGenerated) {
			throw new RuntimeException("Can not change " + 
				propertyName + "since header is already made.");
		}
	}

	/**
	 * Sets encoding for this request.
	 * Can not be changed after header is generated.
	 * 
	 * @param encoding		new encoding.
	 */
	public void setEncoding(String encoding) {
		checkHeader("encoding");
		this.encoding = encoding;
	}
	
	/**
	 * Sets status code for this request.
	 * Can not be changed after header is generated.
	 * 
	 * @param statusCode		new status code.
	 */
	public void setStatusCode(int statusCode) {
		checkHeader("status code");
		this.statusCode = statusCode;
	}
	
	/**
	 * Sets status text for this request.
	 * Can not be changed after header is generated.
	 * 
	 * @param statusText		new status text.
	 */
	public void setStatusText(String statusText) {
		checkHeader("status text");
		this.statusText = statusText;
	}

	/**
	 * Sets content type for this request.
	 * Can not be changed after header is generated.
	 * 
	 * @param mimeType		new content type.
	 */
	public void setMimeType(String mimeType) {
		checkHeader("content type");
		this.mimeType = mimeType;
	}
}
