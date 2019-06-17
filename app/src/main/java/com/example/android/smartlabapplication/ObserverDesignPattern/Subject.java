package com.example.android.smartlabapplication.ObserverDesignPattern;

/**
 * Created by DELL on 3/19/2019.
 */

public interface Subject {
    void registerObserver(UIObserver uiObserver);
    void removeObserver(UIObserver uiObserver);
    void notifyObservers();
}