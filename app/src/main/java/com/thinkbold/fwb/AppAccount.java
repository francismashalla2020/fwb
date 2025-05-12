package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.models.Get_members;
import com.thinkbold.fwb.offlinedatabase.MembersDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppAccount extends AppCompatActivity {
    ImageView menusT;
    TextView news, uname, phone, details;
    CardView adminTools, rLong, payLoan, rEmergency, rEndo;
    LinearLayout layout;
    SharedPreferences regPreferences;
    public static final String MYPREFERENCES = "MyPreferences_001";
    ProgressBar progressBar;
    String id, surname, first_name, middle_name, phone_no, email, address, member_status, role, role_name, date_created, plusmessage, success, fail;
    String c_name, c_role;
    String sid, ssurname, sfirst_name, smiddle_name, sphone_no, semail, saddress, kin_id, kin_first_name, kin_middle_name, sdate_created, kin_phone, kin_address, kin_surname, m_e, m_a;
    Context ctx;
    public static final String MYPREFERENCESS_003 = "MyPreferences_003";//Details
    SharedPreferences.Editor editor;
    private List<Get_members> list_members;

    String member_id,loan_amount, balance, emergency_loan, emergency_balance, description, d1, d2, d3, d4;

    TextView longloan, longloanDebt, emeloan, emeloanDebt;
    String sdata1, sdata2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_account);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);
        first_name = preferences.getString("first_name", null);
        middle_name = preferences.getString("middle_name", null);
        phone_no = preferences.getString("phone_no", null);
        email = preferences.getString("email", null);
        address = preferences.getString("address", null);
        member_status = preferences.getString("member_status", null);
        role = preferences.getString("role", null);
        role_name = preferences.getString("role_name", null);
        date_created = preferences.getString("date_created", null);

        c_name = first_name +" "+surname;
        c_role = role;

        menusT = findViewById(R.id.menuST);
        news = findViewById(R.id.news);
        adminTools = findViewById(R.id.adminTools);
        rLong = findViewById(R.id.rLong);
        layout = findViewById(R.id.layout);
        payLoan = findViewById(R.id.payLoan);
        rEmergency = findViewById(R.id.rEmergency);
        rEndo = findViewById(R.id.rEndo);
        progressBar = findViewById(R.id.my_progressBarCars);

        uname = findViewById(R.id.uname);
        phone = findViewById(R.id.phone);
        details = findViewById(R.id.details);

        longloan = findViewById(R.id.longloan);
        longloanDebt = findViewById(R.id.longloanDebt);
        emeloan = findViewById(R.id.emeloan);
        emeloanDebt = findViewById(R.id.emeloanDebt);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MemberDetails.class));
            }
        });

        payLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoanPayment.class));
            }
        });

        rLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LongTermLoan.class));
            }
        });

        rEndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListToEndorse.class));
            }
        });
        uname.setText(c_name);
        phone.setText(phone_no);
        if (c_role.equals("1")){
            adminTools.setVisibility(View.INVISIBLE);
        }else {
            adminTools.setVisibility(View.VISIBLE);
        }
        adminTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FwbNews.class));
            }
        });

        rEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Dialog dialog = new Dialog(AppAccount.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_emergencyloan);
                dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

                TextView textviewYes = dialog.findViewById(R.id.textviewYes);
                TextView textviewNo = dialog.findViewById(R.id.textviewNo);

                textviewNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                textviewYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        m_e = id;
                m_a = "150000";

                if (isConnected()){
                    //method
                    new sendData().execute();
                }else {
                    String stext;
                    stext = getResources().getString(R.string.a33);
                    //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                    }
                });

                dialog.show();
            }
        });

        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AppAccount.this, v);
                popupMenu.setOnMenuItemClickListener(AppAccount.this::onMenuItemClick);
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

        new loandetails().execute();

        if (isConnected()){
            new userdetails().execute();

            new MyTask_Members().execute();
        }else {
            String stext;
            stext = getResources().getString(R.string.a33);
            //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
            snackbar.show();
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

    public void accountLogout(View view) {
        Dialog dialog = new Dialog(AppAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textviewCancel = dialog.findViewById(R.id.textviewCancel);
        TextView textviewLogout = dialog.findViewById(R.id.textviewLogout);

        textviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textviewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(AppAccount.this, R.string.a67, Toast.LENGTH_SHORT).show();

                // Debug logging
                Log.d("AccountLogout", "Clearing SharedPreferences");

                SharedPreferences sharedpreferences1 = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                editor1.clear();
                editor1.commit();

                Log.d("AccountLogout", "SharedPreferences cleared");

                moveTaskToBack(true);
                finish();

                // Debug logging
                Log.d("AccountLogout", "Starting Home activity");

                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        dialog.show();
    }


    class userdetails extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/get_member_id.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();

                cred.put("id", id);

                Log.d("dataSend", id);

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
                        sid =  userdata.getString("id");
                        ssurname =  userdata.getString("surname");
                        sfirst_name =  userdata.getString("first_name");
                        smiddle_name =  userdata.getString("middle_name");
                        sphone_no =  userdata.getString("phone_no");
                        semail =  userdata.getString("email");
                        saddress =  userdata.getString("address");
                        kin_id =  userdata.getString("kin_id");
                        kin_first_name =  userdata.getString("kin_first_name");
                        kin_middle_name =  userdata.getString("kin_middle_name");
                        kin_surname =  userdata.getString("kin_surname");
                        kin_address =  userdata.getString("kin_address");
                        kin_phone =  userdata.getString("kin_phone");
                        sdate_created =  userdata.getString("date_created");

                        Log.d("userData111", sid + "#" + ssurname + "#" + kin_first_name +"#" + semail);

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
                //Toast.makeText(LoginHealthPeer.this, message, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return plusmessage;
        }
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            if (plusmessage.equals(success)) {

                SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFERENCESS_003, Activity.MODE_PRIVATE);
                editor = mySharedPreferences.edit();
                editor.putString("id", sid);
                editor.putString("first_name", sfirst_name);
                editor.putString("middle_name", smiddle_name);
                editor.putString("surname", ssurname);
                editor.putString("phone_no", sphone_no);
                editor.putString("email", semail);
                editor.putString("address", saddress);
                editor.putString("kin_id", kin_id);
                editor.putString("kin_first_name", kin_first_name);
                editor.putString("kin_middle_name", kin_middle_name);
                editor.putString("kin_surname", kin_surname);
                editor.putString("kin_address", kin_address);
                editor.putString("kin_phone", kin_phone);
                editor.putString("date", sdate_created);
                editor.commit();
            }
            else{
                Toast.makeText(ctx, R.string.a84, Toast.LENGTH_LONG).show();
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

    private class MyTask_Members extends AsyncTask<String, Void, List> {
        @Override
        protected List doInBackground(String... params) {
            try {
                //String urlW="http://thinkbold.africa/fwb/get_member.php?member=All";
                String urlW="http://thinkbold.africa/fwb/get_member.php?member=verfy";
                URL object=new URL(urlW);
                Log.e("URL", urlW);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred   = new JSONObject();

                String type = "1";
                cred.put("ActionType", type);

                Log.d("retrieve23", type +"#");
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(cred.toString());
                wr.flush();

                InputStream in = new BufferedInputStream(con.getInputStream());
                StringBuilder jsonResult = inputStreamToString(in);
                list_members = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(jsonResult.toString());
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String id = jsonChildNode.optString("id");
                    String first_name = jsonChildNode.optString("full_name");
                    String middle_name = jsonChildNode.optString("full_name");
                    String surname = jsonChildNode.optString("full_name");
                    String phone_no = jsonChildNode.optString("phone_no");
                    String email = jsonChildNode.optString("email");
                    String address = jsonChildNode.optString("address");
                    String password = jsonChildNode.optString("password");
                    String role = jsonChildNode.optString("role");
                    String member_status = jsonChildNode.optString("member_status");
                    String date_created = jsonChildNode.optString("date_created");
                    String date_updated = jsonChildNode.optString("date_updated");


                    list_members.add(new Get_members(id, first_name, middle_name, surname, phone_no, email, address, password, role, member_status, date_created, date_updated));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list_members;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (Exception e) {
                finish();
            }
            return answer;
        }
        @Override
        protected void onPostExecute(List list) {
            ListDrawer_Act(list);
        }
    }
    public void ListDrawer_Act(List<Get_members> customList) {
        MembersDb handler = new MembersDb(AppAccount.this);
        handler.deleteAllDataMembers();
//
        Log.e("kpi45", customList.size() + "");
        for (int z = 0; z < customList.size(); z++) {
            Log.e("products46", customList.get(z).getId() + "");
            String id = customList.get(z).getId();
            String first_name = customList.get(z).getFirst_name();
            String middle_name = customList.get(z).getMiddle_name();
            String surname = customList.get(z).getSurname();
            String phone_no = customList.get(z).getPhone_no();
            MembersDb b = new MembersDb(AppAccount.this);
            b.InsertMembers(id, first_name, middle_name, surname, phone_no);
        }
    }


    class loandetails extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "https://thinkbold.africa/fwb/loan_details.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();

                cred.put("member_id", id);

                Log.d("loandataSend", id);

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
                        member_id =  userdata.getString("member_id");
                        loan_amount =  userdata.getString("loan_amount");
                        balance =  userdata.getString("balance");
                        emergency_loan =  userdata.getString("emergency_loan");
                        emergency_balance =  userdata.getString("emergency_balance");
                        description =  userdata.getString("description");

                        Log.d("userData101", member_id + "#" + loan_amount + "#" + balance +"#" + emergency_loan);

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
                //Toast.makeText(LoginHealthPeer.this, message, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return plusmessage;
        }
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            if (plusmessage.equals(success)) {
                Toast.makeText(AppAccount.this, "Loan details retrieved", Toast.LENGTH_SHORT).show();

                d1 = loan_amount;
                d2 = balance;
                d3 = emergency_loan;
                d4 = emergency_balance;
                if (d1.equalsIgnoreCase("0")){
                    longloan.setText("nil amount");

                }else {
                    longloan.setText(loan_amount);
                    payLoan.setVisibility(View.VISIBLE);
                }
                if (d2.equalsIgnoreCase("0")){
                    longloanDebt.setText("nil balance");
                }else {
                    longloanDebt.setText(balance);
                }
                if (d3.equalsIgnoreCase("0")){
                    emeloan.setText("nil amount");
                    rEmergency.setVisibility(View.VISIBLE);
                }else {
                    emeloan.setText(emergency_loan);
                    payLoan.setVisibility(View.VISIBLE);
                }
                if (d4.equalsIgnoreCase("0")){
                    emeloanDebt.setText("nil balance");
                    rEmergency.setVisibility(View.VISIBLE);
                }else {
                    emeloanDebt.setText(emergency_balance);
                }
            }
            else{
                Toast.makeText(AppAccount.this, R.string.a84, Toast.LENGTH_LONG).show();
            }
        }
    }


    class sendData extends AsyncTask {

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
                cred.put("member_id", m_e);
                cred.put("loan_type", 2);
                cred.put("amount", m_a);
                cred.put("referee1_id", "");
                cred.put("referee2_id", "");

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
                Toast.makeText(AppAccount.this, R.string.lp9, Toast.LENGTH_SHORT).show();

                // Example of how to display parsed data (optional)
                Log.d("LoanDetails", "Status: " + sdata1);
                Log.d("LoanDetails", "Description: " + sdata2);

            } else {
                Snackbar snackbar = Snackbar.make(layout, R.string.a34, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }
    //end method to push data
}