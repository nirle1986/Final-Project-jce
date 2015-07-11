package com.example.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import org.json.JSONObject;
import java.lang.ref.WeakReference;


public class MainActivity extends Activity {

    private EditText employeNum;
    private EditText Id;
    // Progress Dialog
    private ProgressDialog pDialog;
    //handler
    private final mainHandler handler = new mainHandler(this);
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    //url
    private static final String LOGIN_URL = "http://www.nir-levi.com/app/login.php";
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USER_LEVEL = "user_level";
    String deviceID;
    JSONObject json;
    String id;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        //match the employ number and the id to their edit text.
        employeNum = (EditText) findViewById(R.id.editText1);
        Id = (EditText) findViewById(R.id.editText2);
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
    }

        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        //call the asynctask when button click-to connect the server
        public void login(View v) {
            //show dialog while try to login
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("מתחבר...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            new Thread(new Runnable() {
                public void run() {
                    int success;
                    //convert the id and the employee number to string.
                    id = Id.getText().toString();
                    String num = employeNum.getText().toString();
                    //registration number of user mobile
                    String regId = GCMRegistrar.getRegistrationId(MainActivity.this);
                    Log.v("registarationid",regId);
                    if(regId.isEmpty()) {
                        GCMRegistrar.register(MainActivity.this, GCMIntentService.SENDER_ID);
                        regId=GCMRegistrar.getRegistrationId(MainActivity.this);
                    }
                    JSONObject parent=new JSONObject();
                    try {
                        //send to parameters to the server
                        parent.put("employee_id",num);
                        parent.put("user_id", id);
                        parent.put("reg_id", regId);
                        parent.put("device_id", deviceID);

                        //using json parser class to send the parameters to the server.
                        json =  jsonParser.sendPost(LOGIN_URL,jsonParser.getPostStringJson(parent));
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
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
        private final WeakReference<MainActivity> myClassWeakReference;
        public mainHandler(MainActivity myClassInstance) {
            myClassWeakReference = new WeakReference<MainActivity>(myClassInstance);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity myClass = myClassWeakReference.get();
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
                        int user_level;
                        user_level = myClass.json.getInt(TAG_USER_LEVEL);
                        if (user_level == 0) {
                            Log.d("employee Login Successful!", myClass.json.toString());
                            Intent i = new Intent(myClass, menu.class);
                            i.putExtra("user_id", myClass.id);
                            myClass.startActivity(i);
                            myClass.finish();
                            Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("manager Login Successful!", myClass.json.toString());
                            Intent i = new Intent(myClass, mmenu.class);
                            i.putExtra("user_id", myClass.id);
                            myClass.startActivity(i);
                            myClass.finish();
                            Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (msg.what == 2) {
                    try {
                    Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    Log.v("json to hebrew",myClass.json.getString(TAG_MESSAGE));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);

            }
        }
    }

    }














