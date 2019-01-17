package org.municipal.myapplication.sr.recentwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.adapter.CustomActivityNoticeAdapter;
import org.municipal.myapplication.sr.adapter.CustomActivityRecentWorkAdapter;
import org.municipal.myapplication.sr.home.ActivityMain;
import org.municipal.myapplication.sr.modelClass.UserModelClass;
import org.municipal.myapplication.sr.notice.NoticeActivityHome;

import java.util.ArrayList;

public class RecentWorkActivityHome extends AppCompatActivity {
    ArrayList<UserModelClass> arrayList = new ArrayList<>();
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recentworkhome);

        //============toolbar code starts===============
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Recent Work(s)");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecentWorkActivityHome.this,ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                RecentWorkActivityHome.this.finish();
//                onBackPressed();
            }
        });
        //============toolbar code ends===============

        //==========Recycler code initializing and setting layoutManager starts======
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_recentwork);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
        loadData();
    }

    //===============load data from server code starts=============
    public void loadData(){
        String url = "http://gangarampur.sambadsaradin.com/Gangarampur_ci/api/fetch_recent_work";
        final ProgressDialog loading = ProgressDialog.show(RecentWorkActivityHome.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        display_notice_data(response);
                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void display_notice_data(String request) {
//              userModelclassList.clear();
        final ProgressDialog loading = ProgressDialog.show(RecentWorkActivityHome.this, "Loading", "Please wait...", true, false);
        try{
            JSONArray jsonArray = new JSONArray(request);
            for (int i=0; i <jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UserModelClass userModelClass = new UserModelClass();
                userModelClass.setRecent_work_heading(jsonObject.getString("recent_work_heading"));
                userModelClass.setRecent_work_content(jsonObject.getString("recent_work_content"));
                userModelClass.setRecent_work_image(jsonObject.getString("recent_work_image"));

                /*userModelClass.setNews_heading(jsonObject.getString("news_heading"));
                userModelClass.setNews_content(jsonObject.getString("news_content"));
                userModelClass.setNews_image(jsonObject.getString("news_image"));*/
                arrayList.add(userModelClass);
            }
            mRecyclerView.setAdapter(new CustomActivityRecentWorkAdapter(RecentWorkActivityHome.this,arrayList));
            loading.dismiss();

        } catch (JSONException e){
            loading.dismiss();
            e.printStackTrace();
        }
    }
    //===============load data from server code ends=============
}
