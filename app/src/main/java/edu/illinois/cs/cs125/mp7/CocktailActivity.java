package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;


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

    public static String getName(final String json) {
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
            JsonObject drink = arrayOfDrinks.get(0).getAsJsonObject();

            return drink.get("strDrink").getAsString();
        }
    }

    public static String getInstructions(final String json) {
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
            JsonObject drink = arrayOfDrinks.get(0).getAsJsonObject();

            return drink.get("strInstructions").getAsString();
        }
    }

    public static String getIngredients(final String json) {
      String Ingredient = "";
      if (json == null) {
        return null;
      } else {
        JsonParser parser = new JsonParser();
        JsonObject drinks = parser.parse(json).getAsJsonObject();
        JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
        JsonObject drink = arrayOfDrinks.get(0).getAsJsonObject();
        for (int i = 1; i < 16; i++) {
          if (drink.get("strIngredient" + i).getAsString().equals("")) {
            Ingredient = Ingredient + "";
          } else {
            Ingredient = Ingredient + drink.get("strIngredient" + i).getAsString() +
                    " (" + drink.get("strMeasure" + i).getAsString() + ")" + "\n";
          }
        }
      }
      return Ingredient;
    }

    public static String getPicture(final String json) {
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
            JsonObject drink = arrayOfDrinks.get(0).getAsJsonObject();
            return drink.get("strDrinkThumb").getAsString();
        }
    }

    String TAG = "MP777";

    public void makeIntentCall() {
        String[] searchData;
        String json;
        int typeOfSearch;
        Intent intent = getIntent();
        searchData = intent.getStringArrayExtra(SearchActivity.jsonToParse);
        json = searchData[0];
        typeOfSearch = Integer.parseInt(searchData[1]);
        Log.d(TAG, searchData[1]);
        Log.d(TAG, Integer.toString(typeOfSearch));
        Log.d(TAG,json);
        if (json != null) {
            Log.d(TAG, "json is not null");
            if (typeOfSearch == 1 || typeOfSearch == 3) {
                String instructions = getInstructions(json);
                String ingredients = getIngredients(json);
                String imageURL = getPicture(json);
                String name = getName(json);
                Log.d(TAG, "got the details");
                final TextView textView = findViewById(R.id.instructionsCocktail);
                textView.setText("Instructions :\n" + instructions);
                final TextView textView1 = findViewById(R.id.textView3);
                textView1.setText("Ingredients :\n" + ingredients);
                final ImageView imageView = findViewById(R.id.imageView3);
                Picasso.with(this).load(imageURL).into(imageView);
                final TextView cocktailName = findViewById(R.id.cocktailName);
                cocktailName.setText(name);
                cocktailName.setVisibility(View.VISIBLE);
            } else if (typeOfSearch == 2 || typeOfSearch == 4) {
                String imageURL = getPicture(json);
                String name = getName(json);
                final ImageView imageView = findViewById(R.id.imageView3);
                Picasso.with(this).load(imageURL).into(imageView);
                final TextView cocktailName = findViewById(R.id.cocktailName);
                cocktailName.setText(name);
                cocktailName.setVisibility(View.VISIBLE);
            }
        }
    }
}
