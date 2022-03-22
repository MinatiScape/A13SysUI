package com.android.systemui.statusbar.notification.row;

import android.util.ArraySet;
import androidx.core.os.CancellationSignal;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BindRequester {
    public BindRequestListener mBindRequestListener;

    /* loaded from: classes.dex */
    public interface BindRequestListener {
    }

    public final CancellationSignal requestRebind(NotificationEntry notificationEntry, final NotifBindPipeline.BindCallback bindCallback) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        BindRequestListener bindRequestListener = this.mBindRequestListener;
        if (bindRequestListener != null) {
            NotifBindPipeline notifBindPipeline = ((NotifBindPipeline$$ExternalSyntheticLambda1) bindRequestListener).f$0;
            Objects.requireNonNull(notifBindPipeline);
            NotifBindPipeline.BindEntry bindEntry = (NotifBindPipeline.BindEntry) notifBindPipeline.mBindEntries.get(notificationEntry);
            if (bindEntry != null) {
                bindEntry.invalidated = true;
                if (bindCallback != null) {
                    final ArraySet arraySet = bindEntry.callbacks;
                    arraySet.add(bindCallback);
                    cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: com.android.systemui.statusbar.notification.row.NotifBindPipeline$$ExternalSyntheticLambda0
                        @Override // androidx.core.os.CancellationSignal.OnCancelListener
                        public final void onCancel() {
                            arraySet.remove(bindCallback);
                        }
                    });
                }
                notifBindPipeline.requestPipelineRun(notificationEntry);
            }
        }
        return cancellationSignal;
    }
}
