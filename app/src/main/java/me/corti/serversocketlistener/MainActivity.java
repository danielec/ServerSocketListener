package me.corti.serversocketlistener;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.corti.serversocketlistener.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            MainActivity activity = (MainActivity)this.getActivity();

            RadioGroup rg = (RadioGroup)rootView.findViewById(R.id.states);
            rg.setOnCheckedChangeListener(activity.checkChange);

            return rootView;
        }
    }

    final RadioGroup.OnCheckedChangeListener checkChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.mode_c:
                    //Abilita Porta e Indirizzo
                    findViewById(R.id.address).setEnabled(true);
                    findViewById(R.id.port).setEnabled(true);
                    findViewById(R.id.connect_btn).setEnabled(true);
                break;
                case R.id.mode_s:
                    //Abilita solo Porta
                    findViewById(R.id.address).setEnabled(false);
                    findViewById(R.id.port).setEnabled(true);
                    findViewById(R.id.connect_btn).setEnabled(true);
                break;
            }
        }
    };


    public void connect(View view){
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        int port = 0;
        try{
            port = Integer.valueOf(((EditText) findViewById(R.id.port)).getText().toString());
        }catch (Exception e){}

        RadioGroup rg = (RadioGroup) this.findViewById(R.id.states);
        int mode = rg.getCheckedRadioButtonId();
        if(mode == R.id.mode_c && address == null){
            Toast.makeText(this, "Attenzione: e' necessario indicare l'indirizzo a cui connettersi!", Toast.LENGTH_LONG).show();
            return;
        }

        if(port <= 0){
            Toast.makeText(this, "Attenzione: e' necessario indicare la porta per la connessione!", Toast.LENGTH_LONG).show();
            return;
        }


        switch (mode){
            case R.id.mode_c:
                startClient(address, port);
                break;
            case R.id.mode_s:
                startServer(port);
            break;
        }

    }

    public void startClient(String address, int port){
        Intent newIntent = new Intent(this, ClientActivity.class);
        newIntent.putExtra(ClientActivity.SERVER_PORT, port);
        newIntent.putExtra(ClientActivity.SERVER_ADDRESS, address);
        startActivity(newIntent);
    }

    public void startServer(int port){
        Intent newIntent = new Intent(this, ServerActivity.class);
        newIntent.putExtra(ServerActivity.SERVER_PORT, port);
        startActivity(newIntent);
    }

}
