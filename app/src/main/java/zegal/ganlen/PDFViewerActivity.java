package zegal.ganlen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;
    Button salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);



        pdfView = findViewById(R.id.pdfViewer);
        salir = findViewById(R.id.btn_Fin);



        onStart();


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
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Calc PDF Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hi PDF is attached in this mail. ");
                emailIntent.setType("application/pdf");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email using:"));
                //startActivity(new Intent(PDFViewerActivity.this, Principal_zegal.class));
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
