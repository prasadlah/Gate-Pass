package c.mileset.gateapp;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import c.mileset.gateapp.model.User;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class OwnerLoginActivity extends AppCompatActivity {

    private TextView lblForgetPassword;
    private Button btnLogin, btnRegister;

    private TextFieldBoxes mobileTextFieldBox, passwordTextFieldBox;
    private ExtendedEditText mobileExtendedText, passwordExtendedText;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        mFirestore = FirebaseFirestore.getInstance();

        mobileTextFieldBox = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        passwordTextFieldBox = (TextFieldBoxes) findViewById(R.id.password_text_field_box);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        passwordExtendedText = (ExtendedEditText) findViewById(R.id.password_extended_text);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        lblForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        lblForgetPassword.setPaintFlags(lblForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
                    getMessage("All Fields Are Required!!!");
                }
                else if(mobileExtendedText.length() > 10){
                    mobileTextFieldBox.setError("Incorrect Mobile Number", true);
                }
                else if(passwordExtendedText.length() < 8 || passwordExtendedText.length() > 15){
                    passwordTextFieldBox.setError("Incorrect Password", true);
                }
                else {
                    User user = new User();
                    user.setMobileNumber(mobileExtendedText.getText().toString().trim());
                    user.setPassword(passwordExtendedText.getText().toString().trim());
                    validateUser(user);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lblForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerLoginActivity.this, ForgetActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validateUser(final User user){
        final String mobileNumber = user.getMobileNumber();
        final String password = user.getPassword();

        mQuery = mFirestore.collection("Register");
        mQuery.whereEqualTo("mobileNumber", mobileNumber).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            if (mobileNumber.equals(queryDocumentSnapshot.get("mobileNumber")) && password.equals(queryDocumentSnapshot.get("password"))) {
                                String userId = queryDocumentSnapshot.getId();
                                intent = new Intent(OwnerLoginActivity.this, HomeActivity.class);
                                intent.putExtra("userId", userId);
                                intent.putExtra("mobile", mobileNumber);
                                startActivity(intent);
                                clearAll();
                                finish();
                            } else {
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
                        getMessage("User Not Found");
                    }
                });
    }

    private void getMessage(String message){
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll(){
        mobileExtendedText.setText("");
        passwordExtendedText.setText("");
    }
}