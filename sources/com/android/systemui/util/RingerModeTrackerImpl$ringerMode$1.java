package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes.dex */
public /* synthetic */ class RingerModeTrackerImpl$ringerMode$1 extends FunctionReferenceImpl implements Function0<Integer> {
    public RingerModeTrackerImpl$ringerMode$1(Object obj) {
        super(0, obj, AudioManager.class, "getRingerMode", "getRingerMode()I", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public final Integer invoke() {
        return Integer.valueOf(((AudioManager) this.receiver).getRingerMode());
    }
}
