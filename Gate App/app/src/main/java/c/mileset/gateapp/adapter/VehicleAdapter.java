package c.mileset.gateapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import c.mileset.gateapp.R;
import c.mileset.gateapp.VehicleActivity;
import c.mileset.gateapp.model.Vehicle;
import c.mileset.gateapp.viewholder.VehicleViewHolder;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    String name;
    VehicleActivity vehicleActivity;
    ArrayList<Vehicle> vehicleArrayList;

    public VehicleAdapter(VehicleActivity vehicleActivity, ArrayList<Vehicle> vehicleArrayList, String name) {
        this.name = name;
        this.vehicleActivity = vehicleActivity;
        this.vehicleArrayList = vehicleArrayList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(vehicleActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.vehicle_view, viewGroup, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VehicleViewHolder vehicleViewHolder, final int i) {
        vehicleViewHolder.tvVehicleNumber.setText(vehicleArrayList.get(i).getVehicleNumber());

        vehicleViewHolder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleActivity.tvVehicleId.setText(vehicleArrayList.get(i).getId());
                vehicleActivity.vehicleNumberExtendedText.setText(vehicleViewHolder.tvVehicleNumber.getText());
                vehicleActivity.btnSave.setText("Update");
            }
        });

        vehicleViewHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleActivity.mFirestore.collection("Register")
                        .document(vehicleActivity.getIntent().getStringExtra("userId"))
                        .collection("Vehicle")
                        .document(vehicleArrayList.get(i).getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(vehicleActivity, "Deleted", Toast.LENGTH_SHORT).show();
                                vehicleActivity.getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error : ", e.getMessage());
                                Toast.makeText(vehicleActivity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleArrayList.size();
    }
}
