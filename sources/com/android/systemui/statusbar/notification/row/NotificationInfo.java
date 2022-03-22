package com.android.systemui.statusbar.notification.row;

import android.animation.TimeInterpolator;
import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda0;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda1;
import com.android.systemui.Dependency;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda4;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda5;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda1;
import com.android.wm.shell.transition.Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class NotificationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mActualHeight;
    public String mAppName;
    public OnAppSettingsClickListener mAppSettingsClickListener;
    public int mAppUid;
    public AssistantFeedbackController mAssistantFeedbackController;
    public TextView mAutomaticDescriptionView;
    public ChannelEditorDialogController mChannelEditorDialogController;
    public Integer mChosenImportance;
    public String mDelegatePkg;
    public NotificationEntry mEntry;
    public NotificationGuts mGutsContainer;
    public INotificationManager mINotificationManager;
    public boolean mIsAutomaticChosen;
    public boolean mIsDeviceProvisioned;
    public boolean mIsNonblockable;
    public boolean mIsSingleDefaultChannel;
    public MetricsLogger mMetricsLogger;
    public int mNumUniqueChannelsInRow;
    public OnSettingsClickListener mOnSettingsClickListener;
    public OnUserInteractionCallback mOnUserInteractionCallback;
    public String mPackageName;
    public Drawable mPkgIcon;
    public PackageManager mPm;
    public boolean mPressedApply;
    public TextView mPriorityDescriptionView;
    public StatusBarNotification mSbn;
    public boolean mShowAutomaticSetting;
    public TextView mSilentDescriptionView;
    public NotificationChannel mSingleNotificationChannel;
    public int mStartingChannelImportance;
    public UiEventLogger mUiEventLogger;
    public Set<NotificationChannel> mUniqueChannelsInRow;
    public boolean mWasShownHighPriority;
    public boolean mPresentingChannelEditorDialog = false;
    @VisibleForTesting
    public boolean mSkipPost = false;
    public AvatarPickerActivity$$ExternalSyntheticLambda0 mOnAutomatic = new AvatarPickerActivity$$ExternalSyntheticLambda0(this, 3);
    public AvatarPickerActivity$$ExternalSyntheticLambda1 mOnAlert = new AvatarPickerActivity$$ExternalSyntheticLambda1(this, 2);
    public InternetDialog$$ExternalSyntheticLambda3 mOnSilent = new InternetDialog$$ExternalSyntheticLambda3(this, 3);
    public PipMenuView$$ExternalSyntheticLambda1 mOnDismissSettings = new PipMenuView$$ExternalSyntheticLambda1(this, 2);

    /* loaded from: classes.dex */
    public interface OnAppSettingsClickListener {
    }

    /* loaded from: classes.dex */
    public interface OnSettingsClickListener {
        void onClick(NotificationChannel notificationChannel, int i);
    }

    /* loaded from: classes.dex */
    public static class UpdateImportanceRunnable implements Runnable {
        public final int mAppUid;
        public final NotificationChannel mChannelToUpdate;
        public final int mCurrentImportance;
        public final INotificationManager mINotificationManager;
        public final int mNewImportance;
        public final String mPackageName;
        public final boolean mUnlockImportance;

        @Override // java.lang.Runnable
        public final void run() {
            boolean z;
            try {
                NotificationChannel notificationChannel = this.mChannelToUpdate;
                if (notificationChannel == null) {
                    INotificationManager iNotificationManager = this.mINotificationManager;
                    String str = this.mPackageName;
                    int i = this.mAppUid;
                    if (this.mNewImportance >= this.mCurrentImportance) {
                        z = true;
                    } else {
                        z = false;
                    }
                    iNotificationManager.setNotificationsEnabledWithImportanceLockForPackage(str, i, z);
                } else if (this.mUnlockImportance) {
                    this.mINotificationManager.unlockNotificationChannel(this.mPackageName, this.mAppUid, notificationChannel.getId());
                } else {
                    notificationChannel.setImportance(this.mNewImportance);
                    this.mChannelToUpdate.lockFields(4);
                    this.mINotificationManager.updateNotificationChannelForPackage(this.mPackageName, this.mAppUid, this.mChannelToUpdate);
                }
            } catch (RemoteException e) {
                Log.e("InfoGuts", "Unable to update notification importance", e);
            }
        }

        public UpdateImportanceRunnable(INotificationManager iNotificationManager, String str, int i, NotificationChannel notificationChannel, int i2, int i3, boolean z) {
            this.mINotificationManager = iNotificationManager;
            this.mPackageName = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mCurrentImportance = i2;
            this.mNewImportance = i3;
            this.mUnlockImportance = z;
        }
    }

    public final void applyAlertingBehavior(int i, boolean z) {
        int i2;
        boolean z2 = true;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(0);
            TransitionSet addTransition = transitionSet.addTransition(new Fade(2)).addTransition(new ChangeBounds());
            Transition duration = new Fade(1).setStartDelay(150L).setDuration(200L);
            PathInterpolator pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
            addTransition.addTransition(duration.setInterpolator(pathInterpolator));
            transitionSet.setDuration(350L);
            transitionSet.setInterpolator((TimeInterpolator) pathInterpolator);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        View findViewById = findViewById(2131427469);
        View findViewById2 = findViewById(2131428861);
        View findViewById3 = findViewById(2131427528);
        if (i == 0) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new PipTaskOrganizer$$ExternalSyntheticLambda5(findViewById, findViewById2, findViewById3, 5));
        } else if (i == 1) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda2(findViewById, findViewById2, findViewById3, 0));
        } else if (i == 2) {
            this.mAutomaticDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(findViewById3, findViewById, findViewById2, 1));
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unrecognized alerting behavior: ", i));
        }
        if (getAlertingBehavior() == i) {
            z2 = false;
        }
        TextView textView = (TextView) findViewById(2131427865);
        if (z2) {
            i2 = 2131952463;
        } else {
            i2 = 2131952462;
        }
        textView.setText(i2);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final View getContentView() {
        return this;
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
    public final boolean willBeRemoved() {
        return false;
    }

    public final void bindInlineControls() {
        int i;
        int i2;
        if (this.mIsNonblockable) {
            findViewById(2131428503).setVisibility(0);
            findViewById(2131428502).setVisibility(8);
            findViewById(2131428134).setVisibility(8);
            ((TextView) findViewById(2131427865)).setText(2131952462);
            findViewById(2131429114).setVisibility(8);
        } else if (this.mNumUniqueChannelsInRow > 1) {
            findViewById(2131428503).setVisibility(8);
            findViewById(2131428134).setVisibility(8);
            findViewById(2131428502).setVisibility(0);
        } else {
            findViewById(2131428503).setVisibility(8);
            findViewById(2131428502).setVisibility(8);
            findViewById(2131428134).setVisibility(0);
        }
        View findViewById = findViewById(2131429114);
        findViewById.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda4(this, 3));
        if (!findViewById.hasOnClickListeners() || this.mIsNonblockable) {
            i = 8;
        } else {
            i = 0;
        }
        findViewById.setVisibility(i);
        View findViewById2 = findViewById(2131427865);
        findViewById2.setOnClickListener(this.mOnDismissSettings);
        findViewById2.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
        View findViewById3 = findViewById(2131428861);
        View findViewById4 = findViewById(2131427469);
        findViewById3.setOnClickListener(this.mOnSilent);
        findViewById4.setOnClickListener(this.mOnAlert);
        View findViewById5 = findViewById(2131427528);
        if (this.mShowAutomaticSetting) {
            TextView textView = this.mAutomaticDescriptionView;
            Context context = ((LinearLayout) this).mContext;
            AssistantFeedbackController assistantFeedbackController = this.mAssistantFeedbackController;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(assistantFeedbackController);
            int feedbackStatus = assistantFeedbackController.getFeedbackStatus(notificationEntry);
            if (feedbackStatus == 1) {
                i2 = 2131952894;
            } else if (feedbackStatus == 2) {
                i2 = 2131952897;
            } else if (feedbackStatus == 3) {
                i2 = 2131952896;
            } else if (feedbackStatus != 4) {
                i2 = 2131952893;
            } else {
                i2 = 2131952895;
            }
            textView.setText(Html.fromHtml(context.getText(i2).toString()));
            findViewById5.setVisibility(0);
            findViewById5.setOnClickListener(this.mOnAutomatic);
        } else {
            findViewById5.setVisibility(8);
        }
        applyAlertingBehavior(getAlertingBehavior(), false);
    }

    public final int getAlertingBehavior() {
        if (!this.mShowAutomaticSetting || this.mSingleNotificationChannel.hasUserSetImportance()) {
            return !this.mWasShownHighPriority ? 1 : 0;
        }
        return 2;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean handleCloseControls(boolean z, boolean z2) {
        int i;
        LogMaker logMaker;
        ChannelEditorDialogController channelEditorDialogController;
        NotificationChannel notificationChannel = null;
        if (this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = false;
            channelEditorDialogController.onFinishListener = null;
            Objects.requireNonNull(channelEditorDialogController);
            channelEditorDialogController.done();
        }
        if (z && !this.mIsNonblockable) {
            if (this.mChosenImportance == null) {
                this.mChosenImportance = Integer.valueOf(this.mStartingChannelImportance);
            }
            if (this.mChosenImportance != null) {
                logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_SAVE_IMPORTANCE);
                MetricsLogger metricsLogger = this.mMetricsLogger;
                Integer num = this.mChosenImportance;
                if (num != null) {
                    i = num.intValue();
                } else {
                    i = this.mStartingChannelImportance;
                }
                Integer valueOf = Integer.valueOf(i);
                StatusBarNotification statusBarNotification = this.mSbn;
                if (statusBarNotification == null) {
                    logMaker = new LogMaker(1621);
                } else {
                    logMaker = statusBarNotification.getLogMaker().setCategory(1621);
                }
                metricsLogger.write(logMaker.setCategory(291).setType(4).setSubtype(valueOf.intValue() - this.mStartingChannelImportance));
                int intValue = this.mChosenImportance.intValue();
                if (this.mStartingChannelImportance != -1000 && ((this.mWasShownHighPriority && this.mChosenImportance.intValue() >= 3) || (!this.mWasShownHighPriority && this.mChosenImportance.intValue() < 3))) {
                    intValue = this.mStartingChannelImportance;
                }
                Handler handler = new Handler((Looper) Dependency.get(Dependency.BG_LOOPER));
                INotificationManager iNotificationManager = this.mINotificationManager;
                String str = this.mPackageName;
                int i2 = this.mAppUid;
                if (this.mNumUniqueChannelsInRow == 1) {
                    notificationChannel = this.mSingleNotificationChannel;
                }
                handler.post(new UpdateImportanceRunnable(iNotificationManager, str, i2, notificationChannel, this.mStartingChannelImportance, intValue, this.mIsAutomaticChosen));
                this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
            }
        }
        return false;
    }

    public final void logUiEvent(NotificationControlsEvent notificationControlsEvent) {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification != null) {
            this.mUiEventLogger.logWithInstanceId(notificationControlsEvent, statusBarNotification.getUid(), this.mSbn.getPackageName(), this.mSbn.getInstanceId());
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final void onFinishedClosing() {
        LogMaker logMaker;
        Integer num = this.mChosenImportance;
        if (num != null) {
            this.mStartingChannelImportance = num.intValue();
        }
        bindInlineControls();
        logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_CLOSE);
        MetricsLogger metricsLogger = this.mMetricsLogger;
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification == null) {
            logMaker = new LogMaker(1621);
        } else {
            logMaker = statusBarNotification.getLogMaker().setCategory(1621);
        }
        metricsLogger.write(logMaker.setCategory(204).setType(1).setSubtype(0).setType(2));
    }

    @Override // android.view.View
    public final boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    public NotificationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mPriorityDescriptionView = (TextView) findViewById(2131427473);
        this.mSilentDescriptionView = (TextView) findViewById(2131428864);
        this.mAutomaticDescriptionView = (TextView) findViewById(2131427531);
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

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mActualHeight = getHeight();
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final int getActualHeight() {
        return this.mActualHeight;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean shouldBeSaved() {
        return this.mPressedApply;
    }
}
