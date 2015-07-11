package com.example.final_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    String requestId;
    //JSON element ids from repsonse of php script:
    private static final String TAG_GET_REQUEST = "get_request";
    private static final String TAG_MESSAGE = "message";

    String message;
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

        Intent i = getIntent();
        message = i.getStringExtra("message");
        requestId=message;

        Name=(TextView)findViewById(R.id.textView4);
        FromDate=(TextView)findViewById(R.id.textView6);
        ToDate=(TextView)findViewById(R.id.textView9);
        Reason=(TextView)findViewById(R.id.textView14);
        Picture=(TextView)findViewById(R.id.textView15);
        Details=(TextView)findViewById(R.id.textView16);

        Name.setText(message);
//        FromDate.setText("21/6/15");
//        ToDate.setText("22/6/15");
//        Reason.setText("מחלה");
//        Picture.setText("אין");
//        Details.setText("מחלה");
//        ManagerDetails.setText("מאושר");

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
                if (msg.what == 0)
                {
                    //dialog error
                }
                if (msg.what == 1) {
                    try {
                        Log.v("get_request", myClass.json.toString());
                        myClass.Name.setText(myClass.json.getString("first_name"));
                        myClass.FromDate.setText(myClass.json.getString("from"));
                        myClass.ToDate.setText(myClass.json.getString("to"));
                        myClass.Reason.setText(myClass.json.getString("reason"));
//                      myClass.Picture.setText("אין");
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




}
