package org.deltaroboticsftc.relicrecovery17_18;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Luke Poellet on 9/17/2017.
 */

public class fragmentEditMatch extends Fragment
{

    private matchBuilder match;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_edit_match, container, false);

        SharedPreferences DRFTCScouting = rootView.getContext().getSharedPreferences("DRFTCScouting", 0);
        String game = rootView.getResources().getString(R.string.OfficialGame1);

        match = new matchBuilder(DRFTCScouting.getString("Game", game), rootView.getContext());

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

        EditText teamNumber = (EditText) rootView.findViewById(R.id.team_number);
        if(match.getMode().equals("Alliance"))
        {
            teamNumber.setVisibility(View.GONE);
        }
        else
        {
            teamNumber.setVisibility(View.VISIBLE);
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

        EditText teamNumber = (EditText) this.getActivity().findViewById(R.id.team_number);
        EditText matchNumber = (EditText) this.getActivity().findViewById(R.id.match_number);
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

        Bundle reviewBundle = new Bundle();
        reviewBundle.putString("matchPath", match.save(teamNumber.getText().toString(), matchNumber.getText().toString(), allianceColor, this.getContext()));

        Fragment fragment = new fragmentReview();
        fragment.setArguments(reviewBundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, fragment);
        transaction.commit();
    }

    public void clearOnClick()
    {
        Fragment fragment = new fragmentSettings();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_fragment, fragment);
        transaction.commit();
    }
}
