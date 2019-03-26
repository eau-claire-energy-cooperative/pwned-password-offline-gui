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
  	
        try {
            String line;
            while (null != (line = m_reader.readLine()))
            {
                m_output.append(line + "\n");
            }
            
            m_reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public String getOutput(){
    	return m_output.toString();
    }
}