package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.reactivex.schedulers.Schedulers;

public class ResetPassword extends AppCompatActivity {

    ImageView imageBack;
    EditText username, password;
    TextView reset;
    String uname, upass;
    ProgressBar progressBar;
    LinearLayout layout;
    String phone_no, new_password, description, plusmessage, fail, success, dd, dph, dp ;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FWBApI mService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mService = Common.getAPIs();
        imageBack = findViewById(R.id.imageBack);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        reset = findViewById(R.id.reset);
        progressBar = findViewById(R.id.my_progressBarCars);
        layout = findViewById(R.id.layout);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                upass = password.getText().toString();

                if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upass)){
                    String stext1;
                    stext1 = getResources().getString(R.string.a57);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()){
                    new userReset().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }


    class userReset extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/reset_password.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("phone_no", uname);
                cred.put("new_password", upass);

                Log.d("dataSend", uname +"MYYY"+ upass);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(cred.toString());
                wr.flush();
                //display what returns the POST request
                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();

                Log.d("conn11", String.valueOf(con));
                if (HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8")
                    );
                    String line = null;
                    while ((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();
                    //some new codes
                    JSONObject o = new JSONObject(sb.toString());
                    JSONArray data = o.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++){
                        JSONObject userdata = data.getJSONObject(i);
                        phone_no =  userdata.getString("phone_no");
                        new_password =  userdata.getString("new_password");
                        description =  userdata.getString("description");

                        Log.d("lM11", String.valueOf(data.length()));
                        Log.d("LM22", data.getString(i));

                    }
                    plusmessage = o.getString("message");
                    //end new codes
                    if (plusmessage.equals("Success")){
                        success = plusmessage;
                        Log.d("lo11", success);
                    }else if (plusmessage.equals("Failed")){
                        fail = plusmessage;
                        Log.d("lo22", fail);
                    }
                }
            }catch (Exception e){
                //Toast.makeText(LoginHealthPeer.this, message, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return plusmessage;
        }
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
            dd = description;
            dph = phone_no;
            dp = new_password;
            String stext;

            if (plusmessage.equals(success) && dd.equalsIgnoreCase("Password reset successful")) {

                String smsd;
                smsd = "Hi, welcome to FWB Mshiko: " + "\n Your username is" + dph + "\n and Password is:" + dp;
                SendSMSWithSenderID(dph, smsd );
                stext = getResources().getString(R.string.rp7); // fallback error message
                Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if (plusmessage.equals(fail) && dd.equalsIgnoreCase("No user found with that phone number")){
                username.setText("");
                password.setText("");
                stext = getResources().getString(R.string.rp6); // fallback error message
                Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else{
                Toast.makeText(ResetPassword.this, R.string.a58, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void SendSMSWithSenderID(String customerPhone, String messageText) {
        String senderId = "Hood"; // Must be approved by NextSMS

        // Format the number (ensure it’s in international format)
        String formattedPhone = customerPhone.replaceFirst("^0", "255");

        compositeDisposable.add(mService.sendSMSViaNextSMS(senderId, formattedPhone, messageText)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        response -> Log.d("NextSMS", "Success: " + response),
                        error -> Log.e("NextSMS", "Error: " + error.getMessage())
                )
        );
    }

//    public void SendSMSWithSenderID(String CustomerPhone, String txtmessage) {
//
//        String formattedPhone = CustomerPhone.replaceFirst("^0", "255");
//
//        compositeDisposable.add(mService.SendSMS(formattedPhone, txtmessage)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                        response -> Log.d("NextSMS", "Success: " + response),
//                    error -> Log.e("NextSMS", "Error: " + error.getMessage())
//
//                ));
//
//    }

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