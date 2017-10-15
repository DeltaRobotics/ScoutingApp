package org.deltaroboticsftc.relicrecovery17_18;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/19/2017.
 */

public class fragmentChangeGame extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_change_game, container, false);

        LinearLayout selectionMenu = (LinearLayout) rootView.findViewById(R.id.selection_menu);

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> json = new ArrayList<>();
        for(int x = rootView.getResources().getInteger(R.integer.OfficialGameCount) - 1; x >= 0; x--)
        {
            titles.add(rootView.getResources().getString(rootView.getContext().getResources().getIdentifier(("Game" + x + "Title"), "string", rootView.getContext().getPackageName())));
            years.add(rootView.getResources().getString(rootView.getContext().getResources().getIdentifier(("Game" + x + "Year"), "string", rootView.getContext().getPackageName())));
            String file = rootView.getResources().getString(rootView.getContext().getResources().getIdentifier(("Game" + x + "File"), "string", rootView.getContext().getPackageName()));
            json.add(getGameJsonFromRaw(rootView.getResources().openRawResource(rootView.getContext().getResources().getIdentifier((file), "raw", rootView.getContext().getPackageName()))));
        }

        selectionMenu.addView(buildLayout("Delta Robotics", titles, years, json));

        return rootView;
    }

    private LinearLayout buildLayout(final String teamName, final ArrayList<String> gameTitle, final ArrayList<String> gameYear, final ArrayList<String> gameJson)
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
        DRTitle.setText(teamName);
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
        DRContentLinearLayout.setLayoutParams(layoutParams);
        DRContentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        for(int x = 0; x < gameTitle.size(); x++)
        {
            Button item = new Button(this.getContext());
            item.setLayoutParams(layoutParams);
            if (Build.VERSION.SDK_INT >= 23)
            {
                item.setTextAppearance(this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
            }
            else
            {
                item.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
            }
            item.setText(gameTitle.get(x) + "\n" + gameYear.get(x));
            final int y = x;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameDescription(gameJson.get(y), gameTitle.get(y), teamName);
                }
            });
            DRContentLinearLayout.addView(item);
        }

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

    private void gameDescription(final String jsonString, String title, String by)
    {
        try
        {
            JSONObject json = new JSONObject(jsonString);

            AlertDialog.Builder warning = new AlertDialog.Builder(this.getContext());
            warning.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            warning.setPositiveButton("Load", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    changeGame(jsonString);
                }
            });
            warning.setTitle(title + "\nBy: " + by);
            warning.setMessage(json.getString("gameDescription"));
            warning.setIcon(R.drawable.ic_file_download_black_24dp);
            warning.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void changeGame(String jsonString)
    {
        SharedPreferences DRFTCScouting = this.getContext().getSharedPreferences("DRFTCScouting", 1);
        SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();
        DRFTCScoutingEditor.putString("CurrentGame", jsonString);
        DRFTCScoutingEditor.apply();

        Bundle bundle = new Bundle();
        bundle.putBoolean("newMatch", true);

        Fragment fragment = new fragmentEditMatch();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, fragment);
        transaction.commit();
    }

    private String getGameJsonFromRaw(InputStream inputStream)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
            inputStream.close();

            return builder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "Failed";
    }

    private String getGameJson(String fileLocation)
    {
//        try
//        {
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
//            return builder.toString();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        return "Failed";
    }

}
