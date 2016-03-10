package edu.carleton.COMP2601.assignment2;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import edu.carleton.COMP2601.assignment2.communication.Event;
import edu.carleton.COMP2601.assignment2.communication.EventHandler;
import edu.carleton.COMP2601.assignment2.communication.EventSource;
import edu.carleton.COMP2601.assignment2.communication.JSONEvent;
import edu.carleton.COMP2601.assignment2.communication.JSONEventSource;
import edu.carleton.COMP2601.assignment2.communication.NoEventHandler;
import edu.carleton.COMP2601.assignment2.communication.Reactor;
import edu.carleton.COMP2601.assignment2.communication.ThreadWithReactor;

/*
*  Name: Sean Benjamin
*  
*  Server runs a reactor server pattern. Accepts messages from clients and converts them to JSON object events
*  The reactor server is based on the code from edu.carleton.COMP2601.assignment2.communication.  
*  That was mostly given to use by the professor
*/

public class Server {

	public static int PORT = 2001;
	public Reactor srvReactor;
	public ConcurrentHashMap<String, ThreadWithReactor> connectedClients;
	private ServerSocket srvSocket;

	public void init() {
		srvReactor = new Reactor();
		srvReactor.register("CONNECT_REQUEST", new ConnectRequestHandler());
		srvReactor.register("DISCONNECT_REQUEST", new DisconnetRequestHandler());
		srvReactor.register("PLAY_GAME_REQUEST", new PlayGameRequestHandler());
		srvReactor.register("PLAY_GAME_RESPONSE", new PlayGameResponseHandler());
		srvReactor.register("GAME_ON",  new GameOnHandler());
		srvReactor.register("MOVE_MESSAGE", new MoveMessageHandler());
		srvReactor.register("GAME_OVER", new GameOverHandler());
		
		//initialize list of current connected clients
		connectedClients = new ConcurrentHashMap<String, ThreadWithReactor>();
	}

	protected void add(String client, ThreadWithReactor twr) {
		connectedClients.put(client, twr);
	}

	protected void remove(String client) {
		connectedClients.remove(client);
	}
	
	protected ThreadWithReactor get(String client) {
		return connectedClients.get(client);
	}
	
	//send USER UPDATE
	protected void broadcast() throws JSONException, IOException {
		JSONObject broadcastMSG = new JSONObject();
		broadcastMSG.put("type", "USERS_UPDATED");
		broadcastMSG.put("users", connectedClients.keySet());
		for (String user : connectedClients.keySet()) {
			EventSource es = ((ThreadWithReactor)connectedClients.get(user)).getEventSource();
			((JSONEventSource)es).write(broadcastMSG);
		}
	}

	//connect to port
	void run() throws IOException, ClassNotFoundException, NoEventHandler,
			JSONException {
		srvSocket = new ServerSocket(PORT);

		if (srvSocket != null)
			System.out.println("Server Started!");

		while (true) {
			Socket skt = srvSocket.accept();
			JSONEventSource JSONserverImp = new JSONEventSource(skt);
			ThreadWithReactor twr = new ThreadWithReactor(JSONserverImp,
					srvReactor);
			twr.start();
		}
	}

	public class ConnectRequestHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException {
			try {
				JSONEvent myJSONEvent = (JSONEvent)event;
				
				//adding all the users to clients Hashmap
				add((String)myJSONEvent.get("source"), (ThreadWithReactor)Thread.currentThread());
				
				//Creating the Connected response message to be sent back to server and respond back to sending client
				JSONObject srvJSONResponse = new JSONObject();
				srvJSONResponse.put("type", "CONNECTED_RESPONSE");
				myJSONEvent.getES().write(srvJSONResponse);
				
				//Let all the users know that a new user has joined.  Sends a USER_UPDATE message to client
				broadcast();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//disconnect request
	public class DisconnetRequestHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			ThreadWithReactor twr = get(myJSONEvent.get("source"));
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","DISCONNECT_RESPONSE");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			twr.quit();
			remove(myJSONEvent.get("source"));
			broadcast();
			
		}
	}
	

	//play game request
	public class PlayGameRequestHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","PLAY_GAME_REQUEST");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			srvJSONResponse.put("destination", myJSONEvent.get("destination"));			
			ThreadWithReactor twr = get(myJSONEvent.get("destination"));
			if (twr == null) {
				srvJSONResponse.put("msg", "Sorry, logged off " + myJSONEvent.get("destination"));
				myJSONEvent.getES().write(srvJSONResponse);
				
			} else {
				srvJSONResponse.put("msg", "Would you like to play a game of Tic-Tac-Toe with "+myJSONEvent.get("source"));
				((JSONEventSource)twr.getEventSource()).write(srvJSONResponse);
			}
		}
	}
	
	//play game response
	public class PlayGameResponseHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","PLAY_GAME_RESPONSE");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			srvJSONResponse.put("destination", myJSONEvent.get("destination"));			
			srvJSONResponse.put("msg", myJSONEvent.get("msg"));
			ThreadWithReactor twr = get(myJSONEvent.get("destination"));
			if (twr == null) {
				srvJSONResponse.put("msg", "Sorry, logged off " + myJSONEvent.get("destination"));
				myJSONEvent.getES().write(srvJSONResponse);		
			} else {
				((JSONEventSource)twr.getEventSource()).write(srvJSONResponse);
			}
		}
	}
	
	//game on handler
	public class GameOnHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","GAME_ON");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			srvJSONResponse.put("destination", myJSONEvent.get("destination"));			
			ThreadWithReactor twr = get(myJSONEvent.get("destination"));
			if (twr == null) {
				srvJSONResponse.put("msg", "Sorry, logged off " + myJSONEvent.get("destination"));
				myJSONEvent.getES().write(srvJSONResponse);		
			} else {
				((JSONEventSource)twr.getEventSource()).write(srvJSONResponse);
			}
		}
	}
	
	//move on the board message handler
	public class MoveMessageHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","MOVE_MESSAGE");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			srvJSONResponse.put("destination", myJSONEvent.get("destination"));	
			srvJSONResponse.put("play",  myJSONEvent.get("play"));
			srvJSONResponse.put("player",  myJSONEvent.get("player"));
			ThreadWithReactor twr = get(myJSONEvent.get("destination"));
			if (twr == null) {
				srvJSONResponse.put("msg", "Sorry, logged off " + myJSONEvent.get("destination"));
				myJSONEvent.getES().write(srvJSONResponse);		
			} else {
				((JSONEventSource)twr.getEventSource()).write(srvJSONResponse);
			}
		}
	}
	
	//game over handler
	public class GameOverHandler implements EventHandler {
		public void handleEvent(Event event) throws JSONException, IOException {
			JSONEvent myJSONEvent = (JSONEvent)event;
			JSONObject srvJSONResponse = new JSONObject();
			srvJSONResponse.put("type","GAME_OVER");
			srvJSONResponse.put("source", myJSONEvent.get("source"));
			srvJSONResponse.put("destination", myJSONEvent.get("destination"));	
			srvJSONResponse.put("play",  myJSONEvent.get("play"));
			srvJSONResponse.put("player",  myJSONEvent.get("player"));
			ThreadWithReactor twr = get(myJSONEvent.get("destination"));
			if (twr == null) {
				srvJSONResponse.put("msg", "Sorry, logged off " + myJSONEvent.get("destination"));
				myJSONEvent.getES().write(srvJSONResponse);		
			} else {
				((JSONEventSource)twr.getEventSource()).write(srvJSONResponse);
			}
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws JSONException
	 * @throws NoEventHandler
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, NoEventHandler, JSONException {
		// Startup the server
		Server newServer = new Server();
		newServer.init();
		newServer.run();
	}
}
