package com.example.android.smartlabapplication.Controller;

import com.example.android.smartlabapplication.ObserverDesignPattern.Subject;
import com.example.android.smartlabapplication.ObserverDesignPattern.UIObserver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by DELL on 6/7/2019.
 */

public class FirebaseController implements Subject{

    private FirebaseDatabase firebaseDatabase;
    private int loginResult;
    private UIObserver loginObserver;
    private static FirebaseController instance;

    public int getLoginResult()
    {
        return loginResult;
    }
    private FirebaseController()
    {

    }

    public  static  FirebaseController getInstance()
    {
       if(instance == null) {
           instance = new FirebaseController();
           instance.firebaseDatabase = FirebaseDatabase.getInstance();
       }
       return instance;
    }

    public void sendLoginRequest(String username, final String password)
    {
        DatabaseReference reference = instance.firebaseDatabase.getReference("users").child(username);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null)
                {
                    loginResult = 1;

                }
               else if(password.equals(dataSnapshot.getValue().toString()))
                {
                    loginResult = 2;
                }else
                {
                    loginResult = 3;

                }
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void registerObserver(UIObserver uiObserver) {
        loginObserver = uiObserver;
    }

    @Override
    public void removeObserver(UIObserver uiObserver) {
     loginObserver = null;
    }

    @Override
    public void notifyObservers() {
      loginObserver.onResponse();
    }
}
