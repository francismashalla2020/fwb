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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddFinancialDetails extends AppCompatActivity {

    TextView next, back;
    ImageView menusT;
    EditText shares, lterm, eloan, phone, email, physical;
    String id, surname, data1, data2, data3, data4, data5, data6, data7, data8;
    LinearLayout layout;
    ProgressBar progressBar;
    public static final String MYPREFERENCES = "MyPreferences_001";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_financial_details);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);

        next = findViewById(R.id.next);

        shares = findViewById(R.id.shares);
        lterm = findViewById(R.id.lterm);
        eloan = findViewById(R.id.eloan);
        layout = findViewById(R.id.layout);
        menusT = findViewById(R.id.menuST);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppAccount.class));
            }
        });
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AddFinancialDetails.this, v);
                popupMenu.setOnMenuItemClickListener(AddFinancialDetails.this::onMenuItemClick);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1 = id;
                data2 = shares.getText().toString();
                data3 = lterm.getText().toString();
                data4 = eloan.getText().toString();

                if (isConnected()){
                    new InsertFinancialData().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
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

    class InsertFinancialData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String plusmessage = "";
            try {
                String url = "https://thinkbold.africa/fwb/insert_financials_api.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("member_id", data1);
                cred.put("shares", data2);
                cred.put("long_term_loan", data3);
                cred.put("emergency_loan", data4);

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
                    JSONArray data = o.getJSONArray("data");
                    JSONObject record = data.getJSONObject(0);

                    data5 = record.getString("shares");
                    data6 = record.getString("long_term_loan");
                    data7 = record.getString("emergency_loan");
                    data8 = record.getString("description");

                    plusmessage = o.getString("message");
                }

            } catch (Exception e) {
                e.printStackTrace();
                plusmessage = "Failed";
            }
            return plusmessage;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            String stext;
            if ("Success".equalsIgnoreCase(result)) {
                stext = data8;
                Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                stext = getResources().getString(R.string.f5);
            }
            Snackbar.make(layout, stext, Snackbar.LENGTH_LONG).show();
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