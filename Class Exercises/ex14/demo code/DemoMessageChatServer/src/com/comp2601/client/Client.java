package com.comp2601.client;

import java.net.*;
import java.io.*;

import com.comp2601.message.Message;

/* This class represents a connection, via a socket, to a server.
 * 
 * This class is designed to expect a single thread to do sendRequests to the
 * server and another thread to asynchronously read server responses. 
   
   WARNING: this is code for illustration with lots of error checking and protocol
   violation checks missing.
*/

public class Client extends Object {

	public static final int DEFAULT_PORT = 3010;
	
	private int PORT = DEFAULT_PORT;
	private String url;
	String response = "";
	InetAddress  address;
	Socket socket;
	//BufferedReader inStream;
	//PrintWriter outStream;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	
    boolean connected = false;


	public Client() {}
	public boolean isConnected(){return connected;}
	
	public void connect(String hostAddress, int port) {	
		PORT = port;
		try {
			if(hostAddress.equals("localhost") || hostAddress.equals("") )
                              address = InetAddress.getLocalHost();
            else address = InetAddress.getByName(hostAddress);
			
			socket = new Socket(address, PORT);

			System.out.println("SOCKET OBTAINED: " + socket);
			System.out.flush();


			//inStream = new BufferedReader(new InputStreamReader( socket.getInputStream()));
			objectInputStream =  new ObjectInputStream( socket.getInputStream());
			//outStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	        connected = true;
			System.out.println("STREAMS WRAPPED");
			System.out.flush();
			

        }
		catch (Exception exception) {
			System.out.println("CONNECT ERROR: Failed to connect to server " + address + ":" + PORT);
			System.out.println(exception);

		
	    }	

	}
    public void disconnect() {
    	   
    	   if(!connected) return;
       
    	   try {
    		  Message disconnectMessage = new Message();
    		  disconnectMessage.header.type = Message.DISCONNECT;
    		  disconnectMessage.header.sender = Message.DEFAULT_SENDER;
    		  disconnectMessage.header.receiver = Message.DEFAULT_RECEIVER;
    		  
 		  objectOutputStream.writeObject(disconnectMessage);
		  objectOutputStream.flush();
          objectOutputStream.close();
          objectInputStream.close();
          socket.close();
          response = "";
          connected = false;
       }
	   catch (Exception exception) {
		  System.out.println(exception);
	   }

    }
	

	
	public synchronized Message readMessage() {
		Message m = null;
        try {
		   m = (Message) objectInputStream.readObject();
        }
		catch (Exception exception) {
			System.out.println(exception);}


        return m;
        }
	
	/*EXERCISE:
	 * If you synchronize the following method things will deadlock.
	 * Can you explain why?
	 */

	public void submitRequest(Message message) {
		
        try {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
			System.out.println("Submitting Message Object to Server");
        }
		catch (Exception exception) {
			System.out.println("ERROR: Unable to submit message object to server");
			System.out.println(exception);}

        }
}
		
