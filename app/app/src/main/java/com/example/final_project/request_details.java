package com.example.final_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class request_details extends Activity {

    public TextView Name;
    public TextView FromDate;
    public TextView ToDate;
    public TextView Reason;
    public TextView Picture;
    public TextView Details;
    public TextView ManagerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        Name=(TextView)findViewById(R.id.textView4);
        FromDate=(TextView)findViewById(R.id.textView6);
        ToDate=(TextView)findViewById(R.id.textView9);
        Reason=(TextView)findViewById(R.id.textView14);
        Picture=(TextView)findViewById(R.id.textView15);
        Details=(TextView)findViewById(R.id.textView16);
        ManagerDetails=(TextView)findViewById(R.id.textView17);

        Name.setText("ניר לוי");
        FromDate.setText("21/6/15");
        ToDate.setText("22/6/15");
        Reason.setText("מחלה");
        Picture.setText("אין");
        Details.setText("מחלה");
        ManagerDetails.setText("מאושר");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_details, menu);
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
}
