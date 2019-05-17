package c.mileset.gateapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnOwnerLogin, btnGuardLogin;

    ConnectivityManager conMan;
    NetworkInfo.State mobileData;
    NetworkInfo.State wifi;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mobileData = conMan.getNetworkInfo(0).getState();
        wifi = conMan.getNetworkInfo(1).getState();

        if(mobileData == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED){

            btnOwnerLogin = (Button) findViewById(R.id.btnOwnerLogin);
            btnGuardLogin = (Button) findViewById(R.id.btnGuardLogin);

            btnOwnerLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, OwnerLoginActivity.class);
                    startActivity(intent);
                }
            });

            btnGuardLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, GuardLoginActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Network Error");
            builder.setMessage("Please Check Your Network Connection")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
