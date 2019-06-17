package com.example.android.smartlabapplication.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.smartlabapplication.Controller.FirebaseController;
import com.example.android.smartlabapplication.Controller.NetworkController;
import com.example.android.smartlabapplication.R;
import com.example.android.smartlabapplication.Security.SecurityModule;
import com.example.android.smartlabapplication.ObserverDesignPattern.UIObserver;

public class LoginUI extends AppCompatActivity implements UIObserver {

    private LinearLayout loginButton;
    private EditText passwordEditText,usernameEditText;
    private FirebaseController firebaseController;
    private NetworkController networkController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);
        loginButton = findViewById(R.id.login_linear_layout);
        networkController = NetworkController.getInstance();
        passwordEditText = findViewById(R.id.login_key);
        usernameEditText = findViewById(R.id.user_name);
        firebaseController = FirebaseController.getInstance();
        firebaseController.registerObserver(this);

        initListener();

    }
    private void initListener()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                if (username.equals("")) {
                    alertUser("please enter your username");

                } else if (password.equals("")) {
                    alertUser("Please enter your password!");
                }
                else {
                    //Check network connectivity
                    if (!networkController.networkAvailable(getBaseContext())) {
                       alertUser("No internet Connection!");
                        return;
                    }
                    SecurityModule authenticator = new SecurityModule();
                    if (authenticator.validateString(username)&&authenticator.validateString(password))
                    {   //Send request to Accounts server
                        sendLoginRequest(username,authenticator.encrypt(password));

                    }
                    else
                    {
                        alertUser("Invalid!");
                    }
                }
            }
        });
    }
    private void startNextActivity()
    {Intent nextUI;
        nextUI = new Intent(this, LabUI.class);
    Toast.makeText(this, "Logged in Successfully",
            Toast.LENGTH_SHORT).show();
    startActivity(nextUI);
    firebaseController.removeObserver(this);
    finish();

}
    private void alertUser(String alertText)
    {
    AlertDialog alertDialog = new AlertDialog.Builder(LoginUI.this).create();
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
    private void sendLoginRequest(String username, final String password)
    {

        firebaseController.sendLoginRequest(username,password);






}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResponse() {
        switch (firebaseController.getLoginResult())
        {
            case 1:
            {
                alertUser("wrong username");
                break;
            }
            case 2:
            {
                startNextActivity();
                break;
            }
            case 3:
            {
                alertUser("wrong password");
            }


        }
}
}
