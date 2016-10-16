package com.cookbook.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.helpers.DBIngredientsHelper;
import com.cookbook.pojo.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private List<Ingredient> mResults;
    private Context context;
    private DBIngredientsHelper dbIngredientsHelper;

    public IngAutoCompleteAdapter(Context context) {
        this.context = context;
        mResults = new ArrayList<>();
        dbIngredientsHelper = new DBIngredientsHelper(context);
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Ingredient getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ing_autocomplete_layout, parent, false);
        }
        Ingredient i = getItem(position);
        ((TextView) convertView.findViewById(R.id.text1)).setText(i.caption);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Ingredient> ings = findIngredients(constraint.toString());
                    filterResults.values = ings;
                    filterResults.count = ings.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<Ingredient>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    private List<Ingredient> findIngredients(String query) {
        List<Ingredient> ingredients = dbIngredientsHelper.getByName(query);
        if (ingredients==null)
            ingredients = new ArrayList<>();
        return ingredients;
    }
}
