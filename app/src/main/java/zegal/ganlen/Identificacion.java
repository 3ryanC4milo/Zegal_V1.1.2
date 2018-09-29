package zegal.ganlen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.Manifest;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import zegal.ganlen.Config.Config;



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
    String in1, in2, in3, in4, in5, in6;
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
// --------------------------------------- Instanciar la clase Template --------------------------------------

    Template template;
    String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11;
    static final int REQUEST_STORAGE_PDF = 5;

//---------------------------------------------- Efectos para subir información ----------------------------------------
    ProgressDialog pProgress;

//------------------------------------------------- Variables para lienzos para firmar ------------------------------------

    View f1, f2;
    LinearLayout lien1, lien2;
    Bitmap bitm1, bitm2;

    Identificacionsignature firmaCliente, firmaAbogado;


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

        template = new Template(getApplicationContext());

        p1= "CONTRATO DE PRESTACIÓN DE SERVICIOS QUE SUSCRIBEN "+ getIntent().getStringExtra("prestador").toUpperCase()
                + " (EN ADELANTE EL PRESTADOR) Y "+ getIntent().getStringExtra("recibe").toUpperCase() +" (EN ADELANTE EL CLIENTE) CONFORME A LAS SIGUIENTES CLÁUSULAS: ";

        p2 = "CLÁUSULAS";

        p3 = "PRIMERA. OBJETO. El objeto del contrato es la prestación de los siguientes servicios por el Prestador al Cliente: ";

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

        p11 = "NOVENA. LEYES Y TRIBUNALES. Para la interpretación y cumplimiento del presente contrato el mismo se regirá por las leyes aplicables en la Ciudad de México y en caso de controversia las partes se someten a los tribunales competentes de la Ciudad de México, renunciando a cualquier otro fuero por razón de sus domicilios presentes o futuros o por cualquier otra circunstancia. \n" +
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

//-------------------------------------- Firma ---------------------------------------------------------------

        lien1 = findViewById(R.id.lyF1);
        lien2 = findViewById(R.id.lyF2);

        firmaCliente = new Identificacionsignature(lien1.getContext().getApplicationContext(),null);
        firmaCliente.setBackgroundColor(Color.WHITE);
        firmaAbogado = new Identificacionsignature(lien2.getContext().getApplicationContext(),null);
        firmaCliente.setBackgroundColor(Color.WHITE);

        lien1.addView(firmaCliente, ViewGroup.LayoutParams.MATCH_PARENT, 500);
        lien2.addView(firmaAbogado, ViewGroup.LayoutParams.MATCH_PARENT, 500);

        f1 = lien1;
        f2 = lien2;

        pProgress = new ProgressDialog(getApplicationContext());

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

                //firmaCliente.saveCliente(f1,bitm1,lien1);
                //firmaAbogado.saveAbogado(f2,bitm2,lien2);

                if(Build.VERSION.SDK_INT>=23)
                {
                    crearPdf();
                }
                else
                    pdfPDF();

                ///CargarDatosServicio(rec, pre, fin, mon, opc, par, date, gen, in1, in2, in3, in4);
                //procesarDatos();
            }


        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void pdfPDF() {
        template.abrePDF();
        template.agregaData("Contrato de Servicios", getIntent().getStringExtra("finalidad"), "ZegalAPP");
        template.agregarParrafo(p1);
        template.addClausula(p2);
        template.agregarParrafo(p3);
        template.agregarParrafo(getIntent().getStringExtra("finalidad").toUpperCase());
        template.agregarParrafo(p4);
        template.agregarParrafo(p5);
        template.agregarParrafo(p6);
        template.agregarParrafo(p7);
        template.agregarParrafo(p8);
        template.agregarParrafo(p9);
        template.agregarParrafo(p10);
        template.agregarParrafo(p11);
        template.crearFirmero(new String[]{"EL ABOGADO","","EL CLIENTE"});
        template.crearFirmero2(new String[]{getIntent().getStringExtra("prestador").toUpperCase(),"",getIntent().getStringExtra("recibe").toUpperCase()});
        template.cierraPDF();
        template.verPDF();
    }


    //----------------------------- Funciones para tomar las respectivas fotos -----------------------
    private void openCamera () {

        if(Build.VERSION.SDK_INT >= 23)
        {
            // check for camera permission
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, Manifest.permission.CAMERA);

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
                        new String[]{Manifest.permission.CAMERA},
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
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, Manifest.permission.CAMERA);

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
                        new String[]{Manifest.permission.CAMERA},
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
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, Manifest.permission.CAMERA);

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
                        new String[]{Manifest.permission.CAMERA},
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
            int permissionCheck = ContextCompat.checkSelfPermission(Identificacion.this, Manifest.permission.CAMERA);

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
                        new String[]{Manifest.permission.CAMERA},
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

        else if(requestCode == REQUEST_STORAGE_PDF)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pdfPDF();
            }
            else
            {
                Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
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
                    try
                    {
                        String detalle = confirmation.toJSONObject().toString(4);
                    }
                    catch (JSONException e)
                    {
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

            //pProgress.setMessage("Subiendo foto a servidor");
            //pProgress.show();

            final StorageReference ine1 = mStorageReference.child("Identificaciones/cliente_"+pic_name+"_front.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] datas = baos.toByteArray();

            ine1.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ine1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            in1 = uri.toString();
                        }
                    });


                    //pProgress.dismiss();

                    Glide.with(Identificacion.this)
                            .using(new FirebaseImageLoader())
                            .load(ine1)
                            .into(ine1R);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //pProgress.setMessage("Cargando " + (int) progress + "%");
                    //pProgress.show();
                }
            });
        }
        else if(requestCode == REQUEST_IMAGE_CAP_TWO && resultCode == RESULT_OK) {



            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            final StorageReference ine2 = mStorageReference.child("Identificaciones/cliente_"+pic_name+"_back.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] datas = baos.toByteArray();

            ine2.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ine2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            in2 = uri.toString();
                        }
                    });

                    //pProgress.dismiss();

                    Glide.with(Identificacion.this)
                            .using(new FirebaseImageLoader())
                            .load(ine2)
                            .into(ine2R);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //pProgress.setMessage("Cargando " + (int) progress + "%");
                    //pProgress.show();
                }
            });

        }
        else if (requestCode == REQUEST_IMAGE_CAP_TRES && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            final StorageReference ine3 = mStorageReference.child("Identificaciones/abogado_"+pic_name+"_front.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] datas = baos.toByteArray();

            ine3.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ine3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            in3 = uri.toString();
                        }
                    });

                    //pProgress.dismiss();

                    Glide.with(Identificacion.this)
                            .using(new FirebaseImageLoader())
                            .load(ine3)
                            .into(ine1P);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //pProgress.setMessage("Cargando " + (int) progress + "%");
                    //pProgress.show();
                }
            });
        }
        else if(requestCode == REQUEST_IMAGE_CAP_CUATRO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            final StorageReference ine4 = mStorageReference.child("Identificaciones/abogado_"+pic_name+"_back.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] datas = baos.toByteArray();

            ine4.putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ine4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            in4 = uri.toString();
                        }
                    });

                    //pProgress.dismiss();

                    Glide.with(Identificacion.this)
                            .using(new FirebaseImageLoader())
                            .load(ine4)
                            .into(ine2P);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //pProgress.setMessage("Cargando " + (int) progress + "%");
                    //pProgress.show();
                }
            });
        }
    }





    private void CargarDatosServicio(String rec, String pre, String fin, double mon, String opc, int par, String date, String gen, String fotoIneR1, String fotoIneR2, String fotoIneP1, String fotoIneP2) {
        Servicio servicio;
        String id = mDatabaseReference.push().getKey();
        servicio = new Servicio(gen, rec, pre, fin, mon, opc, par, date);
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

//---------------------------- Se crea clase firma para darle funcionalidad a los lienzos ----------------------------------------------

    public class Identificacionsignature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public Identificacionsignature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void saveCliente(View v, Bitmap bitmap, LinearLayout mContent) {

            pProgress.setMessage("Subiendo firma a servidor");
            pProgress.show();
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
                // Output the file
                v.draw(canvas);

                final StorageReference firma = mStorageReference.child("Firmas/ "+"ServiciosFirma"+ pic_name+".png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                byte[] drawing = baos.toByteArray();

                firma.putBytes(drawing).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        firma.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pProgress.dismiss();
                                in5 = uri.toString();
                                Toast.makeText(getApplicationContext(), "La firma del cliente fue añadida al Servidor", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        pProgress.setMessage("Cargando " + (int) progress + "%");
                    }
                });
        }

        public void saveAbogado(View v, Bitmap bitmap, LinearLayout mContent) {

            pProgress.setMessage("Subiendo firma a servidor");
            pProgress.show();
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            // Output the file
            v.draw(canvas);

            final StorageReference firma2 = mStorageReference.child("Firmas/ "+"ServiciosFirma"+ pic_name+".png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] drawing = baos.toByteArray();

            firma2.putBytes(drawing).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    firma2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pProgress.dismiss();
                            in6 = uri.toString();
                            Toast.makeText(getApplicationContext(), "La firma del abogado fue añadida al Servidor", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    pProgress.setMessage("Cargando " + (int) progress + "%");
                }
            });
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            //mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------

    public boolean crearPdf()
    {
        if(Build.VERSION.SDK_INT >=23)
        {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                pdfPDF();
                return true;
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE_PDF);
                return false;
            }
        }
        else
        {
            pdfPDF();
            return true;
        }
    }
}


