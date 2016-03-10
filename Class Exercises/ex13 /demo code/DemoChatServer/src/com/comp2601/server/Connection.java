package com.comp2601.server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Connection extends Thread {
	private Thread t;
	private String threadName;
	private Socket client;
	private PrintWriter mOut;
	private OnMessageReceived messageListener;
	private Random mRandom;
	private ArrayList<Connection> connections;
	
	Connection(Socket argClient,OnMessageReceived messageListener, ArrayList<Connection> c){ 
		this.client = argClient;
		this.messageListener = messageListener;
		this.connections = c;
	}
	
	public void sendMessage(String message){
		if (mOut != null && !mOut.checkError()) {
			mOut.println(message);
			mOut.flush();
		}
	       
	}
	
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
	
	@Override
	public void run  () {
		Boolean running = true;
		mRandom = new Random();
		
		try {
			mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			while (running) {
				String message = in.readLine();
				
				if (message != null && messageListener != null) {
					//call the method messageReceived from ServerBoard class
					if (!message.equalsIgnoreCase("Disconnect")){
						messageListener.messageReceived(message);
						String x = Common.mReplies.get(mRandom.nextInt(5));
						sendMessage(x);
						for(Connection c : connections)
					        {
					        	 if(c != this) {
					        		c.sendMessage(message);
					        		c.sendMessage(x);
					        	 }
					       
					        
					        }
					   } else {
						   System.out.println("Disconnecting");
						   running = false;
					   }
				   }
				   message = null;
			   }
			   
			   
		    }
	   		catch (Exception e) {
	   			System.out.println("S: Error");
	   			e.printStackTrace();
	   		} finally {
	   			sendMessage("Disconnected");
	   			try{client.close();}
	   			catch (Exception e){}
	   			System.out.println("S: Done.");
	   		}
		   

	   }
	
}
