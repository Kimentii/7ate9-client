package com.yatty.sevenatenine.client.custom_preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yatty.sevenatenine.client.R;

public class CustomListPreference extends ListPreference {

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleTextView = (TextView) view.findViewById(android.R.id.title);
        //Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/PermanentMarker.ttf");
        //titleTextView.setTypeface(typeface);
        titleTextView.setTextColor(getContext().getResources().getColor(R.color.black));
    }
}
