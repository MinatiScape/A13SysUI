package com.android.wifitrackerlib;

import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.bubbles.BubbleStackView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiEntry$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiEntry$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        int i2 = 0;
        switch (this.$r8$classId) {
            case 0:
                WifiEntry wifiEntry = (WifiEntry) this.f$0;
                Objects.requireNonNull(wifiEntry);
                WifiEntry.ConnectCallback connectCallback = wifiEntry.mConnectCallback;
                if (connectCallback != null) {
                    ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(0);
                    return;
                }
                return;
            case 1:
            default:
                Runnable[] runnableArr = (Runnable[]) this.f$0;
                int length = runnableArr.length;
                while (i2 < length) {
                    runnableArr[i2].run();
                    i2++;
                }
                return;
            case 2:
                AutoHideController autoHideController = (AutoHideController) this.f$0;
                Objects.requireNonNull(autoHideController);
                if (autoHideController.isAnyTransientBarShown()) {
                    try {
                        autoHideController.mWindowManagerService.hideTransientBars(autoHideController.mDisplayId);
                    } catch (RemoteException unused) {
                        Log.w("AutoHideController", "Cannot get WindowManager");
                    }
                    AutoHideUiElement autoHideUiElement = autoHideController.mStatusBar;
                    if (autoHideUiElement != null) {
                        autoHideUiElement.hide();
                    }
                    AutoHideUiElement autoHideUiElement2 = autoHideController.mNavigationBar;
                    if (autoHideUiElement2 != null) {
                        autoHideUiElement2.hide();
                        return;
                    }
                    return;
                }
                return;
            case 3:
                StatusBar.AnonymousClass10 r4 = (StatusBar.AnonymousClass10) this.f$0;
                Objects.requireNonNull(r4);
                StatusBar statusBar = StatusBar.this;
                statusBar.mDeviceInteractive = true;
                statusBar.mWakeUpCoordinator.setWakingUp(true);
                if (!StatusBar.this.mKeyguardBypassController.getBypassEnabled()) {
                    StatusBar.this.mHeadsUpManager.releaseAllImmediately();
                }
                StatusBar.this.updateVisibleToUser();
                StatusBar.this.updateIsKeyguard();
                StatusBar.this.mDozeServiceHost.stopDozing();
                StatusBar.m108$$Nest$mupdateRevealEffect(StatusBar.this, true);
                StatusBar.this.updateNotificationPanelTouchState();
                ScreenOffAnimationController screenOffAnimationController = StatusBar.this.mScreenOffAnimationController;
                Objects.requireNonNull(screenOffAnimationController);
                ArrayList arrayList = screenOffAnimationController.animations;
                if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (((ScreenOffAnimation) it.next()).shouldHideScrimOnWakeUp()) {
                                i2 = 1;
                            }
                        }
                    }
                }
                if (i2 != 0) {
                    StatusBar.this.makeExpandedInvisible();
                    return;
                }
                return;
            case 4:
                ((ShadeController) this.f$0).collapsePanel();
                return;
            case 5:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i3 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.mFlyout.setVisibility(0);
                bubbleStackView.updateTemporarilyInvisibleAnimation(false);
                if (bubbleStackView.mStackAnimationController.isStackOnLeftSide()) {
                    i = -bubbleStackView.mFlyout.getWidth();
                } else {
                    i = bubbleStackView.mFlyout.getWidth();
                }
                bubbleStackView.mFlyoutDragDeltaX = i;
                bubbleStackView.animateFlyoutCollapsed(false, 0.0f);
                bubbleStackView.mFlyout.postDelayed(bubbleStackView.mHideFlyout, 5000L);
                return;
        }
    }
}
