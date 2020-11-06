package com.engineerskasa.holywrit.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.engineerskasa.holywrit.ACVAdapter;
import com.engineerskasa.holywrit.Bible;
import com.engineerskasa.holywrit.Book;
import com.engineerskasa.holywrit.DataAccess;
import com.engineerskasa.holywrit.History;
import com.engineerskasa.holywrit.R;
import com.engineerskasa.holywrit.SaveHistoryDB;
import com.engineerskasa.holywrit.SearchResult;
import com.engineerskasa.holywrit.SettingsActivity;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.engineerskasa.holywrit.ACVAdapter.itemDeleted;
import static com.engineerskasa.holywrit.MainActivity.mode;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    AutoCompleteTextView searchView;
//    private AdView mAdView;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = root.findViewById(R.id.search_field);


//        mAdView = root.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        final DataAccess dataAccess = DataAccess.getInstance(getContext());
        dataAccess.open();
        final SaveHistoryDB mSaveHistoryDB = SaveHistoryDB.getInstance(getContext());
        mSaveHistoryDB.open();
        ArrayList<History> historyList = mSaveHistoryDB.fetchHistory();
        List<Book> bookList = new ArrayList<>();
        bookList = dataAccess.getAllBooks();
        Bible bibleInfo = dataAccess.getBibleInfo();


        ArrayList<String> reff = new ArrayList<>();

        for(History h: historyList)
            reff.add(h.getRef());
        ACVAdapter adapter = new ACVAdapter(getContext(), reff);
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
                            ArrayList<ArrayList<String>> searchResult = dataAccess.refForOneVerse(searchView.getText().toString());
                            if(searchResult.get(0).get(0) != null)
                                mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);
                            Intent intent = new Intent(getActivity(), SearchResult.class);
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
                            intent.putExtra("loadedVerses",args);
                            intent.putExtra("inputRef",searchView.getText().toString());
                            getActivity().startActivity(intent);
                        }catch (Exception ex){
                            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();
                        }

                    }
                    else if(mode == "text") {
                        try{

                            ArrayList<ArrayList<String>> searchResult = dataAccess.textModeSearch(searchView.getText().toString());



                            if(searchResult.get(0).get(0) != null)
                                mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);
                            Intent intent = new Intent(getActivity(), SearchResult.class);
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
                            intent.putExtra("loadedVerses",args);
                            intent.putExtra("inputRef",searchView.getText().toString());
                            getActivity().startActivity(intent);
                        }catch (Exception ex){
                            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();
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
                                ArrayList<ArrayList<String>> searchResult = dataAccess.refForOneVerse(searchView.getText().toString());
                                if(searchResult.get(0).get(0) != null)
                                    mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);
                                Intent intent = new Intent(getActivity(), SearchResult.class);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
                                intent.putExtra("loadedVerses",args);
                                intent.putExtra("inputRef",searchView.getText().toString());
                                getActivity().startActivity(intent);
                            }catch (Exception ex){
                                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();
                            }

                        }else if(mode == "text") {
                            try{

                                ArrayList<ArrayList<String>> searchResult = dataAccess.textModeSearch(searchView.getText().toString());



                                if(searchResult.get(0).get(0) != null)
                                    mSaveHistoryDB.saveHistory(searchView.getText().toString(),mode,format);
                                Intent intent = new Intent(getActivity(), SearchResult.class);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLISTOFVERSES",(Serializable)searchResult);
                                intent.putExtra("loadedVerses",args);
                                intent.putExtra("inputRef",searchView.getText().toString());
                                getActivity().startActivity(intent);
                            }catch (Exception ex){
                                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();
                            }

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        return root;
    }

}
