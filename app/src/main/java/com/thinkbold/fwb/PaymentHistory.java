package com.thinkbold.fwb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.adapters.MemberAdapter;
import com.thinkbold.fwb.adapters.PaymentHistoryAdapter;
import com.thinkbold.fwb.models.Get_payment_history;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PaymentHistory extends AppCompatActivity {

    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, surname, first_name, middle_name, phone_no, email, address, member_status, role, role_name, date_created;

    RecyclerView recyclerNews;
    private java.net.URL url;
    private HttpURLConnection urlConnection;

    private MemberAdapter adapter;
    private List<Get_payment_history> allmembers;
    SearchView sv;
    private String URL_NEWS = "https://thinkbold.africa/fwb/app_member.php";
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, grid2;
    ProgressBar progressBar;
    ImageView menusT, export;
    LinearLayout layout;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

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

        recyclerNews = findViewById(R.id.recyclerNews);
        progressBar = findViewById(R.id.progressBar);
        sv = findViewById(R.id.mSearch);
        export = findViewById(R.id.export);
        sv.onActionViewExpanded();
        sv.setIconified(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.clearFocus();
            }
        }, 500);
        //set query change listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                /**
                 * hides and then unhides search tab to make sure keyboard disappears when query is submitted
                 */
                sv.setVisibility(View.INVISIBLE);
                sv.setVisibility(View.VISIBLE);
                return false;
            }
        });
        layout = findViewById(R.id.layout);

        menusT = findViewById(R.id.menuST);
        menusT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(PaymentHistory.this, v);
                popupMenu.setOnMenuItemClickListener(PaymentHistory.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FinancialSummary.class));
            }
        });

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerNews.setLayoutManager(mStaggeredGridLayoutManager);

        if (haveNetworkConnection()){
            accessWebService_PaymentHistory(id);
        }else {
            String stext;
            stext = getResources().getString(R.string.a33);
            //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportPaymentHistoryToCSV(allmembers);
            }
        });

    }

    private void accessWebService_PaymentHistory(String memberId) {
        String url = "https://thinkbold.africa/fwb/get_payment_history.php";
        new MyTask_PaymentHistory().execute(url, memberId);
    }

    private class MyTask_PaymentHistory extends AsyncTask<String, Void, List<Get_payment_history>> {

        @Override
        protected List<Get_payment_history> doInBackground(String... params) {
            String urlStr = params[0];
            String memberId = params[1];
            List<Get_payment_history> paymentHistoryList = new ArrayList<>();

            HttpURLConnection conn = null;

            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setConnectTimeout(7000);

                // JSON payload
                JSONObject payload = new JSONObject();
                payload.put("member_id", memberId);

                // Send payload
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(payload.toString());
                writer.flush();
                writer.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                StringBuilder result = inputStreamToString(in);

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray dataArray = jsonObject.optJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);

                    paymentHistoryList.add(new Get_payment_history(
                            obj.optString("loan_type"),
                            obj.optString("amount"),
                            obj.optString("payment_date"),
                            obj.optString("description"),
                            obj.optString("loan_amount"),
                            obj.optString("balance"),
                            obj.optString("status")
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) conn.disconnect();
            }

            return paymentHistoryList;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb;
        }

        @Override
        protected void onPostExecute(List<Get_payment_history> list) {
            ListDrawer_PaymentHistory(list);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void ListDrawer_PaymentHistory(List<Get_payment_history> list) {
        allmembers = list;
        PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(list, PaymentHistory.this);
        recyclerNews.setAdapter(adapter);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void exportPaymentHistoryToCSV(List<Get_payment_history> dataList) {
        String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName = "payment_history_" + System.currentTimeMillis() + ".csv";
        File file = new File(baseDir, fileName);

        try {
            FileWriter writer = new FileWriter(file);
            writer.append("Loan Type,Payment Date,Amount,Loan Amount,Balance,Status,Description\n");

            for (Get_payment_history item : dataList) {
                writer.append(item.getLoan_type()).append(",");
                writer.append(item.getPayment_date()).append(",");
                writer.append(item.getAmount()).append(",");
                writer.append(item.getLoan_amount()).append(",");
                writer.append(item.getBalance()).append(",");
                writer.append(item.getStatus()).append(",");
                writer.append(item.getDescription()).append("\n");
            }

            writer.flush();
            writer.close();

            Toast.makeText(this, "CSV exported to Downloads:\n" + fileName, Toast.LENGTH_LONG).show();
            showShareOrOpenDialog(file);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error exporting CSV", Toast.LENGTH_SHORT).show();
        }
    }


    private void showShareOrOpenDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CSV Exported")
                .setMessage("What would you like to do?")
                .setPositiveButton("Open", (dialog, which) -> openCSV(file))
                .setNegativeButton("Share", (dialog, which) -> shareCSV(file))
                .setNeutralButton("Cancel", null)
                .show();
    }
    private void openCSV(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/csv");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app found to open CSV file", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareCSV(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share CSV using"));
    }

}