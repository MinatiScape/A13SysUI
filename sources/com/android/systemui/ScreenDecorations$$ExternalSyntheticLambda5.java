package com.android.systemui;

import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.decor.DecorProvider;
import com.android.systemui.decor.OverlayWindow;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Pair;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda5(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        View view;
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                OverlayWindow overlayWindow = (OverlayWindow) this.f$1;
                DecorProvider decorProvider = (DecorProvider) obj;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                int viewId = decorProvider.getViewId();
                OverlayWindow[] overlayWindowArr = screenDecorations.mOverlays;
                if (overlayWindowArr != null) {
                    for (OverlayWindow overlayWindow2 : overlayWindowArr) {
                        if (overlayWindow2 != null) {
                            Pair pair = (Pair) overlayWindow2.viewProviderMap.get(Integer.valueOf(viewId));
                            if (pair == null) {
                                view = null;
                            } else {
                                view = (View) pair.getFirst();
                            }
                            if (view != null) {
                                overlayWindow2.rootView.removeView(view);
                                overlayWindow2.viewProviderMap.remove(Integer.valueOf(viewId));
                            }
                        }
                    }
                }
                Objects.requireNonNull(overlayWindow);
                overlayWindow.viewProviderMap.put(Integer.valueOf(decorProvider.getViewId()), new Pair(decorProvider.inflateView(overlayWindow.layoutInflater, overlayWindow.rootView), decorProvider));
                if (screenDecorations.mHwcScreenDecorationSupport != null) {
                    overlayWindow.rootView.setVisibility(4);
                    return;
                }
                return;
            default:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                MotionEvent motionEvent = (MotionEvent) this.f$1;
                StatusBar statusBar = (StatusBar) obj;
                int i = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                if (motionEvent.getActionMasked() == 0) {
                    Objects.requireNonNull(statusBar);
                    NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                    Objects.requireNonNull(notificationPanelViewController);
                    if (notificationPanelViewController.mLatencyTracker.isEnabled()) {
                        notificationPanelViewController.mLatencyTracker.onActionStart(0);
                        notificationPanelViewController.mExpandLatencyTracking = true;
                    }
                }
                OverviewProxyService.this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda0(r0, motionEvent, statusBar, 0));
                return;
        }
    }
}
