package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import org.json.JSONObject;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementTextArea extends matchElement {

    private int elementLines;
    private String elementText;

    private EditText editText;

    public elementTextArea(String title, JSONObject elementInfo)
    {
        super(title, "TextArea");
        try
        {
            elementLines = elementInfo.getInt("lines");
            elementText = "";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load(JSONObject toLoad)
    {
        try
        {
            elementText = toLoad.getString("value");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        editText.setText(elementText);

        return super.buildElement(editText, context);
    }

    public JSONObject getValue()
    {
        JSONObject item = new JSONObject();

        try
        {
            item.put("itemType", elementType);
            item.put("title", elementTitle);
            item.put("value", editText.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return item;
    }
}
