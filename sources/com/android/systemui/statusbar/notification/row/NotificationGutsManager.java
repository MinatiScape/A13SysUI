package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.SnoozeCriterion;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda3;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator$mGutsListener$1;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.row.NotificationSnooze;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda9;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda3;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NotificationGutsManager implements Dumpable, NotificationLifetimeExtender, NotifGutsViewManager {
    public final AccessibilityManager mAccessibilityManager;
    public final AssistantFeedbackController mAssistantFeedbackController;
    public final Handler mBgHandler;
    public final Optional<BubblesManager> mBubblesManagerOptional;
    public final ChannelEditorDialogController mChannelEditorDialogController;
    public final Context mContext;
    public final UserContextProvider mContextTracker;
    public NotifGutsViewListener mGutsListener;
    public NotificationMenuRowPlugin.MenuItem mGutsMenuItem;
    public final HighPriorityProvider mHighPriorityProvider;
    @VisibleForTesting
    public String mKeyToRemoveOnGutsClosed;
    public final LauncherApps mLauncherApps;
    public NotificationListContainer mListContainer;
    public final Handler mMainHandler;
    public NotificationActivityStarter mNotificationActivityStarter;
    public NotificationGuts mNotificationGutsExposed;
    public NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    public final INotificationManager mNotificationManager;
    public OnSettingsClickListener mOnSettingsClickListener;
    public final OnUserInteractionCallback mOnUserInteractionCallback;
    public AnonymousClass1 mOpenRunnable;
    public final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    public NotificationPresenter mPresenter;
    public final ShadeController mShadeController;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final UiEventLogger mUiEventLogger;
    public final MetricsLogger mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
    public final NotificationLockscreenUserManager mLockscreenUserManager = (NotificationLockscreenUserManager) Dependency.get(NotificationLockscreenUserManager.class);
    public final StatusBarStateController mStatusBarStateController = (StatusBarStateController) Dependency.get(StatusBarStateController.class);
    public final DeviceProvisionedController mDeviceProvisionedController = (DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class);

    /* loaded from: classes.dex */
    public interface OnSettingsClickListener {
    }

    public NotificationGutsManager(Context context, Lazy lazy, Handler handler, Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController, DumpManager dumpManager) {
        this.mContext = context;
        this.mStatusBarOptionalLazy = lazy;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mAccessibilityManager = accessibilityManager;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mNotificationManager = iNotificationManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mLauncherApps = launcherApps;
        this.mContextTracker = userContextProvider;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mBubblesManagerOptional = optional;
        this.mUiEventLogger = uiEventLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mShadeController = shadeController;
        AppWidgetManager.getInstance(context);
        dumpManager.registerDumpable(this);
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public final void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mKeyToRemoveOnGutsClosed = notificationEntry.mKey;
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Keeping notification because it's showing guts. "), notificationEntry.mKey, "NotificationGutsManager");
                return;
            }
            return;
        }
        String str = this.mKeyToRemoveOnGutsClosed;
        if (str != null && str.equals(notificationEntry.mKey)) {
            this.mKeyToRemoveOnGutsClosed = null;
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Notification that was kept for guts was updated. "), notificationEntry.mKey, "NotificationGutsManager");
            }
        }
    }

    public final void closeAndSaveGuts(boolean z, boolean z2, boolean z3, boolean z4) {
        NotificationGuts notificationGuts = this.mNotificationGutsExposed;
        if (notificationGuts != null) {
            notificationGuts.removeCallbacks(this.mOpenRunnable);
            NotificationGuts notificationGuts2 = this.mNotificationGutsExposed;
            Objects.requireNonNull(notificationGuts2);
            NotificationGuts.GutsContent gutsContent = notificationGuts2.mGutsContent;
            if (gutsContent != null && ((gutsContent.isLeavebehind() && z) || (!notificationGuts2.mGutsContent.isLeavebehind() && z3))) {
                notificationGuts2.closeControls(-1, -1, notificationGuts2.mGutsContent.shouldBeSaved(), z2);
            }
        }
        if (z4) {
            this.mListContainer.resetExposedMenuView();
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationGutsManager state:");
        printWriter.print("  mKeyToRemoveOnGutsClosed (legacy): ");
        printWriter.println(this.mKeyToRemoveOnGutsClosed);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(27:10|(3:89|11|12)|15|(3:87|19|(23:21|23|(1:25)(1:26)|81|27|(1:29)|30|(5:85|32|(2:35|(13:40|54|(1:56)(1:57)|58|(1:60)|61|(1:68)(1:67)|69|(1:71)|72|(1:74)|75|76))|38|(0))|41|(1:43)(5:83|44|(2:47|(1:52)(1:53))|50|(0)(0))|54|(0)(0)|58|(0)|61|(1:63)|68|69|(0)|72|(0)|75|76))|22|23|(0)(0)|81|27|(0)|30|(0)|41|(0)(0)|54|(0)(0)|58|(0)|61|(0)|68|69|(0)|72|(0)|75|76) */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x014e A[Catch: NameNotFoundException -> 0x015a, TRY_LEAVE, TryCatch #0 {NameNotFoundException -> 0x015a, blocks: (B:27:0x0141, B:29:0x014e), top: B:81:0x0141 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0185 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initializeConversationNotificationInfo(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r20, final com.android.systemui.statusbar.notification.row.NotificationConversationInfo r21) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 651
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotificationGutsManager.initializeConversationNotificationInfo(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow, com.android.systemui.statusbar.notification.row.NotificationConversationInfo):void");
    }

    @VisibleForTesting
    public void initializeNotificationInfo(final ExpandableNotificationRow expandableNotificationRow, final NotificationInfo notificationInfo) throws Exception {
        NotificationInfo.OnSettingsClickListener onSettingsClickListener;
        boolean z;
        boolean z2;
        View.OnClickListener onClickListener;
        int i;
        CharSequence charSequence;
        LogMaker logMaker;
        NotificationChannelGroup notificationChannelGroupForPackage;
        Objects.requireNonNull(expandableNotificationRow);
        final NotificationGuts notificationGuts = expandableNotificationRow.mGuts;
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        final StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        final String packageName = statusBarNotification.getPackageName();
        UserHandle user = statusBarNotification.getUser();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, user.getIdentifier());
        NotificationGutsManager$$ExternalSyntheticLambda2 notificationGutsManager$$ExternalSyntheticLambda2 = new NotificationGutsManager$$ExternalSyntheticLambda2(this, notificationGuts, statusBarNotification, expandableNotificationRow);
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            onSettingsClickListener = new NotificationInfo.OnSettingsClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda3
                @Override // com.android.systemui.statusbar.notification.row.NotificationInfo.OnSettingsClickListener
                public final void onClick(NotificationChannel notificationChannel, int i2) {
                    NotificationGutsManager notificationGutsManager = NotificationGutsManager.this;
                    NotificationGuts notificationGuts2 = notificationGuts;
                    StatusBarNotification statusBarNotification2 = statusBarNotification;
                    String str = packageName;
                    ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                    Objects.requireNonNull(notificationGutsManager);
                    notificationGutsManager.mMetricsLogger.action(205);
                    notificationGuts2.resetFalsingCheck();
                    NotificationGutsManager.OnSettingsClickListener onSettingsClickListener2 = notificationGutsManager.mOnSettingsClickListener;
                    String key = statusBarNotification2.getKey();
                    StatusBarNotificationPresenter.AnonymousClass3 r1 = (StatusBarNotificationPresenter.AnonymousClass3) onSettingsClickListener2;
                    Objects.requireNonNull(r1);
                    try {
                        StatusBarNotificationPresenter.this.mBarService.onNotificationSettingsViewed(key);
                    } catch (RemoteException unused) {
                    }
                    notificationGutsManager.startAppNotificationSettingsActivity(str, i2, notificationChannel, expandableNotificationRow2);
                }
            };
        } else {
            onSettingsClickListener = null;
        }
        INotificationManager iNotificationManager = this.mNotificationManager;
        OnUserInteractionCallback onUserInteractionCallback = this.mOnUserInteractionCallback;
        ChannelEditorDialogController channelEditorDialogController = this.mChannelEditorDialogController;
        NotificationChannel channel = expandableNotificationRow.mEntry.getChannel();
        ArraySet<NotificationChannel> uniqueChannels = expandableNotificationRow.getUniqueChannels();
        NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        boolean isDeviceProvisioned = this.mDeviceProvisionedController.isDeviceProvisioned();
        boolean isNonblockable = expandableNotificationRow.getIsNonblockable();
        boolean isHighPriority = this.mHighPriorityProvider.isHighPriority(expandableNotificationRow.mEntry);
        AssistantFeedbackController assistantFeedbackController = this.mAssistantFeedbackController;
        Objects.requireNonNull(notificationInfo);
        notificationInfo.mINotificationManager = iNotificationManager;
        notificationInfo.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        notificationInfo.mOnUserInteractionCallback = onUserInteractionCallback;
        notificationInfo.mChannelEditorDialogController = channelEditorDialogController;
        notificationInfo.mAssistantFeedbackController = assistantFeedbackController;
        notificationInfo.mPackageName = packageName;
        notificationInfo.mUniqueChannelsInRow = uniqueChannels;
        notificationInfo.mNumUniqueChannelsInRow = uniqueChannels.size();
        notificationInfo.mEntry = notificationEntry2;
        Objects.requireNonNull(notificationEntry2);
        notificationInfo.mSbn = notificationEntry2.mSbn;
        notificationInfo.mPm = packageManagerForUser;
        notificationInfo.mAppSettingsClickListener = notificationGutsManager$$ExternalSyntheticLambda2;
        notificationInfo.mAppName = notificationInfo.mPackageName;
        notificationInfo.mOnSettingsClickListener = onSettingsClickListener;
        notificationInfo.mSingleNotificationChannel = channel;
        notificationInfo.mStartingChannelImportance = channel.getImportance();
        notificationInfo.mWasShownHighPriority = isHighPriority;
        notificationInfo.mIsNonblockable = isNonblockable;
        notificationInfo.mAppUid = notificationInfo.mSbn.getUid();
        notificationInfo.mDelegatePkg = notificationInfo.mSbn.getOpPkg();
        notificationInfo.mIsDeviceProvisioned = isDeviceProvisioned;
        AssistantFeedbackController assistantFeedbackController2 = notificationInfo.mAssistantFeedbackController;
        Objects.requireNonNull(assistantFeedbackController2);
        notificationInfo.mShowAutomaticSetting = assistantFeedbackController2.mFeedbackEnabled;
        notificationInfo.mUiEventLogger = uiEventLogger;
        int numNotificationChannelsForPackage = notificationInfo.mINotificationManager.getNumNotificationChannelsForPackage(packageName, notificationInfo.mAppUid, false);
        int i2 = notificationInfo.mNumUniqueChannelsInRow;
        if (i2 != 0) {
            if (i2 == 1 && notificationInfo.mSingleNotificationChannel.getId().equals("miscellaneous") && numNotificationChannelsForPackage == 1) {
                z = true;
            } else {
                z = false;
            }
            notificationInfo.mIsSingleDefaultChannel = z;
            if (notificationInfo.getAlertingBehavior() == 2) {
                z2 = true;
            } else {
                z2 = false;
            }
            notificationInfo.mIsAutomaticChosen = z2;
            notificationInfo.mPkgIcon = null;
            try {
                ApplicationInfo applicationInfo = notificationInfo.mPm.getApplicationInfo(notificationInfo.mPackageName, 795136);
                if (applicationInfo != null) {
                    notificationInfo.mAppName = String.valueOf(notificationInfo.mPm.getApplicationLabel(applicationInfo));
                    notificationInfo.mPkgIcon = notificationInfo.mPm.getApplicationIcon(applicationInfo);
                }
            } catch (PackageManager.NameNotFoundException unused) {
                notificationInfo.mPkgIcon = notificationInfo.mPm.getDefaultActivityIcon();
            }
            ((ImageView) notificationInfo.findViewById(2131428592)).setImageDrawable(notificationInfo.mPkgIcon);
            ((TextView) notificationInfo.findViewById(2131428593)).setText(notificationInfo.mAppName);
            TextView textView = (TextView) notificationInfo.findViewById(2131427812);
            if (!TextUtils.equals(notificationInfo.mPackageName, notificationInfo.mDelegatePkg)) {
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
            View findViewById = notificationInfo.findViewById(2131427507);
            PackageManager packageManager = notificationInfo.mPm;
            String str = notificationInfo.mPackageName;
            NotificationChannel notificationChannel = notificationInfo.mSingleNotificationChannel;
            int id = notificationInfo.mSbn.getId();
            String tag = notificationInfo.mSbn.getTag();
            Intent intent = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.NOTIFICATION_PREFERENCES").setPackage(str);
            List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
            if (queryIntentActivities == null || queryIntentActivities.size() == 0 || queryIntentActivities.get(0) == null) {
                intent = null;
            } else {
                ActivityInfo activityInfo = queryIntentActivities.get(0).activityInfo;
                intent.setClassName(activityInfo.packageName, activityInfo.name);
                if (notificationChannel != null) {
                    intent.putExtra("android.intent.extra.CHANNEL_ID", notificationChannel.getId());
                }
                intent.putExtra("android.intent.extra.NOTIFICATION_ID", id);
                intent.putExtra("android.intent.extra.NOTIFICATION_TAG", tag);
            }
            if (intent == null || TextUtils.isEmpty(notificationInfo.mSbn.getNotification().getSettingsText())) {
                findViewById.setVisibility(8);
            } else {
                findViewById.setVisibility(0);
                findViewById.setOnClickListener(new EditUserInfoController$$ExternalSyntheticLambda3(notificationInfo, intent, 1));
            }
            View findViewById2 = notificationInfo.findViewById(2131428121);
            final int i3 = notificationInfo.mAppUid;
            if (i3 < 0 || notificationInfo.mOnSettingsClickListener == null || !notificationInfo.mIsDeviceProvisioned) {
                onClickListener = null;
            } else {
                onClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        NotificationChannel notificationChannel2;
                        NotificationInfo notificationInfo2 = NotificationInfo.this;
                        int i4 = i3;
                        int i5 = NotificationInfo.$r8$clinit;
                        Objects.requireNonNull(notificationInfo2);
                        NotificationInfo.OnSettingsClickListener onSettingsClickListener2 = notificationInfo2.mOnSettingsClickListener;
                        if (notificationInfo2.mNumUniqueChannelsInRow > 1) {
                            notificationChannel2 = null;
                        } else {
                            notificationChannel2 = notificationInfo2.mSingleNotificationChannel;
                        }
                        onSettingsClickListener2.onClick(notificationChannel2, i4);
                    }
                };
            }
            findViewById2.setOnClickListener(onClickListener);
            if (findViewById2.hasOnClickListeners()) {
                i = 0;
            } else {
                i = 8;
            }
            findViewById2.setVisibility(i);
            TextView textView2 = (TextView) notificationInfo.findViewById(2131427685);
            if (notificationInfo.mIsSingleDefaultChannel || notificationInfo.mNumUniqueChannelsInRow > 1) {
                textView2.setVisibility(8);
            } else {
                textView2.setText(notificationInfo.mSingleNotificationChannel.getName());
            }
            NotificationChannel notificationChannel2 = notificationInfo.mSingleNotificationChannel;
            if (notificationChannel2 == null || notificationChannel2.getGroup() == null || (notificationChannelGroupForPackage = notificationInfo.mINotificationManager.getNotificationChannelGroupForPackage(notificationInfo.mSingleNotificationChannel.getGroup(), notificationInfo.mPackageName, notificationInfo.mAppUid)) == null) {
                charSequence = null;
            } else {
                charSequence = notificationChannelGroupForPackage.getName();
            }
            TextView textView3 = (TextView) notificationInfo.findViewById(2131428044);
            if (charSequence != null) {
                textView3.setText(charSequence);
                textView3.setVisibility(0);
            } else {
                textView3.setVisibility(8);
            }
            notificationInfo.bindInlineControls();
            notificationInfo.logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_OPEN);
            MetricsLogger metricsLogger = notificationInfo.mMetricsLogger;
            StatusBarNotification statusBarNotification2 = notificationInfo.mSbn;
            if (statusBarNotification2 == null) {
                logMaker = new LogMaker(1621);
            } else {
                logMaker = statusBarNotification2.getLogMaker().setCategory(1621);
            }
            metricsLogger.write(logMaker.setCategory(204).setType(1).setSubtype(0));
            return;
        }
        throw new IllegalArgumentException("bindNotification requires at least one channel");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.notification.row.NotificationGutsManager$1, java.lang.Runnable] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean openGutsInternal(android.view.View r10, final int r11, final int r12, final com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin.MenuItem r13) {
        /*
            r9 = this;
            boolean r0 = r10 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            android.os.IBinder r0 = r10.getWindowToken()
            if (r0 != 0) goto L_0x0014
            java.lang.String r9 = "NotificationGutsManager"
            java.lang.String r10 = "Trying to show notification guts, but not attached to window"
            android.util.Log.e(r9, r10)
            return r1
        L_0x0014:
            r4 = r10
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r4
            r10.performHapticFeedback(r1)
            boolean r10 = r4.areGutsExposed()
            r0 = 1
            if (r10 == 0) goto L_0x0025
            r9.closeAndSaveGuts(r1, r1, r0, r0)
            return r1
        L_0x0025:
            com.android.systemui.statusbar.notification.row.NotificationGuts r10 = r4.mGuts
            if (r10 != 0) goto L_0x002e
            android.view.ViewStub r10 = r4.mGutsStub
            r10.inflate()
        L_0x002e:
            com.android.systemui.statusbar.notification.row.NotificationGuts r10 = r4.mGuts
            r9.mNotificationGutsExposed = r10
            boolean r2 = r9.bindGuts(r4, r13)
            if (r2 != 0) goto L_0x0039
            return r1
        L_0x0039:
            if (r10 != 0) goto L_0x003c
            return r1
        L_0x003c:
            r1 = 4
            r10.setVisibility(r1)
            com.android.systemui.statusbar.notification.row.NotificationGutsManager$1 r1 = new com.android.systemui.statusbar.notification.row.NotificationGutsManager$1
            r2 = r1
            r3 = r9
            r5 = r10
            r6 = r11
            r7 = r12
            r8 = r13
            r2.<init>()
            r9.mOpenRunnable = r1
            r10.post(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotificationGutsManager.openGutsInternal(android.view.View, int, int, com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin$MenuItem):boolean");
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public final boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        NotificationGuts notificationGuts;
        boolean z;
        NotificationGuts notificationGuts2 = this.mNotificationGutsExposed;
        if (notificationGuts2 != null) {
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            NotificationGuts notificationGuts3 = null;
            if (expandableNotificationRow != null) {
                notificationGuts = expandableNotificationRow.mGuts;
            } else {
                notificationGuts = null;
            }
            if (notificationGuts != null) {
                if (expandableNotificationRow != null) {
                    notificationGuts3 = expandableNotificationRow.mGuts;
                }
                if (notificationGuts2 == notificationGuts3) {
                    Objects.requireNonNull(notificationGuts2);
                    NotificationGuts.GutsContent gutsContent = notificationGuts2.mGutsContent;
                    if (gutsContent == null || !gutsContent.isLeavebehind()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (!z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public final void startAppNotificationSettingsActivity(String str, int i, NotificationChannel notificationChannel, ExpandableNotificationRow expandableNotificationRow) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", str);
        intent.putExtra("app_uid", i);
        if (notificationChannel != null) {
            Bundle bundle = new Bundle();
            intent.putExtra(":settings:fragment_args_key", notificationChannel.getId());
            bundle.putString(":settings:fragment_args_key", notificationChannel.getId());
            intent.putExtra(":settings:show_fragment_args", bundle);
        }
        StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.mNotificationActivityStarter;
        Objects.requireNonNull(statusBarNotificationActivityStarter);
        StatusBar statusBar = statusBarNotificationActivityStarter.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBarNotificationActivityStarter.mActivityStarter.dismissKeyguardThenExecute(new StatusBarNotificationActivityStarter.AnonymousClass4(expandableNotificationRow, statusBar.shouldAnimateLaunch(true, false), intent, i), null, false);
    }

    @VisibleForTesting
    public boolean bindGuts(ExpandableNotificationRow expandableNotificationRow, NotificationMenuRowPlugin.MenuItem menuItem) {
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        if (expandableNotificationRow.mGuts != null && (menuItem.getGutsView() instanceof NotificationGuts.GutsContent)) {
            NotificationGuts notificationGuts = expandableNotificationRow.mGuts;
            NotificationGuts.GutsContent gutsContent = (NotificationGuts.GutsContent) menuItem.getGutsView();
            Objects.requireNonNull(notificationGuts);
            gutsContent.setGutsParent(notificationGuts);
            gutsContent.setAccessibilityDelegate(notificationGuts.mGutsContentAccessibilityDelegate);
            notificationGuts.mGutsContent = gutsContent;
            notificationGuts.removeAllViews();
            notificationGuts.addView(notificationGuts.mGutsContent.getContentView());
        }
        Objects.requireNonNull(notificationEntry);
        expandableNotificationRow.setTag(notificationEntry.mSbn.getPackageName());
        NotificationGuts notificationGuts2 = expandableNotificationRow.mGuts;
        NotificationGutsManager$$ExternalSyntheticLambda1 notificationGutsManager$$ExternalSyntheticLambda1 = new NotificationGutsManager$$ExternalSyntheticLambda1(this, expandableNotificationRow, notificationEntry);
        Objects.requireNonNull(notificationGuts2);
        notificationGuts2.mClosedListener = notificationGutsManager$$ExternalSyntheticLambda1;
        View gutsView = menuItem.getGutsView();
        try {
            if (gutsView instanceof NotificationSnooze) {
                initializeSnoozeView(expandableNotificationRow, (NotificationSnooze) gutsView);
                return true;
            } else if (gutsView instanceof NotificationInfo) {
                initializeNotificationInfo(expandableNotificationRow, (NotificationInfo) gutsView);
                return true;
            } else if (gutsView instanceof NotificationConversationInfo) {
                initializeConversationNotificationInfo(expandableNotificationRow, (NotificationConversationInfo) gutsView);
                return true;
            } else if (gutsView instanceof PartialConversationInfo) {
                initializePartialConversationNotificationInfo(expandableNotificationRow, (PartialConversationInfo) gutsView);
                return true;
            } else if (!(gutsView instanceof FeedbackInfo)) {
                return true;
            } else {
                FeedbackInfo feedbackInfo = (FeedbackInfo) gutsView;
                AssistantFeedbackController assistantFeedbackController = this.mAssistantFeedbackController;
                NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
                Objects.requireNonNull(assistantFeedbackController);
                if (assistantFeedbackController.mIcons.get(assistantFeedbackController.getFeedbackStatus(notificationEntry2)) == null) {
                    return true;
                }
                NotificationEntry notificationEntry3 = expandableNotificationRow.mEntry;
                Objects.requireNonNull(notificationEntry3);
                StatusBarNotification statusBarNotification = notificationEntry3.mSbn;
                feedbackInfo.bindGuts(StatusBar.getPackageManagerForUser(this.mContext, statusBarNotification.getUser().getIdentifier()), statusBarNotification, expandableNotificationRow.mEntry, expandableNotificationRow, this.mAssistantFeedbackController);
                return true;
            }
        } catch (Exception e) {
            Log.e("NotificationGutsManager", "error binding guts", e);
            return false;
        }
    }

    @VisibleForTesting
    public void initializePartialConversationNotificationInfo(final ExpandableNotificationRow expandableNotificationRow, final PartialConversationInfo partialConversationInfo) throws Exception {
        NotificationInfo.OnSettingsClickListener onSettingsClickListener;
        int i;
        Objects.requireNonNull(expandableNotificationRow);
        final NotificationGuts notificationGuts = expandableNotificationRow.mGuts;
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        final StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        final String packageName = statusBarNotification.getPackageName();
        UserHandle user = statusBarNotification.getUser();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, user.getIdentifier());
        View.OnClickListener onClickListener = null;
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            onSettingsClickListener = new NotificationInfo.OnSettingsClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda4
                @Override // com.android.systemui.statusbar.notification.row.NotificationInfo.OnSettingsClickListener
                public final void onClick(NotificationChannel notificationChannel, int i2) {
                    NotificationGutsManager notificationGutsManager = NotificationGutsManager.this;
                    NotificationGuts notificationGuts2 = notificationGuts;
                    StatusBarNotification statusBarNotification2 = statusBarNotification;
                    String str = packageName;
                    ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                    Objects.requireNonNull(notificationGutsManager);
                    notificationGutsManager.mMetricsLogger.action(205);
                    notificationGuts2.resetFalsingCheck();
                    NotificationGutsManager.OnSettingsClickListener onSettingsClickListener2 = notificationGutsManager.mOnSettingsClickListener;
                    String key = statusBarNotification2.getKey();
                    StatusBarNotificationPresenter.AnonymousClass3 r1 = (StatusBarNotificationPresenter.AnonymousClass3) onSettingsClickListener2;
                    Objects.requireNonNull(r1);
                    try {
                        StatusBarNotificationPresenter.this.mBarService.onNotificationSettingsViewed(key);
                    } catch (RemoteException unused) {
                    }
                    notificationGutsManager.startAppNotificationSettingsActivity(str, i2, notificationChannel, expandableNotificationRow2);
                }
            };
        } else {
            onSettingsClickListener = null;
        }
        ChannelEditorDialogController channelEditorDialogController = this.mChannelEditorDialogController;
        NotificationChannel channel = expandableNotificationRow.mEntry.getChannel();
        ArraySet<NotificationChannel> uniqueChannels = expandableNotificationRow.getUniqueChannels();
        NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
        boolean isDeviceProvisioned = this.mDeviceProvisionedController.isDeviceProvisioned();
        boolean isNonblockable = expandableNotificationRow.getIsNonblockable();
        Objects.requireNonNull(partialConversationInfo);
        partialConversationInfo.mPackageName = packageName;
        Objects.requireNonNull(notificationEntry2);
        StatusBarNotification statusBarNotification2 = notificationEntry2.mSbn;
        partialConversationInfo.mSbn = statusBarNotification2;
        partialConversationInfo.mPm = packageManagerForUser;
        partialConversationInfo.mAppName = partialConversationInfo.mPackageName;
        partialConversationInfo.mOnSettingsClickListener = onSettingsClickListener;
        partialConversationInfo.mNotificationChannel = channel;
        partialConversationInfo.mAppUid = statusBarNotification2.getUid();
        partialConversationInfo.mDelegatePkg = partialConversationInfo.mSbn.getOpPkg();
        partialConversationInfo.mIsDeviceProvisioned = isDeviceProvisioned;
        partialConversationInfo.mIsNonBlockable = isNonblockable;
        partialConversationInfo.mChannelEditorDialogController = channelEditorDialogController;
        partialConversationInfo.mUniqueChannelsInRow = uniqueChannels;
        try {
            ApplicationInfo applicationInfo = partialConversationInfo.mPm.getApplicationInfo(partialConversationInfo.mPackageName, 795136);
            if (applicationInfo != null) {
                partialConversationInfo.mAppName = String.valueOf(partialConversationInfo.mPm.getApplicationLabel(applicationInfo));
                partialConversationInfo.mPkgIcon = partialConversationInfo.mPm.getApplicationIcon(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            partialConversationInfo.mPkgIcon = partialConversationInfo.mPm.getDefaultActivityIcon();
        }
        ((TextView) partialConversationInfo.findViewById(2131428475)).setText(partialConversationInfo.mAppName);
        ((ImageView) partialConversationInfo.findViewById(2131428102)).setImageDrawable(partialConversationInfo.mPkgIcon);
        TextView textView = (TextView) partialConversationInfo.findViewById(2131427812);
        int i2 = 0;
        if (!TextUtils.equals(partialConversationInfo.mPackageName, partialConversationInfo.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        final int i3 = partialConversationInfo.mAppUid;
        if (i3 >= 0 && partialConversationInfo.mOnSettingsClickListener != null && partialConversationInfo.mIsDeviceProvisioned) {
            onClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.PartialConversationInfo$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PartialConversationInfo partialConversationInfo2 = PartialConversationInfo.this;
                    int i4 = i3;
                    int i5 = PartialConversationInfo.$r8$clinit;
                    Objects.requireNonNull(partialConversationInfo2);
                    partialConversationInfo2.mOnSettingsClickListener.onClick(partialConversationInfo2.mNotificationChannel, i4);
                }
            };
        }
        View findViewById = partialConversationInfo.findViewById(2131428121);
        findViewById.setOnClickListener(onClickListener);
        if (findViewById.hasOnClickListeners()) {
            i = 0;
        } else {
            i = 8;
        }
        findViewById.setVisibility(i);
        partialConversationInfo.findViewById(2131428843).setOnClickListener(onClickListener);
        ((TextView) partialConversationInfo.findViewById(2131428503)).setText(partialConversationInfo.getResources().getString(2131952874, partialConversationInfo.mAppName));
        View findViewById2 = partialConversationInfo.findViewById(2131429114);
        findViewById2.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda3(partialConversationInfo, 1));
        if (!findViewById2.hasOnClickListeners() || partialConversationInfo.mIsNonBlockable) {
            i2 = 8;
        }
        findViewById2.setVisibility(i2);
        View findViewById3 = partialConversationInfo.findViewById(2131427865);
        findViewById3.setOnClickListener(partialConversationInfo.mOnDone);
        findViewById3.setAccessibilityDelegate(partialConversationInfo.mGutsContainer.getAccessibilityDelegate());
    }

    public final void initializeSnoozeView(ExpandableNotificationRow expandableNotificationRow, NotificationSnooze notificationSnooze) {
        Objects.requireNonNull(expandableNotificationRow);
        NotificationGuts notificationGuts = expandableNotificationRow.mGuts;
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        NotificationSwipeHelper swipeActionHelper = this.mListContainer.getSwipeActionHelper();
        Objects.requireNonNull(notificationSnooze);
        notificationSnooze.mSnoozeListener = swipeActionHelper;
        notificationSnooze.mSbn = statusBarNotification;
        NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry2);
        List snoozeCriteria = notificationEntry2.mRanking.getSnoozeCriteria();
        if (snoozeCriteria != null) {
            notificationSnooze.mSnoozeOptions.clear();
            notificationSnooze.mSnoozeOptions = notificationSnooze.getDefaultSnoozeOptions();
            int min = Math.min(1, snoozeCriteria.size());
            for (int i = 0; i < min; i++) {
                SnoozeCriterion snoozeCriterion = (SnoozeCriterion) snoozeCriteria.get(i);
                notificationSnooze.mSnoozeOptions.add(new NotificationSnooze.NotificationSnoozeOption(snoozeCriterion, 0, snoozeCriterion.getExplanation(), snoozeCriterion.getConfirmation(), new AccessibilityNodeInfo.AccessibilityAction(2131427447, snoozeCriterion.getExplanation())));
            }
            notificationSnooze.createOptionViews();
        }
        StatusBar$$ExternalSyntheticLambda9 statusBar$$ExternalSyntheticLambda9 = new StatusBar$$ExternalSyntheticLambda9(this, expandableNotificationRow);
        Objects.requireNonNull(notificationGuts);
        notificationGuts.mHeightListener = statusBar$$ExternalSyntheticLambda9;
    }

    public final boolean openGuts(final View view, final int i, final int i2, final NotificationMenuRowPlugin.MenuItem menuItem) {
        if ((menuItem.getGutsView() instanceof NotificationGuts.GutsContent) && ((NotificationGuts.GutsContent) menuItem.getGutsView()).needsFalsingProtection()) {
            StatusBarStateController statusBarStateController = this.mStatusBarStateController;
            if (statusBarStateController instanceof StatusBarStateControllerImpl) {
                StatusBarStateControllerImpl statusBarStateControllerImpl = (StatusBarStateControllerImpl) statusBarStateController;
                Objects.requireNonNull(statusBarStateControllerImpl);
                statusBarStateControllerImpl.mLeaveOpenOnKeyguardHide = true;
            }
            Optional<StatusBar> optional = this.mStatusBarOptionalLazy.get();
            if (optional.isPresent()) {
                optional.get().executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        final NotificationGutsManager notificationGutsManager = NotificationGutsManager.this;
                        final View view2 = view;
                        final int i3 = i;
                        final int i4 = i2;
                        final NotificationMenuRowPlugin.MenuItem menuItem2 = menuItem;
                        Objects.requireNonNull(notificationGutsManager);
                        notificationGutsManager.mMainHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationGutsManager notificationGutsManager2 = NotificationGutsManager.this;
                                View view3 = view2;
                                int i5 = i3;
                                int i6 = i4;
                                NotificationMenuRowPlugin.MenuItem menuItem3 = menuItem2;
                                Objects.requireNonNull(notificationGutsManager2);
                                notificationGutsManager2.openGutsInternal(view3, i5, i6, menuItem3);
                            }
                        });
                    }
                }, false, true, true);
                return true;
            }
        }
        return openGutsInternal(view, i, i2, menuItem);
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public final void setCallback(ScreenshotController$$ExternalSyntheticLambda3 screenshotController$$ExternalSyntheticLambda3) {
        this.mNotificationLifetimeFinishedCallback = screenshotController$$ExternalSyntheticLambda3;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager
    public final void setGutsListener(GutsCoordinator$mGutsListener$1 gutsCoordinator$mGutsListener$1) {
        this.mGutsListener = gutsCoordinator$mGutsListener$1;
    }
}
