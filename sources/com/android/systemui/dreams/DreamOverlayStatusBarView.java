package com.android.systemui.dreams;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.util.Preconditions;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.policy.BatteryController;
/* loaded from: classes.dex */
public class DreamOverlayStatusBarView extends ConstraintLayout implements BatteryController.BatteryStateChangeCallback {
    public BatteryMeterView mBatteryView;
    public ImageView mWifiStatusView;

    public DreamOverlayStatusBarView(Context context) {
        this(context, null);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mBatteryView = (BatteryMeterView) Preconditions.checkNotNull((BatteryMeterView) findViewById(2131427877), "R.id.dream_overlay_battery must not be null");
        ImageView imageView = (ImageView) Preconditions.checkNotNull((ImageView) findViewById(2131427883), "R.id.dream_overlay_wifi_status must not be null");
        this.mWifiStatusView = imageView;
        imageView.setImageDrawable(getContext().getDrawable(2131232270));
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
