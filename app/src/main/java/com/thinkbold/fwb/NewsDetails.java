package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class NewsDetails extends AppCompatActivity {
    TextView head, category, idate, desc, story;
    CardView share, info;
    String id, title, desci, stori, cats, date;
    ImageView menusT;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Bundle data = getIntent().getExtras();
        id = data.getString("id");
        title = data.getString("title");
        desci = data.getString("desci");
        stori = data.getString("stori");
        cats = data.getString("cats");
        date = data.getString("date");

        head = findViewById(R.id.head);
        category = findViewById(R.id.category);
        idate = findViewById(R.id.date);
        desc = findViewById(R.id.desc);
        story = findViewById(R.id.story);
        share = findViewById(R.id.share);
        info = findViewById(R.id.info);

        head.setText(title);
        category.setText(cats);
        idate.setText(date);
        desc.setText(desci);
        story.setText(stori);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle, message, message1, applink, message2;
                applink = "https://play.google.com/store/apps/details?id=com.thinkbold.fwb";
                message = getResources().getString(R.string.a26);
                message1 = getResources().getString(R.string.a27);
                message2 = getResources().getString(R.string.adv9);
                newTitle = title;
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, message + "\n" + newTitle + "\n" + message1 + applink);
                startActivity(Intent.createChooser(i, message2));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rdata;
                rdata = title;
                Intent request = new Intent(getApplicationContext(), RequestInfo.class);
                Bundle datas = new Bundle();
                datas.putString("data", rdata);
                request.putExtras(datas);
                startActivity(request);
            }
        });

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewsDetails.this, v);
                popupMenu.setOnMenuItemClickListener(NewsDetails.this::onMenuItemClick);
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