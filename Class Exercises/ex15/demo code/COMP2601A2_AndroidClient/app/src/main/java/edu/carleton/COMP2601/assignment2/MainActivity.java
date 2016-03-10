package edu.carleton.COMP2601.assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import edu.carleton.COMP2601.assignment2.R;
import edu.carleton.COMP2601.assignment2.communication.Event;
import edu.carleton.COMP2601.assignment2.communication.EventHandler;
import edu.carleton.COMP2601.assignment2.communication.JSONEvent;
import edu.carleton.COMP2601.assignment2.communication.JSONEventSource;
import edu.carleton.COMP2601.assignment2.communication.NoEventHandler;
import edu.carleton.COMP2601.assignment2.communication.Reactor;
import edu.carleton.COMP2601.assignment2.communication.ThreadWithReactor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
 
/*
 *  Name: Sean Benjamin
 *
 *  MainActivity class acts as connection between the server and the game activity.
 *  It executes a reactor pattern to handle events coming from the server.
 *  These are the events the reactor handles:
 *  CONNECTED_RESPONSE
 *  USERS_UPDATED
 *  PLAY_GAME_REQUEST
 *  PLAY_GAME_RESPONSE
 *  GAME_ON
 *  MOVE_MESSAGE
 *  GAME_OVER
 *  DISCONNECT_RESPONSE
 *  
 *  The reactor pattern code is based on the reactor pattern java code provided
 *  by prof. Tony White:
 *  package edu.carleton.COMP2601.assignment2.communication;
 *  
 *  
 */
 //TODO For the purposes of this classroom exercise, this class is complete and should not require modification.


public class MainActivity extends Activity {
	private static String HOST =   "192.168.10.222";//"10.0.2.2"; //local host when accessing local machine via an emulator
	private static int PORT = 3000;//2001; //Port number of server
	static MainActivity instance;  //creating a singleton pattern, so GameActivity can access methods and variables
	
	public String name;  //name of user which is created by random value N + a number from 1 to 100
	private clientThreadWithReactor clientTWR;
	StartReactor myReactor;
	Handler handler;  //handler to allow updates to the UI
	private ArrayList<String> users;   //list of users
	private ArrayAdapter<String> adapter;  //used to display list of users in the listview
	final Context mContext = this;
	TextView status;  //Status of game. Did not want to play game
	ProgressDialog myProgressDialog;  //progress spinner
	public int player = 1;  
	GameActivity gInstance;
	String dest;

	/* create random name
	 * start reactor
	 * create array adapter for listview
	 * send connect request message to server
	 * set up onClick listner for the listview: send a Play Game Request to the person clicked on
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		handler = new Handler();
		status = (TextView) findViewById(R.id.textView1);

		myReactor = new StartReactor();
		myReactor.execute("");
		
		name = "N" + new Random().nextInt(100);
	
		ListView listView = (ListView)findViewById(R.id.listView1);
		users = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, users);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
			    	JSONObject msg = new JSONObject();
			    	try{
			    		msg.put("type", "PLAY_GAME_REQUEST");
			    		msg.put("source", name);
			    		msg.put("destination", ((TextView) view).getText().toString());
			    		new Write(msg).execute("");
			    	} catch (JSONException e){
			    		e.printStackTrace();
			    	}
			  }
		});
		
		
		//Send Connect Request message to server on start up.  Starting the progress spinner dialog.
		JSONObject msg = new JSONObject();
		try {
			msg.put("type", "CONNECT_REQUEST");
			msg.put("source", name);
			new Write(msg).execute("");
			showDialog(0);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// get an instance of this Activity
	public static MainActivity getInstance(){
		return instance;
	}
	
	//setup for creating a Progress Dialog
	protected Dialog onCreateDialog(int id) {
	
	// Arg value corresponds to showDialog(0)
		if (id != 0)
			return null;
		myProgressDialog = new ProgressDialog(this); 
		myProgressDialog.setMessage("Loading ..."); 
		myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		myProgressDialog.setCancelable(false);
		return myProgressDialog ;
	}
	
	//on destory sends a disconnect msg to server
	protected void onDestroy(){
		super.onDestroy();
		JSONObject msg = new JSONObject();
			try {
				msg.put("type", "DISCONNECT_REQUEST");
				msg.put("source", name);
				new Write(msg).execute("");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	/*
	 * This class actually creates the server with the reactor and sets up
	 * reading etc.
	 */
	public class StartReactor extends AsyncTask<String, String, String> {

		protected String doInBackground(String... params) {
			clientTWR = new clientThreadWithReactor();
			clientTWR.init();
			try {
				clientTWR.run();
			} catch (Exception e) {
				return "Failed to initialize server";
			}

			return "Initialized server";
		}
		
		protected void onPostExecute(String result) {
			System.out.println(result);
			super.onPostExecute(result);
		}
	}

	/* 
	 * Require this in order to do network I/O in background
	 */
	public class Write extends AsyncTask<String, String, String> {
		JSONObject msg;

		public Write(JSONObject msg) {
			this.msg = msg;
		}

		protected String doInBackground(String... params) {
			try {
				clientTWR.myEventSource.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return msg.toString();
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	/*
	 * Implementation of the reactor within an object class.
	 */
	private class clientThreadWithReactor {
		public Reactor myReactor;
		public JSONEventSource myEventSource;

		//i
		public void init() {
			myReactor = new Reactor();
			myReactor.register("CONNECTED_RESPONSE", new ConnectedResponsedHandler());
			myReactor.register("USERS_UPDATED", new UsersUpdatedHandler());
			myReactor.register("PLAY_GAME_REQUEST", new PlayGameRequestHandler());
			myReactor.register("PLAY_GAME_RESPONSE", new PlayGameResponseHandler());
			myReactor.register("GAME_ON", new GameOnHandler());
			myReactor.register("MOVE_MESSAGE", new MoveMessageHandler());
			myReactor.register("GAME_OVER", new GameOverHandler());
			myReactor.register("DISCONNECT_RESPONSE", new DisconnectResponseHandler());
		}

		void run() throws IOException, ClassNotFoundException, NoEventHandler,
				JSONException {
			myEventSource = new JSONEventSource(HOST, PORT);
			ThreadWithReactor twr = new ThreadWithReactor(myEventSource, myReactor);
			twr.start();
		}

		/*
		 * Event handler for the CONNECTED RESPONSE message
		 */
		private class ConnectedResponsedHandler implements EventHandler {
			public void handleEvent(Event event) {


				handler.post(new Runnable () {
					public void run() {
						myProgressDialog.dismiss();

						Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();
					}
				});
				
			}
		}
	
		/*
		 * Event handler for the DISCONNECT RESPONSE message
		 */
	
		private class DisconnectResponseHandler implements EventHandler {
			public void handleEvent(Event event) {

				onDestroy();
			}
		}
			
		
		/*
		 * Event handler for the PLAY GAME RESPONSE message
		 */
	
		private class PlayGameResponseHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						final JSONEvent myJSONEvent = (JSONEvent) event;
						if(myJSONEvent.get("msg").equalsIgnoreCase("false")){
							status.setText(myJSONEvent.get("source") + " does not want to play.");
						} else {
							dest = myJSONEvent.get("source").toString();
							Intent i = new Intent(mContext, GameActivity.class);
						    startActivity(i);						       
						}
					}
				});
			}
		}
		
		/*
		 * Event handler for the PLAY GAME REQUEST message
		 */
	
		
		private class PlayGameRequestHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						
						final JSONEvent myJSONEvent = (JSONEvent) event;
					
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

						// set title
						alertDialogBuilder.setTitle(myJSONEvent.get("msg")+"");

						// set dialog message
						alertDialogBuilder
							.setMessage("Yes to play!")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									JSONObject msg = new JSONObject();

									try {
										msg.put("type", "PLAY_GAME_RESPONSE");
										msg.put("source", name);
										msg.put("destination", myJSONEvent.get("source").toString());
										msg.put("msg", "true");
										new Write(msg).execute("");

										//start game activity
										Intent i = new Intent(mContext, GameActivity.class);
									    startActivity(i);
										
									    dest = myJSONEvent.get("source").toString();
										player = 2;  //if user accepts the game request, then they are player 2
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							})
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									JSONObject msg = new JSONObject();

									try {
										msg.put("type", "PLAY_GAME_RESPONSE");
										msg.put("source", name);
										msg.put("destination", myJSONEvent.get("source").toString());
										msg.put("msg", "false");
										new Write(msg).execute("");
										System.out.println("Play game destination" + myJSONEvent.get("source").toString() );

									} catch (JSONException e) {
										e.printStackTrace();
									}
									dialog.cancel();
								}
						});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				});
				
			}
		}
		
		/*
		 * Event handler for the GAME ON message
		 */
	
		private class GameOnHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						final JSONEvent myJSONEvent = (JSONEvent) event;
						gInstance = GameActivity.getInstance();
						gInstance.button.setText(getResources().getString(R.string.button_run));
						gInstance.mGame.resetGameBoard();
						if (player == 1)
							gInstance.enableBoard();
						gInstance.mTextView.setText(myJSONEvent.get("source").toString() + getResources().getString(R.string.player_X_start_game));
					}
				});
			}
		}
		
		/*
		 * Event handler for the MOVE MESSAGE
		 */
	
		
		private class MoveMessageHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						final JSONEvent myJSONEvent = (JSONEvent) event;
						gInstance = GameActivity.getInstance();
						gInstance.mGame.setGameBoard(Integer.parseInt(myJSONEvent.get("play")), Integer.parseInt(myJSONEvent.get("player")));
						gInstance.enableBoard();
						String pressButton = "Button " + Integer.parseInt(myJSONEvent.get("play")) + " clicked by " + instance.dest;
						gInstance.mTextView.setText(pressButton);
						if (gInstance.mGame.isGameDone()){ //if game is done send a GAME OVER message to server
							try {
								JSONObject msg = new JSONObject();
								msg.put("type", "GAME_OVER");
								msg.put("source", name);
								msg.put("destination", dest);
								msg.put("play", myJSONEvent.get("play"));
								msg.put("player", myJSONEvent.get("player"));
								new Write(msg).execute("");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							gInstance.GameComplete(Integer.parseInt(myJSONEvent.get("player")));
						} 
					}
				});
			}
		}
		
		/*
		 * Event handler for the GAME OVER message
		 */
	
		
		private class GameOverHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						final JSONEvent myJSONEvent = (JSONEvent) event;
						gInstance = GameActivity.getInstance();
						if(Integer.parseInt(myJSONEvent.get("player")) != 0 ) {
							gInstance.GameComplete(Integer.parseInt(myJSONEvent.get("player")));
						} else {
							String quit_game =  myJSONEvent.get("source") + " " + getResources().getString(R.string.quit_game);
							gInstance.mTextView.setText(quit_game);
							gInstance.button.setText(getResources().getString(R.string.button_start));
								gInstance.mGame.resetGameBoard();
								gInstance.disableBoard();
						}						
					}
				});
			}
		}


		/*
		 * Event handler for the USER_UPDATED message
		 */
		private class UsersUpdatedHandler implements EventHandler {
			public void handleEvent(final Event event) {
				handler.post(new Runnable () {
					public void run() {
						final JSONEvent myJSONEvent = (JSONEvent) event;
						users.clear();
						JSONArray listOfUsersJSON = null;
						try {
							listOfUsersJSON = new JSONArray(myJSONEvent.get("users"));
							for (int i = 0; i < listOfUsersJSON.length(); i++)
								if (!listOfUsersJSON.getString(i).equalsIgnoreCase(name))
									users.add(listOfUsersJSON.getString(i));
							//update the list view with new user data
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

}

