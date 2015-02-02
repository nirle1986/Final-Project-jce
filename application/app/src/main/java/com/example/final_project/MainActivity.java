package com.example.final_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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


//        String num = employeNum.getText().toString();
//        String id = Id.getText().toString();

//        String email = "shai@webexperts.co.il";
//        String pass = "a123456A";

//           new MyAsyncTask().execute(num, id);


    }

//use asynctask to connect to the server
    public class MyAsyncTask extends AsyncTask<String, String, Double> {
        //  int loginVerified=0;
        public boolean loginVerified = false;
        public String res;
//send Variables to the server
        protected Double doInBackground(String... params) {
// TODO Auto-generated method stub
            postData(params[0], params[1]);
            return null;
        }
//check if user was able to connect and change the screen to the menu screen
        protected void onPostExecute(boolean loginVerified) {
            if (loginVerified == true) {
                Intent menu = new Intent(getApplicationContext(), menu.class);
                //  menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menu);
                finish();
            }
        }

//       protected void onProgressUpdate(int loginVerified){
//           if(loginVerified == 1)
//           {
//               Intent menu = new Intent(getApplicationContext(),menu.class);
//               //homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//               startActivity(menu);
//               finish();
//           }
//       }
//connect to the server
        public void postData(String a, String b) {
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
//check if use was able to connect
                if (res.compareTo("valid")==0) {
                //    Log.v("My Response2::", res);
                    loginVerified = true;
                    onPostExecute(loginVerified);
                }
                Log.v("My Response::", res);




            } catch (ClientProtocolException e) {
// TODO Auto-generated catch block
            } catch (IOException e) {
// TODO Auto-generated catch block
            }
        }

        public String getRes() {
            return res;
        }
    }

    @Override
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
      //  String result = new MyAsyncTask().getRes();
        //   check.setText(result);
       // if (result.compareTo("valid") == 1) {
        //    Intent intent = new Intent(this, menu.class);
         //   startActivity(intent);
        }
        //check the employnum and id and log in
//            if (num.compareTo("shai@webexperts.co.il") == 0 && id.compareTo("a123456A") == 0)

    }









