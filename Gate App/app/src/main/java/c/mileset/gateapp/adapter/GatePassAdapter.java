package c.mileset.gateapp.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import c.mileset.gateapp.GuardHomeActivity;
import c.mileset.gateapp.R;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.viewholder.GatePassViewHolder;

public class GatePassAdapter extends RecyclerView.Adapter<GatePassViewHolder> {

    GuardHomeActivity guardHomeActivity;
    ArrayList<GatePass> gatePassArrayList;
    QRGEncoder qrgEncoder;

    public GatePassAdapter(GuardHomeActivity guardHomeActivity, ArrayList<GatePass> gatePassArrayList) {
        this.guardHomeActivity = guardHomeActivity;
        this.gatePassArrayList = gatePassArrayList;
    }

    @NonNull
    @Override
    public GatePassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(guardHomeActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.gatepass_view, viewGroup, false);
        return new GatePassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GatePassViewHolder gatePassViewHolder, int i) {
        gatePassViewHolder.tvName.setText(gatePassArrayList.get(i).getName());
        gatePassViewHolder.tvVisitDate.setText(gatePassArrayList.get(i).getVisit_date());
        gatePassViewHolder.tvVisitTime.setText(gatePassArrayList.get(i).getVisit_time());

        qrgEncoder = new QRGEncoder(""+gatePassArrayList.get(i), null, QRGContents.Type.TEXT, 10);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            gatePassViewHolder.imgImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return gatePassArrayList.size();
    }
}
