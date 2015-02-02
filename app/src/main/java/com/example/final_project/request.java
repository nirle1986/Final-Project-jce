package com.example.final_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.AndroidCharacter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.text.DateFormat;



public class request extends Activity implements View.OnClickListener {

    DateFormat format= DateFormat.getDateInstance();
    Calendar calendar=Calendar.getInstance();
    TextView label;
    Button btn;
    TextView label1;
    Button btn1;
    Button camera;
    Button addPicButton;


    private Spinner reasonSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
//add picture from gallery
//match the addpicbutton to the related button.
        addPicButton = (Button) findViewById(R.id.addPicButton);
//take a photo with the camera
//match the camera button to the related button.
        camera= (Button) findViewById(R.id.cameraButton);
        //for use the camera
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
//select date
//match all the labels and buttons to their related textview and button.
        label=(TextView)findViewById(R.id.textView2);
        btn=(Button)findViewById(R.id.button);
        label1=(TextView)findViewById(R.id.textView3);
        btn1=(Button)findViewById(R.id.button2);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        updatedate();
        updatedate1();

//add item to the spinner(reason of Absence).
        addItemsToReasonSpinner();
        addListenerToReasonSpinner();
    }
//using the camera
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == 0){
            Bitmap theImage = (Bitmap)data.getExtras().get("data");
        }
    }
//add the spinner
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //to do pop up window "to nothing selceted"

            }
        });
    }



//update the label of the date that was chosen
    public void updatedate(){

        label.setText(format.format(calendar.getTime()));
    }
    public void updatedate1(){

        label1.setText(format.format(calendar.getTime()));
    }
//set the date picker dialog- to pick a date
    public void setDate(){

        new DatePickerDialog(request.this,start,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void setDate1(){

        new DatePickerDialog(request.this,end,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request, menu);
        return true;
    }
//set the date picker
    DatePickerDialog.OnDateSetListener start=new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){

            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate();
        }
    };
    DatePickerDialog.OnDateSetListener end=new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){

            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate1();
        }
    };
//the button of the date
    @Override
    public void onClick(View view) {

        setDate();
    }
//the button of the date
    public void onClick1(View view){

        setDate1();
    }
//the button of the clear fields
    public void clearFields(View view){

        updatedate();

    }
//the button of send the request
    public void sendRequest(View view){
        //send the request
    }
}
