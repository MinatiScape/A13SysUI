package com.android.systemui.tv;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda3;
/* loaded from: classes.dex */
public abstract class TvBottomSheetActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Drawable mBackgroundWithBlur;
    public Drawable mBackgroundWithoutBlur;
    public final ShellCommandHandlerImpl$$ExternalSyntheticLambda3 mBlurConsumer = new ShellCommandHandlerImpl$$ExternalSyntheticLambda3(this, 2);

    @Override // android.app.Activity
    public final void finish() {
        super.finish();
        overridePendingTransition(0, 2130772567);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624629);
        overridePendingTransition(2130772566, 0);
        this.mBackgroundWithBlur = getResources().getDrawable(2131231615);
        this.mBackgroundWithoutBlur = getResources().getDrawable(2131231614);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165391);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = i - (dimensionPixelSize * 2);
        attributes.height = -2;
        attributes.gravity = 81;
        attributes.horizontalMargin = 0.0f;
        attributes.verticalMargin = dimensionPixelSize / i2;
        attributes.format = -2;
        attributes.type = 2008;
        attributes.flags = attributes.flags | 128 | 16777216;
        getWindow().setAttributes(attributes);
        getWindow().setElevation(getWindow().getElevation() + 5.0f);
        getWindow().setBackgroundBlurRadius(getResources().getDimensionPixelSize(2131165381));
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onDetachedFromWindow() {
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
        super.onDetachedFromWindow();
    }
}
