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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import c.mileset.gateapp.adapter.FeedbackAdapter;
import c.mileset.gateapp.model.Feedback;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class FeedbackActivity extends AppCompatActivity {

    private TextFieldBoxes feedbackTextField;
    private ExtendedEditText feedbackExtendedText;
    private Button btnWriteUs;
    private RecyclerView recyclerFeedback;

    public FirebaseFirestore mFirestore;
    Feedback feedback;
    Intent intent;

    ArrayList<Feedback> feedbackArrayList;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFirestore = FirebaseFirestore.getInstance();
        feedbackTextField = (TextFieldBoxes) findViewById(R.id.feedback_text_field_box);
        feedbackExtendedText = (ExtendedEditText) findViewById(R.id.feedback_extended_text);
        btnWriteUs = (Button) findViewById(R.id.btnWriteUs);
        recyclerFeedback = (RecyclerView) findViewById(R.id.recyclerFeedback);

        feedback = new Feedback();
        feedbackArrayList = new ArrayList<Feedback>();

        btnWriteUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback.setFeedback(feedbackExtendedText.getText().toString().trim());
                giveFeedback(feedback);
                getData();
            }
        });

        setRecycler();
        getData();
    }

    private void giveFeedback(Feedback feedback){
        feedback.getFeedback();
        mFirestore.collection("Register")
                .document(intent.getStringExtra("userId").toString())
                .collection("Feedback")
                .add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FeedbackActivity.this, "Feddback Submited", Toast.LENGTH_SHORT).show();
                        clearAll();
                        getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                    }
                });
    }

    private void setRecycler(){
        recyclerFeedback.setHasFixedSize(true);
        recyclerFeedback.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    }

    public void getData(){
        feedbackArrayList.clear();
        intent = getIntent();

        final DocumentReference documentReference;
        documentReference = mFirestore.collection("Register").document(intent.getStringExtra("userId"));
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");
                        documentReference.collection("Feedback")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                                            Feedback feedback = new Feedback();
                                            feedback.setId(queryDocumentSnapshot.getId());
                                            feedback.setFeedback(queryDocumentSnapshot.getString("feedback"));

                                            feedbackArrayList.add(feedback);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FeedbackActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        feedbackAdapter = new FeedbackAdapter(FeedbackActivity.this, feedbackArrayList, name);
                        recyclerFeedback.setAdapter(feedbackAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FeedbackActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll(){
        feedbackExtendedText.setText("");
    }
}