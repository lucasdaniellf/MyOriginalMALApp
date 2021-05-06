package com.example.myoriginalmalapp.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AccessObject;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.SingletonQueue;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class LoginActivity extends AppCompatActivity
{
    private final AccessAPI accessAPI = AppConfig.getAccessApi();
    private int status_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        try {
            findViewById(R.id.progressLayoutHomeId).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_content_login).setAlpha((float) 0.4);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            FileReader reader = new FileReader(new File(LoginActivity.this.getFilesDir(), "user_info.json"));
            BufferedReader br = new BufferedReader(reader);

            AccessObject accessObject = AppConfig.getGson().fromJson(br, AccessObject.class);
            br.close();

            String refresh_token = accessObject.getRefresh_token();
            SingletonQueue.getSingleton(LoginActivity.this).addToRequestQueue(AppConfig.getAccessApi().refreshToken(refresh_token, LoginActivity.this, new AccessAPI.onAuthTokenListener() {
                @Override
                public void onStatusCodeResponse(int status_code) {
                    com.example.myoriginalmalapp.activities.LoginActivity.this.status_code = status_code;
                    if (com.example.myoriginalmalapp.activities.LoginActivity.this.status_code == 200)
                    {
                        Intent intent = new Intent(com.example.myoriginalmalapp.activities.LoginActivity.this, Home_Activity.class);
                        startActivity(intent);

//                        findViewById(R.id.layout_content_login).setAlpha((float) 1);
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);

                        finish();
                    }
                    if (com.example.myoriginalmalapp.activities.LoginActivity.this.status_code >= 400)
                    {
                        findViewById(R.id.layout_content_login).setAlpha((float) 1);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);
                    }
                }
            }));

        } catch (Exception e) {
            findViewById(R.id.layout_content_login).setAlpha((float) 1);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);
        }

        finally
        {

            EditText username = (EditText) findViewById(R.id.editTextUsernameID);
            EditText password = (EditText) findViewById(R.id.editTextPasswordID);

            Button loginButton = (Button) findViewById(R.id.btnLoginID);

            // ------------------ BUTTON LOGIN LISTENER ------------------------ //
            loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LoginActivity.this.findViewById(R.id.progressLayoutHomeId).setVisibility(View.VISIBLE);
                    LoginActivity.this.findViewById(R.id.layout_content_login).setAlpha((float) 0.4);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    SingletonQueue.getSingleton(com.example.myoriginalmalapp.activities.LoginActivity.this).addToRequestQueue(accessAPI.authToken(
                        username.getText().toString(),
                        password.getText().toString(),
                        com.example.myoriginalmalapp.activities.LoginActivity.this,
                        new AccessAPI.onAuthTokenListener() {
                            @Override
                            public void onStatusCodeResponse(int status_code)
                            {
                                com.example.myoriginalmalapp.activities.LoginActivity.this.status_code = status_code;
                                if (com.example.myoriginalmalapp.activities.LoginActivity.this.status_code == 200)
                                {

                                    Intent intent = new Intent(com.example.myoriginalmalapp.activities.LoginActivity.this, Home_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                if (com.example.myoriginalmalapp.activities.LoginActivity.this.status_code >= 400)
                                {
                                    LoginActivity.this.findViewById(R.id.layout_content_login).setAlpha((float) 1);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    LoginActivity.this.findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(com.example.myoriginalmalapp.activities.LoginActivity.this);
                                    builder.setMessage("Wrong Username and/or Password")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    builder.create();
                                    builder.show();
                                }
                            }
                        })
                    );
                }
            });
            // ------------------------ END BUTTON LOGIN LISTENER -------------------- //

            // ------------------------- BUTTON SIGNUP LISTENER ----------------------- //
            findViewById(R.id.sign_up_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "https://myanimelist.net/register.php";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));

                    Intent chooser = Intent.createChooser(intent, "Choose the app:");
                    try
                    {
                        startActivity(chooser);
                    }
                    catch(ActivityNotFoundException e)
                    {
                        Toast.makeText(LoginActivity.this, "The device does not have an application for this.", Toast.LENGTH_LONG).show();
                    }
                }
            });


            // ------------------------- END BUTTON SIGNUP LISTENER ------------------ //
        }
    }
}
