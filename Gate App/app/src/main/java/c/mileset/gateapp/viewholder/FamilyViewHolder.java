package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class FamilyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFlatNumber, tvName, tvOccupation, tvEmail, tvMobileNumber, tvRelationToOwner;
    public ImageView imgFamily;
    public ImageButton btnEdit, btnDelete;

    public FamilyViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvOccupation = (TextView) itemView.findViewById(R.id.tvOccupation);
        tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
        tvMobileNumber = (TextView) itemView.findViewById(R.id.tvMobileNumber);
        tvRelationToOwner = (TextView) itemView.findViewById(R.id.tvRelationToOwner);
        tvFlatNumber = (TextView) itemView.findViewById(R.id.tvFlatNumber);
        imgFamily = (ImageView) itemView.findViewById(R.id.imgFamily);
        btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
    }
}
