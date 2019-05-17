package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    public TextView tvVisitorName, tvScanDate, tvMobileNumber, tvEmail;
    public Button btnAllow, btnDeny;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        tvVisitorName = (TextView) itemView.findViewById(R.id.tvVisitorName);
        tvScanDate = (TextView) itemView.findViewById(R.id.tvScanDate);
        tvMobileNumber = (TextView) itemView.findViewById(R.id.tvMobileNumber);
        tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
        btnAllow = (Button) itemView.findViewById(R.id.btnAllow);
        btnDeny = (Button) itemView.findViewById(R.id.btnDeny);
    }
}
