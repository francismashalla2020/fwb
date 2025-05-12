package com.thinkbold.fwb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.thinkbold.fwb.adapters.MemberAdapter;
import com.thinkbold.fwb.models.Get_members;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AllMembers extends AppCompatActivity {
    RecyclerView recyclerNews;
    private java.net.URL url;
    private HttpURLConnection urlConnection;
    private MemberAdapter adapter;
    private List<Get_members> allmembers;
    SearchView sv;
    private String URL_NEWS = "https://thinkbold.africa/fwb/app_member.php";
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, grid2;
    private MyTask_news taskNews;
    ProgressBar progressBar;
    ImageView menusT;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);
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
                PopupMenu popupMenu = new PopupMenu(AllMembers.this, v);
                popupMenu.setOnMenuItemClickListener(AllMembers.this::onMenuItemClick);
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
        Random rand = new Random();
        int n = rand.nextInt(50000000) + 1;
        String randomstamp = "70timestamp" + n;
        URL_NEWS = "https://thinkbold.africa/fwb/app_member.php?tmps" + randomstamp + "";
        URL_NEWS = URL_NEWS.replaceAll("\\s+", "%20");
        Log.e("URL_TIC", "URL_TIC:" + URL_NEWS);
        taskNews = new MyTask_news();
        taskNews.execute(new String[]{URL_NEWS});
    }
    private class MyTask_news extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                urlConnection.setConnectTimeout(5000);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder jsonResult = inputStreamToString(in);
                allmembers = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(jsonResult.toString());
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String id = jsonChildNode.optString("id");
                    String first_name = jsonChildNode.optString("first_name");
                    String middle_name = jsonChildNode.optString("middle_name");
                    String surname = jsonChildNode.optString("surname");
                    String phone_no = jsonChildNode.optString("phone_no");
                    String email = jsonChildNode.optString("email");
                    String address = jsonChildNode.optString("address");
                    String password = jsonChildNode.optString("password");
                    String role = jsonChildNode.optString("role");
                    String member_status = jsonChildNode.optString("member_status");
                    String date_created = jsonChildNode.optString("date_created");
                    String date_updated = jsonChildNode.optString("date_updated");

                    allmembers.add(new Get_members(id, first_name, middle_name, surname, phone_no, email, address, password, role, member_status, date_created, date_updated));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return allmembers;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (Exception e) {
                finish();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(List list) {
            ListDrawer_ServiceProviders(list);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
        }
    }
    public void ListDrawer_ServiceProviders(List<Get_members> customList) {
        adapter = new MemberAdapter(customList, customList, AllMembers.this);
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
}