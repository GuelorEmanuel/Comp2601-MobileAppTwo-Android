package com.comp2601.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.awt.event.*; 
import java.awt.*;

import javax.swing.*; 
import javax.swing.event.*; 

import com.comp2601.message.Message;

public class  ClientGUI extends JFrame { 

    private JTextField 	URLTextField; 
    private JButton   	submitButton; 
    private JTextArea   webPage; 
    private String      urlString = "";
    private String 	webPageText = "";
    private JFrame browser;
 
    //client object representing the connection to server
    private Client   webClient = new Client();
    private ServerListener serverListener; //thread object that listens for server messages
    private boolean running = true; //true when connected to server

    // Create a menu bars and its pull-down menus 
    JMenuBar menuBar = new JMenuBar(); 
    JMenu  connectMenu = new JMenu("Connection"); 

    public ClientGUI(String name) { 
         super(name); 
         
         browser = this; //remember this for action listener to access
         
         //build menu items and listeners and add them to menus

         JMenuItem menuItem;
         menuItem = new JMenuItem("Connect");
         menuItem.setMnemonic('C');
         menuItem.addActionListener(
            new ActionListener() { 
               public void actionPerformed(ActionEvent event){ 
                 //ask for ip address, default is the loop back address 127.0.0.1
                 String hostAddressAndPort = 
                   JOptionPane.showInputDialog(browser, "Connect To:", "127.0.0.1" + ":" + Client.DEFAULT_PORT); 
                 
                 //NO ERROR CHECK FOR BAD USER INPUT HERE
                 String[] ipAddressAndPort = hostAddressAndPort.split(":");
                 String hostIpAddress = ipAddressAndPort[0];
                 int port = Integer.parseInt(ipAddressAndPort[1]);
                 webClient.connect(hostIpAddress, port);
                 running = true;
                 System.out.println("CONNECTED TO SERVER");
                 System.out.flush();
                 appendText("CONNECTED TO " + hostAddressAndPort);
                 serverListener = new ServerListener();
                 serverListener.start(); //listen for input from server
                 update(); //update GUI
                 } 
             } 
         );
         connectMenu.add(menuItem);

         menuItem = new JMenuItem("Disconnect");
         menuItem.setMnemonic('D');
         menuItem.addActionListener(
            new ActionListener() { 
               public void actionPerformed(ActionEvent event){ 
            	      running = false; //will cause server listener to end;
            	      appendText("DISCONNECTING FROM SERVER");
            	      webClient.disconnect();
                  update();  //udate GUI
                  } 
              } 
         );
         connectMenu.add(menuItem);

         menuItem = new JMenuItem("Clear Text Area");
          menuItem.addActionListener(
            new ActionListener() { 
               public void actionPerformed(ActionEvent event){ 
                  clearText();
                  update(); 
                  } 
              } 
         );
         connectMenu.add(menuItem);



        // add menus to the menu bar 
        menuBar.add(connectMenu); 

        // add menu bar to window and popup menu to the window pane 
        setJMenuBar(menuBar); 

        GridBagLayout layout = new GridBagLayout(); 
        GridBagConstraints layoutConstraints = new GridBagConstraints(); 

        getContentPane().setLayout(layout); 

        URLTextField = new JTextField(); 
        layoutConstraints.gridx = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridy = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridwidth = 1; 
        layoutConstraints.gridheight = 1; 
        layoutConstraints.fill = GridBagConstraints.BOTH; 
        layoutConstraints.insets = new Insets(5, 5, 5, 5); 
        layoutConstraints.weightx = 1.0; 
        layoutConstraints.weighty = 0.0; 
        layout.setConstraints(URLTextField, layoutConstraints); 
        getContentPane().add(URLTextField);
        URLTextField.addActionListener(new SubmitButtonListener());

        submitButton = new JButton("Submit");  
        layoutConstraints.gridx = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridy = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridwidth = GridBagConstraints.REMAINDER; 
        layoutConstraints.gridheight = 1;  
        layoutConstraints.fill = GridBagConstraints.BOTH; 
        layoutConstraints.insets = new Insets(5, 5, 5, 5); 
        layoutConstraints.weightx = 0.0; 
        layoutConstraints.weighty = 0.0; 
        layout.setConstraints(submitButton, layoutConstraints); 
        getContentPane().add(submitButton); 
	    submitButton.addActionListener(new SubmitButtonListener()); 

        webPage = new JTextArea(15,20); 
        webPage.setText(webPageText);
	    webPage.setWrapStyleWord(true);
	    webPage.setLineWrap(true);
        layoutConstraints.gridx = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridy = GridBagConstraints.RELATIVE; 
        layoutConstraints.gridwidth = 2; 
        layoutConstraints.gridheight = 5; 
        layoutConstraints.fill = GridBagConstraints.BOTH; 
        layoutConstraints.insets = new Insets(5, 5, 5, 5); 
        layoutConstraints.anchor = GridBagConstraints.CENTER; 
        layoutConstraints.weightx = 1.0; 
        layoutConstraints.weighty = 1.0; 
        layout.setConstraints(webPage, layoutConstraints); 
        getContentPane().add(webPage); 
     } 

     private synchronized void update() {
        URLTextField.setText(urlString);
		webPage.setText(webPageText);
		webPage.repaint();
     }
     
     private synchronized void appendText(String text){
  	   webPageText += "\n" + text;
   }
     private synchronized void clearText(){
  	   webPageText = "";
   }

    //action listener for the submit button
    class SubmitButtonListener implements ActionListener { 
        public void actionPerformed(ActionEvent theEvent) {
            urlString = URLTextField.getText();
            Message message = new Message();
            message.header.sender = "Lou";
            message.header.receiver = "John";
            message.header.type = Message.DATA;
            message.body.addField(Message.DATA, URLTextField.getText().trim());
            
            if(webClient.isConnected()){
              appendText("Client: \n" + message);
              webClient.submitRequest(message); 
            }
            else{
                appendText("ERROR: Not Connected To Server");                          	
            }
            urlString = ""; //clear URL string            
            update(); 
        } 
    }
    
    class ServerListener extends Thread {
    	   public ServerListener(){}
    	   public void run(){
    		   while(running){
    			  
 			Message response = webClient.readMessage();
			appendText("Server: " + response);
			update();
    			    			    			   
    		   }
    		   
    	   }
    }
    
    

    public static void main(String[] args) { 
         WindowListener listener;
	     ClientGUI frame = new ClientGUI("Client"); 

	     listener = new WindowAdapter() { 
             public void windowClosing(WindowEvent theEvent) { 
                   System.exit(0); 
              } 
         }; 	
         frame.addWindowListener(listener); 
         frame.pack(); //set window size to wrap components 
         frame.setVisible(true); //make GUI window visible
    } 



} 
