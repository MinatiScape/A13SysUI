package com.android.systemui.settings.brightness;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import java.util.Objects;
/* loaded from: classes.dex */
public class BrightnessDialog extends Activity {
    public final Handler mBackgroundHandler;
    public BrightnessController mBrightnessController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final BrightnessSliderController.Factory mToggleSliderFactory;

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 25 || i == 24 || i == 164) {
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public BrightnessDialog(BroadcastDispatcher broadcastDispatcher, BrightnessSliderController.Factory factory, Handler handler) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mToggleSliderFactory = factory;
        this.mBackgroundHandler = handler;
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setGravity(49);
        window.clearFlags(2);
        window.requestFeature(1);
        window.getDecorView();
        window.setLayout(-1, -2);
        setContentView(2131624016);
        FrameLayout frameLayout = (FrameLayout) findViewById(2131427604);
        frameLayout.setVisibility(0);
        BrightnessSliderController create = this.mToggleSliderFactory.create(this, frameLayout);
        create.init();
        frameLayout.addView(create.mView, -1, -2);
        this.mBrightnessController = new BrightnessController(this, create, this.mBroadcastDispatcher, this.mBackgroundHandler);
    }

    @Override // android.app.Activity
    public final void onPause() {
        super.onPause();
        overridePendingTransition(17432576, 17432577);
    }

    @Override // android.app.Activity
    public final void onStart() {
        super.onStart();
        BrightnessController brightnessController = this.mBrightnessController;
        Objects.requireNonNull(brightnessController);
        brightnessController.mBackgroundHandler.post(brightnessController.mStartListeningRunnable);
        MetricsLogger.visible(this, 220);
    }

    @Override // android.app.Activity
    public final void onStop() {
        super.onStop();
        MetricsLogger.hidden(this, 220);
        BrightnessController brightnessController = this.mBrightnessController;
        Objects.requireNonNull(brightnessController);
        brightnessController.mBackgroundHandler.post(brightnessController.mStopListeningRunnable);
        brightnessController.mControlValueInitialized = false;
    }
}
