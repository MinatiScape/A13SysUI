package com.google.android.systemui.gamedashboard;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.Insets;
import android.graphics.ParcelableColorSpace;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ShortcutBarController {
    public EntryPointController mEntryPointController;
    public final FpsController mFpsController;
    public boolean mIsAttached;
    public final ToastController mToast;
    public final GameDashboardUiEventLogger mUiEventLogger;
    public final ShortcutBarView mView;
    public final WindowManager mWindowManager;

    public final void hideUI() {
        if (this.mIsAttached) {
            this.mWindowManager.removeViewImmediate(this.mView);
            this.mIsAttached = false;
        }
        ToastController toastController = this.mToast;
        Objects.requireNonNull(toastController);
        toastController.mLaunchLayout.setVisibility(8);
        toastController.mShortcutView.setVisibility(8);
        toastController.mRecordSaveView.setVisibility(8);
        toastController.removeViewImmediate();
    }

    public final void onButtonVisibilityChange(boolean z) {
        if (z && !this.mView.isAttachedToWindow() && this.mView.shouldBeVisible()) {
            show();
        } else if (!z && this.mView.isAttachedToWindow() && !this.mView.shouldBeVisible()) {
            hideUI();
        }
    }

    public final void registerFps(int i) {
        FpsController fpsController = this.mFpsController;
        Objects.requireNonNull(fpsController);
        fpsController.mWindowManager.unregisterTaskFpsCallback(fpsController.mListener);
        if (fpsController.mCallback != null) {
            fpsController.mCallback = null;
        }
        FpsController fpsController2 = this.mFpsController;
        ShortcutBarView shortcutBarView = this.mView;
        Objects.requireNonNull(shortcutBarView);
        StatusBar$$ExternalSyntheticLambda4 statusBar$$ExternalSyntheticLambda4 = new StatusBar$$ExternalSyntheticLambda4(shortcutBarView);
        Objects.requireNonNull(fpsController2);
        fpsController2.mCallback = statusBar$$ExternalSyntheticLambda4;
        fpsController2.mWindowManager.registerTaskFpsCallback(i, fpsController2.mExecutor, fpsController2.mListener);
    }

    public final void show() {
        if (!this.mIsAttached) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 264, -3);
            layoutParams.setTrustedOverlay();
            layoutParams.setFitInsetsTypes(0);
            layoutParams.layoutInDisplayCutoutMode = 3;
            layoutParams.privateFlags |= 16;
            layoutParams.setTitle("Shortcut Bar");
            this.mWindowManager.addView(this.mView, layoutParams);
            this.mView.slideIn();
            this.mIsAttached = true;
        }
    }

    public final void updateVisibility(boolean z) {
        if (z && !this.mView.isAttachedToWindow() && this.mView.shouldBeVisible()) {
            show();
        } else if (!z && this.mView.isAttachedToWindow()) {
            hideUI();
        }
    }

    public ShortcutBarController(Context context, WindowManager windowManager, FpsController fpsController, ConfigurationController configurationController, final Handler handler, ScreenRecordController screenRecordController, final Optional<TaskSurfaceHelper> optional, GameDashboardUiEventLogger gameDashboardUiEventLogger, ToastController toastController) {
        this.mWindowManager = windowManager;
        this.mFpsController = fpsController;
        this.mToast = toastController;
        this.mUiEventLogger = gameDashboardUiEventLogger;
        int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
        final ShortcutBarView shortcutBarView = (ShortcutBarView) LayoutInflater.from(context).inflate(2131624107, (ViewGroup) null);
        Objects.requireNonNull(shortcutBarView);
        shortcutBarView.mScreenRecordController = screenRecordController;
        shortcutBarView.mConfigurationController = configurationController;
        shortcutBarView.mUiEventLogger = gameDashboardUiEventLogger;
        final ScreenshotHelper screenshotHelper = new ScreenshotHelper(shortcutBarView.getContext());
        shortcutBarView.mScreenshotButton.setOnTouchListener(shortcutBarView.mOnTouchListener);
        shortcutBarView.mScreenshotButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                final ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
                Optional optional2 = optional;
                final ShortcutBarController shortcutBarController = this;
                final ScreenshotHelper screenshotHelper2 = screenshotHelper;
                final Handler handler2 = handler;
                int i2 = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                Objects.requireNonNull(shortcutBarView2);
                shortcutBarView2.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_SCREENSHOT);
                optional2.ifPresent(new Consumer() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda5
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r8v0, types: [com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda4] */
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        final ShortcutBarView shortcutBarView3 = ShortcutBarView.this;
                        ShortcutBarController shortcutBarController2 = shortcutBarController;
                        final ScreenshotHelper screenshotHelper3 = screenshotHelper2;
                        final Handler handler3 = handler2;
                        int i3 = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                        Objects.requireNonNull(shortcutBarView3);
                        Objects.requireNonNull(shortcutBarController2);
                        EntryPointController entryPointController = shortcutBarController2.mEntryPointController;
                        Objects.requireNonNull(entryPointController);
                        final ActivityManager.RunningTaskInfo runningTaskInfo = entryPointController.mGameTaskInfo;
                        DisplayMetrics displayMetrics = shortcutBarView3.getContext().getResources().getDisplayMetrics();
                        final Rect rect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                        ((TaskSurfaceHelper) obj).screenshotTask(runningTaskInfo, rect, shortcutBarView3.getContext().getMainExecutor(), new Consumer() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda4
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj2) {
                                ParcelableColorSpace parcelableColorSpace;
                                ShortcutBarView shortcutBarView4 = ShortcutBarView.this;
                                ScreenshotHelper screenshotHelper4 = screenshotHelper3;
                                Rect rect2 = rect;
                                ActivityManager.RunningTaskInfo runningTaskInfo2 = runningTaskInfo;
                                Handler handler4 = handler3;
                                int i4 = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                                Objects.requireNonNull(shortcutBarView4);
                                Bitmap asBitmap = ((SurfaceControl.ScreenshotHardwareBuffer) obj2).asBitmap();
                                if (asBitmap.getConfig() == Bitmap.Config.HARDWARE) {
                                    if (asBitmap.getColorSpace() == null) {
                                        parcelableColorSpace = new ParcelableColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
                                    } else {
                                        parcelableColorSpace = new ParcelableColorSpace(asBitmap.getColorSpace());
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("bitmap_util_buffer", asBitmap.getHardwareBuffer());
                                    bundle.putParcelable("bitmap_util_color_space", parcelableColorSpace);
                                    screenshotHelper4.provideScreenshot(bundle, rect2, Insets.NONE, runningTaskInfo2.taskId, 1, new ComponentName(shortcutBarView4.getClass().getPackageName(), shortcutBarView4.getClass().getSimpleName()), 5, handler4, (Consumer) null);
                                    return;
                                }
                                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Passed bitmap must have hardware config, found: ");
                                m.append(asBitmap.getConfig());
                                throw new IllegalArgumentException(m.toString());
                            }
                        });
                    }
                });
            }
        });
        shortcutBarView.mRecordButton.setOnTouchListener(shortcutBarView.mOnTouchListener);
        shortcutBarView.mRecordButton.setOnClickListener(new ShortcutBarView$$ExternalSyntheticLambda0(shortcutBarView, 0));
        shortcutBarView.setOnTouchListener(shortcutBarView.mOnTouchListener);
        shortcutBarView.mRevealButton.setOnClickListener(new ShortcutBarView$$ExternalSyntheticLambda1(shortcutBarView, 0));
        shortcutBarView.mRevealButton.setOnTouchListener(shortcutBarView.mRevealButtonOnTouchListener);
        RevealButton revealButton = shortcutBarView.mRevealButton;
        Objects.requireNonNull(revealButton);
        revealButton.mRightSide = true;
        revealButton.invalidate();
        shortcutBarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.4
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                ShortcutBarView shortcutBarView2;
                shortcutBarView.mRevealButton.setTranslationY((shortcutBarView2.getHeight() * 1.0f) / 3.0f);
                shortcutBarView.slideIn();
                int dimensionPixelSize = shortcutBarView.getResources().getDimensionPixelSize(2131165740);
                int dimensionPixelSize2 = shortcutBarView.getResources().getDimensionPixelSize(2131165741);
                Rect rect = new Rect();
                shortcutBarView.mRevealButton.getHitRect(rect);
                rect.top -= dimensionPixelSize2;
                rect.bottom += dimensionPixelSize2;
                rect.left -= dimensionPixelSize;
                rect.right += dimensionPixelSize;
                ((View) shortcutBarView.mRevealButton.getParent()).setTouchDelegate(new TouchDelegate(rect, shortcutBarView.mRevealButton));
                shortcutBarView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        this.mView = shortcutBarView;
    }
}
