package com.cookbook.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cookbook.helpers.BitmapHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "name",
        "icon"
})
public class Category {

    @JsonProperty("id")
    public long id;
    @JsonProperty("name")
    public String name;

    @JsonIgnore
    public Bitmap icon;

    public interface CategoryClickListener {
        void onClick(Category category);
    }

    //Пустой конструктор для JsonConvertor'a
    public Category() {
    }

    @JsonCreator
    public Category(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("icon") byte[] iconBytes) {
        this.id = id;
        this.name = name;
        this.icon = BitmapHelper.getImage(iconBytes);
    }
}