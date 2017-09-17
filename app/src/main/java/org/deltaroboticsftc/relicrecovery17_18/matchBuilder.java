package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.util.Log;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class matchBuilder
{
    String game;

    public matchBuilder(String game, Context context)
    {
        if(game.equals("default"))
        {
            this.game = context.getResources().getString(R.string.game);
        }
        else
        {
            this.game = game;
        }
        Log.i("Game", this.game);
    }

}
