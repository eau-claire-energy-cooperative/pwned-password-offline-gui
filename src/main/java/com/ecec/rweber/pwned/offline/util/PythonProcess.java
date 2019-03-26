package com.ecec.rweber.pwned.offline.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

public class PythonProcess extends Observable {
	private String[] m_hashes = null;
	
	public PythonProcess(String[] hashes){
		m_hashes = hashes;
	}
	
	public String run(){
		String result = "";
		
		//build the command
		List<String> command = new ArrayList<String>();
		command.add("python");
		command.add("binary_search.py");
		command.add("--skip-not-found");  //don't print passwords we didn't find
		
		//add all the hashed passwords
		command.addAll(Arrays.asList(m_hashes));
		
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("python")); //set to location of the binary_search.py file
		
		try{
			Process p = builder.start();
			
			ProcessOutputStreamPrinter printer = new ProcessOutputStreamPrinter("binary_search",p.getInputStream());
			printer.start();
			
			ProcessOutputStreamPrinter error = new ProcessOutputStreamPrinter("error",p.getErrorStream());
			error.start();
			
			p.waitFor();
			
			result = error.getOutput();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
		
	}
}
