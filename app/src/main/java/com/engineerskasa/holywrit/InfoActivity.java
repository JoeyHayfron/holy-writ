package com.engineerskasa.holywrit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoActivity extends AppCompatActivity {
    RecyclerView infoRecyclerView;
    RecyclerView.LayoutManager infoRecyclerLayoutManager;
    RecyclerView.Adapter infoRecyclerAdapter;
    TextView noInfo;
    ArrayList<BookInfo> bookInfo;
    AutoCompleteTextView searchView;
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
        setContentView(R.layout.activity_info);


        noInfo = findViewById(R.id.no_info);
        searchView = findViewById(R.id.info_search_field);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Book Info");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        final DataAccess dataAccess = DataAccess.getInstance(this);
        dataAccess.open();

        bookInfo = dataAccess.fetchBookInfo();

        if(bookInfo.size() == 0)
            noInfo.setVisibility(View.VISIBLE);

        infoRecyclerView = findViewById(R.id.info_rv);
        infoRecyclerLayoutManager = new LinearLayoutManager(this);
        infoRecyclerView.setLayoutManager(infoRecyclerLayoutManager);
        infoRecyclerAdapter = new InfoAdapter(bookInfo);
        infoRecyclerView.setAdapter(infoRecyclerAdapter);


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookInfo = dataAccess.searchInfo(s.toString());
                infoRecyclerAdapter = new InfoAdapter(bookInfo);
                infoRecyclerView.setAdapter(infoRecyclerAdapter);
                infoRecyclerAdapter.notifyDataSetChanged();
                noInfo.setVisibility(View.GONE);

                if(bookInfo.size() == 0)
                    noInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                String format = s.format(new Date());
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchView.getRight() - searchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        bookInfo = dataAccess.searchInfo(searchView.getText().toString());
                        infoRecyclerAdapter = new InfoAdapter(bookInfo);
                        infoRecyclerView.setAdapter(infoRecyclerAdapter);
                        infoRecyclerAdapter.notifyDataSetChanged();
                        noInfo.setVisibility(View.GONE);

                        if(bookInfo.size() == 0)
                            noInfo.setVisibility(View.VISIBLE);


                        return true;
                    }
                }
                return false;
            }
        });


    }
}
