package org.municipal.myapplication.sr.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.Complaint.ComplainActivityHome;
import org.municipal.myapplication.sr.adapter.CustomActivityMainAdapter;
import org.municipal.myapplication.sr.adapter.SlidingImage_Adapter;
import org.municipal.myapplication.sr.career.CareerActivityHome;
import org.municipal.myapplication.sr.event.EventActivityHome;
import org.municipal.myapplication.sr.facility.FacilityActivityHome;
import org.municipal.myapplication.sr.fragment.FragmentComplain;
import org.municipal.myapplication.sr.modelClass.ImageModel;
import org.municipal.myapplication.sr.modelClass.UserModelClass;
import org.municipal.myapplication.sr.notice.NoticeActivityHome;
import org.municipal.myapplication.sr.recentwork.RecentWorkActivityHome;
import org.municipal.myapplication.sr.tender.TenderActivityHome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mRecyclerView;
    ArrayList<UserModelClass> arrayList = new ArrayList<>();
    MultiAutoCompleteTextView ma_tv_message;
    EditText ed_complain_id;

    //=======Sliding images variables starts..============
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.home, R.drawable.events,
            R.drawable.facility};
    //=======Sliding images variables ends..============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //==========Recycler code initializing and setting layoutManager starts======
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_news);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
        loadData();

        //========Sliding images code starts=======
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        init();
        //========Sliding images code ends=======
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_complaint) {
            startActivity(new Intent(ActivityMain.this, ComplainActivityHome.class));
//           fragment = new FragmentComplain();
        }else if (id == R.id.nav_view_complaint){
            view_complain_popup();
        } else if(id == R.id.nav_notice){
            startActivity(new Intent(ActivityMain.this, NoticeActivityHome.class));
        }else if(id == R.id.nav_events){
            startActivity(new Intent(ActivityMain.this, EventActivityHome.class));
        }else if(id == R.id.nav_tender){
            startActivity(new Intent(ActivityMain.this, TenderActivityHome.class));
        }else if(id == R.id.nav_career){
            startActivity(new Intent(ActivityMain.this, CareerActivityHome.class));
        }else if(id == R.id.nav_facility){
            startActivity(new Intent(ActivityMain.this, FacilityActivityHome.class));
        }else if(id == R.id.nav_recent_work){
            startActivity(new Intent(ActivityMain.this, RecentWorkActivityHome.class));
        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
       /* if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //===============load data from server code starts=============
      public void loadData(){
        String url = "http://gangarampur.sambadsaradin.com/Gangarampur_ci/api/fetch_news";
          final ProgressDialog loading = ProgressDialog.show(ActivityMain.this, "Loading", "Please wait...", true, false);
          StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                  Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          display_news_data(response);
                          loading.dismiss();
                      }
                  }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  loading.dismiss();
                  error.printStackTrace();
              }
          });
          stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                  DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
          RequestQueue queue = Volley.newRequestQueue(this);
          queue.add(stringRequest);
      }

          public void display_news_data(String request) {
//              userModelclassList.clear();
              final ProgressDialog loading = ProgressDialog.show(ActivityMain.this, "Loading", "Please wait...", true, false);
              try{
                  JSONArray jsonArray = new JSONArray(request);
                  for (int i=0; i <jsonArray.length(); i++){
                      JSONObject jsonObject = jsonArray.getJSONObject(i);
                      UserModelClass userModelClass = new UserModelClass();
                      userModelClass.setNews_heading(jsonObject.getString("news_heading"));
                      userModelClass.setNews_content(jsonObject.getString("news_content"));
                      userModelClass.setNews_image(jsonObject.getString("news_image"));
                      arrayList.add(userModelClass);
                  }
                  mRecyclerView.setAdapter(new CustomActivityMainAdapter(ActivityMain.this,arrayList));
                  loading.dismiss();

              } catch (JSONException e){
                  loading.dismiss();
                  e.printStackTrace();
              }
          }
    //===============load data from server code ends=============

    //============popup dialog code starts==========
    public void view_complain_popup(){
        LayoutInflater li1 = LayoutInflater.from(this);
        View dialog1 = li1.inflate(R.layout.popup_view_complain, null);
        ImageButton imgbtn_close1 = (ImageButton) dialog1.findViewById(R.id.imgbtn_close);
        ma_tv_message = (MultiAutoCompleteTextView)dialog1.findViewById(R.id.ma_tv_message);
        ed_complain_id = (EditText)dialog1.findViewById(R.id.ed_complain_id);
        Button btn_view_complain = (Button)dialog1.findViewById(R.id.btn_view_complain);

        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setView(dialog1);
        alert1.setCancelable(false);
        //Creating an alert dialog
        final AlertDialog alertDialog1 = alert1.create();
        alertDialog1.show();
        imgbtn_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        btn_view_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma_tv_message.setVisibility(View.VISIBLE);
                fetch_complaint_status();
            }
        });

    }
    //===========popup dialog code ends===========
 //===========code to fetch complaint status data starts===========
    public void fetch_complaint_status(){
        String url = "http://gangarampur.sambadsaradin.com/Gangarampur_ci/api/view_complaint_status";
        final ProgressDialog loading = ProgressDialog.show(ActivityMain.this, "Loading", "Please wait...", true, false);
//        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
//                    Toast.makeText(getApplicationContext(),jsonObj.toString(),Toast.LENGTH_LONG).show();
                    if(jsonObj.getString("success").equals(0)){
                        loading.dismiss();
                        ma_tv_message.setText(jsonObj.getString("message"));
                    }else {
                        loading.dismiss();
                        ma_tv_message.setText(jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                Log.d("msg",response.toString());
//                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error! Please try again",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                int complain_id = Integer.parseInt(ed_complain_id.getText().toString());
                params.put("com_id:", ed_complain_id.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityMain.this);
        requestQueue.add(stringRequest);
    }
 //===========code to fetch complaint status data ends===========

    //=============image sliding code starts===========
    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(ActivityMain.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
    //=============image sliding code ends===========
}
