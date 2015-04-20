package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private EditText employeNum;
    private EditText Id;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //match the employ number and the id to their edit text.
        employeNum = (EditText) findViewById(R.id.editText1);
        Id = (EditText) findViewById(R.id.editText2);

    }

    //use asynctask to connect to the server
    public class MyAsyncTask extends AsyncTask<String, String, Double> {
        private final double TRUE = 1;
        private final double FALSE = 0;
        public String res;

        //send Variables to the server
        protected Double doInBackground(String... params) {
            return postData(params[0], params[1]);
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
                builder.setTitle("שם משתמש או סיסמא שגויים");
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
        public double postData(String a, String b) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://www.nir-levi.com/login/");

            try {
                // send the Variables to the server
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("employee_id", a));
                nameValuePair.add(new BasicNameValuePair("user_id", b));
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
                Log.v("My Response::", res);
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

        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        //call the asynctask when button click-to connect the server
        public void login(View v) {
            String num = employeNum.getText().toString();
            String id = Id.getText().toString();
            new MyAsyncTask().execute(num, id);
        }


    }










