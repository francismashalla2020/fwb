package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class Register extends AppCompatActivity {
    TextView next;
    ImageView menusT;
    EditText fname, mname, lname, phone, email, physical;
    String data1, data2, data3, data4, data5, data6;
    LinearLayout layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        next = findViewById(R.id.next);

        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        physical = findViewById(R.id.physical);
        layout = findViewById(R.id.layout);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), RegisterEx.class));

                data1 = fname.getText().toString();
                data2 = mname.getText().toString();
                data3 = lname.getText().toString();
                data4 = phone.getText().toString();
                data5 = email.getText().toString();
                data6 = physical.getText().toString();

                if (TextUtils.isEmpty(data1) || TextUtils.isEmpty(data2) || TextUtils.isEmpty(data3) || TextUtils.isEmpty(data4) || TextUtils.isEmpty(data5) || TextUtils.isEmpty(data6)){
                    String stext;
                    stext = getResources().getString(R.string.a44);
                    //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    Intent move = new Intent(getApplicationContext(), RegisterEx.class);
                    Bundle data = new Bundle();
                    data.putString("data1", data1);//First name
                    data.putString("data2", data2);//Middle name
                    data.putString("data3", data3);//Surname
                    data.putString("data4", data4);//phone
                    data.putString("data5", data5);//email
                    data.putString("data6", data6);//physical address
                    move.putExtras(data);
                    startActivity(move);
                }
            }
        });

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Register.this, v);
                popupMenu.setOnMenuItemClickListener(Register.this::onMenuItemClick);
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

}