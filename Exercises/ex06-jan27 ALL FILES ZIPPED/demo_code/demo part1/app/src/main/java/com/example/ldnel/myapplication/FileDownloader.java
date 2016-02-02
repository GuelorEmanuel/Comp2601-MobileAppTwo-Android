package com.example.ldnel.myapplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**
 * Created by ldnel on 2016-01-03.
 */
public class FileDownloader {
    /*Utility class for download a file from the internet and store in a local file
    Based on example code at https://github.com/foamyguy/stacksites by Tim Cocks.
    There is a related YouTube video of his demo app at: https://www.youtube.com/watch?v=40mYDQkK44A
     */


    //Tag for Log statements
    private static final String TAG = FileDownloader.class.getSimpleName();


    /************************************************
     * Download a file from the Internet and store it locally
     *
     * @param URL - the url of the file to download
     * @param fos - a FileOutputStream to save the downloaded file to.
     ************************************************/
    public static void DownloadFromUrl(String URL, FileOutputStream fos) {  //this is the downloader method
        try {

            URL url = new URL(URL); //URL of the file

            //keep the start time so we can display how long it took to the Log.
            long startTime = System.currentTimeMillis();
            Log.i(TAG, "file download begining");

			/* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            // this will be useful so that you can show a tipical 0-100% progress bar
            //int lenghtOfFile = ucon.getContentLength();

            Log.i(TAG, "Opened Connection");

            /************************************************
             * Define InputStreams to read from the URLConnection.
             ************************************************/
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.i(TAG, "Got InputStream and BufferedInputStream");

            /************************************************
             * Define OutputStreams to write to our file.
             ************************************************/

            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.i(TAG, "Got FileOutputStream and BufferedOutputStream");

            /************************************************
             * Start reading  and writing  file.
             ************************************************/

            byte data[] = new byte[1024];
            //long total = 0;
            int count;
            //loop and read the current chunk
            while ((count = bis.read(data)) != -1) {
                //keep track of size for progress.
                //total += count;

                //write this chunk
                bos.write(data, 0, count);
            }
            //Have to call flush or the  file can get corrupted.
            bos.flush();
            bos.close();

            Log.i(TAG, "download ready in "
                    + ((System.currentTimeMillis() - startTime))
                    + " milisec");

        } catch (IOException e) {
            Log.d(TAG, "Error: " + e);
        }

    }
}
