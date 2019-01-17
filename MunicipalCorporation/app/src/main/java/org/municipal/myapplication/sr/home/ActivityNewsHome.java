package org.municipal.myapplication.sr.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.Complaint.ComplainActivityHome;
import org.municipal.myapplication.sr.modelClass.UserSingletonModelClass;

public class ActivityNewsHome extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModelClass userSingletonModelClass = UserSingletonModelClass.getInstance();
    TextView tv_content, tv_heading;
    ImageButton btn_share;
    ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home);
        tv_content = (TextView)findViewById(R.id.tv_content);
        tv_heading = (TextView)findViewById(R.id.tv_heading);
        btn_share = (ImageButton) findViewById(R.id.btn_share);
        img = (ImageView)findViewById(R.id.img);
        String result = Html.fromHtml(userSingletonModelClass.getNews_content()).toString(); //---code to remove all html tags
        tv_content.setText(result);
        String result_heading = Html.fromHtml(userSingletonModelClass.getNews_heading()).toString(); //---code to remove all html tags
        tv_heading.setText(result_heading);
        Picasso.with(ActivityNewsHome.this).load(userSingletonModelClass.getNews_image()).into(img);

        //============toolbar code starts===============
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Gangarampur Municipality");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityNewsHome.this,ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ActivityNewsHome.this.finish();
//                onBackPressed();
            }
        });
        //============toolbar code ends===============

        btn_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_share:
                shareIt();
                break;
        }
    }

    //==========function for sharing data, code starts============
    public void shareIt(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String result = Html.fromHtml(userSingletonModelClass.getNews_content()).toString();
        String shareBody = result;
        String result_heading = Html.fromHtml(userSingletonModelClass.getNews_heading()).toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, result_heading);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    //==========function for sharing data, code ends==============
}
