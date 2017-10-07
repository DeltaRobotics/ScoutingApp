package org.deltaroboticsftc.relicrecovery17_18;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Luke Poellet on 9/19/2017.
 */

public class fragmentChangeGame extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_change_game, container, false);

        LinearLayout selectionMenu = (LinearLayout) rootView.findViewById(R.id.selection_menu);
        selectionMenu.addView(buildLayout());

        return rootView;
    }

    private LinearLayout buildLayout()
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams dividerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)this.getResources().getDimension(R.dimen.dividerHeight));
        LinearLayout dividerLayout = new LinearLayout(this.getContext());
        dividerLayout.setLayoutParams(dividerLayoutParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            dividerLayout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary, null));
        }
        else
        {
            dividerLayout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        }

        LinearLayout DRLinearLayout = new LinearLayout(this.getContext());
        DRLinearLayout.setLayoutParams(layoutParams);
        DRLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout DRTitleLinearLayout = new LinearLayout(this.getContext());
        DRTitleLinearLayout.setLayoutParams(layoutParams);
        DRTitleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleViewParams.weight = 3;
        TextView DRTitle = new TextView(this.getContext());
        DRTitle.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            DRTitle.setTextAppearance(this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        else
        {
            DRTitle.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("subTitle", "style", this.getContext().getPackageName()));
        }
        DRTitle.setText("Games By: Delta Robotics");
        DRTitleLinearLayout.addView(DRTitle);

        final ImageView dropDownIcon = new ImageView(this.getContext());
        dropDownIcon.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 21)
        {
            dropDownIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp, null));
        }
        else
        {
            dropDownIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
        DRTitleLinearLayout.addView(dropDownIcon);

        final ImageView dropUpIcon = new ImageView(this.getContext());
        dropUpIcon.setLayoutParams(titleViewParams);
        if (Build.VERSION.SDK_INT >= 21)
        {
            dropUpIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp, null));
        }
        else
        {
            dropUpIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        }
        dropUpIcon.setVisibility(View.GONE);
        DRTitleLinearLayout.addView(dropUpIcon);

        final LinearLayout DRContentLinearLayout = new LinearLayout(this.getContext());
        DRLinearLayout.setLayoutParams(layoutParams);
        DRLinearLayout.setOrientation(LinearLayout.VERTICAL);

        Button testButton = new Button(this.getContext());
        testButton.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= 23)
        {
            testButton.setTextAppearance(this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
        }
        else
        {
            testButton.setTextAppearance(this.getContext(), this.getContext().getResources().getIdentifier("selectionButton", "style", this.getContext().getPackageName()));
        }
        testButton.setText("GAME TITLE\n20__-__");
        DRContentLinearLayout.addView(testButton);

        DRTitleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dropDownIcon.getVisibility() == View.GONE)
                {
                    dropDownIcon.setVisibility(View.VISIBLE);
                    dropUpIcon.setVisibility(View.GONE);
                    DRContentLinearLayout.setVisibility(View.GONE);
                }
                else
                {
                    dropDownIcon.setVisibility(View.GONE);
                    dropUpIcon.setVisibility(View.VISIBLE);
                    DRContentLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        DRContentLinearLayout.setVisibility(View.GONE);
        DRLinearLayout.addView(DRTitleLinearLayout);
        DRLinearLayout.addView(DRContentLinearLayout);

        linearLayout.addView(DRLinearLayout);
        linearLayout.addView(dividerLayout);

        return linearLayout;
    }

}
