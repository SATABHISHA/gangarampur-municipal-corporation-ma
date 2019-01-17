package org.municipal.myapplication.sr.adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.Config.CheckForSDCard;
import org.municipal.myapplication.sr.modelClass.UserModelClass;
import org.municipal.myapplication.sr.modelClass.UserSingletonModelClass;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

import static android.support.constraint.Constraints.TAG;

public class CustomActivityRecentWorkAdapter extends RecyclerView.Adapter<CustomActivityRecentWorkAdapter.MyViewHolder>{
    public LayoutInflater inflater;
    public static ArrayList<UserModelClass> userModelClassArrayList;
    public static String sumValue = "0";
    private Context context;
    UserSingletonModelClass userSingletonModelClass = UserSingletonModelClass.getInstance();

    public CustomActivityRecentWorkAdapter(Context ctx, ArrayList<UserModelClass> userModelClassArrayList){

        inflater = LayoutInflater.from(ctx);
        this.userModelClassArrayList = userModelClassArrayList;
    }
    @NonNull
    @Override
    public CustomActivityRecentWorkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_recentwork, parent, false);
        CustomActivityRecentWorkAdapter.MyViewHolder holder = new CustomActivityRecentWorkAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomActivityRecentWorkAdapter.MyViewHolder holder, int position) {
//        holder.tv_headline.setText(userModelClassArrayList.get(position).getNotice_heading());
        holder.tv_headline.setText(userModelClassArrayList.get(position).getRecent_work_heading());
        /*String result = Html.fromHtml(userModelClassArrayList.get(position).getNotice_content()).toString(); //---code to remove all html tags
        holder.tv_content.setText(result);
        Picasso.with(context).load(userModelClassArrayList.get(position).getNotice_image()).into(holder.img);*/

    }

    @Override
    public int getItemCount() {
        return userModelClassArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        public TextView tv_headline,tv_content;
        MultiAutoCompleteTextView tv_headline;
        ImageButton btn_download;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
//            tv_headline = (TextView)itemView.findViewById(R.id.tv_headline);
//            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
//            img = (ImageView)itemView.findViewById(R.id.img);
            tv_headline = (MultiAutoCompleteTextView)itemView.findViewById(R.id.tv_headline);
            btn_download = (ImageButton) itemView.findViewById(R.id.btn_download);
          /*  tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    userSingletonModelClass.setNews_content(userModelClassArrayList.get(position).getNews_content());
                    userSingletonModelClass.setNews_image(userModelClassArrayList.get(position).getNews_image());
                    context.startActivity(new Intent(context,ActivityNewsHome.class));
                }
            });*/
            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    String url = userModelClassArrayList.get(position).getNews_image();
                    String url = userModelClassArrayList.get(position).getRecent_work_image();

                    if (CheckForSDCard.isSDCardPresent()) {

                        //check if app has permission to write to the external storage.
                        if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //Get the URL entered
                            new CustomActivityRecentWorkAdapter.DownloadFile().execute(url);

                        } else {
                            //If permission is not present request for the same.
                            EasyPermissions.requestPermissions(context, context.getString(R.string.write_file),300, Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    } else {
                        Toast.makeText(context.getApplicationContext(),
                                "SD Card not found", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    //============Function to download file from url, code starts===========
    class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "wbmunicipalcorpfiles/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context.getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
    //============Function to download file from url, code ends===========
}
