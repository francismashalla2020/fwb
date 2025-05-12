package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.offlinedatabase.MembersDb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LongTermLoan extends AppCompatActivity {

    EditText lamount;
    TextView calculate, camount, next, uname, end1, end2;
    Spinner en1, en2;
    ProgressBar progressBar;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, ids, surname, first_name, middle_name, c_name, eAmount, dispAmount, loan_status, descs, plusmessage, success, fail;
    ImageView menusT;
    Double interest, total, abc;
    private MembersDb membersDb;
    private ArrayList<String> membersList, memberListId;
    String ends1, ends2, mAmount, mEnd1, mEnd2, sdata1, sdata2;
    LinearLayout layout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_term_loan);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);
        first_name = preferences.getString("first_name", null);
        middle_name = preferences.getString("middle_name", null);

        c_name = first_name +" "+surname;

        lamount = findViewById(R.id.lamount);
        uname = findViewById(R.id.uname);
        calculate = findViewById(R.id.calculate);
        next = findViewById(R.id.next);
        camount = findViewById(R.id.camount);
        en1 = findViewById(R.id.en1);
        en2 = findViewById(R.id.en2);
        progressBar = findViewById(R.id.my_progressBarCars);
        menusT = findViewById(R.id.menuST);
        end1 = findViewById(R.id.end1);
        end2 = findViewById(R.id.end2);
        layout = findViewById(R.id.layout);

        uname.setText(c_name);

        //Endorser 1 Spinners
        membersDb = new MembersDb(LongTermLoan.this);
        membersList = membersDb.getAllMembers();
        memberListId = membersDb.getAllMiD();
        Log.d("nataion22", String.valueOf(membersList));
        //spinner codes
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, membersList);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //assigning adapter to spinner
        en1.setAdapter(bankAdapter);
        //add listener
        en1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = membersList.get(position);
                ends1 = memberListId.get(position); //value to be sent to the API
                end1.setVisibility(View.VISIBLE);
                end1.setText(items);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Endorser 2 spinner
        ArrayAdapter<String> endAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, membersList);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //assigning adapter to spinner
        en2.setAdapter(endAdapter);
        //add listener
        en2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = membersList.get(position);
                ends2 = memberListId.get(position); //value to be sent to the API
                end2.setVisibility(View.VISIBLE);
                end2.setText(items);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LongTermLoan.this, v);
                popupMenu.setOnMenuItemClickListener(LongTermLoan.this::onMenuItemClick);
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

        calculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                eAmount = lamount.getText().toString();

                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                abc = Double.parseDouble(eAmount);

                if (TextUtils.isEmpty(eAmount)){
                    Toast.makeText(LongTermLoan.this, R.string.lo12, Toast.LENGTH_SHORT).show();
                }else {
                    interest = abc * 0.1;
                    total = abc + interest;
                    String a2;
                    a2 = String.format("%.0f", total);
                    int a22 = Integer.parseInt(a2);
                    String a222 = myFormat.format(a22);
                    camount.setVisibility(View.VISIBLE);
                    camount.setText(a222);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount = lamount.getText().toString();
                mEnd1 = ends1;
                mEnd2 = ends2;
                ids = id;

                if (TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mEnd1) || TextUtils.isEmpty(mEnd2)){
                    String stext;
                    stext = getResources().getString(R.string.a44);
                    Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()){
                    //method to push data
                    new sendLoanData().execute();
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
            startActivity(new Intent(getApplicationContext(), AppAccount.class));
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


    class sendLoanData extends AsyncTask {

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "https://thinkbold.africa/fwb/loan_application.php";
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("member_id", ids);
                cred.put("loan_type", 1);
                cred.put("amount", mAmount);
                cred.put("referee1_id", mEnd1);
                cred.put("referee2_id", mEnd2);

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
                        sdata2 = userdata.optString("description", "");

                        Log.d("ParsedData", sdata1 + ", " + sdata2);
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

                // Example of how to display parsed data (optional)
                Log.d("LoanDetails", "Status: " + sdata1);
                Log.d("LoanDetails", "Description: " + sdata2);

                Dialog dialog = new Dialog(LongTermLoan.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loan_success);
                dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                TextView textviewCancel = dialog.findViewById(R.id.textviewCancel);
                TextView textviewLogout = dialog.findViewById(R.id.textviewLogout);

                textviewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AppAccount.class));
                    }
                });
                textviewLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), LoanStatus.class));
                    }
                });
                dialog.show();

            } else if ("Failed".equals(plusmessage)){
                Dialog dialog = new Dialog(LongTermLoan.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loan_fail);
                dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                TextView textviewCancel = dialog.findViewById(R.id.textviewCancel);
                textviewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AppAccount.class));
                    }
                });
                dialog.show();
            }

            else {
                Snackbar snackbar = Snackbar.make(layout, R.string.a34, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
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
}