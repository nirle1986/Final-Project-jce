package com.example.final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.lang.ref.WeakReference;



public class request_details extends Activity {

    private ProgressDialog pDialog;
    //handler
    private final mainHandler handler = new mainHandler(this);
    JSONObject json;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String GET_REQUEST_URL = "http://www.nir-levi.com/app/get_request.php";
    private static final String DECIDE_REQUEST= "http://www.nir-levi.com/app/decide_request.php";
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
    public TextView Picture;
    public TextView Details;
    public EditText ManagerComments;
    public String SendManagerComments;
    public int approve;
    public int refuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        Intent i = getIntent();
        message = i.getStringExtra("message");
        requestId=message;

        Name=(TextView)findViewById(R.id.textView4);
        FromDate=(TextView)findViewById(R.id.textView6);
        ToDate=(TextView)findViewById(R.id.textView9);
        Reason=(TextView)findViewById(R.id.textView14);
        Picture=(TextView)findViewById(R.id.textView15);
        Details=(TextView)findViewById(R.id.textView16);
        ManagerComments=(EditText)findViewById(R.id.editText3);


        //show dialog while try to send request
        pDialog = new ProgressDialog(request_details.this);
        pDialog.setMessage("מקבל נתוני בקשה...");
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
        private final WeakReference<request_details> myClassWeakReference;
        public mainHandler(request_details myClassInstance) {
            myClassWeakReference = new WeakReference<request_details>(myClassInstance);
        }
        @Override
        public void handleMessage(Message msg) {
            request_details myClass = myClassWeakReference.get();
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
                        myClass.Picture.setText("אין");
                        myClass.Details.setText(myClass.json.getString("comment"));
                        //myClass.ManagerDetails.setText(myClass.json.getString("first_name"));
                        //Intent i = new Intent(myClass, menu.class);
                        //myClass.startActivity(i);
                        //myClass.finish();
                        //Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                if (msg.what == 3) {
                    try {
                        Toast.makeText(myClass, "ההחלטה נשלחה", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == 4) {
                    try {
                        Toast.makeText(myClass, "ההחלטה לא נשלחה", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
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

    public void approve(View view){
        SendManagerComments=ManagerComments.getText().toString();
        approve=1;
            //show dialog while try to send request
            pDialog = new ProgressDialog(request_details.this);
            pDialog.setMessage("שולח החלטה..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            new Thread(new Runnable() {
                public void run() {
                    int decide;
                    JSONObject parent=new JSONObject();
                    try {
                        //send to parameters to the server
                        parent.put("req_id",requestId);
                        parent.put("decision", approve);
                        parent.put("manager_comments", SendManagerComments);

                        //using json parser class to send the parameters to the server.
                        json =  jsonParser.sendPost(DECIDE_REQUEST,jsonParser.getPostStringJson(parent));
                        decide = json.getInt("decide");
                        if (decide == 1) {
                            handler.sendEmptyMessage(3);
                        }
                        else{
                            handler.sendEmptyMessage(4);
                        }
                    }catch(Exception e){
                        handler.sendEmptyMessage(0);
                    }
                }
            }).start();



        }
    public void refuse(View view){
        SendManagerComments=ManagerComments.getText().toString();
        refuse=2;
        //show dialog while try to send request
        pDialog = new ProgressDialog(request_details.this);
        pDialog.setMessage("שולח החלטה..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(new Runnable() {
            public void run() {
                int decide;
                JSONObject parent=new JSONObject();
                try {
                    //send to parameters to the server
                    parent.put("req_id",requestId);
                    parent.put("decision", refuse);
                    parent.put("manager_comments", SendManagerComments);

                    //using json parser class to send the parameters to the server.
                    json =  jsonParser.sendPost(DECIDE_REQUEST,jsonParser.getPostStringJson(parent));
                    decide = json.getInt("decide");
                    if (decide == 1) {
                        handler.sendEmptyMessage(3);
                    }
                    else{
                        handler.sendEmptyMessage(4);
                    }
                }catch(Exception e){
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();



    }

    }



