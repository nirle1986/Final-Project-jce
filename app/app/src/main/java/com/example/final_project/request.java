package com.example.final_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class request extends Activity  implements DatePickerFragment.TheListener {

    Button btn;
    Button btn1;
    ImageView viewImage;
    Button PicButton;
    EditText comments;
    int pressedBtn=-1;
    private Spinner reasonSpinner;
    int NothingSelected;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://www.nir-levi.com/request/";
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //send to server
    String id;
    String FromDate;//Variable to send date
    String ToDate;//Variable to send date
    String reason;//Variable to send the reason for absence
    Bitmap sendimage;//Variable to send the image
    String sendcomments;//Variable to send the comments





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        //match all the buttons to their related buttons.
        btn=(Button)findViewById(R.id.button);
        btn1=(Button)findViewById(R.id.button2);
        PicButton=(Button)findViewById(R.id.PicButton);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        PicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        comments= (EditText) findViewById(R.id.editText);

        //add item to the spinner(reason of Absence).
        addItemsToReasonSpinner();
        addListenerToReasonSpinner();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("user_id");
            id=value;
        }
        Log.v("check", id);
        //listeners to the date buttons
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
                pressedBtn=0;
            }

        });
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
                pressedBtn=1;
            }

        });
    }


    //change to text on the button to selected date
    public void returnDate(String date) {
        if(pressedBtn==0) {
            btn.setText(date);
            FromDate=date;
        }
        if(pressedBtn==1) {
            btn1.setText(date);
            ToDate=date;
        }

    }


    //add the spinner to choose reason of absence
    public void addItemsToReasonSpinner(){

        reasonSpinner = (Spinner) findViewById(R.id.reasonSpinner);
        ArrayAdapter<CharSequence> reasonSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.reason,android.R.layout.simple_spinner_item);

        reasonSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        reasonSpinner.setAdapter(reasonSpinnerAdapter);
    }

    public void addListenerToReasonSpinner(){

        reasonSpinner = (Spinner)findViewById(R.id.reasonSpinner);


        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String itemSelectedInSpinner = parent.getItemAtPosition(pos).toString();
                reason=itemSelectedInSpinner;
                Log.v("reason",reason);
                if(pos==0){
                    NothingSelected=pos;
                }
                NothingSelected=pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request, menu);
        return true;
    }
    //add the 2 option to upload image take picture from camera or to choose from the gallery
    private void selectImage() {
        final CharSequence[] options = { "צלם תמונה", "בחר מהגלריה","ביטול" };

        AlertDialog.Builder builder = new AlertDialog.Builder(request.this);
        builder.setTitle("צרף אישור");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals( "צלם תמונה"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals( "בחר מהגלריה"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("ביטול" )) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //show the picture according to the user's choice.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    sendimage=bitmap;

                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap gallery = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                sendimage=gallery;
                viewImage.setImageBitmap(gallery);
            }
        }
    }

    class sendrequest extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(request.this);
            pDialog.setMessage("שולח בקשה...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int upload;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", id));
                params.add(new BasicNameValuePair("from_date",FromDate ));
                params.add(new BasicNameValuePair("to_date",ToDate));
                params.add(new BasicNameValuePair("reason",reason));
            //    params.add(new BasicNameValuePair("send_image",sendimage));
                params.add(new BasicNameValuePair("send_comments",sendcomments));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                upload = json.getInt("upload");
                if (upload == 1) {
                    Log.d("upload Successful!", json.toString());
                    Log.d("check","request sent");
                    Intent i = new Intent(request.this, menu.class);
                    startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("upload Failure!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(request.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    //the button of the clear fields
    public void clearFields(View view){
        startActivity(getIntent());
        finish();
    }
    //the button of send the request
    public void sendRequest(View view){
        int newrequest=0;
        sendcomments=comments.getText().toString();
        //if no reason of absence was selected
        if (NothingSelected==0){
                final CharSequence[] options = { "נסה מחדש"};
                AlertDialog.Builder builder = new AlertDialog.Builder(request.this);
                builder.setTitle("שגיאה:לא נבחרה סיבת היעדרות");
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
        else{
        //    Intent i = new Intent(request.this, list.class);
        //    finish();
        //    startActivity(i);
       //     Log.v("send","send request");
       //     Log.v("check",FromDate);
        //    Log.v("check",ToDate);
        //    Log.v("check",reason);
         //   Log.v("check",sendcomments);
            new sendrequest().execute();
            newrequest=1;
            //Intent i = new Intent(request.this, request_list.class);
            //i.putExtra("newrequest",newrequest);
            //startActivity(i);
            //finish();

        }
        //send the request
    }

}

