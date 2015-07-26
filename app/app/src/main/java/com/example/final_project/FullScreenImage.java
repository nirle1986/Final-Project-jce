package com.example.final_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FullScreenImage extends Activity {
    //set the image big on new activity on click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");

        ImageView imgDisplay;
        Button btnClose;


        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (Button) findViewById(R.id.btnClose);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);


        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });

        layout.setBackground(imgDisplay.getDrawable());
        imgDisplay.setImageBitmap(bmp );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_image, menu);
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
