package com.remoteconfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFirebaseKeyValues();
    }


    public void getFirebaseKeyValues() {


        final HashMap<String, Object> defaultMap = new HashMap<>();

        defaultMap.put(AppConstants.USERNAME, "");
        defaultMap.put(AppConstants.PASSWORD, "");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).setMinimumFetchIntervalInSeconds(3600).build());

        mFirebaseRemoteConfig.setDefaults(defaultMap);

        Task<Void> fetchTask = mFirebaseRemoteConfig.fetch(3600 * 6);


        fetchTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // arr_lang are returned.
                    mFirebaseRemoteConfig.activateFetched();

                    String username = getValue(AppConstants.USERNAME, defaultMap);
                    String password = getValue(AppConstants.PASSWORD, defaultMap);


                } else {
                    Toast.makeText(MainActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String getValue(String parameterKey, HashMap<String, Object> defaultMap) {
        String value = mFirebaseRemoteConfig.getString(parameterKey);
        if (TextUtils.isEmpty(value))
            value = (String) defaultMap.get(parameterKey);

        return value;
    }
}
