package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This is a program that allows the user to 
 * encrypt/decrypt given file using the AES cryptoalgorithm and the 128-bit encryption key 
 * or calculate and check the SHA-256 file digest. 
 * <p>
 * Program needs arguments.
 * First argument must be a command name, while second and others are command's arguments.
 * Supported commands are:  
 * 		checksha [file]
 * 		encrypt [fromFile] [toFile]
 * 		decrypt [fromFile] [toFile]  
 * </p>
 * 
 * @author Ivica Brebrek
 * @version 1.0  (24.4.2016.)
 */
public class Crypto {

	/**
	 * Entry point.
	 * 
	 * @param args [command_name, arguments...]
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Program needs arguments.");
			return;
		}

		try{
			CryptoCommands.execute(args);
		} catch(Exception ex) {
			System.err.println("Incorrect arguments.");
		}
	}
	
	/**
	 * Converts hexidecimal written in string to array of bytes. 
	 * 
	 * @param hex		string representing hexadecimal number.
	 * @return array of bytes.
	 */
	public static byte[] hextobyte(String hex) {  //yea, hexToByte would be better name, but this was method name given in hw
	    int len = hex.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                             + Character.digit(hex.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * Converts array of bytes to hexidecimal written in string. 
	 * 
	 * @param bytes		array of bytes.
	 * @return string representing hexadecimal number.
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789abcdef".toCharArray();
		
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	/**
	 * Class with all commands supported by {@code Crypto}.
	 * 
	 * To run command simply call method CryptoCommands.execute.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (24.4.2016.)
	 */
	private static class CryptoCommands {
		/** Name of command that checks file digest. */
		private static final String CHECKER = "checksha";
		/** Number of arguments needed for that command. */
		private static final int CHECKER_ARGUMENTS = 1;
		
		/** Name of command that encrypts one file into another. */
		private static final String ENCRYPTER = "encrypt";
		/** Number of arguments needed for that command. */
		private static final int ENCRYPTER_ARGUMENTS = 2;
		
		/** Name of command that decrypts one file into another. */
		private static final String DECRYPTER = "decrypt";
		/** Number of arguments needed for that command. */
		private static final int DECRYPTER_ARGUMENTS = 2;

		/**
		 * Checks if given command is supported, if not writes that to system output.
		 * If given command is supported this method delegates work to the right method.
		 * 
		 * @param args 		first in array must be command name, others are command arguments.
		 * 
		 * @throws IOException	if there is problem with given files (commands arguments).
		 * @throws GeneralSecurityException	if files are OK but encrypting/decrypting/digesting is not possible.
		 */
		public static void execute(String[] args) throws IOException, GeneralSecurityException {
			switch (args[0].toLowerCase()) {
			
				case CHECKER:
					if(invalidArguments(CHECKER, CHECKER_ARGUMENTS, args.length-1)) break;
					check(args[1]);
					break;

				case ENCRYPTER:
					if(invalidArguments(ENCRYPTER, ENCRYPTER_ARGUMENTS, args.length-1)) break;
					crypting(args[1], args[2], true);
					break;

				case DECRYPTER:
					if(invalidArguments(DECRYPTER, DECRYPTER_ARGUMENTS, args.length-1)) break;
					crypting(args[1], args[2], false);
					break;
					
				default:
					System.err.println(args[0] + " is unsupported command.");
					break;
			}
		}
		
		/**
		 * Main body of COMMANDS encrypt and decrypt.
		 * 
		 * @param firstFile		read from this.
		 * @param secondFile	write to this.
		 * @param encrypt		{@code true} if command is encrypt, {@code false} if command is decrypt.
		 * 
		 * @throws IOException	if there is problem with given files.
		 * @throws GeneralSecurityException	if files are OK but encrypting/decrypting is not possible.
		 */
		private static void crypting(String firstFile, String secondFile, boolean encrypt) throws GeneralSecurityException, IOException {
			try(InputStream inputFile = new BufferedInputStream(new FileInputStream(firstFile));
				OutputStream outputFile = new BufferedOutputStream(new FileOutputStream(secondFile));
				BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
				
				System.out.printf("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
				String key = consoleReader.readLine().trim();
				
				System.out.printf("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
				String vector = consoleReader.readLine().trim();
				
				SecretKeySpec keySpec = new SecretKeySpec(hextobyte(key), "AES");
				AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(vector));
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
				
				byte[] buffer = new byte[4000];
				int x;
				
				while ((x = inputFile.read(buffer)) > 0) {
					outputFile.write(cipher.update(buffer, 0, x));
				}
				
				outputFile.write(cipher.doFinal());
				
				System.out.printf("%s completed. ", encrypt ? "Encryption" : "Decryption");
				System.out.printf("Generated file %s based on file %s.%n", secondFile, firstFile);
			}		
		}
		
		/**
		 * Main body of COMMAND checksha.
		 * 		
		 * @param fileName	file being digested.
		 * 
		 * @throws IOException	if there is problem with {@code fileName}.
		 * @throws GeneralSecurityException	if file is OK but it's not possible to digest it.
		 */
		private static void check(String fileName) throws IOException, GeneralSecurityException {
			try(InputStream inputFile = new BufferedInputStream(new FileInputStream(fileName));
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
				
				System.out.printf("Please provide expected sha-256 digest for %s:%n> ", fileName);
				String expected = reader.readLine().trim();
				String actual = digest(inputFile);
			
				System.out.print("Digesting completed. ");
				if(actual.equals(expected)) {
					System.out.printf("Digest of %s matches expected digest.%n", fileName);
				} else {
					System.out.printf("Digest of %s does not match the expected digest. Digest was: %s", fileName, actual);
				}
			}
		}
		
		/**
		 * Digest {@code input} using SHA-256 into hexadecimal string.
		 * 
		 * @param input		input stream.
		 * @return hexadecimal number in string.
		 * 
		 * @throws IOException	if there is a problem with given stream.
		 * @throws GeneralSecurityException if stream is correct but it's not possible to digest it.
		 */
		private static String digest(InputStream input) throws IOException, GeneralSecurityException {
			MessageDigest digester = MessageDigest.getInstance("SHA-256");
			
			byte[] buffer = new byte[4000];
			int x;
			
			while ((x = input.read(buffer)) > 0) {
				digester.update(buffer, 0, x);
			}

			return bytesToHex(digester.digest());
		}

		/**
		 * Checks if number of arguments is INVALID.
		 * Prints to system output if number of arguments is incorrect.
		 * 
		 * @param commandName	command using those arguments.
		 * @param expected		number of needed arguments.
		 * @param actual		number of given arguments.
		 * @return {@code true} if number of arguments is INVALID.
		 */
		private static boolean invalidArguments(String commandName, int expected, int actual) {
			if(actual != expected) {
				System.out.println("Wrong number of arguments for command " + commandName);
				return true;
			}
			return false;
		}
	}
}
