package com.google.android.systemui.assist.uihints;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Objects;
/* compiled from: NavBarFader.kt */
/* loaded from: classes.dex */
public final class NavBarFader implements NgaMessageHandler.NavBarVisibilityListener {
    public final Handler handler;
    public final NavigationBarController navigationBarController;
    public float targetAlpha;
    public final NavBarFader$onTimeout$1 onTimeout = new Runnable() { // from class: com.google.android.systemui.assist.uihints.NavBarFader$onTimeout$1
        @Override // java.lang.Runnable
        public final void run() {
            NavBarFader.this.onVisibleRequest(true);
        }
    };
    public ObjectAnimator animator = new ObjectAnimator();

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.NavBarVisibilityListener
    public final void onVisibleRequest(boolean z) {
        float f;
        boolean z2;
        NavigationBarController navigationBarController = this.navigationBarController;
        Objects.requireNonNull(navigationBarController);
        NavigationBarView navigationBarView = navigationBarController.getNavigationBarView(0);
        if (navigationBarView != null) {
            this.handler.removeCallbacks(this.onTimeout);
            if (!z) {
                this.handler.postDelayed(this.onTimeout, 10000L);
            }
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            if (f == this.targetAlpha) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                this.animator.cancel();
                float alpha = navigationBarView.getAlpha();
                this.targetAlpha = f;
                ObjectAnimator duration = ObjectAnimator.ofFloat(navigationBarView, View.ALPHA, alpha, f).setDuration(Math.abs(f - alpha) * ((float) 80));
                this.animator = duration;
                if (z) {
                    duration.setStartDelay(80L);
                }
                this.animator.start();
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.assist.uihints.NavBarFader$onTimeout$1] */
    public NavBarFader(NavigationBarController navigationBarController, Handler handler) {
        float f;
        this.navigationBarController = navigationBarController;
        this.handler = handler;
        Objects.requireNonNull(navigationBarController);
        NavigationBarView navigationBarView = navigationBarController.getNavigationBarView(0);
        if (navigationBarView == null) {
            f = 1.0f;
        } else {
            f = navigationBarView.getAlpha();
        }
        this.targetAlpha = f;
    }
}
