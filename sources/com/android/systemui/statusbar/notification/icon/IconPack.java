package com.android.systemui.statusbar.notification.icon;

import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.StatusBarIconView;
/* loaded from: classes.dex */
public final class IconPack {
    public final StatusBarIconView mAodIcon;
    public final boolean mAreIconsAvailable;
    public final StatusBarIconView mCenteredIcon;
    public boolean mIsImportantConversation;
    public StatusBarIcon mPeopleAvatarDescriptor;
    public final StatusBarIconView mShelfIcon;
    public StatusBarIcon mSmallIconDescriptor;
    public final StatusBarIconView mStatusBarIcon;

    public IconPack(boolean z, StatusBarIconView statusBarIconView, StatusBarIconView statusBarIconView2, StatusBarIconView statusBarIconView3, StatusBarIconView statusBarIconView4, IconPack iconPack) {
        this.mAreIconsAvailable = z;
        this.mStatusBarIcon = statusBarIconView;
        this.mShelfIcon = statusBarIconView2;
        this.mCenteredIcon = statusBarIconView4;
        this.mAodIcon = statusBarIconView3;
        if (iconPack != null) {
            this.mIsImportantConversation = iconPack.mIsImportantConversation;
        }
    }
}
