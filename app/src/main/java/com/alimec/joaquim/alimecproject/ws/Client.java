package com.alimec.joaquim.alimecproject.ws;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by KithLenovo on 23/02/2015.
 */
public class Client {

    private static String serverAddr = "192.168.1.180";
    private static int serverPorta = 9009;



    public static JSONObject atomicTransaction(String message) throws IOException{

        AsyncTask<String,Void,String> commTask = new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                Socket comm = null;
                String message = params[0];
                try {
                    comm = new Socket(serverAddr,serverPorta);
                    PrintWriter sockWriter = new PrintWriter(comm.getOutputStream());
                    Scanner inputReader = new Scanner(comm.getInputStream());
                    sockWriter.print(message);

                    String response = "";

                    while(inputReader.hasNext()){
                        response += inputReader.nextLine();
                    }
                    inputReader.close();

                    return response;
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        };
        commTask.execute(message);

        String response = null;
        try {
            response = (String) commTask.get();
            if(response == null){
                return null;
            }
            return new JSONObject(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new IOException(response,e);
        }


        return null;
}



}
