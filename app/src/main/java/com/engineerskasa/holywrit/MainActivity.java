package com.engineerskasa.holywrit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;

//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    LinearLayout refMode;
    LinearLayout textMode;
    View refSelect;
    View textSelect;
    public static String mode;

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
                setTheme(R.style.AppTheme3DarkSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppTheme3DarkMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppTheme3DarkLarge);
            }
        }else{
            if(listPref.equals("small")){
                setTheme(R.style.AppTheme2LightSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppTheme2LightMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppTheme2LightLarge);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.bringToFront();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

        refMode = findViewById(R.id.ref_mode);
        textMode = findViewById(R.id.text_mode);
        refSelect = findViewById(R.id.ref_select);
        textSelect = findViewById(R.id.text_select);


        mode = "ref";

        refMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode != "ref"){
                    mode = "ref";
                    refSelect.setBackgroundResource(R.drawable.buttons_bg);
                    textSelect.setBackgroundColor(getResources().getColor(R.color.colorTrans));
                }
            }
        });

        textMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode != "text"){
                    mode = "text";
                    textSelect.setBackgroundResource(R.drawable.buttons_bg);
                    refSelect.setBackgroundColor(getResources().getColor(R.color.colorTrans));
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_info, R.id.nav_history, R.id.nav_about, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.DARK_MODE_SWITCH, false);
        String listPref = sharedPref.getString
                (SettingsActivity.FONT_SIZE, "small");

        if(switchPref){
            if(listPref.equals("small")){
                setTheme(R.style.AppTheme3DarkSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppTheme3DarkMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppTheme3DarkLarge);
            }
        }else{
            if(listPref.equals("small")){
                setTheme(R.style.AppTheme2LightSmall);
            }else if(listPref.equals("medium")){
                setTheme(R.style.AppTheme2LightMedium);
            }else if(listPref.equals("large")){
                setTheme(R.style.AppTheme2LightLarge);
            }
        }
        super.onResume();
    }
}
