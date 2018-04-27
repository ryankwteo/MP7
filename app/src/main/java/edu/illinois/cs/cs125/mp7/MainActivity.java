package edu.illinois.cs.cs125.mp7;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(),"fonts/DancingScript-Regular.ttf");
        textView.setTypeface(typeFace);
        textView2.setTypeface(typeface2);
    }

    public void openCocktails(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
