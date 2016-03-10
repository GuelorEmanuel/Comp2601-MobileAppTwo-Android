package com.comp2601.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


/**
 * The class extends the Thread class so we can receive and send messages at the same time
 */
public class Server extends Thread {

	//TODO:  Set port
    private static final int SERVERPORT = 3010; //FOR NOW
    private boolean running = false;
    private PrintWriter mOut;
    //private OnMessageReceived messageListener;
    private Random mRandom;
    ArrayList<Connection> connections;


  

    public static void main(String[] args) throws IOException {
    	
    	System.out.println("S: Connecting...");
    	
    	//TODO: Create serversocket
    	//create a server socket. A server socket waits for requests to come in over the network.
    	ServerSocket serverSocket = new ServerSocket(SERVERPORT);
    	Boolean running = true;
    	ArrayList<Connection> connections = new ArrayList<>();
    	
    	
    	//TODO: Create a loop to read a line from the input 
    	//and the send a reply back to client using the sendMessage method
    	while (running) {
    		Socket client = serverSocket.accept();
    		Connection clientConnection = new Connection(client, new Connection.OnMessageReceived() {
    			@Override
    			public void messageReceived(String message) {
    				System.out.println("\n" + message);
    			}
    		}, connections);
    		
    		connections.add(clientConnection);
    		clientConnection.start();
        }

    }
}