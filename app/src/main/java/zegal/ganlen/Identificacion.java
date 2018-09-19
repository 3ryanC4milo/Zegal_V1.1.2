package zegal.ganlen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class Identificacion extends AppCompatActivity {



    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAP_TWO = 2;
    private static final String TAG =Identificacion.class.getSimpleName() ;

    ArrayList<ImageView> arr;

    ImageView ine1, ine2, firma;
    ImageButton btnE, btnE1, btnE2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificacion);

        ine1 = findViewById(R.id.imvIneFront);
        ine2 = findViewById(R.id.imvIneBack);
        firma = findViewById(R.id.imageView1);

        btnE1 = findViewById(R.id.ibtErFront);
        btnE2 = findViewById(R.id.ibtErBack);
        btnE = findViewById(R.id.ibtAddFront);

        btnE1.setVisibility(View.INVISIBLE);
        btnE2.setVisibility(View.INVISIBLE);

        arr = new ArrayList<>();

        arr.add(ine1);
        arr.add(ine2);

        String image_path = getIntent().getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        firma.setImageBitmap(bitmap);
    }

    public void openCamera (View v) {

        if(v.getId() == R.id.ibtAddFront) {
            openCamera();
        }
        else if(v.getId() == R.id.ibtAddBack)
        {
            openCameraTwo();
        }
    }

    private void openCamera () {

        // check for camera permission
        int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, CAMERA);

        // do we have camera permission?
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            // we have camera permission, open System camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else {

            // we don't have it, request camera permission from system
            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA},
                    REQUEST_IMAGE_CAPTURE);
        }

    }

    private void openCameraTwo() {

        // check for camera permission
        int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, CAMERA);

        // do we have camera permission?
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            // we have camera permission, open System camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_TWO);
            }
        }
        else {

            // we don't have it, request camera permission from system
            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA},
                    REQUEST_IMAGE_CAP_TWO);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_IMAGE_CAPTURE) {

            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // camera permission was granted
                openCamera();

            } else {

                Log.d(TAG, "permissions not accepted");
            }

        }

        else if (requestCode == REQUEST_IMAGE_CAP_TWO)
        {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // camera permission was granted
                openCameraTwo();

            } else {

                Log.d(TAG, "permissions not accepted");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine1.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_IMAGE_CAP_TWO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine2.setImageBitmap(imageBitmap);
        }

    }

    public void clickear(View view) {
        switch (view.getId())
        {
            case R.id.getSign:
                startActivity(new Intent(Identificacion.this, Signature.class));
                break;
            case R.id.btnContrato:
                if(ine1.getDrawable() == null || ine2.getDrawable() == null || firma.getDrawable() == null)
                {
                    Toast.makeText(this,
                            "Requiere completar los requisitos para validar su contrato",
                            Toast.LENGTH_LONG).show();
                }

                else
                    {
                        firma.buildDrawingCache();
                        Bitmap bmap = firma.getDrawingCache();
                        startActivity(new Intent(Identificacion.this, Firma_Cliente.class).putExtra("btmp",bmap));
                        break;

                    }

        }
    }
}
