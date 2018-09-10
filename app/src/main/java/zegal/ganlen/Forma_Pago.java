package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            //Recibe los datos del activity anterior
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Estado"));
            verTodo(jsonObject.getJSONObject("response"),intent.getStringExtra("Monto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Referencias a subir credenciales

        //Referencias a la firma
    }

    //Funcion que manda los elementos del json a los  textview
    private void verTodo(JSONObject response, String monto) {
        try {
            txttrans.setText(response.getString("id"));
            txtcliente.setText(response.getString("name"));
            txtconc.setText(response.getString("conc"));
            txtmon.setText(response.getString(monto));
            txtstat.setText(response.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
