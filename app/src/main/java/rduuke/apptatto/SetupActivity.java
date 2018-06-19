package rduuke.apptatto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupCircleImage;
    private ProgressBar setupProgress;
    private Uri mainImageURI = null;
    private EditText setupName;
    private Button setupSave;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = (Toolbar) findViewById(R.id.setup_toolbar);

        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Configuración de cuenta");

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setupProgress = (ProgressBar) findViewById(R.id.setup_progress);
        setupCircleImage = (CircleImageView) findViewById(R.id.setup_image);
        setupName = (EditText) findViewById(R.id.setup_name);
        setupSave = (Button) findViewById(R.id.setup_guardar);

        setupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = setupName.getText().toString();

                if (!TextUtils.isEmpty(username) && mainImageURI != null) {
                    setupProgress.setVisibility(View.VISIBLE);
                    String user_id = mAuth.getCurrentUser().getUid();

                    StorageReference imagePath = storageReference.child("profile_images").child(user_id + ".jpg");

                    imagePath.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                Uri download_uri = task.getResult().getDownloadUrl();

                                Toast.makeText(SetupActivity.this, "La imagen fue cargada correctamente", Toast.LENGTH_LONG).show();


                            } else {

                                String errrorMessage = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "Error: " + errrorMessage , Toast.LENGTH_LONG).show();
                            }
                            setupProgress.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }
        });

        setupCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "Permiso denegado", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(SetupActivity.this);


                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();

                setupCircleImage.setImageURI(mainImageURI);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
