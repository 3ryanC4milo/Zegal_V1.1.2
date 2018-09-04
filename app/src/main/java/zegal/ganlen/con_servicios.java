package zegal.ganlen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class con_servicios extends AppCompatActivity {
    EditText recibe, presta, finalidad, monto, parcial;
    Spinner spin;
    CalendarView calendarView;
    DatabaseReference mDatabaseReference;
    Button btnEnvia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_servicios);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        recibe = findViewById(R.id.et_persona_recibe);
        presta = findViewById(R.id.et_persona_presta);
        finalidad = findViewById(R.id.et_finalidad);
        monto = findViewById(R.id.et_monto);

        spin = findViewById(R.id.sp_pago);
        String[] opcion_pago ={"Pago único", "Pagos parciales"};
        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcion_pago));


        parcial = findViewById(R.id.et_pagos);
        parcial.setEnabled(false);
        calendarView = findViewById(R.id.calendarView);
        btnEnvia = findViewById(R.id.btn_enviar_main_con);

        if(spin.getSelectedItem() == "Pagos parciales")
        {
            parcial.setEnabled(true);
        }
        else if(spin.getSelectedItem() == "Pago único")
        {
            parcial.setEnabled(false);
        }

        btnEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rec = recibe.getText().toString();
                String pre = presta.getText().toString();
                String fin = finalidad.getText().toString();
                double mon = Double.parseDouble(monto.getText().toString());
                String opc = spin.getSelectedItem().toString();
                String date = String.valueOf(calendarView.getDate());

                CargaDatosServicios(rec, pre, fin, mon, opc, date);
            }
        });

    }

    private void CargaDatosServicios(String rec, String pre, String fin, double mon, String opc, String date) {
        Map<String, Object> pedido_servicio = new HashMap<>();
        pedido_servicio.put("Receptor", rec);
        pedido_servicio.put("Prestador", pre);
        pedido_servicio.put("Finalidad", fin);
        pedido_servicio.put("Monto", mon);
        pedido_servicio.put("Forma_Pago", opc);

        if(opc.equals("Pago único"))
        {
            pedido_servicio.put("Pagos", 0);
        }
        else if (opc.equals("Pagos parciales"))
        {
            int par = Integer.parseInt(parcial.getText().toString());
            pedido_servicio.put("Pagos", par);
        }

        pedido_servicio.put("Fecha_Pago", date);

        mDatabaseReference.child("Contrato_Servicio").push().setValue(pedido_servicio);
    }
}
