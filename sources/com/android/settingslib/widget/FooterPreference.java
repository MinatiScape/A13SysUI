package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class FooterPreference extends Preference {
    public View.OnClickListener mLearnMoreListener;
    public FooterLearnMoreSpan mLearnMoreSpan;
    public CharSequence mLearnMoreText;

    /* loaded from: classes.dex */
    public static class FooterLearnMoreSpan extends URLSpan {
        public final View.OnClickListener mClickListener;

        public FooterLearnMoreSpan(View.OnClickListener onClickListener) {
            super("");
            this.mClickListener = onClickListener;
        }

        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
        public final void onClick(View view) {
            View.OnClickListener onClickListener = this.mClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    public CharSequence getContentDescription() {
        return null;
    }

    public CharSequence getLearnMoreContentDescription() {
        return null;
    }

    public FooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130969104);
        this.mLayoutResId = 2131624394;
        if (getIcon() == null) {
            setIcon(AppCompatResources.getDrawable(this.mContext, 2131232645));
            this.mIconResId = 2131232645;
        }
        if (2147483646 != this.mOrder) {
            this.mOrder = 2147483646;
            Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
            if (onPreferenceChangeInternalListener != null) {
                PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) onPreferenceChangeInternalListener;
                preferenceGroupAdapter.mHandler.removeCallbacks(preferenceGroupAdapter.mSyncRunnable);
                preferenceGroupAdapter.mHandler.post(preferenceGroupAdapter.mSyncRunnable);
            }
        }
        if (TextUtils.isEmpty(this.mKey)) {
            setKey("footer_preference");
        }
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908310);
        textView.setMovementMethod(new LinkMovementMethod());
        textView.setClickable(false);
        textView.setLongClickable(false);
        textView.setFocusable(false);
        if (!TextUtils.isEmpty(null)) {
            textView.setContentDescription(null);
        }
        TextView textView2 = (TextView) preferenceViewHolder.itemView.findViewById(2131428850);
        if (textView2 == null || this.mLearnMoreListener == null) {
            textView2.setVisibility(8);
            return;
        }
        textView2.setVisibility(0);
        if (TextUtils.isEmpty(this.mLearnMoreText)) {
            this.mLearnMoreText = textView2.getText();
        } else {
            textView2.setText(this.mLearnMoreText);
        }
        SpannableString spannableString = new SpannableString(this.mLearnMoreText);
        FooterLearnMoreSpan footerLearnMoreSpan = this.mLearnMoreSpan;
        if (footerLearnMoreSpan != null) {
            spannableString.removeSpan(footerLearnMoreSpan);
        }
        FooterLearnMoreSpan footerLearnMoreSpan2 = new FooterLearnMoreSpan(this.mLearnMoreListener);
        this.mLearnMoreSpan = footerLearnMoreSpan2;
        spannableString.setSpan(footerLearnMoreSpan2, 0, spannableString.length(), 0);
        textView2.setText(spannableString);
        if (!TextUtils.isEmpty(null)) {
            textView2.setContentDescription(null);
        }
        textView2.setFocusable(false);
    }

    @Override // androidx.preference.Preference
    public final void setSummary$1() {
        setTitle(2131952669);
    }

    @Override // androidx.preference.Preference
    public final void setSummary(CharSequence charSequence) {
        setTitle(charSequence);
    }

    @Override // androidx.preference.Preference
    public final CharSequence getSummary() {
        return this.mTitle;
    }
}
