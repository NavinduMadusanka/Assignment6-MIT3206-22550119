package com.assignment6_mit3206_22550119;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Button take_photo;
    int flag = 0;
    Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.click_image);
        take_photo = (Button) findViewById(R.id.camera_button);

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == 0) {

                    // -- code for taking photo --

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 99);

                } else if(flag == 1) {

                    //--- code for saving photo ---

                    savePhotoToMySdCard(b);

                    Toast.makeText(getApplicationContext(), "Photo Saved in SD/DCMI/Camera", Toast.LENGTH_SHORT).show();

                    flag = 0;
                    take_photo.setText("Take Photo");

                }

            }
        });



    }


    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //super.onActivityResult(requestCode, resultCode, data);

    //if(requestCode == 99 && resultCode == RESULT_OK && data != null){

    //b = (Bitmap) data.getExtras().get("data");

    //iv.setImageBitmap(b);

    //flag = 1;
    //take_photo.setText("Save Photo");

    //}

    //}


    private void savePhotoToMySdCard(Bitmap bit){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String f_name = sdf.format(new Date());

        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root+"/DCMI.Camera");
        folder.mkdirs();

        File my_file = new File(folder, f_name+".JPEG");

        try {

            FileOutputStream stream = new FileOutputStream(my_file);
            bit.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}