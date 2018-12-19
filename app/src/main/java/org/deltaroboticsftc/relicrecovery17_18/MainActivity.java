package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        Bundle bundle = new Bundle();
        bundle.putBoolean("newMatch", true);

        Fragment fragment = new fragmentEditMatch();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, fragment);
        transaction.commit();
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

        // Hi Luke!! Tell me when you find this note.
        if(id == R.id.action_new_match)
        {
            findViewById(R.id.scroll_view).setScrollY(0);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);

            Bundle bundle = new Bundle();
            bundle.putBoolean("newMatch", true);

            Fragment fragment = new fragmentEditMatch();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.parent_fragment, fragment);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        Bundle bundle = new Bundle();

        if (id == R.id.nav_edit_match)
        {
            bundle.putBoolean("newMatch", true);
            fragment = new fragmentEditMatch();
        }
        else if(id == R.id.nav_saved_matches)
        {
            fragment = new fragmentSavedMatches();
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
            findViewById(R.id.scroll_view).setScrollY(0);

            fragment.setArguments(bundle);
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

        boolean displayAlert = true;

        try
        {
            SharedPreferences DRFTCScouting = getSharedPreferences("DRFTCScouting", 0);
            SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();

            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            Log.i("Version Code", Integer.toString(versionCode));
            Log.i("Version Name", versionName);


            if(DRFTCScouting.getBoolean("DefineStorage", true) || DRFTCScouting.getInt("VersionCode", 0) != versionCode)
            {
                DRFTCScoutingEditor.putBoolean("DefineStorage", false);
                DRFTCScoutingEditor.putInt("VersionCode", versionCode);
                DRFTCScoutingEditor.putString("VersionName", versionName);
                DRFTCScoutingEditor.putBoolean("DefaultAllianceColor", false);

                if(displayAlert == true)
                {
                    onLoadAlert();
                }

                InputStream inputStream = getResources().openRawResource(R.raw.relic_recovery_delta);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
                reader.close();
                inputStream.close();

                DRFTCScoutingEditor.putString("CurrentGame", builder.toString());

            }

            DRFTCScoutingEditor.apply();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void onLoadAlert()
    {
        String negativeButton = "Wait";
        String positiveButton = "Delete";
        String title = "Update Conflict";
        String message = "Update Version 1.3.1 changed the saved matches file structure.  Because of this we recommend deleting all saved match data, to prevent conflict.";
        int icon = R.drawable.ic_info_black_24dp;

        try
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Fragment fragment = new fragmentSettings();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.parent_fragment, fragment);
                    transaction.commit();

                }
            });
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setIcon(icon);
            alert.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void allianceColorChanger(View v)
    {
        ToggleButton allianceColor = (ToggleButton) findViewById(R.id.alliance_color);

        SharedPreferences DRFTCScouting = getSharedPreferences("DRFTCScouting", 0);
        SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();
        DRFTCScoutingEditor.putBoolean("DefaultAllianceColor", allianceColor.isChecked());
        DRFTCScoutingEditor.apply();
    }

    public void startingPositionChanger(View v)
    {
        ToggleButton startingPosition = (ToggleButton) findViewById(R.id.starting_position);

        SharedPreferences DRFTCScouting = getSharedPreferences("DRFTCScouting", 0);
        SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();
        DRFTCScoutingEditor.putBoolean("DefaultStartingPosition", startingPosition.isChecked());
        DRFTCScoutingEditor.apply();
    }
}
