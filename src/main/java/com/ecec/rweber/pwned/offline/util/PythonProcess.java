package com.ecec.rweber.pwned.offline.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * @author rweber
 *
 * Launches a python process to do the actual search through the pwned password database. Input stream readers capture the output
 */
public class PythonProcess extends Observable {
	private String[] m_hashes = null;
	private String m_filename = "pwned-passwords-sha1-ordered-by-hash-v4.txt";
	private boolean m_skipNotFound = true;
	
	/**
	 * Create the process assuming no found entries will be excluded from output
	 * @param hashes array of hashes to look for
	 */
	public PythonProcess(String[] hashes){
		this(hashes,true);
	}
	
	/**
	 * @param hashes array of hashes to look for
	 * @param skipNotFound if not found entries should be skipped or displayed in the output
	 */
	public PythonProcess(String[] hashes, boolean skipNotFound){
		m_hashes = hashes;
		this.m_skipNotFound = skipNotFound;
		
		this.findHashFile();
	}
	
	private void findHashFile() {
		
		//check if the python directory is using the default file or a different one
		File pythonDir = new File("python");
		
		File[] txtFiles = pythonDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				//filter out txt files
				boolean result = false;
				
				if(pathname.isFile() && pathname.getName().endsWith(".txt")) {
					result = true;
				}
				
				return result;
			}
			
		});
		
		//the hash file should be the only one in the directory
		if(txtFiles.length == 1) {
			this.m_filename = txtFiles[0].getName();
		}
	}
	
	/**
	 * Launch the python script and wait for it to complete, returning results as a String
	 * @return results as captured from the python process output
	 */
	public String run(){
		String result = "";
		
		//build the command
		List<String> command = new ArrayList<String>();
		command.add("python");
		command.add("binary_search.py");
		command.add("--pwned-passwords-ordered-by-hash-filename");
		command.add(this.m_filename);

		if(m_skipNotFound)
		{
			command.add("--skip-not-found");  //don't print passwords we didn't find
		}
		
		//add all the hashed passwords
		command.addAll(Arrays.asList(m_hashes));

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("python")); //set to location of the binary_search.py file
		
		try{
			Process p = builder.start();

			//create process monitor to capture output
			ProcessOutputStreamPrinter printer = new ProcessOutputStreamPrinter(p.getErrorStream());
			printer.start();
			
			p.waitFor();
			
			result = printer.getOutput();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
		
	}
}
