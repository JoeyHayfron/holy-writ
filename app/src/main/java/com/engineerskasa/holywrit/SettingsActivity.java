package com.engineerskasa.holywrit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.engineerskasa.holywrit.ui.home.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    public static final String
            DARK_MODE_SWITCH = "dark_mode";
    public static final String
            FONT_SIZE = "font";
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
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        registerChangeListener();
    }

    private void registerChangeListener () {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    recreate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}