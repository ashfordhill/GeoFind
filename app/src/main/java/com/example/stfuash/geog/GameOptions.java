package com.example.stfuash.geog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
    }

    public void onPinpointUS(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle b = new Bundle();
        b.putInt("map", 0);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    public void onPinpointEU(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle b = new Bundle();
        b.putInt("map", 1);
        i.putExtras(b);
        startActivity(i);
        finish();
    }
    public void onPinpointCN(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle b = new Bundle();
        b.putInt("map", 2);
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}
