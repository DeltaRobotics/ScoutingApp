package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class matchBuilder
{
    private String game;
    private String gameTitle;
    private String gameYear;
    private String gameDescription;
    private String gameBy;
    private String mode;
    private LinearLayout AutonomousLayout;
    private ArrayList<matchElement> AutonomousElements;
    private LinearLayout TeleOpLayout;
    private ArrayList<matchElement> TeleOpElements;
    private LinearLayout EndGameLayout;
    private ArrayList<matchElement> EndGameElements;
    private LinearLayout ExtrasLayout;
    private ArrayList<matchElement> ExtrasElements;

    public matchBuilder(String game, Context context)
    {
        this.game = cut(game, "Game{", "}Game");

        gameTitle = cutInfo(this.game, "gametitle");
        gameYear = cutInfo(this.game, "gameyear");
        gameDescription = cutInfo(this.game, "gamedescription");
        gameBy = cutInfo(this.game, "creator");
        mode = cutInfo(this.game, "mode");

        AutonomousElements = new ArrayList<>();
        AutonomousLayout = buildLayout(cut(this.game, "Autonomous{", "}Autonomous"), context, AutonomousElements);

        TeleOpElements = new ArrayList<>();
        TeleOpLayout = buildLayout(cut(this.game, "Tele-Op{", "}Tele-Op"), context, TeleOpElements);

        EndGameElements = new ArrayList<>();
        EndGameLayout = buildLayout(cut(this.game, "EndGame{", "}EndGame"), context, EndGameElements);

        if(Boolean.parseBoolean(cutInfo(cut(this.game, "Extras{", "}Extras"), "include")))
        {
            ExtrasElements = new ArrayList<>();
            ExtrasLayout = buildLayout(cut(this.game, "Extras{", "}Extras"), context, ExtrasElements);
        }
        else
        {
            ExtrasLayout = null;
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

    public String getMode()
    {
        return mode;
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

    private String cut(String text, String start, String end)
    {
        return text.substring(text.indexOf(start), text.indexOf(end));
    }

    private String cutInfo(String text, String attribute)
    {
        String temp = text.substring(text.indexOf(attribute + "="));
        String temp2 = temp.substring(0, temp.indexOf(";"));
        return temp2.substring(temp2.indexOf("=") + 1);
    }

    private LinearLayout buildLayout(String text, Context context, ArrayList<matchElement> elements)
    {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        String temp = cutInfo(text, "itemcount");
        int itemCount = Integer.parseInt(temp.substring(temp.indexOf("=") + 1));

        for(int n = 1; n <= itemCount; n++)
        {
            String item = cut(text, "Item" + Integer.toString(n) + "{", "}Item" + Integer.toString(n));

            switch (cutInfo(item, "itemtype"))
            {
                case "checkBox":
                    String checkBoxTitle = cutInfo(item, "title");
                    int checkBoxCount = Integer.parseInt(cutInfo(item, "checkboxcount"));

                    ArrayList<String> checkBoxText = new ArrayList<>();
                    ArrayList<Boolean> checkBoxChecked = new ArrayList<>();

                    for(int x = 1; x <= checkBoxCount; x++)
                    {
                        checkBoxText.add(cutInfo(item, "checkbox" + Integer.toString(x)));
                        checkBoxChecked.add(Boolean.parseBoolean(cutInfo(item, "checkbox" + Integer.toString(x) + "checked")));
                    }

                    elementCheckBox checkBox = new elementCheckBox(checkBoxTitle, checkBoxText, checkBoxChecked);
                    elements.add(checkBox);
                    linearLayout.addView(checkBox.getElement(context));
                    break;

                case "counter":
                    String counterTitle = cutInfo(item, "title");
                    int defaultValue = Integer.parseInt(cutInfo(item, "default"));
                    int modifier = Integer.parseInt(cutInfo(item, "modifier"));
                    int minValue = Integer.parseInt(cutInfo(item, "minvalue"));
                    int maxValue = Integer.parseInt(cutInfo(item, "maxvalue"));

                    elementCounter counter = new elementCounter(counterTitle, defaultValue, modifier, minValue, maxValue);
                    elements.add(counter);
                    linearLayout.addView(counter.getElement(context));
                    break;

                case "radioGroup":
                    String radioGroupTitle = cutInfo(item, "title");
                    int radioCount = Integer.parseInt(cutInfo(item, "radiocount"));
                    int defaultSelect = Integer.parseInt(cutInfo(item, "defaultselect")) - 1;

                    ArrayList<String> radioGroupText = new ArrayList<>();

                    for(int x = 1; x <= radioCount; x++)
                    {
                        radioGroupText.add(cutInfo(item, "radio" + Integer.toString(x)));
                    }

                    elementRadioGroup radioGroup = new elementRadioGroup(radioGroupTitle, radioGroupText, defaultSelect);
                    elements.add(radioGroup);
                    linearLayout.addView(radioGroup.getElement(context));
                    break;

                case "textArea":
                    String textAreaTitle = cutInfo(item, "title");
                    int lines = Integer.parseInt(cutInfo(item, "lines"));

                    elementTextArea textArea = new elementTextArea(textAreaTitle, lines);
                    elements.add(textArea);
                    linearLayout.addView(textArea.getElement(context));
                    break;

                case "toggleButton":
                    String toggleButtonTitle = cutInfo(item, "title");
                    boolean defaultToggle = Boolean.parseBoolean(cutInfo(item, "default"));
                    String trueText = cutInfo(item, "truetext");
                    String falseText = cutInfo(item, "falsetext");

                    elementToggleButton toggleButton = new elementToggleButton(toggleButtonTitle, defaultToggle, trueText, falseText);
                    elements.add(toggleButton);
                    linearLayout.addView(toggleButton.getElement(context));
                    break;
            }
        }

        return linearLayout;
    }

    public String save(String teamNumber, String matchNumber, String allianceColor, Context context)
    {
        File savePath = new File(context.getExternalFilesDir(null), gameBy + "-" + gameTitle);
        File saveMatch;
        int loop = 0;
        do
        {
            if (loop > 0)
            {
                if(this.getMode().equals("Alliance"))
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
                if(this.getMode().equals("Alliance"))
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
        Log.i("saveMatch", saveMatch.getPath());

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("gametitle", this.getGameTitle());
            jsonObject.put("gameby", this.getGameBy());
            if (this.getMode().equals("Team"))
            {
                jsonObject.put("teamNumber", teamNumber);
            }
            jsonObject.put("matchNumber", matchNumber);
            jsonObject.put("allianceColor", allianceColor);

            for (int x = 0; x < 4; x++)
            {
                ArrayList<matchElement> elements = new ArrayList<>();
                String section = null;
                switch (x)
                {
                    case 0:
                        elements = AutonomousElements;
                        section = "Autonomous";
                        break;

                    case 1:
                        elements = TeleOpElements;
                        section = "TeleOpElements";
                        break;

                    case 2:
                        elements = EndGameElements;
                        section = "EndGame";
                        break;

                    case 3:
                        elements = ExtrasElements;
                        section = "Extras";
                        break;
                }

                for(matchElement element: elements)
                {
                    switch(element.getElementType())
                    {
                        case "Counter":
                            elementCounter elementCounter = (elementCounter) element;
                            jsonObject.put(section + ":" + element.getElementTitle(), elementCounter.getValue());
                            break;

                        case "RadioGroup":
                            elementRadioGroup elementRadioGroup = (elementRadioGroup) element;
                            jsonObject.put(section + ":" + element.getElementTitle(), elementRadioGroup.getValue());
                            break;

                        case "TextArea":
                            elementTextArea elementTextArea = (elementTextArea) element;
                            jsonObject.put(section + ":" + element.getElementTitle(), elementTextArea.getValue());
                            break;

                        case "ToggleButton":
                            elementToggleButton elementToggleButton = (elementToggleButton) element;
                            jsonObject.put(section + ":" + element.getElementTitle(), elementToggleButton.getValue());
                            break;

                        case "CheckBox":
                            elementCheckBox elementCheckBox = (elementCheckBox) element;
                            jsonObject.put(section + ":" + element.getElementTitle(), elementCheckBox.getValue());
                            break;
                    }
                }

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
