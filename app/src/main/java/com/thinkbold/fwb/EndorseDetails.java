package com.thinkbold.fwb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class EndorseDetails extends AppCompatActivity {

    TextView name, loanType, amount, status, back;
    String cName, cType, cAmount, sStatus, mid, approval_status;

    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, ids, surname;
    CardView reject, approve;
    ProgressBar progressBar;
    LinearLayout mstatus, layout;
    String lA_ID, reffs_id;
    String pid, member_id, loan_type, description, lA_amount, plusmessage, success, fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endorse_details);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);

        Bundle data = getIntent().getExtras();
        cName = data.getString("member_name");
        cType = data.getString("loan_id");
        cAmount = data.getString("amount");
        sStatus = data.getString("approval_status");
        mid = data.getString("member_id");

        name = findViewById(R.id.name);
        loanType = findViewById(R.id.loanType);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        reject = findViewById(R.id.reject);
        approve = findViewById(R.id.approve);

        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        name.setText(cName);
        amount.setText(cAmount);

        if (sStatus.equalsIgnoreCase("1")){
            status.setText(R.string.approved);
        }else if (sStatus.equalsIgnoreCase("2")){
            status.setText(R.string.rejected);
        }else if (sStatus.equalsIgnoreCase("0")){
            status.setText(R.string.pending);
        }

        if (cType.equalsIgnoreCase("1")){
            loanType.setText(R.string.lts);
        }else if (sStatus.equalsIgnoreCase("2")){
            status.setText(R.string.ltss);
        }

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lA_ID = mid;
                reffs_id = id;
                approval_status = "3";

                if (isConnected()){
                    new loanApprove().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lA_ID = mid;
                reffs_id = id;
                approval_status = "1";

                if (isConnected()){
                    new loanApprove().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    class loanApprove extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "https://thinkbold.africa/fwb/loan_approval.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("member_id", lA_ID);
                cred.put("loan_type", 1);
                cred.put("approval_status", approval_status);
                cred.put("approved_by", reffs_id);
                cred.put("start_date", "");
                cred.put("end_date", "");

                Log.d("dataSend121", lA_ID + reffs_id);

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
                        member_id =  userdata.getString("member_id");
                        loan_type =  userdata.getString("loan_type");
                        lA_amount =  userdata.getString("amount");
                        description =  userdata.getString("description");

                        Log.d("userData1114", pid + "#" + member_id + "#" + loan_type);

                        Log.d("lM11", String.valueOf(data.length()));
                        Log.d("LM22", data.getString(i));
                    }
                    plusmessage = o.getString("message");
                    //end new codes
                    if (plusmessage.equals("success")){
                        success = plusmessage;
                        Log.d("lo11", success);
                    }else if (plusmessage.equals("Failed")){
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
                stext1 = getResources().getString(R.string.a106);
                Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
            else if (plusmessage.equals(fail)){
                String stext1;
                stext1 = getResources().getString(R.string.a107);
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