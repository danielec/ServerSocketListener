package me.corti.serversocketlistener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import me.corti.serversocketlistener.R;

public class ServerActivity extends ServerSocketActivity {


    @Override
    protected void onDataReceive(String datas) {
        EditText txt = (EditText) findViewById(R.id.received_txt);
        txt.append(datas+"\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        if (savedInstanceState == null) {
            WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ip = Integer.reverseBytes(ip);
            }

            byte[] ipByteArray = BigInteger.valueOf(ip).toByteArray();


            String ipAddress = null;

            try {
                ipAddress = InetAddress.getByAddress(ipByteArray).getHostAddress();
            } catch (UnknownHostException ex) {
                Log.e("WIFIIP", "Unable to get host address.");
                ipAddress = null;
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(ipAddress, _port))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private String _serverAddress;
        private int _serverPort;
        public PlaceholderFragment(String _serverAddress, int _serverPort) {
            this._serverAddress = _serverAddress;
            this._serverPort = _serverPort;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_server, container, false);
            TextView tv = (TextView) rootView.findViewById(R.id.server_address);
            tv.setText(this._serverAddress);

            tv = (TextView) rootView.findViewById(R.id.server_port);
            tv.setText(""+this._serverPort);
            return rootView;
        }
    }
}
