package zegal.ganlen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Firma_Cliente extends AppCompatActivity {

    Button out;

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15;

    String p1 = "",p2= "",p3= "",p4= "",p5= "",p6= "",p7= "",p8= "",p9= "",p10= "",p11= "";

    ImageView getfirmaCli, getfirmaAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma__cliente);


        getfirmaAb = findViewById(R.id.imgFirmaP);
        getfirmaCli = findViewById(R.id.imgFirma);

        t1 = findViewById(R.id.lbl1);
        t2 = findViewById(R.id.lbl2);
        t3 = findViewById(R.id.lbl3);
        t4 = findViewById(R.id.lbl4);
        t5 = findViewById(R.id.lbl5);
        t6 = findViewById(R.id.lbl6);
        t7 = findViewById(R.id.lbl7);
        t8 = findViewById(R.id.lbl8);
        t9 = findViewById(R.id.lbl9);
        t10 = findViewById(R.id.lbl10);
        t12 = findViewById(R.id.lblAbt);
        t13 = findViewById(R.id.lblClit);
        t14 = findViewById(R.id.lblAb);
        t15 = findViewById(R.id.lblCli);
        t11 = findViewById(R.id.lbl11);
        out = findViewById(R.id.btnFinal);


        p1 = "CONTRATO DE PRESTACIÓN DE SERVICIOS QUE SUSCRIBEN "+ getIntent().getStringExtra("prestador")
                + " (EN ADELANTE EL PRESTADOR) Y "+ getIntent().getStringExtra("recibe") +" (EN ADELANTE EL CLIENTE) CONFORME A LAS SIGUIENTES CLÁUSULAS: ";

        p2 ="CLÁUSULAS";

        p3 = "PRIMERA. OBJETO. El objeto del contrato es la prestación de los siguientes servicios por el Prestador al Cliente: \n"+ getIntent().getStringExtra("finalidad");

        p4 = "SEGUNDA. CONTRAPRESTACIÓN. Por la prestación de los servicios el Cliente pagará al Prestador la can-tidad de $"
            +getIntent().getStringExtra("PaymentAmount") + ".00 ("+MontoLetra.cantidadConLetra(getIntent().getStringExtra("PaymentAmount"))+" pesos) M.N. más IVA/ misma que será pagadera contra entrega de los servicios correspondientes. " +
                "/ misma que será pagadera mediante un solo pago que deberá ser cubierto el "+getIntent().getStringExtra("primer") +"." +
                "\n La cantidad(es) señalada(s) en esta cláusula podrá ser cubierta(s) en cualquier forma de pago.";

        p5 = "TERCERA. VIGENCIA. El plazo del contrato será indefinido, por la naturaleza de los servicios prestados.\n" +
                "Las partes acuerdan que el contrato terminará su vigencia de forma natural mediante la prestación de los servicios objeto de este contrato.\n";

        p6 = "CUARTA. GASTOS. Ambas partes acuerdan que todos los gastos generados por la prestación de los ser-vicios descritos en la cláusula Primera serán a cargo del Cliente.";

        p7 ="QUINTA. LIBERACIÓN DE RESPONSABILIDAD. El Cliente reconoce que el Prestador obrará de buena fe en la prestación de los servicios, con la capacidad técnica y experiencia necesaria para cumplir " +
                "con el obje-to del contrato, por lo cual en este acto lo libera de cualquier responsabilidad civil, mercantil, penal o admi-nistrativa derivada de la prestación de los servicios prestados. \n" +
                "En caso de que ocurra un sismo, incendio, terremoto, inundación o cualquier otro fenómeno natural que impida que se presten los servicios, el Cliente no se reserva ninguna acción en contra del Prestador " +
                "o su personal y se obliga a sacarlos en paz y a salvo de cualquier procedimiento judicial o extrajudicial en cual-quier materia jurídica, así como a pagar al Prestador cualquier gasto relacionado con ese procedimiento, " +
                "incluidos los honorarios de abogados.";

        p8 = "SEXTA. DATOS PERSONALES. Los datos personales del Cliente serán tratados conforme a la Ley Federal de Protección de Datos Personales en Posesión de Particulares y el Aviso de Privacidad del Prestador.";

        p9 = "SÉPTIMA. CONFIDENCIALIDAD. El Prestador guardará confidencialidad de la información que reciba del Cliente en relación con los servicios objeto del contrato.\n"+
            "El Prestador deberá abstenerse de comunicar por cualquier medio a terceros la información que conozca en virtud de su participación en los proyectos en los que sea parte.\n"+
        "En caso de que el Prestador incumpla su obligación de confidencialidad, deberá pagar los daños y perjui-cios causado al Cliente.";

        p10 = "OCTAVA. DIVISIBILIDAD. En caso de que alguna de las cláusulas del contrato sea anulada mediante orden judicial, las demás cláusulas continuarán siendo válidas para las partes.";

        p11 = "NOVENA. LEYES Y TRIBUNALES. Para la interpretación y cumplimiento del presente contrato el mismo se regirá por las leyes aplicables en la Ciudad de México y en caso de controversia las partes se someten a los tribunales competentes de la Ciudad de México, " +
                "renunciando a cualquier otro fuero por razón de sus domicilios presentes o futuros o por cualquier otra circunstancia. \n" +
                "El presente contrato se firma libre de dolo, error, mala fe o cualquier otro vicio que pueda afectar el con-sentimiento de las partes, en la Ciudad de México a los "+
                getIntent().getStringExtra("dia")+" DÍAS DEL MES DE "+getIntent().getStringExtra("mes")+" DEL AÑO "+getIntent().getStringExtra("anio")+".";

        t1.setText(p1);
        t2.setText(p2);
        t3.setText(p3);
        t4.setText(p4);
        t5.setText(p5);
        t6.setText(p6);
        t7.setText(p7);
        t8.setText(p8);
        t9.setText(p9);
        t10.setText(p10);
        t11.setText(p11);
        t12.setText("EL ABOGADO");
        t13.setText("EL CLIENTE");


        Bundle bu = getIntent().getExtras();
        Bitmap bmp2 = bu.getParcelable("btmp");
        getfirmaCli.setImageBitmap(bmp2);

        t14.setText(getIntent().getStringExtra("prestador"));
        t15.setText(getIntent().getStringExtra("recive"));


        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Firma_Cliente.this, Principal_zegal.class));
                finish();
            }
        });

    }
}
