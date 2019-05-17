package c.mileset.gateapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import c.mileset.gateapp.ImagesActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.Demo;
import c.mileset.gateapp.viewholder.ImageViewHolder;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private ImagesActivity imagesActivity;
    private List<Demo> demoList;

    public ImageAdapter(ImagesActivity imagesActivity, List<Demo> demoList) {
        this.imagesActivity = imagesActivity;
        this.demoList = demoList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(imagesActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.image_view, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Demo demo = demoList.get(i);
        imageViewHolder.txtName.setText(demo.getName());
        Picasso.with(imagesActivity.getBaseContext())
                .load(demo.getImgUrl())
                .fit()
                .centerCrop()
                .into(imageViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return demoList.size();
    }
}
