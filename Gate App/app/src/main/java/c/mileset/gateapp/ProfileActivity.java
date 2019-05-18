package c.mileset.gateapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import c.mileset.gateapp.model.Profile;
import c.mileset.gateapp.model.User;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ProfileActivity extends AppCompatActivity {

    private TextFieldBoxes nameTextField, adharNumberTextField, mobileTextField, emailTextField;
    private ExtendedEditText nameExtendedText, adharNumberExtendedText, mobileExtendedText, emailExtendedText;
    private Spinner spinnerGender;
    private TextView tvSelectPicture, tvChangePassword, tvChangeMobileNumber, tvChangeEmail;
    private ImageView imgProfile;
    private ImageButton ibSave, ibCancel;

    private Uri imageUri;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorage;
    private FirebaseStorage mFirebaseStorage;
    private Query mQuery;

    private Intent intent;
    private String userId;

    private Profile profile;
    private User user;

    private final static int PIC_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("Register").child(userId);
        profile = new Profile();
        user = new User();

        nameTextField = (TextFieldBoxes) findViewById(R.id.name_text_field_box);
        adharNumberTextField = (TextFieldBoxes) findViewById(R.id.adhar_number_text_field_box);
        mobileTextField = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        emailTextField = (TextFieldBoxes) findViewById(R.id.email_text_field_box);
        nameExtendedText = (ExtendedEditText) findViewById(R.id.name_extended_text);
        adharNumberExtendedText = (ExtendedEditText) findViewById(R.id.adhar_number_extended_text);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        emailExtendedText = (ExtendedEditText) findViewById(R.id.email_extended_text);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        tvSelectPicture = (TextView) findViewById(R.id.tvSelectPicture);
        tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        tvChangeMobileNumber = (TextView) findViewById(R.id.tvChangeMobileNumber);
        tvChangeEmail = (TextView) findViewById(R.id.tvChangeEmail);
        imgProfile = (ImageView) findViewById(R.id.ivProfile);
        ibSave = (ImageButton) findViewById(R.id.ibSave);
        ibCancel = (ImageButton) findViewById(R.id.ibCancel);

        tvSelectPicture.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvChangePassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvChangeMobileNumber.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvChangeEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(nameExtendedText.getText().toString());
                profile.setAdhar_number(adharNumberExtendedText.getText().toString());
                profile.setGender(spinnerGender.getSelectedItem().toString());
                updateProfile(user, profile);
            }
        });

        tvSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        tvChangeMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ProfileActivity.this, ChangeMobileNumberActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        tvChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ProfileActivity.this, ChangeEmailActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        getData();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PIC_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).fit().centerCrop().into(imgProfile);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void getData(){
        final DocumentReference documentReference = mFirestore.collection("Register").document(userId);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            nameExtendedText.setText(documentSnapshot.getString("name"));
                            mobileExtendedText.setText(documentSnapshot.getString("mobileNumber"));
                            emailExtendedText.setText(documentSnapshot.getString("email"));

                            documentReference.collection("Profile")
                                    .document("profile")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot ds = task.getResult();
                                                if(ds != null){
                                                    adharNumberExtendedText.setText(ds.getString("adhar_number"));
                                                    Picasso.with(ProfileActivity.this)
                                                            .load(ds.getString("image_url"))
                                                            .fit()
                                                            .centerCrop()
                                                            .into(imgProfile);
                                                }
                                                else {
                                                    adharNumberExtendedText.setText("");

                                                }
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "Not Present Yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateProfile(final User user, final Profile profile){

        if(imageUri != null){
            final StorageReference fileStorage = mStorage.child("Profile")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            final UploadTask uploadTask = fileStorage.putFile(imageUri);
            final Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileStorage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri uri = task.getResult();
                    profile.setImage_url(uri.toString());

                    DocumentReference documentReference = mFirestore.collection("Register")
                            .document(userId);
                    documentReference.update("name", user.getName())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProfileActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                    documentReference.collection("Profile")
                            .document("profile")
                            .set(profile)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

/*        intent = new Intent();
        DocumentReference documentReference = mFirestore.collection("Register").document(userId);
        documentReference.update("name", user.getName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        documentReference.collection("Profile")
                .document("profile")
                .set(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "NOt Successfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        */
    }
}
