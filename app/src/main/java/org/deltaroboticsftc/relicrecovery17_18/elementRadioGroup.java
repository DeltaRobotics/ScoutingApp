package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementRadioGroup extends matchElement {

    private ArrayList<String> elementsText;
    private int elementChecked;
    //-# = No Default

    private ArrayList<RadioButton> radioButtons;

    public elementRadioGroup(String title, JSONObject elementInfo)
    {
        super(title, "RadioGroup");
        try
        {
            elementsText = new ArrayList<>();

            for(int x = 0; x < elementInfo.getInt("count"); x++)
            {
                elementsText.add(elementInfo.getString("radio" + x));
                elementChecked = elementInfo.getInt("default");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public LinearLayout getElement(Context context)
    {
        int radioGroupStyle = context.getResources().getIdentifier("radioGroup", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setLayoutParams(layoutParams);

        radioButtons = new ArrayList<>();

        for(String text: elementsText)
        {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(text);
            if(Build.VERSION.SDK_INT >= 23)
            {
                radioButton.setTextAppearance(radioGroupStyle);
            }
            else
            {
                radioButton.setTextAppearance(context, radioGroupStyle);
            }

            radioGroup.addView(radioButton);
            radioButtons.add(radioButton);
        }

        if(elementChecked >= 0  && elementChecked < radioButtons.size())
        {
            radioGroup.check(radioButtons.get(elementChecked).getId());
        }

        return super.buildElement(radioGroup, context);
    }

    public JSONObject getValue()
    {
        JSONObject item = new JSONObject();

        try
        {
            item.put("itemType", elementType);
            item.put("title", elementTitle);
            int x = 0;
            for(RadioButton radioButton: radioButtons)
            {
                if(radioButton.isChecked())
                {
                    item.put("value", x);
                    item.put("textValue", radioButton.getText());
                    return item;
                }
                x++;
            }
            item.put("value", -1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return item;
    }
}
