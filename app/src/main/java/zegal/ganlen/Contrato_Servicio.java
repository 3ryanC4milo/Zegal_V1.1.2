package zegal.ganlen;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import zegal.ganlen.Config.Config;

public class Contrato_Servicio extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    //Esta variable es para jalar la cuenta de prueba para testeo, se requerira cambiar valores despues de las respectivas pruebas
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    EditText recibe, presta, finalidad, monto, parcial;
    Spinner spin;
    CalendarView calendarView;
    DatabaseReference mDatabaseReference;
    Button btnEnvia;
    String cantidad ="";
    String fec="", seleccion="";

    String [] mes = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
            "septiembre", "octubre", "noviembre", "diciembre"};


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato__servicio);
        //Iniciar servicio PayPay

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

       mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        recibe = findViewById(R.id.et_persona_recibe);
        presta = findViewById(R.id.et_persona_presta);
        finalidad = findViewById(R.id.et_finalidad);
        monto = findViewById(R.id.et_monto);
        //monto.addTextChangedListener(new NumberTextWatcher(monto,"#,###.00"));
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(i1 < mes.length)
                {
                    fec = i2+" de "+mes[(i1)]+" de "+i;
                }
            }
        });
        btnEnvia = findViewById(R.id.btn_enviar_main_con);





       btnEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rec = recibe.getText().toString();
                String pre = presta.getText().toString();
                String fin = finalidad.getText().toString();
                double mon = Double.parseDouble(monto.getText().toString());
                String opc = seleccion;
                int par = Integer.parseInt(parcial.getText().toString());
                String date = fec;


                if (rec.isEmpty() || pre.isEmpty() || fin.isEmpty() || opc.equals("Seleccione") || parcial == null)
                {
                    Toast.makeText(getApplicationContext(),
                            "Debes de llenar todos los campos",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    CargaDatosServicios(rec, pre, fin, mon, opc, par, date);
                    procesarDatos();
                    //finish();
                }
            }
        });
    }

    private void CargaDatosServicios(String rec, String pre, String fin, double mon, String opc, int par, String date) {
        Servicio servicio;
        String id = mDatabaseReference.push().getKey();

        servicio = new Servicio(rec, pre, fin, mon, opc, par, date);
        mDatabaseReference.child("Contrato_Servicio").child(id).setValue(servicio);
        Toast.makeText(getApplicationContext(),
                "Datos capturados exitosamente",
                Toast.LENGTH_LONG).show();
    }

   private void procesarDatos() {

        //Revibe el monto del edittext
        cantidad = monto.getText().toString();



        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(cantidad)),"MXN", "Contrato de servicios", PayPalPayment.PAYMENT_INTENT_SALE);

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
                    try {
                        String paymentDetalle = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,Forma_Pago.class).putExtra("Detalle", paymentDetalle)
                                        .putExtra("PaymentAmount", monto.getText().toString())
                                        .putExtra("Nombre",recibe.getText().toString())
                                        .putExtra("Concepto", "Contrato de Servicios")
                                );
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
    }
}
