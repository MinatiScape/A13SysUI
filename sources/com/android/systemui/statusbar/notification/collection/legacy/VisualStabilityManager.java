package com.android.systemui.statusbar.notification.collection.legacy;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import androidx.collection.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.notification.VisibilityLocationProvider;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class VisualStabilityManager implements OnHeadsUpChangedListener, Dumpable {
    public boolean mGroupChangedAllowed;
    public final Handler mHandler;
    public boolean mIsTemporaryReorderingAllowed;
    public boolean mPanelExpanded;
    public boolean mPulsing;
    public boolean mReorderingAllowed;
    public boolean mScreenOn;
    public long mTemporaryReorderingStart;
    public VisibilityLocationProvider mVisibilityLocationProvider;
    public final VisualStabilityProvider mVisualStabilityProvider;
    public final AnonymousClass3 mWakefulnessObserver;
    public final ArrayList<Callback> mReorderingAllowedCallbacks = new ArrayList<>();
    public final ArraySet<Callback> mPersistentReorderingCallbacks = new ArraySet<>(0);
    public final ArrayList<Callback> mGroupChangesAllowedCallbacks = new ArrayList<>();
    public final ArraySet<Callback> mPersistentGroupCallbacks = new ArraySet<>(0);
    public ArraySet<View> mAllowedReorderViews = new ArraySet<>(0);
    public ArraySet<NotificationEntry> mLowPriorityReorderingViews = new ArraySet<>(0);
    public ArraySet<View> mAddedChildren = new ArraySet<>(0);
    public final TaskView$$ExternalSyntheticLambda4 mOnTemporaryReorderingExpired = new TaskView$$ExternalSyntheticLambda4(this, 8);

    /* loaded from: classes.dex */
    public interface Callback {
        void onChangeAllowed();
    }

    public static void notifyChangeAllowed(ArrayList arrayList, ArraySet arraySet) {
        int i = 0;
        while (i < arrayList.size()) {
            Callback callback = (Callback) arrayList.get(i);
            callback.onChangeAllowed();
            if (!arraySet.contains(callback)) {
                arrayList.remove(callback);
                i--;
            }
            i++;
        }
    }

    public final boolean canReorderNotification(ExpandableNotificationRow expandableNotificationRow) {
        if (this.mReorderingAllowed || this.mAddedChildren.contains(expandableNotificationRow)) {
            return true;
        }
        ArraySet<NotificationEntry> arraySet = this.mLowPriorityReorderingViews;
        Objects.requireNonNull(expandableNotificationRow);
        if (arraySet.contains(expandableNotificationRow.mEntry)) {
            return true;
        }
        if (!this.mAllowedReorderViews.contains(expandableNotificationRow) || this.mVisibilityLocationProvider.isInVisibleLocation(expandableNotificationRow.mEntry)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long j;
        printWriter.println("VisualStabilityManager state:");
        printWriter.print("  mIsTemporaryReorderingAllowed=");
        printWriter.println(this.mIsTemporaryReorderingAllowed);
        printWriter.print("  mTemporaryReorderingStart=");
        printWriter.println(this.mTemporaryReorderingStart);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        printWriter.print("    Temporary reordering window has been open for ");
        if (this.mIsTemporaryReorderingAllowed) {
            j = this.mTemporaryReorderingStart;
        } else {
            j = elapsedRealtime;
        }
        printWriter.print(elapsedRealtime - j);
        printWriter.println("ms");
        printWriter.println();
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mAllowedReorderViews.add(notificationEntry.row);
        }
    }

    public final void updateAllowedStates() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if ((!this.mScreenOn || !this.mPanelExpanded || this.mIsTemporaryReorderingAllowed) && !this.mPulsing) {
            z = true;
        } else {
            z = false;
        }
        if (!z || this.mReorderingAllowed) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mReorderingAllowed = z;
        if (z2) {
            notifyChangeAllowed(this.mReorderingAllowedCallbacks, this.mPersistentReorderingCallbacks);
        }
        this.mVisualStabilityProvider.setReorderingAllowed(z);
        if ((!this.mScreenOn || !this.mPanelExpanded) && !this.mPulsing) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (!z3 || this.mGroupChangedAllowed) {
            z4 = false;
        }
        this.mGroupChangedAllowed = z3;
        if (z4) {
            notifyChangeAllowed(this.mGroupChangesAllowedCallbacks, this.mPersistentGroupCallbacks);
        }
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$3, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public VisualStabilityManager(com.android.systemui.statusbar.notification.NotificationEntryManager r3, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider r4, android.os.Handler r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.keyguard.WakefulnessLifecycle r7, com.android.systemui.dump.DumpManager r8) {
        /*
            r2 = this;
            r2.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r2.mReorderingAllowedCallbacks = r0
            androidx.collection.ArraySet r0 = new androidx.collection.ArraySet
            r1 = 0
            r0.<init>(r1)
            r2.mPersistentReorderingCallbacks = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r2.mGroupChangesAllowedCallbacks = r0
            androidx.collection.ArraySet r0 = new androidx.collection.ArraySet
            r0.<init>(r1)
            r2.mPersistentGroupCallbacks = r0
            androidx.collection.ArraySet r0 = new androidx.collection.ArraySet
            r0.<init>(r1)
            r2.mAllowedReorderViews = r0
            androidx.collection.ArraySet r0 = new androidx.collection.ArraySet
            r0.<init>(r1)
            r2.mLowPriorityReorderingViews = r0
            androidx.collection.ArraySet r0 = new androidx.collection.ArraySet
            r0.<init>(r1)
            r2.mAddedChildren = r0
            com.android.wm.shell.TaskView$$ExternalSyntheticLambda4 r0 = new com.android.wm.shell.TaskView$$ExternalSyntheticLambda4
            r1 = 8
            r0.<init>(r2, r1)
            r2.mOnTemporaryReorderingExpired = r0
            com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$3 r0 = new com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$3
            r0.<init>()
            r2.mWakefulnessObserver = r0
            r2.mVisualStabilityProvider = r4
            r2.mHandler = r5
            r8.registerDumpable(r2)
            if (r3 == 0) goto L_0x0056
            com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$1 r4 = new com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$1
            r4.<init>()
            r3.addNotificationEntryListener(r4)
        L_0x0056:
            if (r6 == 0) goto L_0x006e
            boolean r3 = r6.isPulsing()
            boolean r4 = r2.mPulsing
            if (r4 != r3) goto L_0x0061
            goto L_0x0066
        L_0x0061:
            r2.mPulsing = r3
            r2.updateAllowedStates()
        L_0x0066:
            com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$2 r3 = new com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$2
            r3.<init>()
            r6.addCallback(r3)
        L_0x006e:
            if (r7 == 0) goto L_0x0075
            java.util.ArrayList<T> r2 = r7.mObservers
            r2.add(r0)
        L_0x0075:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.<init>(com.android.systemui.statusbar.notification.NotificationEntryManager, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider, android.os.Handler, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.keyguard.WakefulnessLifecycle, com.android.systemui.dump.DumpManager):void");
    }
}
