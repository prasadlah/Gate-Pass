package c.mileset.gateapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import c.mileset.gateapp.adapter.FamilyAdapter;
import c.mileset.gateapp.model.Family;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class FamilyActivity extends AppCompatActivity {

    public TextView lblId;
    private TextFieldBoxes nameTextField, mobileTextField, emailTextField, relationToOwnerTextField, occupationTextField;
    public ExtendedEditText nameExtendedText, mobileExtentedText, emailExtendedText, relationToOwnerExtendedText, occupationExtendedText, imageUrlExtendedText;
    public Button btnSave, btnSelectImage;
    public RecyclerView recyclerFamily;
    private ProgressBar familyProgressBar;
    private ImageView img;

    public FirebaseFirestore mFirestore;
    private StorageReference storageReference;

    public Intent intent;
    public Family family;
    public ArrayList<Family> familyArrayList;
    FamilyAdapter familyAdapter;

    private static final int PIC_IMAGE_REQUEST = 1;
    private String userId;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        intent = new Intent();
        intent = getIntent();
        userId = intent.getStringExtra("userId");

        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Register").child(userId);
        family = new Family();
        familyArrayList = new ArrayList<Family>();

        lblId = (TextView) findViewById(R.id.lblId);
        nameTextField = (TextFieldBoxes) findViewById(R.id.name_text_field_box);
        mobileTextField = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        emailTextField = (TextFieldBoxes) findViewById(R.id.email_text_field_box);
        relationToOwnerTextField = (TextFieldBoxes) findViewById(R.id.relation_to_owner_text_field_box);
        occupationTextField = (TextFieldBoxes) findViewById(R.id.occupation_text_field_box);
        nameExtendedText = (ExtendedEditText) findViewById(R.id.name_extended_text);
        mobileExtentedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        emailExtendedText = (ExtendedEditText) findViewById(R.id.email_extended_text);
        relationToOwnerExtendedText = (ExtendedEditText) findViewById(R.id.relation_to_owner_extended_text);
        occupationExtendedText = (ExtendedEditText) findViewById(R.id.occupation_extended_text);
        imageUrlExtendedText = (ExtendedEditText) findViewById(R.id.image_url_extended_text);
        img = (ImageView) findViewById(R.id.img);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);

        recyclerFamily = (RecyclerView) findViewById(R.id.recyclerFamily);

        familyProgressBar = (ProgressBar) findViewById(R.id.familyProgressBar);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lblId.getText().toString().isEmpty() || lblId.getText().toString().equals(null)) {
                    if (nameExtendedText.getText().toString().equals(null)
                            || emailExtendedText.getText().toString().equals(null)
                            || mobileExtentedText.getText().toString().equals(null)
                            || relationToOwnerExtendedText.getText().toString().equals(null)
                            || occupationExtendedText.getText().toString().equals(null)) {
                        getMessage("All fields required");
                    } else {
                        family.setName(nameExtendedText.getText().toString().trim());
                        family.setEmail(emailExtendedText.getText().toString().trim());
                        family.setMobileNumber(mobileExtentedText.getText().toString().trim());
                        family.setRelation(relationToOwnerExtendedText.getText().toString().trim());
                        family.setOccupation(occupationExtendedText.getText().toString().trim());
                        System.out.println("Save Button Clicked");
                        saveFamily(family);
                    }
                }
                else {
                    family.setId(lblId.getText().toString());
                    family.setName(nameExtendedText.getText().toString().trim());
                    family.setEmail(emailExtendedText.getText().toString().trim());
                    family.setMobileNumber(mobileExtentedText.getText().toString().trim());
                    family.setRelation(relationToOwnerExtendedText.getText().toString().trim());
                    family.setOccupation(occupationExtendedText.getText().toString().trim());
                    System.out.println("Update Button Clicked");
                    updateFamilyData(family);
                }
            }
        });

        setRecycler();
        getData();

    }

    private void setRecycler(){
        recyclerFamily.setHasFixedSize(true);
        recyclerFamily.setLayoutManager(new LinearLayoutManager(FamilyActivity.this, LinearLayoutManager.HORIZONTAL, true));
    }

    private void openFileChooser(){
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, PIC_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageUrlExtendedText.setText(imageUri.toString());
            Picasso.with(this).load(imageUri).into(img);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveFamily(final Family family){
        if(imageUri != null){

            final StorageReference fileStorage = storageReference.child("Family")
                    .child(family.getName() + "_" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask uploadTask;
            uploadTask = fileStorage.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    family.setImgUrl(uri.toString());

                    mFirestore.collection("Register")
                            .document(userId)
                            .collection("family")
                            .add(family)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    getMessage("Successfully");
                                    clearAll();
                                    getData();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    getMessage("Something Wrong");
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getMessage("Not Uploaded Image...");
                }
            });

        }
        else {
            getMessage("image Url Not Found");
        }

    }

    private void updateFamilyData(final Family family){
        if(imageUri != null){

            final StorageReference fileStorage = storageReference.child("Family")
                    .child(family.getName() + "_" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask uploadTask;
            uploadTask = fileStorage.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    family.setImgUrl(uri.toString());

                    mFirestore.collection("Register")
                            .document(userId)
                            .collection("family")
                            .document(family.getId())
                            .set(family)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getMessage("Updated Successfully");
                                    lblId.setText(null);
                                    clearAll();
                                    getData();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FamilyActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getMessage("Not Uploaded Image...");
                }
            });

        }
        else {
            mFirestore.collection("Register")
                    .document(userId)
                    .collection("family")
                    .document(family.getId())
                    .set(family)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getMessage("Updated Successfully");
                            lblId.setText(null);
                            clearAll();
                            getData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FamilyActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void getData(){
        familyArrayList.clear();
        mFirestore.collection("Register")
                .document(userId)
                .collection("family")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            family.setId(queryDocumentSnapshot.getId());
                            family.setName(queryDocumentSnapshot.getString("name"));
                            family.setEmail(queryDocumentSnapshot.getString("email"));
                            family.setMobileNumber(queryDocumentSnapshot.getString("mobileNumber"));
                            family.setRelation(queryDocumentSnapshot.getString("relation"));
                            family.setOccupation(queryDocumentSnapshot.getString("occupation"));
                            family.setImgUrl(queryDocumentSnapshot.getString("imgUrl"));
                            familyArrayList.add(family);
                        }
                        familyAdapter = new FamilyAdapter(FamilyActivity.this, familyArrayList);
                        recyclerFamily.setAdapter(familyAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getMessage("Error : " + e.getMessage());
                    }
                });
    }

    private void getMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll(){
        nameExtendedText.setText(null);
        emailExtendedText.setText(null);
        mobileExtentedText.setText(null);
        relationToOwnerExtendedText.setText(null);
        occupationExtendedText.setText(null);
        imageUrlExtendedText.setText("");
    }
}