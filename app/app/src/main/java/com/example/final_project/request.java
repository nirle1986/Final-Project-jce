package com.example.final_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import org.json.JSONObject;


public class request extends Activity  implements DatePickerFragment.TheListener {

    Button btn;
    Button btn1;
    ImageView viewImage;
    Button PicButton;
    EditText comments;
    int pressedBtn=-1;
    private Spinner reasonSpinner;
    //if no reason was selected
    int NothingSelected;
    // Progress Dialog
    //handler
    private final mainHandler handler = new mainHandler(this);
    JSONObject json;
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    //url
    private static final String REQUEST_URL = "http://www.nir-levi.com/app/set_request.php";
    //JSON element ids from repsonse of php script:
    private static final String TAG_UPLOAD = "upload";
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
        btn = (Button) findViewById(R.id.button);
        btn1 = (Button) findViewById(R.id.button2);
        PicButton = (Button) findViewById(R.id.PicButton);
        viewImage = (ImageView) findViewById(R.id.viewImage);
        PicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        comments = (EditText) findViewById(R.id.editText);

        //add item to the spinner(reason of Absence).
        addItemsToReasonSpinner();
        addListenerToReasonSpinner();
        //get the user id from the main activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("user_id");
            id = value;
        }
        Log.v("check", id);
        //listeners to the date buttons
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
                pressedBtn = 0;
            }

        });
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
                pressedBtn = 1;
            }

        });
        //make the image bigger on click
        viewImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(request.this, FullScreenImage.class);

                viewImage.buildDrawingCache();
                Bitmap image = viewImage.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);
                startActivity(intent);

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
    //the button of the clear fields
    public void clearFields(View view){
        startActivity(getIntent());
        finish();
    }
    //the button of send the request
    public void sendRequest(View view){
        sendcomments=comments.getText().toString();
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
            //show dialog while try to send request
            pDialog = new ProgressDialog(request.this);
            pDialog.setMessage("שולח בקשה");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            new Thread(new Runnable() {
                public void run() {
                    int upload;
                    JSONObject parent=new JSONObject();
                    try {
                        //send to parameters to the server
                        parent.put("user_id",id);
                        parent.put("from_date", FromDate);
                        parent.put("to_date", ToDate);
                        parent.put("reason", reason);
                        parent.put("send_comments", sendcomments);

                        //using json parser class to send the parameters to the server.
                        json =  jsonParser.sendPost(REQUEST_URL,jsonParser.getPostStringJson(parent));
                        upload = json.getInt(TAG_UPLOAD);
                        if (upload == 1) {
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

    }

    private static class mainHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<request> myClassWeakReference;
        public mainHandler(request myClassInstance) {
            myClassWeakReference = new WeakReference<request>(myClassInstance);
        }
        @Override
        public void handleMessage(Message msg) {
            request myClass = myClassWeakReference.get();
            if (myClass != null) {
                if(myClass.pDialog != null) {
                    myClass.pDialog.dismiss();
                }
                if (msg.what == 0){
                    try {
                        Toast.makeText(myClass, "Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == 1) {
                    try {
                            Log.d("request sent", myClass.json.toString());
                            Intent i = new Intent(myClass, menu.class);
                            myClass.startActivity(i);
                            myClass.finish();
                            Toast.makeText(myClass, myClass.json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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

}