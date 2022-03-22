package com.google.android.systemui.dreamliner;

import android.app.PendingIntent;
import android.os.ResultReceiver;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone$$ExternalSyntheticLambda0;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DockObserver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ DockObserver$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                DockObserver dockObserver = (DockObserver) this.f$0;
                ResultReceiver resultReceiver = (ResultReceiver) this.f$1;
                Objects.requireNonNull(dockObserver);
                DockIndicationController dockIndicationController = dockObserver.mIndicationController;
                Objects.requireNonNull(dockIndicationController);
                dockIndicationController.mShowPromoTimes = 0;
                dockIndicationController.mShowPromo = true;
                if (!dockIndicationController.mDozing || !dockIndicationController.mDocking) {
                    resultReceiver.send(1, null);
                    return;
                }
                dockIndicationController.showPromoInner();
                resultReceiver.send(0, null);
                return;
            case 1:
                int i = NotificationTemplateViewWrapper.$r8$clinit;
                ((PendingIntent) this.f$0).registerCancelListener((PendingIntent.CancelListener) this.f$1);
                return;
            default:
                HeadsUpManagerPhone.HeadsUpEntryPhone headsUpEntryPhone = (HeadsUpManagerPhone.HeadsUpEntryPhone) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) this.f$1;
                Objects.requireNonNull(headsUpEntryPhone);
                VisualStabilityProvider visualStabilityProvider = HeadsUpManagerPhone.this.mVisualStabilityProvider;
                Objects.requireNonNull(visualStabilityProvider);
                if (!visualStabilityProvider.isReorderingAllowed) {
                    Objects.requireNonNull(notificationEntry);
                    ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                    if (expandableNotificationRow == null || !expandableNotificationRow.showingPulsing()) {
                        z = false;
                    }
                    if (!z) {
                        HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.add(notificationEntry);
                        HeadsUpManagerPhone headsUpManagerPhone = HeadsUpManagerPhone.this;
                        VisualStabilityProvider visualStabilityProvider2 = headsUpManagerPhone.mVisualStabilityProvider;
                        HeadsUpManagerPhone$$ExternalSyntheticLambda0 headsUpManagerPhone$$ExternalSyntheticLambda0 = headsUpManagerPhone.mOnReorderingAllowedListener;
                        Objects.requireNonNull(visualStabilityProvider2);
                        if (visualStabilityProvider2.allListeners.addIfAbsent(headsUpManagerPhone$$ExternalSyntheticLambda0)) {
                            visualStabilityProvider2.temporaryListeners.add(headsUpManagerPhone$$ExternalSyntheticLambda0);
                            return;
                        }
                        return;
                    }
                }
                HeadsUpManagerPhone headsUpManagerPhone2 = HeadsUpManagerPhone.this;
                if (headsUpManagerPhone2.mTrackingHeadsUp) {
                    headsUpManagerPhone2.mEntriesToRemoveAfterExpand.add(notificationEntry);
                    return;
                }
                Objects.requireNonNull(notificationEntry);
                headsUpManagerPhone2.removeAlertEntry(notificationEntry.mKey);
                return;
        }
    }
}
