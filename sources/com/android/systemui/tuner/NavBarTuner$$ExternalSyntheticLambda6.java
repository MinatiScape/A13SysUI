package com.android.systemui.tuner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.ImageReader;
import android.os.DeadObjectException;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.IScrollCaptureConnection;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.preference.ListPreference;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.qs.QSFooterView$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.statusbar.notification.collection.coalescer.EventBatch;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.util.RingerModeLiveData;
import com.android.systemui.volume.D;
import com.android.systemui.volume.Events;
import com.android.systemui.volume.Util;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda7;
import com.android.wm.shell.splitscreen.SplitScreenTransitions;
import java.time.Duration;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavBarTuner$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ NavBarTuner$$ExternalSyntheticLambda6(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                String str = (String) this.f$0;
                ListPreference listPreference = (ListPreference) this.f$1;
                int[][] iArr = NavBarTuner.ICONS;
                if (str == null) {
                    str = "default";
                }
                listPreference.setValue(str);
                return;
            case 1:
                CallbackToFutureAdapter.Completer completer = (CallbackToFutureAdapter.Completer) this.f$0;
                ImageExporter.Task task = (ImageExporter.Task) this.f$1;
                Duration duration = ImageExporter.PENDING_ENTRY_TTL;
                try {
                    completer.set(task.execute());
                    return;
                } catch (ImageExporter.ImageExportException | InterruptedException e) {
                    completer.setException(e);
                    return;
                }
            case 2:
                final ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                final ScreenshotController.SavedImageData savedImageData = (ScreenshotController.SavedImageData) this.f$1;
                ScreenshotController.AnonymousClass1 r1 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                AnimatorSet animatorSet = screenshotController.mScreenshotAnimation;
                if (animatorSet == null || !animatorSet.isRunning()) {
                    screenshotController.mScreenshotView.setChipIntents(savedImageData);
                    return;
                } else {
                    screenshotController.mScreenshotAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotController.7
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            screenshotController.mScreenshotView.setChipIntents(savedImageData);
                        }
                    });
                    return;
                }
            case 3:
                ScrollCaptureController scrollCaptureController = (ScrollCaptureController) this.f$0;
                final ScrollCaptureResponse scrollCaptureResponse = (ScrollCaptureResponse) this.f$1;
                Objects.requireNonNull(scrollCaptureController);
                final float f = Settings.Secure.getFloat(scrollCaptureController.mContext.getContentResolver(), "screenshot.scroll_max_pages", 3.0f);
                final ScrollCaptureClient scrollCaptureClient = scrollCaptureController.mClient;
                Objects.requireNonNull(scrollCaptureClient);
                final IScrollCaptureConnection connection = scrollCaptureResponse.getConnection();
                CallbackToFutureAdapter.SafeFuture future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: com.android.systemui.screenshot.ScrollCaptureClient$$ExternalSyntheticLambda1
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer2) {
                        ScrollCaptureClient scrollCaptureClient2 = ScrollCaptureClient.this;
                        IScrollCaptureConnection iScrollCaptureConnection = connection;
                        ScrollCaptureResponse scrollCaptureResponse2 = scrollCaptureResponse;
                        float f2 = f;
                        Objects.requireNonNull(scrollCaptureClient2);
                        if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
                            completer2.setException(new DeadObjectException("No active connection!"));
                            return "";
                        }
                        ScrollCaptureClient.SessionWrapper sessionWrapper = new ScrollCaptureClient.SessionWrapper(iScrollCaptureConnection, scrollCaptureResponse2.getWindowBounds(), scrollCaptureResponse2.getBoundsInWindow(), f2, scrollCaptureClient2.mBgExecutor);
                        ImageReader newInstance = ImageReader.newInstance(sessionWrapper.mTileWidth, sessionWrapper.mTileHeight, 1, 30, 256L);
                        sessionWrapper.mReader = newInstance;
                        sessionWrapper.mStartCompleter = completer2;
                        newInstance.setOnImageAvailableListenerWithExecutor(sessionWrapper, sessionWrapper.mBgExecutor);
                        try {
                            sessionWrapper.mCancellationSignal = sessionWrapper.mConnection.startCapture(sessionWrapper.mReader.getSurface(), sessionWrapper);
                            PipMenuView$$ExternalSyntheticLambda7 pipMenuView$$ExternalSyntheticLambda7 = new PipMenuView$$ExternalSyntheticLambda7(sessionWrapper, 2);
                            SaveImageInBackgroundTask$$ExternalSyntheticLambda0 saveImageInBackgroundTask$$ExternalSyntheticLambda0 = SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE;
                            ResolvableFuture<Void> resolvableFuture = completer2.cancellationFuture;
                            if (resolvableFuture != null) {
                                resolvableFuture.addListener(pipMenuView$$ExternalSyntheticLambda7, saveImageInBackgroundTask$$ExternalSyntheticLambda0);
                            }
                            sessionWrapper.mStarted = true;
                        } catch (RemoteException e2) {
                            sessionWrapper.mReader.close();
                            completer2.setException(e2);
                        }
                        return "IScrollCaptureCallbacks#onCaptureStarted";
                    }
                });
                scrollCaptureController.mSessionFuture = future;
                future.delegate.addListener(new QSFooterView$$ExternalSyntheticLambda0(scrollCaptureController, 4), scrollCaptureController.mContext.getMainExecutor());
                return;
            case 4:
                GroupCoalescer groupCoalescer = (GroupCoalescer) this.f$0;
                EventBatch eventBatch = (EventBatch) this.f$1;
                Objects.requireNonNull(groupCoalescer);
                eventBatch.mCancelShortTimeout = null;
                groupCoalescer.emitBatch(eventBatch);
                return;
            case 5:
                VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass1 r0 = (VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r0);
                int intValue = ((Integer) this.f$1).intValue();
                RingerModeLiveData ringerModeLiveData = VolumeDialogControllerImpl.RingerModeObservers.this.mRingerMode;
                Objects.requireNonNull(ringerModeLiveData);
                if (ringerModeLiveData.initialSticky) {
                    VolumeDialogControllerImpl.this.mState.ringerModeExternal = intValue;
                }
                if (D.BUG) {
                    String str2 = VolumeDialogControllerImpl.TAG;
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onChange ringer_mode rm=");
                    m.append(Util.ringerModeToString(intValue));
                    Log.d(str2, m.toString());
                }
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                String str3 = VolumeDialogControllerImpl.TAG;
                Objects.requireNonNull(volumeDialogControllerImpl);
                VolumeDialogController.State state = volumeDialogControllerImpl.mState;
                boolean z = false;
                if (intValue != state.ringerModeExternal) {
                    state.ringerModeExternal = intValue;
                    Events.writeEvent(12, Integer.valueOf(intValue));
                    z = true;
                }
                if (z) {
                    VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl2.mCallbacks.onStateChanged(volumeDialogControllerImpl2.mState);
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                DragAndDropController.PerDisplay perDisplay = (DragAndDropController.PerDisplay) this.f$1;
                int i = DragAndDropController.$r8$clinit;
                Objects.requireNonNull((DragAndDropController) this.f$0);
                if (perDisplay.activeDragCount == 0) {
                    DragAndDropController.setDropTargetWindowVisibility(perDisplay, 4);
                    return;
                }
                return;
            default:
                SplitScreenTransitions splitScreenTransitions = (SplitScreenTransitions) this.f$0;
                Objects.requireNonNull(splitScreenTransitions);
                splitScreenTransitions.mAnimations.remove((ValueAnimator) this.f$1);
                splitScreenTransitions.onFinish(null);
                return;
        }
    }
}
