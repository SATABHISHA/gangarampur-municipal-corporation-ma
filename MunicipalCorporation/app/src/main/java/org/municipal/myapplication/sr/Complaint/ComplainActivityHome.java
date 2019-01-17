package org.municipal.myapplication.sr.Complaint;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import org.json.JSONException;
import org.json.JSONObject;
import org.municipal.myapplication.R;
import org.municipal.myapplication.sr.home.ActivityMain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplainActivityHome extends AppCompatActivity implements View.OnClickListener {
    EditText input_type, input_name, input_email, input_phone, input_ward, input_address;
    TextInputLayout input_layout_type, input_layout_name, input_layout_email, input_layout_phone, input_layout_ward, input_layout_address;
    Button btn_submit, btn_upload;
    NestedScrollView nested_scroll_desc;
    MultiAutoCompleteTextView input_desc;
    ImageView img;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    public static String imagePath = "";
    Bitmap bm;
    String[] spinnerlist_complain_type = {"Road Related Issue", "Street Light Issue", "Garbage Dumping Issue", "Water Supply Issue", "Mosquito or Other Insect Breeding Issue", "Other Issue"};
    String[] spinnerlist_ward_no = {"Ward No:1", "Ward No:2", "Ward No:3", "Ward No:4", "Ward No:5", "Ward No:6", "Ward No:7", "Ward No:8"};
    MaterialBetterSpinner materialDesignSpinner_complain_type, materialDesignSpinner_spinner_ward_no;
    public static String complain_type, ward_no;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        requestMultiplePermissions();

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
                Intent intent = new Intent(ComplainActivityHome.this,ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ComplainActivityHome.this.finish();
//                onBackPressed();
            }
        });
        //============toolbar code ends===============
//        input_type = (EditText)findViewById(R.id.input_type);
        input_name = (EditText)findViewById(R.id.input_name);
        input_email = (EditText)findViewById(R.id.input_email);
        input_phone = (EditText)findViewById(R.id.input_phone);
//        input_ward = (EditText)findViewById(R.id.input_ward);
        input_address = (EditText)findViewById(R.id.input_address);
//        input_desc = (EditText)findViewById(R.id.input_desc);  //---commenting for test purpose
        input_desc = (MultiAutoCompleteTextView)findViewById(R.id.input_desc);
        nested_scroll_desc = (NestedScrollView)findViewById(R.id.nested_scroll_desc);
        nested_scroll_desc.setFocusableInTouchMode(true);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_upload = (Button)findViewById(R.id.btn_upload);
        img = (ImageView)findViewById(R.id.img);

        //======defining inputLayout id for form validation code starts===========
//        input_layout_type = (TextInputLayout)findViewById(R.id.input_layout_type);
        input_layout_name = (TextInputLayout)findViewById(R.id.input_layout_name);
        input_layout_email = (TextInputLayout)findViewById(R.id.input_layout_email);
        input_layout_phone = (TextInputLayout)findViewById(R.id.input_layout_phone);
//        input_layout_ward = (TextInputLayout)findViewById(R.id.input_layout_ward);
        input_layout_address = (TextInputLayout)findViewById(R.id.input_layout_address);

        //======defining inputLayout id for form validation code ends===========
        //====================material spinner code starts======================
        //--------code for complain_type, starts-----
        materialDesignSpinner_complain_type = (MaterialBetterSpinner)findViewById(R.id.spinner_complain_type);
        final ArrayAdapter<String> arrayAdapter_complain_type = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerlist_complain_type);
        materialDesignSpinner_complain_type.setAdapter(arrayAdapter_complain_type);

        materialDesignSpinner_complain_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                complain_type = arrayAdapter_complain_type.getItem(i);
                Toast.makeText(getApplicationContext(),arrayAdapter_complain_type.getItem(i).toString(),Toast.LENGTH_LONG).show();
            }
        });
        //--------code for complain_type, ends-----

        //--------code for ward_no, starts--------
        materialDesignSpinner_spinner_ward_no = (MaterialBetterSpinner)findViewById(R.id.spinner_ward_no);
        final ArrayAdapter<String> arrayAdapter_ward_no = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerlist_ward_no);
        materialDesignSpinner_spinner_ward_no.setAdapter(arrayAdapter_ward_no);
        materialDesignSpinner_spinner_ward_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ward_no = arrayAdapter_ward_no.getItem(i);
                Toast.makeText(getApplicationContext(),arrayAdapter_ward_no.getItem(i).toString(),Toast.LENGTH_LONG).show();
            }
        });
        //--------code for ward_no, ends--------
        //======================material spinner code ends===========================
        //=====defining onClickListner for validation code starts=========
//        input_type.addTextChangedListener(new MyTextWatcher(input_type));
        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_email.addTextChangedListener(new MyTextWatcher(input_email));
        input_phone.addTextChangedListener(new MyTextWatcher(input_phone));
//        input_ward.addTextChangedListener(new MyTextWatcher(input_ward));
        input_address.addTextChangedListener(new MyTextWatcher(input_address));
        //=====defining onClickListner for validation code ends=========

        nested_scroll_desc.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
//                saveMultipartData();
                submitForm();
                break;
            case R.id.btn_upload:
                showPictureDialog();
                break;
            case R.id.nested_scroll_desc:
                input_desc.setFocusable(true);
                input_desc.setFocusableInTouchMode(true);
                Toast.makeText(getApplicationContext(),"Touched",Toast.LENGTH_LONG).show();
        }
    }

    public void saveData(){
        String url = "http://gangarampur.sambadsaradin.com/Gangarampur_ci/api/take_complaint";
//        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    Toast.makeText(getApplicationContext(),jsonObj.toString(),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Complain Registered Successfully",Toast.LENGTH_LONG).show();
                Log.d("msg",response.toString());
//                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error! Please try again",Toast.LENGTH_LONG).show();
//                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", input_type.getText().toString());
                params.put("name", input_name.getText().toString());
                params.put("email", input_email.getText().toString());
                params.put("phn", input_phone.getText().toString());
                params.put("word", input_ward.getText().toString());
                params.put("address", input_address.getText().toString());
                params.put("feedback_msg", input_desc.getText().toString());

//                params.put("feedback_msg", userSingletonModel.getCorpID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ComplainActivityHome.this);
        requestQueue.add(stringRequest);
    }

    //==========code to save file and string value to the server using volley, code starts=========
    public void saveMultipartData(){
        final ProgressDialog loading = ProgressDialog.show(ComplainActivityHome.this, "Loading", "Please wait...", true, false);
        RequestQueue requestQueue = Volley.newRequestQueue(ComplainActivityHome.this);
        // Add request params excluding files below
        HashMap<String, String> params = new HashMap<>();
//        params.put("type", input_type.getText().toString());
        params.put("type", complain_type);
        params.put("name", input_name.getText().toString());
        params.put("email", input_email.getText().toString());
        params.put("phn", input_phone.getText().toString());
//        params.put("word", input_ward.getText().toString());
        params.put("word", ward_no);
        params.put("address", input_address.getText().toString());
        params.put("feedback_msg", input_desc.getText().toString());
//Add files to a request
        HashMap<String, File> fileParams = new HashMap<>();
        fileParams.put("photo", new File(imagePath));

//        fileParams.put("file2", file2);
// Add header to a request, if any
        Map<String, String> header = new HashMap<>();
//        header.put("header_key","header_value");
        String url = "http://gangarampur.sambadsaradin.com/Gangarampur_ci/api/take_complaint";
        /**
         * Create a new Multipart request for sending data in form of Map<String,String > ,along with files
         */
        MultipartRequest mMultipartRequest = new MultipartRequest(url,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        // error handling
//                        mProgressDialog.cancel();
                        loading.dismiss();
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("multipartResult",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Integer success = Integer.parseInt(jsonObject.getString("success"));
                            if(success == 1){
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ComplainActivityHome.this,ActivityMain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                ComplainActivityHome.this.finish();
                            }else{
                                loading.dismiss();
                               /* Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ComplainActivityHome.this,ActivityMain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);*/
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, fileParams, params, header
        );
        mMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
/**
 * adding request to queue
 */
        requestQueue.add(mMultipartRequest);

    }
    //==========code to save file and string value to the server using volley, code ends=========

    //==============Camera/Gallery code added on 7th dec, code starts=================
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    img.setVisibility(View.VISIBLE);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(ComplainActivityHome.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ComplainActivityHome.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            img.setVisibility(View.VISIBLE);
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(ComplainActivityHome.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
//            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            imagePath = f.getAbsolutePath();
            Log.d("TAG", "File Saved::--->" + imagePath);

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    //===============Camera/Gallery code added on 7th dec, code ends==================

    //=============validate form code starts===============
    public void submitForm(){

       /* if (!validateType()) {
            return;
        }*/
        if (!validateFullName()) {
            return;
        }
        String email = input_email.getText().toString();
        if (!validateEmail() || !isValidEmail(email)) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
       /* if (!validateWard()) {
            return;
        }*/
        if (!validateAddress()) {
            return;
        }
//        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        saveMultipartData();  //----function to save data to the server
    }

   /* private boolean validateType() {
        if (input_type.getText().toString().trim().isEmpty()) {
            input_layout_type.setError("Field can't be left blank");
            requestFocus(input_type);
            return false;
        } else {
            input_layout_type.setErrorEnabled(false);
        }
        return true;
    }*/

    private boolean validateFullName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            input_layout_name.setError("Field can't be left blank");
            requestFocus(input_name);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        if (input_email.getText().toString().trim().isEmpty()) {
            input_layout_email.setError("Field can't be left blank");
            requestFocus(input_email);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone() {
        if (input_phone.getText().toString().trim().isEmpty()) {
            input_layout_phone.setError("Field can't be left blank");
            requestFocus(input_phone);
            return false;
        } else {
            input_layout_phone.setErrorEnabled(false);
        }
        return true;
    }

   /* private boolean validateWard() {
        if (input_ward.getText().toString().trim().isEmpty()) {
            input_layout_ward.setError("Field can't be left blank");
            requestFocus(input_ward);
            return false;
        } else {
            input_layout_ward.setErrorEnabled(false);
        }
        return true;
    }*/

    private boolean validateAddress() {
        if (input_address.getText().toString().trim().isEmpty()) {
            input_layout_address.setError("Field can't be left blank");
            requestFocus(input_address);
            return false;
        } else {
            input_layout_address.setErrorEnabled(false);
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        if(!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            input_layout_email.setError("Please Enter valid Email");
            requestFocus(input_email);
            return false;
        }
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (view.getId()){
               /* case R.id.input_type:
                    validateType();
                    break;*/
                case R.id.input_name:
                    validateFullName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
               /* case R.id.input_ward:
                    validateWard();
                    break;*/
                case R.id.input_address:
                    validateAddress();
                    break;
            }
        }
    }
    //==============validate form code ends=================
}
