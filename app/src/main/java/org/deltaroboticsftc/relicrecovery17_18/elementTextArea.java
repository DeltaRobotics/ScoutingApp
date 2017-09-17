package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementTextArea extends matchElement {

    private int elementLines;

    private EditText editText;

    public elementTextArea(String title, int elementLines)
    {
        super(title);
        this.elementLines = elementLines;
    }

    public String getValue()
    {
        return editText.getText().toString();
    }

    public LinearLayout getElement(Context context)
    {
        int editTextStyle = context.getResources().getIdentifier("editText", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editText = new EditText(context);
        editText.setLayoutParams(layoutParams);
        editText.setLines(elementLines);
        if(Build.VERSION.SDK_INT >= 23)
        {
            editText.setTextAppearance(editTextStyle);
        }
        else
        {
            editText.setTextAppearance(context, editTextStyle);
        }

        return super.buildElement(editText, context);
    }
}
