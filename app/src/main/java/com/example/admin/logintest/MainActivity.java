package com.example.admin.logintest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
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
    private TextView userFirstName, userLastName, gender, birthDay, eMail, userID;
    private ProgressDialog progressDialog;
    private LoginButton loginButton;
    private Button buttonLoginFB, buttonLogOut;
    private ImageView profilePicture;
    private Profile profile;
    private static final String GRAPH_URL = "https://graph.facebook.com/";
    private URL imageURL;
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
        userID = findViewById(R.id.userID);
        buttonLoginFB = findViewById(R.id.buttonLoginFB);
        buttonLogOut = findViewById(R.id.buttonLogOut);

        loginButton = findViewById(R.id.facebookLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_gender", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving Data.....");
                progressDialog.show();

                buttonLoginFB.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                buttonLogOut.setVisibility(View.VISIBLE);

                String accessToken = loginResult.getAccessToken().getToken();
                Preferences.setFacebookAccessToken(getApplicationContext(), accessToken);
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

        profile = Profile.getCurrentProfile().getCurrentProfile();
        if (profile != null) {
            buttonLoginFB.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            buttonLogOut.setVisibility(View.VISIBLE);
            displayUserInfo();
        } else {
            buttonLoginFB.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            buttonLogOut.setVisibility(View.GONE);
            displayUserInfo();
        }

        buttonLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();

                if (profile == null) {
                    buttonLoginFB.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    buttonLogOut.setVisibility(View.GONE);
                    clearData();
                }
            }
        });


        displayUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, ": LogTag: RequestCode: " + requestCode);
        Log.d(TAG, ": LogTag: ResultCode: " + resultCode);
        Log.d(TAG, ": LogTag: Data: " + data);
        displayUserInfo();
    }

    public void getData(JSONObject object) {

        try {
            URL profilePictureUrl = new URL(GRAPH_URL + object.getString("id") + "/picture?width=250&height=250");
            imageURL = profilePictureUrl;
            Preferences.setProfilePicture(getApplicationContext(), imageURL.toString());
            Preferences.setFacebookID(getApplicationContext(), object.getString("id"));
            Preferences.setUserFirstName(getApplicationContext(), object.getString("first_name"));
            Preferences.setUserLastName(getApplicationContext(), object.getString("last_name"));
            Preferences.setUserGender(getApplicationContext(), object.getString("gender"));
            Preferences.setUserBirthDate(getApplicationContext(), object.getString("birthday"));
            Preferences.setUserEmail(getApplicationContext(), object.getString("email"));

            displayUserInfo();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayUserInfo() {
        userFirstName.setText(Preferences.getUserFirstName(getApplicationContext()));
        userLastName.setText(Preferences.getUserLastName(getApplicationContext()));
        gender.setText(Preferences.getUserGender(getApplicationContext()));
        birthDay.setText(Preferences.getUserBirthDate(getApplicationContext()));
        eMail.setText(Preferences.getUserEmail(getApplicationContext()));
        userID.setText(Preferences.getFacebookID(getApplicationContext()));
        Picasso.with(this).load(Preferences.getProfilePicture(getApplicationContext())).into(profilePicture);
    }

    private void clearData() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        displayUserInfo();
    }

}
