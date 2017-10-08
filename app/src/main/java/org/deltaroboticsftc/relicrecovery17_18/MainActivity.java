package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.defineStorage();
        this.readStorage();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, new fragmentEditMatch());
        transaction.commit();

        try
        {
            JSONObject game = new JSONObject();

            game.put("gameTitle", "Relic Recovery");
            game.put("gameYear", 17);
            game.put("gameDescription", "Official Game2 Description");
            game.put("gameBy", "DR-2015-Official");
            game.put("gameMode", "Team");

            JSONObject auto = new JSONObject();
            auto.put("itemCount", 4);

            JSONObject autoItem1 = new JSONObject();
            autoItem1.put("itemType", "checkBox");
            autoItem1.put("title", "Jewel");
            autoItem1.put("count", 2);
            autoItem1.put("box1", "Blue Jewel Moved");
            autoItem1.put("box1Checked", false);
            autoItem1.put("box2", "Red Jewel Moved");
            autoItem1.put("box2Checked", false);
            auto.put("item1", autoItem1);

            JSONObject autoItem2 = new JSONObject();
            autoItem2.put("itemType", "counter");
            autoItem2.put("title", "Glyphs Scored");
            autoItem2.put("default", 0);
            autoItem2.put("modifier", 1);
            autoItem2.put("minValue", 0);
            autoItem2.put("maxValue", 24);
            auto.put("item2", autoItem2);

            game.put("Autonomous", auto);

            Log.i("Game", game.toString(1));
        }
        catch (Exception e)
        {

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_edit_match)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.parent_fragment, new fragmentEditMatch());
            transaction.commit();
            return true;
        }
        else if(id == R.id.action_new_match)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.parent_fragment, new fragmentEditMatch());
            transaction.commit();
            return true;
        }
        else if(id == R.id.action_review)
        {
            return true;
        }
        else if(id == R.id.action_tutorial)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        fragment = null;

        if (id == R.id.nav_edit_match)
        {
            fragment = new fragmentEditMatch();
        }
        else if(id == R.id.nav_game_selector)
        {
            fragment = new fragmentChangeGame();
        }
        else if(id == R.id.nav_setting)
        {
            fragment = new fragmentSettings();
        }

        if(fragment != null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.parent_fragment, fragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void defineStorage()
    {
        SharedPreferences DRFTCScouting = getSharedPreferences("DRFTCScouting", 1);
        SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();
        File gamesDir = new File(getFilesDir(), "Games");

        //if(DRFTCScouting.getBoolean("FirstLaunch", true))
//        if(true)
//        {
//            DRFTCScoutingEditor.putBoolean("FirstLaunch", false);
//            DRFTCScoutingEditor.putString("CurrentGame", getResources().getString(R.string.OfficialGame1));
//            DRFTCScoutingEditor.putInt("GameCount", 1);
//            DRFTCScoutingEditor.putBoolean("SettingsOutputReadable", false);
//            DRFTCScoutingEditor.putBoolean("SettingsOutputCSV", true);
//            DRFTCScoutingEditor.apply();
//
//            try
//            {
//                File DRRelicRecoveryFile = new File(gamesDir, "OfficialGame1.DRSgame");
//                Log.i("DR-RR File Deleted", Boolean.toString(DRRelicRecoveryFile.delete()));
//                Log.i("DR-RR File Created", Boolean.toString(DRRelicRecoveryFile.createNewFile()));
//
//                FileOutputStream outputStream = new FileOutputStream(DRRelicRecoveryFile);
//                outputStream.write(getResources().getString(R.string.OfficialGame1).getBytes());
//                outputStream.flush();
//                outputStream.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }

//            File outputDir = getExternalFilesDir(null);
//            Log.i("DR-RR Output Deleted", Boolean.toString(outputDir.delete()));
//
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            intent.setData(Uri.fromFile(outputDir));
//            sendBroadcast(intent);
//        }
    }

    private void readStorage()
    {
        File gamesDir = new File(getFilesDir(), "Games");

//        try
//        {
//            File DRRelicRecoveryFile = new File(gamesDir, "OfficialGame1.DRSgame");
//            InputStream inputStream = new BufferedInputStream(new FileInputStream(DRRelicRecoveryFile));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuilder builder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null)
//            {
//                builder.append(line);
//            }
//            reader.close();
//            inputStream.close();
//
//            Log.i("File", builder.toString());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }
}
