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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.q;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import zegal.ganlen.Config.Config;

import static android.Manifest.permission.CAMERA;

public class Identificacion extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    //Esta variable es para jalar la cuenta de prueba para testeo, se requerira cambiar valores despues de las respectivas pruebas
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);


    //BANDERAS PARA TOMAR FOTOGRAFIAS
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAP_TWO = 2;
    static final int REQUEST_IMAGE_CAP_TRES = 3;
    static final int REQUEST_IMAGE_CAP_CUATRO = 4;


    private static final String TAG =Identificacion.class.getSimpleName() ;
    String [] mes = {"ENERO","fEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO",
                        "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    GregorianCalendar fecha = new GregorianCalendar();
    DatabaseReference mDatabaseReference;
    ImageView ine1R, ine2R, ine1P, ine2P;
    Button cont;
    //String txtgen="", txtcliente="", txtab="", txtfin="", txtfac="", txtpar = "", txtfecpago = "";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificacion);

        //Iniciar servicio PayPay

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        ine1R = findViewById(R.id.imvIneFront);
        ine2R = findViewById(R.id.imvIneBack);
        ine1P = findViewById(R.id.imvIneFront2);
        ine2P = findViewById(R.id.imvIneBack2);

        cont = findViewById(R.id.btnContrato);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String dia = String.valueOf(fecha.get(Calendar.DATE));
                String month = mes[fecha.get(Calendar.MONTH)];
                String anio = String.valueOf(fecha.get(Calendar.YEAR));

                String rec = getIntent().getStringExtra("recibe");
                String pre = getIntent().getStringExtra("prestador");
                String fin = getIntent().getStringExtra("finalidad");
                double mon = 2000;
                String opc = getIntent().getStringExtra("tipoPago");
                String p = getIntent().getStringExtra("parcial");
                int par = Integer.parseInt(p);
                String date = getIntent().getStringExtra("fecPago");
                String gen = dia + " de " + month + " de " + anio;

                ine1R.buildDrawingCache();
                Bitmap b1 = ine1R.getDrawingCache();
                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                b1.compress(Bitmap.CompressFormat.PNG, 90 , stream1);
                byte[] ine1 = stream1.toByteArray();
                String fotoIneR1 = new String(ine1);

                ine2R.buildDrawingCache();
                Bitmap b2 = ine2R.getDrawingCache();
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                b2.compress(Bitmap.CompressFormat.PNG, 90 , stream2);
                byte[] ine2 = stream1.toByteArray();
                String fotoIneR2 = new String(ine2);

                ine1P.buildDrawingCache();
                Bitmap b3 = ine1P.getDrawingCache();
                ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                b3.compress(Bitmap.CompressFormat.PNG, 90 , stream3);
                byte[] ine3 = stream1.toByteArray();
                String fotoIneP1 = new String(ine3);

                ine2P.buildDrawingCache();
                Bitmap b4 = ine2P.getDrawingCache();
                ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
                b4.compress(Bitmap.CompressFormat.PNG, 90 , stream4);
                byte[] ine4 = stream1.toByteArray();
                String fotoIneP2 = new String(ine4);

                CargarDatosServicio(rec, pre, fin, mon, opc, par, date, gen, fotoIneR1, fotoIneR2, fotoIneP1, fotoIneP2);

               /* mDatabaseReference.child("Contrato_Servicio").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Servicio serv = dataSnapshot.getValue(Servicio.class);

                        txtgen = serv.getFecGenerado();
                        txtcliente = serv.getPrestatario();
                        txtab = serv.getPrestador();
                        txtfin = serv.getFinalidad();
                        txtfac = serv.getFacilidad();
                        txtpar = String.valueOf(serv.getN_pagos());
                        txtfecpago = serv.getFecha_primer_pago();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                procesarDatos();
            }
        });
    }
    //----------------------------- Funciones para tomar las respectivas fotos -----------------------
    private void openCamera () {

        if(Build.VERSION.SDK_INT >= 23)
        {
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
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void openCameraTwo() {
        if(Build.VERSION.SDK_INT >= 23)
        {
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
                        REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_TWO);
            }
        }
    }

    private void openCameraTres() {

        if(Build.VERSION.SDK_INT >= 23)
        {
            // check for camera permission
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, CAMERA);

            // do we have camera permission?
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                // we have camera permission, open System camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_TRES);
                }
            }
            else {

                // we don't have it, request camera permission from system
                ActivityCompat.requestPermissions(this,
                        new String[]{CAMERA},
                        REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_TRES);
            }
        }
    }

    private void openCameraCuatro() {
        if(Build.VERSION.SDK_INT >= 23)
        {
            // check for camera permission
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, CAMERA);

            // do we have camera permission?
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                // we have camera permission, open System camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_CUATRO);
                }
            }
            else {

                // we don't have it, request camera permission from system
                ActivityCompat.requestPermissions(this,
                        new String[]{CAMERA},
                        REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAP_CUATRO);
            }
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

        else if (requestCode == REQUEST_IMAGE_CAP_TRES)
        {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // camera permission was granted
                openCameraTres();

            } else {

                Log.d(TAG, "permissions not accepted");
            }
        }

        else if (requestCode == REQUEST_IMAGE_CAP_CUATRO)
        {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // camera permission was granted
                openCameraCuatro();

            } else {

                Log.d(TAG, "permissions not accepted");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PAYPAL_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null)
                {
                    try {
                        ine1R.buildDrawingCache();
                        Bitmap b1 = ine1R.getDrawingCache();
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        b1.compress(Bitmap.CompressFormat.PNG, 90 , stream1);
                        byte[] ine1 = stream1.toByteArray();
                        String fotoIneR1 = Base64.encodeToString(ine1, Base64.DEFAULT);

                        ine2R.buildDrawingCache();
                        Bitmap b2 = ine2R.getDrawingCache();
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        b2.compress(Bitmap.CompressFormat.PNG, 90 , stream2);
                        byte[] ine2 = stream1.toByteArray();
                        String fotoIneR2 = Base64.encodeToString(ine2, Base64.DEFAULT);

                        /*ine1P.buildDrawingCache();
                        Bitmap b3 = ine1P.getDrawingCache();
                        ine2P.buildDrawingCache();
                        Bitmap b4 = ine2P.getDrawingCache();*/

                        String mail = getIntent().getStringExtra("mail");
                        String dia = String.valueOf(fecha.get(Calendar.DATE));
                        String month = mes[fecha.get(Calendar.MONTH)];
                        String anio = String.valueOf(fecha.get(Calendar.YEAR));
                        String gen = dia + " de " + month + " de " + anio;

                        String paymentDetalle = confirmation.toJSONObject().toString(4);


                        startActivity(new Intent(this,Forma_Pago.class).putExtra("Detalle", paymentDetalle)
                                .putExtra("PaymentAmount", "2000")
                                .putExtra("Concepto", "Contrato de Servicios")
                                .putExtra("mail", mail)
                                .putExtra("recibe", getIntent().getStringExtra("recibe"))
                                .putExtra("presta", getIntent().getStringExtra("prestador"))
                                .putExtra("gen", gen)
                                .putExtra("finalidad", getIntent().getStringExtra("finalidad"))
                                .putExtra("parcial",  getIntent().getStringExtra("parcial"))
                                .putExtra("facilidad",  getIntent().getStringExtra("tipoPago"))
                                .putExtra("primer",  getIntent().getStringExtra("fecPago"))
                        );
                        Toast.makeText(this, "Su transacción se relizó de manera exitosa.", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            else if(resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(this,"Operacion cancelada",Toast.LENGTH_LONG).show();
            }
            else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Toast.makeText(this,"Operacion invalida",Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine1R.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_IMAGE_CAP_TWO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine2R.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQUEST_IMAGE_CAP_TRES && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine1P.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_IMAGE_CAP_CUATRO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ine2P.setImageBitmap(imageBitmap);
        }
    }

    private void CargarDatosServicio(String rec, String pre, String fin, double mon, String opc, int par, String date, String gen, String fotoIneR1, String fotoIneR2, String fotoIneP1, String fotoIneP2) {
        Servicio servicio;
        String id = mDatabaseReference.push().getKey();
        servicio = new Servicio(gen, rec, pre, fin, mon, opc, par, date, fotoIneR1, fotoIneR2, fotoIneP1, fotoIneP2);
        mDatabaseReference.child("Contrato_Servicio").child(id).setValue(servicio);
        Toast.makeText(getApplicationContext(),
                "Datos capturados exitosamente",
                Toast.LENGTH_LONG).show();
    }

    //--------------------------------- Evento click para tomar las fotografías ---------------------------------
    public void clickear(View view) {
        switch (view.getId())
        {
            case R.id.ibtAddFront:
                openCamera();
                break;
            case R.id.ibtAddBack:
                openCameraTwo();
                break;
            case R.id.ibtAddFront2:
                openCameraCuatro();
                break;
            case R.id.ibtAddBack2:
                openCameraTres();
                break;
        }
    }
//---------------------------- Funcion para procesar datos en paypal ------------------------------------------
    private void procesarDatos() {

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf("2000")),"MXN", "Contrato de servicios", PayPalPayment.PAYMENT_INTENT_SALE);

        //Envia parametros
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

}
