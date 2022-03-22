package com.android.settingslib.wifi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.wifi.WifiConfiguration;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.util.Objects;
/* loaded from: classes.dex */
public class AccessPointPreference extends Preference {
    public AccessPoint mAccessPoint;
    public final int mBadgePadding;
    public final StateListDrawable mFrictionSld;
    public int mLevel;
    public final AnonymousClass1 mNotifyChanged;
    public TextView mTitleView;
    public static final int[] STATE_SECURED = {2130969766};
    public static final int[] STATE_METERED = {2130969769};
    public static final int[] WIFI_CONNECTION_STRENGTH = {2131951764, 2131951823, 2131951828, 2131951827, 2131951826};

    /* loaded from: classes.dex */
    public static class IconInjector {
    }

    /* loaded from: classes.dex */
    public static class UserBadgeCache {
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.settingslib.wifi.AccessPointPreference$1] */
    public AccessPointPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNotifyChanged = new Runnable() { // from class: com.android.settingslib.wifi.AccessPointPreference.1
            @Override // java.lang.Runnable
            public final void run() {
                AccessPointPreference.this.notifyChanged();
            }
        };
        this.mFrictionSld = null;
        this.mBadgePadding = 0;
    }

    public static CharSequence buildContentDescription(Context context, Preference preference, AccessPoint accessPoint) {
        String str;
        Objects.requireNonNull(preference);
        CharSequence charSequence = preference.mTitle;
        CharSequence summary = preference.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            charSequence = TextUtils.concat(charSequence, ",", summary);
        }
        int level = accessPoint.getLevel();
        if (level >= 0) {
            int[] iArr = WIFI_CONNECTION_STRENGTH;
            if (level < 5) {
                charSequence = TextUtils.concat(charSequence, ",", context.getString(iArr[level]));
            }
        }
        CharSequence[] charSequenceArr = new CharSequence[3];
        charSequenceArr[0] = charSequence;
        charSequenceArr[1] = ",";
        if (accessPoint.security == 0) {
            str = context.getString(2131951824);
        } else {
            str = context.getString(2131951825);
        }
        charSequenceArr[2] = str;
        return TextUtils.concat(charSequenceArr);
    }

    public static void setTitle(AccessPointPreference accessPointPreference, AccessPoint accessPoint) {
        accessPointPreference.setTitle(accessPoint.getTitle());
    }

    @Override // androidx.preference.Preference
    public final void notifyChanged() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            TextView textView = this.mTitleView;
            if (textView != null) {
                textView.post(this.mNotifyChanged);
                return;
            }
            return;
        }
        super.notifyChanged();
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        boolean z;
        super.onBindViewHolder(preferenceViewHolder);
        if (this.mAccessPoint != null) {
            Drawable icon = getIcon();
            if (icon != null) {
                icon.setLevel(this.mLevel);
            }
            TextView textView = (TextView) preferenceViewHolder.findViewById(16908310);
            this.mTitleView = textView;
            if (textView != null) {
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                this.mTitleView.setCompoundDrawablePadding(this.mBadgePadding);
            }
            preferenceViewHolder.itemView.setContentDescription(null);
            ImageView imageView = (ImageView) preferenceViewHolder.findViewById(2131427994);
            if (!(imageView == null || this.mFrictionSld == null)) {
                AccessPoint accessPoint = this.mAccessPoint;
                Objects.requireNonNull(accessPoint);
                if (accessPoint.security != 0) {
                    AccessPoint accessPoint2 = this.mAccessPoint;
                    Objects.requireNonNull(accessPoint2);
                    if (accessPoint2.security != 4) {
                        this.mFrictionSld.setState(STATE_SECURED);
                        imageView.setImageDrawable(this.mFrictionSld.getCurrent());
                    }
                }
                AccessPoint accessPoint3 = this.mAccessPoint;
                Objects.requireNonNull(accessPoint3);
                if (accessPoint3.mIsScoredNetworkMetered || WifiConfiguration.isMetered(accessPoint3.mConfig, accessPoint3.mInfo)) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    this.mFrictionSld.setState(STATE_METERED);
                }
                imageView.setImageDrawable(this.mFrictionSld.getCurrent());
            }
            preferenceViewHolder.findViewById(2131429134).setVisibility(4);
        }
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.settingslib.wifi.AccessPointPreference$1] */
    public AccessPointPreference(AccessPoint accessPoint, Context context, UserBadgeCache userBadgeCache, int i, boolean z, StateListDrawable stateListDrawable, int i2, IconInjector iconInjector) {
        super(context);
        this.mNotifyChanged = new Runnable() { // from class: com.android.settingslib.wifi.AccessPointPreference.1
            @Override // java.lang.Runnable
            public final void run() {
                AccessPointPreference.this.notifyChanged();
            }
        };
        this.mLayoutResId = 2131624384;
        this.mWidgetLayoutResId = 2131623983;
        this.mAccessPoint = accessPoint;
        Objects.requireNonNull(accessPoint);
        this.mLevel = i2;
        this.mFrictionSld = stateListDrawable;
        this.mBadgePadding = context.getResources().getDimensionPixelSize(2131167336);
    }
}
