package org.deltaroboticsftc.scoutingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
    private int gameVersion;
    private LinearLayout AutonomousLayout;
    private ArrayList<matchElement> AutonomousElements;
    private LinearLayout TeleOpLayout;
    private ArrayList<matchElement> TeleOpElements;
    private LinearLayout EndGameLayout;
    private ArrayList<matchElement> EndGameElements;
    private LinearLayout PenaltiesLayout;
    private ArrayList<matchElement> PenaltiesElements;
    private LinearLayout ExtrasLayout;
    private ArrayList<matchElement> ExtrasElements;
    private boolean includeExtras;
    private boolean newMatch;
    private File replaceMatch;

    public matchBuilder(JSONObject game, Context context, boolean newMatchPass, JSONObject loadMatch, File replaceMatchPass)
    {
        this.game = game;

        try
        {
            gameTitle = game.getString("gameTitle");
            gameYear = (int) game.get("gameYear");
            gameDescription = game.getString("gameDescription");
            gameBy = game.getString("gameBy");
            gameMode = game.getString("gameMode");
            gameVersion = game.getInt("version");
            newMatch = newMatchPass;
            replaceMatch = replaceMatchPass;

            JSONObject loadAutonomous = new JSONObject();
            JSONObject loadTeleOp = new JSONObject();
            JSONObject loadEndGame = new JSONObject();
            JSONObject loadPenalties = new JSONObject();
            JSONObject loadExtras = new JSONObject();

            if(!newMatch && (!gameTitle.equals(loadMatch.getString("gameTitle")) || !gameBy.equals(loadMatch.getString("gameBy")) || !
                    gameMode.equals(loadMatch.getString("gameMode"))))
            {
                newMatch = true;

                AlertDialog.Builder warning = new AlertDialog.Builder(context);
                warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                warning.setTitle("ERROR");
                warning.setMessage("Match to load does not match the format of the currently loaded game.");
                warning.setIcon(R.drawable.ic_error_outline_black_24dp);
                warning.show();
            }
            else if (!newMatch)
            {
                loadAutonomous = loadMatch.getJSONObject("Autonomous");
                loadTeleOp = loadMatch.getJSONObject("TeleOp");
                loadEndGame = loadMatch.getJSONObject("EndGame");
                loadPenalties = loadMatch.getJSONObject("Penalties");
                loadExtras = loadMatch.getJSONObject("Extras");
            }

            AutonomousElements = new ArrayList<>();
            AutonomousLayout = buildLayout(game.getJSONObject("Autonomous"), context, AutonomousElements, newMatch, loadAutonomous);

            TeleOpElements = new ArrayList<>();
            TeleOpLayout = buildLayout(game.getJSONObject("TeleOp"), context, TeleOpElements, newMatch, loadTeleOp);

            EndGameElements = new ArrayList<>();
            EndGameLayout = buildLayout(game.getJSONObject("EndGame"), context, EndGameElements, newMatch, loadEndGame);

            PenaltiesElements = new ArrayList<>();
            PenaltiesLayout = buildLayout(game.getJSONObject("Penalties"), context, PenaltiesElements, newMatch, loadPenalties);

            if(game.getJSONObject("Extras").getBoolean("include"))
            {
                includeExtras = true;
                ExtrasElements = new ArrayList<>();
                ExtrasLayout = buildLayout(game.getJSONObject("Extras"), context, ExtrasElements, newMatch, loadExtras);
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

    public LinearLayout getPenaltiesLayout() {
        return PenaltiesLayout;
    }

    public LinearLayout getExtrasLayout()
    {
        return ExtrasLayout;
    }

    private LinearLayout buildLayout(JSONObject section, Context context, ArrayList<matchElement> elements, boolean newMatch, JSONObject loadSection)
    {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        try
        {
            for(int n = 0; n < section.getInt("itemCount"); n++)
            {
                JSONObject item = section.getJSONObject("item" + n);

                JSONObject loadItem =  new JSONObject();
                if(!newMatch)
                {
                    loadItem = loadSection.getJSONObject("item" + n);
                }

                switch (item.getString("itemType"))
                {
                    case "checkBox":
                        elementCheckBox checkBox = new elementCheckBox(item.getString("title"), item);
                        if(!newMatch)
                        {
                            if(loadItem.getString("itemType").equals("CheckBox"))
                            {
                                checkBox.load(loadItem);
                            }
                        }
                        elements.add(checkBox);
                        linearLayout.addView(checkBox.getElement(context));
                        break;

                    case "counter":
                        elementCounter counter = new elementCounter(item.getString("title"), item);
                        if(!newMatch)
                        {
                            if(loadItem.getString("itemType").equals("Counter"))
                            {
                                counter.load(loadItem);
                            }
                        }
                        elements.add(counter);
                        linearLayout.addView(counter.getElement(context));
                        break;

                    case "radioGroup":
                        elementRadioGroup radioGroup = new elementRadioGroup(item.getString("title"), item);
                        if(!newMatch)
                        {
                            if(loadItem.getString("itemType").equals("RadioGroup"))
                            {
                                radioGroup.load(loadItem);
                            }
                        }
                        elements.add(radioGroup);
                        linearLayout.addView(radioGroup.getElement(context));
                        break;

                    case "textArea":
                        elementTextArea textArea = new elementTextArea(item.getString("title"), item);
                        if(!newMatch)
                        {
                            if(loadItem.getString("itemType").equals("TextArea"))
                            {
                                textArea.load(loadItem);
                            }
                        }
                        elements.add(textArea);
                        linearLayout.addView(textArea.getElement(context));
                        break;

                    case "toggleButton":
                        elementToggleButton toggleButton = new elementToggleButton(item.getString("title"), item);
                        if(!newMatch)
                        {
                            if(loadItem.getString("itemType").equals("ToggleButton"))
                            {
                                toggleButton.load(loadItem);
                            }
                        }
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

    public String save(String teamNumber, String matchNumber, String allianceColor, String startingPosition, Context context)
    {
        File savePath = new File(context.getExternalFilesDir(null), "MatchData" +  File.separator + gameBy + " - " + gameTitle + File.separator + "Version" + gameVersion);
        Log.i("SavePath-Builder", savePath.toString());
        File saveMatch;

        if(!newMatch)
        {
            saveMatch = replaceMatch;
            Log.i("Delete", Boolean.toString(saveMatch.delete()));
        }
        else
        {
            int loop = 0;
            do
            {
                if (loop > 0)
                {
                    if(this.getGameMode().equals("Alliance"))
                    {
                        saveMatch = new File(savePath, allianceColor + "-Match" + matchNumber + "-C" + loop + ".json");
                    }
                    else
                    {
                        saveMatch = new File(savePath, teamNumber + "-Match" + matchNumber + "-C" + loop + ".json");
                    }
                }
                else
                {
                    if(this.getGameMode().equals("Alliance"))
                    {
                        saveMatch = new File(savePath, allianceColor + "-Match" + matchNumber + ".json");
                    }
                    else
                    {
                        saveMatch = new File(savePath, teamNumber + "-Match" + matchNumber + ".json");
                    }
                }
                loop++;
            } while(saveMatch.exists());
        }

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
            jsonObject.put("startingPosition", startingPosition);
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
