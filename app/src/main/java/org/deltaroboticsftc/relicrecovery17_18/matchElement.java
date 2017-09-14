package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Luke Poellet on 9/13/2017.
 */

public abstract class matchElement {

    protected String elementTitle = null;
    protected int gameSection = 0;
    //gameSection = 0 : Section is Unknown
    //gameSection = 1 : Autonomous
    //gameSection = 2 : Tele-Op
    //gameSection = 3 : End Game
    //gameSection = 4 : Extra Items

    protected matchElement(String title)
    {
        elementTitle = title;
    }

    protected abstract LinearLayout getElement(int section, Context context);

    protected LinearLayout buildElement(View element)
    {
        return null;
    }

}
