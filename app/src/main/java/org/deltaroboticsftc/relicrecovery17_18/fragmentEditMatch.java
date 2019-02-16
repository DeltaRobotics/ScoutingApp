package org.deltaroboticsftc.relicrecovery17_18;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Luke Poellet on 9/17/2017.
 */

public class fragmentEditMatch extends Fragment
{
    public static final String STARTING_POSITION_DEPOT = "depot";
    public static final String STARTING_POSITION_CRATER = "crater";
    private static final String[] TEAM_NUMBERS = new String[] {
            "4156", "4175", "5143", "5975", "6121", "6189", "6661", "7332", "7618", "8650", "9052", "9925", "9974", "10012", "10214", "10435", "11142", "11222", "11316", "12754", "13186", "13497", "13532", "15523"
    };

    private AutoCompleteTextView mTeamNumberAutoCompleteView;
    private matchBuilder match;
    private ToggleButton startingPositionToggle;
    private TextView startingPositionTextBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_edit_match, container, false);
        //setting up auto complete
        mTeamNumberAutoCompleteView = (AutoCompleteTextView)  rootView.findViewById(R.id.team_number);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, TEAM_NUMBERS);
        mTeamNumberAutoCompleteView.setAdapter(adapter);
        mTeamNumberAutoCompleteView.setThreshold(1);

        startingPositionToggle = (ToggleButton) rootView.findViewById(R.id.starting_position);
        startingPositionTextBox = (TextView) rootView.findViewById(R.id.textView5);


        final Bundle bundle = getArguments();

        try
        {
            SharedPreferences DRFTCScouting = rootView.getContext().getSharedPreferences("DRFTCScouting", 0);

            JSONObject loadMatch;
            File loadFile;
            if(!bundle.getBoolean("newMatch"))
            {
                StringBuilder builder;
                loadFile = new File(bundle.getString("matchPath"));
                InputStream inputStream = new BufferedInputStream(new FileInputStream(loadFile));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
                reader.close();
                inputStream.close();

                loadMatch = new JSONObject(builder.toString());

                mTeamNumberAutoCompleteView.setText(loadMatch.getString("teamNumber"));
                mTeamNumberAutoCompleteView.setKeyListener(null);
                EditText loadMatchNumber = (EditText) rootView.findViewById(R.id.match_number);
                loadMatchNumber.setText(loadMatch.getString("matchNumber"));
                loadMatchNumber.setKeyListener(null);
                ToggleButton loadAllianceToggle = (ToggleButton) rootView.findViewById(R.id.alliance_color);
                loadAllianceToggle.setKeyListener(null);
                if(loadMatch.getString("allianceColor").equals("Blue"))
                {
                    loadAllianceToggle.setChecked(false);
                }
                else
                {
                    loadAllianceToggle.setChecked(true);
                }
                startingPositionToggle.setKeyListener(null);
                if(loadMatch.getString("startingPosition").equals(STARTING_POSITION_DEPOT))
                {
                    startingPositionToggle.setChecked(false);
                }
                else
                {
                    startingPositionToggle.setChecked(true);
                }
            }
            else
            {
                Boolean defaultAllianceColor = DRFTCScouting.getBoolean("DefaultAllianceColor", false);
                ToggleButton allianceColor = (ToggleButton) rootView.findViewById(R.id.alliance_color);
                allianceColor.setChecked(defaultAllianceColor);
                Boolean defaultStartingPosition = DRFTCScouting.getBoolean("DefaultStartingPosition", false);
                startingPositionToggle.setChecked(defaultStartingPosition);

                loadFile = new File(this.getContext().getExternalFilesDir(null), "");
                loadMatch = new JSONObject();
            }

            match = new matchBuilder(new JSONObject(DRFTCScouting.getString("CurrentGame", "Failed")), rootView.getContext(), bundle.getBoolean("newMatch"), loadMatch, loadFile);
            if (!match.getGameTitle().equals("Rover Ruckus")) {
                startingPositionToggle.setVisibility(View.GONE);
                startingPositionTextBox.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        TextView gameTitle = (TextView) rootView.findViewById(R.id.game_title);
        gameTitle.setText(match.getGameTitle());

        TextView gameBy = (TextView) rootView.findViewById(R.id.game_by);
        ImageView banner = (ImageView) rootView.findViewById(R.id.delta_banner);
        if(match.getGameBy().equals("DR-2015-Official"))
        {
            gameBy.setVisibility(View.GONE);
            banner.setVisibility(View.VISIBLE);
        }
        else
        {
            gameBy.setVisibility(View.VISIBLE);
            gameBy.setText(match.getGameTitle() + " By: " + match.getGameBy());
            banner.setVisibility(View.GONE);
        }

        if(match.getGameMode().equals("Alliance"))
        {
            mTeamNumberAutoCompleteView.setVisibility(View.GONE);
        }
        else
        {
            mTeamNumberAutoCompleteView.setVisibility(View.VISIBLE);
        }

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.game_autonomous);
        layout.addView(match.getAutonomousLayout());

        layout = (LinearLayout) rootView.findViewById(R.id.game_teleop);
        layout.addView(match.getTeleOpLayout());

        layout = (LinearLayout) rootView.findViewById(R.id.game_endgame);
        layout.addView(match.getEndGameLayout());

        layout = (LinearLayout) rootView.findViewById(R.id.game_extras);
        if(match.getExtrasLayout() == null)
        {
            layout.setVisibility(View.GONE);
        }
        else
        {
            layout.addView(match.getExtrasLayout());
        }

        Button saveButton = (Button) rootView.findViewById(R.id.game_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOnClick();
            }
        });

        final Button clearButton = (Button) rootView.findViewById(R.id.game_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearOnClick();
            }
        });


        return rootView;
    }

    public void saveOnClick()
    {
        this.getActivity().findViewById(R.id.scroll_view).setScrollY(0);

        EditText matchNumber = (EditText) this.getActivity().findViewById(R.id.match_number);

        final AlertDialog.Builder warning = new AlertDialog.Builder(this.getContext());
        warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        warning.setIcon(R.drawable.ic_error_outline_black_24dp);

        if(mTeamNumberAutoCompleteView.getText().toString().equals("") && mTeamNumberAutoCompleteView.getVisibility() == View.VISIBLE)
        {
            warning.setTitle("Alert");
            warning.setMessage("Please enter a team number before saving.");
            warning.show();
            mTeamNumberAutoCompleteView.requestFocus();
            return;
        }

        if(matchNumber.getText().toString().equals(""))
        {
            warning.setTitle("Alert");
            warning.setMessage("Please enter a match number before saving.");
            warning.show();
            matchNumber.requestFocus();
            return;
        }

        ToggleButton allianceToggle = (ToggleButton) this.getActivity().findViewById(R.id.alliance_color);
        String allianceColor;
        if (allianceToggle.isChecked())
        {
            allianceColor = "Red";
        }
        else
        {
            allianceColor = "Blue";
        }
        String startingPosition;
        if (startingPositionToggle.isChecked())
        {
            startingPosition = STARTING_POSITION_CRATER;
        }
        else
        {
            startingPosition = STARTING_POSITION_DEPOT;
        }

        Bundle reviewBundle = new Bundle();
        reviewBundle.putString("matchPath", match.save(mTeamNumberAutoCompleteView.getText().toString(), matchNumber.getText().toString(), allianceColor, startingPosition, this.getContext()));


        Fragment fragment = new fragmentReview();
        fragment.setArguments(reviewBundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, fragment);
        transaction.commit();
    }

    public void clearOnClick()
    {
        AlertDialog.Builder warning = new AlertDialog.Builder(this.getContext());
        warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().findViewById(R.id.scroll_view).setScrollY(0);

                Bundle bundle = new Bundle();
                bundle.putBoolean("newMatch", true);

                Fragment fragment = new fragmentEditMatch();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.parent_fragment, fragment);
                transaction.commit();
            }
        });
        warning.setTitle("WARNING");
        warning.setMessage("Are you sure you want to clear this match without saving?");
        warning.setIcon(R.drawable.ic_warning_black_24dp);
        warning.show();
    }
}
