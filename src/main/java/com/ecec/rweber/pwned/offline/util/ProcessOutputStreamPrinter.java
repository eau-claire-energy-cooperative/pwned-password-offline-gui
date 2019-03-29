package com.ecec.rweber.pwned.offline.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author rweber
 *
 * Stream reader to take output from a running process so it can be retrieved as a string 
 */
public class ProcessOutputStreamPrinter extends Thread {
    private BufferedReader m_reader;
    private StringBuilder m_output = null;
    
    /**
     * @param stream the input stream to watch for text
     */
    public ProcessOutputStreamPrinter(InputStream stream) {
        m_reader = new BufferedReader(new InputStreamReader(stream));
        m_output = new StringBuilder();
    }
    
    public void run() {
  	
        try {
            String line;
            
            //watch for new lines until the process closes
            while (null != (line = m_reader.readLine()))
            {
                m_output.append(line + "\n");
            }
            
            m_reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * @return output from the stream
     */
    public String getOutput(){
    	return m_output.toString();
    }
}