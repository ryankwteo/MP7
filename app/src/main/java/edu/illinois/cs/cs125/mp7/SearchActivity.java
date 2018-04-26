package edu.illinois.cs.cs125.mp7;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        requestQueue = Volley.newRequestQueue(this);

        final Button nameSearch = findViewById(R.id.nameSearch);
        nameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchType = 1;
                search(searchType);
            }
        });

        final Button ingredientSearch = findViewById(R.id.ingredientSearch);
        ingredientSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchType = 2;
                search(searchType);
            }
        });

        final Button randomSearch = findViewById(R.id.randomSearch);
        randomSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchType = 3;
                APICall("", searchType);
            }
        });

        final Button cocktailType = findViewById(R.id.cocktailType);
        cocktailType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchType = 4;
                search(searchType);
            }
        });

    }

    static int searchType;
    static String json;
    static String jsonToParse;
    String nameToSearch;
    String TAG = "MP777";
    private static RequestQueue requestQueue;

    public void search(final int searchType) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Choose your poison");
        final EditText input = new EditText(SearchActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int unused) {
                nameToSearch = input.getText().toString().toLowerCase();
                APICall(nameToSearch, searchType);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int unused) {
                dialog.cancel();
            }
        });

        // Display the dialog
        builder.show();
    }


    public void APICall(final String search, final int searchType) {

        String searchParameter = null;

        switch (searchType) {
            case 1: {
                searchParameter = "search.php?s=";
                break;
            }
            case 2: {
                searchParameter = "filter.php?i=";
                break;
            }
            case 3: {
                searchParameter = "random.php";
                break;
            }
            case 4: {
                searchParameter = "filter.php?a=";
                break;
            }
        }

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.thecocktaildb.com/api/json/v1/1/" + searchParameter + search,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            json = response.toString();
                            Intent toCocktailActivity = new Intent(SearchActivity.this,
                                    CocktailActivity.class);
                            toCocktailActivity.putExtra(jsonToParse, json);
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
