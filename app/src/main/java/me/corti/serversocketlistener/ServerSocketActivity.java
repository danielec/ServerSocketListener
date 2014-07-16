package me.corti.serversocketlistener;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by daniele on 15/07/14.
 */
public abstract class ServerSocketActivity extends Activity{

    public static final String  SERVER_PORT = "me.corti.serversocketlistener.SERVER_PORT";

    protected abstract void onDataReceive(String datas);

    protected Thread _serverThread;
    protected ServerSocket _serverSocket;
    protected int _port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _port = getIntent().getIntExtra(SERVER_PORT, -1);
        if(_port > 0){
            _serverThread = new Thread(new ServerRunnable());
            _serverThread.start();
        }
    }

    final class ServerRunnable implements Runnable{

        @Override
        public void run() {
            Socket _socket = null;

            try{
                ServerSocketActivity.this._serverSocket = new ServerSocket(ServerSocketActivity.this._port);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    _socket = _serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(_socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    final class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    final String read = input.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ServerSocketActivity.this.onDataReceive(read);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            _serverSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            _serverThread.interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
