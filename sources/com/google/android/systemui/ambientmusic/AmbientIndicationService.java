package com.google.android.systemui.ambientmusic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AmbientIndicationService extends BroadcastReceiver {
    public final AlarmManager mAlarmManager;
    public final AmbientIndicationContainer mAmbientIndicationContainer;
    public final Context mContext;
    public boolean mStarted;
    public final AnonymousClass1 mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationService.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            AmbientIndicationService.this.onUserSwitched();
        }
    };
    public final AmbientIndicationService$$ExternalSyntheticLambda0 mHideIndicationListener = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationService$$ExternalSyntheticLambda0
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            AmbientIndicationService ambientIndicationService = AmbientIndicationService.this;
            Objects.requireNonNull(ambientIndicationService);
            AmbientIndicationContainer ambientIndicationContainer = ambientIndicationService.mAmbientIndicationContainer;
            Objects.requireNonNull(ambientIndicationContainer);
            ambientIndicationContainer.setAmbientMusic(null, null, null, 0, false, null);
        }
    };

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (!isForCurrentUser()) {
            Log.i("AmbientIndication", "Suppressing ambient, not for this user.");
            return;
        }
        int intExtra = intent.getIntExtra("com.google.android.ambientindication.extra.VERSION", 0);
        boolean z = true;
        if (intExtra != 1) {
            Log.e("AmbientIndication", "AmbientIndicationApi.EXTRA_VERSION is 1, but received an intent with version " + intExtra + ", dropping intent.");
            z = false;
        }
        if (z) {
            String action = intent.getAction();
            Objects.requireNonNull(action);
            if (action.equals("com.google.android.ambientindication.action.AMBIENT_INDICATION_HIDE")) {
                this.mAlarmManager.cancel(this.mHideIndicationListener);
                AmbientIndicationContainer ambientIndicationContainer = this.mAmbientIndicationContainer;
                Objects.requireNonNull(ambientIndicationContainer);
                ambientIndicationContainer.setAmbientMusic(null, null, null, 0, false, null);
                Log.i("AmbientIndication", "Hiding ambient indication.");
            } else if (action.equals("com.google.android.ambientindication.action.AMBIENT_INDICATION_SHOW")) {
                long min = Math.min(Math.max(intent.getLongExtra("com.google.android.ambientindication.extra.TTL_MILLIS", 180000L), 0L), 180000L);
                boolean booleanExtra = intent.getBooleanExtra("com.google.android.ambientindication.extra.SKIP_UNLOCK", false);
                int intExtra2 = intent.getIntExtra("com.google.android.ambientindication.extra.ICON_OVERRIDE", 0);
                String stringExtra = intent.getStringExtra("com.google.android.ambientindication.extra.ICON_DESCRIPTION");
                this.mAmbientIndicationContainer.setAmbientMusic(intent.getCharSequenceExtra("com.google.android.ambientindication.extra.TEXT"), (PendingIntent) intent.getParcelableExtra("com.google.android.ambientindication.extra.OPEN_INTENT"), (PendingIntent) intent.getParcelableExtra("com.google.android.ambientindication.extra.FAVORITING_INTENT"), intExtra2, booleanExtra, stringExtra);
                this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + min, "AmbientIndication", this.mHideIndicationListener, null);
                Log.i("AmbientIndication", "Showing ambient indication.");
            }
        }
    }

    public void onUserSwitched() {
        AmbientIndicationContainer ambientIndicationContainer = this.mAmbientIndicationContainer;
        Objects.requireNonNull(ambientIndicationContainer);
        ambientIndicationContainer.setAmbientMusic(null, null, null, 0, false, null);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.ambientmusic.AmbientIndicationService$1] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.ambientmusic.AmbientIndicationService$$ExternalSyntheticLambda0] */
    public AmbientIndicationService(Context context, AmbientIndicationContainer ambientIndicationContainer, AlarmManager alarmManager) {
        this.mContext = context;
        this.mAmbientIndicationContainer = ambientIndicationContainer;
        this.mAlarmManager = alarmManager;
    }

    public int getCurrentUser() {
        return KeyguardUpdateMonitor.getCurrentUser();
    }

    public boolean isForCurrentUser() {
        if (getSendingUserId() == getCurrentUser() || getSendingUserId() == -1) {
            return true;
        }
        return false;
    }
}
