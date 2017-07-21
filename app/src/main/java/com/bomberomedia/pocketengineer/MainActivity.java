package com.bomberomedia.pocketengineer;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MIKE";

    TextView hoseLenVal, flowRateVal, elevationVal, fricValue;
    SeekBar hoseLenSeek, flowRateSeek, elevationSeek;
    Spinner hoseTypeSpinner;

    Double C = 0.0;
    int Q = 0;
    int elevLoss = 0;
    int L = 0;
    int frictionLoss =0;

    ArrayList<HoseTypes> hoseTypes = new ArrayList<>();
    ArrayList<String> usHoseTypes = new ArrayList<>();

    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "C: " + C + "  Q: " + Q + "  L: " + L + "EL: " + elevLoss);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        res = getResources();

        hoseTypes.add(new HoseTypes(0.75, 1100.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.0, 150.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.25, 80.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.5, 24.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.75, 15.5, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(2.0, 8.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(2.5, 2.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(3.0, 0.677, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(3.5, 0.34, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(4.0, 0.2, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(4.5, 0.1, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(5.0, 0.08, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(6.0, 0.05, Boolean.TRUE));

        for (int i=0; i < hoseTypes.size(); i++) {
            if (hoseTypes.get(i).active) {
                usHoseTypes.add(hoseTypes.get(i).diameter.toString() + " inch");
            }
        }

        hoseLenVal = (TextView)findViewById(R.id.tv_hose_len_val);
        flowRateVal = (TextView)findViewById(R.id.tv_flow_rate_val);
        elevationVal = (TextView)findViewById(R.id.tv_elev_value);
        fricValue = (TextView) findViewById(R.id.tv_FL_value);

        hoseLenSeek = (SeekBar)findViewById(R.id.seek_hose_length);
        flowRateSeek = (SeekBar)findViewById(R.id.seek_flow_rate);
        elevationSeek = (SeekBar)findViewById(R.id.seek_elev);

        hoseLenSeek.setOnSeekBarChangeListener(this);
        flowRateSeek.setOnSeekBarChangeListener(this);
        elevationSeek.setOnSeekBarChangeListener(this);

        hoseTypeSpinner = (Spinner)findViewById(R.id.hose_type_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usHoseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoseTypeSpinner.setAdapter(adapter);

        hoseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                C = hoseTypes.get(position).coefficient;
                recalc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recalc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                // Zero all values and update ui
                C = 0.0;
                Q = 0;
                L = 0;
                frictionLoss = 0;

                hoseLenSeek.setProgress(0);
                flowRateSeek.setProgress(0);
                elevationSeek.setProgress(10);

                hoseTypeSpinner.setSelection(0, true);
                recalc();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void updateUI(){
        //update textviews and friction loss master display  flowRateVal, elevationVal;
        hoseLenVal.setText(res.getString(R.string.hose_len_val, L));
        flowRateVal.setText(res.getString(R.string.flow_value, Q));
        elevationVal.setText(res.getString(R.string.elev_value, elevLoss));

        fricValue.setText(res.getString(R.string.fl_value, frictionLoss));

    }

    public void recalc(){
        //recalc friction loss
        // FL =  C  *  (Q  / 100) ^2 *   L   / 100
        double elevFl = elevLoss / 2;
        double newQ = (Q / 100.0) * (Q/100.0);
  //      newQ = newQ * newQ;

        frictionLoss = (int) (C * newQ * (L / 100) + elevFl);
        updateUI();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seek_hose_length:
                //update hose len variable
                L = progress * 50;
                recalc();
                break;

            case R.id.seek_flow_rate:
                //update flow rate variable
                Q = progress * 10;
                recalc();
                break;

            case R.id.seek_elev:
                //update elevation variable
                elevLoss = 5 * (progress - 10);
                recalc();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
