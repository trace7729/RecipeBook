package com.example.hole1.recipebook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplayRecipe extends AppCompatActivity implements View.OnClickListener {

    private int chosenRecipe;
    private String chosenRecipeTitle;
    private Cursor cursor;

    private EditText title;
    private EditText intro;
    private EditText ingrid;

    private Button editRecipe;
    private Button deleteRecipe;
    private Button save;
    private Spinner category;

    private String recipeTitle;
    private String recipeIntro;
    private String recipeIngrid;
    private String recipeCategory;

    List<String> spinnerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        if (getIntent().getStringExtra("recipeTitle") == null) {
            displayById();
        } else {
            displayByTitle();
        }





        deleteRecipe.setOnClickListener(this);
        editRecipe.setOnClickListener(this);
    }


    /*
       If the user click the recipe in listview,system will use this function
       to get the information of recipe.
     */
    public void displayById() {
        Bundle bundle = getIntent().getExtras();
        chosenRecipe = bundle.getInt("chosenRecipe") + 1;

        initialComponent();

        getAllRecipes();

        for (int i = 0; i < chosenRecipe; i++) {
            cursor.moveToNext();
        }
        recipeTitle = cursor.getString(cursor.getColumnIndex(MyProviderContract.TITLE));
        title.setText(recipeTitle);
        recipeIntro = cursor.getString(cursor.getColumnIndex(MyProviderContract.INSTRUCTION));
        intro.setText(recipeIntro);
        recipeIngrid = cursor.getString(cursor.getColumnIndex(MyProviderContract.INGREDIENTS));
        ingrid.setText(recipeIngrid);

        recipeCategory = cursor.getString(cursor.getColumnIndex(MyProviderContract.CATEGORY));
        setCategory(recipeCategory);
    }


    /*
        If the user search the recipe title, system will use this function to get
    the information of recipe.
     */
    public void displayByTitle() {
        chosenRecipeTitle = getIntent().getStringExtra("recipeTitle");
        initialComponent();
        getAllRecipes();

        while (cursor.moveToNext()) {
            if (chosenRecipeTitle.equals(cursor.getString(cursor.getColumnIndex(MyProviderContract.TITLE)))) {
                recipeTitle = cursor.getString(cursor.getColumnIndex(MyProviderContract.TITLE));
                recipeIntro = cursor.getString(cursor.getColumnIndex(MyProviderContract.INSTRUCTION));
                recipeIngrid = cursor.getString(cursor.getColumnIndex(MyProviderContract.INGREDIENTS));
                recipeCategory = cursor.getString(cursor.getColumnIndex(MyProviderContract.CATEGORY));
            }
        }
        title.setText(recipeTitle);
        intro.setText(recipeIntro);
        ingrid.setText(recipeIngrid);
        setCategory(recipeCategory);


    }

    //get all the recipes from the database.
    public void getAllRecipes() {
        String[] projection = new String[]{
                MyProviderContract._ID,
                MyProviderContract.TITLE,
                MyProviderContract.INSTRUCTION,
                MyProviderContract.INGREDIENTS,
                MyProviderContract.CATEGORY
        };
        cursor = getContentResolver().query(MyProviderContract.RECIPE_URI, projection, null, null, MyProviderContract.TITLE+" COLLATE NOCASE ASC");
    }


    public void initialComponent() {
        title = (EditText) findViewById(R.id.chosenRecipeTitle);
        intro = (EditText) findViewById(R.id.chosenRecipeIntro);
        ingrid = (EditText) findViewById(R.id.ingredients);

        category = (Spinner) findViewById(R.id.category);
        category.setEnabled(false);

        editRecipe = (Button) findViewById(R.id.editRecipe);
        deleteRecipe = (Button) findViewById(R.id.deleteRecipe);
        save = (Button) findViewById(R.id.save);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editRecipe:
                editRecipe();
                break;
            case R.id.deleteRecipe:
                deleteRecipe();
                break;
            case R.id.save:
                updateRecipe();
                title.setFocusable(false);
                intro.setFocusable(false);
                ingrid.setFocusable(false);

                category.setEnabled(false);
                category.setClickable(false);



                save.setEnabled(false);
                Intent intent = new Intent(DisplayRecipe.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }


    //User could choose to edit the recipe.
    public void editRecipe() {
        title.setFocusableInTouchMode(true);
        intro.setFocusableInTouchMode(true);
        ingrid.setFocusableInTouchMode(true);
        category.setFocusableInTouchMode(true);

        category.setEnabled(true);
        category.setClickable(true);

        save.setEnabled(true);
        save.setOnClickListener(this);
    }


    /*
        After user edit the recipe, the button 'save will be allowed to click, click this button
    could update the recipe's information.
    */
    public void updateRecipe() {
        try {
            int chosenID = cursor.getInt(cursor.getColumnIndex(MyProviderContract._ID));
            ContentUris uris = new ContentUris();
            Uri editedUri = uris.withAppendedId(MyProviderContract.RECIPE_URI, chosenID);

            String editTitle = title.getText().toString();
            String editintro = intro.getText().toString();
            String editingrid = ingrid.getText().toString();
            String cat = category.getSelectedItem().toString();

            ContentValues editRecipe = new ContentValues();
            editRecipe.put(MyProviderContract.TITLE, editTitle);
            editRecipe.put(MyProviderContract.INSTRUCTION, editintro);
            editRecipe.put(MyProviderContract.INGREDIENTS, editingrid);
            editRecipe.put(MyProviderContract.CATEGORY, cat);

            getContentResolver().update(editedUri, editRecipe, MyProviderContract._ID + "=?", null);

            Toast.makeText(this, "Recipe Successfully Updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }


    }

    //Click the 'delete' button to delete a specific recipe.
    //Remind user before delete the recipe.
    public void deleteRecipe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRecipe.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this recipe?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int chosenID = cursor.getInt(cursor.getColumnIndex(MyProviderContract._ID));
                ContentUris uris = new ContentUris();
                Uri editedUri = uris.withAppendedId(MyProviderContract.RECIPE_URI, chosenID);
                getContentResolver().delete(editedUri, MyProviderContract._ID + "=?", null);
                Intent intent = new Intent(DisplayRecipe.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DisplayRecipe.this, "Nothing Changed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadCategories() {
        spinnerArray = new ArrayList<String>();
        spinnerArray.add("appetizer");
        spinnerArray.add("entree");
        spinnerArray.add("desert");
        spinnerArray.add("salad");
        spinnerArray.add("side dish");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter);

    }

    private void setCategory(String categoryValue) {
        loadCategories();
        int index = spinnerArray.indexOf(categoryValue);

        category.setSelection(index);
    }
}
