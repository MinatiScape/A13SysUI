package com.android.systemui.navigationbar;

import android.graphics.Rect;
import android.util.Property;
import android.view.WindowManager;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda13 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda13(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Integer num = (Integer) obj;
                Objects.requireNonNull(navigationBar);
                NavigationBarView navigationBarView = navigationBar.mNavigationBarView;
                if (navigationBarView != null) {
                    if (navigationBarView.mCurrentRotation != num.intValue()) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        int intValue = num.intValue();
                        NavigationBarView navigationBarView2 = navigationBar.mNavigationBarView;
                        if (navigationBarView2 != null && navigationBarView2.isAttachedToWindow()) {
                            navigationBar.prepareNavigationBarView();
                            WindowManager windowManager = navigationBar.mWindowManager;
                            NavigationBarFrame navigationBarFrame = navigationBar.mFrame;
                            WindowManager.LayoutParams barLayoutParamsForRotation = navigationBar.getBarLayoutParamsForRotation(intValue);
                            barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
                            for (int i = 0; i <= 3; i++) {
                                barLayoutParamsForRotation.paramsForRotation[i] = navigationBar.getBarLayoutParamsForRotation(i);
                            }
                            windowManager.updateViewLayout(navigationBarFrame, barLayoutParamsForRotation);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 1:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                Property property = (Property) obj;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                Runnable runnable = notificationPanelViewController.mPanelAlphaEndAction;
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            default:
                ((BubbleController) this.f$0).promoteBubbleFromOverflow((Bubble) obj);
                return;
        }
    }
}
