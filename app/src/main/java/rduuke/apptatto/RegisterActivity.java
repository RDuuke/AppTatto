package rduuke.apptatto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password;
    Button register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userE = email.getText().toString();
                String passwordE = password.getText().toString();

                if (!TextUtils.isEmpty(userE) && !TextUtils.isEmpty(passwordE)) {
                    auth.createUserWithEmailAndPassword(userE, passwordE)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(getApplicationContext(), "usuario creado correctamente",
                                            Toast.LENGTH_LONG);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Ocurrio un problema",
                                                Toast.LENGTH_LONG);
                                    } else {

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Agregar correo o contraseña", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
