package zegal.ganlen;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firma_Cliente extends AppCompatActivity {

    DatabaseReference mDatabaseReference;

    ImageView getfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma__cliente);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();



        getfirma = findViewById(R.id.imgFirma);

        Bundle bu = getIntent().getExtras();
        Bitmap bmp2 = bu.getParcelable("btmp");
        getfirma.setImageBitmap(bmp2);
    }
}
