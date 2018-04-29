package edu.illinois.cs.cs125.mp7;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arr;
    private static RequestQueue requestQueue;
    static String jsonFromID;
    static String jsonToParseFromID;
    static String typeOfSearch = "5";
    String TAG = "MP777";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        requestQueue = Volley.newRequestQueue(this);
        String[] searchData;
        String json;
        int typeOfSearch;
        Intent intent = getIntent();
        searchData = intent.getStringArrayExtra(SearchActivity.jsonToParseToList);
        json = searchData[0];
        typeOfSearch = Integer.parseInt(searchData[1]);
        Log.d(TAG, searchData[1]);

        if (typeOfSearch == 3) {
            Intent toCocktailActivity = new Intent(ListActivity.this, CocktailActivity.class);
            toCocktailActivity.putExtra(jsonToParseFromID, new String[] {json, searchData[1]});
            startActivity(toCocktailActivity);
        }

        int numberOfDrinks = getLength(json);
        final String[] drinkIDs = getIDs(json, numberOfDrinks);

        listView = findViewById(R.id.list);
        String[] nameData = getNames(json, numberOfDrinks);
        arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView item = (TextView) super.getView(position, convertView, parent);
                item.setTextColor(Color.parseColor("#E0F2F1"));
                return item;
            }
        };
        listView.setAdapter(arr);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                APIIDCall(drinkIDs[position]);
            }
        });
    }

    public static String[] getNames(final String json, final int size) {
        String[] names = new String[size];
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
            for (int i = 0; i < arrayOfDrinks.size(); i++) {
                JsonObject drink = arrayOfDrinks.get(i).getAsJsonObject();
                names[i] = drink.get("strDrink").getAsString();
            }

            return names;
        }
    }

    public static int getLength(final String json) {
        if (json == null) {
            return 0;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();

            return arrayOfDrinks.size();
        }
    }

    public static String[] getIDs(final String json, final int size) {
        String[] IDs = new String[size];
        if (json == null) {
            return null;
        } else {
            JsonParser parser = new JsonParser();
            JsonObject drinks = parser.parse(json).getAsJsonObject();
            JsonArray arrayOfDrinks = drinks.get("drinks").getAsJsonArray();
            for (int i = 0; i < arrayOfDrinks.size(); i++) {
                JsonObject drink = arrayOfDrinks.get(i).getAsJsonObject();
                IDs[i] = drink.get("idDrink").getAsString();
            }

            return IDs;
        }
    }

    public void APIIDCall(final String search) {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + search,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            jsonFromID = response.toString();
                            Intent toCocktailActivity = new Intent(ListActivity.this,
                                    CocktailActivity.class);
                            toCocktailActivity.putExtra(jsonToParseFromID, new String[] {jsonFromID, typeOfSearch});
                            startActivity(toCocktailActivity);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.d(TAG, "error obtaining json");
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
