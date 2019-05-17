package c.mileset.gateapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import c.mileset.gateapp.model.User;
import c.mileset.gateapp.util.SendSms;
import okio.Timeout;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ForgetActivity extends AppCompatActivity {

    private TextFieldBoxes mobileTextField, otpTextField;
    private ExtendedEditText mobileExtendedText, otpExtendedText;
    private Button btnGenerateOtp, btnVerify;

    private String mobileNumber, strOtp, verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    int counter = 0;

    Intent intent;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private Query mQuery;
    User user;
    SendSms sendSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        intent = new Intent();
        mFirestore = FirebaseFirestore.getInstance();

        mobileTextField = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        otpTextField = (TextFieldBoxes) findViewById(R.id.otp_text_field);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        otpExtendedText = (ExtendedEditText) findViewById(R.id.otp_extended_text);
        btnGenerateOtp = (Button) findViewById(R.id.btnGenerateOtp);
        btnVerify = (Button) findViewById(R.id.btnVerify);

        btnGenerateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber = mobileExtendedText.getText().toString();
                mFirestore.collection("Register")
                        .whereEqualTo("mobileNumber", mobileNumber)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshots) {
                                strOtp = "2121";
                                sendSms = new SendSms(mobileNumber, strOtp);
                                //sendSms.send();
                                sendSms.execute();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetActivity.this, "Sorry your number is not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpExtendedText.getText().toString().equals(strOtp)){
                    Toast.makeText(ForgetActivity.this, "Validate", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgetActivity.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                    counter++;
                    if(counter > 3){
                        Toast.makeText(ForgetActivity.this, "You are block", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
