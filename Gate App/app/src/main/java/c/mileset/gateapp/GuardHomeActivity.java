package c.mileset.gateapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import c.mileset.gateapp.adapter.GatePassAdapter;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.model.UserNotification;

public class GuardHomeActivity extends AppCompatActivity {

    private ImageButton ibSetting, ibLogout;
    private Button btnEdit, btnScanQr;
    private TextView tvName, tvMobileNumber, tvAadharNumber, temp;
    private ImageView imgProfile;
    private RecyclerView recyclerGatePass, recyclerVisitorStatus;

    private Intent intent;
    private FirebaseFirestore mFirestore;
    private ArrayList<GatePass> gatePassArrayList;

    private final Activity activity = this;

    UserNotification userNotification;
    GatePass gatePass;
    GatePassAdapter gatePassAdapter;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_home);

        intent = new Intent();
        intent = getIntent();
//        gatePass = new GatePass();
        gatePassArrayList = new ArrayList<GatePass>();
        mFirestore = FirebaseFirestore.getInstance();

        ibSetting = (ImageButton) findViewById(R.id.ibSetting);
        ibLogout = (ImageButton) findViewById(R.id.ibLogout);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnScanQr = (Button) findViewById(R.id.btnScanQrCode);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
        tvAadharNumber = (TextView) findViewById(R.id.tvAdhaarNumber);
        temp = (TextView) findViewById(R.id.temp);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        recyclerGatePass = (RecyclerView) findViewById(R.id.recyclerGatePass);
        recyclerVisitorStatus = (RecyclerView) findViewById(R.id.recyclerVisitorStatus);

        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GuardHomeActivity.this, MainActivity.class);
                intent.removeExtra("userName");
                startActivity(intent);
                finish();
            }
        });

        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQrCode();
            }
        });

        setRecyclerView();
        getGatePassData();
    }

    private void setRecyclerView(){
        recyclerGatePass.setHasFixedSize(false);
        recyclerGatePass.setLayoutManager(new LinearLayoutManager(GuardHomeActivity.this ,LinearLayout.HORIZONTAL, true));
    }

    private void getGatePassData(){
        gatePassArrayList.clear();
        final CollectionReference collectionReference;
        collectionReference = mFirestore.collection("Register");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for(final QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            String regId = queryDocumentSnapshot.getId();
                            collectionReference.document(regId)
                                    .collection("Gate Pass")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot_1) {
                                            for(QueryDocumentSnapshot queryDocumentSnapshot_1 : querySnapshot_1) {
                                                gatePass = new GatePass();
                                                gatePass.setName(queryDocumentSnapshot_1.getString("name"));
                                                gatePass.setVisit_date(queryDocumentSnapshot_1.getString("visit_date"));
                                                gatePass.setVisit_time(queryDocumentSnapshot_1.getString("visit_time"));

                                                System.out.println("\n\n ======================");
                                                System.out.println("Name : " + gatePass.getName());
                                                System.out.println("Visit Date : " + gatePass.getVisit_date());
                                                System.out.println("Visit Time : " + gatePass.getVisit_time());
                                                System.out.println("\n\n");
                                                gatePassArrayList.add(gatePass);
                                            }
                                            gatePassAdapter = new GatePassAdapter(GuardHomeActivity.this, gatePassArrayList);
                                            recyclerGatePass.setAdapter(gatePassAdapter);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println("Error : " + e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error : " + e.getMessage());
                    }
                });

    }

    private void scanQrCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scann");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if(intentResult.getContents() == null)
                Toast.makeText(this, "you cancelled scanning", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(this, "Code = " + intentResult.getContents(), Toast.LENGTH_SHORT).show();
                String code = intentResult.getContents();
                temp.setText(code);
                sendNotification(code);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendNotification(final String code){
        final String scanTime = new SimpleDateFormat("hh:mm:ss a").format(calendar.getTime());
        userNotification = new UserNotification();
        final CollectionReference collectionReference;
        collectionReference = mFirestore.collection("Register");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            final String regId = queryDocumentSnapshot.getId();
                            collectionReference.document(regId)
                                    .collection("Gate Pass")
                                    .whereEqualTo("pass_code", code)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                                userNotification.setPass_id(snapshot.getId());
                                                userNotification.setReg_id(regId);
                                                userNotification.setScan_time(scanTime);
                                                userNotification.setPermission(0);
                                                userNotification.setIn_entry_time(scanTime);

                                                mFirestore.collection("Register")
                                                        .document(regId)
                                                        .collection("Notification")
                                                        .add(userNotification)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(activity, "Send Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(activity, "Something Wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                temp.setText(regId + "=" +snapshot.getId());
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println(" \n\n\n\n Error : " + e.getMessage() + "\n\n\n\n");
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}