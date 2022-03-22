package com.android.systemui.statusbar.notification.row;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.android.systemui.statusbar.InflationTask;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl$$ExternalSyntheticLambda1;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RowInflaterTask implements InflationTask, AsyncLayoutInflater.OnInflateFinishedListener {
    public boolean mCancelled;
    public NotificationEntry mEntry;
    public Throwable mInflateOrigin;
    public RowInflationFinishedListener mListener;

    /* loaded from: classes.dex */
    public interface RowInflationFinishedListener {
    }

    @Override // com.android.systemui.statusbar.InflationTask
    public final void abort() {
        this.mCancelled = true;
    }

    @Override // androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener
    public final void onInflateFinished(View view, ViewGroup viewGroup) {
        if (!this.mCancelled) {
            try {
                NotificationEntry notificationEntry = this.mEntry;
                Objects.requireNonNull(notificationEntry);
                notificationEntry.mRunningTask = null;
                ((NotificationRowBinderImpl$$ExternalSyntheticLambda1) this.mListener).onInflationFinished((ExpandableNotificationRow) view);
            } catch (Throwable th) {
                if (this.mInflateOrigin != null) {
                    Log.e("RowInflaterTask", "Error in inflation finished listener: " + th, this.mInflateOrigin);
                    th.addSuppressed(this.mInflateOrigin);
                }
                throw th;
            }
        }
    }
}
