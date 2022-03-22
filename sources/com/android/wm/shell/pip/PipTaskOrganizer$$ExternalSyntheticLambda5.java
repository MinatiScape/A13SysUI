package com.android.wm.shell.pip;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Region;
import android.media.AudioAttributes;
import android.os.RemoteException;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.RemoteAnimationAdapter;
import android.view.SurfaceControl;
import android.view.View;
import android.view.WindowManagerGlobal;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda5(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PipTaskOrganizer pipTaskOrganizer = (PipTaskOrganizer) this.f$0;
                SurfaceControl surfaceControl = (SurfaceControl) this.f$2;
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.finishResizeForMenu((Rect) this.f$1);
                pipTaskOrganizer.sendOnPipTransitionFinished(2);
                if (surfaceControl != null) {
                    pipTaskOrganizer.fadeOutAndRemoveOverlay(surfaceControl, null, false);
                    return;
                }
                return;
            case 1:
                EdgeBackGestureHandler.AnonymousClass1 r0 = (EdgeBackGestureHandler.AnonymousClass1) this.f$0;
                Region region = (Region) this.f$1;
                Region region2 = (Region) this.f$2;
                int i = EdgeBackGestureHandler.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                EdgeBackGestureHandler.this.mExcludeRegion.set(region);
                Region region3 = EdgeBackGestureHandler.this.mUnrestrictedExcludeRegion;
                if (region2 != null) {
                    region = region2;
                }
                region3.set(region);
                return;
            case 2:
                ActionProxyReceiver actionProxyReceiver = (ActionProxyReceiver) this.f$0;
                Intent intent = (Intent) this.f$1;
                Context context = (Context) this.f$2;
                int i2 = ActionProxyReceiver.$r8$clinit;
                Objects.requireNonNull(actionProxyReceiver);
                Objects.requireNonNull(actionProxyReceiver.mActivityManagerWrapper);
                try {
                    ActivityManager.getService().closeSystemDialogs("screenshot");
                } catch (RemoteException e) {
                    Log.w("ActivityManagerWrapper", "Failed to close system windows", e);
                }
                PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android:screenshot_action_intent");
                ActivityOptions makeBasic = ActivityOptions.makeBasic();
                makeBasic.setDisallowEnterPictureInPictureWhileLaunching(intent.getBooleanExtra("android:screenshot_disallow_enter_pip", false));
                try {
                    pendingIntent.send(context, 0, null, null, null, null, makeBasic.toBundle());
                    if (intent.getBooleanExtra("android:screenshot_override_transition", false)) {
                        try {
                            WindowManagerGlobal.getWindowManagerService().overridePendingAppTransitionRemote(new RemoteAnimationAdapter(ScreenshotController.SCREENSHOT_REMOTE_RUNNER, 0L, 0L), 0);
                            return;
                        } catch (Exception e2) {
                            Log.e("ActionProxyReceiver", "Error overriding screenshot app transition", e2);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (PendingIntent.CanceledException e3) {
                    Log.e("ActionProxyReceiver", "Pending intent canceled", e3);
                    return;
                }
            case 3:
                VibratorHelper vibratorHelper = (VibratorHelper) this.f$0;
                VibrationAttributes vibrationAttributes = VibratorHelper.TOUCH_VIBRATION_ATTRIBUTES;
                Objects.requireNonNull(vibratorHelper);
                vibratorHelper.mVibrator.vibrate((VibrationEffect) this.f$1, (AudioAttributes) this.f$2);
                return;
            case 4:
                NotificationContentInflater notificationContentInflater = (NotificationContentInflater) this.f$0;
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$1;
                Objects.requireNonNull(notificationContentInflater);
                Objects.requireNonNull(expandableNotificationRow);
                expandableNotificationRow.mPublicLayout.setContractedChild(null);
                notificationContentInflater.mRemoteViewCache.removeCachedView((NotificationEntry) this.f$2, 8);
                return;
            default:
                int i3 = NotificationInfo.$r8$clinit;
                ((View) this.f$0).setSelected(true);
                ((View) this.f$1).setSelected(false);
                ((View) this.f$2).setSelected(false);
                return;
        }
    }
}
