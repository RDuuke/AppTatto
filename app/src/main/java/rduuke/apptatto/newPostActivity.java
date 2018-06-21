package rduuke.apptatto;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class newPostActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar newPostToolbar;

    private ImageView newPostImage;

    private EditText newPostDescription;
    private Button newPostBtn;
    private ProgressBar newProgress;

    private Uri postUriImage;

    private StorageReference storageReference;

    private FirebaseFirestore firebaseFirestore;

    private FirebaseAuth mAuth;

    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        newPostToolbar = findViewById(R.id.newPostToolbar);

        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Nueva publicación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        current_user_id = mAuth.getCurrentUser().getUid();

        newPostImage = (ImageView) findViewById(R.id.new_post_image);
        newPostDescription = (EditText) findViewById(R.id.new_post_description);
        newPostBtn = (Button) findViewById(R.id.new_post_btn);
        newProgress = (ProgressBar) findViewById(R.id.new_post_progress);

        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1, 1)
                        .start(newPostActivity.this);


            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDescription.getText().toString();

                if (!TextUtils.isEmpty(desc) && postUriImage != null) {
                    newProgress.setVisibility(View.VISIBLE);
                    String randonName = FieldValue.serverTimestamp().toString();

                    StorageReference filePath = storageReference.child("post_image").child(randonName + ".jpg");
                    filePath.putFile(postUriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                String downloadUri = task.getResult().getDownloadUrl().toString();

                                Map<String, Object> postMap = new HashMap<>();

                                postMap.put("image_url", downloadUri);
                                postMap.put("description", desc);
                                postMap.put("user_id", current_user_id);
                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()) {

                                            Toast.makeText(newPostActivity.this, "Publicación generada", Toast.LENGTH_LONG).show();
                                            Intent mainIntent = new Intent(newPostActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();

                                        } else {

                                        }
                                        newProgress.setVisibility(View.INVISIBLE);
                                    }
                                });

                            } else {
                                newProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
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

                postUriImage = result.getUri();

                newPostImage.setImageURI(postUriImage);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
