package com.cookbook.pojo;

import android.graphics.Bitmap;

import com.cookbook.helpers.BitmapHelper;
import com.cookbook.helpers.StringsUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        this.name = StringsUtils.capitalize(name);
        this.icon = BitmapHelper.getImage(iconBytes);
    }
}