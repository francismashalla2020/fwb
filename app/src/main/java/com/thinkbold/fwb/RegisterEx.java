package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.Retrofit.FWBApI;
import com.thinkbold.fwb.util.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterEx extends AppCompatActivity {

    ImageView menusT;
    String data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12, data13;
    EditText fname, mname, lname, phone, physical, passk, passks, relationship;
    LinearLayout layout;
    TextView submit;
    String s1, s2, s3, s4, s5, s6;
    String sdata1, sdata2, sdata3, sdata4, sdata5, sdata6, datar, plusmessage, formatedP;
    ProgressBar progressBar;
    Context ctx;
    public static final String MYPREFERENCES = "MyPreferences_001";//registration
    SharedPreferences.Editor editor;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FWBApI mService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ex);

        mService = Common.getAPIs();
        Bundle data = getIntent().getExtras();
        assert data != null;
        data1 = data.getString("data1");
        data2 = data.getString("data2");
        data3 = data.getString("data3");
        data4 = data.getString("data4");
        data5 = data.getString("data5");
        data6 = data.getString("data6");

        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        phone = findViewById(R.id.phone);
        passk = findViewById(R.id.passk);
        passks = findViewById(R.id.passks);
        physical = findViewById(R.id.physical);
        progressBar = findViewById(R.id.my_progressBarCars);
        relationship = findViewById(R.id.relationship);

        layout = findViewById(R.id.layout);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data7 = fname.getText().toString();
                data8 = mname.getText().toString();
                data9 = lname.getText().toString();
                data10 = phone.getText().toString();
                data11 = physical.getText().toString();
                data12 = passk.getText().toString();
                data13 = passks.getText().toString();
                datar = relationship.getText().toString();

                s1 = data1;
                s2 = data2;
                s3 = data3;
                s4 = data4;
                s5 = data5;
                s6 = data6;

                if (data12.length() < 6 || data13.length() < 6){
                    String stext;
                    stext = getResources().getString(R.string.a45);
                    //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (!data12.equals(data13)){
                    String stext1;
                    stext1 = getResources().getString(R.string.a46);
                    //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()) {
                    //method to push data
                    new sendData().execute();

                    String formattedPhoneNumber = formatPhoneNumber(s4);

                    SendSMSWithSenderID(formattedPhoneNumber, "Hi," +data1 + "Welcome to FWB Mshiko, your username is:"+ s4 +".and Password is:" + data12 );

                    Log.d("formData", formattedPhoneNumber +"#" + s4);

                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(RegisterEx.this, v);
                popupMenu.setOnMenuItemClickListener(RegisterEx.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //method to push Edit data to the API
    class sendData extends AsyncTask {

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/add_member.php";
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("first_name", s1);
                cred.put("middle_name", s2);
                cred.put("surname", s3);
                cred.put("phone_no", s4);
                cred.put("email", s5);//sort
                cred.put("address", s6);
                cred.put("password", data12);
                cred.put("kin_first_name", data7);
                cred.put("kin_middle_name", data8);
                cred.put("kin_surname", data9);
                cred.put("kin_address", data10);
                cred.put("kin_phone", data11);
                cred.put("relationship", datar);

                Log.d("sendData11", s1 +" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+data12+" "+data7+" "+data8+" "+data9+" "+data10+" "+data11+" "+datar);
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(cred.toString());
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();

                Log.d("conn11", String.valueOf(con));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8")
                    );
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    //some new codes
                    JSONObject o = new JSONObject(sb.toString());
                    JSONArray data = o.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject userdata = data.getJSONObject(i);
                        sdata1 = userdata.getString("first_name");
                        sdata2 = userdata.getString("surname");
                        sdata3 = userdata.getString("email");
                        sdata4 = userdata.getString("phone_no");
                        Log.d("lM11", String.valueOf(data.length()));
                        Log.d("LM22", data.getString(i));
                        Log.d("reData", sdata1 +" "+sdata2+" "+sdata3+" "+sdata4);

                    }
                    plusmessage = o.getString("message");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return plusmessage;
        }

        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
            if (plusmessage.equals("Success")) {
                ctx = RegisterEx.this;
                String smsd;
                smsd = "Hi, welcome to FWB Mshiko: " + "\n Your username is" + sdata4 + "\n and Password is:" + sdata1;
                String formattedPhoneNumber = formatPhoneNumber(sdata4);
                SendSMSWithSenderID(formattedPhoneNumber, smsd );
                Log.d("formData22", formattedPhoneNumber +"#" + sdata4);
                Log.d("captData", sdata4 +"" +sdata1);

                Intent intent = new Intent(ctx, CheckStatus.class);
                Toast.makeText(ctx, R.string.a48, Toast.LENGTH_SHORT).show();
                SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFERENCES, Activity.MODE_PRIVATE);
                editor = mySharedPreferences.edit();
                editor.putString("fname", sdata1);
                editor.putString("sname", sdata2);
                editor.putString("email", sdata3);
                editor.putString("phone", sdata4);
                editor.commit();
                ctx.startActivity(intent);
                finish();
            } else {
                String stext1;
                stext1 = getResources().getString(R.string.a34);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
    //end method to push data

    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account) {
            // Letter on
            // Check user verified
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), Appinfo.class));
            return true;
        } else if (id == R.id.docs) {
            startActivity(new Intent(getApplicationContext(), AppDocs.class));
            return true;
        } else if (id == R.id.help) {
            startActivity(new Intent(getApplicationContext(), Help.class));
            return true;
        } else if (id == R.id.share) {
            String applink = "https://play.google.com/store/apps/details?id=com.thinkbold.fwb";
            String message = getResources().getString(R.string.adv7);
            String message1 = getResources().getString(R.string.adv8);
            String message2 = getResources().getString(R.string.adv9);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, message + "\n" + message1 + "\n" + applink);
            startActivity(Intent.createChooser(i, message2));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), ContactUs.class));
            return true;
        } else if (id == R.id.status) {
            startActivity(new Intent(getApplicationContext(), CheckMemberStatus.class));
            return true;
        } else {
            return false;
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void SendSMSWithSenderID(String CustomerPhone, String txtmessage) {

        compositeDisposable.add(mService.SendSMS(CustomerPhone, txtmessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   Log.d("yaMessage", "MESSAGE: " + s);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d("yaMessage", "ERROR MESSAGE: " + throwable.getMessage());
                               }
                           }

                ));

    }

    public static String formatPhoneNumber(String phoneNumber) {
        // Check if the phone number starts with "0"
        if (phoneNumber.startsWith("0")) {
            // Replace the "0" with "+255"
            return "+255" + phoneNumber.substring(1);
        } else {
            // If it doesn't start with "0", return it as is or handle accordingly
            return phoneNumber;
        }
    }

}

