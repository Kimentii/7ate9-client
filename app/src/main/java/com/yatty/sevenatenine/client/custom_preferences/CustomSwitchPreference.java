package com.yatty.sevenatenine.client.custom_preferences;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomSwitchPreference extends SwitchPreference {

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleTextView = (TextView) view.findViewById(android.R.id.title);
        TextView summaryTextView = view.findViewById(android.R.id.summary);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/PermanentMarker.ttf");
        titleTextView.setTypeface(typeface);
        summaryTextView.setTypeface(typeface);
    }
}
