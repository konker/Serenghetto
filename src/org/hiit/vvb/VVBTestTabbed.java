package org.hiit.vvb;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;

public class VVBTestTabbed extends TabActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Do the same for the other tabs
        intent = new Intent().setClass(this, VVBTestGame.class);
        spec = tabHost.newTabSpec("game").setIndicator("Game",
                          res.getDrawable(R.drawable.tab_game))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, VVBTestCodes.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("codes").setIndicator("Codes",
                          res.getDrawable(R.drawable.tab_codes))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}


