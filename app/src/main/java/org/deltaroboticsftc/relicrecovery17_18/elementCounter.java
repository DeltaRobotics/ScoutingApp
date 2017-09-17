package org.deltaroboticsftc.relicrecovery17_18;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Luke Poellet on 9/16/2017.
 */

public class elementCounter extends matchElement {

    private int elementDefault;
    private int elementModifier;
    private int elementMinValue;
    private int elementMaxValue;


    private int elementCurrentValue;

    private TextView textView;

    public elementCounter(String title, int elementDefault, int elementModifier, int elementMinValue, int elementMaxValue)
    {
        super(title);
        this.elementDefault = elementDefault;
        this.elementModifier = elementModifier;
        this.elementMinValue = elementMinValue;
        this.elementMaxValue = elementMaxValue;
        if(elementDefault < elementMinValue)
        {
            elementDefault = elementMinValue;
        }
        else if(elementDefault > elementMaxValue)
        {
            elementDefault = elementMaxValue;
        }
        this.elementCurrentValue = elementDefault;
    }

    public int getValue()
    {
        return Integer.parseInt(textView.getText().toString());
    }

    public LinearLayout getElement(Context context)
    {
        int counterNumberStyle = context.getResources().getIdentifier("counterNumber", "style", context.getPackageName());
        int counterButtonStyle = context.getResources().getIdentifier("counterButton", "style", context.getPackageName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.weight = 1;

        LinearLayout counterLayout = new LinearLayout(context);
        counterLayout.setLayoutParams(layoutParams);
        counterLayout.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(Integer.toString(elementDefault));
        if (Build.VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(counterNumberStyle);
        }
        else
        {
            textView.setTextAppearance(context, counterNumberStyle);
        }
        counterLayout.addView(textView);

        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setLayoutParams(layoutParams);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button removeModifier = new Button(context);
        removeModifier.setLayoutParams(buttonLayoutParams);
        removeModifier.setText("-" + Integer.toString(elementModifier));
        if (Build.VERSION.SDK_INT >= 23) {
            removeModifier.setTextAppearance(counterButtonStyle);
        }
        else
        {
            removeModifier.setTextAppearance(context, counterButtonStyle);
        }
        removeModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModifier(v);
            }
        });
        buttonsLayout.addView(removeModifier);

        Button addModifier = new Button(context);
        addModifier.setLayoutParams(buttonLayoutParams);
        addModifier.setText("+" + Integer.toString(elementModifier));
        if (Build.VERSION.SDK_INT >= 23) {
            addModifier.setTextAppearance(counterButtonStyle);
        }
        else
        {
            addModifier.setTextAppearance(context, counterButtonStyle);
        }
        addModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addModifier(v);
            }
        });
        buttonsLayout.addView(addModifier);

        counterLayout.addView(buttonsLayout);

        return super.buildElement(counterLayout, context);
    }

    private void removeModifier(View v)
    {
        elementCurrentValue = elementCurrentValue - elementModifier;
        if(elementCurrentValue < elementMinValue)
        {
            elementCurrentValue = elementMinValue;
        }
        else if(elementCurrentValue > elementMaxValue)
        {
            elementCurrentValue = elementMaxValue;
        }
        textView.setText(Integer.toString(elementCurrentValue));
    }

    private void addModifier(View v)
    {
        elementCurrentValue = elementCurrentValue + elementModifier;
        if(elementCurrentValue < elementMinValue)
        {
            elementCurrentValue = elementMinValue;
        }
        else if(elementCurrentValue > elementMaxValue)
        {
            elementCurrentValue = elementMaxValue;
        }
        textView.setText(Integer.toString(elementCurrentValue));
    }
}
