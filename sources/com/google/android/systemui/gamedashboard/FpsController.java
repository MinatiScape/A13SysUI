package com.google.android.systemui.gamedashboard;

import android.view.WindowManager;
import android.window.TaskFpsCallback;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class FpsController {
    public Callback mCallback;
    public final Executor mExecutor;
    public final AnonymousClass1 mListener = new TaskFpsCallback() { // from class: com.google.android.systemui.gamedashboard.FpsController.1
        public final void onFpsReported(float f) {
            Callback callback = FpsController.this.mCallback;
            if (callback != null) {
                StatusBar$$ExternalSyntheticLambda4 statusBar$$ExternalSyntheticLambda4 = (StatusBar$$ExternalSyntheticLambda4) callback;
                Objects.requireNonNull(statusBar$$ExternalSyntheticLambda4);
                ShortcutBarView shortcutBarView = (ShortcutBarView) statusBar$$ExternalSyntheticLambda4.f$0;
                Objects.requireNonNull(shortcutBarView);
                shortcutBarView.mFpsView.setText(String.valueOf(Math.round(f)));
            }
        }
    };
    public final WindowManager mWindowManager;

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public FpsController(Executor executor, WindowManager windowManager) {
        this.mExecutor = executor;
        this.mWindowManager = windowManager;
    }
}
