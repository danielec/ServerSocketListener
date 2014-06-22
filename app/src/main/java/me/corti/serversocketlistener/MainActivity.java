package me.corti.serversocketlistener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import corti.me.serversocketlistener.R;


public class MainActivity extends SocketActivity {

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

    @Override
    protected void onDataRead(String data) {
        TextView mTextView = (TextView)this.findViewById(R.id.output_txt);
        mTextView.append(data+"\n");
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
                break;
                case R.id.mode_s:
                    //Abilita solo Porta
                break;
            }
        }
    };

    public void sendTxt(View v){
        EditText t = (EditText) findViewById(R.id.input_txt);
        String tosend = t.getText().toString();
        this.sendData(tosend);
    }
}
