package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Forma_Pago extends AppCompatActivity {

    TextView txttrans,txtcliente,txtconc,txtmon,txtstat;

    Button btnfin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma__pago);

        //Referencias a paypal

        txttrans = findViewById(R.id.tvTrans);
        txtcliente = findViewById(R.id.tvCliente);
        txtconc = findViewById(R.id.tvConcepto);
        txtmon = findViewById(R.id.tvMonto);
        txtstat = findViewById(R.id.tvStatus);

        btnfin = findViewById(R.id.btnSalirDetalle);

        btnfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forma_Pago.this, Identificacion.class));
                finish();
            }
        });

            //Recibe los datos del activity anterior
        Intent intent = getIntent();
        txtcliente.setText("Cliente: "+intent.getStringExtra("Nombre"));
        txtconc.setText("Concepto de pago: "+intent.getStringExtra("Concepto"));
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Detalle"));
            verTodo(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //Funcion que manda los elementos del json a los  textview
    private void verTodo(JSONObject response, String paymentAmount) {
        try {
            txttrans.setText("Transacción PayPal: \n"+response.getString("id"));
            txtstat.setText("Status de la transacción: "+response.getString("state"));
            txtmon.setText(response.getString("Total: "+String.format("$"+paymentAmount)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
