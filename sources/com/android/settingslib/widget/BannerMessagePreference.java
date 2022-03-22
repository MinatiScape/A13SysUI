package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class BannerMessagePreference extends Preference {
    public static final boolean IS_AT_LEAST_S = BuildCompatUtils.isAtLeastS();
    public AttentionLevel mAttentionLevel;
    public String mSubtitle;
    public final ButtonInfo mPositiveButtonInfo = new ButtonInfo();
    public final ButtonInfo mNegativeButtonInfo = new ButtonInfo();
    public final DismissButtonInfo mDismissButtonInfo = new DismissButtonInfo();

    /* loaded from: classes.dex */
    public static class ButtonInfo {
        public Button mButton;
        public int mColor;

        public final void setUpButton() {
            this.mButton.setText((CharSequence) null);
            this.mButton.setOnClickListener(null);
            if (BannerMessagePreference.IS_AT_LEAST_S) {
                this.mButton.setTextColor(this.mColor);
            }
            if (!TextUtils.isEmpty(null)) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DismissButtonInfo {
        public ImageButton mButton;
    }

    /* loaded from: classes.dex */
    public enum AttentionLevel {
        HIGH(0, 2131099715, 2131099712),
        /* JADX INFO: Fake field, exist only in values array */
        MEDIUM(1, 2131099717, 2131099714),
        /* JADX INFO: Fake field, exist only in values array */
        LOW(2, 2131099716, 2131099713);
        
        private final int mAccentColorResId;
        private final int mAttrValue;
        private final int mBackgroundColorResId;

        AttentionLevel(int i, int i2, int i3) {
            this.mAttrValue = i;
            this.mBackgroundColorResId = i2;
            this.mAccentColorResId = i3;
        }

        public static AttentionLevel fromAttr(int i) {
            AttentionLevel[] values;
            for (AttentionLevel attentionLevel : values()) {
                if (attentionLevel.mAttrValue == i) {
                    return attentionLevel;
                }
            }
            throw new IllegalArgumentException();
        }

        public final int getAccentColorResId() {
            return this.mAccentColorResId;
        }

        public final int getBackgroundColorResId() {
            return this.mBackgroundColorResId;
        }
    }

    public BannerMessagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAttentionLevel = AttentionLevel.HIGH;
        if (this.mSelectable) {
            this.mSelectable = false;
            notifyChanged();
        }
        this.mLayoutResId = 2131624479;
        if (IS_AT_LEAST_S && attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BannerMessagePreference);
            this.mAttentionLevel = AttentionLevel.fromAttr(obtainStyledAttributes.getInt(0, 0));
            this.mSubtitle = obtainStyledAttributes.getString(1);
            obtainStyledAttributes.recycle();
        }
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        Context context = this.mContext;
        TextView textView = (TextView) preferenceViewHolder.findViewById(2131427554);
        CharSequence charSequence = this.mTitle;
        textView.setText(charSequence);
        int i2 = 8;
        if (charSequence == null) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        ((TextView) preferenceViewHolder.findViewById(2131427553)).setText(getSummary());
        this.mPositiveButtonInfo.mButton = (Button) preferenceViewHolder.findViewById(2131427551);
        this.mNegativeButtonInfo.mButton = (Button) preferenceViewHolder.findViewById(2131427550);
        Resources.Theme theme = context.getTheme();
        int color = context.getResources().getColor(this.mAttentionLevel.getAccentColorResId(), theme);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(2131427549);
        if (imageView != null) {
            Drawable icon = getIcon();
            if (icon == null) {
                icon = this.mContext.getDrawable(2131232330);
            }
            imageView.setImageDrawable(icon);
            imageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        if (IS_AT_LEAST_S) {
            int color2 = context.getResources().getColor(this.mAttentionLevel.getBackgroundColorResId(), theme);
            preferenceViewHolder.mDividerAllowedAbove = false;
            preferenceViewHolder.mDividerAllowedBelow = false;
            preferenceViewHolder.itemView.getBackground().setTint(color2);
            this.mPositiveButtonInfo.mColor = color;
            this.mNegativeButtonInfo.mColor = color;
            this.mDismissButtonInfo.mButton = (ImageButton) preferenceViewHolder.findViewById(2131427548);
            DismissButtonInfo dismissButtonInfo = this.mDismissButtonInfo;
            Objects.requireNonNull(dismissButtonInfo);
            dismissButtonInfo.mButton.setOnClickListener(null);
            dismissButtonInfo.mButton.setVisibility(8);
            TextView textView2 = (TextView) preferenceViewHolder.findViewById(2131427552);
            textView2.setText(this.mSubtitle);
            if (this.mSubtitle != null) {
                i2 = 0;
            }
            textView2.setVisibility(i2);
        } else {
            preferenceViewHolder.mDividerAllowedAbove = true;
            preferenceViewHolder.mDividerAllowedBelow = true;
        }
        this.mPositiveButtonInfo.setUpButton();
        this.mNegativeButtonInfo.setUpButton();
    }
}
