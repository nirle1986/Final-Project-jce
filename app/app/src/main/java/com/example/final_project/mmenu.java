package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class mmenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //change to request screen when the button of send request is clicked
    public void sendRequest(View view)
    {
        //get the user id from the main activity
        String id="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("user_id");
            id=value;
            Log.v("user_id", id);
        }
        //send the user id to the next activity
        Intent i = new Intent(mmenu.this, request.class);
        i.putExtra("user_id",id);
        startActivity(i);

    }
    public void getRequests(View view)
    {
        Intent intent = new Intent(this,manager_request_list.class);
        startActivity(intent);

    }





}
