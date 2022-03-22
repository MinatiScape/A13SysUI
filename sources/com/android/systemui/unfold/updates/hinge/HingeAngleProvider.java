package com.android.systemui.unfold.updates.hinge;

import androidx.core.util.Consumer;
import com.android.systemui.statusbar.policy.CallbackController;
/* compiled from: HingeAngleProvider.kt */
/* loaded from: classes.dex */
public interface HingeAngleProvider extends CallbackController<Consumer<Float>> {
    void start();

    void stop();
}
