package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ChangeMobileNumberActivity extends AppCompatActivity {

    private TextFieldBoxes oldMobileNumberTextFieldBox, newMobileNumberTextFieldBox;
    private ExtendedEditText oldMobileNumberExtendedEditText, newMobileNumberExtendedEditText;
    private Button btnChangeMobileNumber;

    private FirebaseFirestore mFirestore;
    private DocumentReference documentReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile_number);

        userId = getIntent().getStringExtra("userId");
        mFirestore = FirebaseFirestore.getInstance();
        documentReference = mFirestore.collection("Register")
                .document(userId);

        oldMobileNumberTextFieldBox = (TextFieldBoxes) findViewById(R.id.old_mobile_text_field_box);
        newMobileNumberTextFieldBox = (TextFieldBoxes) findViewById(R.id.new_mobile_text_field_box);
        oldMobileNumberExtendedEditText = (ExtendedEditText) findViewById(R.id.old_mobile_extended_text);
        newMobileNumberExtendedEditText = (ExtendedEditText) findViewById(R.id.new_mobile_extended_text);
        btnChangeMobileNumber = (Button) findViewById(R.id.btnChangeMobileNumber);

        btnChangeMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldMobileNumberExtendedEditText.getText().equals("") || oldMobileNumberExtendedEditText.getText().equals(null)
                || newMobileNumberExtendedEditText.getText().equals("") || newMobileNumberExtendedEditText.getText().equals(null)){
                    Toast.makeText(ChangeMobileNumberActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else if(oldMobileNumberExtendedEditText.length() > 10) {
                    oldMobileNumberTextFieldBox.setError("Wrong Mobile number", true);
                }
                else if(newMobileNumberExtendedEditText.length() > 10) {
                    newMobileNumberTextFieldBox.setError("Wrong Mobile number", true);
                }
                else {
                    if(newMobileNumberExtendedEditText.getText().equals(oldMobileNumberExtendedEditText.getText())){
                        Toast.makeText(ChangeMobileNumberActivity.this, "Both Mobile Numbers Are Same..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        documentReference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String mobileNumber = documentSnapshot.getString("mobileNumber");
                                        if(oldMobileNumberExtendedEditText.getText().toString().equals(mobileNumber)){
                                            changeMobileNumber(newMobileNumberExtendedEditText.getText().toString());
                                            clearAll();
                                        }
                                        else {
                                            Toast.makeText(ChangeMobileNumberActivity.this, "Mobile Number Not Found", Toast.LENGTH_SHORT).show();
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

    private void changeMobileNumber(String mobileNumber){
        documentReference.update("mobileNumber", mobileNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeMobileNumberActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeMobileNumberActivity.this, "Not Changed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll(){
        oldMobileNumberExtendedEditText.setText("");
        newMobileNumberExtendedEditText.setText("");
    }
}
