package com.ecec.rweber.pwned.offline.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessOutputStreamPrinter extends Thread {
	private String m_name = null;
    private BufferedReader m_reader;
    private StringBuilder m_output = null;
    
    public ProcessOutputStreamPrinter(String name, InputStream stream) {
    	m_name = name;
        m_reader = new BufferedReader(new InputStreamReader(stream));
        m_output = new StringBuilder();
    }
    
    public void run() {
    	System.out.println(m_name + ": gathering output");
    	
        try {
            String line;
            while (null != (line = m_reader.readLine()))
            {
            	System.out.println(m_name + ": " + line);
            	
                m_output.append(line);
            }
            
            m_reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(m_name + ": completed");
    }
    
    public String getOutput(){
    	return m_output.toString();
    }
}