package rduuke.apptatto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAuth = (FirebaseAuth) FirebaseAuth.getInstance();
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
