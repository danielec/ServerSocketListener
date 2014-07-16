package me.corti.serversocketlistener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by daniele on 15/07/14.
 */
public class ClientSocketActivity extends Activity {

    public final static String  SERVER_PORT = "me.corti.serversocketlistener.SERVER_PORT",
                                SERVER_ADDRESS = "me.corti.serversocketlistener.SERVER_ADDRESS";


    protected Socket _socket;

    protected int _serverPort;
    protected String _serverAddress;

    protected Thread _clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        _serverPort = intent.getIntExtra(SERVER_PORT, -1);
        _serverAddress = intent.getStringExtra(SERVER_ADDRESS);

        if(_serverPort > 0 && _serverAddress != null){
            this._clientThread = new Thread(new ClientThread());
            this._clientThread.start();
            try{
                this._clientThread.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendToServer(String datas){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(_socket.getOutputStream())),
                    true);
            out.println(datas);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(_serverAddress);

                _socket = new Socket(serverAddr, _serverPort);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            this._clientThread.interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
