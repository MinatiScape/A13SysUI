package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes.dex */
public final class ManagedProfileControllerImpl implements ManagedProfileController {
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Context mContext;
    public int mCurrentUser;
    public boolean mListening;
    public final UserManager mUserManager;
    public final ArrayList mCallbacks = new ArrayList();
    public final AnonymousClass1 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.ManagedProfileControllerImpl.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            ManagedProfileControllerImpl.this.reloadManagedProfiles();
            Iterator it = ManagedProfileControllerImpl.this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ManagedProfileController.Callback) it.next()).onManagedProfileChanged();
            }
        }
    };
    public final LinkedList<UserInfo> mProfiles = new LinkedList<>();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(ManagedProfileController.Callback callback) {
        ManagedProfileController.Callback callback2 = callback;
        this.mCallbacks.add(callback2);
        if (this.mCallbacks.size() == 1) {
            setListening(true);
        }
        callback2.onManagedProfileChanged();
    }

    @Override // com.android.systemui.statusbar.phone.ManagedProfileController
    public final boolean hasActiveProfile() {
        boolean z;
        if (!this.mListening) {
            reloadManagedProfiles();
        }
        synchronized (this.mProfiles) {
            if (this.mProfiles.size() > 0) {
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    @Override // com.android.systemui.statusbar.phone.ManagedProfileController
    public final boolean isWorkModeEnabled() {
        if (!this.mListening) {
            reloadManagedProfiles();
        }
        synchronized (this.mProfiles) {
            Iterator<UserInfo> it = this.mProfiles.iterator();
            while (it.hasNext()) {
                if (it.next().isQuietModeEnabled()) {
                    return false;
                }
            }
            return true;
        }
    }

    public final void reloadManagedProfiles() {
        boolean z;
        synchronized (this.mProfiles) {
            if (this.mProfiles.size() > 0) {
                z = true;
            } else {
                z = false;
            }
            int currentUser = ActivityManager.getCurrentUser();
            this.mProfiles.clear();
            for (UserInfo userInfo : this.mUserManager.getEnabledProfiles(currentUser)) {
                if (userInfo.isManagedProfile()) {
                    this.mProfiles.add(userInfo);
                }
            }
            if (this.mProfiles.size() == 0 && z && currentUser == this.mCurrentUser) {
                Iterator it = this.mCallbacks.iterator();
                while (it.hasNext()) {
                    ((ManagedProfileController.Callback) it.next()).onManagedProfileRemoved();
                }
            }
            this.mCurrentUser = currentUser;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(ManagedProfileController.Callback callback) {
        if (this.mCallbacks.remove(callback) && this.mCallbacks.size() == 0) {
            setListening(false);
        }
    }

    public final void setListening(boolean z) {
        this.mListening = z;
        if (z) {
            reloadManagedProfiles();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
            this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter, null, UserHandle.ALL);
            return;
        }
        this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
    }

    @Override // com.android.systemui.statusbar.phone.ManagedProfileController
    public final void setWorkModeEnabled(boolean z) {
        boolean z2;
        synchronized (this.mProfiles) {
            Iterator<UserInfo> it = this.mProfiles.iterator();
            while (it.hasNext()) {
                UserInfo next = it.next();
                UserManager userManager = this.mUserManager;
                if (!z) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!userManager.requestQuietModeEnabled(z2, UserHandle.of(next.id))) {
                    ((StatusBarManager) this.mContext.getSystemService("statusbar")).collapsePanels();
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.phone.ManagedProfileControllerImpl$1] */
    public ManagedProfileControllerImpl(Context context, BroadcastDispatcher broadcastDispatcher) {
        this.mContext = context;
        this.mUserManager = UserManager.get(context);
        this.mBroadcastDispatcher = broadcastDispatcher;
    }
}
