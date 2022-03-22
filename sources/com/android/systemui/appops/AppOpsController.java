package com.android.systemui.appops;

import com.android.systemui.privacy.PrivacyItemController$cb$1;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface AppOpsController {

    /* loaded from: classes.dex */
    public interface Callback {
        void onActiveStateChanged(int i, int i2, String str, boolean z);
    }

    void addCallback(int[] iArr, Callback callback);

    ArrayList getActiveAppOps();

    ArrayList getActiveAppOps(boolean z);

    boolean isMicMuted();

    void removeCallback(int[] iArr, PrivacyItemController$cb$1 privacyItemController$cb$1);
}
