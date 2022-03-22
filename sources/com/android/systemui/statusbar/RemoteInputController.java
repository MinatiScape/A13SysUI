package com.android.systemui.statusbar;

import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Pair;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RemoteInputController {
    public static final boolean ENABLE_REMOTE_INPUT = SystemProperties.getBoolean("debug.enable_remote_input", true);
    public final Delegate mDelegate;
    public final RemoteInputUriController mRemoteInputUriController;
    public final ArrayList<Pair<WeakReference<NotificationEntry>, Object>> mOpen = new ArrayList<>();
    public final ArrayMap<String, Object> mSpinning = new ArrayMap<>();
    public final ArrayList<Callback> mCallbacks = new ArrayList<>(3);

    /* loaded from: classes.dex */
    public interface Callback {
        default void onRemoteInputActive(boolean z) {
        }

        default void onRemoteInputSent(NotificationEntry notificationEntry) {
        }
    }

    /* loaded from: classes.dex */
    public interface Delegate {
    }

    public final boolean isRemoteInputActive() {
        pruneWeakThenRemoveAndContains(null, null, null);
        return !this.mOpen.isEmpty();
    }

    public final void apply(NotificationEntry notificationEntry) {
        Delegate delegate = this.mDelegate;
        boolean pruneWeakThenRemoveAndContains = pruneWeakThenRemoveAndContains(notificationEntry, null, null);
        NotificationStackScrollLayoutController.AnonymousClass14 r0 = (NotificationStackScrollLayoutController.AnonymousClass14) delegate;
        Objects.requireNonNull(r0);
        HeadsUpManagerPhone headsUpManagerPhone = NotificationStackScrollLayoutController.this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        Objects.requireNonNull(notificationEntry);
        HeadsUpManagerPhone.HeadsUpEntryPhone headsUpEntryPhone = (HeadsUpManagerPhone.HeadsUpEntryPhone) headsUpManagerPhone.mAlertEntries.get(notificationEntry.mKey);
        if (!(headsUpEntryPhone == null || headsUpEntryPhone.remoteInputActive == pruneWeakThenRemoveAndContains)) {
            headsUpEntryPhone.remoteInputActive = pruneWeakThenRemoveAndContains;
            if (pruneWeakThenRemoveAndContains) {
                headsUpEntryPhone.removeAutoRemovalCallbacks();
            } else {
                headsUpEntryPhone.updateEntry(false);
            }
        }
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.notifyHeightChanged(true);
        }
        NotificationStackScrollLayoutController.this.updateFooter();
        boolean isRemoteInputActive = isRemoteInputActive();
        int size = this.mCallbacks.size();
        for (int i = 0; i < size; i++) {
            this.mCallbacks.get(i).onRemoteInputActive(isRemoteInputActive);
        }
    }

    public final boolean pruneWeakThenRemoveAndContains(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, Object obj) {
        boolean z;
        boolean z2 = false;
        for (int size = this.mOpen.size() - 1; size >= 0; size--) {
            NotificationEntry notificationEntry3 = (NotificationEntry) ((WeakReference) this.mOpen.get(size).first).get();
            Object obj2 = this.mOpen.get(size).second;
            if (obj == null || obj2 == obj) {
                z = true;
            } else {
                z = false;
            }
            if (notificationEntry3 == null || (notificationEntry3 == notificationEntry2 && z)) {
                this.mOpen.remove(size);
            } else if (notificationEntry3 == notificationEntry) {
                if (obj == null || obj == obj2) {
                    z2 = true;
                } else {
                    this.mOpen.remove(size);
                }
            }
        }
        return z2;
    }

    public RemoteInputController(NotificationStackScrollLayoutController.AnonymousClass14 r3, RemoteInputUriController remoteInputUriController) {
        this.mDelegate = r3;
        this.mRemoteInputUriController = remoteInputUriController;
    }

    public final void removeRemoteInput(NotificationEntry notificationEntry, Object obj) {
        Objects.requireNonNull(notificationEntry);
        if ((!notificationEntry.mRemoteEditImeVisible || !notificationEntry.mRemoteEditImeAnimatingAway) && pruneWeakThenRemoveAndContains(notificationEntry, null, null)) {
            pruneWeakThenRemoveAndContains(null, notificationEntry, obj);
            apply(notificationEntry);
        }
    }
}
