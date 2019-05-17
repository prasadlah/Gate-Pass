package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class ComplaintViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvFlatNumber, tvSubject, tvComplaint;
    public ImageButton btnDelete;

    public ComplaintViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvFlatNumber = (TextView) itemView.findViewById(R.id.tvFlatNumber);
        tvSubject = (TextView) itemView.findViewById(R.id.tvSubject);
        tvComplaint = (TextView) itemView.findViewById(R.id.tvComplaint);
        btnDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
    }
}
