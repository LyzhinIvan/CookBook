package com.cookbook.adapters;

import android.content.Context;
import android.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.pojo.Ingredient;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<Pair<Ingredient,String>> ingredients;
    private Ingredient.IngredientClickListener listener;
    private Context context;

    public RecipeIngredientAdapter(Context context, List<Pair<Ingredient,String>> ingredients, Ingredient.IngredientClickListener listener) {
        this.context = context;
        this.ingredients = ingredients;
        this.listener = listener;
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
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCaption;
        private TextView tvQuantity;
        private ImageButton btnAddToShopList;

        ViewHolder(View itemView) {
            super(itemView);

            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            btnAddToShopList = (ImageButton)itemView.findViewById(R.id.btnToShopList);
            btnAddToShopList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(ingredients.get(getAdapterPosition()).first);
        }
    }
}