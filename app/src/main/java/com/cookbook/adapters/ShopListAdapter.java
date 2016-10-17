package com.cookbook.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookbook.R;

import java.util.List;

public class ShopListAdapter  extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private List<String> lines;

    public ShopListAdapter(List<String> lines) {
        this.lines = lines;
    }

    public void add(String line) {
        lines.add(line);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        lines.remove(position);
        notifyDataSetChanged();
    }

    public boolean contains(String line) {
        return lines.contains(line);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new ShopListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCaption.setText(lines.get(position));
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCaption;

        ViewHolder(View itemView) {
            super(itemView);
            tvCaption = (TextView)itemView.findViewById(R.id.tvCaption);
        }
    }
}
