package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by Luke Poellet on 9/13/2017.
 */

public class elementToggleButton extends matchElement
{

    private boolean elementDefaultToggle;
    private String elementToggledTrueText;
    private String elementToggledFalseText;

    private ToggleButton toggleButton;

    public elementToggleButton(String title, boolean elementDefaultToggle, String elementToggledTrueText, String elementToggledFalseText)
    {
        super(title, "ToggleButton");
        this.elementDefaultToggle = elementDefaultToggle;
        this.elementToggledTrueText = elementToggledTrueText;
        this.elementToggledFalseText = elementToggledFalseText;
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

    public String getValue()
    {
        switch (Boolean.toString(toggleButton.isChecked()))
        {
            case "false":
                return toggleButton.getTextOff().toString();

            case "true":
                return toggleButton.getTextOn().toString();
        }

        return "No Value Found";
    }
}
