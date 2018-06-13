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

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login, register;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailE = email.getText().toString();
                String passwordE = password.getText().toString();

                if (!TextUtils.isEmpty(emailE) && !TextUtils.isEmpty(passwordE)) {
                    auth.signInWithEmailAndPassword(emailE, passwordE)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Colocar usuario y contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
