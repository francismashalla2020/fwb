package com.thinkbold.fwb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Home extends AppCompatActivity {
    TextView greetings, login, register, status, profile;
    SharedPreferences regPreferences, staffprefs;

    public static final String MYPREFERENCES = "MyPreferences_001";
    String phoneNo, sphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        greetings = findViewById(R.id.greetings);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        status = findViewById(R.id.status);
        profile = findViewById(R.id.profile);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings.setText(R.string.gm);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings.setText(R.string.gf);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetings.setText(R.string.ge);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greetings.setText(R.string.ge);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkifUserVerified();
                //startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckMemberStatus.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkifUserVerified();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle(R.string.app_name8);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.welerror6)
                        .setCancelable(false)
                        .setPositiveButton(R.string.welerrorY, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(R.string.welerrorN, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void checkifUserVerified() {
        regPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE );
        phoneNo = regPreferences.getString("phone_no", null);
        Log.d("userInfo1", phoneNo + "#");
        sphone = phoneNo;

        if (sphone == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else {
            startActivity(new Intent(getApplicationContext(), AppAccount.class));
        }
    }
}