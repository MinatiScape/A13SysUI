package com.android.systemui.statusbar.tv.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public final class TvNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public SparseArray<StatusBarNotification> mNotifications;

    /* loaded from: classes.dex */
    public static class TvNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mDetails;
        public PendingIntent mPendingIntent;
        public final TextView mTitle;

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            try {
                PendingIntent pendingIntent = this.mPendingIntent;
                if (pendingIntent != null) {
                    pendingIntent.send();
                }
            } catch (PendingIntent.CanceledException unused) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pending intent canceled for : ");
                m.append(this.mPendingIntent);
                Log.d("TvNotificationAdapter", m.toString());
            }
        }

        public TvNotificationViewHolder(View view) {
            super(view);
            this.mTitle = (TextView) view.findViewById(2131429118);
            this.mDetails = (TextView) view.findViewById(2131429116);
            view.setOnClickListener(this);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        SparseArray<StatusBarNotification> sparseArray = this.mNotifications;
        if (sparseArray == null) {
            return 0;
        }
        return sparseArray.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final long getItemId(int i) {
        return this.mNotifications.keyAt(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        SparseArray<StatusBarNotification> sparseArray = this.mNotifications;
        if (sparseArray == null) {
            Log.e("TvNotificationAdapter", "Could not bind view holder because the notification is missing");
            return;
        }
        TvNotificationViewHolder tvNotificationViewHolder = (TvNotificationViewHolder) viewHolder;
        Notification notification = sparseArray.valueAt(i).getNotification();
        tvNotificationViewHolder.mTitle.setText(notification.extras.getString("android.title"));
        tvNotificationViewHolder.mDetails.setText(notification.extras.getString("android.text"));
        tvNotificationViewHolder.mPendingIntent = notification.contentIntent;
    }

    public TvNotificationAdapter() {
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        return new TvNotificationViewHolder(LayoutInflater.from(recyclerView.getContext()).inflate(2131624630, (ViewGroup) recyclerView, false));
    }
}
