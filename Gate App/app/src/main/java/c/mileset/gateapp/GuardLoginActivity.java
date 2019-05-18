package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class GuardLoginActivity extends AppCompatActivity {

    private TextFieldBoxes mobileTextFieldBox, passwordTextFieldBox;
    private ExtendedEditText mobileExtendedText, passwordExtendedText;
    private Button btnLogin;

    FirebaseFirestore mFirestore;
    Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_login);

        mFirestore = FirebaseFirestore.getInstance();

        mobileTextFieldBox = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        passwordTextFieldBox = (TextFieldBoxes) findViewById(R.id.password_text_field_box);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        passwordExtendedText = (ExtendedEditText) findViewById(R.id.password_extended_text);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        passwordTextFieldBox.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                char[] chars = {'@', '$', ' ', '#', '!', '"', '\'', '£', '/', '*', '-', '+'};

                for(char ch : chars) {
                    if (theNewText.equals(ch)) {
                        passwordTextFieldBox.setError("You Enter Wrong Value", true);
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileExtendedText.getText().toString().equals("") || mobileExtendedText.getText().toString().equals(null)
                || passwordExtendedText.getText().toString().equals("") || passwordExtendedText.getText().toString().equals(null)){
                    getMessage("All Fields Required!!!");
                }
                else if(mobileExtendedText.length() > 10){
                    mobileTextFieldBox.setError("Wrong Mobile Number", true);
                }
                else if(passwordExtendedText.length() < 8 || passwordExtendedText.length() > 15){
                    passwordTextFieldBox.setError("Wrong Password", true);
                }
                else {
                    String userName, password;

                    userName = mobileExtendedText.getText().toString().trim();
                    password = passwordExtendedText.getText().toString().trim();

                    validateGuard(userName, password);
                }
            }
        });
    }

    private void validateGuard(final String userName, final String password){

        if(userName.equals("1111111111") && password.equals("11111111")){
            Intent intent = new Intent(GuardLoginActivity.this, GuardHomeActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish();
            clearAll();
        }
        else {
            getMessage("Password Not Matched");
        }

/*
        mQuery = mFirestore.collection("Guard");
        mQuery.whereEqualTo("userName", userName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            if(queryDocumentSnapshot.getString("userName").equals(userName) && queryDocumentSnapshot.getString("password").equals(password)){
                                Intent intent = new Intent();
                                intent.putExtra("userName", userName);
                                startActivity(intent);
                                finish();
                                clearAll();
                            }
                            else {
                                getMessage("Password Not Matched");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                        getMessage("Error : " + e.getMessage());
                    }
                });
        */
    }

    private void getMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll(){
        mobileExtendedText.setText("");
        passwordExtendedText.setText("");
    }
}