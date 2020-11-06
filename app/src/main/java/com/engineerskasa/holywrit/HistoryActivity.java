package com.engineerskasa.holywrit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.engineerskasa.holywrit.MainActivity.mode;

public class HistoryActivity extends AppCompatActivity implements ItemClickListener{

    RecyclerView historyRecyclerView;
    RecyclerView.LayoutManager historyRecyclerLayoutManager;
    RecyclerView.Adapter historyRecyclerAdapter;
    TextView noHistory;
    ArrayList<History> historyInfo;
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
        setContentView(R.layout.activity_history);

        noHistory = findViewById(R.id.no_history);
        searchView = findViewById(R.id.history_search_field);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        final SaveHistoryDB mSaveHistoryDB = SaveHistoryDB.getInstance(this);
        mSaveHistoryDB.open();

        historyInfo = mSaveHistoryDB.fetchHistory();
        Collections.reverse(historyInfo);
        if(historyInfo.size() == 0)
            noHistory.setVisibility(View.VISIBLE);

        historyRecyclerView = findViewById(R.id.history_rv);
        historyRecyclerLayoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(historyRecyclerLayoutManager);
        historyRecyclerAdapter = new HistoryAdapter(historyInfo,this);
        historyRecyclerView.setAdapter(historyRecyclerAdapter);


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                historyInfo = mSaveHistoryDB.searchHistory(searchView.getText().toString());
                historyRecyclerAdapter = new HistoryAdapter(historyInfo,HistoryActivity.this);
                historyRecyclerView.setAdapter(historyRecyclerAdapter);
                historyRecyclerAdapter.notifyDataSetChanged();
                noHistory.setVisibility(View.GONE);

                if(historyInfo.size() == 0)
                    noHistory.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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

                        historyInfo = mSaveHistoryDB.searchHistory(searchView.getText().toString());
                        historyRecyclerAdapter = new HistoryAdapter(historyInfo,HistoryActivity.this);
                        historyRecyclerView.setAdapter(historyRecyclerAdapter);
                        historyRecyclerAdapter.notifyDataSetChanged();


                        if(historyInfo.size() == 0)
                            noHistory.setVisibility(View.VISIBLE);

                        return true;
                    }
                }
                return false;
            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemClick(View v, int position) {
        final DataAccess dataAccess = DataAccess.getInstance(this);
        dataAccess.open();
        TextView history = v.findViewById(R.id.history);
        TextView mode = v.findViewById(R.id.mode);

        if (mode.getText().toString().equals("ref") ) {
             ArrayList<ArrayList<String>> searchResult = dataAccess.refForOneVerse(history.getText().toString());
             Intent intent = new Intent(HistoryActivity.this, SearchResult.class);
             Bundle args = new Bundle();
             args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
             intent.putExtra("inputRef",history.getText().toString());
             intent.putExtra("loadedVerses",args);
             startActivity(intent);
        }
        if (mode.getText().toString().equals("text") ) {
            ArrayList<ArrayList<String>> searchResult = dataAccess.textModeSearch(history.getText().toString());
            Intent intent = new Intent(HistoryActivity.this, SearchResult.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
            intent.putExtra("inputRef",history.getText().toString());
            intent.putExtra("loadedVerses",args);
            startActivity(intent);
        }
    }
}

