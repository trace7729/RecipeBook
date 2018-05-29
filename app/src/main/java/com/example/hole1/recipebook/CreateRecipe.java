package com.example.hole1.recipebook;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipe extends AppCompatActivity {

    private EditText titleField;
    private EditText introField;
    private EditText ingriField;
    private Spinner category;


    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        titleField = (EditText) findViewById(R.id.title);
        introField = (EditText) findViewById(R.id.intro);
        ingriField = (EditText) findViewById(R.id.ingredients);
        category = (Spinner) findViewById(R.id.category);

        loadCategories();


        create = (Button) findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleField.getText().toString();
                String intro = introField.getText().toString();
                String ingri = ingriField.getText().toString();
                String cat = category.getSelectedItem().toString();

                ContentValues newRecipe = new ContentValues();
                newRecipe.put(MyProviderContract.TITLE, title);
                newRecipe.put(MyProviderContract.INSTRUCTION, intro);
                newRecipe.put(MyProviderContract.INGREDIENTS, ingri);
                newRecipe.put(MyProviderContract.CATEGORY, cat);

                //Insert the title and introduction got from the EditText field.
                getContentResolver().insert(MyProviderContract.RECIPE_URI, newRecipe);

                Intent intent = new Intent(CreateRecipe.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadCategories(){
        List<String> spinnerArray =  new ArrayList<String>();
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
}
