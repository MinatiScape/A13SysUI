package com.android.systemui.settings;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public abstract class CurrentUserTracker {
    public CurrentUserTracker$$ExternalSyntheticLambda0 mCallback;
    public final UserReceiver mUserReceiver;

    @VisibleForTesting
    public CurrentUserTracker(UserReceiver userReceiver) {
        this.mCallback = new CurrentUserTracker$$ExternalSyntheticLambda0(this, 0);
        this.mUserReceiver = userReceiver;
    }

    public abstract void onUserSwitched(int i);

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class UserReceiver extends BroadcastReceiver {
        public static UserReceiver sInstance;
        public final BroadcastDispatcher mBroadcastDispatcher;
        public ArrayList mCallbacks = new ArrayList();
        public int mCurrentUserId;
        public boolean mReceiverRegistered;

        @VisibleForTesting
        public UserReceiver(BroadcastDispatcher broadcastDispatcher) {
            this.mBroadcastDispatcher = broadcastDispatcher;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            int intExtra;
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction()) && this.mCurrentUserId != (intExtra = intent.getIntExtra("android.intent.extra.user_handle", 0))) {
                this.mCurrentUserId = intExtra;
                Iterator it = new ArrayList(this.mCallbacks).iterator();
                while (it.hasNext()) {
                    Consumer consumer = (Consumer) it.next();
                    if (this.mCallbacks.contains(consumer)) {
                        consumer.accept(Integer.valueOf(intExtra));
                    }
                }
            }
        }
    }

    public final int getCurrentUserId() {
        UserReceiver userReceiver = this.mUserReceiver;
        Objects.requireNonNull(userReceiver);
        return userReceiver.mCurrentUserId;
    }

    public final void startTracking() {
        UserReceiver userReceiver = this.mUserReceiver;
        CurrentUserTracker$$ExternalSyntheticLambda0 currentUserTracker$$ExternalSyntheticLambda0 = this.mCallback;
        UserReceiver userReceiver2 = UserReceiver.sInstance;
        Objects.requireNonNull(userReceiver);
        if (!userReceiver.mCallbacks.contains(currentUserTracker$$ExternalSyntheticLambda0)) {
            userReceiver.mCallbacks.add(currentUserTracker$$ExternalSyntheticLambda0);
        }
        if (!userReceiver.mReceiverRegistered) {
            userReceiver.mCurrentUserId = ActivityManager.getCurrentUser();
            userReceiver.mBroadcastDispatcher.registerReceiver(userReceiver, new IntentFilter("android.intent.action.USER_SWITCHED"), null, UserHandle.ALL);
            userReceiver.mReceiverRegistered = true;
        }
    }

    public final void stopTracking() {
        UserReceiver userReceiver = this.mUserReceiver;
        CurrentUserTracker$$ExternalSyntheticLambda0 currentUserTracker$$ExternalSyntheticLambda0 = this.mCallback;
        UserReceiver userReceiver2 = UserReceiver.sInstance;
        Objects.requireNonNull(userReceiver);
        if (userReceiver.mCallbacks.contains(currentUserTracker$$ExternalSyntheticLambda0)) {
            userReceiver.mCallbacks.remove(currentUserTracker$$ExternalSyntheticLambda0);
            if (userReceiver.mCallbacks.size() == 0 && userReceiver.mReceiverRegistered) {
                userReceiver.mBroadcastDispatcher.unregisterReceiver(userReceiver);
                userReceiver.mReceiverRegistered = false;
            }
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CurrentUserTracker(BroadcastDispatcher broadcastDispatcher) {
        this(UserReceiver.sInstance);
        if (UserReceiver.sInstance == null) {
            UserReceiver.sInstance = new UserReceiver(broadcastDispatcher);
        }
    }
}
