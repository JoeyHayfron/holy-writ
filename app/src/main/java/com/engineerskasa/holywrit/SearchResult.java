package com.engineerskasa.holywrit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.engineerskasa.holywrit.MainActivity.mode;

public class SearchResult extends AppCompatActivity {

    RecyclerView searchRecyclerView;
    RecyclerView.LayoutManager searchRecyclerViewLayoutManager;
    RecyclerView.Adapter searchRecyclerViewAdapter;
    TextView notFound;
    AutoCompleteTextView searchView;
    ArrayList<ArrayList<String>> searchResult = new ArrayList<>();
//    private AdView mAdView;


    @SuppressLint("ClickableViewAccessibility")
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
        setContentView(R.layout.activity_search_result);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        notFound = findViewById(R.id.not_found);
        searchView = findViewById(R.id.sResult_search_field);
        searchView.setFocusableInTouchMode(false);
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(true);
        searchView.setFocusable(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("inputRef"));
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("loadedVerses");
        searchResult = (ArrayList<ArrayList<String>>) args.getSerializable("ARRAYLISTOFVERSES");


        if(mode == "text" && (searchResult.get(0).size() == 0))
            notFound.setVisibility(View.VISIBLE);

        searchRecyclerView = findViewById(R.id.search_result_rv);
        searchRecyclerViewLayoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(searchRecyclerViewLayoutManager);
        searchRecyclerViewAdapter = new SearchResultAdapter(searchResult.get(0),searchResult.get(1));
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);

        final DataAccess dataAccess = DataAccess.getInstance(this);
        dataAccess.open();
        final SaveHistoryDB mSaveHistoryDB = SaveHistoryDB.getInstance(this);
        mSaveHistoryDB.open();

        ArrayList<History> historyList = mSaveHistoryDB.fetchHistory();
        ArrayList<String> reff = new ArrayList<>();
        for(History h: historyList)
            reff.add(h.getRef());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,reff);
        searchView.setThreshold(1);


        searchView.setAdapter(adapter);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String format = s.format(new Date());
                    if(mode == "ref") {

                        try{
                            searchResult = dataAccess.refForOneVerse(searchView.getText().toString());
                            if(searchResult.get(0).get(0) != null)
                                mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);

                            searchRecyclerViewAdapter = new SearchResultAdapter(searchResult.get(0),searchResult.get(1));
                            searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
                            searchRecyclerViewAdapter.notifyDataSetChanged();
                            actionBar.setTitle(searchView.getText().toString());

                        }catch (Exception ex){
                            Toast.makeText(SearchResult.this, "Invalid input", Toast.LENGTH_LONG).show();
                        }

                    }
                    else if(mode == "text") {
                        try{

                            searchResult = dataAccess.textModeSearch(searchView.getText().toString());
                            if(searchResult.get(0).get(0) != null)
                                mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);

                            searchRecyclerViewAdapter = new SearchResultAdapter(searchResult.get(0),searchResult.get(1));
                            searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
                            searchRecyclerViewAdapter.notifyDataSetChanged();
                            actionBar.setTitle(searchView.getText().toString());

                        }catch (Exception ex){
                            Toast.makeText(SearchResult.this, "Invalid input", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                return false;
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
                        // your action here
                        if(mode == "ref") {
                            try{
                                searchResult = dataAccess.refForOneVerse(searchView.getText().toString());
                                if(searchResult.get(0).get(0) != null)
                                    mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);

                                searchRecyclerViewAdapter = new SearchResultAdapter(searchResult.get(0),searchResult.get(1));
                                searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
                                searchRecyclerViewAdapter.notifyDataSetChanged();
                            }catch (Exception ex){
                                Toast.makeText(SearchResult.this, "Invalid input. Check the mode.", Toast.LENGTH_LONG).show();
                            }

                        }else if(mode == "text") {
                            try{
                                searchResult = new ArrayList<>();
                                searchResult = dataAccess.textModeSearch(searchView.getText().toString());

                                if(searchResult.get(0).get(0) != null)
                                    mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);

                                searchRecyclerViewAdapter = new SearchResultAdapter(searchResult.get(0),searchResult.get(1));
                                searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
                                searchRecyclerViewAdapter.notifyDataSetChanged();
                            }catch (Exception ex){
                                Toast.makeText(SearchResult.this, "Invalid input. Check Mode.", Toast.LENGTH_LONG).show();
                            }

                        }

                        return true;
                    }
                }
                return false;
            }
        });

    }
}
