package hr.fer.zemris.java.hw15.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Class used to obtain hex-encode of 
 * SHA-1 hash obtained from given string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (20.6.2016.)
 */
public final class Hasher {
	
	/**
	 * From given string calculates SHA-1 hash.
	 * Hash is displayed as hex value.
	 * Given string is encoded into bytes using UTF-8.
	 * Returns empty string if given string is null.
	 * 
	 * @param string		any string.
	 * @return hex-encoded SHA-1 hash value obtained by given string.
	 */
	public static String encrypt(String string) {
		if(string == null) return "";
	    String sha1 = "";
	    try {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(string.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
	        throw new RuntimeException("Unable to hash password.");
	    }
	    return sha1;
	}

	/**
	 * Hex-encodes given bytes.
	 * 
	 * @param bytes		any bytes.
	 * @return hex-encoded bytes.
	 */
	public static String byteToHex(byte[] bytes) {
	    Formatter formatter = new Formatter();
	    for (byte b : bytes) {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
