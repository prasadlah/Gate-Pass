package c.mileset.gateapp.adapter;

import android.hardware.camera2.TotalCaptureResult;
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

import c.mileset.gateapp.ComplaintActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.Complaint;
import c.mileset.gateapp.viewholder.ComplaintViewHolder;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintViewHolder> {

    String name;
    ComplaintActivity complaintActivity;
    ArrayList<Complaint> complaintArrayList;

    public ComplaintAdapter(ComplaintActivity complaintActivity, ArrayList<Complaint> complaintArrayList, String name) {
        this.complaintActivity = complaintActivity;
        this.complaintArrayList = complaintArrayList;
        this.name = name;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(complaintActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.complaint_view, viewGroup, false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComplaintViewHolder complaintViewHolder, final int i) {
        complaintViewHolder.tvSubject.setText(complaintArrayList.get(i).getSubject());
        complaintViewHolder.tvComplaint.setText(complaintArrayList.get(i).getComplaint());
        complaintViewHolder.tvName.setText(name);

        complaintViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintActivity.mFirestore.collection("Register")
                        .document(complaintActivity.getIntent().getStringExtra("userId"))
                        .collection("Complaints")
                        .document(complaintArrayList.get(i).getC_id())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(complaintActivity, "Deleted", Toast.LENGTH_SHORT).show();
                                complaintActivity.getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error : ", e.getMessage());
                                Toast.makeText(complaintActivity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintArrayList.size();
    }
}
