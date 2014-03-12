package com.example.todo;



import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

        private ArrayAdapter<String> adapter;
        private ArrayList<String> taskList = new ArrayList<String>();
         EditText editText;
          ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                editText = (EditText) findViewById(R.id.editText1);
                listView = (ListView) findViewById(R.id.tasks);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);
                listView.setAdapter(adapter);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }
        
        /** Called when the user clicks the add task */
        public void addTask(View view) {

                taskList.add(0,editText.getText().toString());
                adapter.notifyDataSetChanged();
        }

}
