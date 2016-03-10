package com.comp2601.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.comp2601.message.Message;


/**
 * This simple "chat room" server keeps a list of current connections
 * and echos its responses to all currently connected clients.
 */
public class Server {

	//TODO:  Set port
	public final static int DEFAULT_PORT = 3010;
    private static final int SERVERPORT = DEFAULT_PORT; //FOR NOW
    private boolean running = false;
    

    public static void main(String[] args) {

    //Create server and start it. 
     Server server = new Server();
     System.out.println("STARTING SERVER ON PORT: " + SERVERPORT);
     server.run();
    }

    /**
     * Constructor of the class
     * @param messageListener listens for the messages
     */
    public Server() {
    }

    /**
     * Method to send the messages from server to client
     * @param message the message sent by the server
     */
    


    public void run() {

        running = true;
        

        try {
            System.out.println("S: Connecting...");

            ServerSocket serverSocket = new ServerSocket(SERVERPORT);
            while(running){
            
            	
               Socket client = serverSocket.accept();              
               System.out.println("S: Received  Client Connection Request...");
               Connection clientConnection = new Connection(client, new Connection.OnMessageReceived() {
				
				  @Override
				  public void messageReceived(Message message) {
					  System.out.println("\nRECEIVED: " + message);
				  }
			   });
               clientConnection.start();
            } //end while
            

        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }

      
    }
}