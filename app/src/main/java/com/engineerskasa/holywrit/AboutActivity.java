package com.engineerskasa.holywrit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class AboutActivity extends AppCompatActivity {
//    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.DARK_MODE_SWITCH, false);
        String listPref = sharedPref.getString
                (SettingsActivity.FONT_SIZE, "small");

        if(switchPref){
            if(listPref.equals("small")){
                setTheme(R.style.AppThemeDarkSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppThemeDarkMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppThemeDarkLarge);
            }
        }else{
            if(listPref.equals("small")){
                setTheme(R.style.AppThemeLightSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppThemeLightMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppThemeLightLarge);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }
}
