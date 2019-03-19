package com.ecec.rweber.pwned.offline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Encode extends JFrame{
	private static final long serialVersionUID = -1355985486330269435L;
	private int HEIGHT = 600;
	private int WIDTH = 1000;
	private JTextArea m_passInput = null;
	private JTextArea m_passOutput = null;
	private MessageDigest m_encoder = null;
	
	public Encode(){
		this.setTitle("Encode Passwords");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{
			m_encoder = MessageDigest.getInstance("SHA-1");
		}
		catch(Exception e)
		{
			//can't go any further, kill the program
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	private void saveFile(){
		JFileChooser saveAs = new JFileChooser();
		saveAs.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));
		saveAs.setAcceptAllFileFilterUsed(false);
		
		int returnVal = saveAs.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String file = saveAs.getSelectedFile().toString();
			
			if(!file.endsWith(".txt"))
			{
				file = file + ".txt";
			}
			
			if(!writeFile(file))
			{
				JOptionPane.showMessageDialog(this, "Saving file failed");
			}
		}
	}
	
	private boolean writeFile(String filename){
		boolean result = true;
	
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			
			//get the hashes
			String[] hashes = m_passOutput.getText().split("\\n");
			
			for(int count = 0; count < hashes.length; count ++)
			{
				writer.write(hashes[count]);
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
	
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem fileSave = new JMenuItem("Save Hashes");
		fileSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
			
		});
		fileMenu.add(fileSave);
		
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
		fileMenu.add(fileExit);
		
		result.add(fileMenu);
		
		return result;
	}
	
	private String byteArrayToHexString(byte[] b) {
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result +=
	          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	  }
	  return result.toUpperCase();
	}
	
	public void run(){
		this.setSize(WIDTH,HEIGHT);
		this.setJMenuBar(this.createMenuBar());
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		m_passInput = new JTextArea();
		JScrollPane scroll1 = new JScrollPane(m_passInput);	
		mainPanel.add(scroll1);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		m_passOutput = new JTextArea();
		m_passOutput.setEditable(false);
		JScrollPane scroll2 = new JScrollPane(m_passOutput);
		mainPanel.add(scroll2);
		
		//create the buttons
		JButton convertButton = new JButton("Encode");
		convertButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] passwords = m_passInput.getText().split("\\n");
				
				//reset the output pane
				m_passOutput.setText("");
				
				for(int count = 0; count < passwords.length; count ++)
				{
					m_passOutput.setText(m_passOutput.getText() + byteArrayToHexString(m_encoder.digest(passwords[count].getBytes())) + "\n");
				}
			}
			
		});
		
		JButton findButton = new JButton("Password Search");
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(convertButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(findButton);
		
		//add both panels to the content area
		Container contentPane = this.getContentPane();
		this.setJMenuBar(this.createMenuBar());
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPane,BorderLayout.PAGE_END);
		
		//open in the middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth()/2 - (WIDTH/2)), (int)(screenSize.getHeight()/2 - (HEIGHT/2)));
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Encode e = new Encode();
		e.run();
	}

}
