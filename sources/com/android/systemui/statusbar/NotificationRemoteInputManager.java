package com.android.systemui.statusbar;

import android.animation.Animator;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.pm.UserInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.fragment.R$id;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.phone.BiometricUnlockController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
/* loaded from: classes.dex */
public final class NotificationRemoteInputManager implements Dumpable {
    public static final boolean ENABLE_REMOTE_INPUT = SystemProperties.getBoolean("debug.enable_remote_input", true);
    public static boolean FORCE_REMOTE_INPUT_HISTORY = SystemProperties.getBoolean("debug.force_remoteinput_history", true);
    public Callback mCallback;
    public final NotificationClickNotifier mClickNotifier;
    public final NotificationEntryManager mEntryManager;
    public final KeyguardManager mKeyguardManager;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final ActionClickLogger mLogger;
    public final Handler mMainHandler;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final RemoteInputNotificationRebuilder mRebuilder;
    public RemoteInputController mRemoteInputController;
    public RemoteInputListener mRemoteInputListener;
    public final RemoteInputUriController mRemoteInputUriController;
    public final SmartReplyController mSmartReplyController;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final StatusBarStateController mStatusBarStateController;
    public final UserManager mUserManager;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final ArrayList mControllerCallbacks = new ArrayList();
    public final AnonymousClass1 mInteractionHandler = new AnonymousClass1();
    public IStatusBarService mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));

    /* renamed from: com.android.systemui.statusbar.NotificationRemoteInputManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements RemoteViews.InteractionHandler {
        public AnonymousClass1() {
        }

        public final boolean onInteraction(View view, PendingIntent pendingIntent, RemoteViews.RemoteResponse remoteResponse) {
            NotificationEntry notificationEntry;
            boolean z;
            int i;
            String str;
            RemoteInput[] remoteInputArr;
            String str2;
            String str3;
            NotificationListenerService.Ranking ranking;
            NotificationChannel channel;
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationRemoteInputManager.this.mStatusBarOptionalLazy.get().ifPresent(new PipMotionHelper$$ExternalSyntheticLambda1(view, 2));
            ViewParent parent = view.getParent();
            while (true) {
                if (parent == null) {
                    notificationEntry = null;
                    break;
                } else if (parent instanceof ExpandableNotificationRow) {
                    notificationEntry = ((ExpandableNotificationRow) parent).mEntry;
                    break;
                } else {
                    parent = parent.getParent();
                }
            }
            ActionClickLogger actionClickLogger = NotificationRemoteInputManager.this.mLogger;
            Objects.requireNonNull(actionClickLogger);
            LogBuffer logBuffer = actionClickLogger.buffer;
            ActionClickLogger$logInitialClick$2 actionClickLogger$logInitialClick$2 = ActionClickLogger$logInitialClick$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logInitialClick$2);
                if (notificationEntry == null) {
                    str2 = null;
                } else {
                    str2 = notificationEntry.mKey;
                }
                obtain.str1 = str2;
                if (notificationEntry == null || (ranking = notificationEntry.mRanking) == null || (channel = ranking.getChannel()) == null) {
                    str3 = null;
                } else {
                    str3 = channel.getId();
                }
                obtain.str2 = str3;
                obtain.str3 = pendingIntent.getIntent().toString();
                logBuffer.push(obtain);
            }
            boolean z2 = false;
            boolean z3 = true;
            if (NotificationRemoteInputManager.this.mCallback.shouldHandleRemoteInput()) {
                z = true;
            } else {
                Object tag = view.getTag(16909390);
                if (tag instanceof RemoteInput[]) {
                    remoteInputArr = (RemoteInput[]) tag;
                } else {
                    remoteInputArr = null;
                }
                if (remoteInputArr != null) {
                    RemoteInput remoteInput = null;
                    for (RemoteInput remoteInput2 : remoteInputArr) {
                        if (remoteInput2.getAllowFreeFormInput()) {
                            remoteInput = remoteInput2;
                        }
                    }
                    if (remoteInput != null) {
                        NotificationRemoteInputManager notificationRemoteInputManager = NotificationRemoteInputManager.this;
                        Objects.requireNonNull(notificationRemoteInputManager);
                        z = true;
                        z3 = notificationRemoteInputManager.activateRemoteInput(view, remoteInputArr, remoteInput, pendingIntent, null, null, null);
                    }
                }
                z = true;
                z3 = false;
            }
            if (z3) {
                ActionClickLogger actionClickLogger2 = NotificationRemoteInputManager.this.mLogger;
                Objects.requireNonNull(actionClickLogger2);
                LogBuffer logBuffer2 = actionClickLogger2.buffer;
                ActionClickLogger$logRemoteInputWasHandled$2 actionClickLogger$logRemoteInputWasHandled$2 = ActionClickLogger$logRemoteInputWasHandled$2.INSTANCE;
                Objects.requireNonNull(logBuffer2);
                if (!logBuffer2.frozen) {
                    LogMessageImpl obtain2 = logBuffer2.obtain("ActionClickLogger", logLevel, actionClickLogger$logRemoteInputWasHandled$2);
                    if (notificationEntry == null) {
                        str = null;
                    } else {
                        str = notificationEntry.mKey;
                    }
                    obtain2.str1 = str;
                    logBuffer2.push(obtain2);
                }
                return z;
            }
            Notification.Action actionFromView = getActionFromView(view, notificationEntry, pendingIntent);
            if (actionFromView != null) {
                ViewParent parent2 = view.getParent();
                Objects.requireNonNull(notificationEntry);
                String key = notificationEntry.mSbn.getKey();
                if (view.getId() != 16908716 || parent2 == null || !(parent2 instanceof ViewGroup)) {
                    i = -1;
                } else {
                    i = ((ViewGroup) parent2).indexOfChild(view);
                }
                NotificationVisibility obtain3 = NotificationRemoteInputManager.this.mVisibilityProvider.obtain(notificationEntry, z);
                NotificationClickNotifier notificationClickNotifier = NotificationRemoteInputManager.this.mClickNotifier;
                Objects.requireNonNull(notificationClickNotifier);
                try {
                    notificationClickNotifier.barService.onNotificationActionClick(key, i, actionFromView, obtain3, false);
                } catch (RemoteException unused) {
                }
                notificationClickNotifier.mainExecutor.execute(new NotificationClickNotifier$onNotificationActionClick$1(notificationClickNotifier, key));
            }
            try {
                ActivityManager.getService().resumeAppSwitches();
            } catch (RemoteException unused2) {
            }
            Notification.Action actionFromView2 = getActionFromView(view, notificationEntry, pendingIntent);
            Callback callback = NotificationRemoteInputManager.this.mCallback;
            if (actionFromView2 != null) {
                z2 = actionFromView2.isAuthenticationRequired();
            }
            return callback.handleRemoteViewClick(pendingIntent, z2, new NotificationRemoteInputManager$1$$ExternalSyntheticLambda0(this, remoteResponse, view, notificationEntry, pendingIntent));
        }

        public static Notification.Action getActionFromView(View view, NotificationEntry notificationEntry, PendingIntent pendingIntent) {
            Integer num = (Integer) view.getTag(16909269);
            if (num == null) {
                return null;
            }
            if (notificationEntry == null) {
                Log.w("NotifRemoteInputManager", "Couldn't determine notification for click.");
                return null;
            }
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            Notification.Action[] actionArr = statusBarNotification.getNotification().actions;
            if (actionArr == null || num.intValue() >= actionArr.length) {
                Log.w("NotifRemoteInputManager", "statusBarNotification.getNotification().actions is null or invalid");
                return null;
            }
            Notification.Action action = statusBarNotification.getNotification().actions[num.intValue()];
            if (Objects.equals(action.actionIntent, pendingIntent)) {
                return action;
            }
            Log.w("NotifRemoteInputManager", "actionIntent does not match");
            return null;
        }
    }

    /* loaded from: classes.dex */
    public interface AuthBypassPredicate {
        boolean canSendRemoteInputWithoutBouncer();
    }

    /* loaded from: classes.dex */
    public interface BouncerChecker {
    }

    /* loaded from: classes.dex */
    public interface Callback {
        boolean handleRemoteViewClick(PendingIntent pendingIntent, boolean z, NotificationRemoteInputManager$1$$ExternalSyntheticLambda0 notificationRemoteInputManager$1$$ExternalSyntheticLambda0);

        void onLockedRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view);

        void onLockedWorkRemoteInput(int i, View view);

        void onMakeExpandedVisibleForRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view, boolean z, NotificationRemoteInputManager$$ExternalSyntheticLambda2 notificationRemoteInputManager$$ExternalSyntheticLambda2);

        boolean shouldHandleRemoteInput();
    }

    /* loaded from: classes.dex */
    public interface ClickHandler {
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class LegacyRemoteInputLifetimeExtender implements RemoteInputListener, Dumpable {
        public final ArrayList<NotificationLifetimeExtender> mLifetimeExtenders;
        public NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
        public RemoteInputController mRemoteInputController;
        public final ArraySet<String> mKeysKeptForRemoteInputHistory = new ArraySet<>();
        public final ArraySet<NotificationEntry> mEntriesKeptForRemoteInputActive = new ArraySet<>();

        /* loaded from: classes.dex */
        public class RemoteInputActiveExtender extends RemoteInputExtender {
            public RemoteInputActiveExtender() {
                super();
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                if (z) {
                    if (Log.isLoggable("NotifRemoteInputManager", 3)) {
                        ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Keeping notification around while remote input active "), notificationEntry.mKey, "NotifRemoteInputManager");
                    }
                    LegacyRemoteInputLifetimeExtender.this.mEntriesKeptForRemoteInputActive.add(notificationEntry);
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mEntriesKeptForRemoteInputActive.remove(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.isRemoteInputActive(notificationEntry);
            }
        }

        /* loaded from: classes.dex */
        public abstract class RemoteInputExtender implements NotificationLifetimeExtender {
            public RemoteInputExtender() {
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final void setCallback(ScreenshotController$$ExternalSyntheticLambda3 screenshotController$$ExternalSyntheticLambda3) {
                LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender = LegacyRemoteInputLifetimeExtender.this;
                if (legacyRemoteInputLifetimeExtender.mNotificationLifetimeFinishedCallback == null) {
                    legacyRemoteInputLifetimeExtender.mNotificationLifetimeFinishedCallback = screenshotController$$ExternalSyntheticLambda3;
                }
            }
        }

        /* loaded from: classes.dex */
        public class RemoteInputHistoryExtender extends RemoteInputExtender {
            public RemoteInputHistoryExtender() {
                super();
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                boolean z2;
                if (z) {
                    RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = NotificationRemoteInputManager.this.mRebuilder;
                    Objects.requireNonNull(remoteInputNotificationRebuilder);
                    CharSequence charSequence = notificationEntry.remoteInputText;
                    if (TextUtils.isEmpty(charSequence)) {
                        charSequence = notificationEntry.remoteInputTextWhenReset;
                    }
                    StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, charSequence, false, notificationEntry.remoteInputMimeType, notificationEntry.remoteInputUri);
                    notificationEntry.lastRemoteInputSent = -2000L;
                    notificationEntry.remoteInputTextWhenReset = null;
                    if (rebuildWithRemoteInputInserted != null) {
                        NotificationEntryManager notificationEntryManager = NotificationRemoteInputManager.this.mEntryManager;
                        Objects.requireNonNull(notificationEntryManager);
                        try {
                            notificationEntryManager.updateNotificationInternal(rebuildWithRemoteInputInserted, null);
                        } catch (InflationException e) {
                            notificationEntryManager.handleInflationException(rebuildWithRemoteInputInserted, e);
                        }
                        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                        if (expandableNotificationRow == null || expandableNotificationRow.mRemoved) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (!z2) {
                            if (Log.isLoggable("NotifRemoteInputManager", 3)) {
                                ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Keeping notification around after sending remote input "), notificationEntry.mKey, "NotifRemoteInputManager");
                            }
                            LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.add(notificationEntry.mKey);
                            return;
                        }
                        return;
                    }
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.remove(notificationEntry.mKey);
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.shouldKeepForRemoteInputHistory(notificationEntry);
            }
        }

        /* loaded from: classes.dex */
        public class SmartReplyHistoryExtender extends RemoteInputExtender {
            public SmartReplyHistoryExtender() {
                super();
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
                boolean z2;
                if (z) {
                    RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = NotificationRemoteInputManager.this.mRebuilder;
                    Objects.requireNonNull(remoteInputNotificationRebuilder);
                    StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, null, false, null, null);
                    if (rebuildWithRemoteInputInserted != null) {
                        NotificationEntryManager notificationEntryManager = NotificationRemoteInputManager.this.mEntryManager;
                        Objects.requireNonNull(notificationEntryManager);
                        try {
                            notificationEntryManager.updateNotificationInternal(rebuildWithRemoteInputInserted, null);
                        } catch (InflationException e) {
                            notificationEntryManager.handleInflationException(rebuildWithRemoteInputInserted, e);
                        }
                        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                        if (expandableNotificationRow == null || expandableNotificationRow.mRemoved) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (!z2) {
                            if (Log.isLoggable("NotifRemoteInputManager", 3)) {
                                ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Keeping notification around after sending smart reply "), notificationEntry.mKey, "NotifRemoteInputManager");
                            }
                            LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.add(notificationEntry.mKey);
                            return;
                        }
                        return;
                    }
                    return;
                }
                LegacyRemoteInputLifetimeExtender.this.mKeysKeptForRemoteInputHistory.remove(notificationEntry.mKey);
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
            public final boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
                return NotificationRemoteInputManager.this.shouldKeepForSmartReplyHistory(notificationEntry);
            }
        }

        @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
        public final void onPanelCollapsed() {
            for (int i = 0; i < this.mEntriesKeptForRemoteInputActive.size(); i++) {
                NotificationEntry valueAt = this.mEntriesKeptForRemoteInputActive.valueAt(i);
                RemoteInputController remoteInputController = this.mRemoteInputController;
                if (remoteInputController != null) {
                    remoteInputController.removeRemoteInput(valueAt, null);
                }
                NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                if (notificationSafeToRemoveCallback != null) {
                    Objects.requireNonNull(valueAt);
                    ((ScreenshotController$$ExternalSyntheticLambda3) notificationSafeToRemoveCallback).onSafeToRemove(valueAt.mKey);
                }
            }
            this.mEntriesKeptForRemoteInputActive.clear();
        }

        public LegacyRemoteInputLifetimeExtender() {
            ArrayList<NotificationLifetimeExtender> arrayList = new ArrayList<>();
            this.mLifetimeExtenders = arrayList;
            arrayList.add(new RemoteInputHistoryExtender());
            arrayList.add(new SmartReplyHistoryExtender());
            arrayList.add(new RemoteInputActiveExtender());
        }

        @Override // com.android.systemui.Dumpable
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.println("LegacyRemoteInputLifetimeExtender:");
            printWriter.print("  mKeysKeptForRemoteInputHistory: ");
            printWriter.println(this.mKeysKeptForRemoteInputHistory);
            printWriter.print("  mEntriesKeptForRemoteInputActive: ");
            printWriter.println(this.mEntriesKeptForRemoteInputActive);
        }

        @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
        public final boolean isNotificationKeptForRemoteInputHistory(String str) {
            return this.mKeysKeptForRemoteInputHistory.contains(str);
        }

        @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
        public final void onRemoteInputSent(NotificationEntry notificationEntry) {
            if (NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY) {
                Objects.requireNonNull(notificationEntry);
                if (isNotificationKeptForRemoteInputHistory(notificationEntry.mKey)) {
                    ((ScreenshotController$$ExternalSyntheticLambda3) this.mNotificationLifetimeFinishedCallback).onSafeToRemove(notificationEntry.mKey);
                    return;
                }
            }
            if (this.mEntriesKeptForRemoteInputActive.contains(notificationEntry)) {
                NotificationRemoteInputManager.this.mMainHandler.postDelayed(new BiometricUnlockController$$ExternalSyntheticLambda0(this, notificationEntry, 1), 200L);
            }
        }

        @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
        public final void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry) {
            String str = notificationEntry.mKey;
            if (isNotificationKeptForRemoteInputHistory(str)) {
                NotificationRemoteInputManager.this.mMainHandler.postDelayed(new PipTaskOrganizer$$ExternalSyntheticLambda4(this, str, 2), 200L);
            }
        }

        @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
        public final void setRemoteInputController(RemoteInputController remoteInputController) {
            this.mRemoteInputController = remoteInputController;
        }

        @VisibleForTesting
        public Set<NotificationEntry> getEntriesKeptForRemoteInputActive() {
            return this.mEntriesKeptForRemoteInputActive;
        }
    }

    /* loaded from: classes.dex */
    public interface RemoteInputListener {
        boolean isNotificationKeptForRemoteInputHistory(String str);

        void onPanelCollapsed();

        void onRemoteInputSent(NotificationEntry notificationEntry);

        void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry);

        void setRemoteInputController(RemoteInputController remoteInputController);
    }

    public final boolean isRemoteInputActive() {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        return remoteInputController != null && remoteInputController.isRemoteInputActive();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v2, types: [com.android.systemui.statusbar.NotificationRemoteInputManager$$ExternalSyntheticLambda2] */
    public final boolean activateRemoteInput(final View view, final RemoteInput[] remoteInputArr, final RemoteInput remoteInput, final PendingIntent pendingIntent, final NotificationEntry.EditedSuggestionInfo editedSuggestionInfo, final String str, final AuthBypassPredicate authBypassPredicate) {
        RemoteInputView remoteInputView;
        RemoteInputView remoteInputView2;
        ExpandableNotificationRow expandableNotificationRow;
        boolean z;
        ViewParent parent = view.getParent();
        while (true) {
            remoteInputView = null;
            if (parent == null) {
                remoteInputView2 = null;
                expandableNotificationRow = null;
                break;
            }
            if (parent instanceof View) {
                View view2 = (View) parent;
                if (view2.isRootNamespace()) {
                    remoteInputView2 = (RemoteInputView) view2.findViewWithTag(RemoteInputView.VIEW_TAG);
                    expandableNotificationRow = (ExpandableNotificationRow) view2.getTag(2131428725);
                    break;
                }
            }
            parent = parent.getParent();
        }
        if (expandableNotificationRow == null) {
            return false;
        }
        expandableNotificationRow.setUserExpanded(true, false);
        if (authBypassPredicate != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z && showBouncerForRemoteInput(view, pendingIntent, expandableNotificationRow)) {
            return true;
        }
        if (remoteInputView2 != null && !remoteInputView2.isAttachedToWindow()) {
            remoteInputView2 = null;
        }
        if (remoteInputView2 == null) {
            NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
            Objects.requireNonNull(notificationContentView);
            View view3 = notificationContentView.mExpandedChild;
            if (view3 != null) {
                remoteInputView = (RemoteInputView) view3.findViewWithTag(RemoteInputView.VIEW_TAG);
            }
            if (remoteInputView == null) {
                return false;
            }
            remoteInputView2 = remoteInputView;
        }
        NotificationContentView notificationContentView2 = expandableNotificationRow.mPrivateLayout;
        Objects.requireNonNull(notificationContentView2);
        if (remoteInputView2 == notificationContentView2.mExpandedRemoteInput) {
            NotificationContentView notificationContentView3 = expandableNotificationRow.mPrivateLayout;
            Objects.requireNonNull(notificationContentView3);
            if (!notificationContentView3.mExpandedChild.isShown()) {
                this.mCallback.onMakeExpandedVisibleForRemoteInput(expandableNotificationRow, view, z, new Runnable() { // from class: com.android.systemui.statusbar.NotificationRemoteInputManager$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationRemoteInputManager notificationRemoteInputManager = NotificationRemoteInputManager.this;
                        View view4 = view;
                        RemoteInput[] remoteInputArr2 = remoteInputArr;
                        RemoteInput remoteInput2 = remoteInput;
                        PendingIntent pendingIntent2 = pendingIntent;
                        NotificationEntry.EditedSuggestionInfo editedSuggestionInfo2 = editedSuggestionInfo;
                        String str2 = str;
                        NotificationRemoteInputManager.AuthBypassPredicate authBypassPredicate2 = authBypassPredicate;
                        Objects.requireNonNull(notificationRemoteInputManager);
                        notificationRemoteInputManager.activateRemoteInput(view4, remoteInputArr2, remoteInput2, pendingIntent2, editedSuggestionInfo2, str2, authBypassPredicate2);
                    }
                });
                return true;
            }
        }
        if (!remoteInputView2.isAttachedToWindow()) {
            return false;
        }
        int width = view.getWidth();
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.getLayout() != null) {
                width = Math.min(width, textView.getCompoundPaddingRight() + textView.getCompoundPaddingLeft() + ((int) textView.getLayout().getLineWidth(0)));
            }
        }
        int left = (width / 2) + view.getLeft();
        int height = (view.getHeight() / 2) + view.getTop();
        int width2 = remoteInputView2.getWidth();
        int height2 = remoteInputView2.getHeight() - height;
        int i = width2 - left;
        int max = Math.max(Math.max(left + height, left + height2), Math.max(i + height, i + height2));
        remoteInputView2.mRevealCx = left;
        remoteInputView2.mRevealCy = height;
        remoteInputView2.mRevealR = max;
        remoteInputView2.mPendingIntent = pendingIntent;
        remoteInputView2.mViewController.setPendingIntent(pendingIntent);
        remoteInputView2.setRemoteInput(remoteInputArr, remoteInput, editedSuggestionInfo);
        remoteInputView2.mViewController.setRemoteInput(remoteInput);
        remoteInputView2.mViewController.setRemoteInputs(remoteInputArr);
        if (remoteInputView2.getVisibility() != 0) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(remoteInputView2, remoteInputView2.mRevealCx, remoteInputView2.mRevealCy, 0.0f, remoteInputView2.mRevealR);
            createCircularReveal.setDuration(360L);
            createCircularReveal.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            createCircularReveal.start();
        }
        remoteInputView2.focus();
        if (str != null) {
            remoteInputView2.mEditText.setText(str);
        }
        if (z) {
            remoteInputView2.mViewController.setBouncerChecker(new NotificationRemoteInputManager$$ExternalSyntheticLambda0(this, authBypassPredicate, view, pendingIntent, expandableNotificationRow));
        }
        return true;
    }

    public final void addControllerCallback(RemoteInputController.Callback callback) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            Objects.requireNonNull(callback);
            remoteInputController.mCallbacks.add(callback);
            return;
        }
        this.mControllerCallbacks.add(callback);
    }

    public final void closeRemoteInputs() {
        ExpandableNotificationRow expandableNotificationRow;
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            Objects.requireNonNull(remoteInputController);
            if (remoteInputController.mOpen.size() != 0) {
                ArrayList arrayList = new ArrayList(remoteInputController.mOpen.size());
                int size = remoteInputController.mOpen.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    NotificationEntry notificationEntry = (NotificationEntry) ((WeakReference) remoteInputController.mOpen.get(size).first).get();
                    if (notificationEntry != null && notificationEntry.rowExists()) {
                        arrayList.add(notificationEntry);
                    }
                }
                int size2 = arrayList.size();
                while (true) {
                    size2--;
                    if (size2 >= 0) {
                        NotificationEntry notificationEntry2 = (NotificationEntry) arrayList.get(size2);
                        if (notificationEntry2.rowExists() && (expandableNotificationRow = notificationEntry2.row) != null) {
                            expandableNotificationRow.closeRemoteInput();
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public LegacyRemoteInputLifetimeExtender createLegacyRemoteInputLifetimeExtender(Handler handler, NotificationEntryManager notificationEntryManager, SmartReplyController smartReplyController) {
        return new LegacyRemoteInputLifetimeExtender();
    }

    public final boolean isRemoteInputActive(NotificationEntry notificationEntry) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            Objects.requireNonNull(remoteInputController);
            if (remoteInputController.pruneWeakThenRemoveAndContains(notificationEntry, null, null)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isSpinning(String str) {
        RemoteInputController remoteInputController = this.mRemoteInputController;
        if (remoteInputController != null) {
            Objects.requireNonNull(remoteInputController);
            if (remoteInputController.mSpinning.containsKey(str)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public void onPerformRemoveNotification(NotificationEntry notificationEntry, String str) {
        ((LegacyRemoteInputLifetimeExtender) this.mRemoteInputListener).mKeysKeptForRemoteInputHistory.remove(str);
        if (isRemoteInputActive(notificationEntry)) {
            notificationEntry.mRemoteEditImeVisible = false;
            this.mRemoteInputController.removeRemoteInput(notificationEntry, null);
        }
    }

    public final boolean shouldKeepForRemoteInputHistory(NotificationEntry notificationEntry) {
        boolean z;
        if (!FORCE_REMOTE_INPUT_HISTORY) {
            return false;
        }
        Objects.requireNonNull(notificationEntry);
        if (!isSpinning(notificationEntry.mKey)) {
            if (SystemClock.elapsedRealtime() < notificationEntry.lastRemoteInputSent + 500) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldKeepForSmartReplyHistory(NotificationEntry notificationEntry) {
        if (!FORCE_REMOTE_INPUT_HISTORY) {
            return false;
        }
        SmartReplyController smartReplyController = this.mSmartReplyController;
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        Objects.requireNonNull(smartReplyController);
        return smartReplyController.mSendingKeys.contains(str);
    }

    public final boolean showBouncerForRemoteInput(View view, PendingIntent pendingIntent, ExpandableNotificationRow expandableNotificationRow) {
        boolean z;
        boolean z2;
        UserInfo profileParent;
        if (this.mLockscreenUserManager.shouldAllowLockscreenRemoteInput()) {
            return false;
        }
        int identifier = pendingIntent.getCreatorUserHandle().getIdentifier();
        if (!this.mUserManager.getUserInfo(identifier).isManagedProfile() || !this.mKeyguardManager.isDeviceLocked(identifier)) {
            z = false;
        } else {
            z = true;
        }
        if (!z || (profileParent = this.mUserManager.getProfileParent(identifier)) == null || !this.mKeyguardManager.isDeviceLocked(profileParent.id)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (this.mLockscreenUserManager.isLockscreenPublicMode(identifier) || this.mStatusBarStateController.getState() == 1) {
            if (!z || z2) {
                this.mCallback.onLockedRemoteInput(expandableNotificationRow, view);
            } else {
                this.mCallback.onLockedWorkRemoteInput(identifier, view);
            }
            return true;
        } else if (!z) {
            return false;
        } else {
            this.mCallback.onLockedWorkRemoteInput(identifier, view);
            return true;
        }
    }

    public NotificationRemoteInputManager(Context context, NotifPipelineFlags notifPipelineFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, SmartReplyController smartReplyController, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, Lazy<Optional<StatusBar>> lazy, StatusBarStateController statusBarStateController, Handler handler, RemoteInputUriController remoteInputUriController, NotificationClickNotifier notificationClickNotifier, ActionClickLogger actionClickLogger, DumpManager dumpManager) {
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mSmartReplyController = smartReplyController;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mStatusBarOptionalLazy = lazy;
        this.mMainHandler = handler;
        this.mLogger = actionClickLogger;
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mRebuilder = remoteInputNotificationRebuilder;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            this.mRemoteInputListener = createLegacyRemoteInputLifetimeExtender(handler, notificationEntryManager, smartReplyController);
        }
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mStatusBarStateController = statusBarStateController;
        this.mRemoteInputUriController = remoteInputUriController;
        this.mClickNotifier = notificationClickNotifier;
        dumpManager.registerDumpable(this);
        notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.NotificationRemoteInputManager.2
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
                if (z) {
                    NotificationRemoteInputManager.this.onPerformRemoveNotification(notificationEntry, notificationEntry.mKey);
                }
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public final void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationRemoteInputManager.this.mSmartReplyController.stopSending(notificationEntry);
            }
        });
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PrintWriter asIndenting = R$id.asIndenting(printWriter);
        if (this.mRemoteInputController != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mRemoteInputController: ");
            m.append(this.mRemoteInputController);
            asIndenting.println(m.toString());
            asIndenting.increaseIndent();
            RemoteInputController remoteInputController = this.mRemoteInputController;
            Objects.requireNonNull(remoteInputController);
            asIndenting.print("isRemoteInputActive: ");
            asIndenting.println(remoteInputController.isRemoteInputActive());
            asIndenting.println("mOpen: " + remoteInputController.mOpen.size());
            R$id.withIncreasedIndent(asIndenting, new CarrierTextManager$$ExternalSyntheticLambda2(remoteInputController, asIndenting, 2));
            asIndenting.println("mSpinning: " + remoteInputController.mSpinning.size());
            R$id.withIncreasedIndent(asIndenting, new CarrierTextManager$$ExternalSyntheticLambda1(remoteInputController, asIndenting, 3));
            asIndenting.println(remoteInputController.mSpinning);
            asIndenting.print("mDelegate: ");
            asIndenting.println(remoteInputController.mDelegate);
            asIndenting.decreaseIndent();
        }
        if (this.mRemoteInputListener instanceof Dumpable) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("mRemoteInputListener: ");
            m2.append(this.mRemoteInputListener.getClass().getSimpleName());
            asIndenting.println(m2.toString());
            asIndenting.increaseIndent();
            ((Dumpable) this.mRemoteInputListener).dump(fileDescriptor, asIndenting, strArr);
            asIndenting.decreaseIndent();
        }
    }
}
