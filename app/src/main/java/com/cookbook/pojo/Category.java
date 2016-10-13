package com.cookbook.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cookbook.helpers.BitmapHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Category {

    public long id;
    public String name;

    @JsonIgnore
    public Bitmap icon;

    public interface CategoryClickListener {
        void onClick(Category category);
    }

    @JsonCreator
    public Category(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("icon") byte[] iconBytes) {
        this.id = id;
        this.name = name;
        this.icon = BitmapHelper.getImage(iconBytes);
    }
}