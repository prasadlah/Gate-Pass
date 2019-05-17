package c.mileset.gateapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.mileset.gateapp.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView img;
    public TextView txtName;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        img = (ImageView) itemView.findViewById(R.id.img);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
    }
}
