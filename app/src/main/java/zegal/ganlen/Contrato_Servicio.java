package zegal.ganlen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import zegal.ganlen.Config.Config;

public class Contrato_Servicio extends AppCompatActivity {

    //-------------------------Variables de paypal-------------------------------------------------------------------

    private static final int PAYPAL_REQUEST_CODE = 7171;
    //Esta variable es para jalar la cuenta de prueba para testeo, se requerira cambiar valores despues de las respectivas pruebas
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    EditText recibe, presta, finalidad, parcial;
    TextView v;
    Spinner spin;
    CalendarView calendarView;
    Button btnEnvia, canc;
    String fec="", seleccion="";
    GregorianCalendar aux = new GregorianCalendar();

    String [] mes = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
            "septiembre", "octubre", "noviembre", "diciembre"};

    String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11;
    static final int REQUEST_STORAGE_PDF = 5;

    DatabaseReference mDatabaseReference;

    Template template;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato__servicio);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        recibe = findViewById(R.id.et_persona_recibe);
        presta = findViewById(R.id.et_persona_presta);
        finalidad = findViewById(R.id.et_finalidad);
        parcial = findViewById(R.id.et_pagos);


        spin = findViewById(R.id.sp_pago);
        String[] opcion_pago ={"Seleccione", "Pago único", "Pagos parciales"};
        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcion_pago));

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        seleccion = "Seleccione";
                        parcial.setEnabled(false);
                        parcial.setText("0");
                        break;
                    case 1:
                        seleccion = "Pago único";
                        parcial.setEnabled(false);
                        parcial.setText("0");
                        break;
                    case 2:
                        seleccion = "Pagos parciales";
                        parcial.setEnabled(true);
                        parcial.setText("");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {



            }
        });



        calendarView = findViewById(R.id.calendarView);
        btnEnvia = findViewById(R.id.btn_enviar_main_con);
        canc = findViewById(R.id.btn_cancelar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(i1 < mes.length)
                {
                    fec = i2+" de "+mes[(i1)]+" de "+i;
                }
            }
        });

        template = new Template(getApplicationContext());


        btnEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rec = recibe.getText().toString();
                String pre = presta.getText().toString();
                String fin = finalidad.getText().toString();
                String opc = seleccion;
                String par = parcial.getText().toString();
                String date;

                if(fec.isEmpty())
                {
                    date = String.valueOf(aux.get(Calendar.DATE))+" de "+mes[aux.get(Calendar.MONTH)]+" de "+String.valueOf(aux.get(Calendar.YEAR));
                }
                else
                {
                    date = fec;
                }
                p1= "CONTRATO DE PRESTACIÓN DE SERVICIOS QUE SUSCRIBEN "+ pre.toUpperCase()
                        + " (EN ADELANTE EL PRESTADOR) Y "+ rec.toUpperCase() +" (EN ADELANTE EL CLIENTE) CONFORME A LAS SIGUIENTES CLÁUSULAS: ";

                p2 = "CLÁUSULAS";

                p3 = "PRIMERA. OBJETO. El objeto del contrato es la prestación de los siguientes servicios por el Prestador al Cliente: ";


                p4 = "SEGUNDA. CONTRAPRESTACIÓN. Por la prestación de los servicios el Cliente pagará al Prestador la can-tidad de $ 2000.00 ("+MontoLetra.cantidadConLetra("2000")+" pesos) M.N. más IVA/ misma que será pagadera contra entrega de los servicios correspondientes. " +
                        "/ misma que será pagadera mediante un solo pago que deberá ser cubierto el "+date +"." +
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
                        String.valueOf(aux.get(Calendar.DATE))+" DÍAS DEL MES DE "+mes[aux.get(Calendar.MONTH)].toUpperCase()+" DEL AÑO "+String.valueOf(aux.get(Calendar.YEAR))+".";


                if(rec.isEmpty() || pre.isEmpty() || fin.isEmpty() || opc.equals("Seleccione"))
                {
                    Toast.makeText(
                            Contrato_Servicio.this,
                            "Por favor, complete los campos",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    CargarDatosServicio(rec, pre, fin, 2000, opc, Integer.parseInt(par), date, String.valueOf(aux.get(Calendar.DATE))+" de "+mes[aux.get(Calendar.MONTH)]+" de "+String.valueOf(aux.get(Calendar.YEAR)));
                    procesarDatos();

                }
            }
        });

       canc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(Contrato_Servicio.this, Principal_zegal.class));
               finish();
           }
       });
    }

    private void procesarDatos() {

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf("2000")),"MXN", "Contrato de servicios", PayPalPayment.PAYMENT_INTENT_SALE);

        //Envia parametros
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

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

                    if(Build.VERSION.SDK_INT>=23)
                    {
                        crearPdf();
                    }
                    else
                    {
                        pdfPDF();
                    }
                    /*try
                    {
                        String detalle = confirmation.toJSONObject().toString(4);
                        crearPdf();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }*/
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
    }

    private void CargarDatosServicio(String rec, String pre, String fin, double mon, String opc, int par, String date, String gen) {
        Servicio servicio;
        String id = mDatabaseReference.push().getKey();
        servicio = new Servicio(gen, rec, pre, fin, mon, opc, par, date);
        mDatabaseReference.child("Contrato_Servicio").child(id).setValue(servicio);
        Toast.makeText(getApplicationContext(),
                "Datos capturados exitosamente",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_STORAGE_PDF)
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

    private void pdfPDF() {
        template.abrePDF();
        template.agregaData("Contrato de Servicios", finalidad.getText().toString(), "ZegalAPP");
        template.agregarParrafo(p1);
        template.addClausula(p2);
        template.agregarParrafo(p3);
        template.agregarParrafo(finalidad.getText().toString().toUpperCase());
        template.agregarParrafo(p4);
        template.agregarParrafo(p5);
        template.agregarParrafo(p6);
        template.agregarParrafo(p7);
        template.agregarParrafo(p8);
        template.agregarParrafo(p9);
        template.agregarParrafo(p10);
        template.agregarParrafo(p11);
        template.crearFirmero(new String[]{"EL ABOGADO","","EL CLIENTE"});
        template.crearFirmero2(new String[]{presta.getText().toString().toUpperCase(),"",recibe.getText().toString().toUpperCase()});
        template.cierraPDF();
        template.verPDF();
    }

    public boolean crearPdf()
    {
        if(Build.VERSION.SDK_INT >=23)
        {
            if(getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
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
