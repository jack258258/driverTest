package com.example.testing.drivertest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    //okhttp connection
    private String url = "http://172.104.110.249:3000/app/api/driver_login";

    // UI references.
    private EditText mPhoneView;
    private EditText mPasswordView;
    private EditText mTeamCodeView;
    private View mProgressView;
    private View mLoginFormView;


    //SharedPreferences
    private SharedPreferences settings;
    private static final String data_token = "DATA";

    private Boolean Login_success;
    private String token;
    private String team;

    ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        mPhoneView = (EditText) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mTeamCodeView = (EditText)findViewById(R.id.team_code);

        //mProgressView = findViewById(R.id.login);         //進度bar


        settings = getSharedPreferences(data_token,0);            //讀取Sharedpreference 物件內容getString(key, "unknow")，讀取被寫入的資料
        String token = settings.getString("token", "");

        Log.e("TOKEN", token);
        Button SignInButton = (Button) findViewById(R.id.login);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    private String phone, team_code;
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mPhoneView.setError(null);              //改phone
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();
        team_code = mTeamCodeView.getText().toString();
        /*if(team_code.isEmpty()){
            team_code = "000";
        }
        Log.i("team_code", team_code);*/

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.


        // Check for a valid email address.


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Log.d("OKHTTP", "Post function call");
            login_connection(url, phone, password, team_code);
        }
    }

    private void login_connection(String url, final String account, String password, final String team_code){

        RequestBody body = new FormBody.Builder()
                .add("account", account)      //.add("鍵", "值")
                .add("password", password)
                .add("team_code", team_code)                           //車隊專屬碼
                .build();
        Log.i("account", account);
        OkHttpManager.postEnqueueAsync(url, body, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    Log.d("OkHttp Message-result", result);
                    Boolean Login_success = new JSONObject(result).getBoolean("success");
                    String token = new JSONObject(result).getString("token");
                    String serviceList = new JSONObject(result).getString("serviceList");
                    /*Gson gson = new Gson();
                    JsonPara jsonPara = gson.fromJson(result, JsonPara.class);
                    Login_success = jsonPara.getSuccess();*/
                    Log.d("OkHttp Success", String.valueOf(Login_success));
                    // Log.d("OkHttp Message", jsonPara.getMessage());
                    if(Login_success.equals(false)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //收取資料成功，所以讓ProgressBar  進度條消失
                                mProgressView.setVisibility(View.GONE);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("訊息")
                                        .setMessage("登入失敗")
                                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        });}
                    if(Login_success.equals(true)){
                        saveData(token, account, team_code,serviceList);
                        Log.d("Token",token);
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Mock1Activity.class);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    /*
        *           使用Sharedperference 儲存token
        */
    public void saveData(String token, String phone, String team_code, String serviceList){
        settings = getSharedPreferences(data_token,0);
        settings.edit()
                .putString("token", token)        //寫入Sharedpreference 物件內容putString(key, value)  getText()讀取EDIT上的資料
                .commit();
        Log.i("setting", String.valueOf(settings));

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}
