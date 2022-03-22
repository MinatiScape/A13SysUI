package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.graphics.Region;
import android.os.SystemClock;
import android.util.Pools;
import androidx.collection.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.OnReorderingAllowedListener;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
/* loaded from: classes.dex */
public final class HeadsUpManagerPhone extends HeadsUpManager implements Dumpable, OnHeadsUpChangedListener {
    public AnimationStateHandler mAnimationStateHandler;
    public final KeyguardBypassController mBypassController;
    public final GroupMembershipManager mGroupMembershipManager;
    public boolean mHeadsUpGoingAway;
    public int mHeadsUpInset;
    public boolean mIsExpanded;
    public boolean mReleaseOnExpandFinish;
    public int mStatusBarState;
    public final AnonymousClass3 mStatusBarStateListener;
    public boolean mTrackingHeadsUp;
    public final VisualStabilityProvider mVisualStabilityProvider;
    public final ArrayList mHeadsUpPhoneListeners = new ArrayList();
    public final HashSet<String> mSwipedOutKeys = new HashSet<>();
    public final HashSet<NotificationEntry> mEntriesToRemoveAfterExpand = new HashSet<>();
    public final ArraySet<NotificationEntry> mEntriesToRemoveWhenReorderingAllowed = new ArraySet<>(0);
    public final Region mTouchableRegion = new Region();
    public final AnonymousClass1 mEntryPool = new AnonymousClass1();
    public final HeadsUpManagerPhone$$ExternalSyntheticLambda0 mOnReorderingAllowedListener = new OnReorderingAllowedListener() { // from class: com.android.systemui.statusbar.phone.HeadsUpManagerPhone$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.notification.collection.provider.OnReorderingAllowedListener
        public final void onReorderingAllowed() {
            HeadsUpManagerPhone headsUpManagerPhone = HeadsUpManagerPhone.this;
            Objects.requireNonNull(headsUpManagerPhone);
            NotificationStackScrollLayoutController$$ExternalSyntheticLambda3 notificationStackScrollLayoutController$$ExternalSyntheticLambda3 = (NotificationStackScrollLayoutController$$ExternalSyntheticLambda3) headsUpManagerPhone.mAnimationStateHandler;
            Objects.requireNonNull(notificationStackScrollLayoutController$$ExternalSyntheticLambda3);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController$$ExternalSyntheticLambda3.f$0;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mHeadsUpGoingAwayAnimationsAllowed = false;
            ArraySet<NotificationEntry> arraySet = headsUpManagerPhone.mEntriesToRemoveWhenReorderingAllowed;
            Objects.requireNonNull(arraySet);
            ArraySet.ElementIterator elementIterator = new ArraySet.ElementIterator();
            while (elementIterator.hasNext()) {
                NotificationEntry notificationEntry = (NotificationEntry) elementIterator.next();
                Objects.requireNonNull(notificationEntry);
                if (headsUpManagerPhone.isAlerting(notificationEntry.mKey)) {
                    headsUpManagerPhone.removeAlertEntry(notificationEntry.mKey);
                }
            }
            headsUpManagerPhone.mEntriesToRemoveWhenReorderingAllowed.clear();
            NotificationStackScrollLayoutController$$ExternalSyntheticLambda3 notificationStackScrollLayoutController$$ExternalSyntheticLambda32 = (NotificationStackScrollLayoutController$$ExternalSyntheticLambda3) headsUpManagerPhone.mAnimationStateHandler;
            Objects.requireNonNull(notificationStackScrollLayoutController$$ExternalSyntheticLambda32);
            NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController$$ExternalSyntheticLambda32.f$0;
            Objects.requireNonNull(notificationStackScrollLayout2);
            notificationStackScrollLayout2.mHeadsUpGoingAwayAnimationsAllowed = true;
        }
    };
    @VisibleForTesting
    public final int mExtensionTime = this.mContext.getResources().getInteger(2131492866);

    /* renamed from: com.android.systemui.statusbar.phone.HeadsUpManagerPhone$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Pools.Pool<HeadsUpEntryPhone> {
        public Stack<HeadsUpEntryPhone> mPoolObjects = new Stack<>();

        public AnonymousClass1() {
        }

        public final Object acquire() {
            if (!this.mPoolObjects.isEmpty()) {
                return this.mPoolObjects.pop();
            }
            return new HeadsUpEntryPhone();
        }

        public final boolean release(Object obj) {
            this.mPoolObjects.push((HeadsUpEntryPhone) obj);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public interface AnimationStateHandler {
    }

    /* loaded from: classes.dex */
    public class HeadsUpEntryPhone extends HeadsUpManager.HeadsUpEntry {
        public boolean extended;
        public boolean mMenuShownPinned;

        public HeadsUpEntryPhone() {
            super();
        }

        @Override // com.android.systemui.statusbar.AlertingNotificationManager.AlertEntry
        public final void setEntry(NotificationEntry notificationEntry) {
            DockObserver$$ExternalSyntheticLambda0 dockObserver$$ExternalSyntheticLambda0 = new DockObserver$$ExternalSyntheticLambda0(this, notificationEntry, 2);
            this.mEntry = notificationEntry;
            this.mRemoveAlertRunnable = dockObserver$$ExternalSyntheticLambda0;
            this.mPostTime = calculatePostTime();
            updateEntry(true);
        }

        @Override // com.android.systemui.statusbar.policy.HeadsUpManager.HeadsUpEntry
        public final void setExpanded(boolean z) {
            if (this.expanded != z) {
                this.expanded = z;
                if (z) {
                    removeAutoRemovalCallbacks();
                } else {
                    updateEntry(false);
                }
            }
        }

        @Override // com.android.systemui.statusbar.policy.HeadsUpManager.HeadsUpEntry, com.android.systemui.statusbar.AlertingNotificationManager.AlertEntry
        public final long calculateFinishTime() {
            int i;
            long calculateFinishTime = super.calculateFinishTime();
            if (this.extended) {
                i = HeadsUpManagerPhone.this.mExtensionTime;
            } else {
                i = 0;
            }
            return calculateFinishTime + i;
        }

        @Override // com.android.systemui.statusbar.policy.HeadsUpManager.HeadsUpEntry, com.android.systemui.statusbar.AlertingNotificationManager.AlertEntry
        public final boolean isSticky() {
            if (super.isSticky() || this.mMenuShownPinned) {
                return true;
            }
            return false;
        }

        @Override // com.android.systemui.statusbar.policy.HeadsUpManager.HeadsUpEntry, com.android.systemui.statusbar.AlertingNotificationManager.AlertEntry
        public final void reset() {
            super.reset();
            this.mMenuShownPinned = false;
            this.extended = false;
        }

        @Override // com.android.systemui.statusbar.AlertingNotificationManager.AlertEntry
        public final void updateEntry(boolean z) {
            super.updateEntry(z);
            if (HeadsUpManagerPhone.this.mEntriesToRemoveAfterExpand.contains(this.mEntry)) {
                HeadsUpManagerPhone.this.mEntriesToRemoveAfterExpand.remove(this.mEntry);
            }
            if (HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.contains(this.mEntry)) {
                HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.remove(this.mEntry);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnHeadsUpPhoneListenerChange {
        void onHeadsUpGoingAwayStateChanged(boolean z);
    }

    @Override // com.android.systemui.statusbar.AlertingNotificationManager
    public final boolean canRemoveImmediately(String str) {
        if (this.mSwipedOutKeys.contains(str)) {
            this.mSwipedOutKeys.remove(str);
            return true;
        }
        HeadsUpEntryPhone headsUpEntryPhone = (HeadsUpEntryPhone) this.mAlertEntries.get(str);
        HeadsUpEntryPhone headsUpEntryPhone2 = (HeadsUpEntryPhone) getTopHeadsUpEntry();
        if (headsUpEntryPhone == null || headsUpEntryPhone != headsUpEntryPhone2 || super.canRemoveImmediately(str)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.HeadsUpManager, com.android.systemui.statusbar.AlertingNotificationManager
    public final HeadsUpManager.HeadsUpEntry createAlertEntry() {
        return (HeadsUpManager.HeadsUpEntry) this.mEntryPool.acquire();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("HeadsUpManagerPhone state:");
        printWriter.print("  mTouchAcceptanceDelay=");
        printWriter.println(this.mTouchAcceptanceDelay);
        printWriter.print("  mSnoozeLengthMs=");
        printWriter.println(this.mSnoozeLengthMs);
        printWriter.print("  now=");
        Objects.requireNonNull(this.mClock);
        printWriter.println(SystemClock.elapsedRealtime());
        printWriter.print("  mUser=");
        printWriter.println(this.mUser);
        for (AlertingNotificationManager.AlertEntry alertEntry : this.mAlertEntries.values()) {
            printWriter.print("  HeadsUpEntry=");
            printWriter.println(alertEntry.mEntry);
        }
        int size = this.mSnoozedPackages.size();
        printWriter.println("  snoozed packages: " + size);
        for (int i = 0; i < size; i++) {
            printWriter.print("    ");
            printWriter.print(this.mSnoozedPackages.valueAt(i));
            printWriter.print(", ");
            printWriter.println(this.mSnoozedPackages.keyAt(i));
        }
        printWriter.print("  mBarState=");
        printWriter.println(this.mStatusBarState);
        printWriter.print("  mTouchableRegion=");
        printWriter.println(this.mTouchableRegion);
    }

    public final void setHeadsUpGoingAway(boolean z) {
        if (z != this.mHeadsUpGoingAway) {
            this.mHeadsUpGoingAway = z;
            Iterator it = this.mHeadsUpPhoneListeners.iterator();
            while (it.hasNext()) {
                ((OnHeadsUpPhoneListenerChange) it.next()).onHeadsUpGoingAwayStateChanged(z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.AlertingNotificationManager, com.android.systemui.statusbar.NotificationLifetimeExtender
    public final boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        VisualStabilityProvider visualStabilityProvider = this.mVisualStabilityProvider;
        Objects.requireNonNull(visualStabilityProvider);
        if (!visualStabilityProvider.isReorderingAllowed || !(!canRemoveImmediately(notificationEntry.mKey))) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.HeadsUpManager
    public final boolean shouldHeadsUpBecomePinned(NotificationEntry notificationEntry) {
        boolean z;
        boolean z2;
        if (this.mStatusBarState != 0 || this.mIsExpanded) {
            z = false;
        } else {
            z = true;
        }
        if (this.mBypassController.getBypassEnabled()) {
            if (this.mStatusBarState == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            z |= z2;
        }
        if (z || HeadsUpManager.hasFullScreenIntent(notificationEntry)) {
            return true;
        }
        return false;
    }

    public final void updateResources() {
        Resources resources = this.mContext.getResources();
        this.mHeadsUpInset = resources.getDimensionPixelSize(2131165797) + SystemBarUtils.getStatusBarHeight(this.mContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.android.systemui.statusbar.phone.HeadsUpManagerPhone$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r2v8, types: [com.android.systemui.statusbar.phone.HeadsUpManagerPhone$3, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public HeadsUpManagerPhone(android.content.Context r2, com.android.systemui.statusbar.policy.HeadsUpManagerLogger r3, com.android.systemui.plugins.statusbar.StatusBarStateController r4, com.android.systemui.statusbar.phone.KeyguardBypassController r5, com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r6, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider r7, com.android.systemui.statusbar.policy.ConfigurationController r8) {
        /*
            r1 = this;
            r1.<init>(r2, r3)
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r1.mHeadsUpPhoneListeners = r2
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            r1.mSwipedOutKeys = r2
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            r1.mEntriesToRemoveAfterExpand = r2
            androidx.collection.ArraySet r2 = new androidx.collection.ArraySet
            r3 = 0
            r2.<init>(r3)
            r1.mEntriesToRemoveWhenReorderingAllowed = r2
            android.graphics.Region r2 = new android.graphics.Region
            r2.<init>()
            r1.mTouchableRegion = r2
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone$1 r2 = new com.android.systemui.statusbar.phone.HeadsUpManagerPhone$1
            r2.<init>()
            r1.mEntryPool = r2
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone$$ExternalSyntheticLambda0 r2 = new com.android.systemui.statusbar.phone.HeadsUpManagerPhone$$ExternalSyntheticLambda0
            r2.<init>()
            r1.mOnReorderingAllowedListener = r2
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone$3 r2 = new com.android.systemui.statusbar.phone.HeadsUpManagerPhone$3
            r2.<init>()
            r1.mStatusBarStateListener = r2
            android.content.Context r3 = r1.mContext
            android.content.res.Resources r3 = r3.getResources()
            r0 = 2131492866(0x7f0c0002, float:1.8609196E38)
            int r3 = r3.getInteger(r0)
            r1.mExtensionTime = r3
            r4.addCallback(r2)
            r1.mBypassController = r5
            r1.mGroupMembershipManager = r6
            r1.mVisualStabilityProvider = r7
            r1.updateResources()
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone$2 r2 = new com.android.systemui.statusbar.phone.HeadsUpManagerPhone$2
            r2.<init>()
            r8.addCallback(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.HeadsUpManagerPhone.<init>(android.content.Context, com.android.systemui.statusbar.policy.HeadsUpManagerLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.phone.KeyguardBypassController, com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider, com.android.systemui.statusbar.policy.ConfigurationController):void");
    }

    @Override // com.android.systemui.statusbar.policy.HeadsUpManager, com.android.systemui.statusbar.AlertingNotificationManager
    public final void onAlertEntryRemoved(AlertingNotificationManager.AlertEntry alertEntry) {
        super.onAlertEntryRemoved(alertEntry);
        this.mEntryPool.release((HeadsUpEntryPhone) alertEntry);
    }

    public final void setMenuShown(NotificationEntry notificationEntry, boolean z) {
        Objects.requireNonNull(notificationEntry);
        HeadsUpManager.HeadsUpEntry headsUpEntry = getHeadsUpEntry(notificationEntry.mKey);
        if ((headsUpEntry instanceof HeadsUpEntryPhone) && notificationEntry.isRowPinned()) {
            HeadsUpEntryPhone headsUpEntryPhone = (HeadsUpEntryPhone) headsUpEntry;
            Objects.requireNonNull(headsUpEntryPhone);
            if (headsUpEntryPhone.mMenuShownPinned != z) {
                headsUpEntryPhone.mMenuShownPinned = z;
                if (z) {
                    headsUpEntryPhone.removeAutoRemovalCallbacks();
                } else {
                    headsUpEntryPhone.updateEntry(false);
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.HeadsUpManager
    public final void snooze() {
        super.snooze();
        this.mReleaseOnExpandFinish = true;
    }

    @Override // com.android.systemui.statusbar.policy.HeadsUpManager
    public final boolean isTrackingHeadsUp() {
        return this.mTrackingHeadsUp;
    }
}
