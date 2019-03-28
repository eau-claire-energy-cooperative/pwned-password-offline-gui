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
import java.util.Observable;
import java.util.Observer;

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

import com.ecec.rweber.pwned.offline.util.PythonProcess;
import com.ecec.rweber.pwned.offline.util.SHA1Encoder;

public class Encode extends JFrame {
	private static final long serialVersionUID = -1355985486330269435L;
	private int HEIGHT = 600;
	private int WIDTH = 1000;
	private JTextArea m_passInput = null;
	private JTextArea m_passOutput = null;
	
	public Encode(){
		this.setTitle("Pwned Password GUI");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private String[] getHashes(){
		String[] passwords = m_passInput.getText().split("\\n");
		
		for(int count = 0; count < passwords.length; count ++)
		{
			passwords[count] = SHA1Encoder.encode(passwords[count]);
		}
		
		return passwords;
	}
	
	private String chooseFile(){
		String result = null;
		
		//choose the file
		JFileChooser saveAs = new JFileChooser();
		saveAs.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));
		saveAs.setAcceptAllFileFilterUsed(false);
		
		int returnVal = saveAs.showSaveDialog(this);
		
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
	
	private boolean writeFile(String filename, String output){
		boolean result = true;
	
		if(filename != null)
		{
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(filename));
				
				//break output into an array
				String[] outputArray = output.split("\\n");
				
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
	
	private void startSearch(){
		PythonProcess p = new PythonProcess(getHashes());
		
		m_passOutput.append(p.run());
		
		m_passOutput.append("\nSearch Complete");
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
	
		//create file menu
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem fileSave = new JMenuItem("Save Hashes");
		fileSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String filename = chooseFile();
				
				//create a string just of the hashes
				String hashes = "";
				
				String[] passwords = getHashes();
				for(int count = 0; count < passwords.length; count ++)
				{
					hashes = hashes + passwords[count] + "\n";
				}
				
				if(!writeFile(filename,hashes))
				{
					JOptionPane.showMessageDialog(null, "Saving file failed");
				}
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
		
		//create help menu
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem helpUsage = new JMenuItem("Instructions");
		helpUsage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Type your passwords in the top text box. Click Start Search to see which passwords are found in the Pwned database file. ");
			}
			
		});
		helpMenu.add(helpUsage);
		
		JMenuItem helpAbout = new JMenuItem("About");
		helpAbout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pwned Password GUI v0.1.1\n Written By: Rob Weber \n Python Search File modifed from: https://github.com/pinae/HaveIBeenPwnedOffline \n Source: https://github.com/eau-claire-energy-cooperative/pwned-password-offline-gui");
			}
			
		});
		
		helpMenu.add(helpAbout);
		
		result.add(fileMenu);
		result.add(helpMenu);
		
		return result;
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
		JButton searchButton = new JButton("Start Search");
		searchButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] hashes = getHashes();
				
				//reset the output pane
				m_passOutput.setText("Converting passwords to SHA-1:\n\n");
				
				for(int count = 0; count < hashes.length; count ++)
				{
					m_passOutput.append(hashes[count] + "\n");
				}
				
				m_passOutput.append("\nStarting Search.....\n\n");
				
				startSearch();
				
			}
			
		});
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(searchButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
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
