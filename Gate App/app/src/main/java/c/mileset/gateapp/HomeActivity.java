package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import c.mileset.gateapp.adapter.NotificationAdapter;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.model.UserNotification;

public class HomeActivity extends AppCompatActivity {

    private TextView lblFlatNumber, lblName, lblMobileNumber, lblEmail;
    private Button btnContact, btnGatePass, btnComplaint, btnFeedback, btnEdit;
    private ImageButton btnNotification, btnSettings, btnLogout;
    private ImageView imgProfile;
    private LinearLayout familyLayout, vehicleLayout,visitorLayout;
    private RecyclerView recyclerNotification;
    private Intent intent;

    FirebaseFirestore mFirestore;
    UserNotification userNotification;
    GatePass gatePass;
    ArrayList<UserNotification> userNotificationArrayList;
    ArrayList<GatePass> gatePassArrayList;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirestore = FirebaseFirestore.getInstance();
        intent = getIntent();

        userNotificationArrayList = new ArrayList<>();
        gatePassArrayList = new ArrayList<>();

        final String userId = intent.getStringExtra("userId");
        String mobileNumber = intent.getStringExtra("mobile");

        lblFlatNumber = (TextView) findViewById(R.id.lblFlatNumber);
        lblName = (TextView) findViewById(R.id.lblName);
        lblMobileNumber = (TextView) findViewById(R.id.lblMobileNumber);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        btnContact = (Button) findViewById(R.id.btnContact);
        btnGatePass = (Button) findViewById(R.id.btnGatePass);
        btnComplaint = (Button) findViewById(R.id.btnComplaint);
        btnFeedback = (Button) findViewById(R.id.btnFeedback);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        btnNotification = (ImageButton) findViewById(R.id.btnNotification);
        btnSettings = (ImageButton) findViewById(R.id.btnSetting);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);

        familyLayout = (LinearLayout) findViewById(R.id.familyLayout);
        vehicleLayout = (LinearLayout) findViewById(R.id.vehicleLayout);
        visitorLayout = (LinearLayout) findViewById(R.id.visitorLayout);

        recyclerNotification = (RecyclerView) findViewById(R.id.recyclerNotification);

        familyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, FamilyActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        vehicleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, VehicleActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        visitorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Contact Page", Toast.LENGTH_SHORT).show();
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, ComplaintActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnGatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, GatePassActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.removeExtra("userId");
                intent.removeExtra("mobileNumber");
                startActivity(intent);
                finish();
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, NotificationActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        getDate(userId);
        getNotification(userId);
    }

    private void setRecycler(){
        recyclerNotification.setHasFixedSize(true);
        recyclerNotification.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void getNotification(final String userId){

        userNotificationArrayList.clear();
        gatePassArrayList.clear();

        CollectionReference collectionReference;
        collectionReference = mFirestore.collection("Register")
                .document(userId)
                .collection("Notification");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            userNotification = new UserNotification();
                            userNotification.setNotificationId(queryDocumentSnapshot.getId());
                            userNotification.setPass_id(queryDocumentSnapshot.getString("pass_id"));
                            userNotification.setScan_time(queryDocumentSnapshot.getString("scan_time"));

                            final DocumentReference documentReference;
                            documentReference = mFirestore.collection("Register")
                                    .document(userId)
                                    .collection("Gate Pass")
                                    .document(userNotification.getPass_id());
                            documentReference.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(!task.getResult().equals(null)){
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                gatePass = new GatePass();
                                                gatePass.setName(documentSnapshot.getString("name"));
                                                gatePass.setEmail(documentSnapshot.getString("email"));
                                                gatePass.setMobile(documentSnapshot.getString("mobile"));

                                                userNotificationArrayList.add(userNotification);
                                                gatePassArrayList.add(gatePass);

                                                notificationAdapter = new NotificationAdapter(HomeActivity.this, userNotificationArrayList, gatePassArrayList);
                                                recyclerNotification.setAdapter(notificationAdapter);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                    }
                });
    }


    private void getDate(String userId){
        final DocumentReference documentReference = mFirestore.collection("Register")
                .document(userId);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot reference = task.getResult();
                            lblName.setText(reference.getString("name"));
                            lblMobileNumber.setText(reference.getString("mobileNumber"));
                            lblEmail.setText(reference.getString("email"));

                            documentReference.collection("Profile")
                                    .document("profile")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot ds = task.getResult();
                                                if(ds != null){
                                                    Picasso.with(HomeActivity.this)
                                                            .load(ds.getString("image_url"))
                                                            .fit()
                                                            .centerCrop()
                                                            .into(imgProfile);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}