package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import c.mileset.gateapp.model.User;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class CreatePasswordActivity extends AppCompatActivity {

    private TextFieldBoxes newPasswordTextField, confirmPasswordTextField;
    private ExtendedEditText newPasswordExtendedText, confirmPasswordExtendedText;
    private Button btnCreate;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private DocumentReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        Intent intent = getIntent();
        User user = new User();
        user.setUserId(intent.getStringExtra("userId"));
        user.setMobileNumber(intent.getStringExtra("mobile"));
        user.setPassword(intent.getStringExtra("password"));

        String regId = user.getUserId();
        String mobile = user.getMobileNumber();
        final String password = user.getPassword();

        mFirestore = FirebaseFirestore.getInstance();
        mReference = mFirestore.collection("Register").document(regId);

        newPasswordTextField = (TextFieldBoxes) findViewById(R.id.new_password_text_field_box);
        confirmPasswordTextField = (TextFieldBoxes) findViewById(R.id.confirm_password_text_field_box);
        newPasswordExtendedText = (ExtendedEditText) findViewById(R.id.new_password_extended_text);
        confirmPasswordExtendedText = (ExtendedEditText) findViewById(R.id.confirm_password_extended_text);
        btnCreate = (Button) findViewById(R.id.btnCreateNewPassword);


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

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword, confirmPassword;
                newPassword = newPasswordExtendedText.getText().toString().trim();
                confirmPassword = confirmPasswordExtendedText.getText().toString().trim();
                if(newPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(CreatePasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(newPasswordExtendedText.length() < 8 && newPasswordExtendedText.length() > 15){
                    newPasswordTextField.setError("Wrong Password", true);
                }
                else if(confirmPasswordExtendedText.length() < 8 && confirmPasswordExtendedText.length() > 15){
                    confirmPasswordTextField.setError("Wrong Password", true);
                }
                else {
                    if(newPassword.equals(password)) {
                        Toast.makeText(CreatePasswordActivity.this, "You Entered Old Password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (newPassword.equals(confirmPassword)) {
                            mReference.update("password", newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CreatePasswordActivity.this, "Password created", Toast.LENGTH_SHORT).show();
                                    clearAll();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String message = e.getMessage();
                                    Toast.makeText(CreatePasswordActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(CreatePasswordActivity.this, "Password can't matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void clearAll(){
        newPasswordExtendedText.setText("");
        confirmPasswordExtendedText.setText("");
    }
}
