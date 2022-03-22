package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PanelViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class PanelView extends FrameLayout {
    public OnConfigurationChangedListener mOnConfigurationChangedListener;
    public PanelViewController.TouchHandler mTouchHandler;

    /* loaded from: classes.dex */
    public interface OnConfigurationChangedListener {
    }

    public PanelView(Context context) {
        super(context);
    }

    public PanelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x020e  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:171:? A[RETURN, SYNTHETIC] */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r13) {
        /*
            Method dump skipped, instructions count: 855
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PanelView.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public PanelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchConfigurationChanged(Configuration configuration) {
        super.dispatchConfigurationChanged(configuration);
        NotificationPanelViewController.OnConfigurationChangedListener onConfigurationChangedListener = (NotificationPanelViewController.OnConfigurationChangedListener) this.mOnConfigurationChangedListener;
        Objects.requireNonNull(onConfigurationChangedListener);
        PanelViewController.this.loadDimens();
        KeyguardAffordanceHelper keyguardAffordanceHelper = NotificationPanelViewController.this.mAffordanceHelper;
        Objects.requireNonNull(keyguardAffordanceHelper);
        keyguardAffordanceHelper.initDimens();
        keyguardAffordanceHelper.initIcons();
        NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
        int i = configuration.orientation;
        Objects.requireNonNull(notificationPanelViewController);
    }

    public final void setOnTouchListener(NotificationPanelViewController.AnonymousClass17 r1) {
        super.setOnTouchListener((View.OnTouchListener) r1);
        this.mTouchHandler = r1;
    }

    public PanelView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
