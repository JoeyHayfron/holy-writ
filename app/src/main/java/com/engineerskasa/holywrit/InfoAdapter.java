package com.engineerskasa.holywrit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    ArrayList<BookInfo> bookInfo = new ArrayList<>();

    public InfoAdapter(ArrayList<BookInfo> mBookInfo) {
        bookInfo = mBookInfo;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_info_layout,parent, false);
        InfoViewHolder iVH = new InfoViewHolder(v);
        return iVH;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {

        holder.name.setText(bookInfo.get(position).getName());
        holder.info.setText(bookInfo.get(position).getCaption());
        holder.testament.setText("("+bookInfo.get(position).getTestament()+" Testament)");
        holder.author.setText("Written By: "+bookInfo.get(position).getAuthor());
        holder.chapters.setText(bookInfo.get(position).getChapters_no()+" Chapters");
        holder.genre.setText("Genre: "+bookInfo.get(position).getGenre());

        if(Character.isDigit(bookInfo.get(position).getName().charAt(0)))
            holder.bookShort.setText(bookInfo.get(position).getName().substring(0,5).replace(" ","")+".");
        else
            holder.bookShort.setText(bookInfo.get(position).getName().substring(0,3)+".");
    }

    @Override
    public int getItemCount() {
        return bookInfo.size();
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView info;
        TextView genre;
        TextView chapters;
        TextView testament;
        TextView author;
        TextView bookShort;
        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bookName);
            info = itemView.findViewById(R.id.info);
            genre = itemView.findViewById(R.id.bookGenre);
            chapters = itemView.findViewById(R.id.bookNoOfChapters);
            testament = itemView.findViewById(R.id.bookTestament);
            author = itemView.findViewById(R.id.bookAuthor);
            bookShort = itemView.findViewById(R.id.bookShort);
        }
    }
}
