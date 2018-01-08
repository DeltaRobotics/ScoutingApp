package org.deltaroboticsftc.relicrecovery17_18;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.File;

/**
 * Created by Luke Poellet on 9/19/2017.
 */

public class fragmentSettings extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final Button eventData = (Button) rootView.findViewById(R.id.settings_event_data);
        eventData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventData();
            }
        });

        final Button deleteMatches = (Button) rootView.findViewById(R.id.settings_delete_matches);
        deleteMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMatches();
            }
        });

        final Button deleteGames = (Button) rootView.findViewById(R.id.settings_delete_games);
        deleteGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGames();
            }
        });

        RadioGroup settingsDefaultColor = (RadioGroup) rootView.findViewById(R.id.settings_default_color);

        SharedPreferences DRFTCScouting = rootView.getContext().getSharedPreferences("DRFTCScouting", 0);

        if(DRFTCScouting.getBoolean("SettingsDefaultColorRed", true))
        {
            settingsDefaultColor.check(R.id.settings_default_color_red);
        }
        else
        {
            settingsDefaultColor.check(R.id.settings_default_color_blue);
        }

        return rootView;
    }

    private void eventData()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setTitle("Under Construction");
        alert.setIcon(R.drawable.ic_build_black_24dp);
        alert.setMessage("Sorry, but this feature has not yet been added.");
        alert.show();


    }

    private void deleteMatches()
    {
        final File matchData = new File(this.getContext().getExternalFilesDir(null), "MatchData");

        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Deleting Match Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final AlertDialog.Builder postDelete = new AlertDialog.Builder(this.getContext());
        postDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog.Builder warning = new AlertDialog.Builder(this.getContext());
        warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.show();

                boolean successful = false;
                if(matchData.exists())
                {
                    successful = deleteDir(matchData);
                }
                else
                {
                    successful = true;
                }

                if(successful)
                {
                    progressDialog.dismiss();
                    postDelete.setTitle("Delete Successful");
                    postDelete.setIcon(R.drawable.ic_done_black_24dp);
                    postDelete.setMessage("All match data deleted.");
                }
                else
                {
                    progressDialog.dismiss();
                    postDelete.setTitle("Delete Failed");
                    postDelete.setIcon(R.drawable.ic_error_outline_black_24dp);
                    postDelete.setMessage("Failed to delete match data.");
                }

                postDelete.show();
            }
        });
        warning.setTitle("WARNING");
        warning.setMessage("Are you sure you want to permanently delete all saved match data?");
        warning.setIcon(R.drawable.ic_warning_black_24dp);
        warning.show();
    }

    private void deleteGames()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Deleting Imported Games");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final AlertDialog.Builder postDelete = new AlertDialog.Builder(this.getContext());
        postDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog.Builder warning = new AlertDialog.Builder(this.getContext());
        warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.show();

                boolean successful;
                if(true)
                {
                    successful = false;
                }
                else
                {
                    successful = true;
                }

                if(successful)
                {
                    progressDialog.dismiss();
                    postDelete.setTitle("Delete Successful");
                    postDelete.setIcon(R.drawable.ic_done_black_24dp);
                    postDelete.setMessage("All imported games deleted");
                }
                else
                {
                    progressDialog.dismiss();
                    postDelete.setTitle("Delete Failed");
                    postDelete.setIcon(R.drawable.ic_error_outline_black_24dp);
                    postDelete.setMessage("Failed to delete imported games");
                }

                postDelete.show();
            }
        });
        warning.setTitle("WARNING");
        warning.setMessage("Are you sure you want to permanently delete all imported games?");
        warning.setIcon(R.drawable.ic_warning_black_24dp);
        warning.show();
    }

    private boolean deleteDir(File file)
    {
        boolean successful;
        String[] childFile = file.list();
        for(int x = 0; x < childFile.length; x++)
        {
            if(new File(file, childFile[x]).isDirectory())
            {
                successful = deleteDir(new File(file, childFile[x]));
                Log.i("DeleteDir", Boolean.toString(successful));
            }
            else
            {
                successful = new File(file, childFile[x]).delete();
                Log.i("DeleteFile", Boolean.toString(successful));
            }

            if(!successful)
            {
                return false;
            }
        }
        successful = file.delete();
        return successful;
    }

}
