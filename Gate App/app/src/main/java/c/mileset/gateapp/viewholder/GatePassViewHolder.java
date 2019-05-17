package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class GatePassViewHolder extends RecyclerView.ViewHolder {

    public TextView tvFlatNumber, tvName, tvVisitDate, tvVisitTime;
    public ImageView imgImage;

    public GatePassViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFlatNumber = (TextView) itemView.findViewById(R.id.tvFlatNumber);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvVisitDate = (TextView) itemView.findViewById(R.id.tvVisitDate);
        tvVisitTime = (TextView) itemView.findViewById(R.id.tvVisitTime);
        imgImage = (ImageView) itemView.findViewById(R.id.imgFamily);
    }
}
