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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.da;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import zegal.ganlen.Config.Config;

import static android.Manifest.permission.CAMERA;

public class Identificacion extends AppCompatActivity {

    //-------------------------Variables de paypal-------------------------------------------------------------------

    private static final int PAYPAL_REQUEST_CODE = 7171;
    //Esta variable es para jalar la cuenta de prueba para testeo, se requerira cambiar valores despues de las respectivas pruebas
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);


    //------------------------------------BANDERAS PARA TOMAR FOTOGRAFIAS----------------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAP_TWO = 2;
    static final int REQUEST_IMAGE_CAP_TRES = 3;
    static final int REQUEST_IMAGE_CAP_CUATRO = 4;
//------------------------------------------ Variables-------------------------------------------------
    private static final String TAG =Identificacion.class.getSimpleName() ;
    String [] mes = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
            "septiembre", "octubre", "noviembre", "diciembre"};
    GregorianCalendar fecha = new GregorianCalendar();
    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;
    ImageView ine1R, ine2R, ine1P, ine2P;
    Button cont, reg;
    String in1, in2, in3, in4;
    Uri fotoUri1, fotoUri2, fotoUri3, fotoUri4;

    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
// --------------------------------------- Instanciar la clase Template --------------------------------------

    Template template;
    String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11;


//---------------------------Desturctor del servicio de Paypal ------------------------

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    //---------------------------Funcion principal -------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificacion);

        //Iniciar servicio PayPay

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

//----------------------------Inicializar objeto del template--------------------------------------
        p1= "CONTRATO DE PRESTACIÓN DE SERVICIOS QUE SUSCRIBEN "+ getIntent().getStringExtra("prestador")
                + " (EN ADELANTE EL PRESTADOR) Y "+ getIntent().getStringExtra("recibe") +" (EN ADELANTE EL CLIENTE) CONFORME A LAS SIGUIENTES CLÁUSULAS: ";

        p2 = "CLÁUSULAS";

        p3 = "PRIMERA. OBJETO. El objeto del contrato es la prestación de los siguientes servicios por el Prestador al Cliente: \n"+ getIntent().getStringExtra("finalidad");

        p4 = "SEGUNDA. CONTRAPRESTACIÓN. Por la prestación de los servicios el Cliente pagará al Prestador la can-tidad de $ 2000.00 ("+MontoLetra.cantidadConLetra("2000")+" pesos) M.N. más IVA/ misma que será pagadera contra entrega de los servicios correspondientes. " +
                "/ misma que será pagadera mediante un solo pago que deberá ser cubierto el "+getIntent().getStringExtra("fecPago") +"." +
                "\n La cantidad(es) señalada(s) en esta cláusula podrá ser cubierta(s) en cualquier forma de pago.";

        p5 = "TERCERA. VIGENCIA. El plazo del contrato será indefinido, por la naturaleza de los servicios prestados.\n" +
                "Las partes acuerdan que el contrato terminará su vigencia de forma natural mediante la prestación de los servicios objeto de este contrato.\n";

        p6 = "CUARTA. GASTOS. Ambas partes acuerdan que todos los gastos generados por la prestación de los ser-vicios descritos en la cláusula Primera serán a cargo del Cliente.";

        p7 ="QUINTA. LIBERACIÓN DE RESPONSABILIDAD. El Cliente reconoce que el Prestador obrará de buena fe en la prestación de los servicios, con la capacidad técnica y experiencia necesaria para cumplir " +
                "con el objeto del contrato, por lo cual en este acto lo libera de cualquier responsabilidad civil, mercantil, penal o administrativa derivada de la prestación de los servicios prestados. \n" +
                "En caso de que ocurra un sismo, incendio, terremoto, inundación o cualquier otro fenómeno natural que impida que se presten los servicios, el Cliente no se reserva ninguna acción en contra del Prestador " +
                "o su personal y se obliga a sacarlos en paz y a salvo de cualquier procedimiento judicial o extrajudicial en cual-quier materia jurídica, así como a pagar al Prestador cualquier gasto relacionado con ese procedimiento, " +
                "incluidos los honorarios de abogados.";

        p8 = "SEXTA. DATOS PERSONALES. Los datos personales del Cliente serán tratados conforme a la Ley Federal de Protección de Datos Personales en Posesión de Particulares y el Aviso de Privacidad del Prestador.";

        p9 = "SÉPTIMA. CONFIDENCIALIDAD. El Prestador guardará confidencialidad de la información que reciba del Cliente en relación con los servicios objeto del contrato.\n"+
                "El Prestador deberá abstenerse de comunicar por cualquier medio a terceros la información que conozca en virtud de su participación en los proyectos en los que sea parte.\n"+
                "En caso de que el Prestador incumpla su obligación de confidencialidad, deberá pagar los daños y perjui-cios causado al Cliente.";

        p10 = "OCTAVA. DIVISIBILIDAD. En caso de que alguna de las cláusulas del contrato sea anulada mediante orden judicial, las demás cláusulas continuarán siendo válidas para las partes.";

        p11 = "NOVENA. LEYES Y TRIBUNALES. Para la interpretación y cumplimiento del presente contrato el mismo se regirá por las leyes aplicables en la Ciudad de México y en caso de controversia las partes se someten a los tribunales competentes de la Ciudad de México, " +
                "renunciando a cualquier otro fuero por razón de sus domicilios presentes o futuros o por cualquier otra circunstancia. \n" +
                "El presente contrato se firma libre de dolo, error, mala fe o cualquier otro vicio que pueda afectar el con-sentimiento de las partes, en la Ciudad de México a los "+
                String.valueOf(fecha.get(Calendar.DATE))+" DÍAS DEL MES DE "+mes[fecha.get(Calendar.MONTH)].toUpperCase()+" DEL AÑO "+String.valueOf(fecha.get(Calendar.YEAR))+".";



//----------------------Referencias a Firebase --------------------------------------------------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        ine1R = findViewById(R.id.imvIneFront);
        ine2R = findViewById(R.id.imvIneBack);
        ine1P = findViewById(R.id.imvIneFront2);
        ine2P = findViewById(R.id.imvIneBack2);
        reg = findViewById(R.id.btn_return);
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

                template = new Template(getApplicationContext());
                template.abrePDF();
                template.agregaData("Contrato de Servicios", getIntent().getStringExtra("finalidad"), "ZegalAPP");
                template.addPrimer(p1);
                template.agregaClausula(p2);
                template.addTexto(p3);
                template.addTexto(p4);
                template.addTexto(p5);
                template.addTexto(p6);
                template.addTexto(p7);
                template.addTexto(p8);
                template.addTexto(p9);
                template.addTexto(p10);
                template.addTexto(p11);
                template.crearFirmero(new String[]{"EL ABOGADO", "EL CLIENTE"});
                template.TerminarFirmero(new String[]{getIntent().getStringExtra("prestador"), getIntent().getStringExtra("recibe")});
                template.cierraPDF();

                CargarDatosServicio(rec, pre, fin, mon, opc, par, date, gen, in1, in2, in3, in4);
                procesarDatos();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Identificacion.this, Contrato_Servicio.class));
                finish();
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
                    template.verPDF();
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

            StorageReference ineref = mStorageReference.child("Identificaciones/ "+pic_name+".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,90,baos);
            byte[] datas = baos.toByteArray();


            ineref.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(),"Subida con exito",Toast.LENGTH_LONG);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Hubo un error",Toast.LENGTH_LONG);
                }
            });
            in1 = ineref.getDownloadUrl().toString();
            ine1R.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_IMAGE_CAP_TWO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            StorageReference ineref = mStorageReference.child("Identificaciones/ "+pic_name+".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,90,baos);
            byte[] datas = baos.toByteArray();


            ineref.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(),"Subida con exito",Toast.LENGTH_LONG);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Hubo un error",Toast.LENGTH_LONG);
                }
            });
            in2 = ineref.getDownloadUrl().toString();
            ine2R.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQUEST_IMAGE_CAP_TRES && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            StorageReference ineref = mStorageReference.child("Identificaciones/ "+pic_name+".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,90,baos);
            byte[] datas = baos.toByteArray();


            ineref.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(),"Subida con exito",Toast.LENGTH_LONG);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Hubo un error",Toast.LENGTH_LONG);
                }
            });

            in3 = ineref.getDownloadUrl().toString();
            ine1P.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_IMAGE_CAP_CUATRO && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            StorageReference ineref = mStorageReference.child("Identificaciones/ "+pic_name+".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,90,baos);
            byte[] datas = baos.toByteArray();


            ineref.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(),"Subida con exito",Toast.LENGTH_LONG);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Hubo un error",Toast.LENGTH_LONG);
                }
            });
            in4 = ineref.getDownloadUrl().toString();

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
                openCameraTres();
                break;
            case R.id.ibtAddBack2:
                openCameraCuatro();
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
