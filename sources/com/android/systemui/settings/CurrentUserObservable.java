package com.android.systemui.settings;

import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
/* loaded from: classes.dex */
public final class CurrentUserObservable {
    public final AnonymousClass1 mCurrentUser = new MutableLiveData<Integer>() { // from class: com.android.systemui.settings.CurrentUserObservable.1
        @Override // androidx.lifecycle.LiveData
        public final void onActive() {
            startTracking();
        }

        @Override // androidx.lifecycle.LiveData
        public final void onInactive() {
            stopTracking();
        }
    };
    public final AnonymousClass2 mTracker;

    public final AnonymousClass1 getCurrentUser() {
        if (getValue() == null) {
            setValue(Integer.valueOf(getCurrentUserId()));
        }
        return this.mCurrentUser;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.settings.CurrentUserObservable$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.settings.CurrentUserObservable$2] */
    public CurrentUserObservable(BroadcastDispatcher broadcastDispatcher) {
        this.mTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.settings.CurrentUserObservable.2
            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                setValue(Integer.valueOf(i));
            }
        };
    }
}
