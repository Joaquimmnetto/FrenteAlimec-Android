package com.alimec.joaquim.alimecproject.ws;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by KithLenovo on 23/02/2015.
 */
public class Transaction {

//
//
    private String serverAddr;
    private int serverPorta;

    private boolean exceptionOcourred = false;

    public Transaction(String address, int port) {
        this.serverAddr = address;
        this.serverPorta = port;
    }

    public JSONObject fazerComando(JSONObject comando) throws IOException{

        AsyncTask<String,Void,Object> commTask = new AsyncTask<String,Void,Object>(){

            @Override
            protected Object doInBackground(String... params) {
                Socket comm = null;
                String message = params[0];
                try {
                    comm = new Socket();
                    comm.connect(new InetSocketAddress(serverAddr,serverPorta),4000);

                    BufferedOutputStream out = new BufferedOutputStream(comm.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(comm.getInputStream()));

                    out.write(message.getBytes());
                    out.write(255);
                    out.flush();



                    StringWriter response = new StringWriter();
                    int readen = -1;
                    while(true){
                        readen = in.read();

                        if(readen < 0 || readen > 254){break;}

                        response.write((char)readen);
                    }

                    return response.toString();
                } catch (Exception e) {
                    exceptionOcourred = true;
                    return e;
                }
            }
        };
        commTask.execute(comando.toString());

        Object response = null;
        try {
            response = commTask.get();

            if(response == null){
                return null;
            }

            if(exceptionOcourred){
                throw (IOException)response;
            }
            return new JSONObject((String)response);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new IOException(e);
        }


        return null;
    }

    public static Transaction newTransaction(String address,int port) {
        return new Transaction(address,port);
    }
}
