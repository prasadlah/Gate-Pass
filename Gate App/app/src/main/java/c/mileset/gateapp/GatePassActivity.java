package c.mileset.gateapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import androidmads.library.qrgenearator.QRGEncoder;
import c.mileset.gateapp.model.GatePass;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class GatePassActivity extends AppCompatActivity {

    private TextFieldBoxes nameTextField, emailTextField, mobileTextField, visitDateTextField, visitTimeTextField;
    private ExtendedEditText nameExtendedText, emailExtendedText, mobileExtendedText, visitDateExtendedText, visitTimeExtendedText;
    private Button btnGeneratePass, btnShare, btnCancel;
    private ImageView imgQrCode;

    FirebaseFirestore mFirestore;
    GatePass gatePass;
    Intent intent;
    ArrayList<GatePass> gatePassArrayList;

    String savePath;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
    QRGEncoder qrgEncoder;

    Calendar calendar = Calendar.getInstance();
    String currentDate, currentTime, ampm, expireTime;
    int currentHour, currentMinute, currentYear, currentMonth, currentDay;

    TimePickerDialog visitTime;
    DatePickerDialog visitDate;

    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass);

        intent = getIntent();
        gatePass = new GatePass();
        gatePassArrayList = new ArrayList<GatePass>();
        savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode";

        mFirestore = FirebaseFirestore.getInstance();

        nameTextField = (TextFieldBoxes) findViewById(R.id.name_text_field_box);
        emailTextField = (TextFieldBoxes) findViewById(R.id.email_text_field_box);
        mobileTextField = (TextFieldBoxes) findViewById(R.id.mobile_text_field_box);
        visitDateTextField = (TextFieldBoxes) findViewById(R.id.visit_date_text_field_box);
        visitTimeTextField = (TextFieldBoxes) findViewById(R.id.visit_time_text_field_box);
        nameExtendedText = (ExtendedEditText) findViewById(R.id.name_extended_text);
        emailExtendedText = (ExtendedEditText) findViewById(R.id.email_extended_text);
        mobileExtendedText = (ExtendedEditText) findViewById(R.id.mobile_extended_text);
        visitDateExtendedText = (ExtendedEditText) findViewById(R.id.visit_date_extended_text);
        visitTimeExtendedText = (ExtendedEditText) findViewById(R.id.visit_time_extended_text);
        btnGeneratePass = (Button) findViewById(R.id.btnGeneratePass);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        imgQrCode = (ImageView) findViewById(R.id.imgQrCode);

        visitDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        visitTimeTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog();
            }
        });

        btnGeneratePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameExtendedText.getText().toString().equals("") || nameExtendedText.getText().toString().equals(null)
                || emailExtendedText.getText().toString().equals("") || emailExtendedText.getText().toString().equals(null)
                || mobileExtendedText.getText().toString().equals("") || mobileExtendedText.getText().toString().equals(null)
                || visitDateExtendedText.getText().toString().equals("") || visitDateExtendedText.getText().toString().equals(null)
                || visitTimeExtendedText.getText().toString().equals("") || visitTimeExtendedText.getText().toString().equals(null)){
                    getMessage("All Fields Required!!!");
                }
                else {

                    currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                    currentTime = new SimpleDateFormat("hh:mm:ss a").format(calendar.getTime());

                    gatePass.setName(nameExtendedText.getText().toString().trim());
                    gatePass.setEmail(emailExtendedText.getText().toString().trim());
                    gatePass.setMobile(mobileExtendedText.getText().toString().trim());
                    gatePass.setVisit_date(visitDateExtendedText.getText().toString().trim());
                    gatePass.setVisit_time(visitTimeExtendedText.getText().toString().trim());
                    gatePass.setPass_date(currentDate);
                    gatePass.setPass_time(currentTime);
                    gatePass.setExpire(expireTime);
                    createPass(gatePass);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                currentTime = new SimpleDateFormat("hh:mm:ss a").format(calendar.getTime());

                gatePass.setName(nameExtendedText.getText().toString().trim());
                gatePass.setEmail(emailExtendedText.getText().toString().trim());
                gatePass.setMobile(mobileExtendedText.getText().toString().trim());
                gatePass.setVisit_date(visitDateExtendedText.getText().toString().trim());
                gatePass.setVisit_time(visitDateExtendedText.getText().toString().trim());
                gatePass.setPass_date(currentDate);
                gatePass.setPass_time(currentTime);
                gatePass.setPass_code(gatePass.getPass_code());
//                saveGatePass(gatePass);

                share();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clearAll();
                bitmapDrawable = (BitmapDrawable) imgQrCode.getDrawable();
                Bitmap bmp = bitmapDrawable.getBitmap();

                FileOutputStream fos = null;

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/DCIM/QeImage");
                dir.mkdir();
                String fileName = String.format("%d.jpg",System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                Toast.makeText(GatePassActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();

                try{
                    fos = new FileOutputStream(outFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                    Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    i.setData(Uri.fromFile(outFile));
                    sendBroadcast(i);
                }
                catch (FileNotFoundException f){
                    f.printStackTrace();
                }
                catch (IOException ie){
                    ie.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPass(GatePass gatePass) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            String qrCode = createKey(gatePass.getMobile());
            gatePass.setPass_code(qrCode);

            BitMatrix bitMatrix = multiFormatWriter.encode(qrCode, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQrCode.setImageBitmap(bitmap);
        }
        catch (WriterException we){
            we.printStackTrace();
        }
    }

    private void saveGatePass(GatePass gatePass){

        mFirestore.collection("Register").document(intent.getStringExtra("userId"))
                .collection("Gate Pass")
                .add(gatePass)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getMessage("Share Successfully");
                        clearAll();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getMessage("Error : " + e.getMessage());
                    }
                });
    }

    private void openDatePickerDialog(){
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        visitDate = new DatePickerDialog(GatePassActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                if(year < currentYear){
                    getMessage("You Select Wromg Year");
                }
                else {
                    if(month < currentMonth){
                        getMessage("You Select Wrong Month");
                    }
                    else {
                        if (currentDay > dayOfMonth){
                            getMessage("You Select Wrong Day");
                        }
                        else {
                            String date = dayOfMonth + "/" + month + "/" + year;
                            visitDateExtendedText.setText(date);
                        }
                    }
                }

            }
        }, currentYear, currentMonth, currentDay);

        visitDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        visitDate.show();
    }

    private void openTimePickerDialog(){
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        visitTime = new TimePickerDialog(GatePassActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay >= 12)
                    ampm = "PM";
                else
                    ampm = "AM";

                visitTimeExtendedText.setText(String.format("%02d:%02d", hourOfDay, minute) + ampm);
                expireTime = String.format("%02d:%02d", (hourOfDay + 2), minute) + ampm;
//                        txtVisitTime.setText(hourOfDay +" : " + minute + " " + ampm);
            }
        }, currentHour, currentMinute, false);
        visitTime.show();
    }

    private void getMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll(){
        nameExtendedText.setText("");
        emailExtendedText.setText("");
        mobileExtendedText.setText("");
        visitDateExtendedText.setText("");
        visitTimeExtendedText.setText("");
        imgQrCode.setImageResource(0);
    }

    private String createKey(String mobile){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(4);
        for(int i = 0; i < 4; i++){
            stringBuilder.append(random.nextInt(96) + 32);
        }
        return String.valueOf(stringBuilder+mobile);
    }

    protected void share(){
        bitmapDrawable = (BitmapDrawable) imgQrCode.getDrawable();
        Bitmap bmp = bitmapDrawable.getBitmap();


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(shareIntent);
        }
        catch (ActivityNotFoundException a){
            a.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmp);
    }
}