package com.example.final_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class MainActivity extends Activity {

    private EditText employeNum;
    private EditText Id;
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://www.nir-levi.com/login/";
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //match the employ number and the id to their edit text.
        employeNum = (EditText) findViewById(R.id.editText1);
        Id = (EditText) findViewById(R.id.editText2);
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

    }
/*
    //use asynctask to connect to the server
    public class MyAsyncTask extends AsyncTask<String, String, Double> {
        private final double TRUE = 1;
        private final double FALSE = 0;
        public String res;

        //send Variables to the server
        protected Double doInBackground(String... params) {
            return postData(params[0], params[1],params[2]);
        }

        //check if user was able to connect and change the screen to the menu screen
        @Override
        protected void onPostExecute(Double loginVerified) {
            if (loginVerified == TRUE) {
                Intent menu = new Intent(getApplicationContext(), menu.class);
                startActivity(menu);
                finish();
            }
            if(loginVerified == FALSE) {
             //   Toast.makeText(MainActivity.this.getApplicationContext(), "שם משתמש או סיסמא שגויים", Toast.LENGTH_SHORT).show();
                final CharSequence[] options = { "נסה מחדש"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("שגיאה:שם משתמש או סיסמא שגויים");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("נסה מחדש")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        }

        //connect to the server
        public double postData(String a, String b, String c) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://www.nir-levi.com/login/");

            try {
                // send the Variables to the server
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
                nameValuePair.add(new BasicNameValuePair("employee_id", a));
                nameValuePair.add(new BasicNameValuePair("user_id", b));
                nameValuePair.add(new BasicNameValuePair("reg_id",c));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                // reed the response of the server
                HttpResponse response = httpClient.execute(httpPost);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = null;
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                res = sb.toString();
                Log.v("My Response:", res);
                //check if use was able to connect
                if (res.compareTo("valid") == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (ClientProtocolException e) {
// TODO Auto-generated catch block
            } catch (IOException e) {
// TODO Auto-generated catch block
            }
            return 0;
        }
    }
*/


    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("מתחבר...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String id = Id.getText().toString();
            String num = employeNum.getText().toString();
            String regId = GCMRegistrar.getRegistrationId(MainActivity.this);
                if(regId.isEmpty()) {
                    Log.v("registration:", regId);
                }
            //    else {
            //        GCMRegistrar.register(MainActivity.this,GCMIntentService.SENDER_ID);
            //        regId=GCMRegistrar.getRegistrationId(MainActivity.this);
            //    }

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("employee_id", num));
                params.add(new BasicNameValuePair("user_id", id));
                params.add(new BasicNameValuePair("reg_id",regId));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(MainActivity.this, mmenu.class);
                    i.putExtra("user_id",id);
                    startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }


        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        //call the asynctask when button click-to connect the server
        public void login(View v) {
        //    String num = employeNum.getText().toString();
        //    String id = Id.getText().toString();
        //    String regId = GCMRegistrar.getRegistrationId(this);
            //    String sendreg="";
            //    if(regId.isEmpty()) {
            //        Log.v("registration:", regId);
            //        sendreg = "o";
            //        Log.v("registration:", sendreg);
            //    }
            //    else {
            //regId= GCMRegistrar.register(MainActivity.this,
            //         GCMIntentService.SENDER_ID);
            //        sendreg="1";
            //        Log.v("registration",sendreg);
            new AttemptLogin().execute();

        }
    }














