package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Principal_zegal extends AppCompatActivity {

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_zegal);

        auth = FirebaseAuth.getInstance();
    }

    public void botonClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_alianza:
                Toast.makeText(this, "Próximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_laboral:
                Toast.makeText(this, "Próximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_software:
                Toast.makeText(this, "Próximamente", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_servicios:
                startActivity(new Intent(Principal_zegal.this,Contrato_Servicio.class));
                //Toast.makeText(this, "Bienvenido "+ mail, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_cerrar_sesion:
                auth.signOut();
                startActivity(new Intent(Principal_zegal.this, Ingresar.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
