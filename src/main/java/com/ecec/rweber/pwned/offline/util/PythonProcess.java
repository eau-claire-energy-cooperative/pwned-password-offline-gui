package com.ecec.rweber.pwned.offline.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PythonProcess {
	private File m_hashes = null;
	
	public PythonProcess(String hashFile){
		m_hashes = new File(hashFile);
	}
	
	public void run(){
		
		//build the command
		List<String> command = new ArrayList<String>();
		command.add("python");
		command.add("binary_search.py");
		command.add(m_hashes.getAbsolutePath());
		command.add("--skip-not-found");  //don't print passwords we didn't find
		
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("python")); //set to location of the binary_search.py file
		
		try{
			Process p = builder.start();
			
			ProcessOutputStreamPrinter printer = new ProcessOutputStreamPrinter("binary_search",p.getInputStream());
			printer.start();
			
			p.waitFor();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
