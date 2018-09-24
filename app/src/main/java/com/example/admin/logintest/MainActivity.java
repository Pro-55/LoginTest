package com.example.admin.logintest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView userFirstName, userLastName, gender, birthDay, eMail;
    private ProgressDialog progressDialog;
    private ImageView profilePicture;
    private static final String GRAPH_URL = "https://graph.facebook.com/";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        userFirstName = findViewById(R.id.userFirstName);
        userLastName = findViewById(R.id.userLastName);
        profilePicture = findViewById(R.id.profilePicture);
        gender = findViewById(R.id.gender);
        birthDay = findViewById(R.id.birthDay);
        eMail = findViewById(R.id.eMail);

        LoginButton loginButton = findViewById(R.id.facebookLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_gender", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving Data.....");
                progressDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
                        Log.d(TAG, ": LogTag: Response: " + object.toString());
                        getData(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,gender,email,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            eMail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, ": LogTag: RequestCode: " + requestCode);
        Log.d(TAG, ": LogTag: ResultCode: " + resultCode);
        Log.d(TAG, ": LogTag: Data: " + data);
    }

    public void getData(JSONObject object) {
        try {
            URL profilePictureUrl = new URL(GRAPH_URL + object.getString("id") + "/picture?width=250&height=250");

            Picasso.with(this).load(profilePictureUrl.toString()).into(profilePicture);

            userFirstName.setText(object.getString("first_name"));
            userLastName.setText(object.getString("last_name"));
            gender.setText(object.getString("gender"));
            birthDay.setText(object.getString("birthday"));
            eMail.setText(object.getString("email"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    
}
