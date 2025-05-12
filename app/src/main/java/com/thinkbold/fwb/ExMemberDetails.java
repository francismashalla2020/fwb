package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExMemberDetails extends AppCompatActivity {
    TextView name, emails, phone, physical, roles, status, datec, edit, back;
    CardView approve, reject;
    String c_name, c_role, c_status, adminID;
    public static final String MYPREFERENCES = "MyPreferences_002";
    String id, first_name, middle_name, surname, phone_no, email, address, password, role, member_status, date_created, date_updated;
    ProgressBar progressBar;
    LinearLayout mstatus, layout;
    String sid1, sid2, astatus, plusmessage, fail, success;
    String pid, pname, pmname, psurname, ppass, pphone, pdate, pstatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_member_details);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        adminID = preferences.getString("id", null);

        Bundle data = getIntent().getExtras();
        id = data.getString("id");
        first_name = data.getString("fname");
        middle_name = data.getString("mname");
        surname = data.getString("surname");
        phone_no = data.getString("phone");
        email = data.getString("email");
        address = data.getString("address");
        password = data.getString("password");
        role = data.getString("role");
        member_status = data.getString("status");
        date_created = data.getString("date1");
        date_updated = data.getString("date2");

        c_name = first_name+" "+middle_name+" "+surname;
        c_role = role;
        c_status = member_status;

        name = findViewById(R.id.name);
        name.setText(c_name);
        emails = findViewById(R.id.email);
        emails.setText(email);
        phone = findViewById(R.id.phone);
        phone.setText(phone_no);
        physical = findViewById(R.id.physical);
        physical.setText(address);
        roles = findViewById(R.id.role);
        status = findViewById(R.id.status);
        datec = findViewById(R.id.datec);
        datec.setText(date_created);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);

        if (c_status.equalsIgnoreCase("1")){
            status.setText(R.string.a88);
            approve.setVisibility(View.INVISIBLE);
        }else if (c_status.equalsIgnoreCase("0")){
            status.setText(R.string.a89);
        } else if (c_status.equalsIgnoreCase("2")) {
            status.setText(R.string.a90);
        }

        if (c_role.equalsIgnoreCase("1")){
            roles.setText(R.string.a97);
        }else if (c_role.equalsIgnoreCase("2")){
            roles.setText(R.string.a98);
        } else if (c_role.equalsIgnoreCase("3")) {
            roles.setText(R.string.a99);
        } else if (c_role.equalsIgnoreCase("4")) {
            roles.setText(R.string.a100);
        } else if (c_role.equalsIgnoreCase("5")) {
            roles.setText(R.string.a101);
        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid1 = adminID;
                sid2 = id;
                astatus = "1";
                if (isConnected()){
                    new verifyMember().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid1 = adminID;
                sid2 = id;
                astatus = "2";
                if (isConnected()){
                    new rejectMember().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
        });


    }

    class verifyMember extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/member_verification.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("id", sid2);
                cred.put("member_status", astatus);
                cred.put("approved_by", sid1);

                Log.d("dataSend121", sid2);

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
                        pid =  userdata.getString("id");
                        pname =  userdata.getString("first_name");
                        pmname =  userdata.getString("middle_name");
                        psurname =  userdata.getString("surname");
                        pphone =  userdata.getString("phone_no");
                        pstatus =  userdata.getString("member_status");
                        pdate =  userdata.getString("date_updated");
                        ppass =  userdata.getString("password");

                        Log.d("userData1114", pid + "#" + pname + "#" + pstatus);

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
                String stext1;
                stext1 = getResources().getString(R.string.a102);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
            else if (plusmessage.equals(fail)){
                String stext1;
                stext1 = getResources().getString(R.string.a104);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }

        }
    }
    class rejectMember extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/member_verification.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("id", sid2);
                cred.put("member_status", astatus);
                cred.put("approved_by", sid1);

                Log.d("dataSend121", sid2);

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
                        pid =  userdata.getString("id");
                        pname =  userdata.getString("first_name");
                        pmname =  userdata.getString("middle_name");
                        psurname =  userdata.getString("surname");
                        pphone =  userdata.getString("phone_no");
                        pstatus =  userdata.getString("member_status");
                        pdate =  userdata.getString("date_updated");
                        ppass =  userdata.getString("password");

                        Log.d("userData1114", pid + "#" + pname + "#" + pstatus);

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
                String stext1;
                stext1 = getResources().getString(R.string.a102);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
            else if (plusmessage.equals(fail)){
                String stext1;
                stext1 = getResources().getString(R.string.a104);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
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