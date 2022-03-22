package com.android.systemui.statusbar.tv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import com.android.internal.net.VpnConfig;
import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.policy.SecurityController;
/* compiled from: VpnStatusObserver.kt */
/* loaded from: classes.dex */
public final class VpnStatusObserver extends CoreStartable implements SecurityController.SecurityControllerCallback {
    public final NotificationManager notificationManager;
    public final SecurityController securityController;
    public boolean vpnConnected;
    public final Notification.Builder vpnConnectedNotificationBuilder;
    public final Notification vpnDisconnectedNotification;

    @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
    public final void onStateChanged() {
        boolean isVpnEnabled = this.securityController.isVpnEnabled();
        if (this.vpnConnected != isVpnEnabled) {
            if (isVpnEnabled) {
                NotificationManager notificationManager = this.notificationManager;
                Notification.Builder builder = this.vpnConnectedNotificationBuilder;
                String primaryVpnName = this.securityController.getPrimaryVpnName();
                if (primaryVpnName == null) {
                    primaryVpnName = this.securityController.getWorkProfileVpnName();
                }
                if (primaryVpnName != null) {
                    builder.setContentText(this.mContext.getString(2131952910, primaryVpnName));
                }
                notificationManager.notify("VpnStatusObserver", 20, builder.build());
            } else {
                NotificationManager notificationManager2 = this.notificationManager;
                notificationManager2.cancel("VpnStatusObserver", 20);
                notificationManager2.notify("VpnStatusObserver", 17, this.vpnDisconnectedNotification);
            }
            this.vpnConnected = isVpnEnabled;
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.securityController.addCallback(this);
    }

    public VpnStatusObserver(Context context, SecurityController securityController) {
        super(context);
        int i;
        this.securityController = securityController;
        NotificationManager from = NotificationManager.from(context);
        this.notificationManager = from;
        from.createNotificationChannel(new NotificationChannel("VPN Status", "VPN Status", 4));
        Notification.Builder builder = new Notification.Builder(this.mContext, "VPN Status");
        int i2 = 2131232674;
        if (securityController.isVpnBranded()) {
            i = 2131232674;
        } else {
            i = 2131232697;
        }
        this.vpnConnectedNotificationBuilder = builder.setSmallIcon(i).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setOngoing(true).setContentTitle(this.mContext.getString(2131952926)).setContentIntent(VpnConfig.getIntentForStatusPanel(this.mContext));
        this.vpnDisconnectedNotification = new Notification.Builder(this.mContext, "VPN Status").setSmallIcon(!securityController.isVpnBranded() ? 2131232697 : i2).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setTimeoutAfter(5000L).setContentTitle(this.mContext.getString(2131952927)).build();
    }
}
