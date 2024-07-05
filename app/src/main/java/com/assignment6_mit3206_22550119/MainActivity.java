package com.assignment6_mit3206_22550119;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
    }

    // Check for permission at runtime
    public void captureImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    // Open camera application
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!= null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            // Resize the image
            Bitmap resizedBitmap = resizeBitmap(imageBitmap, 800, 800);

            // Save the resized image to SD card
            saveImageToSDCard(resizedBitmap);
        }
    }

    private Bitmap resizeBitmap(Bitmap original, int width, int height) {
        return Bitmap.createScaledBitmap(original, width, height, true);
    }

    private void saveImageToSDCard(Bitmap bitmap) {
        // Reduce the size of the captured image
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 600, true);

        // Create a file to save the image
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "captured_image.jpg");

        try {
            // Save the image to the SD card
            FileOutputStream fos = new FileOutputStream(imageFile);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            // Notify the user that the image has been saved
            Uri imageUri = Uri.fromFile(imageFile);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}