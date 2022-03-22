package com.android.wm.shell.pip;

import android.animation.AnimationHandler;
import android.provider.DeviceConfig;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipAnimationController$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ PipAnimationController$$ExternalSyntheticLambda0 INSTANCE$1 = new PipAnimationController$$ExternalSyntheticLambda0(1);
    public static final /* synthetic */ PipAnimationController$$ExternalSyntheticLambda0 INSTANCE = new PipAnimationController$$ExternalSyntheticLambda0(0);

    public /* synthetic */ PipAnimationController$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.$r8$classId) {
            case 0:
                AnimationHandler animationHandler = new AnimationHandler();
                animationHandler.setProvider(new SfVsyncFrameCallbackProvider());
                return animationHandler;
            default:
                int i = MicrophoneToggleTile.$r8$clinit;
                return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "mic_toggle_enabled", true));
        }
    }
}
