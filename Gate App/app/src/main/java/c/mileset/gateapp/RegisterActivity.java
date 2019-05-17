package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import c.mileset.gateapp.model.FlatNumber;
import c.mileset.gateapp.model.Society;
import c.mileset.gateapp.model.User;
import c.mileset.gateapp.model.Wing;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

//===============   Validation Required (Pending)     ===============//
public class RegisterActivity extends AppCompatActivity {

    private Spinner spinnerSociety, spinnerWing, spinnerFlatNumber;
    private TextFieldBoxes nameTextFieldBox, mobileTextFieldBox, emailTextFieldBox, passwordTextFieldBox, confirmPasswordTextFieldBox;
    private ExtendedEditText nameExtendedText, mobileExtendedText, emailExtendedText, passwordExtendedText, confirmPasswordExtendedText;
    private Button btnRegister, btnLogin;

    User user;
    List<Society> listSociety;
    List<Wing> listWing;
    List<FlatNumber> listFlatNumber;

    Society society;
    Wing wing;
    FlatNumber flatNumber;

    FirebaseFirestore mFirestore;
    Task<QuerySnapshot> mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirestore = FirebaseFirestore.getInstance();

        listSociety = new ArrayList<Society>();
        listWing = new ArrayList<Wing>();
        listFlatNumber = new ArrayList<FlatNumber>();

        society = new Society();
        wing = new Wing();
        flatNumber = new FlatNumber();

        spinnerSociety = (Spinner) findViewById(R.id.spinnerSociety);
        spinnerWing = (Spinner) findViewById(R.id.spinnerWing);
        spinnerFlatNumber = (Spinner) findViewById(R.id.spinnerFlatNumber);
        nameTextFieldBox = (TextFieldBoxes) findViewById(R.id.name_text_field_box);
        mobileTextFieldBox = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        emailTextFieldBox = (TextFieldBoxes) findViewById(R.id.email_text_field_box);
        passwordTextFieldBox = (TextFieldBoxes) findViewById(R.id.password_text_field_box);
        confirmPasswordTextFieldBox = (TextFieldBoxes) findViewById(R.id.confirm_password_text_field_box);
        nameExtendedText = (ExtendedEditText) findViewById(R.id.name_extended_text);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        emailExtendedText = (ExtendedEditText) findViewById(R.id.email_extended_text);
        passwordExtendedText = (ExtendedEditText) findViewById(R.id.password_extended_text);
        confirmPasswordExtendedText = (ExtendedEditText) findViewById(R.id.confirm_password_extended_text);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        mFirestore.collection("Society").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listSociety.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    society = new Society();
                    //Society s=(Society)queryDocumentSnapshot;
                    society.setS_id(queryDocumentSnapshot.getId());
                    society.setS_name(queryDocumentSnapshot.getString("societyName"));

                    listSociety.add(society);
                }
                ArrayAdapter<Society> adapterSociety = new ArrayAdapter<Society>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, listSociety);
                adapterSociety.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinnerSociety.setPrompt("Select Society");
                spinnerSociety.setAdapter(adapterSociety);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        spinnerSociety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFirestore.collection("Society")
                        .document(society.getS_id())
                        .collection("Wing")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.getResult().size() > 0) {
                                    listWing.clear();
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        wing = new Wing();
                                        wing.setW_id(queryDocumentSnapshot.getId());
                                        wing.setWing(queryDocumentSnapshot.get("wingName").toString());

                                        listWing.add(wing);
                                    }
                                    ArrayAdapter<Wing> adapterWing = new ArrayAdapter<Wing>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, listWing);
                                    adapterWing.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                    spinnerWing.setPrompt("Select Wing");
                                    spinnerWing.setAdapter(adapterWing);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Not present", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFirestore.collection("Society").document(society.getS_id())
                        .collection("Wing").document(wing.getW_id())
                        .collection("flatInfo").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().size() > 0) {
                                    listFlatNumber.clear();
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        flatNumber = new FlatNumber();
                                        flatNumber.setF_id(queryDocumentSnapshot.getId());
                                        flatNumber.setFlat_number(queryDocumentSnapshot.get("number").toString());

                                        listFlatNumber.add(flatNumber);
                                    }
                                    ArrayAdapter<FlatNumber> adapterFlatNumber = new ArrayAdapter<FlatNumber>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, listFlatNumber);
                                    adapterFlatNumber.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                    spinnerFlatNumber.setPrompt("Select Flat Number");
                                    spinnerFlatNumber.setAdapter(adapterFlatNumber);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Not Present, empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameExtendedText.getText().toString().equals("") || nameExtendedText.getText().toString().equals(null)
                        || emailExtendedText.getText().toString().equals("") || emailExtendedText.getText().toString().equals(null)
                        || mobileExtendedText.getText().toString().equals("") || mobileExtendedText.getText().toString().equals(null)
                        || passwordExtendedText.getText().toString().equals("") || passwordExtendedText.getText().toString().equals(null)
                        || confirmPasswordExtendedText.getText().toString().equals("") || confirmPasswordExtendedText.getText().toString().equals(null)) {
                    getMessage("All Fields Are Required!!!");
                } else {
                    if(mobileExtendedText.getText().length() > 10){
                        getMessage("Wrong Mobile Number!");
                    }
                    else {
                        if (passwordExtendedText.getText().toString().trim().equals(confirmPasswordExtendedText.getText().toString().trim())) {
                            user = new User();
                            user.setName(nameExtendedText.getText().toString().trim());
                            user.setEmail(emailExtendedText.getText().toString().trim());
                            user.setMobileNumber(mobileExtendedText.getText().toString().trim());
                            user.setPassword(passwordExtendedText.getText().toString().trim());
                            user.setFlag(1);
                            registerUser(user);
                        } else {
                            getMessage("Password Not Matched");
                        }
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, OwnerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(User user) {
        mFirestore.collection("Register")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        clearAll();
                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMessage(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll() {
        nameExtendedText.setText("");
        emailExtendedText.setText("");
        mobileExtendedText.setText("");
        passwordExtendedText.setText("");
        confirmPasswordExtendedText.setText("");
    }
}