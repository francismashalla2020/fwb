package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.thinkbold.fwb.adapters.EndorseAdapter;
import com.thinkbold.fwb.models.Get_Endorse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListToEndorse extends AppCompatActivity {

    RecyclerView recyclerNews;
    private java.net.URL url;
    private HttpURLConnection urlConnection;
    private EndorseAdapter adapter;
    private List<Get_Endorse> allmembers;
    SearchView sv;
    private String URL_NEWS = "http://thinkbold.africa/fwb/referee_dtls.php";
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, grid2;
    //private MyTask_news taskNews;
    ProgressBar progressBar;
    ImageView menusT;
    LinearLayout layout;
    private MyTask_news taskNews;
    public static final String MYPREFERENCES = "MyPreferences_001";
    String id, surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_to_endorse);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        id = preferences.getString("id", null);
        surname = preferences.getString("surname", null);

        recyclerNews = findViewById(R.id.recyclerNews);
        progressBar = findViewById(R.id.progressBar);
        sv = findViewById(R.id.mSearch);
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
                PopupMenu popupMenu = new PopupMenu(ListToEndorse.this, v);
                popupMenu.setOnMenuItemClickListener(ListToEndorse.this::onMenuItemClick);
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
            accessWebService_News();
        }else {
            String stext;
            stext = getResources().getString(R.string.a33);
            //Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(layout, stext, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void accessWebService_News() {
        URL_NEWS = "http://thinkbold.africa/fwb/referee_dtls.php";
        Log.e("URL_TIC", "URL_TIC:" + URL_NEWS);
        taskNews = new MyTask_news();
        taskNews.execute(URL_NEWS);
    }

    private class MyTask_news extends AsyncTask<String, Void, List<Get_Endorse>> {

        @Override
        protected List<Get_Endorse> doInBackground(String... params) {
            List<Get_Endorse> endorseList = new ArrayList<>();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setDoOutput(true);

                // Create JSON request data
                JSONObject requestData = new JSONObject();
                requestData.put("id", id); // Example id, replace with actual dynamic data

                // Send the request payload
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(requestData.toString());
                writer.flush();
                writer.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    StringBuilder jsonResult = inputStreamToString(in);

                    // Parse JSON response
                    JSONObject responseJson = new JSONObject(jsonResult.toString());
                    String message = responseJson.optString("message", "");
                    if ("success".equalsIgnoreCase(message)) {
                        JSONArray dataArray = responseJson.optJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject item = dataArray.getJSONObject(i);
                            String refereeId = item.optString("referee_id");
                            String loanId = item.optString("loan_id");
                            String memberId = item.optString("member_id");
                            String memberName = item.optString("member_name");
                            String amount = item.optString("amount");
                            String approvalStatus = item.optString("approval_status");

                            endorseList.add(new Get_Endorse(refereeId, loanId, memberId, memberName, amount, approvalStatus));
                        }
                    }
                } else {
                    Log.e("MyTask_news", "Server returned response code: " + responseCode);
                }

            } catch (Exception e) {
                Log.e("MyTask_news", "Error during HTTP request", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return endorseList;
        }

        private StringBuilder inputStreamToString(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Get_Endorse> endorseList) {
            if (endorseList != null && !endorseList.isEmpty()) {
                ListDrawer_ServiceProviders(endorseList);
            } else {
                progressBar.setVisibility(View.GONE);
                Log.e("MyTask_news", "No data received from the server.");
            }
        }
    }

    public void ListDrawer_ServiceProviders(List<Get_Endorse> endorseList) {
        EndorseAdapter adapter = new EndorseAdapter(endorseList, endorseList, ListToEndorse.this);
        adapter.notifyDataSetChanged();
        recyclerNews.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        // Enable filtering
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
}