package com.cookbook.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cookbook.ButtonRemoveClickListener;
import com.cookbook.R;
import com.cookbook.pojo.Ingredient;

import java.util.List;
import java.util.Objects;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private ButtonRemoveClickListener removeButtonClickListener;

    public IngredientListAdapter(List<Ingredient> ingredients, ButtonRemoveClickListener removeButtonClickListener) {
        this.ingredients = ingredients;
        this.removeButtonClickListener = removeButtonClickListener;
    }

    public List<Ingredient> getIngredients() {return ingredients;}

    public void add(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        ingredients.remove(position);
        notifyDataSetChanged();
    }

    public boolean contains(Ingredient ing) {
        for (Ingredient i : ingredients) {
            if (i.equals(ing)) return true;
        }
        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCaption.setText(ingredients.get(position).caption);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public Ingredient get(int position) {
        return ingredients.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCaption;
        ImageButton btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            tvCaption = (TextView)itemView.findViewById(R.id.tvCaption);
            btnRemove = (ImageButton)itemView.findViewById(R.id.btnRemove);
            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeButtonClickListener.onClick(getAdapterPosition());
        }
    }
}
