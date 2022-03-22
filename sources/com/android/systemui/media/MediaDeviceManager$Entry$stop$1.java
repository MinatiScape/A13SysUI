package com.android.systemui.media;

import android.media.session.MediaController;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;
import java.util.Objects;
/* compiled from: MediaDeviceManager.kt */
/* loaded from: classes.dex */
public final class MediaDeviceManager$Entry$stop$1 implements Runnable {
    public final /* synthetic */ MediaDeviceManager.Entry this$0;

    public MediaDeviceManager$Entry$stop$1(MediaDeviceManager.Entry entry) {
        this.this$0 = entry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaDeviceManager.Entry entry = this.this$0;
        entry.started = false;
        MediaController mediaController = entry.controller;
        if (mediaController != null) {
            mediaController.unregisterCallback(entry);
        }
        MediaDeviceManager.Entry entry2 = this.this$0;
        Objects.requireNonNull(entry2);
        entry2.localMediaManager.stopScan();
        MediaDeviceManager.Entry entry3 = this.this$0;
        Objects.requireNonNull(entry3);
        LocalMediaManager localMediaManager = entry3.localMediaManager;
        MediaDeviceManager.Entry entry4 = this.this$0;
        Objects.requireNonNull(localMediaManager);
        localMediaManager.mCallbacks.remove(entry4);
        MediaDeviceManager.Entry entry5 = this.this$0;
        Objects.requireNonNull(entry5);
        MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager = entry5.muteAwaitConnectionManager;
        if (mediaMuteAwaitConnectionManager != null) {
            mediaMuteAwaitConnectionManager.audioManager.unregisterMuteAwaitConnectionCallback(mediaMuteAwaitConnectionManager.muteAwaitConnectionChangeListener);
        }
    }
}
