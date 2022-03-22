package com.android.systemui.volume;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.IAudioService;
import android.media.IVolumeController;
import android.media.MediaMetadata;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.VolumePolicy;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.service.notification.Condition;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.annotations.GuardedBy;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.settingslib.volume.D;
import com.android.settingslib.volume.MediaSessions;
import com.android.settingslib.volume.Util;
import com.android.systemui.Dumpable;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.QuickAccessWalletTile$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda6;
import com.android.systemui.util.RingerModeLiveData;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public final class VolumeDialogControllerImpl implements VolumeDialogController, Dumpable {
    public static final ArrayMap<Integer, Integer> STREAMS;
    public AudioManager mAudio;
    public IAudioService mAudioService;
    public final Context mContext;
    public final boolean mHasVibrator;
    public long mLastToggledRingerOn;
    public final MediaSessions mMediaSessions;
    public final MediaSessionsCallbacks mMediaSessionsCallbacksW;
    public final NotificationManager mNoMan;
    public final PackageManager mPackageManager;
    public final RingerModeObservers mRingerModeObservers;
    public final MediaRouter2Manager mRouter2Manager;
    public boolean mShowA11yStream;
    public boolean mShowSafetyWarning;
    public boolean mShowVolumeDialog;
    public final VolumeDialogController.State mState;
    @GuardedBy({"this"})
    public UserActivityListener mUserActivityListener;
    public final VibratorHelper mVibrator;
    public final VC mVolumeController;
    public VolumePolicy mVolumePolicy;
    public final AnonymousClass1 mWakefullnessLifecycleObserver;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final W mWorker;
    public static final String TAG = Util.logTag(VolumeDialogControllerImpl.class);
    public static final AudioAttributes SONIFICIATION_VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public C mCallbacks = new C();
    public boolean mDeviceInteractive = true;
    public boolean mShowDndTile = true;

    /* loaded from: classes.dex */
    public class C implements VolumeDialogController.Callbacks {
        public final ConcurrentHashMap mCallbackMap = new ConcurrentHashMap();

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onAccessibilityModeChanged(Boolean bool) {
            final boolean z;
            if (bool == null) {
                z = false;
            } else {
                z = bool.booleanValue();
            }
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.10
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onAccessibilityModeChanged(Boolean.valueOf(z));
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onCaptionComponentStateChanged(Boolean bool, final Boolean bool2) {
            final boolean z;
            if (bool == null) {
                z = false;
            } else {
                z = bool.booleanValue();
            }
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl$C$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        Map.Entry entry2 = entry;
                        boolean z2 = z;
                        ((VolumeDialogController.Callbacks) entry2.getKey()).onCaptionComponentStateChanged(Boolean.valueOf(z2), bool2);
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onConfigurationChanged() {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onConfigurationChanged();
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onDismissRequested(final int i) {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onDismissRequested(i);
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onLayoutDirectionChanged(final int i) {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onLayoutDirectionChanged(i);
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onScreenOff() {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onScreenOff();
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowRequested(final int i) {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onShowRequested(i);
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowSafetyWarning(final int i) {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.9
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onShowSafetyWarning(i);
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowSilentHint() {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.7
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onShowSilentHint();
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowVibrateHint() {
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.6
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onShowVibrateHint();
                    }
                });
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onStateChanged(VolumeDialogController.State state) {
            System.currentTimeMillis();
            final VolumeDialogController.State copy = state.copy();
            for (final Map.Entry entry : this.mCallbackMap.entrySet()) {
                ((Handler) entry.getValue()).post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogControllerImpl.C.3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((VolumeDialogController.Callbacks) entry.getKey()).onStateChanged(copy);
                    }
                });
            }
            String str = Events.TAG;
        }
    }

    /* loaded from: classes.dex */
    public final class MediaSessionsCallbacks implements MediaSessions.Callbacks {
        public final boolean mVolumeAdjustmentForRemoteGroupSessions;
        public final HashMap<MediaSession.Token, Integer> mRemoteStreams = new HashMap<>();
        public int mNextStream = 100;

        public MediaSessionsCallbacks(Context context) {
            this.mVolumeAdjustmentForRemoteGroupSessions = context.getResources().getBoolean(17891813);
        }

        public final void addStream(MediaSession.Token token, String str) {
            synchronized (this.mRemoteStreams) {
                if (!this.mRemoteStreams.containsKey(token)) {
                    this.mRemoteStreams.put(token, Integer.valueOf(this.mNextStream));
                    String str2 = VolumeDialogControllerImpl.TAG;
                    Log.d(str2, str + ": added stream " + this.mNextStream + " from token + " + token.toString());
                    this.mNextStream = this.mNextStream + 1;
                }
            }
        }

        public final boolean showForSession(MediaSession.Token token) {
            boolean z;
            if (this.mVolumeAdjustmentForRemoteGroupSessions) {
                return true;
            }
            String packageName = new MediaController(VolumeDialogControllerImpl.this.mContext, token).getPackageName();
            Iterator it = VolumeDialogControllerImpl.this.mRouter2Manager.getRoutingSessions(packageName).iterator();
            boolean z2 = false;
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                RoutingSessionInfo routingSessionInfo = (RoutingSessionInfo) it.next();
                if (!routingSessionInfo.isSystemSession()) {
                    if (routingSessionInfo.getSelectedRoutes().size() > 1) {
                        z = true;
                        z2 = true;
                        break;
                    }
                    z2 = true;
                }
            }
            if (z2) {
                return !z;
            }
            DialogFragment$$ExternalSyntheticOutline0.m("No routing session for ", packageName, VolumeDialogControllerImpl.TAG);
            return false;
        }

        public final void onRemoteRemoved(MediaSession.Token token) {
            if (showForSession(token)) {
                synchronized (this.mRemoteStreams) {
                    if (!this.mRemoteStreams.containsKey(token)) {
                        String str = VolumeDialogControllerImpl.TAG;
                        Log.d(str, "onRemoteRemoved: stream doesn't exist, aborting remote removed for token:" + token.toString());
                        return;
                    }
                    int intValue = this.mRemoteStreams.get(token).intValue();
                    VolumeDialogControllerImpl.this.mState.states.remove(intValue);
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    if (volumeDialogControllerImpl.mState.activeStream == intValue) {
                        volumeDialogControllerImpl.updateActiveStreamW(-1);
                    }
                    VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl2.mCallbacks.onStateChanged(volumeDialogControllerImpl2.mState);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public final class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean z = false;
            if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                int intExtra = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int intExtra2 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                int intExtra3 = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
                if (D.BUG) {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(GridLayoutManager$$ExternalSyntheticOutline0.m("onReceive VOLUME_CHANGED_ACTION stream=", intExtra, " level=", intExtra2, " oldLevel="), intExtra3, VolumeDialogControllerImpl.TAG);
                }
                z = VolumeDialogControllerImpl.this.updateStreamLevelW(intExtra, intExtra2);
            } else if (action.equals("android.media.STREAM_DEVICES_CHANGED_ACTION")) {
                int intExtra4 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int intExtra5 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_DEVICES", -1);
                int intExtra6 = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_DEVICES", -1);
                if (D.BUG) {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(GridLayoutManager$$ExternalSyntheticOutline0.m("onReceive STREAM_DEVICES_CHANGED_ACTION stream=", intExtra4, " devices=", intExtra5, " oldDevices="), intExtra6, VolumeDialogControllerImpl.TAG);
                }
                z = VolumeDialogControllerImpl.this.checkRoutedToBluetoothW(intExtra4) | VolumeDialogControllerImpl.this.onVolumeChangedW(intExtra4, 0);
            } else if (action.equals("android.media.STREAM_MUTE_CHANGED_ACTION")) {
                int intExtra7 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                boolean booleanExtra = intent.getBooleanExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", false);
                if (D.BUG) {
                    String str = VolumeDialogControllerImpl.TAG;
                    Log.d(str, "onReceive STREAM_MUTE_CHANGED_ACTION stream=" + intExtra7 + " muted=" + booleanExtra);
                }
                z = VolumeDialogControllerImpl.this.updateStreamMuteW(intExtra7, booleanExtra);
            } else if (action.equals("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_EFFECTS_SUPPRESSOR_CHANGED");
                }
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                z = volumeDialogControllerImpl.updateEffectsSuppressorW(volumeDialogControllerImpl.mNoMan.getEffectsSuppressor());
            } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_CONFIGURATION_CHANGED");
                }
                VolumeDialogControllerImpl.this.mCallbacks.onConfigurationChanged();
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_SCREEN_OFF");
                }
                VolumeDialogControllerImpl.this.mCallbacks.onScreenOff();
            } else if (action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_CLOSE_SYSTEM_DIALOGS");
                }
                VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                Objects.requireNonNull(volumeDialogControllerImpl2);
                volumeDialogControllerImpl2.mCallbacks.onDismissRequested(2);
            }
            if (z) {
                VolumeDialogControllerImpl volumeDialogControllerImpl3 = VolumeDialogControllerImpl.this;
                volumeDialogControllerImpl3.mCallbacks.onStateChanged(volumeDialogControllerImpl3.mState);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class RingerModeObservers {
        public final RingerModeLiveData mRingerMode;
        public final RingerModeLiveData mRingerModeInternal;
        public final AnonymousClass1 mRingerModeObserver = new AnonymousClass1();
        public final AnonymousClass2 mRingerModeInternalObserver = new AnonymousClass2();

        /* renamed from: com.android.systemui.volume.VolumeDialogControllerImpl$RingerModeObservers$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 implements Observer<Integer> {
            public AnonymousClass1() {
            }

            @Override // androidx.lifecycle.Observer
            public final void onChanged(Integer num) {
                VolumeDialogControllerImpl.this.mWorker.post(new NavBarTuner$$ExternalSyntheticLambda6(this, num, 5));
            }
        }

        /* renamed from: com.android.systemui.volume.VolumeDialogControllerImpl$RingerModeObservers$2  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass2 implements Observer<Integer> {
            public AnonymousClass2() {
            }

            @Override // androidx.lifecycle.Observer
            public final void onChanged(Integer num) {
                VolumeDialogControllerImpl.this.mWorker.post(new QuickAccessWalletTile$$ExternalSyntheticLambda0(this, num, 4));
            }
        }

        public RingerModeObservers(RingerModeLiveData ringerModeLiveData, RingerModeLiveData ringerModeLiveData2) {
            this.mRingerMode = ringerModeLiveData;
            this.mRingerModeInternal = ringerModeLiveData2;
        }
    }

    /* loaded from: classes.dex */
    public final class SettingObserver extends ContentObserver {
        public final Uri ZEN_MODE_URI = Settings.Global.getUriFor("zen_mode");
        public final Uri ZEN_MODE_CONFIG_URI = Settings.Global.getUriFor("zen_mode_config_etag");

        public SettingObserver(W w) {
            super(w);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            boolean z2;
            if (this.ZEN_MODE_URI.equals(uri)) {
                z2 = VolumeDialogControllerImpl.this.updateZenModeW();
            } else {
                z2 = false;
            }
            if (this.ZEN_MODE_CONFIG_URI.equals(uri)) {
                z2 |= VolumeDialogControllerImpl.this.updateZenConfig();
            }
            if (z2) {
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface UserActivityListener {
    }

    /* loaded from: classes.dex */
    public final class VC extends IVolumeController.Stub {
        public final String TAG = MotionController$$ExternalSyntheticOutline1.m(new StringBuilder(), VolumeDialogControllerImpl.TAG, ".VC");

        public VC() {
        }

        public final void dismiss() throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "dismiss requested");
            }
            Objects.requireNonNull(VolumeDialogControllerImpl.this);
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(2, 2, 0).sendToTarget();
            VolumeDialogControllerImpl.this.mWorker.sendEmptyMessage(2);
        }

        public final void displaySafeVolumeWarning(int i) throws RemoteException {
            if (D.BUG) {
                String str = this.TAG;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("displaySafeVolumeWarning ");
                m.append(Util.audioManagerFlagsToString(i));
                Log.d(str, m.toString());
            }
            Objects.requireNonNull(VolumeDialogControllerImpl.this);
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(14, i, 0).sendToTarget();
        }

        public final void masterMuteChanged(int i) throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "masterMuteChanged");
            }
        }

        public final void setA11yMode(int i) {
            if (D.BUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("setA11yMode to ", i, this.TAG);
            }
            Objects.requireNonNull(VolumeDialogControllerImpl.this);
            if (i == 0) {
                VolumeDialogControllerImpl.this.mShowA11yStream = false;
            } else if (i != 1) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Invalid accessibility mode ", i, this.TAG);
            } else {
                VolumeDialogControllerImpl.this.mShowA11yStream = true;
            }
            VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
            volumeDialogControllerImpl.mWorker.obtainMessage(15, Boolean.valueOf(volumeDialogControllerImpl.mShowA11yStream)).sendToTarget();
        }

        public final void setLayoutDirection(int i) throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "setLayoutDirection");
            }
            Objects.requireNonNull(VolumeDialogControllerImpl.this);
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(8, i, 0).sendToTarget();
        }

        public final void volumeChanged(int i, int i2) throws RemoteException {
            if (D.BUG) {
                String str = this.TAG;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("volumeChanged ");
                m.append(AudioSystem.streamToString(i));
                m.append(" ");
                m.append(Util.audioManagerFlagsToString(i2));
                Log.d(str, m.toString());
            }
            Objects.requireNonNull(VolumeDialogControllerImpl.this);
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(1, i, i2).sendToTarget();
        }
    }

    /* loaded from: classes.dex */
    public final class W extends Handler {
        public W(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            boolean z = false;
            boolean z2 = true;
            int i = 100;
            Uri uri = null;
            MediaSession.Token token = null;
            switch (message.what) {
                case 1:
                    VolumeDialogControllerImpl.this.onVolumeChangedW(message.arg1, message.arg2);
                    return;
                case 2:
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    int i2 = message.arg1;
                    Objects.requireNonNull(volumeDialogControllerImpl);
                    volumeDialogControllerImpl.mCallbacks.onDismissRequested(i2);
                    return;
                case 3:
                    VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                    Objects.requireNonNull(volumeDialogControllerImpl2);
                    for (Integer num : VolumeDialogControllerImpl.STREAMS.keySet()) {
                        int intValue = num.intValue();
                        volumeDialogControllerImpl2.updateStreamLevelW(intValue, volumeDialogControllerImpl2.mAudio.getLastAudibleStreamVolume(intValue));
                        volumeDialogControllerImpl2.streamStateW(intValue).levelMin = volumeDialogControllerImpl2.mAudio.getStreamMinVolumeInt(intValue);
                        volumeDialogControllerImpl2.streamStateW(intValue).levelMax = Math.max(1, volumeDialogControllerImpl2.mAudio.getStreamMaxVolume(intValue));
                        volumeDialogControllerImpl2.updateStreamMuteW(intValue, volumeDialogControllerImpl2.mAudio.isStreamMute(intValue));
                        VolumeDialogController.StreamState streamStateW = volumeDialogControllerImpl2.streamStateW(intValue);
                        streamStateW.muteSupported = volumeDialogControllerImpl2.mAudio.isStreamAffectedByMute(intValue);
                        streamStateW.name = VolumeDialogControllerImpl.STREAMS.get(Integer.valueOf(intValue)).intValue();
                        volumeDialogControllerImpl2.checkRoutedToBluetoothW(intValue);
                    }
                    int intValue2 = volumeDialogControllerImpl2.mRingerModeObservers.mRingerMode.getValue().intValue();
                    VolumeDialogController.State state = volumeDialogControllerImpl2.mState;
                    if (intValue2 != state.ringerModeExternal) {
                        state.ringerModeExternal = intValue2;
                        Events.writeEvent(12, Integer.valueOf(intValue2));
                    }
                    volumeDialogControllerImpl2.updateZenModeW();
                    volumeDialogControllerImpl2.updateZenConfig();
                    volumeDialogControllerImpl2.updateEffectsSuppressorW(volumeDialogControllerImpl2.mNoMan.getEffectsSuppressor());
                    volumeDialogControllerImpl2.mCallbacks.onStateChanged(volumeDialogControllerImpl2.mState);
                    return;
                case 4:
                    VolumeDialogControllerImpl volumeDialogControllerImpl3 = VolumeDialogControllerImpl.this;
                    int i3 = message.arg1;
                    if (message.arg2 != 0) {
                        z = true;
                    }
                    Objects.requireNonNull(volumeDialogControllerImpl3);
                    if (z) {
                        volumeDialogControllerImpl3.mAudio.setRingerMode(i3);
                        return;
                    } else {
                        volumeDialogControllerImpl3.mAudio.setRingerModeInternal(i3);
                        return;
                    }
                case 5:
                    VolumeDialogControllerImpl volumeDialogControllerImpl4 = VolumeDialogControllerImpl.this;
                    int i4 = message.arg1;
                    Objects.requireNonNull(volumeDialogControllerImpl4);
                    if (D.BUG) {
                        ExifInterface$$ExternalSyntheticOutline1.m("onSetZenModeW ", i4, VolumeDialogControllerImpl.TAG);
                    }
                    volumeDialogControllerImpl4.mNoMan.setZenMode(i4, null, VolumeDialogControllerImpl.TAG);
                    return;
                case FalsingManager.VERSION /* 6 */:
                    VolumeDialogControllerImpl volumeDialogControllerImpl5 = VolumeDialogControllerImpl.this;
                    Condition condition = (Condition) message.obj;
                    Objects.requireNonNull(volumeDialogControllerImpl5);
                    NotificationManager notificationManager = volumeDialogControllerImpl5.mNoMan;
                    int i5 = volumeDialogControllerImpl5.mState.zenMode;
                    if (condition != null) {
                        uri = condition.id;
                    }
                    notificationManager.setZenMode(i5, uri, VolumeDialogControllerImpl.TAG);
                    return;
                case 7:
                    VolumeDialogControllerImpl volumeDialogControllerImpl6 = VolumeDialogControllerImpl.this;
                    int i6 = message.arg1;
                    if (message.arg2 == 0) {
                        z2 = false;
                    }
                    Objects.requireNonNull(volumeDialogControllerImpl6);
                    AudioManager audioManager = volumeDialogControllerImpl6.mAudio;
                    if (z2) {
                        i = -100;
                    }
                    audioManager.adjustStreamVolume(i6, i, 0);
                    return;
                case 8:
                    VolumeDialogControllerImpl.this.mCallbacks.onLayoutDirectionChanged(message.arg1);
                    return;
                case 9:
                    VolumeDialogControllerImpl.this.mCallbacks.onConfigurationChanged();
                    return;
                case 10:
                    VolumeDialogControllerImpl volumeDialogControllerImpl7 = VolumeDialogControllerImpl.this;
                    int i7 = message.arg1;
                    int i8 = message.arg2;
                    Objects.requireNonNull(volumeDialogControllerImpl7);
                    if (D.BUG) {
                        Log.d(VolumeDialogControllerImpl.TAG, "onSetStreamVolume " + i7 + " level=" + i8);
                    }
                    if (i7 >= 100) {
                        MediaSessionsCallbacks mediaSessionsCallbacks = volumeDialogControllerImpl7.mMediaSessionsCallbacksW;
                        Objects.requireNonNull(mediaSessionsCallbacks);
                        synchronized (mediaSessionsCallbacks.mRemoteStreams) {
                            Iterator<Map.Entry<MediaSession.Token, Integer>> it = mediaSessionsCallbacks.mRemoteStreams.entrySet().iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    Map.Entry<MediaSession.Token, Integer> next = it.next();
                                    if (next.getValue().equals(Integer.valueOf(i7))) {
                                        token = next.getKey();
                                    }
                                }
                            }
                        }
                        if (token == null) {
                            GridLayoutManager$$ExternalSyntheticOutline1.m("setStreamVolume: No token found for stream: ", i7, VolumeDialogControllerImpl.TAG);
                            return;
                        } else if (mediaSessionsCallbacks.showForSession(token)) {
                            MediaSessions mediaSessions = VolumeDialogControllerImpl.this.mMediaSessions;
                            Objects.requireNonNull(mediaSessions);
                            MediaSessions.MediaControllerRecord mediaControllerRecord = (MediaSessions.MediaControllerRecord) mediaSessions.mRecords.get(token);
                            if (mediaControllerRecord == null) {
                                Log.w(MediaSessions.TAG, "setVolume: No record found for token " + token);
                                return;
                            }
                            if (D.BUG) {
                                ExifInterface$$ExternalSyntheticOutline1.m("Setting level to ", i8, MediaSessions.TAG);
                            }
                            mediaControllerRecord.controller.setVolumeTo(i8, 0);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        volumeDialogControllerImpl7.mAudio.setStreamVolume(i7, i8, 0);
                        return;
                    }
                case QSTileImpl.H.STALE /* 11 */:
                    VolumeDialogControllerImpl volumeDialogControllerImpl8 = VolumeDialogControllerImpl.this;
                    int i9 = message.arg1;
                    Objects.requireNonNull(volumeDialogControllerImpl8);
                    if (volumeDialogControllerImpl8.updateActiveStreamW(i9)) {
                        volumeDialogControllerImpl8.mCallbacks.onStateChanged(volumeDialogControllerImpl8.mState);
                        return;
                    }
                    return;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    VolumeDialogControllerImpl volumeDialogControllerImpl9 = VolumeDialogControllerImpl.this;
                    if (message.arg1 != 0) {
                        z = true;
                    }
                    Objects.requireNonNull(volumeDialogControllerImpl9);
                    volumeDialogControllerImpl9.mAudio.notifyVolumeControllerVisible(volumeDialogControllerImpl9.mVolumeController, z);
                    if (!z && volumeDialogControllerImpl9.updateActiveStreamW(-1)) {
                        volumeDialogControllerImpl9.mCallbacks.onStateChanged(volumeDialogControllerImpl9.mState);
                        return;
                    }
                    return;
                case QS.VERSION /* 13 */:
                    VolumeDialogControllerImpl volumeDialogControllerImpl10 = VolumeDialogControllerImpl.this;
                    Objects.requireNonNull(volumeDialogControllerImpl10);
                    synchronized (volumeDialogControllerImpl10) {
                        UserActivityListener userActivityListener = volumeDialogControllerImpl10.mUserActivityListener;
                        if (userActivityListener != null) {
                            ((VolumeDialogComponent) userActivityListener).mKeyguardViewMediator.userActivity();
                        }
                    }
                    return;
                case 14:
                    VolumeDialogControllerImpl volumeDialogControllerImpl11 = VolumeDialogControllerImpl.this;
                    int i10 = message.arg1;
                    Objects.requireNonNull(volumeDialogControllerImpl11);
                    if (volumeDialogControllerImpl11.mShowSafetyWarning) {
                        volumeDialogControllerImpl11.mCallbacks.onShowSafetyWarning(i10);
                        return;
                    }
                    return;
                case 15:
                    VolumeDialogControllerImpl volumeDialogControllerImpl12 = VolumeDialogControllerImpl.this;
                    Objects.requireNonNull(volumeDialogControllerImpl12);
                    volumeDialogControllerImpl12.mCallbacks.onAccessibilityModeChanged((Boolean) message.obj);
                    return;
                case 16:
                    VolumeDialogControllerImpl volumeDialogControllerImpl13 = VolumeDialogControllerImpl.this;
                    boolean booleanValue = ((Boolean) message.obj).booleanValue();
                    Objects.requireNonNull(volumeDialogControllerImpl13);
                    try {
                        String string = volumeDialogControllerImpl13.mContext.getString(17039931);
                        if (TextUtils.isEmpty(string)) {
                            volumeDialogControllerImpl13.mCallbacks.onCaptionComponentStateChanged(Boolean.FALSE, Boolean.valueOf(booleanValue));
                            return;
                        }
                        if (D.BUG) {
                            Log.i(VolumeDialogControllerImpl.TAG, String.format("isCaptionsServiceEnabled componentNameString=%s", string));
                        }
                        ComponentName unflattenFromString = ComponentName.unflattenFromString(string);
                        if (unflattenFromString == null) {
                            volumeDialogControllerImpl13.mCallbacks.onCaptionComponentStateChanged(Boolean.FALSE, Boolean.valueOf(booleanValue));
                            return;
                        }
                        C c = volumeDialogControllerImpl13.mCallbacks;
                        if (volumeDialogControllerImpl13.mPackageManager.getComponentEnabledSetting(unflattenFromString) == 1) {
                            z = true;
                        }
                        c.onCaptionComponentStateChanged(Boolean.valueOf(z), Boolean.valueOf(booleanValue));
                        return;
                    } catch (Exception e) {
                        Log.e(VolumeDialogControllerImpl.TAG, "isCaptionsServiceEnabled failed to check for captions component", e);
                        volumeDialogControllerImpl13.mCallbacks.onCaptionComponentStateChanged(Boolean.FALSE, Boolean.valueOf(booleanValue));
                        return;
                    }
                default:
                    return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.Object, com.android.systemui.volume.VolumeDialogControllerImpl$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public VolumeDialogControllerImpl(android.content.Context r16, com.android.systemui.broadcast.BroadcastDispatcher r17, com.android.systemui.util.RingerModeTracker r18, com.android.systemui.util.concurrency.ThreadFactory r19, android.media.AudioManager r20, android.app.NotificationManager r21, com.android.systemui.statusbar.VibratorHelper r22, android.media.IAudioService r23, android.view.accessibility.AccessibilityManager r24, android.content.pm.PackageManager r25, com.android.systemui.keyguard.WakefulnessLifecycle r26) {
        /*
            Method dump skipped, instructions count: 254
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogControllerImpl.<init>(android.content.Context, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.util.RingerModeTracker, com.android.systemui.util.concurrency.ThreadFactory, android.media.AudioManager, android.app.NotificationManager, com.android.systemui.statusbar.VibratorHelper, android.media.IAudioService, android.view.accessibility.AccessibilityManager, android.content.pm.PackageManager, com.android.systemui.keyguard.WakefulnessLifecycle):void");
    }

    public final boolean checkRoutedToBluetoothW(int i) {
        boolean z;
        if (i != 3) {
            return false;
        }
        boolean z2 = true;
        if ((this.mAudio.getDevicesForStream(3) & 896) != 0) {
            z = true;
        } else {
            z = false;
        }
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        if (streamStateW.routedToBluetooth == z) {
            z2 = false;
        } else {
            streamStateW.routedToBluetooth = z;
            if (D.BUG) {
                Log.d(TAG, "updateStreamRoutedToBluetoothW stream=" + i + " routedToBluetooth=" + z);
            }
        }
        return false | z2;
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final boolean isCaptionStreamOptedOut() {
        return false;
    }

    static {
        ArrayMap<Integer, Integer> arrayMap = new ArrayMap<>();
        STREAMS = arrayMap;
        arrayMap.put(4, 2131953326);
        arrayMap.put(6, 2131953327);
        arrayMap.put(8, 2131953328);
        arrayMap.put(3, 2131953329);
        arrayMap.put(10, 2131953325);
        arrayMap.put(5, 2131953330);
        arrayMap.put(2, 2131953331);
        arrayMap.put(1, 2131953332);
        arrayMap.put(7, 2131953333);
        arrayMap.put(9, 2131953334);
        arrayMap.put(0, 2131953335);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void addCallback(VolumeDialogController.Callbacks callbacks, Handler handler) {
        C c = this.mCallbacks;
        Objects.requireNonNull(c);
        if (callbacks == null || handler == null) {
            throw new IllegalArgumentException();
        }
        c.mCallbackMap.put(callbacks, handler);
        callbacks.onAccessibilityModeChanged(Boolean.valueOf(this.mShowA11yStream));
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final boolean areCaptionsEnabled() {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "odi_captions_enabled", 0, -2) == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("VolumeDialogControllerImpl state:");
        printWriter.print("  mDestroyed: ");
        int i = 0;
        printWriter.println(false);
        printWriter.print("  mVolumePolicy: ");
        printWriter.println(this.mVolumePolicy);
        printWriter.print("  mState: ");
        printWriter.println(this.mState.toString(4));
        printWriter.print("  mShowDndTile: ");
        printWriter.println(this.mShowDndTile);
        printWriter.print("  mHasVibrator: ");
        printWriter.println(this.mHasVibrator);
        synchronized (this.mMediaSessionsCallbacksW.mRemoteStreams) {
            printWriter.print("  mRemoteStreams: ");
            printWriter.println(this.mMediaSessionsCallbacksW.mRemoteStreams.values());
        }
        printWriter.print("  mShowA11yStream: ");
        printWriter.println(this.mShowA11yStream);
        printWriter.println();
        MediaSessions mediaSessions = this.mMediaSessions;
        Objects.requireNonNull(mediaSessions);
        printWriter.println("MediaSessions state:");
        printWriter.print("  mInit: ");
        printWriter.println(mediaSessions.mInit);
        printWriter.print("  mRecords.size: ");
        printWriter.println(mediaSessions.mRecords.size());
        for (MediaSessions.MediaControllerRecord mediaControllerRecord : mediaSessions.mRecords.values()) {
            i++;
            MediaController mediaController = mediaControllerRecord.controller;
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("  Controller ", i, ": ");
            m.append(mediaController.getPackageName());
            printWriter.println(m.toString());
            Bundle extras = mediaController.getExtras();
            long flags = mediaController.getFlags();
            MediaMetadata metadata = mediaController.getMetadata();
            MediaController.PlaybackInfo playbackInfo = mediaController.getPlaybackInfo();
            PlaybackState playbackState = mediaController.getPlaybackState();
            List<MediaSession.QueueItem> queue = mediaController.getQueue();
            CharSequence queueTitle = mediaController.getQueueTitle();
            int ratingType = mediaController.getRatingType();
            PendingIntent sessionActivity = mediaController.getSessionActivity();
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    PlaybackState: ");
            m2.append(Util.playbackStateToString(playbackState));
            printWriter.println(m2.toString());
            printWriter.println("    PlaybackInfo: " + Util.playbackInfoToString(playbackInfo));
            if (metadata != null) {
                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("  MediaMetadata.desc=");
                m3.append(metadata.getDescription());
                printWriter.println(m3.toString());
            }
            printWriter.println("    RatingType: " + ratingType);
            printWriter.println("    Flags: " + flags);
            if (extras != null) {
                printWriter.println("    Extras:");
                for (String str : extras.keySet()) {
                    StringBuilder m4 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("      ", str, "=");
                    m4.append(extras.get(str));
                    printWriter.println(m4.toString());
                }
            }
            if (queueTitle != null) {
                printWriter.println("    QueueTitle: " + ((Object) queueTitle));
            }
            if (queue != null && !queue.isEmpty()) {
                printWriter.println("    Queue:");
                Iterator<MediaSession.QueueItem> it = queue.iterator();
                while (it.hasNext()) {
                    printWriter.println("      " + it.next());
                }
            }
            if (playbackInfo != null) {
                printWriter.println("    sessionActivity: " + sessionActivity);
            }
        }
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void getCaptionsComponentState(boolean z) {
        this.mWorker.obtainMessage(16, Boolean.valueOf(z)).sendToTarget();
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void getState() {
        this.mWorker.sendEmptyMessage(3);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void notifyVisible(boolean z) {
        this.mWorker.obtainMessage(12, z ? 1 : 0, 0).sendToTarget();
    }

    public final boolean onVolumeChangedW(int i, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        int i3 = wakefulnessLifecycle.mWakefulness;
        int i4 = 3;
        if (i3 == 0 || i3 == 3 || !this.mDeviceInteractive || (i2 & 1) == 0 || !this.mShowVolumeDialog) {
            z = false;
        } else {
            z = true;
        }
        if ((i2 & 4096) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((i2 & 2048) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        if ((i2 & 128) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (z) {
            z5 = updateActiveStreamW(i) | false;
        } else {
            z5 = false;
        }
        int lastAudibleStreamVolume = this.mAudio.getLastAudibleStreamVolume(i);
        boolean updateStreamLevelW = z5 | updateStreamLevelW(i, lastAudibleStreamVolume);
        if (!z) {
            i4 = i;
        }
        boolean checkRoutedToBluetoothW = checkRoutedToBluetoothW(i4) | updateStreamLevelW;
        if (checkRoutedToBluetoothW) {
            this.mCallbacks.onStateChanged(this.mState);
        }
        if (z) {
            this.mCallbacks.onShowRequested(1);
        }
        if (z3) {
            this.mCallbacks.onShowVibrateHint();
        }
        if (z4) {
            this.mCallbacks.onShowSilentHint();
        }
        if (checkRoutedToBluetoothW && z2) {
            Events.writeEvent(4, Integer.valueOf(i), Integer.valueOf(lastAudibleStreamVolume));
        }
        return checkRoutedToBluetoothW;
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void removeCallback(VolumeDialogController.Callbacks callbacks) {
        C c = this.mCallbacks;
        Objects.requireNonNull(c);
        c.mCallbackMap.remove(callbacks);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void setActiveStream(int i) {
        this.mWorker.obtainMessage(11, i, 0).sendToTarget();
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void setCaptionsEnabled(boolean z) {
        Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "odi_captions_enabled", z ? 1 : 0, -2);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void setRingerMode(int i, boolean z) {
        this.mWorker.obtainMessage(4, i, z ? 1 : 0).sendToTarget();
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void setStreamVolume(int i, int i2) {
        this.mWorker.obtainMessage(10, i, i2).sendToTarget();
    }

    public final void setVolumePolicy(VolumePolicy volumePolicy) {
        this.mVolumePolicy = volumePolicy;
        if (volumePolicy != null) {
            try {
                this.mAudio.setVolumePolicy(volumePolicy);
            } catch (NoSuchMethodError unused) {
                Log.w(TAG, "No volume policy api");
            }
        }
    }

    public final VolumeDialogController.StreamState streamStateW(int i) {
        VolumeDialogController.StreamState streamState = this.mState.states.get(i);
        if (streamState != null) {
            return streamState;
        }
        VolumeDialogController.StreamState streamState2 = new VolumeDialogController.StreamState();
        this.mState.states.put(i, streamState2);
        return streamState2;
    }

    public final boolean updateActiveStreamW(int i) {
        VolumeDialogController.State state = this.mState;
        if (i == state.activeStream) {
            return false;
        }
        state.activeStream = i;
        Events.writeEvent(2, Integer.valueOf(i));
        if (D.BUG) {
            ExifInterface$$ExternalSyntheticOutline1.m("updateActiveStreamW ", i, TAG);
        }
        if (i >= 100) {
            i = -1;
        }
        if (D.BUG) {
            ExifInterface$$ExternalSyntheticOutline1.m("forceVolumeControlStream ", i, TAG);
        }
        this.mAudio.forceVolumeControlStream(i);
        return true;
    }

    public final boolean updateEffectsSuppressorW(ComponentName componentName) {
        String str;
        if (Objects.equals(this.mState.effectsSuppressor, componentName)) {
            return false;
        }
        VolumeDialogController.State state = this.mState;
        state.effectsSuppressor = componentName;
        PackageManager packageManager = this.mPackageManager;
        if (componentName == null) {
            str = null;
        } else {
            str = componentName.getPackageName();
            try {
                String trim = Objects.toString(packageManager.getApplicationInfo(str, 0).loadLabel(packageManager), "").trim();
                if (trim.length() > 0) {
                    str = trim;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        state.effectsSuppressorName = str;
        VolumeDialogController.State state2 = this.mState;
        Events.writeEvent(14, state2.effectsSuppressor, state2.effectsSuppressorName);
        return true;
    }

    public final boolean updateRingerModeInternalW(int i) {
        VolumeDialogController.State state = this.mState;
        if (i == state.ringerModeInternal) {
            return false;
        }
        state.ringerModeInternal = i;
        Events.writeEvent(11, Integer.valueOf(i));
        if (this.mState.ringerModeInternal == 2 && System.currentTimeMillis() - this.mLastToggledRingerOn < 1000) {
            try {
                this.mAudioService.playSoundEffect(5, -2);
            } catch (RemoteException unused) {
            }
        }
        return true;
    }

    public final boolean updateZenConfig() {
        boolean z;
        boolean z2;
        boolean z3;
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        int i = consolidatedNotificationPolicy.priorityCategories;
        if ((i & 32) == 0) {
            z = true;
        } else {
            z = false;
        }
        if ((i & 64) == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((i & 128) == 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        boolean areAllPriorityOnlyRingerSoundsMuted = ZenModeConfig.areAllPriorityOnlyRingerSoundsMuted(consolidatedNotificationPolicy);
        VolumeDialogController.State state = this.mState;
        if (state.disallowAlarms == z && state.disallowMedia == z2 && state.disallowRinger == areAllPriorityOnlyRingerSoundsMuted && state.disallowSystem == z3) {
            return false;
        }
        state.disallowAlarms = z;
        state.disallowMedia = z2;
        state.disallowSystem = z3;
        state.disallowRinger = areAllPriorityOnlyRingerSoundsMuted;
        Events.writeEvent(17, "disallowAlarms=" + z + " disallowMedia=" + z2 + " disallowSystem=" + z3 + " disallowRinger=" + areAllPriorityOnlyRingerSoundsMuted);
        return true;
    }

    public final boolean updateZenModeW() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", 0);
        VolumeDialogController.State state = this.mState;
        if (state.zenMode == i) {
            return false;
        }
        state.zenMode = i;
        Events.writeEvent(13, Integer.valueOf(i));
        return true;
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void userActivity() {
        this.mWorker.removeMessages(13);
        this.mWorker.sendEmptyMessage(13);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void vibrate(VibrationEffect vibrationEffect) {
        this.mVibrator.vibrate(vibrationEffect, SONIFICIATION_VIBRATION_ATTRIBUTES);
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final void scheduleTouchFeedback() {
        this.mLastToggledRingerOn = System.currentTimeMillis();
    }

    public final boolean updateStreamLevelW(int i, int i2) {
        boolean z;
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        if (streamStateW.level == i2) {
            return false;
        }
        streamStateW.level = i2;
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 6) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Events.writeEvent(10, Integer.valueOf(i), Integer.valueOf(i2));
        }
        return true;
    }

    public final boolean updateStreamMuteW(int i, boolean z) {
        boolean z2;
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        boolean z3 = false;
        if (streamStateW.muted == z) {
            return false;
        }
        streamStateW.muted = z;
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 6) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            Events.writeEvent(15, Integer.valueOf(i), Boolean.valueOf(z));
        }
        if (z) {
            if (i == 2 || i == 5) {
                z3 = true;
            }
            if (z3) {
                updateRingerModeInternalW(this.mRingerModeObservers.mRingerModeInternal.getValue().intValue());
            }
        }
        return true;
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final AudioManager getAudioManager() {
        return this.mAudio;
    }

    @Override // com.android.systemui.plugins.VolumeDialogController
    public final boolean hasVibrator() {
        return this.mHasVibrator;
    }
}
