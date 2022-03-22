package com.android.systemui.qs.tiles;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.screenshot.ImageLoader$Result;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenRecordTile$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenRecordTile$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        final boolean z;
        switch (this.$r8$classId) {
            case 0:
                final ScreenRecordTile screenRecordTile = (ScreenRecordTile) this.f$0;
                final View view = (View) this.f$1;
                if (view == null) {
                    Objects.requireNonNull(screenRecordTile);
                } else if (!screenRecordTile.mKeyguardStateController.isShowing()) {
                    z = true;
                    StandardWifiEntry$$ExternalSyntheticLambda0 standardWifiEntry$$ExternalSyntheticLambda0 = new StandardWifiEntry$$ExternalSyntheticLambda0(screenRecordTile, 4);
                    RecordingController recordingController = screenRecordTile.mController;
                    Context context = screenRecordTile.mContext;
                    Objects.requireNonNull(recordingController);
                    final ScreenRecordDialog screenRecordDialog = new ScreenRecordDialog(context, recordingController, recordingController.mUserContextProvider, standardWifiEntry$$ExternalSyntheticLambda0);
                    screenRecordTile.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda0
                        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                        public final boolean onDismiss() {
                            ScreenRecordTile screenRecordTile2 = ScreenRecordTile.this;
                            boolean z2 = z;
                            ScreenRecordDialog screenRecordDialog2 = screenRecordDialog;
                            View view2 = view;
                            if (z2) {
                                DialogLaunchAnimator dialogLaunchAnimator = screenRecordTile2.mDialogLaunchAnimator;
                                Objects.requireNonNull(dialogLaunchAnimator);
                                dialogLaunchAnimator.showFromView(screenRecordDialog2, view2, false);
                            } else {
                                Objects.requireNonNull(screenRecordTile2);
                                screenRecordDialog2.show();
                            }
                            return false;
                        }
                    }, false, true);
                    return;
                }
                z = false;
                StandardWifiEntry$$ExternalSyntheticLambda0 standardWifiEntry$$ExternalSyntheticLambda02 = new StandardWifiEntry$$ExternalSyntheticLambda0(screenRecordTile, 4);
                RecordingController recordingController2 = screenRecordTile.mController;
                Context context2 = screenRecordTile.mContext;
                Objects.requireNonNull(recordingController2);
                final ScreenRecordDialog screenRecordDialog2 = new ScreenRecordDialog(context2, recordingController2, recordingController2.mUserContextProvider, standardWifiEntry$$ExternalSyntheticLambda02);
                screenRecordTile.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        ScreenRecordTile screenRecordTile2 = ScreenRecordTile.this;
                        boolean z2 = z;
                        ScreenRecordDialog screenRecordDialog22 = screenRecordDialog2;
                        View view2 = view;
                        if (z2) {
                            DialogLaunchAnimator dialogLaunchAnimator = screenRecordTile2.mDialogLaunchAnimator;
                            Objects.requireNonNull(dialogLaunchAnimator);
                            dialogLaunchAnimator.showFromView(screenRecordDialog22, view2, false);
                        } else {
                            Objects.requireNonNull(screenRecordTile2);
                            screenRecordDialog22.show();
                        }
                        return false;
                    }
                }, false, true);
                return;
            case 1:
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = (PeopleSpaceWidgetManager) this.f$0;
                int[] iArr = (int[]) this.f$1;
                HashMap hashMap = PeopleSpaceWidgetManager.mListeners;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                try {
                    if (iArr.length != 0) {
                        synchronized (peopleSpaceWidgetManager.mLock) {
                            peopleSpaceWidgetManager.updateSingleConversationWidgets(iArr);
                        }
                        return;
                    }
                    return;
                } catch (Exception e) {
                    Log.e("PeopleSpaceWidgetMgr", "Exception: " + e);
                    return;
                }
            case 2:
                LongScreenshotActivity longScreenshotActivity = (LongScreenshotActivity) this.f$0;
                ListenableFuture listenableFuture = (ListenableFuture) this.f$1;
                int i = LongScreenshotActivity.$r8$clinit;
                Objects.requireNonNull(longScreenshotActivity);
                Log.d("Screenshot", "cached bitmap load complete");
                try {
                    longScreenshotActivity.onCachedImageLoaded((ImageLoader$Result) listenableFuture.get());
                    return;
                } catch (InterruptedException | CancellationException | ExecutionException e2) {
                    Log.e("Screenshot", "Failed to load cached image", e2);
                    File file = longScreenshotActivity.mSavedImagePath;
                    if (file != null) {
                        file.delete();
                        longScreenshotActivity.mSavedImagePath = null;
                    }
                    longScreenshotActivity.finishAndRemoveTask();
                    return;
                }
            case 3:
                PluginActionManager pluginActionManager = (PluginActionManager) this.f$0;
                Objects.requireNonNull(pluginActionManager);
                pluginActionManager.onPluginDisconnected((PluginInstance) this.f$1);
                return;
            default:
                OneHandedController.OneHandedImpl oneHandedImpl = (OneHandedController.OneHandedImpl) this.f$0;
                int i2 = OneHandedController.OneHandedImpl.$r8$clinit;
                Objects.requireNonNull(oneHandedImpl);
                OneHandedController oneHandedController = OneHandedController.this;
                Objects.requireNonNull(oneHandedController);
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = oneHandedController.mDisplayAreaOrganizer;
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                oneHandedDisplayAreaOrganizer.mTransitionCallbacks.add((OneHandedTransitionCallback) this.f$1);
                return;
        }
    }
}
