package com.example.android.smartlabapplication.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.smartlabapplication.Controller.NetworkController;
import com.example.android.smartlabapplication.Model.ActuatorData;
import com.example.android.smartlabapplication.Model.SensorData;
import com.example.android.smartlabapplication.R;
import com.example.android.smartlabapplication.ObserverDesignPattern.UIObserver;

import org.json.JSONObject;

public class ButtonsUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,UIObserver{

    private NetworkController networkController;
    private ActuatorData actuatorData;
    private SensorData sensorData;
    private int requestId;
    private Button btn[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons_ui);
        networkController = NetworkController.getInstance();
        networkController.registerObserver(this);
        actuatorData = new ActuatorData();
        actuatorData.setActuatorId("");
        sensorData  = new SensorData("","");
        btn = new Button[3];
        initComponents();

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

    @SuppressLint("ResourceType")
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


    private void initComponents()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        //add listners for buttons
        addButtonListners();

    }

    private void addButtonListners() {
        btn[0] = (Button) findViewById(R.id.on_button);
        btn[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestId = 1;
                sendActuatorRequest();
            }



        });
        btn[1]= (Button) findViewById(R.id.off_button);
        btn[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestId = 2;
                sendActuatorRequest();
            }



        });
        btn[2] = (Button) findViewById(R.id.sensor_btn);
        btn[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestId = 3;
                sendSensorRequest();
            }



        });
    }

    private void sendSensorRequest() {
        if(!networkController.networkAvailable(this))
        {
            alertUser("No internet Connection");
            return;
        }
        networkController.sendRequestToFiware(sensorData.getUrl(),new JSONObject(),2);

    }

    private void alertUser(String alertText)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ButtonsUI.this).create();
        alertDialog.setTitle("Login Error");
        alertDialog.setMessage(alertText);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private  void sendActuatorRequest()
    {
        if(!networkController.networkAvailable(this))
        {
            alertUser("No internet Connection");
         return;
        }
        if(requestId==1)
            actuatorData.setCommand("on");
        else
            actuatorData.setCommand("off");
        networkController.sendRequestToFiware(actuatorData.getUrl(),actuatorData.getApiObject(),1);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_voice) {
            Intent voiceCommandsUI =  new Intent(ButtonsUI.this, SpeechUI.class);
            startActivity(voiceCommandsUI);
            finish();
        } else if (id == R.id.nav_buttons) {
            Intent commandButtonsUI =  new Intent(ButtonsUI.this, ButtonsUI.class);
            startActivity(commandButtonsUI);
            finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResponse() {
        if(requestId==1)
          alertUser("Light has been opened");
       else if(requestId==2)
           alertUser("Light has been closed");
        else
            alertUser("Temperature is : "+networkController.getResponseString()+" *C");

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



}
