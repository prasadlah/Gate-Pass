package c.mileset.gateapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import c.mileset.gateapp.adapter.NotificationAdapter;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.model.UserNotification;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notificationRecycler;
    private String userId;

    public FirebaseFirestore mFirestore;

    UserNotification userNotification;
    GatePass gatePass;
    NotificationAdapter notificationAdapter;

    ArrayList<UserNotification> userNotificationArrayList;
    ArrayList<GatePass> gatePassArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        userId = getIntent().getStringExtra("userId");
        mFirestore = FirebaseFirestore.getInstance();

        userNotificationArrayList = new ArrayList<>();
        gatePassArrayList = new ArrayList<>();

        notificationRecycler = (RecyclerView) findViewById(R.id.notificationRecycler);

        setRecycler();
        getNotification(userId);
    }

    private void setRecycler(){
        notificationRecycler.setHasFixedSize(true);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
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

                                                notificationAdapter = new NotificationAdapter(NotificationActivity.this, userNotificationArrayList, gatePassArrayList);
                                                notificationRecycler.setAdapter(notificationAdapter);
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
}