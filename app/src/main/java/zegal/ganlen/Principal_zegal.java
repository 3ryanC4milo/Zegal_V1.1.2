package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
                startActivity(new Intent(Principal_zegal.this,con_alianza.class));
                break;
            case R.id.btn_laboral:
                startActivity(new Intent(Principal_zegal.this,Con_laboral.class));
                break;
            case R.id.btn_software:
                startActivity(new Intent(Principal_zegal.this,Con_software.class));
                break;
            case R.id.btn_servicios:
                startActivity(new Intent(Principal_zegal.this,Contrato_Servicio.class));
                break;
        }
    }
}
