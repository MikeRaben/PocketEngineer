package com.bomberomedia.pocketengineer;

import android.content.res.Resources;
import android.graphics.Color;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MIKE";
    private AdView adView;

    private FirebaseAnalytics mFirebaseAnalytics;

    TextView hoseLenVal, flowRateVal, elevationVal, fricValue;
    SeekBar hoseLenSeek, flowRateSeek, elevationSeek;
    Spinner hoseTypeSpinner;

    Double C = 0.0;     //Hose coefficient
    int Q = 0;          //GPM
    int elevLoss = 0;   //+/- .5 height in feet
    int L = 0;          //Hose Length in feet

    int frictionLoss =0;

    ArrayList<HoseTypes> hoseTypes = HoseTypes.getUsHoseTypes();
    ArrayList<String> userHoseTypes = new ArrayList<>();

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        res = getResources();

        MobileAds.initialize(this, "ca-app-pub-3581816025507456~1968374174");

        adView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("5718014B0F30D06E9979A741D0B6AFB9")
                .build();

        adView.loadAd(request);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1234");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "myAd");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Ad");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        for (int i=0; i < hoseTypes.size(); i++) {
            if (hoseTypes.get(i).active) {
                userHoseTypes.add(hoseTypes.get(i).diameter.toString() + " inch");
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userHoseTypes);
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
        double newL = (L / 100.0);

        frictionLoss = (int) (C * newQ * newL + elevFl);
        updateUI();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seek_hose_length:
                L = progress * 50;
                recalc();
                break;

            case R.id.seek_flow_rate:
                Q = progress * 10;
                recalc();
                break;

            case R.id.seek_elev:
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
