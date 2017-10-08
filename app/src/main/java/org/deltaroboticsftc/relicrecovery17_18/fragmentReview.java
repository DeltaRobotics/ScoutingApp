package org.deltaroboticsftc.relicrecovery17_18;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Luke Poellet on 9/19/2017.
 */

public class fragmentReview extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_review_match, container, false);

        Bundle reviewBundle = getArguments();

        StringBuilder AutonomousInfo = new StringBuilder();
        StringBuilder TeleOpInfo = new StringBuilder();
        StringBuilder EndGameInfo = new StringBuilder();
        StringBuilder ExtraInfo = new StringBuilder();
        JSONObject matchInfo = new JSONObject();

        try
        {
            File match = new File(reviewBundle.getString("matchPath"));
            InputStream inputStream = new BufferedInputStream(new FileInputStream(match));
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
            matchInfo = new JSONObject(builder.toString());

            TextView gameTitle = (TextView) rootView.findViewById(R.id.review_game_title);
            gameTitle.setText(matchInfo.getString("gameTitle"));
            TextView teamNumber = (TextView) rootView.findViewById(R.id.review_team_number);
            if (matchInfo.getString("teamNumber").equals("Team"))
            {
                teamNumber.setVisibility(View.GONE);
            }
            else
            {
                teamNumber.setText(matchInfo.getString("teamNumber"));
            }
            TextView matchNumber = (TextView) rootView.findViewById(R.id.review_match_number);
            matchNumber.setText(matchInfo.getString("matchNumber"));
            TextView allianceColor = (TextView) rootView.findViewById(R.id.review_alliance_color);
            allianceColor.setText(matchInfo.getString("allianceColor"));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return rootView;
    }

}
