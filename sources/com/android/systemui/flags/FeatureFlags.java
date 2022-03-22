package com.android.systemui.flags;
/* compiled from: FeatureFlags.kt */
/* loaded from: classes.dex */
public interface FeatureFlags extends FlagListenable {
    boolean isEnabled(BooleanFlag booleanFlag);

    boolean isEnabled(ResourceBooleanFlag resourceBooleanFlag);
}
