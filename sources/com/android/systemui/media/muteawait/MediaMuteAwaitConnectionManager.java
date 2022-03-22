package com.android.systemui.media.muteawait;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.settingslib.media.LocalMediaManager;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaMuteAwaitConnectionManager.kt */
/* loaded from: classes.dex */
public final class MediaMuteAwaitConnectionManager {
    public final AudioManager audioManager;
    public final Context context;
    public AudioDeviceAttributes currentMutedDevice;
    public final DeviceIconUtil deviceIconUtil;
    public final LocalMediaManager localMediaManager;
    public final Executor mainExecutor;
    public final MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1 muteAwaitConnectionChangeListener = new AudioManager.MuteAwaitConnectionCallback() { // from class: com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1
        public final void onMutedUntilConnection(AudioDeviceAttributes audioDeviceAttributes, int[] iArr) {
            if (ArraysKt___ArraysKt.contains(iArr, 1)) {
                MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager = MediaMuteAwaitConnectionManager.this;
                Objects.requireNonNull(mediaMuteAwaitConnectionManager);
                mediaMuteAwaitConnectionManager.currentMutedDevice = audioDeviceAttributes;
                MediaMuteAwaitConnectionManager.this.localMediaManager.dispatchAboutToConnectDeviceChanged(audioDeviceAttributes.getName(), MediaMuteAwaitConnectionManager.this.getIcon(audioDeviceAttributes));
            }
        }

        public final void onUnmutedEvent(int i, AudioDeviceAttributes audioDeviceAttributes, int[] iArr) {
            MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager = MediaMuteAwaitConnectionManager.this;
            Objects.requireNonNull(mediaMuteAwaitConnectionManager);
            if (Intrinsics.areEqual(mediaMuteAwaitConnectionManager.currentMutedDevice, audioDeviceAttributes) && ArraysKt___ArraysKt.contains(iArr, 1)) {
                MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager2 = MediaMuteAwaitConnectionManager.this;
                Objects.requireNonNull(mediaMuteAwaitConnectionManager2);
                mediaMuteAwaitConnectionManager2.currentMutedDevice = null;
                MediaMuteAwaitConnectionManager.this.localMediaManager.dispatchAboutToConnectDeviceChanged(null, null);
            }
        }
    };

    public final Drawable getIcon(AudioDeviceAttributes audioDeviceAttributes) {
        int i;
        DeviceIconUtil deviceIconUtil = this.deviceIconUtil;
        int type = audioDeviceAttributes.getType();
        Context context = this.context;
        Objects.requireNonNull(deviceIconUtil);
        if (deviceIconUtil.mAudioDeviceTypeToIconMap.containsKey(Integer.valueOf(type))) {
            i = ((DeviceIconUtil.Device) deviceIconUtil.mAudioDeviceTypeToIconMap.get(Integer.valueOf(type))).mIconDrawableRes;
        } else {
            i = 2131232271;
        }
        return context.getDrawable(i);
    }

    public MediaMuteAwaitConnectionManager(Executor executor, LocalMediaManager localMediaManager, Context context, DeviceIconUtil deviceIconUtil) {
        this.mainExecutor = executor;
        this.localMediaManager = localMediaManager;
        this.context = context;
        this.deviceIconUtil = deviceIconUtil;
        Object systemService = context.getSystemService("audio");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.media.AudioManager");
        this.audioManager = (AudioManager) systemService;
    }
}
