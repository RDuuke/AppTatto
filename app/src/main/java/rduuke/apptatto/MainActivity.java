package rduuke.apptatto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FloatingActionButton addPostBtn;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);


        mAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        addPostBtn = (FloatingActionButton) findViewById(R.id.addPostBtn);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPostIntent = new Intent(MainActivity.this, newPostActivity.class);
                startActivity(addPostIntent);
            }
        });

        //FirebaseAuth.getInstance().signOut();

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("AppTatto");

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currenUser = mAuth.getInstance().getCurrentUser();
        if (currenUser == null) {
            sendToLogin();

        } else {

            firebaseFirestore.collection("Users").document(currenUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (! task.getResult().exists()) {
                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                logout();
                return true;
            case R.id.action_setting_btn:
                Intent intentSetup = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intentSetup);
            default:
                return false;
        }
    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
