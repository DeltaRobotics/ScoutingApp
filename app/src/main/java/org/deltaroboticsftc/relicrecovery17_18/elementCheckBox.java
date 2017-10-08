package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementCheckBox extends matchElement {

    private ArrayList<String> elementsText;
    private ArrayList<Boolean> elementsChecked;

    private ArrayList<CheckBox> checkBoxs;

    public elementCheckBox(String title, JSONObject elementInfo)
    {
        super(title, "CheckBox");
        try
        {
            elementsText = new ArrayList<>();
            elementsChecked = new ArrayList<>();

            for(int x = 0; x < elementInfo.getInt("count"); x++)
            {
                elementsText.add(elementInfo.getString("box" + x));
                elementsChecked.add(elementInfo.getBoolean("box" + x + "Checked"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public LinearLayout getElement(Context context)
    {
        int checkBoxStyle = context.getResources().getIdentifier("checkBox", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout checkBoxLayout = new LinearLayout(context);
        checkBoxLayout.setLayoutParams(layoutParams);
        checkBoxLayout.setOrientation(LinearLayout.VERTICAL);

        checkBoxs = new ArrayList<>();

        int n = 0;
        for(String text: elementsText)
        {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setText(text);
            checkBox.setChecked(elementsChecked.get(n));
            if(Build.VERSION.SDK_INT >= 23)
            {
                checkBox.setTextAppearance(checkBoxStyle);
            }
            else
            {
                checkBox.setTextAppearance(context, checkBoxStyle);
            }

            checkBoxs.add(checkBox);
            checkBoxLayout.addView(checkBox);
            n++;
        }

        return super.buildElement(checkBoxLayout, context);
    }

    public JSONObject getValue()
    {
        JSONObject item = new JSONObject();

        try
        {
            item.put("itemType", elementType);
            item.put("title", elementTitle);
            item.put("count", elementsText.size());

            for(int x = 0;x < elementsText.size(); x++)
            {
                item.put("box" + x, elementsText.get(x));
                item.put("box" + x + "Checked", checkBoxs.get(x).isChecked());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return item;
    }
}
