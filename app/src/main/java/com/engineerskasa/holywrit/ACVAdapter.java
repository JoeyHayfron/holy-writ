package com.engineerskasa.holywrit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.engineerskasa.holywrit.R;

import java.util.ArrayList;

public class ACVAdapter extends ArrayAdapter<String> {
    private ArrayList<String> items;
    private ArrayList<String> itemsAll;
    private ArrayList<String> suggestions;
    private int viewResourceId;
    public static boolean itemDeleted = false;

    @SuppressWarnings("unchecked")
    public ACVAdapter(Context context,
                                ArrayList<String> items) {
        super(context, android.R.layout.simple_dropdown_item_1line, items);
        this.items = items;
        this.itemsAll = (ArrayList<String>) items.clone();
        this.suggestions = new ArrayList<String>();

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.acv_suggestions, parent,false);
        }
        String product = items.get(position);
        if (product != null) {
            TextView productLabel = (TextView)  convertView.findViewById(R.id.my_text);
            ImageView del = convertView.findViewById(R.id.delete_history);
            if (productLabel != null) {
                productLabel.setText(product);
            }

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SaveHistoryDB mSaveHistoryDB = SaveHistoryDB.getInstance(getContext());
                    mSaveHistoryDB.open();
                    long itemID = 0;
                    String oneToDelete = "";
                    for(History h:mSaveHistoryDB.fetchHistory()){
                        if(h.getRef().equals(product)) {
                            itemID = h.getId();
                            oneToDelete = product;
                        }
                    }
                    mSaveHistoryDB.deleteID(String.valueOf(itemID));
                    suggestions.remove(oneToDelete);
                }
            });
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = String.valueOf(resultValue);
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String product : itemsAll) {
                    if (product.toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (String c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}