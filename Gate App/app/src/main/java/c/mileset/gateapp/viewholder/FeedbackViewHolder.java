package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class FeedbackViewHolder extends RecyclerView.ViewHolder {

    public TextView tvFeedback, tvName, tvFlatNumber;
    public ImageButton ibDelete;

    public FeedbackViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFeedback = (TextView) itemView.findViewById(R.id.tvFeedback);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvFlatNumber = (TextView) itemView.findViewById(R.id.tvFlatNumber);
        ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
    }
}
