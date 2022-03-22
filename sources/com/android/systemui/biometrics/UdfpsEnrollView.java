package com.android.systemui.biometrics;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class UdfpsEnrollView extends UdfpsAnimationView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final UdfpsEnrollProgressBarDrawable mFingerprintProgressDrawable;
    public ImageView mFingerprintProgressView;
    public ImageView mFingerprintView;
    public final UdfpsEnrollDrawable mFingerprintDrawable = new UdfpsEnrollDrawable(((FrameLayout) this).mContext);
    public final Handler mHandler = new Handler(Looper.getMainLooper());

    public UdfpsEnrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintProgressDrawable = new UdfpsEnrollProgressBarDrawable(context);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        this.mFingerprintView = (ImageView) findViewById(2131429140);
        this.mFingerprintProgressView = (ImageView) findViewById(2131429139);
        this.mFingerprintView.setImageDrawable(this.mFingerprintDrawable);
        this.mFingerprintProgressView.setImageDrawable(this.mFingerprintProgressDrawable);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }
}
