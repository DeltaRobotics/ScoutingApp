package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementCheckBox extends matchElement {

    private ArrayList<String> elementsText;
    private ArrayList<Boolean> elementsChecked;

    private ArrayList<CheckBox> checkBoxs;

    public elementCheckBox(String title, ArrayList<String> elementsText, ArrayList<Boolean> elementsChecked)
    {
        super(title);
        this.elementsText = elementsText;
        this.elementsChecked = elementsChecked;
    }

    public String getText()
    {
        return null;
    }

    public boolean getValue(int index)
    {
        return checkBoxs.get(index).isChecked();
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
}
