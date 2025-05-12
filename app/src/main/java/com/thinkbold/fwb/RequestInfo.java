package com.thinkbold.fwb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RequestInfo extends AppCompatActivity {

    TextView title, call, whats;
    String head;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);

        Bundle data = getIntent().getExtras();
        head = data.getString("data");

        title = findViewById(R.id.title);
        call = findViewById(R.id.call);
        whats = findViewById(R.id.whats);

        title.setText(head);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phones = "+255767067974";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phones, null));
                startActivity(intent);
            }
        });
        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://chat.whatsapp.com/JMMaRO8BPYaFtKAVdj0XGW";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}