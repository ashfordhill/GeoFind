package com.example.stfuash.geog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public String distanceMessage ="";
    private boolean userClicked = false; //if user made a guess (which will show as marker)
    private boolean gameover = false; //if user has submitted their guess (by pressing the confirm button)
    private ArrayList<Marker> markers;
    private GoogleMap mMap;
    private LatLng answerCoord; //the answer to the game
    private LatLng userGuessCoord; //user's current best guess
    private final LatLng[] mapCameraCoords = {new LatLng(37.0902, -95.7129), //US
            new LatLng(54.5260, 15.2551), //EU
            new LatLng(36.6171, 101.7782)}; //CN
    private String answerName; //string name of the answer to the game
    private int distanceTrue = 0;

   //pulled from bundle passed by GameOptions, based on user location selection
    private int gameType = 0; //0 = US, 1 = EU, 2 = CN

    private static final String newGame = "New Game", confirm = "Confirm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Pull LocationPicker code for region
        //Map starts based on game type
        Bundle b = getIntent().getExtras();
        if(b != null) {
           gameType = b.getInt("map");
        }

        //Generates a new location based on user selection
        generateAnswerLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Button to handle dialog box for new game when game round is over
        final Button newGameButton = (Button) findViewById(R.id.ConfirmNewGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userClicked) {

                    //Show distance value text on popup box
                    distanceTrue = (int) (calculateDistance() * 0.000621);
                    showAnswerLocation(v);
                    drawLine();
                    if(distanceTrue > 10000) {
                        distanceMessage = getResources().getString(R.string.loser_message);
                    } else {
                        distanceMessage = getResources().getString(R.string.distance_message, distanceTrue);

                    }



                    //Call and position popup box. Gets rid of dimming and makes transparent
                    final PopupBox pp = new PopupBox(MapsActivity.this);

                    pp.setCanceledOnTouchOutside(false);
                    pp.getWindow().setGravity(Gravity.CENTER);
                    //pp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams param = pp.getWindow().getAttributes();

                    param.y = 250;
                    param.gravity = Gravity.CENTER;
                    pp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pp.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    pp.show();
                    pp.receiveString(distanceMessage);

                    Typeface mTypeface = Typeface.createFromAsset(getAssets(),"fonts/fredoka.ttf");

                    Button b1 = (Button) pp.findViewById(R.id.popup_newgameButton);
                    Button b2 = (Button) pp.findViewById(R.id.popup_quitButton);

                    b1.setTypeface(mTypeface);
                    b2.setTypeface(mTypeface);

                } else {
                    //do
                }




            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Update starting position of camera to proper location
        CameraUpdate starter = CameraUpdateFactory.newLatLng(mapCameraCoords[gameType]);
        mMap.moveCamera(starter);

        //Set the custom style for google maps
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle);
        mMap.setMapStyle(style);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override

            //When user taps map, adds a draggable marker. Will not add multiple markers on map
            public void onMapClick(LatLng point) {
                if(!userClicked) {
                    markers = new ArrayList<Marker>();
                    Marker userGuess = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(point.latitude, point.longitude)).draggable(true).title("Best Guess"));
                    markers.add(userGuess);
                    userGuessCoord = userGuess.getPosition();
                    userClicked = true;
                }

            }

        });
    }



    private double calculateDistance() {
        if(userClicked) {
            float[] f = new float[2];
            Location.distanceBetween(userGuessCoord.latitude, userGuessCoord.longitude,
                    answerCoord.latitude, answerCoord.longitude, f);
            return (double) f[0];
        }
        return 0;

    }

    //For gameover, shows the distance between guess and actual location in a red line
    private void drawLine() {
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add((userGuessCoord),(answerCoord))
                .width(5)
                .color(Color.RED));
    }

    public void removeMarker(View view) {
        if(userClicked) {
            clearMarkers();
        }
        userClicked = false;
    }

    public void clearMarkers() {
        if(markers == null) {
            return;
        }
        for(Marker m: markers) {
            m.remove();
        }
        markers = null;
    }

    public void generateAnswerLocation(View view) {
        LocationPicker lp = new LocationPicker();
        answerName = lp.pickCity(gameType);
        answerCoord = getCityCoords(answerName);
        TextView t = (TextView) findViewById(R.id.LocationNameXML);
        t.setText("Place " + answerName);
        clearMarkers();
    }

    public void generateAnswerLocation() {
        LocationPicker lp = new LocationPicker();
        answerName = lp.pickCity(gameType);
        answerCoord = getCityCoords(answerName);
        TextView t = (TextView ) findViewById(R.id.LocationNameXML);
        t.setText("Place " + answerName);
        clearMarkers();
    }


    //To DO:
    //Make a way to send a message if user tries to confirm but hasn't guessed yet
    public void showAnswerLocation(View view) {
        //if(userClicked) {

            markers.add(mMap.addMarker(new MarkerOptions().position(
                    answerCoord).title("Answer")));

            gameover=true;
        //}

        //Add private method to show a path and distance

    }


    //Unused for now
    public LatLng getCityCoords(String city) {
        Geocoder geo = new Geocoder(this, Locale.US);
        Address a = null;
        LatLng ll = null;

        try {
            a = (geo.getFromLocationName(city, 1)).get(0);
            ll = new LatLng(a.getLatitude(), a.getLongitude());

        } catch (Exception e) {

        }

        return ll;
    }
}
