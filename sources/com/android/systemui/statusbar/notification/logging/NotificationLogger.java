package com.android.systemui.statusbar.notification.logging;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda0;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda8;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class NotificationLogger implements StatusBarStateController.StateListener {
    public final ExpansionStateLogger mExpansionStateLogger;
    public long mLastVisibilityReportUptimeMs;
    public NotificationListContainer mListContainer;
    public final NotifLiveDataStore mNotifLiveDataStore;
    public final NotificationListener mNotificationListener;
    public final NotificationPanelLogger mNotificationPanelLogger;
    public final Executor mUiBgExecutor;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final ArraySet<NotificationVisibility> mCurrentlyVisibleNotifications = new ArraySet<>();
    public Handler mHandler = new Handler();
    public final Object mDozingLock = new Object();
    @GuardedBy({"mDozingLock"})
    public Boolean mDozing = null;
    @GuardedBy({"mDozingLock"})
    public Boolean mLockscreen = null;
    public Boolean mPanelExpanded = null;
    public boolean mLogging = false;
    public final AnonymousClass1 mNotificationLocationsChangedListener = new AnonymousClass1();
    public Runnable mVisibilityReporter = new Runnable() { // from class: com.android.systemui.statusbar.notification.logging.NotificationLogger.2
        public final ArraySet<NotificationVisibility> mTmpNewlyVisibleNotifications = new ArraySet<>();
        public final ArraySet<NotificationVisibility> mTmpCurrentlyVisibleNotifications = new ArraySet<>();
        public final ArraySet<NotificationVisibility> mTmpNoLongerVisibleNotifications = new ArraySet<>();

        @Override // java.lang.Runnable
        public final void run() {
            NotificationLogger.this.mLastVisibilityReportUptimeMs = SystemClock.uptimeMillis();
            NotificationLogger notificationLogger = NotificationLogger.this;
            Objects.requireNonNull(notificationLogger);
            List list = (List) notificationLogger.mNotifLiveDataStore.getActiveNotifList().getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NotificationEntry notificationEntry = (NotificationEntry) list.get(i);
                Objects.requireNonNull(notificationEntry);
                String key = notificationEntry.mSbn.getKey();
                boolean isInVisibleLocation = NotificationLogger.this.mListContainer.isInVisibleLocation(notificationEntry);
                NotificationVisibility obtain = NotificationVisibility.obtain(key, i, size, isInVisibleLocation, NotificationLogger.getNotificationLocation(notificationEntry));
                boolean contains = NotificationLogger.this.mCurrentlyVisibleNotifications.contains(obtain);
                if (isInVisibleLocation) {
                    this.mTmpCurrentlyVisibleNotifications.add(obtain);
                    if (!contains) {
                        this.mTmpNewlyVisibleNotifications.add(obtain);
                    }
                } else {
                    obtain.recycle();
                }
            }
            this.mTmpNoLongerVisibleNotifications.addAll((ArraySet<? extends NotificationVisibility>) NotificationLogger.this.mCurrentlyVisibleNotifications);
            this.mTmpNoLongerVisibleNotifications.removeAll((ArraySet<? extends NotificationVisibility>) this.mTmpCurrentlyVisibleNotifications);
            NotificationLogger.this.logNotificationVisibilityChanges(this.mTmpNewlyVisibleNotifications, this.mTmpNoLongerVisibleNotifications);
            NotificationLogger.recycleAllVisibilityObjects(NotificationLogger.this.mCurrentlyVisibleNotifications);
            NotificationLogger.this.mCurrentlyVisibleNotifications.addAll((ArraySet<? extends NotificationVisibility>) this.mTmpCurrentlyVisibleNotifications);
            ExpansionStateLogger expansionStateLogger = NotificationLogger.this.mExpansionStateLogger;
            ArraySet<NotificationVisibility> arraySet = this.mTmpCurrentlyVisibleNotifications;
            expansionStateLogger.onVisibilityChanged(arraySet, arraySet);
            Trace.traceCounter(4096L, "Notifications [Active]", size);
            Trace.traceCounter(4096L, "Notifications [Visible]", NotificationLogger.this.mCurrentlyVisibleNotifications.size());
            NotificationLogger notificationLogger2 = NotificationLogger.this;
            ArraySet<NotificationVisibility> arraySet2 = this.mTmpNoLongerVisibleNotifications;
            Objects.requireNonNull(notificationLogger2);
            NotificationLogger.recycleAllVisibilityObjects(arraySet2);
            this.mTmpCurrentlyVisibleNotifications.clear();
            this.mTmpNewlyVisibleNotifications.clear();
            this.mTmpNoLongerVisibleNotifications.clear();
        }
    };
    public IStatusBarService mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));

    /* renamed from: com.android.systemui.statusbar.notification.logging.NotificationLogger$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements OnChildLocationsChangedListener {
        public AnonymousClass1() {
        }
    }

    /* loaded from: classes.dex */
    public static class ExpansionStateLogger {
        public final Executor mUiBgExecutor;
        public final ArrayMap mExpansionStates = new ArrayMap();
        public final ArrayMap mLoggedExpansionState = new ArrayMap();
        @VisibleForTesting
        public IStatusBarService mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));

        /* loaded from: classes.dex */
        public static class State {
            public Boolean mIsExpanded;
            public Boolean mIsUserAction;
            public Boolean mIsVisible;
            public NotificationVisibility.NotificationLocation mLocation;

            public State() {
            }

            public State(int i) {
            }

            public State(State state) {
                this.mIsUserAction = state.mIsUserAction;
                this.mIsExpanded = state.mIsExpanded;
                this.mIsVisible = state.mIsVisible;
                this.mLocation = state.mLocation;
            }
        }

        public final State getState(String str) {
            State state = (State) this.mExpansionStates.get(str);
            if (state != null) {
                return state;
            }
            State state2 = new State(0);
            this.mExpansionStates.put(str, state2);
            return state2;
        }

        public final void maybeNotifyOnNotificationExpansionChanged(String str, State state) {
            boolean z;
            if (state.mIsUserAction == null || state.mIsExpanded == null || state.mIsVisible == null || state.mLocation == null) {
                z = false;
            } else {
                z = true;
            }
            if (z && state.mIsVisible.booleanValue()) {
                Boolean bool = (Boolean) this.mLoggedExpansionState.get(str);
                if (bool == null && !state.mIsExpanded.booleanValue()) {
                    return;
                }
                if (bool == null || state.mIsExpanded != bool) {
                    this.mLoggedExpansionState.put(str, state.mIsExpanded);
                    this.mUiBgExecutor.execute(new TaskView$$ExternalSyntheticLambda8(this, str, new State(state), 2));
                }
            }
        }

        @VisibleForTesting
        public void onEntryRemoved(String str) {
            this.mExpansionStates.remove(str);
            this.mLoggedExpansionState.remove(str);
        }

        @VisibleForTesting
        public void onEntryUpdated(String str) {
            this.mLoggedExpansionState.remove(str);
        }

        public ExpansionStateLogger(Executor executor) {
            this.mUiBgExecutor = executor;
        }

        @VisibleForTesting
        public void onExpansionChanged(String str, boolean z, boolean z2, NotificationVisibility.NotificationLocation notificationLocation) {
            State state = getState(str);
            state.mIsUserAction = Boolean.valueOf(z);
            state.mIsExpanded = Boolean.valueOf(z2);
            state.mLocation = notificationLocation;
            maybeNotifyOnNotificationExpansionChanged(str, state);
        }

        @VisibleForTesting
        public void onVisibilityChanged(Collection<NotificationVisibility> collection, Collection<NotificationVisibility> collection2) {
            NotificationVisibility[] cloneVisibilitiesAsArr = NotificationLogger.cloneVisibilitiesAsArr(collection);
            NotificationVisibility[] cloneVisibilitiesAsArr2 = NotificationLogger.cloneVisibilitiesAsArr(collection2);
            for (NotificationVisibility notificationVisibility : cloneVisibilitiesAsArr) {
                State state = getState(notificationVisibility.key);
                state.mIsVisible = Boolean.TRUE;
                state.mLocation = notificationVisibility.location;
                maybeNotifyOnNotificationExpansionChanged(notificationVisibility.key, state);
            }
            for (NotificationVisibility notificationVisibility2 : cloneVisibilitiesAsArr2) {
                getState(notificationVisibility2.key).mIsVisible = Boolean.FALSE;
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnChildLocationsChangedListener {
    }

    public static NotificationVisibility.NotificationLocation getNotificationLocation(NotificationEntry notificationEntry) {
        ExpandableNotificationRow expandableNotificationRow;
        if (!(notificationEntry == null || (expandableNotificationRow = notificationEntry.row) == null)) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mViewState != null) {
                ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                Objects.requireNonNull(expandableNotificationRow2);
                int i = expandableNotificationRow2.mViewState.location;
                if (i == 1) {
                    return NotificationVisibility.NotificationLocation.LOCATION_FIRST_HEADS_UP;
                }
                if (i == 2) {
                    return NotificationVisibility.NotificationLocation.LOCATION_HIDDEN_TOP;
                }
                if (i == 4) {
                    return NotificationVisibility.NotificationLocation.LOCATION_MAIN_AREA;
                }
                if (i == 8) {
                    return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_PEEKING;
                }
                if (i == 16) {
                    return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_HIDDEN;
                }
                if (i != 64) {
                    return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
                }
                return NotificationVisibility.NotificationLocation.LOCATION_GONE;
            }
        }
        return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
    }

    @GuardedBy({"mDozingLock"})
    public final void maybeUpdateLoggingStatus() {
        boolean z;
        if (this.mPanelExpanded != null && this.mDozing != null) {
            Boolean bool = this.mLockscreen;
            if (bool == null) {
                z = false;
            } else {
                z = bool.booleanValue();
            }
            if (!this.mPanelExpanded.booleanValue() || this.mDozing.booleanValue()) {
                stopNotificationLogging();
                return;
            }
            this.mNotificationPanelLogger.logPanelShown(z, (List) this.mNotifLiveDataStore.getActiveNotifList().getValue());
            if (!this.mLogging) {
                this.mLogging = true;
                this.mListContainer.setChildLocationsChangedListener(this.mNotificationLocationsChangedListener);
                AnonymousClass1 r4 = this.mNotificationLocationsChangedListener;
                Objects.requireNonNull(r4);
                NotificationLogger notificationLogger = NotificationLogger.this;
                if (!notificationLogger.mHandler.hasCallbacks(notificationLogger.mVisibilityReporter)) {
                    NotificationLogger notificationLogger2 = NotificationLogger.this;
                    notificationLogger2.mHandler.postAtTime(notificationLogger2.mVisibilityReporter, notificationLogger2.mLastVisibilityReportUptimeMs + 500);
                }
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        synchronized (this.mDozingLock) {
            this.mDozing = Boolean.valueOf(z);
            maybeUpdateLoggingStatus();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        synchronized (this.mDozingLock) {
            boolean z = true;
            if (!(i == 1 || i == 2)) {
                z = false;
            }
            this.mLockscreen = Boolean.valueOf(z);
        }
    }

    public final void stopNotificationLogging() {
        if (this.mLogging) {
            this.mLogging = false;
            if (!this.mCurrentlyVisibleNotifications.isEmpty()) {
                logNotificationVisibilityChanges(Collections.emptyList(), this.mCurrentlyVisibleNotifications);
                recycleAllVisibilityObjects(this.mCurrentlyVisibleNotifications);
            }
            this.mHandler.removeCallbacks(this.mVisibilityReporter);
            this.mListContainer.setChildLocationsChangedListener(null);
        }
    }

    public NotificationLogger(NotificationListener notificationListener, Executor executor, NotifPipelineFlags notifPipelineFlags, NotifLiveDataStore notifLiveDataStore, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, StatusBarStateController statusBarStateController, ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        this.mNotificationListener = notificationListener;
        this.mUiBgExecutor = executor;
        this.mNotifLiveDataStore = notifLiveDataStore;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mExpansionStateLogger = expansionStateLogger;
        this.mNotificationPanelLogger = notificationPanelLogger;
        statusBarStateController.addCallback(this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.logging.NotificationLogger.4
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                    ExpansionStateLogger expansionStateLogger2 = NotificationLogger.this.mExpansionStateLogger;
                    Objects.requireNonNull(notificationEntry);
                    expansionStateLogger2.onEntryRemoved(notificationEntry.mKey);
                }

                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
                    ExpansionStateLogger expansionStateLogger2 = NotificationLogger.this.mExpansionStateLogger;
                    Objects.requireNonNull(notificationEntry);
                    expansionStateLogger2.onEntryUpdated(notificationEntry.mKey);
                }
            });
        } else {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.logging.NotificationLogger.3
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
                    NotificationLogger.this.mExpansionStateLogger.onEntryRemoved(notificationEntry.mKey);
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onInflationError(StatusBarNotification statusBarNotification, Exception exc) {
                    NotificationLogger notificationLogger = NotificationLogger.this;
                    Objects.requireNonNull(notificationLogger);
                    try {
                        notificationLogger.mBarService.onNotificationError(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), exc.getMessage(), statusBarNotification.getUserId());
                    } catch (RemoteException unused) {
                    }
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onPreEntryUpdated(NotificationEntry notificationEntry) {
                    NotificationLogger.this.mExpansionStateLogger.onEntryUpdated(notificationEntry.mKey);
                }
            });
        }
    }

    public static NotificationVisibility[] cloneVisibilitiesAsArr(Collection<NotificationVisibility> collection) {
        NotificationVisibility[] notificationVisibilityArr = new NotificationVisibility[collection.size()];
        int i = 0;
        for (NotificationVisibility notificationVisibility : collection) {
            if (notificationVisibility != null) {
                notificationVisibilityArr[i] = notificationVisibility.clone();
            }
            i++;
        }
        return notificationVisibilityArr;
    }

    public static void recycleAllVisibilityObjects(ArraySet arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            ((NotificationVisibility) arraySet.valueAt(i)).recycle();
        }
        arraySet.clear();
    }

    public final void logNotificationVisibilityChanges(Collection collection, ArraySet arraySet) {
        if (!collection.isEmpty() || !arraySet.isEmpty()) {
            this.mUiBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(this, cloneVisibilitiesAsArr(collection), cloneVisibilitiesAsArr(arraySet), 1));
        }
    }

    @VisibleForTesting
    public void setVisibilityReporter(Runnable runnable) {
        this.mVisibilityReporter = runnable;
    }

    @VisibleForTesting
    public Runnable getVisibilityReporter() {
        return this.mVisibilityReporter;
    }
}
