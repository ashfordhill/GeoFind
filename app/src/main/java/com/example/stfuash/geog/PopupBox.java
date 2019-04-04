package com.example.stfuash.geog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* Popup box shows the dialog for when a game is complete
    Will show "Quit" which will return to GameOptions
    Will show "New Game" which refreshes the MapActivity with a new coordinate
 */
public class PopupBox extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button quit, newgame;

    public PopupBox(Activity a) {
        super(a);
        this.c = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.popup_box);

        quit = (Button) findViewById(R.id.popup_newgameButton);
        newgame = (Button) findViewById(R.id.popup_quitButton);

        quit.setOnClickListener(this);
        newgame.setOnClickListener(this);









    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_quitButton:
                c.finish();
                break;
            case R.id.popup_newgameButton:
                dismiss();
                Intent i = new Intent(c, MapsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(i);
                break;
            default:
                break;

        }
        dismiss();
    }

    public void receiveString(String s) {
        TextView text = (TextView) findViewById(R.id.distance);
        text.setText(s);

    }
}
