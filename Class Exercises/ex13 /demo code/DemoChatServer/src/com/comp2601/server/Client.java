package com.comp2601.server;

import java.net.*;
import java.io.*;

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
	BufferedReader inStream;
	PrintWriter outStream;
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

			inStream = new BufferedReader(new InputStreamReader( socket.getInputStream()));
			outStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	        connected = true;
			

        }
		catch (Exception exception) {
			System.out.println("CONNECT ERROR: Failed to connect to server " + address + ":" + PORT);
			System.out.println(exception);

		
	    }	

	}
    public void disconnect() {
    	if(!connected) return;
    	
    	try {
    		outStream.println("disconnect");
    		outStream.flush();
    		outStream.close();
    		inStream.close();
    		socket.close();
    		response = "";
    		connected = false;
    	}
    	catch (Exception exception) {
    		System.out.println(exception);
	   }
    }
	
	public synchronized String readRequest() {
		String s = null;
        try {
		   s = inStream.readLine();
        }
		catch (Exception exception) {
			System.out.println(exception);}


        return s;
        }
	
	/*EXERCISE:
	 * If you synchronize the following method things will deadlock.
	 * Can you explain why?
	 */
	public void submitRequest(String url) {
		
        try {
			outStream.println(url);
			outStream.flush();
			System.out.println("Waiting response from server");
                    }
		catch (Exception exception) {
			System.out.println("ERROR: Unable to submit request to server");
			System.out.println(exception);}

        }
}