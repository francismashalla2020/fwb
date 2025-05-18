package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    ImageView menusT;
    TextView login, reset;
    ProgressBar progressBar;
    EditText username, password;
    String uname, upass;
    LinearLayout layout;
    String id, surname, first_name, middle_name, phone_no, email, address, member_status, role, role_name, date_created, plusmessage, success, fail;
    Context ctx;
    public static final String MYPREFERENCES = "MyPreferences_001";//login
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        menusT = findViewById(R.id.menuST);
        progressBar = findViewById(R.id.my_progressBarCars);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        layout = findViewById(R.id.layout);
        reset = findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                startActivity(new Intent(getApplicationContext(), AppAccount.class));
                uname = username.getText().toString();
                upass = password.getText().toString();

                if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upass)){
                    String stext1;
                    stext1 = getResources().getString(R.string.a57);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (isConnected()){
                    new userLogin().execute();
                }else {
                    String stext1;
                    stext1 = getResources().getString(R.string.a33);
                    Snackbar snackbar = Snackbar.make(layout, stext1, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Login.this, v);
                popupMenu.setOnMenuItemClickListener(Login.this::onMenuItemClick);
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

    class userLogin extends AsyncTask {
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String url = "http://thinkbold.africa/fwb/member_login.php";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject cred = new JSONObject();
                cred.put("phone_no", uname);
                cred.put("password", upass);

                Log.d("dataSend", uname +"MYYY"+ upass);

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
                        id =  userdata.getString("id");
                        surname =  userdata.getString("surname");
                        first_name =  userdata.getString("first_name");
                        middle_name =  userdata.getString("middle_name");
                        phone_no =  userdata.getString("phone_no");
                        email =  userdata.getString("email");
                        address =  userdata.getString("address");
                        member_status =  userdata.getString("member_status");
                        role =  userdata.getString("role");
                        role_name =  userdata.getString("role_name");
                        date_created =  userdata.getString("date_created");

                        Log.d("userData11", id + "#" + surname + "#" + surname);

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
                //Toast.makeText(LoginHealthPeer.this, message, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return plusmessage;
        }
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
            if (plusmessage.equals(success) && member_status.equalsIgnoreCase("1")) {
                ctx = Login.this;
                Intent intent = new Intent(ctx, AppAccount.class);
                Toast.makeText(ctx, R.string.a59, Toast.LENGTH_LONG).show();
                SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFERENCES, Activity.MODE_PRIVATE);
                editor = mySharedPreferences.edit();
                editor.putString("id", id);
                editor.putString("first_name", first_name);
                editor.putString("middle_name", middle_name);
                editor.putString("surname", surname);
                editor.putString("phone_no", phone_no);
                editor.putString("email", email);
                editor.putString("address", address);
                editor.putString("member_status", member_status);
                editor.putString("role", role);
                editor.putString("role_name", role_name);
                editor.putString("date", date_created);
                editor.commit();
                ctx.startActivity(intent);
                finish();
            }else if (plusmessage.equals(success) && member_status.equalsIgnoreCase("0")){
                Toast.makeText(ctx, R.string.a60, Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
                startActivity(new Intent(getApplicationContext(), CheckMemberStatus.class));
            }
            else if (plusmessage.equals(success) && member_status.equalsIgnoreCase("2")){
                Toast.makeText(ctx, R.string.a61, Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
                startActivity(new Intent(getApplicationContext(), ContactUs.class));
            }
            else if (plusmessage.equals(fail) && member_status.equalsIgnoreCase("")){
                Toast.makeText(ctx, R.string.a58, Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
            }
            else{
                Toast.makeText(ctx, R.string.a58, Toast.LENGTH_LONG).show();
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