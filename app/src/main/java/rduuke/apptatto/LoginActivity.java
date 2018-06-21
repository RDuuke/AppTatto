package rduuke.apptatto;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailTxt, loginPassText;
    private Button loginBtn, loginCreBtn;
    private ProgressBar loginProgress;
    private  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailTxt = (EditText) findViewById(R.id.login_email);
        loginPassText = (EditText) findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.login_enter);
        loginCreBtn = (Button) findViewById(R.id.login_create);

        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmailTxt.getText().toString();
                String password = loginPassText.getText().toString();

                Log.d("[Email and Password]", email + " " + password);

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        loginCreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntet = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntet);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
