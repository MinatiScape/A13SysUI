package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class PartialConversationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    public static final /* synthetic */ int $r8$clinit = 0;
    public String mAppName;
    public int mAppUid;
    public ChannelEditorDialogController mChannelEditorDialogController;
    public String mDelegatePkg;
    public NotificationGuts mGutsContainer;
    public boolean mIsDeviceProvisioned;
    public boolean mIsNonBlockable;
    public NotificationChannel mNotificationChannel;
    public NotificationInfo.OnSettingsClickListener mOnSettingsClickListener;
    public String mPackageName;
    public Drawable mPkgIcon;
    public PackageManager mPm;
    public boolean mPressedApply;
    public StatusBarNotification mSbn;
    public Set<NotificationChannel> mUniqueChannelsInRow;
    public boolean mPresentingChannelEditorDialog = false;
    @VisibleForTesting
    public boolean mSkipPost = false;
    public KeyguardPatternViewController$$ExternalSyntheticLambda0 mOnDone = new KeyguardPatternViewController$$ExternalSyntheticLambda0(this, 1);

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final View getContentView() {
        return this;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean handleCloseControls(boolean z, boolean z2) {
        return false;
    }

    @VisibleForTesting
    public boolean isAnimating() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean needsFalsingProtection() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final void onFinishedClosing() {
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean willBeRemoved() {
        return false;
    }

    @Override // android.view.View
    public final boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    public PartialConversationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final int getActualHeight() {
        return getHeight();
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mGutsContainer != null && accessibilityEvent.getEventType() == 32) {
            NotificationGuts notificationGuts = this.mGutsContainer;
            Objects.requireNonNull(notificationGuts);
            if (notificationGuts.mExposed) {
                accessibilityEvent.getText().add(((LinearLayout) this).mContext.getString(2131952887, this.mAppName));
            } else {
                accessibilityEvent.getText().add(((LinearLayout) this).mContext.getString(2131952886, this.mAppName));
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean shouldBeSaved() {
        return this.mPressedApply;
    }
}
