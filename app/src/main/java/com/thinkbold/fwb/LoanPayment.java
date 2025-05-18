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
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.Retrofit.FWBApI;
import com.thinkbold.fwb.util.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoanPayment extends AppCompatActivity {

    Spinner natSpinner;
    String itemDoc, value;
    TextView ltype, next, back;
    EditText amount, reffs, note;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, phone_no;
    ProgressBar progressBar;
    String data1, data2, data3, data4, sdata1, data5, sdata2, sdata3, sdata4, sdata5, sdata6, sdata7, plusmessage, notes;
    LinearLayout layout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FWBApI mService;
    ImageView menusT;
    String selectedLoanType = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_payment);

        mService = Common.getAPIs();

        SharedPreferences preferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        phone_no = preferences.getString("phone_no", null);

        natSpinner = findViewById(R.id.natSpinner);
        amount = findViewById(R.id.amount);
        reffs = findViewById(R.id.reffs);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.my_progressBarCars);
        layout = findViewById(R.id.layout);
        ltype = findViewById(R.id.ltype);
        note = findViewById(R.id.note);
        back = findViewById(R.id.back);
        menusT = findViewById(R.id.menuST);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppAccount.class));
            }
        });
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppAccount.class));
            }
        });

        ArrayAdapter<CharSequence> nadapter = ArrayAdapter.createFromResource(this,
                R.array.loan_type, android.R.layout.simple_spinner_item);
        nadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        natSpinner.setAdapter(nadapter);

//        natSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                itemDoc = parent.getItemAtPosition(position).toString();
//                ltype.setText(itemDoc.equalsIgnoreCase("Long term loan") ? "1" : "2");
//                ltype.setVisibility(View.INVISIBLE);
//            }
        natSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemDoc = parent.getItemAtPosition(position).toString();

                if (itemDoc.equalsIgnoreCase("Long-Term Loan")) {
                    ltype.setText("Long-Term Loan");
                    selectedLoanType = "1";
                } else if (itemDoc.equalsIgnoreCase("Emergency Loan")) {
                    ltype.setText("Emergency Loan");
                    selectedLoanType = "2";
                } else {
                    ltype.setText("Unknown");
                    selectedLoanType = "";
                }

                ltype.setVisibility(View.VISIBLE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        next.setOnClickListener(v -> {
            data1 = amount.getText().toString();
            data2 = reffs.getText().toString();
            //data3 = ltype.getText().toString();
            data3 = selectedLoanType;
            data4 = id;
            data5 = note.getText().toString();

            if (TextUtils.isEmpty(data1) || TextUtils.isEmpty(data2) || TextUtils.isEmpty(data3)) {
                Snackbar.make(layout, R.string.lp7, Snackbar.LENGTH_LONG).show();
            } else if (isConnected()) {
                new sendData().execute();
            } else {
                Snackbar.make(layout, R.string.a33, Snackbar.LENGTH_LONG).show();
            }
        });

        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LoanPayment.this, v);
                popupMenu.setOnMenuItemClickListener(LoanPayment.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });
    }

    // === SEND PAYMENT DATA TO API ===
    class sendData extends AsyncTask<Void, Void, String> {
        @Override protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override protected String doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL("https://thinkbold.africa/fwb/loan_payment.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject payload = new JSONObject();
                payload.put("member_id", data4);
                payload.put("loan_type", data3);
                payload.put("amount", data1);
                payload.put("ref_no", data2);
                payload.put("created_by", ""); // Optional
                payload.put("source", "mobile"); // Optional
                payload.put("note", data5);
                payload.put("force_payment", "false");

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(payload.toString());
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                String response = sb.toString().trim();
                Log.d("APIResponse", response);

                if (response.startsWith("{")) {
                    JSONObject res = new JSONObject(response);
                    plusmessage = res.optString("message", "");

                    if (res.has("data")) {
                        JSONArray dataArr = res.getJSONArray("data");
                        if (dataArr.length() > 0) {
                            JSONObject data = dataArr.getJSONObject(0);
                            sdata1 = data.optString("loan_id", "");
                            sdata2 = String.valueOf(data.optInt("loan_type", 0));
                            sdata3 = data.optString("amount_paid", "");
                            sdata4 = data.optString("payment_amount", "");
                            sdata5 = data.optString("remaining_balance", "");
                            sdata6 = data.optString("loan_status", "");
                            sdata7 = data.optString("description", "");
                        }
                    }
                } else {
                    plusmessage = "Failed";
                }
            } catch (Exception e) {
                Log.e("LoanPayment", "Exception", e);
                plusmessage = "Failed";
            }
            return plusmessage;
        }

        @Override protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            String stext;

            if ("Success".equalsIgnoreCase(result)) {
                //Toast.makeText(LoanPayment.this, R.string.lp8, Toast.LENGTH_SHORT).show();

                //String smsd = "Hi, your payment details are stored";
                //SendSMSWithSenderID(formatPhoneNumber(phone_no), smsd);
                stext = sdata7;
                Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar.make(layout, R.string.a34, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isConnected();
        } catch (Exception e) {
            Log.e("Connectivity", "Exception", e);
            return false;
        }
    }

    public void SendSMSWithSenderID(String customerPhone, String txtmessage) {
        compositeDisposable.add(
                mService.SendSMS(customerPhone, txtmessage)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                s -> Log.d("SMS", "MESSAGE: " + s),
                                throwable -> Log.e("SMS", "ERROR: " + throwable.getMessage())
                        )
        );
    }

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return "+255" + phoneNumber.substring(1);
        } else {
            return phoneNumber;
        }
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
}
