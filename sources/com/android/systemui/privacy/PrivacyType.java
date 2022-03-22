package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public enum PrivacyType {
    TYPE_CAMERA(2131953023, 17303150, "android.permission-group.CAMERA", "camera"),
    TYPE_MICROPHONE(2131953025, 17303155, "android.permission-group.MICROPHONE", "microphone"),
    TYPE_LOCATION(2131953024, 17303154, "android.permission-group.LOCATION", "location");
    
    private final int iconId;
    private final String logName;
    private final int nameId;
    private final String permGroupName;

    PrivacyType(int i, int i2, String str, String str2) {
        this.nameId = i;
        this.iconId = i2;
        this.permGroupName = str;
        this.logName = str2;
    }

    public final Drawable getIcon(Context context) {
        return context.getResources().getDrawable(this.iconId, context.getTheme());
    }

    public final String getName(Context context) {
        return context.getResources().getString(this.nameId);
    }

    public final int getIconId() {
        return this.iconId;
    }

    public final String getLogName() {
        return this.logName;
    }

    public final int getNameId() {
        return this.nameId;
    }

    public final String getPermGroupName() {
        return this.permGroupName;
    }
}
