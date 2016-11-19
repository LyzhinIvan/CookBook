package com.cookbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.pojo.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesGridAdapter extends RecyclerView.Adapter<CategoriesGridAdapter.ViewHolder>{

    private List<Category> categories;
    private Category.CategoryClickListener categoryClickListener;
    private Context context;

    public CategoriesGridAdapter(Context context, List<Category> categories, Category.CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.categoryClickListener = categoryClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category c = categories.get(position);

        if (c.icon!=null)
            holder.ivIcon.setImageBitmap(c.icon);
        holder.tvCaption.setText(c.name);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivIcon;
        private TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivCategoryIcon);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCategoryCaption);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            categoryClickListener.onClick(categories.get(getAdapterPosition()));
        }
    }
}
