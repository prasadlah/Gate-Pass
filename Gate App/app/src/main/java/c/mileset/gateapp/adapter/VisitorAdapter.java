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
import c.mileset.gateapp.R;
import c.mileset.gateapp.VisitorActivity;
import c.mileset.gateapp.model.GatePass;
import c.mileset.gateapp.viewholder.VisitorViewHolder;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorViewHolder> {

    VisitorActivity visitorActivity;
    GatePass gatePass;
    ArrayList<GatePass> gatePassArrayList;
    QRGEncoder qrgEncoder;

    public VisitorAdapter(VisitorActivity visitorActivity, ArrayList<GatePass> gatePassArrayList) {
        this.visitorActivity = visitorActivity;
        this.gatePassArrayList = gatePassArrayList;
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(visitorActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.visitor_view, viewGroup, false);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder visitorViewHolder, int i) {
        visitorViewHolder.tvVisitorName.setText(gatePassArrayList.get(i).getName());
        visitorViewHolder.tvVisitorEmail.setText(gatePassArrayList.get(i).getEmail());
        visitorViewHolder.tvVisitorMobile.setText(gatePassArrayList.get(i).getMobile());
        visitorViewHolder.tvVisitorVisitDate.setText(gatePassArrayList.get(i).getVisit_date());

        qrgEncoder = new QRGEncoder(""+gatePassArrayList.get(i), null, QRGContents.Type.TEXT, 10);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            visitorViewHolder.imgQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gatePassArrayList.size();
    }
}
