package com.example.final_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.ref.WeakReference;


public class decision_details extends Activity {

    private ProgressDialog pDialog;
    //handler
    private final mainHandler handler = new mainHandler(this);
    JSONObject json;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String GET_REQUEST_URL = "http://www.nir-levi.com/app/get_request.php";
    String requestId;
    //JSON element ids from repsonse of php script:
    private static final String TAG_GET_REQUEST = "get_request";
    private static final String TAG_MESSAGE = "message";

    String FirstName;
    String LastName;
    String message;
    public TextView Name;
    public TextView FromDate;
    public TextView ToDate;
    public TextView Reason;
    public TextView Decision;
    public TextView ManagerComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_details);

        Intent i = getIntent();
        message = i.getStringExtra("message");
        requestId=message;

        Name=(TextView)findViewById(R.id.textView18);
        FromDate=(TextView)findViewById(R.id.textView6);
        ToDate=(TextView)findViewById(R.id.textView16);
        Reason=(TextView)findViewById(R.id.textView9);
        Decision=(TextView)findViewById(R.id.textView13);
        ManagerComments=(TextView)findViewById(R.id.textView20);


        //show dialog while try to send request
        pDialog = new ProgressDialog(decision_details.this);
        pDialog.setMessage("מקבל את תגובת המנהל..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(new Runnable() {
            public void run() {
                int get_request;
                JSONObject parent=new JSONObject();
                try {
                    //send to parameters to the server
                    parent.put("request_id", requestId);
                    //using json parser class to send the parameters to the server.
                    json =  jsonParser.sendPost(GET_REQUEST_URL,jsonParser.getPostStringJson(parent));
                    get_request = json.getInt(TAG_GET_REQUEST);
                    if (get_request == 1) {
                        handler.sendEmptyMessage(1);
                    }
                    else{
                        handler.sendEmptyMessage(2);
                    }
                }catch(Exception e){
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
    private static class mainHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<decision_details> myClassWeakReference;
        public mainHandler(decision_details myClassInstance) {
            myClassWeakReference = new WeakReference<decision_details>(myClassInstance);
        }
        @Override
        public void handleMessage(Message msg) {
            decision_details myClass = myClassWeakReference.get();
            if (myClass != null) {
                if(myClass.pDialog != null) {
                    myClass.pDialog.dismiss();
                }
                if (msg.what == 0) {
                    try {
                        Toast.makeText(myClass, "Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == 1) {
                    try {
                        //Log.v("get_request", myClass.json.toString());
                        myClass.FirstName = myClass.json.getString("first_name");
                        myClass.LastName = myClass.json.getString("last_name");
                        myClass.Name.setText(myClass.FirstName+" "+myClass.LastName);
                        myClass.FromDate.setText(myClass.json.getString("from"));
                        myClass.ToDate.setText(myClass.json.getString("to"));
                        myClass.Reason.setText(myClass.json.getString("reason"));
                        myClass.ManagerComments.setText(myClass.json.getString("manager_comment"));
                        if(myClass.json.getString("decision").equals("1")){
                            myClass.Decision.setText("מאושר");
                            myClass.Decision.setTextColor(Color.GREEN);
                        }else{
                            myClass.Decision.setText("נדחה");
                            myClass.Decision.setTextColor(Color.RED);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (msg.what == 2) {
                    try {
                        Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);

            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decision_details, menu);
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
