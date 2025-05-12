package com.thinkbold.fwb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MemberDetails extends AppCompatActivity {
    TextView back, name, nname, email, phone, nphone, physical, nphysical;
    String fname, kfname;
    ImageView menusT;
    public static final String MYPREFERENCESS_003 = "MyPreferences_003";//Details
    String sid, ssurname, sfirst_name, smiddle_name, sphone_no, semail, saddress, kin_id, kin_first_name, kin_middle_name, sdate_created, kin_phone, kin_address, kin_surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCESS_003, Context.MODE_PRIVATE);
        sid = preferences.getString("id", null);
        ssurname = preferences.getString("surname", null);
        sfirst_name = preferences.getString("first_name", null);
        smiddle_name = preferences.getString("middle_name", null);
        sphone_no = preferences.getString("phone_no", null);
        semail = preferences.getString("email", null);
        saddress = preferences.getString("address", null);
        kin_id = preferences.getString("kin_id", null);
        kin_first_name = preferences.getString("kin_first_name", null);
        kin_middle_name = preferences.getString("kin_middle_name", null);
        kin_phone = preferences.getString("kin_phone", null);
        kin_address = preferences.getString("kin_address", null);
        kin_surname = preferences.getString("kin_surname", null);
        sdate_created = preferences.getString("date_created", null);

        Log.d("Rdata", semail +" "+ saddress);

        fname = sfirst_name+ " " +smiddle_name+ " "+ssurname;
        kfname = kin_first_name+ " "+kin_middle_name+" "+kin_surname;

        back = findViewById(R.id.back);

        name = findViewById(R.id.name);
        nname = findViewById(R.id.nname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        nphone = findViewById(R.id.nphone);
        physical = findViewById(R.id.physical);
        nphysical = findViewById(R.id.nphysical);
        menusT = findViewById(R.id.menuST);

        name.setText(fname);
        nname.setText(kfname);
        email.setText(semail);
        phone.setText(sphone_no);
        nphone.setText(kin_phone);
        physical.setText(saddress);
        nphysical.setText(kin_address);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppAccount.class));
            }
        });

        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MemberDetails.this, v);
                popupMenu.setOnMenuItemClickListener(MemberDetails.this::onMenuItemClick);
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

}