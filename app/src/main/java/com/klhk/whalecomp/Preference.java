package com.klhk.whalecomp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.Set;

public class Preference {
    private static Preference mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private Preference(){ }

    public static Preference getInstance(){
        if (mInstance == null) mInstance = new Preference();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writePreference(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }
    public void writePreferenceSet(String key, Set<String> value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putStringSet(key,  value);
        e.commit();
    }
    public void writeBooleanPreference(String key, Boolean value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putBoolean(key, value);
        e.commit();
    }
    public void clearPreferences(){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.clear();
        e.commit();
    }

    public String returnValue(String key){
        return mMyPreferences.getString(key,"");
    }
    public Set<String> returnArray(String key){
        return mMyPreferences.getStringSet(key, Collections.singleton(""));
    }

    public boolean returnBoolean(String key){
        return mMyPreferences.getBoolean(key,false);
    }
}
