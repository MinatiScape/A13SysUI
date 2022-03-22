package com.android.systemui.user;

import android.content.pm.UserInfo;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.wifitrackerlib.PasspointNetworkDetailsTracker;
import com.android.wm.shell.pip.Pip;
import com.google.android.systemui.assist.uihints.NgaUiController;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CreateUserActivity$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CreateUserActivity$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                CreateUserActivity createUserActivity = (CreateUserActivity) this.f$0;
                int i = CreateUserActivity.$r8$clinit;
                Objects.requireNonNull(createUserActivity);
                try {
                    createUserActivity.mActivityManager.switchUser(((UserInfo) obj).id);
                } catch (RemoteException e) {
                    Log.e("CreateUserActivity", "Couldn't switch user.", e);
                }
                if (!createUserActivity.isFinishing() && !createUserActivity.isDestroyed()) {
                    createUserActivity.finish();
                    return;
                }
                return;
            case 1:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                NotificationPanelUnfoldAnimationController notificationPanelUnfoldAnimationController = (NotificationPanelUnfoldAnimationController) obj;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = notificationPanelViewController.mNotificationContainerParent;
                Objects.requireNonNull(notificationPanelUnfoldAnimationController);
                UnfoldConstantTranslateAnimator unfoldConstantTranslateAnimator = (UnfoldConstantTranslateAnimator) notificationPanelUnfoldAnimationController.translateAnimator$delegate.getValue();
                Objects.requireNonNull(unfoldConstantTranslateAnimator);
                unfoldConstantTranslateAnimator.rootView = notificationsQuickSettingsContainer;
                unfoldConstantTranslateAnimator.translationMax = notificationPanelUnfoldAnimationController.context.getResources().getDimensionPixelSize(2131166677);
                unfoldConstantTranslateAnimator.progressProvider.addCallback(unfoldConstantTranslateAnimator);
                return;
            case 2:
                PasspointNetworkDetailsTracker passpointNetworkDetailsTracker = (PasspointNetworkDetailsTracker) this.f$0;
                Objects.requireNonNull(passpointNetworkDetailsTracker);
                passpointNetworkDetailsTracker.mChosenEntry.updatePasspointConfig((PasspointConfiguration) obj);
                return;
            case 3:
                ((Pip) obj).dump((PrintWriter) this.f$0);
                return;
            default:
                boolean z = NgaUiController.VERBOSE;
                ((Region) this.f$0).op((Region) obj, Region.Op.UNION);
                return;
        }
    }
}
