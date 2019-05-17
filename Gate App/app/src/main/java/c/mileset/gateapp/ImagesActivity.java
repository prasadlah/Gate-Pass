package c.mileset.gateapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import c.mileset.gateapp.adapter.ImageAdapter;
import c.mileset.gateapp.model.Demo;

public class ImagesActivity extends AppCompatActivity {

    RecyclerView recyclerImage;
    ImageAdapter imageAdapter;

    private FirebaseFirestore firebaseFirestore;
    private List<Demo> demoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerImage = (RecyclerView) findViewById(R.id.recyclerImage);
        recyclerImage.setHasFixedSize(true);
        recyclerImage.setLayoutManager(new LinearLayoutManager(this));

        demoList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("uploads")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            Demo demo = new Demo();
                            demo.setName(queryDocumentSnapshot.getString("name"));
                            demo.setImgUrl(queryDocumentSnapshot.getString("imgUrl"));

                            demoList.add(demo);
                        }
                        imageAdapter = new ImageAdapter(ImagesActivity.this, demoList);
                        recyclerImage.setAdapter(imageAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImagesActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
