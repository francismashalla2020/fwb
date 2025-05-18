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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminTools extends AppCompatActivity {

    CardView loan, reports;
    TextView allmembers, uname, roles, allmembersfinancial;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, surname, first_name, middle_name, phone_no, email, address, member_status, role, role_name, date_created;
    String c_name, c_role;
    ImageView menusT;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tools);

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

        loan = findViewById(R.id.loan);
        reports = findViewById(R.id.reports);
        allmembers = findViewById(R.id.allmembers);
        uname = findViewById(R.id.uname);
        roles = findViewById(R.id.role);
        allmembersfinancial = findViewById(R.id.allmembersfinancial);

        c_name = first_name + " " +surname;
        uname.setText(c_name);
        c_role = role_name;
        roles.setText(c_role);
        menusT = findViewById(R.id.menuST);

        allmembersfinancial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllMFinancialDetails.class));
            }
        });

        allmembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllMembers.class));
            }
        });
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdminTools.this, v);
                popupMenu.setOnMenuItemClickListener(AdminTools.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppAccount.class));
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