package com.android.systemui.media.muteawait;

import android.content.Context;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.systemui.media.MediaFlags;
import java.util.concurrent.Executor;
/* compiled from: MediaMuteAwaitConnectionManagerFactory.kt */
/* loaded from: classes.dex */
public final class MediaMuteAwaitConnectionManagerFactory {
    public final Context context;
    public final DeviceIconUtil deviceIconUtil = new DeviceIconUtil();
    public final Executor mainExecutor;
    public final MediaFlags mediaFlags;

    public MediaMuteAwaitConnectionManagerFactory(MediaFlags mediaFlags, Context context, Executor executor) {
        this.mediaFlags = mediaFlags;
        this.context = context;
        this.mainExecutor = executor;
    }
}
