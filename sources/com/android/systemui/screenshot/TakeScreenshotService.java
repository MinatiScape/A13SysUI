package com.android.systemui.screenshot;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4;
import com.android.systemui.screenshot.TakeScreenshotService;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class TakeScreenshotService extends Service {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AnonymousClass1 mCloseSystemDialogs = new BroadcastReceiver() { // from class: com.android.systemui.screenshot.TakeScreenshotService.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            ScreenshotController screenshotController;
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()) && (screenshotController = TakeScreenshotService.this.mScreenshot) != null) {
                ScreenshotView screenshotView = screenshotController.mScreenshotView;
                Objects.requireNonNull(screenshotView);
                if (!screenshotView.mPendingSharedTransition) {
                    TakeScreenshotService.this.mScreenshot.dismissScreenshot();
                }
            }
        }
    };
    public final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.android.systemui.screenshot.TakeScreenshotService$$ExternalSyntheticLambda0
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            ScreenshotEvent screenshotEvent;
            String str;
            boolean z;
            boolean z2;
            Insets insets;
            TakeScreenshotService takeScreenshotService = TakeScreenshotService.this;
            int i = TakeScreenshotService.$r8$clinit;
            Objects.requireNonNull(takeScreenshotService);
            Messenger messenger = message.replyTo;
            final DozeTriggers$$ExternalSyntheticLambda4 dozeTriggers$$ExternalSyntheticLambda4 = new DozeTriggers$$ExternalSyntheticLambda4(messenger, 1);
            TakeScreenshotService.RequestCallbackImpl requestCallbackImpl = new TakeScreenshotService.RequestCallbackImpl(messenger);
            if (!takeScreenshotService.mUserManager.isUserUnlocked()) {
                Log.w("Screenshot", "Skipping screenshot because storage is locked!");
                takeScreenshotService.mNotificationsController.notifyScreenshotError(2131953228);
                try {
                    messenger.send(Message.obtain(null, 1, null));
                } catch (RemoteException e) {
                    Log.d("Screenshot", "ignored remote exception", e);
                }
                try {
                    requestCallbackImpl.mReplyTo.send(Message.obtain((Handler) null, 2));
                    return true;
                } catch (RemoteException e2) {
                    Log.d("Screenshot", "ignored remote exception", e2);
                    return true;
                }
            } else {
                ScreenshotHelper.ScreenshotRequest screenshotRequest = (ScreenshotHelper.ScreenshotRequest) message.obj;
                final ComponentName topComponent = screenshotRequest.getTopComponent();
                UiEventLogger uiEventLogger = takeScreenshotService.mUiEventLogger;
                int source = screenshotRequest.getSource();
                if (source == 0) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_GLOBAL_ACTIONS;
                } else if (source == 1) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_KEY_CHORD;
                } else if (source == 2) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_KEY_OTHER;
                } else if (source == 3) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_OVERVIEW;
                } else if (source == 4) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_ACCESSIBILITY_ACTIONS;
                } else if (source != 6) {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_OTHER;
                } else {
                    screenshotEvent = ScreenshotEvent.SCREENSHOT_REQUESTED_VENDOR_GESTURE;
                }
                if (topComponent == null) {
                    str = "";
                } else {
                    str = topComponent.getPackageName();
                }
                uiEventLogger.log(screenshotEvent, 0, str);
                int i2 = message.what;
                if (i2 == 1) {
                    ScreenshotController screenshotController = takeScreenshotService.mScreenshot;
                    Objects.requireNonNull(screenshotController);
                    screenshotController.mCurrentRequestCallback = requestCallbackImpl;
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    screenshotController.mDisplayManager.getDisplay(0).getRealMetrics(displayMetrics);
                    screenshotController.takeScreenshotInternal(topComponent, dozeTriggers$$ExternalSyntheticLambda4, new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
                    return true;
                } else if (i2 == 2) {
                    final ScreenshotController screenshotController2 = takeScreenshotService.mScreenshot;
                    Objects.requireNonNull(screenshotController2);
                    screenshotController2.mScreenshotView.reset();
                    screenshotController2.mCurrentRequestCallback = requestCallbackImpl;
                    screenshotController2.attachWindow();
                    screenshotController2.mWindow.setContentView(screenshotController2.mScreenshotView);
                    screenshotController2.mScreenshotView.requestApplyInsets();
                    ScreenshotView screenshotView = screenshotController2.mScreenshotView;
                    Consumer<Rect> screenshotController$$ExternalSyntheticLambda7 = new Consumer() { // from class: com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda7
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ScreenshotController screenshotController3 = ScreenshotController.this;
                            Objects.requireNonNull(screenshotController3);
                            screenshotController3.takeScreenshotInternal(topComponent, dozeTriggers$$ExternalSyntheticLambda4, (Rect) obj);
                        }
                    };
                    Objects.requireNonNull(screenshotView);
                    ScreenshotSelectorView screenshotSelectorView = screenshotView.mScreenshotSelectorView;
                    Objects.requireNonNull(screenshotSelectorView);
                    screenshotSelectorView.mOnScreenshotSelected = screenshotController$$ExternalSyntheticLambda7;
                    screenshotView.mScreenshotSelectorView.setVisibility(0);
                    screenshotView.mScreenshotSelectorView.requestFocus();
                    return true;
                } else if (i2 != 3) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid screenshot option: ");
                    m.append(message.what);
                    Log.w("Screenshot", m.toString());
                    return false;
                } else {
                    Bundle bitmapBundle = screenshotRequest.getBitmapBundle();
                    if (!bitmapBundle.containsKey("bitmap_util_buffer") || !bitmapBundle.containsKey("bitmap_util_color_space")) {
                        throw new IllegalArgumentException("Bundle does not contain a hardware bitmap");
                    }
                    HardwareBuffer hardwareBuffer = (HardwareBuffer) bitmapBundle.getParcelable("bitmap_util_buffer");
                    Objects.requireNonNull(hardwareBuffer);
                    Bitmap wrapHardwareBuffer = Bitmap.wrapHardwareBuffer(hardwareBuffer, bitmapBundle.getParcelable("bitmap_util_color_space").getColorSpace());
                    Rect boundsInScreen = screenshotRequest.getBoundsInScreen();
                    Insets insets2 = screenshotRequest.getInsets();
                    screenshotRequest.getTaskId();
                    screenshotRequest.getUserId();
                    if (wrapHardwareBuffer == null) {
                        Log.e("Screenshot", "Got null bitmap from screenshot message");
                        takeScreenshotService.mNotificationsController.notifyScreenshotError(2131953225);
                        try {
                            messenger.send(Message.obtain(null, 1, null));
                        } catch (RemoteException e3) {
                            Log.d("Screenshot", "ignored remote exception", e3);
                        }
                        try {
                            requestCallbackImpl.mReplyTo.send(Message.obtain((Handler) null, 2));
                            return true;
                        } catch (RemoteException e4) {
                            Log.d("Screenshot", "ignored remote exception", e4);
                            return true;
                        }
                    } else {
                        ScreenshotController screenshotController3 = takeScreenshotService.mScreenshot;
                        Objects.requireNonNull(screenshotController3);
                        int width = (wrapHardwareBuffer.getWidth() - insets2.left) - insets2.right;
                        int height = (wrapHardwareBuffer.getHeight() - insets2.top) - insets2.bottom;
                        if (height == 0 || width == 0 || wrapHardwareBuffer.getWidth() == 0 || wrapHardwareBuffer.getHeight() == 0 || Math.abs((width / height) - (boundsInScreen.width() / boundsInScreen.height())) >= 0.1f) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if (!z) {
                            insets = Insets.NONE;
                            boundsInScreen.set(0, 0, wrapHardwareBuffer.getWidth(), wrapHardwareBuffer.getHeight());
                            z2 = true;
                        } else {
                            z2 = false;
                            insets = insets2;
                        }
                        screenshotController3.mCurrentRequestCallback = requestCallbackImpl;
                        screenshotController3.saveScreenshot(wrapHardwareBuffer, dozeTriggers$$ExternalSyntheticLambda4, boundsInScreen, insets, topComponent, z2);
                        return true;
                    }
                }
            }
        }
    });
    public final ScreenshotNotificationsController mNotificationsController;
    public ScreenshotController mScreenshot;
    public final UiEventLogger mUiEventLogger;
    public final UserManager mUserManager;

    /* loaded from: classes.dex */
    public interface RequestCallback {
    }

    @Override // android.app.Service
    public final void onCreate() {
    }

    /* loaded from: classes.dex */
    public static class RequestCallbackImpl implements RequestCallback {
        public final Messenger mReplyTo;

        public RequestCallbackImpl(Messenger messenger) {
            this.mReplyTo = messenger;
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        registerReceiver(this.mCloseSystemDialogs, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
        return new Messenger(this.mHandler).getBinder();
    }

    @Override // android.app.Service
    public final boolean onUnbind(Intent intent) {
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.removeWindow();
            this.mScreenshot = null;
        }
        unregisterReceiver(this.mCloseSystemDialogs);
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.screenshot.TakeScreenshotService$1] */
    public TakeScreenshotService(ScreenshotController screenshotController, UserManager userManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController) {
        this.mScreenshot = screenshotController;
        this.mUserManager = userManager;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationsController = screenshotNotificationsController;
    }

    @Override // android.app.Service
    public final void onDestroy() {
        super.onDestroy();
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.removeWindow();
            ScreenshotController screenshotController2 = this.mScreenshot;
            Objects.requireNonNull(screenshotController2);
            screenshotController2.mContext.unregisterReceiver(screenshotController2.mCopyBroadcastReceiver);
            screenshotController2.mContext.release();
            MediaPlayer mediaPlayer = screenshotController2.mCameraSound;
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            screenshotController2.mBgExecutor.shutdownNow();
            this.mScreenshot = null;
        }
    }
}
