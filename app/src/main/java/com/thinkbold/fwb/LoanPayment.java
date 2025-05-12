package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class LoanPayment extends AppCompatActivity {

    Spinner natSpinner;
    String itemDoc, value;
    TextView ltype, next;
    EditText amount, reffs;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, phone_no;
    ProgressBar progressBar;
    String data1, data2, data3, data4, sdata1, sdata2, sdata3, sdata4, plusmessage;
    LinearLayout layout;
    Context ctx;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FWBApI mService;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_payment);

        mService = Common.getAPIs();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        phone_no = preferences.getString("phone_no", null);

        natSpinner = findViewById(R.id.natSpinner);
        amount = findViewById(R.id.amount);
        reffs = findViewById(R.id.reffs);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.my_progressBarCars);
        layout = findViewById(R.id.layout);
        ltype = findViewById(R.id.ltype);

        ArrayAdapter<CharSequence> nadapter = ArrayAdapter.createFromResource(this,
                R.array.loan_type, android.R.layout.simple_spinner_item);
        nadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        natSpinner.setAdapter(nadapter);
        //add listener
        natSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemDoc = parent.getItemAtPosition(position).toString();
                Log.d("items11", itemDoc);
                if (itemDoc.equalsIgnoreCase("Long term loan")){
                    ltype.setVisibility(View.VISIBLE);
                    ltype.setText("1");
                }else if (itemDoc.equalsIgnoreCase("Emergency loan")){
                    ltype.setVisibility(View.VISIBLE);
                    ltype.setText("2");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1 = amount.getText().toString();
                data2 = reffs.getText().toString();
                data3 = ltype.getText().toString();
                data4 = id;

                Log.d("cdata", data1 + data2 + data3 + data4);

                if (TextUtils.isEmpty(data1) || TextUtils.isEmpty(data2) || TextUtils.isEmpty(data3) ){
                    String stext;
                    stext = getResources().getString(R.string.lp7);
                    //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()){
                    //send data method
                    new sendData().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

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
                String url = "https://thinkbold.africa/fwb/loan_payment.php";
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("loan_type", data3);
                cred.put("amount", data1);
                cred.put("ref_no", data2);

                cred.put("member_id", data4);
                cred.put("created_by", ""); // For future use



                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(cred.toString());
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                }

                // Log the raw response for debugging
                Log.d("APIResponse", "Response: " + sb.toString());

                // Parse the JSON response
                JSONObject responseObject = new JSONObject(sb.toString().trim());
                plusmessage = responseObject.optString("message", "");

                if (responseObject.has("data")) {
                    JSONArray dataArray = responseObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject userdata = dataArray.getJSONObject(i);

                        // Correctly retrieve integer and string fields
                        sdata1 = userdata.optString("loan_status", "");
                        sdata2 = String.valueOf(userdata.optInt("balance", 0)); // Convert integer to string
                        sdata3 = userdata.optString("loan_amount", "");
                        sdata4 = userdata.optString("description", "");

                        Log.d("ParsedData", sdata1 + ", " + sdata2 + ", " + sdata3 + ", " + sdata4);
                    }
                }
            } catch (Exception e) {
                Log.e("doInBackground", "Error:", e);
                plusmessage = null; // Set an error message or handle exceptions gracefully
            }
            return plusmessage;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);

            if ("Success".equals(plusmessage)) {
                Toast.makeText(LoanPayment.this, R.string.lp8, Toast.LENGTH_SHORT).show();

                // Example of how to display parsed data (optional)
                Log.d("LoanDetails", "Status: " + sdata1);
                Log.d("LoanDetails", "Balance: " + sdata2);
                Log.d("LoanDetails", "Amount: " + sdata3);
                Log.d("LoanDetails", "Description: " + sdata4);

                String smsd;
                smsd = "Hi, your payment details are stored";
                String formattedPhoneNumber = formatPhoneNumber(phone_no);
                SendSMSWithSenderID(formattedPhoneNumber, smsd);
                Log.d("formData22", formattedPhoneNumber + "#" + phone_no);

                Intent intent = new Intent(LoanPayment.this, AppAccount.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar snackbar = Snackbar.make(layout, R.string.a34, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }
    //end method to push data
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