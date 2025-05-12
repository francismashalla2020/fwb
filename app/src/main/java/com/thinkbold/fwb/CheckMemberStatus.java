package com.thinkbold.fwb;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

public class CheckMemberStatus extends AppCompatActivity {

    EditText phone;
    TextView submit, status, login, getC;
    LinearLayout mstatus, layout;
    ProgressBar progressBar;
    String phone_no, sphone, spass, sstatus, plusmessage, success, fail, data1, data2, data3;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FWBApI mService;
    ImageView menusT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member_status);

        mService = Common.getAPIs();
        phone = findViewById(R.id.phone);
        submit = findViewById(R.id.submit);

        mstatus = findViewById(R.id.mstatus);
        status = findViewById(R.id.status);
        login = findViewById(R.id.login);
        getC = findViewById(R.id.getC);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_no = phone.getText().toString();

                if (TextUtils.isEmpty(phone_no)){
                    String stext1;
                    stext1 = getResources().getString(R.string.a85);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()){
                    new checkStatus().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

//        getC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //a = phone;
//                String a, cc, b;
//                a = "+255758048457";
//                cc = "Francis";
//                String kk;
//                kk = "Message for chichi";
//                b = "Hi," +cc + " welcome to FWB Mshiko: " + "\n Your username is" + a + "\n and Password is:" + cc;
//                if (isConnected()){
//                    SendSMSWithSenderID(a, b);
//                    Log.d("sms111", a + "##" +b);
//                }else {
//                    String stext1;
//                    stext1 = getResources().getString(R.string.a33);
//                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                }
//            }
//        });

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(CheckMemberStatus.this, v);
                popupMenu.setOnMenuItemClickListener(CheckMemberStatus.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

    }

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

    class checkStatus extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/member_status.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("phone_no", phone_no);

                Log.d("dataSend12", phone_no);

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
                        sphone =  userdata.getString("phone_no");
                        spass =  userdata.getString("password");
                        sstatus =  userdata.getString("member_status");

                        Log.d("userData112", sphone + "#" + spass + "#" + sstatus);

                        Log.d("lM11", String.valueOf(data.length()));
                        Log.d("LM22", data.getString(i));
                    }
                    plusmessage = o.getString("message");
                    //end new codes
                    if (plusmessage.equals("success")){
                        success = plusmessage;
                        Log.d("lo11", success);
                    }else if (plusmessage.equals("failed")){
                        fail = plusmessage;
                        Log.d("lo22", fail);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return plusmessage;
        }
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
            if (plusmessage.equals(success)) {
                mstatus.setVisibility(View.VISIBLE);
                data1 = sphone;
                data2 = spass;
                data3 = sstatus;
                status.setText(data3);
                if (data3.equalsIgnoreCase("Verified")){
                    login.setVisibility(View.VISIBLE);
                }else {
                    login.setVisibility(View.INVISIBLE);
                }

            }
            else if (plusmessage.equals(fail)){
                String stext1;
                stext1 = getResources().getString(R.string.a86);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else{
                Toast.makeText(CheckMemberStatus.this, R.string.a87, Toast.LENGTH_SHORT).show();
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
}