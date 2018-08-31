package zegal.ganlen;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Contratos_Main_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratos__main);
     }

    public void botonClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_alianza:
                startActivity(new Intent(Contratos_Main_Activity.this,con_alianza.class));
                break;
            case R.id.btn_help:
                new PreferenceManager(Contratos_Main_Activity.this).clearPreference();
                startActivity(new Intent(Contratos_Main_Activity.this, WelcomeActivity.class));
                finish();
                break;
            case R.id.btn_laboral:
                startActivity(new Intent(Contratos_Main_Activity.this,Con_laboral.class));
                break;
            case R.id.btn_software:
                startActivity(new Intent(Contratos_Main_Activity.this,Con_software.class));
                break;
            case R.id.btn_servicios:
                startActivity(new Intent(Contratos_Main_Activity.this,con_servicios.class));
                break;
        }
    }
}
