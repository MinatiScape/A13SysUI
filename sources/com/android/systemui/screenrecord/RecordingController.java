package com.android.systemui.screenrecord;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class RecordingController implements CallbackController<RecordingStateChangeCallback> {
    public BroadcastDispatcher mBroadcastDispatcher;
    public boolean mIsRecording;
    public boolean mIsStarting;
    public PendingIntent mStopIntent;
    public UserContextProvider mUserContextProvider;
    public AnonymousClass3 mCountDownTimer = null;
    public CopyOnWriteArrayList<RecordingStateChangeCallback> mListeners = new CopyOnWriteArrayList<>();
    @VisibleForTesting
    public final BroadcastReceiver mUserChangeReceiver = new BroadcastReceiver() { // from class: com.android.systemui.screenrecord.RecordingController.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            RecordingController.this.stopRecording();
        }
    };
    @VisibleForTesting
    public final BroadcastReceiver mStateChangeReceiver = new BroadcastReceiver() { // from class: com.android.systemui.screenrecord.RecordingController.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent != null && "com.android.systemui.screenrecord.UPDATE_STATE".equals(intent.getAction())) {
                if (intent.hasExtra("extra_state")) {
                    RecordingController.this.updateState(intent.getBooleanExtra("extra_state", false));
                    return;
                }
                Log.e("RecordingController", "Received update intent with no state");
            }
        }
    };

    /* loaded from: classes.dex */
    public interface RecordingStateChangeCallback {
        default void onCountdown(long j) {
        }

        default void onCountdownEnd() {
        }

        default void onRecordingEnd() {
        }

        default void onRecordingStart() {
        }
    }

    public final synchronized boolean isRecording() {
        return this.mIsRecording;
    }

    public final synchronized void updateState(boolean z) {
        if (!z) {
            if (this.mIsRecording) {
                this.mBroadcastDispatcher.unregisterReceiver(this.mUserChangeReceiver);
                this.mBroadcastDispatcher.unregisterReceiver(this.mStateChangeReceiver);
            }
        }
        this.mIsRecording = z;
        Iterator<RecordingStateChangeCallback> it = this.mListeners.iterator();
        while (it.hasNext()) {
            RecordingStateChangeCallback next = it.next();
            if (z) {
                next.onRecordingStart();
            } else {
                next.onRecordingEnd();
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(RecordingStateChangeCallback recordingStateChangeCallback) {
        this.mListeners.add(recordingStateChangeCallback);
    }

    public final void cancelCountdown() {
        AnonymousClass3 r0 = this.mCountDownTimer;
        if (r0 != null) {
            r0.cancel();
        } else {
            Log.e("RecordingController", "Timer was null");
        }
        this.mIsStarting = false;
        Iterator<RecordingStateChangeCallback> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onCountdownEnd();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(RecordingStateChangeCallback recordingStateChangeCallback) {
        this.mListeners.remove(recordingStateChangeCallback);
    }

    public final void stopRecording() {
        try {
            PendingIntent pendingIntent = this.mStopIntent;
            if (pendingIntent != null) {
                pendingIntent.send();
            } else {
                Log.e("RecordingController", "Stop intent was null");
            }
            updateState(false);
        } catch (PendingIntent.CanceledException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error stopping: ");
            m.append(e.getMessage());
            Log.e("RecordingController", m.toString());
        }
    }

    public RecordingController(BroadcastDispatcher broadcastDispatcher, UserContextProvider userContextProvider) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserContextProvider = userContextProvider;
    }
}
