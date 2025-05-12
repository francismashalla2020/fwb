package com.thinkbold.fwb;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class Appinfo extends AppCompatActivity {
    ImageView menusT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Appinfo.this, v);
                popupMenu.setOnMenuItemClickListener(Appinfo.this::onMenuItemClick);
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

//    public boolean onMenuItemClick (MenuItem item){
//        switch (item.getItemId()){
//            case R.id.account:
//                //letter on
//                //checkUserverified
//                startActivity(new Intent(getApplicationContext(), Home.class));
//                return true;
//            case R.id.about:
//                startActivity(new Intent(getApplicationContext(), Appinfo.class));
//                return true;
//            case R.id.docs:
//                startActivity(new Intent(getApplicationContext(), AppDocs.class));
//                return true;
//            case R.id.help:
//                startActivity(new Intent(getApplicationContext(), Help.class));
//                return true;
//            case R.id.share:
//                String applink = "https://play.google.com/store/apps/details?id=com.thinkbold.fwb";
//                String message = getResources().getString(R.string.adv7);
//                String message1 = getResources().getString(R.string.adv8);
//                String message2 = getResources().getString(R.string.adv9);
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, message + "\n" + message1 + "\n" + applink);
//                startActivity(Intent.createChooser(i, message2));
//                return true;
//
//            case R.id.contact:
//                startActivity(new Intent(getApplicationContext(), ContactUs.class));
//                return true;
//            case R.id.status:
//                startActivity(new Intent(getApplicationContext(), CheckMemberStatus.class));
//                return true;
//            default:
//                return false;
//        }
//    }

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