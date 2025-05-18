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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditFinancials extends AppCompatActivity {

    TextView next, back, mname;
    ImageView menusT;
    EditText shares, lterm, eloan, phone, email, physical;
    String id, surname,role,  data1, data0, data11, data12, data13, data14, data15, data8;
    LinearLayout layout;
    ProgressBar progressBar;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String fid, fname, fshare, data5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_financials);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);
        role = preferences.getString("role", null);

        Bundle data = getIntent().getExtras();
        fid = data.getString("id");
        fname = data.getString("memberName");
        fshare = data.getString("shares");
        data1 = data.getString("long_term_loan");
        data5 = data.getString("emergency_loan");

        next = findViewById(R.id.next);
        shares = findViewById(R.id.shares);
        lterm = findViewById(R.id.lterm);
        eloan = findViewById(R.id.eloan);
        mname = findViewById(R.id.mname);
        layout = findViewById(R.id.layout);
        menusT = findViewById(R.id.menuST);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllMFinancialDetails.class));
            }
        });
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(EditFinancials.this, v);
                popupMenu.setOnMenuItemClickListener(EditFinancials.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

        mname.setText(fname);
        shares.setText(fshare);
        lterm.setText(data1);
        eloan.setText(data5);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data0 = id;
                data11 = role;
                data12 = fid;
                data13 = shares.getText().toString();
                data14 = lterm.getText().toString();
                data15 = eloan.getText().toString();

                if (isConnected()){
                    new UpdateFinancialData().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
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

    class UpdateFinancialData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String plusmessage = "";
            try {
                String url = "https://thinkbold.africa/fwb/update_financial.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("member_id", data12);         // member to update
                cred.put("shares", data13);            // can be empty
                cred.put("long_term_loan", data14);    // can be empty
                cred.put("emergency_loan", data15);    // can be empty
                cred.put("updated_by", data0);        // current admin ID
                cred.put("role", data11);             // current admin role


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
                    data8 = o.optString("description");  // for response message
                    plusmessage = o.optString("message");
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
            String stext, userMessage;
            if ("Success".equalsIgnoreCase(result)) {
                stext = data8; // server message
                userMessage = "Financial details updated";
                Snackbar snackbar = Snackbar.make(layout, userMessage, Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(new Intent(getApplicationContext(), AllMFinancialDetails.class));
            } else {
                stext = getResources().getString(R.string.f5); // fallback error message
                Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                snackbar.show();
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