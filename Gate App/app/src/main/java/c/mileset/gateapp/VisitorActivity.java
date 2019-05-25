package c.mileset.gateapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import c.mileset.gateapp.adapter.GatePassAdapter;
import c.mileset.gateapp.adapter.VisitorAdapter;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.model.UserNotification;

public class VisitorActivity extends AppCompatActivity {

    private RecyclerView visitorRecyclerView;

    private Intent intent;
    private FirebaseFirestore mFirestore;
    private ArrayList<GatePass> gatePassArrayList;

    private final Activity activity = this;

    UserNotification userNotification;
    GatePass gatePass;
    VisitorAdapter visitorAdapter;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);

        intent = new Intent();
        intent = getIntent();
        String userId = intent.getStringExtra("userId");
//        gatePass = new GatePass();
        gatePassArrayList = new ArrayList<GatePass>();
        mFirestore = FirebaseFirestore.getInstance();

        visitorRecyclerView = (RecyclerView) findViewById(R.id.visitorRecycler);

        setRecycler();
        getData(userId);
    }

    protected void setRecycler(){
        visitorRecyclerView.setHasFixedSize(true);
        visitorRecyclerView.setLayoutManager(new LinearLayoutManager(VisitorActivity.this, LinearLayout.VERTICAL, false));
    }

    protected void getData(String userId){
        gatePassArrayList.clear();
        final CollectionReference collectionReference;
        collectionReference = mFirestore.collection("Register")
                .document(userId)
                .collection("Gate Pass");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (final QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                            String regId = queryDocumentSnapshot.getId();
                            gatePass = new GatePass();
                            gatePass.setName(queryDocumentSnapshot.getString("name"));
                            gatePass.setEmail(queryDocumentSnapshot.getString("email"));
                            gatePass.setMobile(queryDocumentSnapshot.getString("mobile"));
                            gatePass.setVisit_date(queryDocumentSnapshot.getString("visit_date"));
                            System.out.println("\n\n ======================");
                            System.out.println("Name : " + gatePass.getName());
                            System.out.println("Email : " + gatePass.getEmail());
                            System.out.println("Mobile : " + gatePass.getMobile());
                            System.out.println("Visit Date : " + gatePass.getVisit_date());
                            System.out.println("\n\n");
                            gatePassArrayList.add(gatePass);
                        }

                        visitorAdapter = new VisitorAdapter(VisitorActivity.this, gatePassArrayList);
                        visitorRecyclerView.setAdapter(visitorAdapter);
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
