package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CocktailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);

        makeIntentCall();
    }

    public void openSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public static String getInstructions(final String json) {
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray typeOfDrinks = drinks.get("drinks").getAsJsonArray();
            JsonObject drink = typeOfDrinks.get(0).getAsJsonObject();

            return drink.get("strInstructions").getAsString();
        }
    }

    public void makeIntentCall() {
        Intent intent = getIntent();
        String json = intent.getStringExtra(SearchActivity.jsonToParse);
        if (json != null) {
            String instructions = getInstructions(json);
            final TextView textView = findViewById(R.id.instructionsCocktail);
            textView.setText(instructions);
        }
    }
}
