package com.android.wm.shell;

import android.graphics.Rect;
import android.provider.DeviceConfig;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipResizeGestureHandler;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda3 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda3();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        final PipTouchHandler pipTouchHandler = (PipTouchHandler) obj;
        Objects.requireNonNull(pipTouchHandler);
        pipTouchHandler.mEnableResize = pipTouchHandler.mContext.getResources().getBoolean(2131034152);
        pipTouchHandler.reloadResources();
        PipMotionHelper pipMotionHelper = pipTouchHandler.mMotionHelper;
        Objects.requireNonNull(pipMotionHelper);
        PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        PhysicsAnimator<Rect> instance = PhysicsAnimator.getInstance(motionBoundsState.mBoundsInMotion);
        pipMotionHelper.mTemporaryBoundsPhysicsAnimator = instance;
        instance.customAnimationHandler = pipMotionHelper.mSfAnimationHandlerThreadLocal.get();
        final PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
        Objects.requireNonNull(pipResizeGestureHandler);
        pipResizeGestureHandler.mContext.getDisplay().getRealSize(pipResizeGestureHandler.mMaxSize);
        pipResizeGestureHandler.reloadResources();
        pipResizeGestureHandler.mEnablePinchResize = DeviceConfig.getBoolean("systemui", "pip_pinch_resize", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", pipResizeGestureHandler.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipResizeGestureHandler.1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("pip_pinch_resize")) {
                    pipResizeGestureHandler.mEnablePinchResize = properties.getBoolean("pip_pinch_resize", true);
                }
            }
        });
        pipTouchHandler.mPipDismissTargetHandler.init();
        pipTouchHandler.mEnableStash = DeviceConfig.getBoolean("systemui", "pip_stashing", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", pipTouchHandler.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                PipTouchHandler pipTouchHandler2 = PipTouchHandler.this;
                Objects.requireNonNull(pipTouchHandler2);
                if (properties.getKeyset().contains("pip_stashing")) {
                    pipTouchHandler2.mEnableStash = properties.getBoolean("pip_stashing", true);
                }
            }
        });
        pipTouchHandler.mStashVelocityThreshold = DeviceConfig.getFloat("systemui", "pip_velocity_threshold", 18000.0f);
        DeviceConfig.addOnPropertiesChangedListener("systemui", pipTouchHandler.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                PipTouchHandler pipTouchHandler2 = PipTouchHandler.this;
                Objects.requireNonNull(pipTouchHandler2);
                if (properties.getKeyset().contains("pip_velocity_threshold")) {
                    pipTouchHandler2.mStashVelocityThreshold = properties.getFloat("pip_velocity_threshold", 18000.0f);
                }
            }
        });
    }
}
