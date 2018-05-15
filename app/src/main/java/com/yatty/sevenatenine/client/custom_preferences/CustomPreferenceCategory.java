package com.yatty.sevenatenine.client.custom_preferences;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class CustomPreferenceCategory extends PreferenceCategory {

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleTextView = (TextView) view.findViewById(android.R.id.title);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/PermanentMarker.ttf");
        titleTextView.setTypeface(typeface);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }
}
