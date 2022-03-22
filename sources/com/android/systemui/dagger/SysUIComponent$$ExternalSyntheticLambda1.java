package com.android.systemui.dagger;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SysUIComponent$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda1 INSTANCE = new SysUIComponent$$ExternalSyntheticLambda1(0);
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda1 INSTANCE$1 = new SysUIComponent$$ExternalSyntheticLambda1(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SysUIComponent$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                SysUIUnfoldComponent sysUIUnfoldComponent = (SysUIUnfoldComponent) obj;
                sysUIUnfoldComponent.getUnfoldLightRevealOverlayAnimation().init();
                UnfoldTransitionWallpaperController unfoldTransitionWallpaperController = sysUIUnfoldComponent.getUnfoldTransitionWallpaperController();
                Objects.requireNonNull(unfoldTransitionWallpaperController);
                unfoldTransitionWallpaperController.unfoldTransitionProgressProvider.addCallback(new UnfoldTransitionWallpaperController.TransitionListener());
                return;
            default:
                ((StatusBar) obj).checkBarModes();
                return;
        }
    }
}
