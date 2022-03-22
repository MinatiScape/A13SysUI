package com.android.systemui.settings;

import android.content.pm.UserInfo;
import android.os.UserHandle;
import java.util.List;
import java.util.concurrent.Executor;
/* compiled from: UserTracker.kt */
/* loaded from: classes.dex */
public interface UserTracker extends UserContentResolverProvider, UserContextProvider {

    /* compiled from: UserTracker.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        default void onProfilesChanged() {
        }

        default void onUserChanged(int i) {
        }
    }

    void addCallback(Callback callback, Executor executor);

    UserHandle getUserHandle();

    int getUserId();

    UserInfo getUserInfo();

    List<UserInfo> getUserProfiles();

    void removeCallback(Callback callback);
}
