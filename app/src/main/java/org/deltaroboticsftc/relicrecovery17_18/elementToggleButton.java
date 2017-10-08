package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import org.json.JSONObject;

/**
 * Created by Luke Poellet on 9/13/2017.
 */

public class elementToggleButton extends matchElement
{

    private boolean elementDefaultToggle;
    private String elementToggledTrueText;
    private String elementToggledFalseText;

    private ToggleButton toggleButton;

    public elementToggleButton(String title, JSONObject elementInfo)
    {
        super(title, "ToggleButton");
        try
        {
            elementDefaultToggle = elementInfo.getBoolean("default");
            elementToggledTrueText = elementInfo.getString("trueText");
            elementToggledFalseText = elementInfo.getString("falseText");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public LinearLayout getElement(Context context)
    {
        int toggleButtonStyle = context.getResources().getIdentifier("toggleButton", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        toggleButton = new ToggleButton(context);
        toggleButton.setLayoutParams(layoutParams);
        toggleButton.setTextOff(this.elementToggledFalseText);
        toggleButton.setTextOn(this.elementToggledTrueText);
        toggleButton.setChecked(elementDefaultToggle);
        if(Build.VERSION.SDK_INT >= 23)
        {
            toggleButton.setTextAppearance(toggleButtonStyle);
        }
        else
        {
            toggleButton.setTextAppearance(context, toggleButtonStyle);
        }

        return super.buildElement(toggleButton, context);
    }

    public JSONObject getValue()
    {
        JSONObject item = new JSONObject();

        try
        {
            item.put("itemType", elementType);
            item.put("title", elementTitle);
            item.put("value", toggleButton.isChecked());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return item;
    }
}
