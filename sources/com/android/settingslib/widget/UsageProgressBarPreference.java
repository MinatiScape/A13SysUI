package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class UsageProgressBarPreference extends Preference {
    public final Pattern mNumberPattern = Pattern.compile("[\\d]*[\\٫.,]?[\\d]+");
    public int mPercent = -1;

    public UsageProgressBarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutResId = 2131624404;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        String str;
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.mDividerAllowedAbove = false;
        preferenceViewHolder.mDividerAllowedBelow = false;
        TextView textView = (TextView) preferenceViewHolder.findViewById(2131429163);
        if (TextUtils.isEmpty(null)) {
            str = "";
        } else {
            Matcher matcher = this.mNumberPattern.matcher(null);
            if (matcher.find()) {
                SpannableString spannableString = new SpannableString(null);
                spannableString.setSpan(new AbsoluteSizeSpan(64, true), matcher.start(), matcher.end(), 33);
                str = spannableString;
            } else {
                str = null;
            }
        }
        textView.setText(str);
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(2131429081);
        TextView textView3 = (TextView) preferenceViewHolder.findViewById(2131427602);
        if (TextUtils.isEmpty(null)) {
            textView3.setVisibility(8);
        } else {
            textView3.setVisibility(0);
            textView3.setText((CharSequence) null);
        }
        ProgressBar progressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        if (this.mPercent < 0) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setProgress(this.mPercent);
        }
        FrameLayout frameLayout = (FrameLayout) preferenceViewHolder.findViewById(2131427792);
        frameLayout.removeAllViews();
        frameLayout.setVisibility(8);
    }
}
