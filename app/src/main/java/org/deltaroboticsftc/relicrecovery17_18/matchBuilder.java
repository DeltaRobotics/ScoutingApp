package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class matchBuilder
{
    private JSONObject game;
    private String gameTitle;
    private int gameYear;
    private String gameDescription;
    private String gameBy;
    private String gameMode;
    private LinearLayout AutonomousLayout;
    private ArrayList<matchElement> AutonomousElements;
    private LinearLayout TeleOpLayout;
    private ArrayList<matchElement> TeleOpElements;
    private LinearLayout EndGameLayout;
    private ArrayList<matchElement> EndGameElements;
    private LinearLayout ExtrasLayout;
    private ArrayList<matchElement> ExtrasElements;
    private boolean includeExtras;

    public matchBuilder(JSONObject game, Context context)
    {
        this.game = game;

        try
        {
            gameTitle = game.getString("gameTitle");
            gameYear = (int) game.get("gameYear");
            gameDescription = game.getString("gameDescription");
            gameBy = game.getString("gameBy");
            gameMode = game.getString("gameMode");

            AutonomousElements = new ArrayList<>();
            AutonomousLayout = buildLayout(game.getJSONObject("Autonomous"), context, AutonomousElements);

            TeleOpElements = new ArrayList<>();
            TeleOpLayout = buildLayout(game.getJSONObject("TeleOp"), context, TeleOpElements);

            EndGameElements = new ArrayList<>();
            EndGameLayout = buildLayout(game.getJSONObject("EndGame"), context, EndGameElements);

            if(game.getJSONObject("Extras").getBoolean("include"))
            {
                includeExtras = true;
                ExtrasElements = new ArrayList<>();
                ExtrasLayout = buildLayout(game.getJSONObject("Extras"), context, ExtrasElements);
            }
            else
            {
                ExtrasElements = new ArrayList<>();
                includeExtras = false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getGameTitle()
    {
        return gameTitle;
    }

    public String getGameBy()
    {
        return gameBy;
    }

    public String getGameMode()
    {
        return gameMode;
    }

    public LinearLayout getAutonomousLayout()
    {
        return AutonomousLayout;
    }

    public LinearLayout getTeleOpLayout()
    {
        return TeleOpLayout;
    }

    public LinearLayout getEndGameLayout()
    {
        return EndGameLayout;
    }

    public LinearLayout getExtrasLayout()
    {
        return ExtrasLayout;
    }

    private LinearLayout buildLayout(JSONObject jsonObject, Context context, ArrayList<matchElement> elements)
    {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        try
        {
            for(int n = 0; n < jsonObject.getInt("itemCount"); n++)
            {
                JSONObject item = jsonObject.getJSONObject("item" + n);
                switch (item.getString("itemType"))
                {
                    case "checkBox":
                        elementCheckBox checkBox = new elementCheckBox(item.getString("title"), item);
                        elements.add(checkBox);
                        linearLayout.addView(checkBox.getElement(context));
                        break;

                    case "counter":
                        elementCounter counter = new elementCounter(item.getString("title"), item);
                        elements.add(counter);
                        linearLayout.addView(counter.getElement(context));
                        break;

                    case "radioGroup":
                        elementRadioGroup radioGroup = new elementRadioGroup(item.getString("title"), item);
                        elements.add(radioGroup);
                        linearLayout.addView(radioGroup.getElement(context));
                        break;

                    case "textArea":
                        elementTextArea textArea = new elementTextArea(item.getString("title"), item);
                        elements.add(textArea);
                        linearLayout.addView(textArea.getElement(context));
                        break;

                    case "toggleButton":
                        elementToggleButton toggleButton = new elementToggleButton(item.getString("title"), item);
                        elements.add(toggleButton);
                        linearLayout.addView(toggleButton.getElement(context));
                        break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return linearLayout;
    }

    public String save(String teamNumber, String matchNumber, String allianceColor, Context context)
    {
        File savePath = new File(context.getExternalFilesDir(null), "MatchData" +  File.separator + gameBy + " - " + gameTitle);
        File saveMatch;
        int loop = 0;
        do
        {
            if (loop > 0)
            {
                if(this.getGameMode().equals("Alliance"))
                {
                    saveMatch = new File(savePath, "Match" + matchNumber + "-" + allianceColor + "-C" + loop + ".json");
                }
                else
                {
                    saveMatch = new File(savePath, "Match" + matchNumber + "-" + teamNumber + "-C" + loop + ".json");
                }
            }
            else
            {
                if(this.getGameMode().equals("Alliance"))
                {
                    saveMatch = new File(savePath, "Match" + matchNumber + "-" + allianceColor + ".json");
                }
                else
                {
                    saveMatch = new File(savePath, "Match" + matchNumber + "-" + teamNumber + ".json");
                }
            }
            loop++;
        } while(saveMatch.exists());

        savePath.mkdirs();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("gameTitle", this.getGameTitle());
            jsonObject.put("gameBy", this.getGameBy());
            jsonObject.put("gameMode", this.getGameMode());
            if (this.getGameMode().equals("Team"))
            {
                jsonObject.put("teamNumber", teamNumber);
            }
            else
            {
                jsonObject.put("teamNumber", "Team");
            }
            jsonObject.put("matchNumber", matchNumber);
            jsonObject.put("allianceColor", allianceColor);

            for (int x = 0; x < 4; x++)
            {
                ArrayList<matchElement> elements = new ArrayList<>();
                JSONObject jsonSection = new JSONObject();
                String section = null;
                switch (x)
                {
                    case 0:
                        elements = AutonomousElements;
                        section = "Autonomous";
                        break;

                    case 1:
                        elements = TeleOpElements;
                        section = "TeleOp";
                        break;

                    case 2:
                        elements = EndGameElements;
                        section = "EndGame";
                        break;

                    case 3:
                        elements = ExtrasElements;
                        section = "Extras";
                        jsonSection.put("include", includeExtras);
                        break;
                }

                jsonSection.put("itemCount", elements.size());
                int y = 0;
                for(matchElement element: elements)
                {
                    switch(element.getElementType())
                    {
                        case "CheckBox":
                            elementCheckBox elementCheckBox = (elementCheckBox) element;
                            jsonSection.put("item" + y, elementCheckBox.getValue());
                            break;

                        case "Counter":
                            elementCounter elementCounter = (elementCounter) element;
                            jsonSection.put("item" + y, elementCounter.getValue());
                            break;

                        case "RadioGroup":
                            elementRadioGroup elementRadioGroup = (elementRadioGroup) element;
                            jsonSection.put("item" + y, elementRadioGroup.getValue());
                            break;

                        case "TextArea":
                            elementTextArea elementTextArea = (elementTextArea) element;
                            jsonSection.put("item" + y, elementTextArea.getValue());
                            break;

                        case "ToggleButton":
                            elementToggleButton elementToggleButton = (elementToggleButton) element;
                            jsonSection.put("item" + y, elementToggleButton.getValue());
                            break;

                    }
                    y++;
                }

                jsonObject.put(section, jsonSection);
            }

            FileOutputStream outputStream = new FileOutputStream(saveMatch);
            outputStream.write(jsonObject.toString(1).getBytes());
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(saveMatch));
        context.sendBroadcast(intent);

        return saveMatch.getPath();
    }

}
