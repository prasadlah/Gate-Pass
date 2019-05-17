package c.mileset.gateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import c.mileset.gateapp.adapter.ComplaintAdapter;
import c.mileset.gateapp.model.Complaint;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ComplaintActivity extends AppCompatActivity {

    private TextFieldBoxes subjectTextField, complaintTextField;
    private ExtendedEditText subjectExtendedText, complaintExtendedText;
    private Button btnUriteUs;
    private RecyclerView recyclerComplaint;

    public FirebaseFirestore mFirestore;
    Intent intent;

    Complaint complaint;
    ArrayList<Complaint> complaintArrayList;
    ComplaintAdapter complaintAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        intent = getIntent();
        mFirestore = FirebaseFirestore.getInstance();
        complaintArrayList = new ArrayList<Complaint>();
        complaint = new Complaint();

        subjectTextField = (TextFieldBoxes) findViewById(R.id.subject_text_field_box);
        complaintTextField = (TextFieldBoxes) findViewById(R.id.complaint_text_field_box);
        subjectExtendedText = (ExtendedEditText) findViewById(R.id.subject_extended_text);
        complaintExtendedText = (ExtendedEditText) findViewById(R.id.complaint_extended_text);
        btnUriteUs = (Button) findViewById(R.id.btnWriteUs);
        recyclerComplaint = (RecyclerView) findViewById(R.id.recyclerComplaint);

        btnUriteUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subjectExtendedText.getText().toString().equals("") || subjectExtendedText.getText().toString().equals(null)
                || complaintExtendedText.getText().toString().equals("") || complaintExtendedText.getText().toString().equals(null)){
                    getMessage("All Fields Are Required");
                }
                else{
                    complaint.setSubject(subjectExtendedText.getText().toString().trim());
                    complaint.setComplaint(complaintExtendedText.getText().toString().trim());
                    giveComplaint(complaint);
                    getData();
                }
            }
        });

        setRecycler();
        getData();
    }

    private void giveComplaint(Complaint complaint) {
        complaint.getSubject();
        complaint.getComplaint();
        mFirestore.collection("Register").document(intent.getStringExtra("userId"))
                .collection("Complaints")
                .add(complaint)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getMessage("Your Complaint is submitted successfully");
                        clearAll();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void setRecycler(){
        recyclerComplaint.setHasFixedSize(true);
        recyclerComplaint.setLayoutManager(new LinearLayoutManager(ComplaintActivity.this, LinearLayoutManager.HORIZONTAL, true));
    }

    public void getData(){
        complaintArrayList.clear();
        final DocumentReference mQuery;
        mQuery = mFirestore.collection("Register").document(intent.getStringExtra("userId"));
        mQuery.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");

                        mQuery.collection("Complaints")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                                            complaint.setC_id(queryDocumentSnapshot.getId());
                                            complaint.setSubject(queryDocumentSnapshot.getString("subject"));
                                            complaint.setComplaint(queryDocumentSnapshot.getString("complaint"));

                                            complaintArrayList.add(complaint);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ComplaintActivity.this, "Can't Fetch", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        complaintAdapter = new ComplaintAdapter(ComplaintActivity.this, complaintArrayList, name);
                        recyclerComplaint.setAdapter(complaintAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ComplaintActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll() {
        subjectExtendedText.setText(null);
        complaintExtendedText.setText(null);
    }

    private void getMessage(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}