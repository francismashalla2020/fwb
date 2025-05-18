package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FinancialExtraDetails extends AppCompatActivity {

    TextView memberName, shares, long_term_loan,long_term_paid, long_term_balance, long_term_status, emergency_loan, emergency_paid, emergency_balance, emergency_status, back;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String adminID;

    String fid, fname, fshare, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10;
    CardView editF, deleteF;
    ProgressBar progressBar;
    ImageView menusT, imageBack;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_financial_extra_details);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        adminID = preferences.getString("id", null);

        Bundle data = getIntent().getExtras();
        fid = data.getString("id");
        fname = data.getString("memberName");
        fshare = data.getString("shares");
        data1 = data.getString("long_term_loan");
        data2 = data.getString("long_term_paid");
        data3 = data.getString("long_term_balance");
        data4 = data.getString("long_term_status");

        data5 = data.getString("emergency_loan");
        data6 = data.getString("emergency_paid");
        data7 = data.getString("emergency_balance");
        data8 = data.getString("emergency_status");

        memberName = findViewById(R.id.memberName);
        shares = findViewById(R.id.shares);
        long_term_loan = findViewById(R.id.long_term_loan);
        long_term_paid = findViewById(R.id.long_term_paid);
        long_term_balance = findViewById(R.id.long_term_balance);
        long_term_status = findViewById(R.id.long_term_status);
        emergency_loan = findViewById(R.id.emergency_loan);
        emergency_paid = findViewById(R.id.emergency_paid);
        emergency_balance = findViewById(R.id.emergency_balance);
        emergency_status = findViewById(R.id.emergency_status);
        editF = findViewById(R.id.editF);
        deleteF = findViewById(R.id.deleteF);
        back = findViewById(R.id.back);
        imageBack = findViewById(R.id.imageBack);

        memberName.setText(fname);
        shares.setText(fshare);
        long_term_loan.setText(data1);
        long_term_paid.setText(data2);
        long_term_balance.setText(data3);
        long_term_status.setText(data4);

        emergency_loan.setText(data5);
        emergency_paid.setText(data6);
        emergency_balance.setText(data7);
        emergency_status.setText(data8);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllMFinancialDetails.class));
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllMFinancialDetails.class));
            }
        });

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(FinancialExtraDetails.this, v);
                popupMenu.setOnMenuItemClickListener(FinancialExtraDetails.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });
        editF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(FinancialExtraDetails.this, EditFinancials.class);
                Bundle datas = new Bundle();
                datas.putString("id", fid);
                datas.putString("memberName", fname);
                datas.putString("shares", fshare);
                datas.putString("long_term_loan", data1);
                datas.putString("emergency_loan", data5);
                move.putExtras(datas);
                startActivity(move);
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
}