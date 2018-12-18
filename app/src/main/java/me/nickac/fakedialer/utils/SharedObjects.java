package me.nickac.fakedialer.utils;

import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum SharedObjects {
    INSTANCE;

    private FragmentTransaction fragmentTransaction;
    private Gson gson;

    public FragmentTransaction getFragmentTransaction() {
        return fragmentTransaction;
    }

    public void setFragmentTransaction(FragmentTransaction fragmentTransaction) {
        this.fragmentTransaction = fragmentTransaction;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().create();
        }
        return gson;
    }
}
