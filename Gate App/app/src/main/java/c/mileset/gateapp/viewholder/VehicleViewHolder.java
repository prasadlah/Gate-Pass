package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvFlatNumber, tvVehicleNumber;
    public ImageButton ibDelete, ibEdit;

    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvFlatNumber = (TextView) itemView.findViewById(R.id.tvFlatNumber);
        tvVehicleNumber = (TextView) itemView.findViewById(R.id.tvVehicleNumber);
        ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
        ibEdit = (ImageButton) itemView.findViewById(R.id.ibEdit);
    }
}
