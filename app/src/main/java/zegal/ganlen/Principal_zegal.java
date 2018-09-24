package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Principal_zegal extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_zegal);
    }

    public void botonClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_alianza:
                Toast.makeText(this, "Proximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_laboral:
                Toast.makeText(this, "Proximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_software:
                Toast.makeText(this, "Proximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_servicios:
                String mail = getIntent().getStringExtra("mail");
                startActivity(new Intent(Principal_zegal.this,Contrato_Servicio.class).putExtra("mail",mail));
                Toast.makeText(this, "Bienvenido "+ mail, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
