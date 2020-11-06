package com.engineerskasa.holywrit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SRViewHolder> {

    ArrayList<String> verseRef = new ArrayList<>();
    ArrayList<String> verses = new ArrayList<>();

    public SearchResultAdapter(ArrayList<String> mVerseRef, ArrayList<String> mVerses) {
        verseRef = mVerseRef;
        verses = mVerses;
    }




    @NonNull
    @Override
    public SRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.verse_card_layout,parent,false);
        SRViewHolder srViewHolder = new SRViewHolder(v);
        return srViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SRViewHolder holder, int position) {
        holder.reference.setText(verseRef.get(position));
        holder.verse.setText(verses.get(position));
    }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    public class SRViewHolder extends RecyclerView.ViewHolder{

        public TextView reference;
        public TextView verse;
        public SRViewHolder(@NonNull View itemView) {
            super(itemView);
            reference = itemView.findViewById(R.id.heading);
            verse = itemView.findViewById(R.id.verse);
        }
    }

    public void setList(ArrayList<String> mVerseRef, ArrayList<String> mVerses) {
        this.verseRef = mVerseRef;
        this.verses = mVerses;
    }
}
