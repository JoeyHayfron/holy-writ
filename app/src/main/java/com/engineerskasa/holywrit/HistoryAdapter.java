package com.engineerskasa.holywrit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    ArrayList<History> histories = new ArrayList<>();
    private static ItemClickListener sItemClickListener;

    public HistoryAdapter(ArrayList<History> mHistories, ItemClickListener itemClickListener) {
        histories = mHistories;
        sItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        HistoryViewHolder HV = new HistoryViewHolder(v);
        return HV;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        holder.history.setText(histories.get(position).getRef());
        holder.timestamp.setText(histories.get(position).getTimestamp());
        holder.mode.setText(histories.get(position).getMode());

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView history;
        TextView timestamp;
        TextView mode;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            history = itemView.findViewById(R.id.history);
            timestamp = itemView.findViewById(R.id.timestamp);
            mode = itemView.findViewById(R.id.mode);
        }

        @Override
        public void onClick(View v) {
            sItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
