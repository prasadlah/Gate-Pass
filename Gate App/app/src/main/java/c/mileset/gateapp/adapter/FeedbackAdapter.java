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

import c.mileset.gateapp.FeedbackActivity;
import c.mileset.gateapp.HomeActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.Feedback;
import c.mileset.gateapp.viewholder.FeedbackViewHolder;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackViewHolder> {

    String name;
    FeedbackActivity feedbackActivity;
    ArrayList<Feedback> feedbackArrayList;


    public FeedbackAdapter(FeedbackActivity feedbackActivity, ArrayList<Feedback> feedbackArrayList, String name) {
        this.feedbackActivity = feedbackActivity;
        this.feedbackArrayList = feedbackArrayList;
        this.name = name;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(feedbackActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.feedback_view, viewGroup, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, final int i) {
        feedbackViewHolder.tvFeedback.setText(feedbackArrayList.get(i).getFeedback());
        feedbackViewHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelected(i);
            }
        });
    }

    private void deleteSelected(int i){
        feedbackActivity.mFirestore.collection("Register")
                .document(feedbackActivity.getIntent().getStringExtra("userId"))
                .collection("Feedback")
                .document(feedbackArrayList.get(i).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(feedbackActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        feedbackActivity.getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error : ", e.getMessage());
                        Toast.makeText(feedbackActivity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }
}
