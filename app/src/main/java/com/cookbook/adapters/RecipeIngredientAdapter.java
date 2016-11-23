package com.cookbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.dao.DBShopListHelper;
import com.cookbook.pojo.Ingredient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<Pair<Ingredient, String>> ingredients;
    private Ingredient.IngredientClickListener listener;
    private Set<String> inShopList;

    public RecipeIngredientAdapter(Context context, List<Pair<Ingredient, String>> ingredients, Ingredient.IngredientClickListener listener) {

        this.ingredients = ingredients;
        this.listener = listener;

        inShopList = new HashSet<>();
        for (String s : new DBShopListHelper(context).getAll()) {
            inShopList.add(s.toLowerCase());
        }
    }

    public boolean isInList(String ing) {
        return inShopList.contains(ing.toLowerCase());
    }

    public void addInList(String ing) {
        inShopList.add(ing.toLowerCase());
    }

    @Override
    public RecipeIngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_ingredient_item, parent, false);

        return new RecipeIngredientAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeIngredientAdapter.ViewHolder holder, int position) {
        String caption = ingredients.get(position).first.caption;
        String quantity = ingredients.get(position).second;

        holder.tvCaption.setText(caption);
        holder.tvQuantity.setText(quantity);
        if (!inShopList.contains(caption.toLowerCase())) {
            holder.btnAddToShopList.setImageResource(R.drawable.ic_add_to_shop_list);
        } else {
            holder.btnAddToShopList.setImageResource(R.drawable.ic_in_shop_list);
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void removeIng(String ing) {
        inShopList.remove(ing.toLowerCase());
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCaption;
        private TextView tvQuantity;
        private ImageButton btnAddToShopList;

        ViewHolder(View itemView) {
            super(itemView);

            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            btnAddToShopList = (ImageButton) itemView.findViewById(R.id.btnToShopList);
            btnAddToShopList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(ingredients.get(getAdapterPosition()).first);
            notifyItemChanged(getAdapterPosition());
        }
    }
}
