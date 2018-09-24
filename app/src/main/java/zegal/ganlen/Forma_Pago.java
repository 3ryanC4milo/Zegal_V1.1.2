package zegal.ganlen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Forma_Pago extends AppCompatActivity {

    /*TextView txttrans,txtgen,txtcliente,txtab,txtconc,txtfin, txtmon,txtfac, txtpar, txtfecpago,txtstat, txtm;
    ImageView c1,c2,c3,c4;*/
    ImageView imagen;
    TextView gran, texto;
    Button btnfin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma__pago);



        //Referencias a paypal

       /* txttrans = findViewById(R.id.tvTrans);
        txtgen = findViewById(R.id.tvfec);
        txtcliente = findViewById(R.id.tvCliente);
        txtab = findViewById(R.id.tvAbogado);
        txtconc = findViewById(R.id.tvConcepto);
        txtfin = findViewById(R.id.tvFinal);
        txtmon = findViewById(R.id.tvMonto);
        txtfac = findViewById(R.id.tvFacilidad);
        txtpar = findViewById(R.id.tvParcial);
        txtfecpago = findViewById(R.id.tvPrim);
        txtstat = findViewById(R.id.tvStatus);
        txtm = findViewById(R.id.tvMail);

        c1 = findViewById(R.id.imv1);
        c2 = findViewById(R.id.imv2);
        c3 = findViewById(R.id.imv3);
        c4 = findViewById(R.id.imv4);*/

        btnfin = findViewById(R.id.btnSalirDetalle);

  /*      txtgen.setText(getIntent().getStringExtra("gen"));
        txtcliente.setText(getIntent().getStringExtra("recibe"));
        txtab.setText(getIntent().getStringExtra("presta"));
        txtfin.setText(getIntent().getStringExtra("finalidad"));
        txtfac.setText(getIntent().getStringExtra("facilidad"));
        txtpar.setText(getIntent().getStringExtra("parcial"));
        txtfecpago.setText(getIntent().getStringExtra("primer"));

        txtm.setText(getIntent().getStringExtra("mail"));

        String pathIne1 = getIntent().getStringExtra("ine1");
        final Bitmap u1 = retornaImagen(pathIne1);
        c2.setImageBitmap(u1);

        String pathIne2 = getIntent().getStringExtra("ine2");
        final Bitmap u2 = retornaImagen(pathIne2);
        c2.setImageBitmap(u2);
*/

        //c3.setImageBitmap(u3);
        //c4.setImageBitmap(u4);



        btnfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = getIntent().getStringExtra("mail");
                startActivity(new Intent(Forma_Pago.this, Principal_zegal.class)
                .putExtra("mail", mail));
                finish();
            }
        });

            //Recibe los datos del activity anterior
        /*Intent intent = getIntent();
        txtconc.setText(intent.getStringExtra("Concepto"));
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Detalle"));
            verTodo(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }

    //Funcion que manda los elementos del json a los  textview
    /*private void verTodo(JSONObject response, String paymentAmount) {
        try {
            txttrans.setText(response.getString("id"));
            txtstat.setText(response.getString("state"));
            txtmon.setText("$"+paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Bitmap retornaImagen(String img)
    {
        try{
            byte [] encode = Base64.decode(img, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encode, 0 , img.length());
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }

    }*/
}
