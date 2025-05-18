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
import android.util.Log;
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
import com.thinkbold.fwb.adapters.FinancialAdapter;
import com.thinkbold.fwb.adapters.MemberAdapter;
import com.thinkbold.fwb.models.Get_financials_details;

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
import java.util.Random;

public class AllMFinancialDetails extends AppCompatActivity {

    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, surname, first_name, middle_name, phone_no, email, address, member_status, role, role_name, date_created;

    RecyclerView recyclerNews;
    private java.net.URL url;
    private HttpURLConnection urlConnection;

    private MemberAdapter adapter;
    private List<Get_financials_details> allmembers;
    SearchView sv;
    private String URL_NEWS = "https://thinkbold.africa/fwb/app_member.php";
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, grid2;
    private MyTask_financials taskNews;
    ProgressBar progressBar;
    ImageView menusT, export;
    LinearLayout layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_mfinancial_details);
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
                PopupMenu popupMenu = new PopupMenu(AllMFinancialDetails.this, v);
                popupMenu.setOnMenuItemClickListener(AllMFinancialDetails.this::onMenuItemClick);
                popupMenu.inflate(R.menu.user_menu);
                popupMenu.show();

            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminTools.class));
            }
        });

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerNews.setLayoutManager(mStaggeredGridLayoutManager);

        if (haveNetworkConnection()){
            accessWebService_Financials();
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
                exportFinancialsToCSV(allmembers);
            }
        });

    }


    private void accessWebService_Financials() {
        Random rand = new Random();
        int n = rand.nextInt(50000000) + 1;
        String randomstamp = "financialtimestamp" + n;
        String URL_FINANCIALS = "https://thinkbold.africa/fwb/get_all_financials.php?tmps=" + randomstamp;
        URL_FINANCIALS = URL_FINANCIALS.replaceAll("\\s+", "%20");
        Log.e("URL_FIN", "URL_FIN:" + URL_FINANCIALS);

        new MyTask_financials().execute(URL_FINANCIALS);
    }

    private class MyTask_financials extends AsyncTask<String, Void, List<Get_financials_details>> {

        @Override
        protected List<Get_financials_details> doInBackground(String... params) {
            List<Get_financials_details> allFinancials = new ArrayList<>();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setConnectTimeout(5000);

                // JSON payload
                JSONObject payload = new JSONObject();
                payload.put("member_id", id);  // <-- replace with dynamic if needed
                payload.put("role", role);

                // Send request body
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(payload.toString());
                writer.flush();
                writer.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder jsonResult = inputStreamToString(in);

                JSONObject jsonResponse = new JSONObject(jsonResult.toString());
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChild = jsonMainNode.getJSONObject(i);

                    allFinancials.add(new Get_financials_details(
                            jsonChild.optString("member_id"),
                            jsonChild.optString("member_name"),
                            jsonChild.optString("shares"),
                            jsonChild.optString("long_term_loan"),
                            jsonChild.optString("long_term_paid"),
                            jsonChild.optString("long_term_loan_balance"),
                            jsonChild.optString("long_term_status"),
                            jsonChild.optString("long_term_last_payment_date"),
                            jsonChild.optString("emergency_loan"),
                            jsonChild.optString("emergency_paid"),
                            jsonChild.optString("emergency_loan_balance"),
                            jsonChild.optString("emergency_status"),
                            jsonChild.optString("emergency_last_payment_date")
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }

            return allFinancials;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            StringBuilder answer = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                String rLine;
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(List<Get_financials_details> list) {
            ListDrawer_Financials(list);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
        }
    }
    public void ListDrawer_Financials(List<Get_financials_details> list) {
        allmembers = list; // <-- This line fixes your crash

        FinancialAdapter adapter = new FinancialAdapter(list, AllMFinancialDetails.this);
        adapter.notifyDataSetChanged();
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

    private void exportFinancialsToCSV(List<Get_financials_details> dataList) {
        String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName = "financial_records_" + System.currentTimeMillis() + ".csv";
        File file = new File(baseDir, fileName);

        try {
            FileWriter writer = new FileWriter(file);
            writer.append("Member ID,Member Name,Shares,Long-Term Loan,Paid,Loan Balance,Status,Last Payment,Emergency Loan,Paid,Loan Balance,Status,Last Payment\n");

            for (Get_financials_details item : dataList) {
                writer.append(item.getMember_id()).append(",");
                writer.append(item.getMember_name()).append(",");
                writer.append(item.getShares()).append(",");
                writer.append(item.getLong_term_loan()).append(",");
                writer.append(item.getLong_term_paid()).append(",");
                writer.append(item.getLong_term_loan_balance()).append(",");
                writer.append(item.getLong_term_status()).append(",");
                writer.append(item.getLong_term_last_payment_date()).append(",");
                writer.append(item.getEmergency_loan()).append(",");
                writer.append(item.getEmergency_paid()).append(",");
                writer.append(item.getEmergency_loan_balance()).append(",");
                writer.append(item.getEmergency_status()).append(",");
                writer.append(item.getEmergency_last_payment_date()).append("\n");
            }

            writer.flush();
            writer.close();

            Toast.makeText(this, "CSV exported to Downloads:\n" + fileName, Toast.LENGTH_LONG).show();

            // Offer to share or open
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