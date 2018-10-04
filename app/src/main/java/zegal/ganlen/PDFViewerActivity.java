package zegal.ganlen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;
    Button salir, tyc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);



        pdfView = findViewById(R.id.pdfViewer);
        salir = findViewById(R.id.btn_Fin);
        tyc = findViewById(R.id.btn_Tyc);

        onStart();

        tyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(PDFViewerActivity.this)
                        .setTitle("ZEGAL")
                        .setMessage("Terminos y condiciones.\n" +
                                "\nPRIMERA. OBJETO. El objeto del contrato consiste en que el Prestador ponga a disposición del Cliente la app para que éste genere sus propios contratos de forma autónoma, sin la asesoría legal del Prestador.\n" +
                                "\nSEGUNDA. CONTRAPRESTACIÓN. Por la prestación de los servicios el Cliente pagará al Prestador la cantidad señalada en la app o aquella cantidad comunicada por el Prestador por escrito a través de medios digitales o por cualquier otro medio posible.\n" +
                                "\nTERCERA. VIGENCIA. El plazo del contrato será indefinido. Las partes acuerdan que el contrato terminará su vigencia de forma natural mediante la prestación de los servicios objeto de este contrato.\n" +
                                "El Cliente tendrá un plazo de 15 días naturales para hacer observaciones a los servicios recibidos por una única vez, en el entendido, de que si no objeta los servicios prestados en el plazo indicado se considerará que ha aceptado de forma tácita los servicios recibidos.\n" +
                                "\nCUARTA. GASTOS. Ambas partes acuerdan que todos los gastos generados por la prestación de los servicios solicitados por el Cliente y descritos en la cláusula Primera serán a cargo del Cliente. \n" +
                                "\n" +
                                "QUINTA. LIBERACIÓN DE RESPONSABILIDAD. El Cliente reconoce que el Prestador obrará de buena fe en la prestación de los servicios, con la capacidad técnica y experiencia necesaria para cumplir con el objeto del contrato, por lo cual en este acto lo libera de cualquier responsabilidad civil, profesional, penal o administrativa derivada de la prestación de los servicios y renuncia expresamente a los artículos 2104, 2107, 2110, 2118 y 2615 del Código Civil para para el Distrito Federal. \n" +
                                "El Cliente se obliga a sacar en paz y a salvo al Prestador de cualquier acción judicial o extrajudicial iniciada en su contra por la prestación de lo servicios y el Cliente se compromete a pagar los gastos erogados por la atención del juicio o procedimiento extrajudicial, de forma enunciativa y no limitativa: los gastos judiciales, honorarios de abogados externos, peritos, entre otros.\n" +
                                "En ningún caso el Prestador será responsable del uso que se brinde al contrato generado en la app, toda vez que las partes reconocen que el contrato fue generado por el Cliente de forma unilateral sin la asesoría legal del Prestador.\n" +
                                "\n" +
                                "SEXTA. DATOS PERSONALES. Los datos personales del Cliente serán tratados conforme a la Ley Federal de Protección de Datos Personales en Posesión de Particulares y el Aviso de Privacidad del Prestador, que podrá ser consultado en https://ajacadenaabogados.com/aviso-de-privacidad.html\n" +
                                "\n" +
                                "SÉPTIMA. CONSENTIMIENTO POR MEDIOS ELECTRÓNICOS. El Cliente conviene en otorgar su consentimiento a la propuesta realizada por el Prestador a través de medios electrónicos. El Cliente acepta el contenido del presente contrato al seleccionar la casilla correspondiente en la app Zegal.\n" +
                                "\n" +
                                "OCTAVA. DIVISIBILIDAD. En caso de que alguna de las cláusulas del contrato sea anulada mediante orden judicial, las demás cláusulas continuarán siendo válidas para las partes.\n" +
                                "\n" +
                                "NOVENA. LEYES Y TRIBUNALES. Para la interpretación y cumplimiento del presente contrato el mismo se regirá por las leyes aplicables en la Ciudad de México y en caso de controversia las partes se someten a los tribunales competentes de la Ciudad de México, renunciando a cualquier otro fuero por razón de sus domicilios presentes o futuros o por cualquier otra circunstancia. \n" +
                                "\n" +
                                "El presente contrato se firma libre de dolo, error, mala fe o cualquier otro vicio que pueda afectar el consentimiento de las partes, en la Ciudad de México en la fecha señalada en la app Zegal.\n" +
                                "\n\nAviso de privacidad.\n" +
                                "\nEl Responsable utilizará los datos personales recabados al Titular para cumplir con los fines señalados en el presente aviso de privacidad, conforme a la Ley Federal de Protección de Datos Personales en Posesión de Particulares (en lo sucesivo la Ley).\n" +
                                "\nEl responsable informa al Titular por medio del aviso de privacidad la siguiente información:\n" +
                                "\nI. Titular de los datos personales. Es la persona física que tiene una relación comercial o jurídica con el señor Jesús Fernando Aja Cadena.\n" +
                                "\nII. Datos personales que se recaban. Nombre completo, correo electrónico, teléfono celular, ciudad e información necesaria para atender la consulta o prestar el servicio.\n" +
                                "\nIII. La identidad y domicilio del responsable que recaba los datos personales. Jesús Fernando Aja Cadena con domicilio en Insurgentes sur 1863, despacho 301B, Colonia Guadalupe Inn, C.P.01020, Álvaro Obregón, Ciudad de México.\n" +
                                "\nIV. Las finalidades del tratamiento de datos. (i) Para el cumplimiento del objeto del contrato de servicios suscrito entre el Titular y el Responsable, (ii) para fines comerciales y de mercadotecnia y (iii) para realizar gestiones de cobranza.\n" +
                                "\nV. Las opciones y medios que el responsable ofrezca a los titulares para limitar el uso o divulgación de los datos. En caso de que desee limitar el uso o la divulgación de los datos personales deberá enviar un aviso al siguiente correo electrónico jesus.aja@ajacadenaabogados.com manifestando esa situación.\n" +
                                "\nVI. Los medios para ejercer los derechos de acceso, rectificación, cancelación u oposición, de conformidad con lo dispuesto en la Ley. El Titular tiene derecho a conocer qué datos personales se tienen, para qué se utilizan y las condiciones del uso que se les da (Acceso). Asimismo, es su derecho solicitar la corrección de su información personal en caso de que esté desactualizada, sea inexacta o incompleta (Rectificación); que se elimine de nuestros registros o bases de datos cuando considere que la misma no está siendo utilizada conforme a los principios, deberes y obligaciones previstas en la normativa (Cancelación); así como oponerse al uso de sus datos personales para fines específicos (Oposición). Estos derechos se conocen como derechos ARCO.\n" +
                                "\nPara el ejercicio de cualquiera de los derechos ARCO, usted deberá presentar la solicitud respectiva  por correo electrónico a la siguiente dirección jesus.aja@ajacadenaabogados.com.\n" +
                                "\nVII. Las transferencias de datos que se efectúen. Los datos personales podrán ser transferidos a cualquier persona relacionada con el objeto del contrato. El Titular reconoce que no se requiere su consentimiento para realizar la transferencia señalada en este apartado, en términos del artículo 37 de la Ley.\n" +
                                "\nVIII.El procedimiento y medio por el cual el Responsable comunicará a los Titulares los cambios al aviso de privacidad. El responsable podrá realizar cambios al aviso de privacidad periódicamente, por lo cual se recomienda al Titular consultar el aviso de privacidad en www.ajacadenaabogados.com\n")
                        .setPositiveButton("Aceptar", null)
                        .create()
                        .show();
            }
        });


        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                Bundle bundle = getIntent().getExtras();
                                String[] mailto = {""};
                                file = new File(bundle.getString("path", ""));
                                Uri uri = Uri.fromFile(file);
                                startActivity(new Intent(PDFViewerActivity.this, Principal_zegal.class));
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contrato de servicios");
                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Este es el archivo con el contrato de servicio.");
                                emailIntent.setType("application/pdf");
                                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(emailIntent, "Enviando email utilizando:"));
                                finish();
            }
        });

    }

    public boolean plasmarPdf()
    {
        if(Build.VERSION.SDK_INT >=23)
        {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                Bundle bundle = getIntent().getExtras();
                if(bundle != null)
                {
                    file = new File(bundle.getString("path", ""));
                }

                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .enableAntialiasing(true)
                        .spacing(4)
                        .load();
                return true;
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else
        {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                file = new File(bundle.getString("path", ""));
            }

            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .spacing(4)
                    .load();
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Build.VERSION.SDK_INT>=23)
        {
            plasmarPdf();
        }
        else
        {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                file = new File(bundle.getString("path", ""));
            }

            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .spacing(4)
                    .load();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            plasmarPdf();
        }
        else
        {
            Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
        }
    }
}
