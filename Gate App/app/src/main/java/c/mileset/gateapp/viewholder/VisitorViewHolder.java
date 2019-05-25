package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class VisitorViewHolder extends RecyclerView.ViewHolder {

    public TextView tvVisitorName, tvVisitorEmail, tvVisitorMobile, tvVisitorVisitDate;
    public ImageView imgQrCode;

    public VisitorViewHolder(@NonNull View itemView) {
        super(itemView);

        tvVisitorName = (TextView) itemView.findViewById(R.id.tvVisitorName);
        tvVisitorEmail = (TextView) itemView.findViewById(R.id.tvVisitorEmail);
        tvVisitorMobile = (TextView) itemView.findViewById(R.id.tvVisitorMobileNumber);
        tvVisitorVisitDate = (TextView) itemView.findViewById(R.id.tvVisitorVisitDate);
        imgQrCode = (ImageView) itemView.findViewById(R.id.imgQrCode);
    }
}
