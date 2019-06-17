package com.example.android.smartlabapplication.UI;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.smartlabapplication.Controller.NetworkController;
import com.example.android.smartlabapplication.Model.ActuatorData;
import com.example.android.smartlabapplication.Model.SensorData;
import com.example.android.smartlabapplication.R;
import com.example.android.smartlabapplication.Util.TextAnalyzer;
import com.example.android.smartlabapplication.Security.SecurityModule;
import com.example.android.smartlabapplication.ObserverDesignPattern.UIObserver;

import java.util.ArrayList;
import java.util.Locale;


@SuppressWarnings("ALL")
public class SpeechUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,UIObserver {


    private ImageView voiceButton;
    private EditText editText ;
    private SpeechRecognizer mSpeechRecognizer ;
    private Intent mIntent;
    private  ArrayList<String> matches;
    //Natural Language Processing Component for getting the device and command
    private TextAnalyzer textAnalyzer;
    private SensorData sensorData;
    private NetworkController networkController;
    private ActuatorData actuatorData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_ui);
        editText = findViewById(R.id.text);
        networkController = NetworkController.getInstance();
        networkController.registerObserver(this);
        initComponents();

    }
    @Override
    protected void onDestroy()
    {   super.onDestroy();
        networkController.removeObserver(this);
    }
    @Override
    protected  void onStop()
    {   super.onStop();
        networkController.removeObserver(this);


    }

    @Override
    protected  void onPause()
    {   super.onPause();
        networkController.removeObserver(this);


    }
    @Override
    protected  void onResume()
    {   super.onResume();
        networkController.registerObserver(this);
    }
    @Override
    protected  void onStart()
    {   super.onStart();
        networkController.registerObserver(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.layout.main_drawer_view, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_buttons) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initComponents(){
         textAnalyzer = new TextAnalyzer(this);
         addNavigationDrawer();
         initSpeechComponent();

    voiceButton = findViewById(R.id.voice_btn);
    voiceButton.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_UP :
                {   editText.setHint("Results will come here");
                    mSpeechRecognizer.stopListening();
                    break;

                }
                case MotionEvent.ACTION_DOWN:
                {
                    editText.setHint("Listening....");
                    mSpeechRecognizer.startListening(mIntent);
                    break;
                }

            }
            return false;
        }


    });
}

    private void addNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initSpeechComponent() {

        mIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
              onSpeechResults(bundle);

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }

    private void onSpeechResults(Bundle bundle)
    {
        matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        editText.setText("");
        if(matches==null)
        {
            editText.setText("Couldnt recoqnize your speech input , please try again");
            return;

        }
        /**
         * here we will use the string matches.get(0) to find a matching device + command
         */
        SecurityModule securityModule = new SecurityModule();
        if(!securityModule.validateString(matches.get(0)))
        {
            editText.setText("Invalid Command");
            return;
        }
        String sensorDetails = textAnalyzer.processStringForSensor(matches.get(0));
        //sensor found id , type
        if(!sensorDetails.equals(""))
        {  String arr[] = sensorDetails.split(",");
            sensorData = new SensorData(arr[0],arr[1]);
            // retrieve related sensor value from fiware
            networkController.sendRequestToFiware(sensorData.getUrl(),null,2);
            return;
        }
        /*
        *if no sensor was found check for acutator
        */
        ArrayList<String> data = textAnalyzer.processStringForActuatorAndCommand(matches.get(0));
        textAnalyzer.finish();
        if(data==null)
        {
            editText.setText("We couldnt find a matching device or command , please try again");
            return;
        }

        /**
         *ApiController component will be called to send that data to fiware
         */
        networkController = NetworkController.getInstance();
        actuatorData = new ActuatorData();
        actuatorData.setCommand(data.get(0));
        actuatorData.setActuatorId(data.get(1));

        if(networkController.networkAvailable(getBaseContext())) {

            networkController.sendRequestToFiware(actuatorData.getUrl(), actuatorData.getApiObject(),1);

        }
        else
            editText.setText("There is no network access!");





    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_voice) {
            Intent voiceCommandsUI =  new Intent(SpeechUI.this, SpeechUI.class);
            startActivity(voiceCommandsUI);
            finish();
        } else if (id == R.id.nav_buttons) {
            Intent commandButtonsUI =  new Intent(SpeechUI.this, ButtonsUI.class);
            startActivity(commandButtonsUI);
            finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResponse() {
        Log.e( "onResponse: ", networkController.getResponseString());

    }
}


