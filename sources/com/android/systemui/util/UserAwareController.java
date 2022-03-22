package com.android.systemui.util;

import android.os.UserHandle;
/* compiled from: UserAwareController.kt */
/* loaded from: classes.dex */
public interface UserAwareController {
    default void changeUser(UserHandle userHandle) {
    }

    int getCurrentUserId();
}
