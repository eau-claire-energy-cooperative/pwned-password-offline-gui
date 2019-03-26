package com.ecec.rweber.pwned.offline.util;

import java.security.MessageDigest;

public abstract class SHA1Encoder {

	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		
		for (int i=0; i < b.length; i++) {
			result +=
	        Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		
		return result.toUpperCase();
	}
	
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
