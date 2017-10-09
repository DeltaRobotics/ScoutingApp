package org.deltaroboticsftc.relicrecovery17_18;

import android.os.Build;
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
        JSONObject matchInfo;

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
                teamNumber.setText("Team: " + matchInfo.getString("teamNumber"));
            }
            TextView matchNumber = (TextView) rootView.findViewById(R.id.review_match_number);
            matchNumber.setText("Match: " + matchInfo.getString("matchNumber"));
            TextView allianceColor = (TextView) rootView.findViewById(R.id.review_alliance_color);
            allianceColor.setText(matchInfo.getString("allianceColor") + " Alliance");

            LinearLayout Autonomous = (LinearLayout) rootView.findViewById(R.id.review_autonomous);
            Autonomous.addView(buildSection(matchInfo.getJSONObject("Autonomous")));
            LinearLayout TeleOp = (LinearLayout) rootView.findViewById(R.id.review_teleop);
            TeleOp.addView(buildSection(matchInfo.getJSONObject("TeleOp")));
            LinearLayout EndGame = (LinearLayout) rootView.findViewById(R.id.review_endgame);
            EndGame.addView(buildSection(matchInfo.getJSONObject("EndGame")));
            LinearLayout Extras = (LinearLayout) rootView.findViewById(R.id.review_extras);
            if(matchInfo.getJSONObject("Extras").getBoolean("include"))
            {
                Extras.addView(buildSection(matchInfo.getJSONObject("Extras")));
            }
            else
            {
                Extras.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return rootView;
    }

    public LinearLayout buildSection(JSONObject section)
    {
        int subTitleStyle = this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName());
        int subTextStyle = this.getContext().getResources().getIdentifier("text", "style", this.getContext().getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout sectionLayout = new LinearLayout(this.getContext());
        sectionLayout.setLayoutParams(layoutParams);
        sectionLayout.setOrientation(LinearLayout.VERTICAL);

        try
        {
            int itemCount = section.getInt("itemCount");

            for(int x = 0; x < itemCount; x++)
            {
                JSONObject item = section.getJSONObject("item" + x);
                TextView itemTitleView = new TextView(this.getContext());
                itemTitleView.setLayoutParams(layoutParams);
                if(Build.VERSION.SDK_INT >= 23)
                {
                    itemTitleView.setTextAppearance(subTitleStyle);
                }
                else
                {
                    itemTitleView.setTextAppearance(this.getContext(), subTitleStyle);
                }

                TextView itemView = new TextView(this.getContext());
                itemView.setLayoutParams(layoutParams);
                if(Build.VERSION.SDK_INT >= 23)
                {
                    itemView.setTextAppearance(subTextStyle);
                }
                else
                {
                    itemView.setTextAppearance(this.getContext(), subTextStyle);
                }

                switch(item.getString("itemType"))
                {
                    case "CheckBox":
                        itemTitleView.setText(item.getString("title"));
                        for(int y = 0; y < item.getInt("count"); y++)
                        {
                            itemView.setText(itemView.getText() + item.getString("box" + x) + ": " + item.getBoolean("box" + x + "Checked") + "\n");
                        }
                        break;

                    case "Counter":
                        itemTitleView.setText(item.getString("title"));
                        itemView.setText(Integer.toString(item.getInt("value")));
                        break;

                    case "RadioGroup":
                        itemTitleView.setText(item.getString("title"));
                        if(item.getInt("value") == -1)
                        {
                            itemView.setText("Nothing Selected");
                        }
                        else
                        {
                            itemView.setText(item.getString("textValue"));
                        }
                        break;

                    case "TextArea":
                        itemTitleView.setText(item.getString("title"));
                        itemView.setText(item.getString("value"));
                        break;

                    case "ToggleButton":
                        itemTitleView.setText(item.getString("title"));
                        itemView.setText(Boolean.toString(item.getBoolean("value")));
                        break;
                }

                sectionLayout.addView(itemTitleView);
                sectionLayout.addView(itemView);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sectionLayout;
    }

}
