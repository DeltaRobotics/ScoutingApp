package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.File;
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

    public void save(int location, Context context)
    {
        SharedPreferences DRFTCScouting = context.getSharedPreferences("DRFTCScouting", 1);
        SharedPreferences.Editor DRFTCScoutingEditor = DRFTCScouting.edit();

        Gson gson = new Gson();

    }

}
