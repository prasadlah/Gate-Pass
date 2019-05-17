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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import c.mileset.gateapp.adapter.VehicleAdapter;
import c.mileset.gateapp.model.Vehicle;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class VehicleActivity extends AppCompatActivity {

    public TextView tvVehicleId;
    private TextFieldBoxes vehicleNumberTextField;
    public ExtendedEditText vehicleNumberExtendedText;
    public Button btnSave;
    private RecyclerView recyclerVehicle;

    public FirebaseFirestore mFirestore;
    public Intent intent;
    Vehicle vehicle;
    ArrayList<Vehicle> vehicleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        intent = getIntent();
        mFirestore = FirebaseFirestore.getInstance();
        vehicle = new Vehicle();
        vehicleArrayList = new ArrayList<Vehicle>();

        tvVehicleId = (TextView) findViewById(R.id.tv_vehicle_id);
        vehicleNumberTextField = (TextFieldBoxes) findViewById(R.id.vehicle_number_text_field_box);
        vehicleNumberExtendedText = (ExtendedEditText) findViewById(R.id.vehicle_number_extended_text);
        btnSave = (Button) findViewById(R.id.btnSave);
        recyclerVehicle = (RecyclerView) findViewById(R.id.recyclerVehicle);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvVehicleId.getText().toString().equals("") || tvVehicleId.getText().toString().equals(null)) {
                    if (vehicleNumberExtendedText.getText().toString().equals("") || vehicleNumberExtendedText.getText().toString().equals(null)) {
                        getMessage("All Fields Are Required!!!");
                    } else {
                        vehicle.setVehicleNumber(vehicleNumberExtendedText.getText().toString().trim());
                        saveVehicle(vehicle);
                    }
                }
                else {
                    vehicle.setId(tvVehicleId.getText().toString());
                    vehicle.setVehicleNumber(vehicleNumberExtendedText.getText().toString());
                    updateVehicle(vehicle);
                }
            }
        });

        setRecycler();
        getData();
    }

    private void saveVehicle(Vehicle vehicle){
        vehicle.getVehicleNumber();
        mFirestore.collection("Register").document(intent.getStringExtra("userId"))
                .collection("Vehicle")
                .add(vehicle)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getMessage("Vehicle Added Successfully!!!");
                        clearAll();
                        getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                        getMessage("Error : " + e.getMessage());
                    }
                });
    }

    private void updateVehicle(Vehicle vehicle){
        mFirestore.collection("Register")
                .document(intent.getStringExtra("userId"))
                .collection("Vehicle")
                .document(vehicle.getId())
                .update("vehicleNumber", vehicle.getVehicleNumber())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        clearAll();
                        btnSave.setText("Save");
                        tvVehicleId.setText("");
                        getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void setRecycler(){
        recyclerVehicle.setHasFixedSize(true);
        recyclerVehicle.setLayoutManager(new LinearLayoutManager(VehicleActivity.this, LinearLayoutManager.HORIZONTAL, true));
    }

    public void getData(){
        vehicleArrayList.clear();
        final DocumentReference documentReference;
        documentReference = mFirestore.collection("Register").document(intent.getStringExtra("userId"));
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");

                        documentReference.collection("Vehicle")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                                            vehicle.setId(queryDocumentSnapshot.getId());
                                            vehicle.setVehicleNumber(queryDocumentSnapshot.getString("vehicleNumber"));

                                            vehicleArrayList.add(vehicle);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.v("Error : ", e.getMessage());
                                        getMessage("Error : " + e.getMessage() + " \n Vehicle Not Present");
                                    }
                                });
                        VehicleAdapter vehicleAdapter = new VehicleAdapter(VehicleActivity.this, vehicleArrayList, name);
                        recyclerVehicle.setAdapter(vehicleAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                        getMessage("Error : " + e.getMessage());
                    }
                });
    }

    private void setRecyclerVehicle(){
        recyclerVehicle.setHasFixedSize(true);
        recyclerVehicle.setLayoutManager(new LinearLayoutManager(VehicleActivity.this, LinearLayoutManager.HORIZONTAL, true));
    }

    private void getMessage(String message){
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll(){
        vehicleNumberExtendedText.setText("");
    }
}
