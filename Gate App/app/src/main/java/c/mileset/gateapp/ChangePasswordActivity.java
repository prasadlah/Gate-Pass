package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextFieldBoxes oldPasswordTextField, newPasswordTextField, confirmPasswordTextField;
    private ExtendedEditText oldPasswordExtendedText, newPasswordExtendedText, confirmPasswordExtendedText;
    private Button btnChangePassword;

    private FirebaseFirestore mFirestore;
    private Intent intent;
    private DocumentReference documentReference;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        intent = getIntent();
        mFirestore = FirebaseFirestore.getInstance();
        documentReference = mFirestore.collection("Register").document(intent.getStringExtra("userId"));

        oldPasswordTextField = (TextFieldBoxes) findViewById(R.id.old_password_text_field_box);
        newPasswordTextField = (TextFieldBoxes) findViewById(R.id.new_password_text_field_box);
        confirmPasswordTextField = (TextFieldBoxes) findViewById(R.id.confirm_password_text_field_box);
        oldPasswordExtendedText = (ExtendedEditText) findViewById(R.id.old_password_extended_text);
        newPasswordExtendedText = (ExtendedEditText) findViewById(R.id.new_password_extended_text);
        confirmPasswordExtendedText = (ExtendedEditText) findViewById(R.id.confirm_password_extended_text);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);


        oldPasswordTextField.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                char[] chars = {'@', '$', ' ', '#', '!', '"', '\'', '£', '/', '*', '-', '+'};

                for(char ch : chars) {
                    if (theNewText.equals(ch)) {
                        oldPasswordTextField.setError("You Enter Wrong Value", true);
                    }
                }
            }
        });
        newPasswordTextField.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                char[] chars = {'@', '$', ' ', '#', '!', '"', '\'', '£', '/', '*', '-', '+'};

                for(char ch : chars) {
                    if (theNewText.equals(ch)) {
                        newPasswordTextField.setError("You Enter Wrong Value", true);
                    }
                }
            }
        });
        confirmPasswordTextField.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                char[] chars = {'@', '$', ' ', '#', '!', '"', '\'', '£', '/', '*', '-', '+'};

                for(char ch : chars) {
                    if (theNewText.equals(ch)) {
                        confirmPasswordTextField.setError("You Enter Wrong Value", true);
                    }
                }
            }
        });



        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassword = oldPasswordExtendedText.getText().toString().trim();
                final String newPassword = newPasswordExtendedText.getText().toString().trim();
                final String confirmPassword = confirmPasswordExtendedText.getText().toString().trim();

                if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else if(oldPasswordExtendedText.length() < 8 && oldPasswordExtendedText.length() > 15){
                    oldPasswordTextField.setError("Wrong Password", true);
                }
                else if(newPasswordExtendedText.length() < 8 && newPasswordExtendedText.length() > 15){
                    newPasswordTextField.setError("Wrong Password", true);
                }
                else if(confirmPasswordExtendedText.length() < 8 && confirmPasswordExtendedText.length() > 15){
                    confirmPasswordTextField.setError("Wrong Password", true);
                }
                else {
                    documentReference.get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String oldPassword = task.getResult().get("password").toString();
                                    if(!newPassword.equals(oldPassword)){
                                        if(newPassword.equals(confirmPassword)){
                                            changePassword(intent.getStringExtra("userId"), newPassword);
                                        }
                                        else {
                                            Toast.makeText(ChangePasswordActivity.this, "Password Cant Matcjed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(ChangePasswordActivity.this, "Old password same", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });
    }

    protected void changePassword(String regId, String password){
        documentReference.update("password", password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                clearAll();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangePasswordActivity.this, "Something Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void clearAll(){
        oldPasswordExtendedText.setText("");
        newPasswordExtendedText.setText("");
        confirmPasswordExtendedText.setText("");
    }
}
