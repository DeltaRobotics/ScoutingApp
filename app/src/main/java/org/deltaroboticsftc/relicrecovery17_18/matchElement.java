package org.deltaroboticsftc.relicrecovery17_18;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.security.Policy;

/**
 * Created by Luke Poellet on 9/13/2017.
 */

public abstract class matchElement {

    protected String elementTitle = null;
    protected String elementType = null;

    protected matchElement(String title, String type)
    {
        elementTitle = title;
        elementType = type;
    }

    public abstract LinearLayout getElement(Context context);

    protected LinearLayout buildElement(View element, Context context)
    {
        int titleStyle = context.getResources().getIdentifier("subTitle", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView title = new TextView(context);
        title.setLayoutParams(layoutParams);
        title.setText(elementTitle);
        if(Build.VERSION.SDK_INT >= 23)
        {
            title.setTextAppearance(titleStyle);
        }
        else
        {
            title.setTextAppearance(context, titleStyle);
        }

        LinearLayout finalElement = new LinearLayout(context);
        finalElement.setLayoutParams(layoutParams);
        finalElement.setOrientation(LinearLayout.VERTICAL);
        finalElement.addView(title);
        finalElement.addView(element);

        return finalElement;
    }

    public String getElementTitle()
    {
        return elementTitle;
    }

    public String getElementType()
    {
        return elementType;
    }

}
