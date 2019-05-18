package c.mileset.gateapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ChangeEmailActivity extends AppCompatActivity {

    private TextFieldBoxes oldEmailTextFieldBox, newEmailTextFieldBox;
    private ExtendedEditText oldEmailExtendedEditText, newEmailExtendedEditText;
    private Button btnChangeEmail;

    private FirebaseFirestore mFirestore;
    private DocumentReference documentReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);


        userId = getIntent().getStringExtra("userId");
        mFirestore = FirebaseFirestore.getInstance();
        documentReference = mFirestore.collection("Register")
                .document(userId);

        oldEmailTextFieldBox = (TextFieldBoxes) findViewById(R.id.old_email_text_field_box);
        newEmailTextFieldBox = (TextFieldBoxes) findViewById(R.id.new_email_text_field_box);
        oldEmailExtendedEditText = (ExtendedEditText) findViewById(R.id.old_email_extended_text);
        newEmailExtendedEditText = (ExtendedEditText) findViewById(R.id.new_email_extended_text);
        btnChangeEmail = (Button) findViewById(R.id.btnChangeEmail);

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldEmailExtendedEditText.getText().equals("") || oldEmailExtendedEditText.getText().equals(null)
                        || newEmailExtendedEditText.getText().equals("") || newEmailExtendedEditText.getText().equals(null)){
                    Toast.makeText(ChangeEmailActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(newEmailExtendedEditText.getText().equals(oldEmailExtendedEditText.getText())){
                        Toast.makeText(ChangeEmailActivity.this, "Both Mobile Numbers Are Same..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        documentReference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String email = documentSnapshot.getString("email");
                                        if(oldEmailExtendedEditText.getText().toString().equals(email)){
                                            changeEmail(newEmailExtendedEditText.getText().toString());
                                            clearAll();
                                        }
                                        else {
                                            Toast.makeText(ChangeEmailActivity.this, "Email Not Found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.v("Error : ", e.getMessage());
                                    }
                                });
                    }
                }
            }
        });

    }
    private void changeEmail(String email){
        documentReference.update("email", email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeEmailActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeEmailActivity.this, "Not Changed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll(){
        oldEmailExtendedEditText.setText("");
        newEmailExtendedEditText.setText("");
    }
}
