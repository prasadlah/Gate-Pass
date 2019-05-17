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
import java.util.zip.Inflater;

import c.mileset.gateapp.NotificationActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.model.UserNotification;
import c.mileset.gateapp.viewholder.NotificationViewHolder;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    NotificationActivity notificationActivity;
    ArrayList<UserNotification> userNotificationArrayList;
    ArrayList<GatePass> gatePassArrayList;

    public NotificationAdapter(NotificationActivity notificationActivity, ArrayList<UserNotification> userNotificationArrayList, ArrayList<GatePass> gatePassArrayList) {
        this.notificationActivity = notificationActivity;
        this.userNotificationArrayList = userNotificationArrayList;
        this.gatePassArrayList = gatePassArrayList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(notificationActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.notification_layout, viewGroup, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, final int i) {
        notificationViewHolder.tvVisitorName.setText(gatePassArrayList.get(i).getName());
        notificationViewHolder.tvMobileNumber.setText(gatePassArrayList.get(i).getMobile());
        System.out.println("email: "+gatePassArrayList.get(i).getEmail());
        notificationViewHolder.tvEmail.setText(gatePassArrayList.get(i).getEmail());
        notificationViewHolder.tvScanDate.setText(userNotificationArrayList.get(i).getScan_time());

        notificationViewHolder.btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationActivity.mFirestore.collection("Register")
                        .document(notificationActivity.getIntent().getStringExtra("userId"))
                        .collection("Notification")
                        .document(userNotificationArrayList.get(i).getNotificationId())
                        .update("permission", 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.v("Success : ", "Visitor Allowed");
                                Toast.makeText(notificationActivity, "Visited Allowed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error : ", e.getMessage());
                            }
                        });
            }
        });

        notificationViewHolder.btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = notificationActivity.getIntent().getStringExtra("userId");
                notificationActivity.mFirestore.collection("Register")
                        .document(userId)
                        .collection("Notification")
                        .document(userNotificationArrayList.get(i).getNotificationId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.v("Success : ", "Visitor Not Allowed");
                                Toast.makeText(notificationActivity, "Visited Not Allowed", Toast.LENGTH_SHORT).show();
                                notificationActivity.getNotification(userId);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error : ", e.getMessage());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNotificationArrayList.size();
    }
}
