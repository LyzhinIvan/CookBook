package com.cookbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.pojo.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {


    private List<Recipe> recipes;
    private Recipe.RecipeClickListener recipeClickListener;
    private Context context;

    public RecipeListAdapter(Context context, List<Recipe> recipes, Recipe.RecipeClickListener recipeClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.recipeClickListener = recipeClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe r = recipes.get(position);

        if (r.icon!=null)
            holder.ivIcon.setImageBitmap(r.icon);

        holder.tvCaption.setText(r.name);
        holder.tvCookingTime.setText(String.format("%d мин",r.cookingTime));
        holder.tvSatiety.setText(r.satiety.toString());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivIcon;
        private TextView tvCaption;
        private TextView tvCookingTime;
        private TextView tvSatiety;

        ViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvSatiety = (TextView) itemView.findViewById(R.id.tvSatiety);
            tvCookingTime = (TextView) itemView.findViewById(R.id.tvCookingTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recipeClickListener.onClick(recipes.get(getAdapterPosition()));
        }
    }
}
