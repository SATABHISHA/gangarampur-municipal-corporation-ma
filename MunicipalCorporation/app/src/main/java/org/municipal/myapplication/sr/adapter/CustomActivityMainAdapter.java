package org.municipal.myapplication.sr.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.home.ActivityNewsHome;
import org.municipal.myapplication.sr.modelClass.UserModelClass;
import org.municipal.myapplication.sr.modelClass.UserSingletonModelClass;

import java.util.ArrayList;

public class CustomActivityMainAdapter extends RecyclerView.Adapter<CustomActivityMainAdapter.MyViewHolder> {

    public LayoutInflater inflater;
    public static ArrayList<UserModelClass> userModelClassArrayList;
    public static String sumValue = "0";
    private Context context;
    UserSingletonModelClass userSingletonModelClass = UserSingletonModelClass.getInstance();

    public CustomActivityMainAdapter(Context ctx, ArrayList<UserModelClass> userModelClassArrayList){

        inflater = LayoutInflater.from(ctx);
        this.userModelClassArrayList = userModelClassArrayList;
    }
    @NonNull
    @Override
    public CustomActivityMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_news_feed, parent, false);
        CustomActivityMainAdapter.MyViewHolder holder = new CustomActivityMainAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomActivityMainAdapter.MyViewHolder holder, int position) {
        holder.tv_headline.setText(userModelClassArrayList.get(position).getNews_heading());
        String result = Html.fromHtml(userModelClassArrayList.get(position).getNews_content()).toString(); //---code to remove all html tags
        holder.tv_content.setText(result);
        Picasso.with(context).load(userModelClassArrayList.get(position).getNews_image()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return userModelClassArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_headline,tv_content;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_headline = (TextView)itemView.findViewById(R.id.tv_headline);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            img = (ImageView)itemView.findViewById(R.id.img);
            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    userSingletonModelClass.setNews_content(userModelClassArrayList.get(position).getNews_content());
                    userSingletonModelClass.setNews_image(userModelClassArrayList.get(position).getNews_image());
                    userSingletonModelClass.setNews_heading(userModelClassArrayList.get(position).getNews_heading());
                    context.startActivity(new Intent(context,ActivityNewsHome.class));
                }
            });

        }
    }
}
