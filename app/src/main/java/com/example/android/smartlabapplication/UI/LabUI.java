package com.example.android.smartlabapplication.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.smartlabapplication.R;

public class LabUI extends AppCompatActivity {

   private String[] labs = {
           "lab1",

   };
   public static String currentLab;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("labui", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_ui);
        ArrayAdapter<String> labAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        labs

                );
        ListView labList = (ListView) findViewById(R.id.listview);
        labList.setAdapter(labAdapter);
        labList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                //based on the position
                currentLab =  labs[position];
                Intent intent = new Intent(LabUI.this,ButtonsUI.class);
                startActivity(intent);

            }
        });


}
}