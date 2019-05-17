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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.mileset.gateapp.FamilyActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.Family;
import c.mileset.gateapp.viewholder.FamilyViewHolder;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {

    FamilyActivity familyActivity;
    ArrayList<Family> familyArrayList;

    public FamilyAdapter(FamilyActivity familyActivity, ArrayList<Family> familyArrayList) {
        this.familyActivity = familyActivity;
        this.familyArrayList = familyArrayList;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(familyActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.family_view, viewGroup, false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FamilyViewHolder familyViewHolder, final int i) {
        familyViewHolder.tvName.setText(familyArrayList.get(i).getName());
        familyViewHolder.tvOccupation.setText(familyArrayList.get(i).getOccupation());
        familyViewHolder.tvMobileNumber.setText(familyArrayList.get(i).getMobileNumber());
        familyViewHolder.tvEmail.setText(familyArrayList.get(i).getEmail());
        Picasso.with(familyActivity.getBaseContext())
                .load(familyArrayList.get(i).getImgUrl())
                .fit()
                .centerCrop()
                .into(familyViewHolder.imgFamily);

        familyViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyActivity.nameExtendedText.setText(familyViewHolder.tvName.getText().toString());
                familyActivity.emailExtendedText.setText(familyViewHolder.tvEmail.getText().toString());
                familyActivity.mobileExtentedText.setText(familyViewHolder.tvMobileNumber.getText().toString());
                familyActivity.relationToOwnerExtendedText.setText(familyViewHolder.tvRelationToOwner.getText());
                familyActivity.occupationExtendedText.setText(familyViewHolder.tvOccupation.getText().toString());
                familyActivity.lblId.setText(familyArrayList.get(i).getId());
                familyActivity.btnSave.setText("Update");

            }
        });

        familyViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyActivity.mFirestore.collection("Register")
                        .document(familyActivity.intent.getStringExtra("userId"))
                        .collection("Family")
                        .document(familyArrayList.get(i).getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(familyActivity, "Deleted", Toast.LENGTH_SHORT).show();
                                familyActivity.getData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error : ", e.getMessage());
                                Toast.makeText(familyActivity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return familyArrayList.size();
    }
}
