package org.deltaroboticsftc.relicrecovery17_18;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Luke Poellet on 10/15/2017.
 */

public class fragmentSavedMatches extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_saved_matches, container, false);

        SharedPreferences DRFTCScouting = this.getContext().getSharedPreferences("DRFTCScouting", 1);
        try
        {
            JSONObject game = new JSONObject(DRFTCScouting.getString("CurrentGame", "Failed"));

            TextView gameTitle = (TextView) rootView.findViewById(R.id.saved_matches_game_title);
            gameTitle.setText(game.getString("gameTitle"));

            LinearLayout menu = (LinearLayout) rootView.findViewById(R.id.selection_menu_saved_matches);
            menu.addView(buildLayout(game.getString("gameTitle"), game.getString("gameBy")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return rootView;
    }

    private LinearLayout buildLayout(String gameBy, String gameTitle)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        File savePath = new File(this.getContext().getExternalFilesDir(null), "MatchData" +  File.separator + gameBy + " - " + gameTitle);
        //if(savePath.exists())
        if(false)
        {
            ArrayList<File> savedMatches = getMatches(savePath);

        }
        else
        {
            linearLayout.addView(savedMatchesError(layoutParams));
        }
        return linearLayout;
    }

    private TextView savedMatchesError(LinearLayout.LayoutParams layoutParams)
    {
        TextView savedMatchesNotFound = new TextView(this.getContext());
        savedMatchesNotFound.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            savedMatchesNotFound.setTextAppearance(this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        else
        {
            savedMatchesNotFound.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        savedMatchesNotFound.setText("No Saved Matches Found");
        return savedMatchesNotFound;
    }

    private ArrayList<File> getMatches(File file)
    {
        ArrayList<File> matchesFiles = new ArrayList<>();
        String[] childFile = file.list();
        for(int x = 0; x < childFile.length; x++)
        {
            if(!new File(file, childFile[x]).isDirectory())
            {
                if(checkFile(new File(file, childFile[x])))
                {
                    matchesFiles.add(new File(file, childFile[x]));
                }
            }
        }
        return matchesFiles;
    }

    private boolean checkFile(File file)
    {
        try
        {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
            inputStream.close();


            Log.i("Json", builder.toString());
            new JSONObject(builder.toString());
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }








/*
    private LinearLayout buildLayout()
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams dividerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)this.getResources().getDimension(R.dimen.dividerHeight));
        LinearLayout dividerLayout = new LinearLayout(this.getContext());
        dividerLayout.setLayoutParams(dividerLayoutParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            dividerLayout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary, null));
        }
        else
        {
            dividerLayout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        }

        LinearLayout DRLinearLayout = new LinearLayout(this.getContext());
        DRLinearLayout.setLayoutParams(layoutParams);
        DRLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout DRTitleLinearLayout = new LinearLayout(this.getContext());
        DRTitleLinearLayout.setLayoutParams(layoutParams);
        DRTitleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleViewParams.weight = 3;
        TextView DRTitle = new TextView(this.getContext());
        DRTitle.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            DRTitle.setTextAppearance(this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        else
        {
            DRTitle.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        DRTitle.setText("Team: 9925");
        DRTitleLinearLayout.addView(DRTitle);

        final ImageView dropDownIcon = new ImageView(this.getContext());
        dropDownIcon.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 21)
        {
            dropDownIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp, null));
        }
        else
        {
            dropDownIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
        DRTitleLinearLayout.addView(dropDownIcon);

        final ImageView dropUpIcon = new ImageView(this.getContext());
        dropUpIcon.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 21)
        {
            dropUpIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp, null));
        }
        else
        {
            dropUpIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        }
        dropUpIcon.setVisibility(View.GONE);
        DRTitleLinearLayout.addView(dropUpIcon);

        final LinearLayout DRContentLinearLayout = new LinearLayout(this.getContext());
        DRLinearLayout.setLayoutParams(layoutParams);
        DRLinearLayout.setOrientation(LinearLayout.VERTICAL);

        Button testButton = new Button(this.getContext());
        testButton.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            testButton.setTextAppearance(this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
        }
        else
        {
            testButton.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
        }
        testButton.setText("Match: __");
        DRContentLinearLayout.addView(testButton);

        DRTitleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dropDownIcon.getVisibility() == View.GONE)
                {
                    dropDownIcon.setVisibility(View.VISIBLE);
                    dropUpIcon.setVisibility(View.GONE);
                    DRContentLinearLayout.setVisibility(View.GONE);
                }
                else
                {
                    dropDownIcon.setVisibility(View.GONE);
                    dropUpIcon.setVisibility(View.VISIBLE);
                    DRContentLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        DRContentLinearLayout.setVisibility(View.GONE);
        DRLinearLayout.addView(DRTitleLinearLayout);
        DRLinearLayout.addView(DRContentLinearLayout);

        linearLayout.addView(DRLinearLayout);
        linearLayout.addView(dividerLayout);

        return linearLayout;
    }
*/

}
