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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class request_list extends Activity {

    private ProgressDialog pDialog;
    //handler
    private final mainHandler handler = new mainHandler(this);
    JSONObject json;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String GET_REQUEST_URL = "http://www.nir-levi.com/app/get_emp_requests.php";
    String requestId;
    //JSON element ids from repsonse of php script:
    private static final String TAG_GET_REQUEST = "get_emp_request";
    private static final String TAG_MESSAGE = "message";

    public ListView list;
    public ArrayList<String> request;
    int addrequest;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        list = (ListView) findViewById(R.id.listView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("user_id");
            id = value;
        }
        Log.v("user_id", id);

        //show dialog while try to get the requests
        pDialog = new ProgressDialog(request_list.this);
        pDialog.setMessage("מקבל בקשות..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(new Runnable() {
            public void run() {
                int get_emp_request;
                JSONObject parent = new JSONObject();
                try {
                    //send to parameters to the server
                    parent.put("user_id", id);
                    //using json parser class to send the parameters to the server.
                    json = jsonParser.sendPost(GET_REQUEST_URL, jsonParser.getPostStringJson(parent));
                    get_emp_request = json.getInt(TAG_GET_REQUEST);
                    if (get_emp_request == 1) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();

        request = new ArrayList<String>();
        if (addrequest == 1) {
            request.add("בקשה חדשה");
        }


        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, request);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(request_list.this, menu.class);
                startActivity(intent);
            }
        });
    }
    private static class mainHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<request_list> myClassWeakReference;

        public mainHandler(request_list myClassInstance) {
            myClassWeakReference = new WeakReference<request_list>(myClassInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            request_list myClass = myClassWeakReference.get();
            if (myClass != null) {
                if (myClass.pDialog != null) {
                    myClass.pDialog.dismiss();
                }
                //if the connection failed-connection problem
                if (msg.what == 0) {
                    try {
                        Toast.makeText(myClass, "Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //if the request received successfully
                if (msg.what == 1) {
                    try {
                        Log.v("get_request", myClass.json.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //if the connection failed-server problem
                if (msg.what == 2) {
                    try {
                        Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
            getMenuInflater().inflate(R.menu.menu_request_list, menu);
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