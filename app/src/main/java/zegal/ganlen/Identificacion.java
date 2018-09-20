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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.q;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.Manifest.permission.CAMERA;

public class Identificacion extends AppCompatActivity {



    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAP_TWO = 2;
    private static final String TAG =Identificacion.class.getSimpleName() ;
    String [] mes = {"ENERO","fEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO",
                        "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    GregorianCalendar fecha = new GregorianCalendar();
    DatabaseReference mDatabaseReference;
    ImageView ine1, ine2, firma;
    ImageButton btnE, btnE1, btnE2;
    Button cont;

    String dia, month, anio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificacion);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        ine1 = findViewById(R.id.imvIneFront);
        ine2 = findViewById(R.id.imvIneBack);
        firma = findViewById(R.id.imageView1);

        btnE1 = findViewById(R.id.ibtErFront);
        btnE2 = findViewById(R.id.ibtErBack);
        btnE = findViewById(R.id.ibtAddFront);

        btnE1.setVisibility(View.INVISIBLE);
        btnE2.setVisibility(View.INVISIBLE);

        cont = findViewById(R.id.btnContrato);

        dia = String.valueOf(fecha.get(Calendar.DATE));
        month = mes[fecha.get(Calendar.MONTH)];
        anio = String.valueOf(fecha.get(Calendar.YEAR));


        String image_path = getIntent().getStringExtra("imagePath");
        final Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        firma.setImageBitmap(bitmap);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Identificacion.this, Firma_Cliente.class);
                String id = mDatabaseReference.child("Contrato_Servicio").getKey();
                Query query = mDatabaseReference.orderByChild("Contrato_Servicio").equalTo(id);

                if(ine1.getDrawable() == null || ine2.getDrawable() == null || firma.getDrawable() == null)
                {
                    Toast.makeText(Identificacion.this,
                            "Requiere completar los requisitos para validar su contrato",
                            Toast.LENGTH_LONG).show();
                }
                else
                {


                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                            {
                                Servicio servicio = dataSnapshot1.getValue(Servicio.class);
                                String prestador = servicio.getPrestador();
                                Log.e("nombre", prestador);
                                String prestatario = servicio.getPrestatario();
                                Log.e("abogado", prestatario);
                                String finalidad = servicio.getFinalidad();
                                Log.e("fin", finalidad);
                                String monto = String.valueOf(servicio.getMonto());
                                Log.e("monto", monto);
                                String facilidad = servicio.getFacilidad();
                                Log.e("facilidad", facilidad);
                                String pagos = String.valueOf(servicio.getN_pagos());
                                Log.e("pagos", pagos);
                                String primer = servicio.getFecha_primer_pago();
                                Log.e("primer", primer);

                                        intent.putExtra("Prestador", prestador);
                                        intent.putExtra("Prestatario", prestatario);
                                        intent.putExtra("finalidad",finalidad);
                                        intent.putExtra("monto",monto);
                                        intent.putExtra("facilidad",facilidad);
                                        intent.putExtra("pagos",pagos);
                                        intent.putExtra("primer",primer);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    intent.putExtra("day",dia);
                    intent.putExtra("month",month);
                    intent.putExtra("year",anio);
                    firma.buildDrawingCache();
                    Bitmap bmap = firma.getDrawingCache();
                    intent.putExtra("btmp", bmap);
                    startActivity(intent);
                    finish();
                }


            }
        });


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
            case R.id.ibtAddFront:
                openCamera();
                break;
            case R.id.ibtAddBack:
                openCameraTwo();
                break;

        }
    }


}
