package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FinancialSummary extends AppCompatActivity {

    public static final String MYPREFERENCES = "MyPreferences_001";
    String id;
    TextView memberName, shares, long_term_loan, long_term_paid, long_term_balance, long_term_status, emergency_loan, emergency_paid, emergency_balance, emergency_status, back, history;
    ProgressBar progressBar;
    ImageView menusT, imageBack;

    String member_id, member_name, sharess, long_term_loans, long_term_paids, long_term_balances, long_term_statuss, long_term_last_payment_date, emergency_last_payment_date;
    String emergency_loans, emergency_paids, emergency_balances, emergency_statuss, plusmessage, success, fail;
    String fid, d0, d1, d2, d3, d4, a1, a2, a3, a4, a5, a6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_summary);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        Log.d("DebugMemberID", "ID from SharedPreferences: " + id);

        Bundle data = getIntent().getExtras();
        fid = data.getString("memberId");

        shares = findViewById(R.id.shares);
        long_term_loan = findViewById(R.id.long_term_loan);
        long_term_paid = findViewById(R.id.long_term_paid);
        long_term_balance = findViewById(R.id.long_term_balance);
        long_term_status = findViewById(R.id.long_term_status);
        emergency_loan = findViewById(R.id.emergency_loan);
        emergency_paid = findViewById(R.id.emergency_paid);
        emergency_balance = findViewById(R.id.emergency_balance);
        emergency_status = findViewById(R.id.emergency_status);
        back = findViewById(R.id.back);
        imageBack = findViewById(R.id.imageBack);
        memberName = findViewById(R.id.memberName);
        progressBar = findViewById(R.id.progressBar);
        menusT = findViewById(R.id.menuST);
        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PaymentHistory.class));
            }
        });

        back.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AppAccount.class)));
        imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AppAccount.class)));


        new RetreavFinancialDetails().execute();

        menusT.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(FinancialSummary.this, v);
            popupMenu.setOnMenuItemClickListener(FinancialSummary.this::onMenuItemClick);
            popupMenu.inflate(R.menu.user_menu);
            popupMenu.show();
        });
    }

    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account) {
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

    class RetreavFinancialDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = "https://thinkbold.africa/fwb/get_member_financial_summary.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("member_id", id);
                Log.d("DebugMemberID", "ID from SharedPreferences: " + id);

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

                    JSONObject o = new JSONObject(sb.toString());
                    plusmessage = o.getString("message");

                    if ("Success".equalsIgnoreCase(plusmessage)) {
                        JSONArray dataArray = o.getJSONArray("data");
                        JSONObject userdata = dataArray.getJSONObject(0);

                        member_id = userdata.getString("member_id");
                        member_name = userdata.getString("member_name");
                        sharess = userdata.getString("shares");
                        long_term_loans = userdata.getString("long_term_loan");
                        long_term_paids = userdata.getString("long_term_paid");
                        long_term_balances = userdata.getString("long_term_balance");
                        long_term_statuss = userdata.getString("long_term_status");
                        long_term_last_payment_date = userdata.getString("long_term_last_payment_date");
                        emergency_loans = userdata.getString("emergency_loan");
                        emergency_paids = userdata.getString("emergency_paid");
                        emergency_balances = userdata.getString("emergency_balance");
                        emergency_statuss = userdata.getString("emergency_status");
                        emergency_last_payment_date = userdata.getString("emergency_last_payment_date");
                    }
                }
            } catch (Exception e) {
                Log.e("RetreavFinancialDetails", "Error:", e);
                plusmessage = "Failed";
            }
            return plusmessage;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if ("Success".equalsIgnoreCase(result)) {
                d0 = sharess;
                d1 = long_term_loans;
                d2 = long_term_balances;
                d3 = emergency_loans;
                d4 = emergency_balances;

                a1 = member_name;
                a2 = long_term_paids;
                a3 = long_term_statuss;
                a4 = emergency_paids;
                a5 = emergency_statuss;

                memberName.setText(a1);
                shares.setText(d0);

                emergency_paid.setText("0".equals(a4) ? "nil amount" : emergency_paids);
                emergency_status.setText("0".equals(a5) ? "nil amount" : emergency_statuss);
                long_term_paid.setText("0".equals(a2) ? "nil amount" : long_term_paids);
                long_term_status.setText("0".equals(a3) ? "nil amount" : long_term_statuss);
                long_term_loan.setText("0".equals(d1) ? "nil amount" : long_term_loans);
                long_term_balance.setText("0".equals(d2) ? "nil balance" : long_term_balances);
                emergency_loan.setText("0".equals(d3) ? "nil amount" : emergency_loans);
                emergency_balance.setText("0".equals(d4) ? "nil balance" : emergency_balances);
            } else {
                Toast.makeText(FinancialSummary.this, "Failed to retrieve data", Toast.LENGTH_LONG).show();
            }
        }
    }
}