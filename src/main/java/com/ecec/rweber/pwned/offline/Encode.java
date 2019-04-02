package com.ecec.rweber.pwned.offline;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.ecec.rweber.pwned.offline.util.PythonProcess;
import com.ecec.rweber.pwned.offline.util.SHA1Encoder;

/**
 * @author rweber
 * 
 * Launches the main GUI for the entire program. Enter passwords to be hashed and searched via python against the PwnedPassword database. 
 *
 */
public class Encode extends JFrame {
	private static final long serialVersionUID = -1355985486330269435L;
	private String VERSION = "0.1.4";
	private int HEIGHT = 600;
	private int WIDTH = 1000;
	
	private JTextArea m_passInput = null;
	private JTextArea m_passOutput = null;
	private JCheckBoxMenuItem m_showNotFound = null;
	private FileSaver m_saver = null;
	
	public Encode(){
		this.setTitle("Pwned Password GUI v" + VERSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		m_saver = new FileSaver(this);
	}
	
	/**
	 * 
	 * Get passwords (one per line) from the text input area and hash with sha-1
	 * 
	 * @return array of hashed passwords
	 */
	private String[] getHashes(){
		String[] passwords = m_passInput.getText().split("\\n");
		
		for(int count = 0; count < passwords.length; count ++)
		{
			passwords[count] = SHA1Encoder.encode(passwords[count]);
		}
		
		return passwords;
	}
	
	/**
	 * Start the search process by passing hashes to python process launcher. This will block until completed. 
	 */
	private void startSearch(){
		//inverse showNot found as arg is "skipNotfound"
		PythonProcess p = new PythonProcess(getHashes(),!m_showNotFound.isSelected());
		
		m_passOutput.append(p.run());
		
		m_passOutput.append("\nSearch Complete");
	}
	
	/**
	 * creates the top menu bar
	 * 
	 * @return menu bar to attach to GUI
	 */
	private JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
	
		//create FILE menu
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem fileSave = new JMenuItem("Save Hashes");
		fileSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//create a string just of the hashes
				String hashes = "";
				
				String[] passwords = getHashes();
				for(int count = 0; count < passwords.length; count ++)
				{
					hashes = hashes + passwords[count] + "\n";
				}
				
				//attempt to save hashes in a file
				if(!m_saver.save(hashes))
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
		
		//create OPTIONS menu
		JMenu opsMenu = new JMenu("Options");
		
		//checkbox to toggle if no found hashes are shown in the display
		m_showNotFound = new JCheckBoxMenuItem("Show Not Found");
		m_showNotFound.setSelected(true);
		opsMenu.add(m_showNotFound);
		
		//create HELP menu
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
				JOptionPane.showMessageDialog(null, "Pwned Password GUI v" + VERSION + "\n Written By: Rob Weber \n Python Search File modifed from: https://github.com/pinae/HaveIBeenPwnedOffline \n Source: https://github.com/eau-claire-energy-cooperative/pwned-password-offline-gui");
			}
			
		});
		
		helpMenu.add(helpAbout);
		
		//add all drop downs to main menu
		result.add(fileMenu);
		result.add(opsMenu);
		result.add(helpMenu);
		
		return result;
	}
	
	
	/**
	 *  Initialize and launch the GUI
	 */
	public void run(){
		this.setSize(WIDTH,HEIGHT);
		this.setJMenuBar(this.createMenuBar());
		
		//main panel to be added to JFrame
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		//initialize text input area
		m_passInput = new JTextArea();
		JScrollPane scroll1 = new JScrollPane(m_passInput);	
		mainPanel.add(scroll1);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		//add separation between text areas
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		//initialize text output area
		m_passOutput = new JTextArea();
		m_passOutput.setEditable(false);
		JScrollPane scroll2 = new JScrollPane(m_passOutput);
		mainPanel.add(scroll2);
		
		//create a reset button
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m_passInput.setText("");
				m_passOutput.setText("");
			}
			
		});
		
		//create the search button
		JButton searchButton = new JButton("Start Search");
		searchButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//convert any entered passwords to SHA-1
				String[] hashes = getHashes();
				
				//reset the output pane
				m_passOutput.setText("Converting passwords to SHA-1:\n\n");
				
				for(int count = 0; count < hashes.length; count ++)
				{
					m_passOutput.append(hashes[count] + "\n");
				}
				
				m_passOutput.append("\nStarting Search.....\n\n");
				
				//launch search process
				startSearch();
				
			}
			
		});
		
		//create panel for button area
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(searchButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(resetButton);
		
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
