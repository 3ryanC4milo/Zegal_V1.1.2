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

    EditText recibe, presta, finalidad, parcial;
    TextView v;
    Spinner spin;
    CalendarView calendarView;
    Button btnEnvia;
    String fec="", seleccion="";
    GregorianCalendar aux = new GregorianCalendar();

    String [] mes = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
            "septiembre", "octubre", "noviembre", "diciembre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato__servicio);


        v = findViewById(R.id.tv_seven);
        recibe = findViewById(R.id.et_persona_recibe);
        presta = findViewById(R.id.et_persona_presta);
        finalidad = findViewById(R.id.et_finalidad);
        parcial = findViewById(R.id.et_pagos);
        parcial.setEnabled(false);
        parcial.setVisibility(View.INVISIBLE);
        v.setVisibility(View.INVISIBLE);

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
                        v.setVisibility(View.INVISIBLE);
                        parcial.setEnabled(false);
                        parcial.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        seleccion = "Pago único";
                        v.setVisibility(View.VISIBLE);
                        parcial.setVisibility(View.VISIBLE);
                        parcial.setEnabled(false);
                        parcial.setText("0");
                        break;
                    case 2:
                        seleccion = "Pagos parciales";
                        v.setVisibility(View.VISIBLE);
                        parcial.setVisibility(View.VISIBLE);
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(i1 < mes.length)
                {
                    fec = i2+" de "+mes[(i1)]+" de "+i;
                }
            }
        });

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

                String mail = getIntent().getStringExtra("mail");


                if(rec.isEmpty() || pre.isEmpty() || fin.isEmpty() || opc.equals("Seleccione"))
                {
                    Toast.makeText(
                            Contrato_Servicio.this,
                            "Por favor, complete los campos",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    startActivity(new Intent(Contrato_Servicio.this, Identificacion.class)
                            .putExtra("mail", mail)
                            .putExtra("recibe", rec)
                            .putExtra("prestador", pre)
                            .putExtra("finalidad", fin)
                            .putExtra("tipoPago", opc)
                            .putExtra("parcial", par)
                            .putExtra("fecPago", date));
                    Toast.makeText(Contrato_Servicio.this,
                            "Bienvenido "+mail +
                            "\nRecibe servicio :"+rec+
                                "\nPresta el servicio: "+pre+
                                "\nFinalidad: "+fin+
                                "\nFacilidad: "+opc+
                                "\nPagos a realizar: "+par+
                                "\nPrimer pago: "+date, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
