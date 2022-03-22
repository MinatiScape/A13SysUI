package com.android.systemui.statusbar.phone.userswitcher;

import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.UserInfoController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
/* compiled from: StatusBarUserInfoTracker.kt */
/* loaded from: classes.dex */
public final class StatusBarUserInfoTracker implements CallbackController<CurrentUserChipInfoUpdatedListener>, Dumpable {
    public final Executor backgroundExecutor;
    public Drawable currentUserAvatar;
    public String currentUserName;
    public boolean listening;
    public final Executor mainExecutor;
    public final UserInfoController userInfoController;
    public final UserManager userManager;
    public boolean userSwitcherEnabled;
    public final ArrayList listeners = new ArrayList();
    public final StatusBarUserInfoTracker$userInfoChangedListener$1 userInfoChangedListener = new UserInfoController.OnUserInfoChangedListener() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker$userInfoChangedListener$1
        @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
        public final void onUserInfoChanged(String str, Drawable drawable) {
            StatusBarUserInfoTracker statusBarUserInfoTracker = StatusBarUserInfoTracker.this;
            statusBarUserInfoTracker.currentUserAvatar = drawable;
            statusBarUserInfoTracker.currentUserName = str;
            Iterator it = statusBarUserInfoTracker.listeners.iterator();
            while (it.hasNext()) {
                ((CurrentUserChipInfoUpdatedListener) it.next()).onCurrentUserChipInfoUpdated();
            }
        }
    };

    public final void addCallback(CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        if (this.listeners.isEmpty()) {
            this.listening = true;
            this.userInfoController.addCallback(this.userInfoChangedListener);
        }
        if (!this.listeners.contains(currentUserChipInfoUpdatedListener)) {
            this.listeners.add(currentUserChipInfoUpdatedListener);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.userSwitcherEnabled, "  userSwitcherEnabled=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.listening, "  listening=", printWriter);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        this.listeners.remove(currentUserChipInfoUpdatedListener);
        if (this.listeners.isEmpty()) {
            this.listening = false;
            this.userInfoController.removeCallback(this.userInfoChangedListener);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker$userInfoChangedListener$1] */
    public StatusBarUserInfoTracker(UserInfoController userInfoController, UserManager userManager, DumpManager dumpManager, Executor executor, Executor executor2) {
        this.userInfoController = userInfoController;
        this.userManager = userManager;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        dumpManager.registerDumpable("StatusBarUserInfoTracker", this);
    }
}
