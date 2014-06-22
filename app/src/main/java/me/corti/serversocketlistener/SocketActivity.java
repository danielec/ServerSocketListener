package me.corti.serversocketlistener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Daniele Corti on 22/06/2014.
 */
public abstract class SocketActivity extends Activity {

    protected ServerSocket _server = null;
    protected Socket _client = null;

    protected Socket socket(){
        return _client;
    }

    protected Thread _serverThread = null, _clientThread = null;

    protected void prepareServer() throws IOException{
        if(_server == null) throw new IllegalStateException("Server Not Ready");
        _client = _server.accept();
        if(_client == null) throw new IllegalStateException("Accept not worked");
        SocketListener sl = new SocketListener(this);
        _serverThread = new Thread(sl);
        _serverThread.start();
    }

    protected String _tosend;
    protected void sendData(String tosend){
        if(_client == null)
            throw new IllegalStateException("Client not ready");

        _tosend = tosend;

        SocketSender ss = new SocketSender(this);
        _clientThread = new Thread(ss);
        _clientThread.start();
    }

    protected String tosend(){
        return _tosend;
    }

    final protected class SocketSender implements Runnable{

        SocketActivity _activity;
        private static final String CRLF = "\r\n";


        public SocketSender(SocketActivity _activity){
            this._activity = _activity;
        }

        @Override
        public void run() {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(_activity.socket().getInputStream()));
                DataOutputStream strm = new DataOutputStream(_activity.socket().getOutputStream());
                strm.writeBytes(_activity.tosend()+CRLF);

                String reply = br.readLine();
                if(reply != null && reply.length() > 0)
                    _activity.onDataRead(reply);
                br.close();
                strm.close();
            }catch (IOException e){
                Log.e("IO_SOCKET", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        this._serverThread.interrupt();
        this._serverThread = null;
        if(this._clientThread != null)
            this._clientThread.interrupt();
        this._clientThread = null;
        try {
            this._client.close();
        }catch (Exception e){}
        try {
            this._server.close();
        }catch (Exception e){}
        super.onStop();
    }

    protected abstract void onDataRead(String data);

    final protected class SocketListener implements Runnable{

        private static final String CRLF = "\r\n";
        private static final String ACK = "ACK";
        protected SocketActivity _socket;

        public SocketListener(SocketActivity socket){
            this._socket = socket;
        }


        @Override
        public void run() {
            while (true){
                try {
                    InputStream is = this._socket.socket().getInputStream();
                    if (is.available() > 0) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line = null;
                        while((line = br.readLine()) != null){
                            this._socket.onDataRead(line);
                        }

                        DataOutputStream dos = new DataOutputStream(this._socket.socket().getOutputStream());
                        dos.writeBytes(ACK+CRLF);

                        dos.close();
                        br.close();
                        isr.close();
                    }
                    is.close();
                    wait(200);
                }catch (IOException e){
                    Log.e("IO_SOCKET", e.getMessage());
                    e.printStackTrace();
                }catch (InterruptedException e){
                    Log.e("INTER_SOCKET", e.getMessage());
                    e.printStackTrace();
                }

            }

        }
    }

}

