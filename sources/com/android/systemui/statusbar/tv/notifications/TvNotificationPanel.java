package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.CommandQueue;
/* loaded from: classes.dex */
public class TvNotificationPanel extends CoreStartable implements CommandQueue.Callbacks {
    public final CommandQueue mCommandQueue;
    public final String mNotificationHandlerPackage = this.mContext.getResources().getString(17039992);

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void animateCollapsePanels(int i, boolean z) {
        if (this.mNotificationHandlerPackage.isEmpty() || (i & 4) != 0) {
            openInternalNotificationPanel("android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL");
            return;
        }
        Intent intent = new Intent("android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL");
        intent.setPackage(this.mNotificationHandlerPackage);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void animateExpandNotificationsPanel() {
        if (!this.mNotificationHandlerPackage.isEmpty()) {
            startNotificationHandlerActivity(new Intent("android.app.action.OPEN_NOTIFICATION_HANDLER_PANEL"));
        } else {
            openInternalNotificationPanel("android.app.action.OPEN_NOTIFICATION_HANDLER_PANEL");
        }
    }

    public final void openInternalNotificationPanel(String str) {
        Intent intent = new Intent(this.mContext, TvNotificationPanelActivity.class);
        intent.setFlags(603979776);
        intent.setAction(str);
        this.mContext.startActivityAsUser(intent, UserHandle.SYSTEM);
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    public final void startNotificationHandlerActivity(Intent intent) {
        ActivityInfo activityInfo;
        intent.setPackage(this.mNotificationHandlerPackage);
        ResolveInfo resolveActivity = this.mContext.getPackageManager().resolveActivity(intent, 1048576);
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Not launching notification handler activity: Could not resolve activityInfo for intent ");
            m.append(intent.getAction());
            Log.e("TvNotificationPanel", m.toString());
            return;
        }
        String str = activityInfo.permission;
        if (str == null || !str.equals("android.permission.STATUS_BAR_SERVICE")) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Not launching notification handler activity: Notification handler does not require the STATUS_BAR_SERVICE permission for intent ");
            m2.append(intent.getAction());
            Log.e("TvNotificationPanel", m2.toString());
            return;
        }
        intent.setFlags(603979776);
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void togglePanel() {
        if (!this.mNotificationHandlerPackage.isEmpty()) {
            startNotificationHandlerActivity(new Intent("android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL"));
        } else {
            openInternalNotificationPanel("android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL");
        }
    }

    public TvNotificationPanel(Context context, CommandQueue commandQueue) {
        super(context);
        this.mCommandQueue = commandQueue;
    }
}
