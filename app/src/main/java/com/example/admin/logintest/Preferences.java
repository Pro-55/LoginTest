package com.example.admin.logintest;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.zxing.client.result.EmailAddressParsedResult;

public class Preferences {

    public static final String FACEBOOK_ACCESS_TOKEN = "facebook_access_token";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_BIRTH_DATE = "birth_date";
    public static final String USER_GENDER = "gender";
    public static final String USER_EMAIL = "email";
    public static final String PROFILE_PICTURE = "profile_picture";

    public static String getFacebookAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(FACEBOOK_ACCESS_TOKEN, null);
    }

    public static void setFacebookAccessToken(Context context, String accessToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FACEBOOK_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public static String getUserFirstName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_FIRST_NAME, null);
    }

    public static void setUserFirstName(Context context, String firstName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_FIRST_NAME, firstName);
        editor.commit();
    }

    public static String getUserLastName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_LAST_NAME, null);
    }

    public static void setUserLastName(Context context, String lastName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LAST_NAME, lastName);
        editor.commit();
    }

    public static String getUserBirthDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_BIRTH_DATE, null);
    }

    public static void setUserBirthDate(Context context, String birthDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_BIRTH_DATE, birthDate);
        editor.commit();
    }

    public static String getUserGender(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_GENDER, null);
    }

    public static void setUserGender(Context context, String gender) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_GENDER, gender);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public static String getProfilePicture(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString(PROFILE_PICTURE, null);
    }

    public static void setProfilePicture(Context context, String profilePictureURL) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PICTURE, profilePictureURL);
        editor.commit();
    }
}
