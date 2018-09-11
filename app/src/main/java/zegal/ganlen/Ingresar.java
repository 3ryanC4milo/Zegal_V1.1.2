package zegal.ganlen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Ingresar extends AppCompatActivity {
    Context mContext;
    private EditText login_email, login_password;
    private Button btn_firebase_login, btn_firebase_registro;
    private FirebaseAuth auth;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        PD = new ProgressDialog(this);
        PD.setMessage("Cargando...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        //Get FireBase auth instance
        auth = FirebaseAuth.getInstance();

        login_email = findViewById(R.id.ingresar_edit_text_email);
        login_password = findViewById(R.id.ingresar_edit_text_password);
        btn_firebase_registro = findViewById(R.id.button_registro);
        btn_firebase_login = findViewById(R.id.button_ingresar);
        // Validating Login Credits with Firebase
        btn_firebase_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = login_email.getText().toString();
                final String password = login_password.getText().toString();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        // Authenticate user
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Ingresar.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(
                                                    Ingresar.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {
                                            Intent intent = new Intent(Ingresar.this, Principal_zegal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                Ingresar.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Registration Screen
        btn_firebase_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ingresar.this, Principal_zegal.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onResume() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Ingresar.this, Principal_zegal.class));
            finish();
        }
        super.onResume();
    }
}

