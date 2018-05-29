package com.example.hole1.recipebook;

import android.net.Uri;


public class MyProviderContract  {
    public static final String AUTHORITY="com.example.hole1.recipebook.CProvider";

    public static final Uri RECIPE_URI = Uri.parse("content://"+AUTHORITY+"/recipeTable");

    public static final String _ID="_id";
    public static final String TITLE="title";
    public static final String INSTRUCTION="instruction";
    public static final String INGREDIENTS="ingredients";
    public static final String CATEGORY="category";
    public static final String sortOrder=MyProviderContract.TITLE+" COLLATE NOCASE ASC";
}

