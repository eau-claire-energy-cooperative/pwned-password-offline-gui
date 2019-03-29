package com.ecec.rweber.pwned.offline;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author rweber
 *
 * Given a string to save, let the use choose where to save it and write the file
 */
public class FileSaver {
	private Component m_parent = null;
	
	public FileSaver(Component parent){
		m_parent = parent;
	}
	
	/**
	 * Select where to save text file using JFileChooser
	 * 
	 * @return filename to save - could be NULL if cancel pressed
	 */
	private String chooseFile(){
		String result = null;
		
		//choose the file
		JFileChooser saveAs = new JFileChooser();
		saveAs.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));
		saveAs.setAcceptAllFileFilterUsed(false);

		int returnVal = saveAs.showSaveDialog(m_parent);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			result = saveAs.getSelectedFile().toString();
			
			if(!result.endsWith(".txt"))
			{
				result = result + ".txt";
			}
		}
		
		return result;
	}
	
	/**
	 * @param contents - the contents of the file as a string (will be split by newline)
	 * @return if save completed without error
	 */
	public boolean save(String contents){
		boolean result = true; 
		
		String filename = this.chooseFile();
		
		//check if cancel pressed - this return true 
		if(filename != null)
		{
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(filename));
				
				//break output into an array
				String[] outputArray = contents.split("\\n");
				
				for(int count = 0; count < outputArray.length; count ++)
				{
					writer.write(outputArray[count]);
					writer.newLine();
				}
				
				writer.flush();
				writer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
		}
		
		return result;
	}
}
