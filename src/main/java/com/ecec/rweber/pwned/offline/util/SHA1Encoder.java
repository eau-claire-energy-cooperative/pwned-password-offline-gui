package com.ecec.rweber.pwned.offline.util;

import java.security.MessageDigest;

/**
 * @author rweber
 * Encodes strings using SHA-1 encryption
 */
public abstract class SHA1Encoder {

	/**
	 * @param b bytstring to convert
	 * @return the given bytestring as a hexidecimal value
	 */
	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		
		for (int i=0; i < b.length; i++) {
			result +=
	        Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		
		return result.toUpperCase();
	}
	
	/**
	 * @param s the string to encode
	 * @return string encoded with SHA-1
	 */
	public static final String encode(String s){
		MessageDigest encoder = null;
		
		try{
			encoder = MessageDigest.getInstance("SHA-1");
		}
		catch(Exception e)
		{
			//can't go any further, kill the program
			e.printStackTrace();
			System.exit(2);
		}
		
		return byteArrayToHexString(encoder.digest(s.getBytes()));
	}
}
