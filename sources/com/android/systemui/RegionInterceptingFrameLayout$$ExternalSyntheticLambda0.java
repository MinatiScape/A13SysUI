package com.android.systemui;

import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class RegionInterceptingFrameLayout$$ExternalSyntheticLambda0 implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ FrameLayout f$0;

    public /* synthetic */ RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(FrameLayout frameLayout, int i) {
        this.$r8$classId = i;
        this.f$0 = frameLayout;
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                RegionInterceptingFrameLayout.$r8$lambda$etD9bSGE3_mjVzLmmjglFoqooLY((RegionInterceptingFrameLayout) this.f$0, internalInsetsInfo);
                return;
            default:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                int i = NavigationBarView.$r8$clinit;
                Objects.requireNonNull(navigationBarView);
                EdgeBackGestureHandler edgeBackGestureHandler = navigationBarView.mEdgeBackGestureHandler;
                Objects.requireNonNull(edgeBackGestureHandler);
                if (!edgeBackGestureHandler.mIsEnabled || !edgeBackGestureHandler.mIsBackGestureAllowed) {
                    z = false;
                } else {
                    z = true;
                }
                if (!z) {
                    internalInsetsInfo.setTouchableInsets(0);
                    return;
                }
                internalInsetsInfo.setTouchableInsets(3);
                internalInsetsInfo.touchableRegion.set(navigationBarView.getButtonLocations(false, false, false));
                return;
        }
    }
}
