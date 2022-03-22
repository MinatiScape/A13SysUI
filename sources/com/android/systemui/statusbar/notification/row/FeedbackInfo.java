package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda2;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class FeedbackInfo extends LinearLayout implements NotificationGuts.GutsContent {
    public static final /* synthetic */ int $r8$clinit = 0;
    public String mAppName;
    public NotificationEntry mEntry;
    public ExpandableNotificationRow mExpandableNotificationRow;
    public AssistantFeedbackController mFeedbackController;
    public NotificationGuts mGutsContainer;
    public NotificationMenuRowPlugin mMenuRowPlugin;
    public NotificationGutsManager mNotificationGutsManager;
    public String mPkg;
    public PackageManager mPm;
    public NotificationListenerService.Ranking mRanking;
    public IStatusBarService mStatusBarService;

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final View getContentView() {
        return this;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean handleCloseControls(boolean z, boolean z2) {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean needsFalsingProtection() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean shouldBeSaved() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean willBeRemoved() {
        return false;
    }

    public final void handleFeedback(boolean z) {
        int i;
        Bundle bundle = new Bundle();
        if (z) {
            i = 1;
        } else {
            i = -1;
        }
        bundle.putInt("feedback.rating", i);
        AssistantFeedbackController assistantFeedbackController = this.mFeedbackController;
        Objects.requireNonNull(assistantFeedbackController);
        if (assistantFeedbackController.mFeedbackEnabled) {
            try {
                this.mStatusBarService.onNotificationFeedbackReceived(this.mRanking.getKey(), bundle);
            } catch (RemoteException unused) {
            }
        }
    }

    public static void $r8$lambda$v7f9kE2ar9FL2Q9Wqe6UnRy2T6A(FeedbackInfo feedbackInfo, View view) {
        Objects.requireNonNull(feedbackInfo);
        ExpandableNotificationRow expandableNotificationRow = feedbackInfo.mExpandableNotificationRow;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationMenuRowPlugin notificationMenuRowPlugin = expandableNotificationRow.mMenuRow;
        feedbackInfo.mMenuRowPlugin = notificationMenuRowPlugin;
        NotificationMenuRowPlugin.MenuItem menuItem = null;
        if (notificationMenuRowPlugin != null) {
            menuItem = notificationMenuRowPlugin.getLongpressMenuItem(((LinearLayout) feedbackInfo).mContext);
        }
        feedbackInfo.mGutsContainer.closeControls(view, false);
        feedbackInfo.mNotificationGutsManager.openGuts(feedbackInfo.mExpandableNotificationRow, 0, 0, menuItem);
        feedbackInfo.handleFeedback(false);
    }

    public final void bindGuts(PackageManager packageManager, StatusBarNotification statusBarNotification, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, AssistantFeedbackController assistantFeedbackController) {
        Drawable drawable;
        this.mPkg = statusBarNotification.getPackageName();
        this.mPm = packageManager;
        this.mEntry = notificationEntry;
        this.mExpandableNotificationRow = expandableNotificationRow;
        Objects.requireNonNull(notificationEntry);
        this.mRanking = notificationEntry.mRanking;
        this.mFeedbackController = assistantFeedbackController;
        this.mAppName = this.mPkg;
        this.mStatusBarService = (IStatusBarService) Dependency.get(IStatusBarService.class);
        this.mNotificationGutsManager = (NotificationGutsManager) Dependency.get(NotificationGutsManager.class);
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPkg, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf(this.mPm.getApplicationLabel(applicationInfo));
                drawable = this.mPm.getApplicationIcon(applicationInfo);
            } else {
                drawable = null;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            drawable = this.mPm.getDefaultActivityIcon();
        }
        ((ImageView) findViewById(2131428592)).setImageDrawable(drawable);
        ((TextView) findViewById(2131428593)).setText(this.mAppName);
        TextView textView = (TextView) findViewById(2131428632);
        TextView textView2 = (TextView) findViewById(2131429289);
        TextView textView3 = (TextView) findViewById(2131428496);
        textView2.setVisibility(0);
        textView3.setVisibility(0);
        textView2.setOnClickListener(new NavigationBar$$ExternalSyntheticLambda2(this, 1));
        textView3.setOnClickListener(new ShortcutBarView$$ExternalSyntheticLambda0(this, 1));
        StringBuilder sb = new StringBuilder();
        int feedbackStatus = this.mFeedbackController.getFeedbackStatus(this.mEntry);
        if (feedbackStatus == 1) {
            sb.append(((LinearLayout) this).mContext.getText(2131952354));
        } else if (feedbackStatus == 2) {
            sb.append(((LinearLayout) this).mContext.getText(2131952358));
        } else if (feedbackStatus == 3) {
            sb.append(((LinearLayout) this).mContext.getText(2131952356));
        } else if (feedbackStatus == 4) {
            sb.append(((LinearLayout) this).mContext.getText(2131952355));
        }
        sb.append(" ");
        sb.append(((LinearLayout) this).mContext.getText(2131952357));
        textView.setText(Html.fromHtml(sb.toString()));
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

    public FeedbackInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
