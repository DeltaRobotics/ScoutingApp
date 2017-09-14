package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by Luke Poellet on 9/13/2017.
 */

public class elementToggleButton extends matchElement {

    private boolean elementDefaultToggle = false;
    private String elementToggledTrueText = "Checked";
    private String elementToggledFalseText = "Not Checked";

    public elementToggleButton(String title, boolean elementDefaultToggle, String elementToggledTrueText, String elementToggledFalseText)
    {
        super(title);
        this.elementDefaultToggle = elementDefaultToggle;
        this.elementToggledTrueText = elementToggledTrueText;
        this.elementToggledFalseText = elementToggledFalseText;
    }

    protected void setElementTitle(String title)
    {
        super.elementTitle = title;
    }

    protected LinearLayout getElement(int section, Context context)
    {
        ToggleButton toggleButton = new ToggleButton(context);
        toggleButton.setTextOff(this.elementToggledFalseText);
        toggleButton.setTextOn(this.elementToggledTrueText);

        super.buildElement(toggleButton);
        return null;
    }
}
