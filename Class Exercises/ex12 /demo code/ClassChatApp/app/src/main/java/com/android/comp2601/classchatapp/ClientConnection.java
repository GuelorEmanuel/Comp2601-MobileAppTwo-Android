package com.android.comp2601.classchatapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
/**
 * Created by seansteelebenjamin on 16-02-06.
 */
public class ClientConnection {

    private String serverMessage;
    private final String TAG ="ClientConnection";

    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter mOut;
    BufferedReader mIn;

    //Declare the interface. The method messageReceived(String message)
    //must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    /**
     *  Constructor of the class.
     *  OnMessagedReceived listens for the messages
     *  received from server
     */
    public ClientConnection(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){

        //TODO output message back to client
        Log.e(TAG, "Message being sent: " + message);
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }

    }

    public void stopClient(){
        sendMessage("Disconnect");
        mRun = false;
    }

    public void run() throws ConnectException {


            mRun = true;

            try {


                //here you must put your computer's IP address.
                InetAddress serverAddr = InetAddress.getByName(Common.mIPAddress);

                Log.e(TAG, "C: Connecting...");

                //create a socket to make the connection with the server
                Socket socket = new Socket(serverAddr, Common.mPort);
                if(socket == null) throw new ConnectException();

                try {

                    //send the message to the server
                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);

                    Log.e(TAG, "C: Sent.");
                    Log.e(TAG, "C: Done.");

                    mIn = null;
                    //receive the message which the server sends back
                    mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //in this while the client listens for the
                    // messages sent by the server

                    //TODO: read a line of from input stream and read
                    // response from server and display it using
                    // the messageRecieved method
                    while (mRun) {
                        serverMessage = mIn.readLine();

                        if (serverMessage != null) {
                            mMessageListener.messageReceived(serverMessage);
                            mOut.flush();
                        }
                        serverMessage = null;
                    }
                    Log.e(TAG, "S: Received Message: '" + serverMessage + "'");

                } catch (Exception e) {

                    Log.e(TAG, "S: Error", e);

                } finally {
                    //the socket must be closed. It is not possible
                    // to reconnect to this socket after it is closed,
                    // which means a new socket instance has to be created.

                    socket.close();
                }

            } catch (ConnectException e) {

                Log.e(TAG, "C: Error", e);
                throw e;


            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "IOException");
            }

            Log.e(TAG, "Thread Done");
    }

}
