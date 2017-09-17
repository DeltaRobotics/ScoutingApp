package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementRadioGroup extends matchElement {

    private ArrayList<String> elementsText;
    private int elementChecked;
    //-# = No Default

    private ArrayList<RadioButton> radioButtons;

    public elementRadioGroup(String title, ArrayList<String> elementsText, int elementChecked)
    {
        super(title);
        this.elementsText = elementsText;
        this.elementChecked = elementChecked;
    }

    public int getValue()
    {
        int n = 0;
        for(RadioButton radioButton: radioButtons)
        {
            if(radioButton.isChecked())
            {
                return n;
            }
            n++;
        }

        return -1;
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
}
