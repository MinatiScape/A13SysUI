package com.android.systemui;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.graphics.common.DisplayDecorationSupport;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.DisplayUtils;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.RoundedCorners;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.CameraAvailabilityListener;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.decor.OverlayWindow;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.ViewState;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.ExecutorImpl;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public class ScreenDecorations extends CoreStartable implements TunerService.Tunable {
    public static final boolean DEBUG_COLOR;
    public static final boolean DEBUG_DISABLE_SCREEN_DECORATIONS = SystemProperties.getBoolean("debug.disable_screen_decorations", false);
    public static final boolean DEBUG_SCREENSHOT_ROUNDED_CORNERS;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public CameraAvailabilityListener mCameraListener;
    public AnonymousClass4 mColorInversionSetting;
    public DisplayCutoutView[] mCutoutViews;
    public float mDensity;
    public DisplayManager.DisplayListener mDisplayListener;
    public DisplayManager mDisplayManager;
    public String mDisplayUniqueId;
    public final PrivacyDotDecorProviderFactory mDotFactory;
    public final PrivacyDotViewController mDotViewController;
    public DelayableExecutor mExecutor;
    public Handler mHandler;
    public DisplayDecorationSupport mHwcScreenDecorationSupport;
    public boolean mIsRegistered;
    public boolean mIsRoundedCornerMultipleRadius;
    public final Executor mMainExecutor;
    public boolean mPendingRotationChange;
    public int mRotation;
    public Drawable mRoundedCornerDrawable;
    public Drawable mRoundedCornerDrawableBottom;
    public Drawable mRoundedCornerDrawableTop;
    public ScreenDecorHwcLayer mScreenDecorHwcLayer;
    public ViewGroup mScreenDecorHwcWindow;
    public final SecureSettings mSecureSettings;
    public final ThreadFactory mThreadFactory;
    public final TunerService mTunerService;
    public final UserTracker mUserTracker;
    public WindowManager mWindowManager;
    public Point mRoundedDefault = new Point(0, 0);
    public Point mRoundedDefaultTop = new Point(0, 0);
    public Point mRoundedDefaultBottom = new Point(0, 0);
    public OverlayWindow[] mOverlays = null;
    public int mTintColor = -16777216;
    public AnonymousClass1 mCameraTransitionCallback = new CameraAvailabilityListener.CameraTransitionCallback() { // from class: com.android.systemui.ScreenDecorations.1
        @Override // com.android.systemui.CameraAvailabilityListener.CameraTransitionCallback
        public final void onApplyCameraProtection(Path path, Rect rect) {
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            ScreenDecorHwcLayer screenDecorHwcLayer = screenDecorations.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.setProtection(path, rect);
                ScreenDecorations.this.mScreenDecorHwcLayer.enableShowProtection(true);
                return;
            }
            DisplayCutoutView[] displayCutoutViewArr = screenDecorations.mCutoutViews;
            if (displayCutoutViewArr == null) {
                Log.w("ScreenDecorations", "DisplayCutoutView do not initialized");
                return;
            }
            for (DisplayCutoutView displayCutoutView : displayCutoutViewArr) {
                if (displayCutoutView != null) {
                    displayCutoutView.setProtection(path, rect);
                    displayCutoutView.enableShowProtection(true);
                }
            }
        }

        @Override // com.android.systemui.CameraAvailabilityListener.CameraTransitionCallback
        public final void onHideCameraProtection() {
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            ScreenDecorHwcLayer screenDecorHwcLayer = screenDecorations.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.enableShowProtection(false);
                return;
            }
            DisplayCutoutView[] displayCutoutViewArr = screenDecorations.mCutoutViews;
            if (displayCutoutViewArr == null) {
                Log.w("ScreenDecorations", "DisplayCutoutView do not initialized");
                return;
            }
            for (DisplayCutoutView displayCutoutView : displayCutoutViewArr) {
                if (displayCutoutView != null) {
                    displayCutoutView.enableShowProtection(false);
                }
            }
        }
    };
    public AnonymousClass2 mPrivacyDotShowingListener = new AnonymousClass2();
    public final AnonymousClass6 mUserSwitchIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.ScreenDecorations.6
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            setUserId(ActivityManager.getCurrentUser());
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            ScreenDecorations.m27$$Nest$mupdateColorInversion(screenDecorations, screenDecorations.mColorInversionSetting.getValue());
        }
    };

    /* renamed from: com.android.systemui.ScreenDecorations$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements PrivacyDotViewController.ShowingListener {
        public AnonymousClass2() {
        }

        public final void onPrivacyDotShown(View view) {
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            if (screenDecorations.mHwcScreenDecorationSupport != null && view != null) {
                screenDecorations.mExecutor.execute(new ScreenDecorations$2$$ExternalSyntheticLambda1(this, view, 0));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DisplayCutoutView extends DisplayCutoutBaseView {
        public int mInitialPosition;
        public int mPosition;
        public int mRotation;
        public final ArrayList mBounds = new ArrayList();
        public final Rect mBoundingRect = new Rect();
        public Rect mTotalBounds = new Rect();

        public static void boundsFromDirection(DisplayCutout displayCutout, int i, Rect rect) {
            if (i == 3) {
                rect.set(displayCutout.getBoundingRectLeft());
            } else if (i == 5) {
                rect.set(displayCutout.getBoundingRectRight());
            } else if (i == 48) {
                rect.set(displayCutout.getBoundingRectTop());
            } else if (i != 80) {
                rect.setEmpty();
            } else {
                rect.set(displayCutout.getBoundingRectBottom());
            }
        }

        public final int getGravity(DisplayCutout displayCutout) {
            int i = this.mPosition;
            if (i == 0) {
                if (!displayCutout.getBoundingRectLeft().isEmpty()) {
                    return 3;
                }
                return 0;
            } else if (i == 1) {
                if (!displayCutout.getBoundingRectTop().isEmpty()) {
                    return 48;
                }
                return 0;
            } else if (i == 3) {
                if (!displayCutout.getBoundingRectBottom().isEmpty()) {
                    return 80;
                }
                return 0;
            } else if (i != 2 || displayCutout.getBoundingRectRight().isEmpty()) {
                return 0;
            } else {
                return 5;
            }
        }

        @Override // android.view.View
        public final void onMeasure(int i, int i2) {
            if (this.mBounds.isEmpty()) {
                super.onMeasure(i, i2);
            } else if (this.showProtection) {
                this.mTotalBounds.union(this.mBoundingRect);
                Rect rect = this.mTotalBounds;
                RectF rectF = this.protectionRect;
                rect.union((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                setMeasuredDimension(View.resolveSizeAndState(this.mTotalBounds.width(), i, 0), View.resolveSizeAndState(this.mTotalBounds.height(), i2, 0));
            } else {
                setMeasuredDimension(View.resolveSizeAndState(this.mBoundingRect.width(), i, 0), View.resolveSizeAndState(this.mBoundingRect.height(), i2, 0));
            }
        }

        public DisplayCutoutView(Context context, int i) {
            super(context);
            this.mInitialPosition = i;
            this.paint.setColor(-16777216);
            this.paint.setStyle(Paint.Style.FILL);
            setId(2131427854);
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x007e  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x0104  */
        /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
        @Override // com.android.systemui.DisplayCutoutBaseView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void updateCutout() {
            /*
                Method dump skipped, instructions count: 264
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ScreenDecorations.DisplayCutoutView.updateCutout():void");
        }
    }

    /* loaded from: classes.dex */
    public class RestartingPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        public final int mTargetRotation;
        public final View mView;

        public RestartingPreDrawListener(ViewGroup viewGroup, int i) {
            this.mView = viewGroup;
            this.mTargetRotation = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
            int i = this.mTargetRotation;
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            if (i == screenDecorations.mRotation) {
                return true;
            }
            screenDecorations.mPendingRotationChange = false;
            screenDecorations.updateOrientation();
            this.mView.invalidate();
            return false;
        }
    }

    /* loaded from: classes.dex */
    public class ValidatingPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        public final View mView;

        public ValidatingPreDrawListener(View view) {
            this.mView = view;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            int rotation = ScreenDecorations.this.mContext.getDisplay().getRotation();
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            if (rotation == screenDecorations.mRotation || screenDecorations.mPendingRotationChange) {
                return true;
            }
            this.mView.invalidate();
            return false;
        }
    }

    public static int getBoundPositionFromRotation(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    public final boolean isDefaultShownOverlayPos(int i, DisplayCutout displayCutout) {
        boolean z;
        if (displayCutout == null || displayCutout.isBoundsEmpty()) {
            z = true;
        } else {
            z = false;
        }
        return (z || !displayCutout.getBoundingRectsAll()[getBoundPositionFromRotation(1, this.mRotation)].isEmpty() || !displayCutout.getBoundingRectsAll()[getBoundPositionFromRotation(3, this.mRotation)].isEmpty()) ? i == 1 || i == 3 : i == 0 || i == 2;
    }

    public boolean isTopRoundedCorner(int i, int i2) {
        if (i != 0) {
            if (i == 1) {
                return true;
            }
            if (i != 2) {
                if (i == 3) {
                    return false;
                }
                throw new IllegalArgumentException("Unknown bounds position");
            }
        }
        if (this.mRotation == 3) {
            if (i2 == 2131428242) {
                return false;
            }
            return true;
        } else if (i2 == 2131428242) {
            return true;
        } else {
            return false;
        }
    }

    static {
        boolean z = SystemProperties.getBoolean("debug.screenshot_rounded_corners", false);
        DEBUG_SCREENSHOT_ROUNDED_CORNERS = z;
        DEBUG_COLOR = z;
    }

    public static WindowManager.LayoutParams getWindowLayoutBaseParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2024, 545259816, -3);
        int i = layoutParams.privateFlags | 80 | 536870912;
        layoutParams.privateFlags = i;
        if (!DEBUG_SCREENSHOT_ROUNDED_CORNERS) {
            layoutParams.privateFlags = i | 1048576;
        }
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.privateFlags |= 16777216;
        return layoutParams;
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "ScreenDecorations state:", "  DEBUG_DISABLE_SCREEN_DECORATIONS:"), DEBUG_DISABLE_SCREEN_DECORATIONS, printWriter, "  mIsRoundedCornerMultipleRadius:"), this.mIsRoundedCornerMultipleRadius, printWriter, "  mIsPrivacyDotEnabled:");
        PrivacyDotDecorProviderFactory privacyDotDecorProviderFactory = this.mDotFactory;
        Objects.requireNonNull(privacyDotDecorProviderFactory);
        m.append(privacyDotDecorProviderFactory.res.getBoolean(2131034133));
        printWriter.println(m.toString());
        printWriter.println("  mPendingRotationChange:" + this.mPendingRotationChange);
        printWriter.println("  mHwcScreenDecorationSupport:");
        boolean z4 = true;
        if (this.mHwcScreenDecorationSupport == null) {
            printWriter.println("    null");
        } else {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    format: ");
            m2.append(PixelFormat.formatToString(this.mHwcScreenDecorationSupport.format));
            printWriter.println(m2.toString());
            StringBuilder sb = new StringBuilder();
            sb.append("    alphaInterpretation: ");
            int i = this.mHwcScreenDecorationSupport.alphaInterpretation;
            if (i == 0) {
                str = "COVERAGE";
            } else if (i != 1) {
                str = VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown: ", i);
            } else {
                str = "MASK";
            }
            sb.append(str);
            printWriter.println(sb.toString());
        }
        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("  mRoundedDefault(x,y)=(");
        m3.append(this.mRoundedDefault.x);
        m3.append(",");
        m3.append(this.mRoundedDefault.y);
        m3.append(")");
        printWriter.println(m3.toString());
        printWriter.println("  mRoundedDefaultTop(x,y)=(" + this.mRoundedDefaultTop.x + "," + this.mRoundedDefaultTop.y + ")");
        printWriter.println("  mRoundedDefaultBottom(x,y)=(" + this.mRoundedDefaultBottom.x + "," + this.mRoundedDefaultBottom.y + ")");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mOverlays(left,top,right,bottom)=(");
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr == null || overlayWindowArr[0] == null) {
            z = false;
        } else {
            z = true;
        }
        sb2.append(z);
        sb2.append(",");
        OverlayWindow[] overlayWindowArr2 = this.mOverlays;
        if (overlayWindowArr2 == null || overlayWindowArr2[1] == null) {
            z2 = false;
        } else {
            z2 = true;
        }
        sb2.append(z2);
        sb2.append(",");
        OverlayWindow[] overlayWindowArr3 = this.mOverlays;
        if (overlayWindowArr3 == null || overlayWindowArr3[2] == null) {
            z3 = false;
        } else {
            z3 = true;
        }
        sb2.append(z3);
        sb2.append(",");
        OverlayWindow[] overlayWindowArr4 = this.mOverlays;
        if (overlayWindowArr4 == null || overlayWindowArr4[3] == null) {
            z4 = false;
        }
        sb2.append(z4);
        sb2.append(")");
        printWriter.println(sb2.toString());
    }

    public DisplayCutout getCutout() {
        return this.mContext.getDisplay().getCutout();
    }

    public final View getOverlayView(int i) {
        View view;
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr == null) {
            return null;
        }
        for (OverlayWindow overlayWindow : overlayWindowArr) {
            if (overlayWindow != null) {
                Pair pair = (Pair) overlayWindow.viewProviderMap.get(Integer.valueOf(i));
                if (pair == null) {
                    view = null;
                } else {
                    view = (View) pair.getFirst();
                }
                if (view != null) {
                    return view;
                }
            }
        }
        return null;
    }

    public boolean hasOverlays() {
        if (this.mOverlays == null) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (this.mOverlays[i] != null) {
                return true;
            }
        }
        this.mOverlays = null;
        return false;
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
        } else {
            this.mExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda2(this, 0));
        }
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
        } else {
            this.mExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda0(this, str, str2, 0));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:145:0x02fb, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7, r3) != false) goto L_0x036b;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x039a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x039b  */
    /* JADX WARN: Type inference failed for: r1v14, types: [com.android.systemui.ScreenDecorations$4] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setupDecorations() {
        /*
            Method dump skipped, instructions count: 1008
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ScreenDecorations.setupDecorations():void");
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
            return;
        }
        Handler buildHandlerOnNewThread = this.mThreadFactory.buildHandlerOnNewThread();
        this.mHandler = buildHandlerOnNewThread;
        ExecutorImpl buildDelayableExecutorOnHandler = this.mThreadFactory.buildDelayableExecutorOnHandler(buildHandlerOnNewThread);
        this.mExecutor = buildDelayableExecutorOnHandler;
        buildDelayableExecutorOnHandler.execute(new ScreenDecorations$$ExternalSyntheticLambda1(this, 0));
        PrivacyDotViewController privacyDotViewController = this.mDotViewController;
        DelayableExecutor delayableExecutor = this.mExecutor;
        Objects.requireNonNull(privacyDotViewController);
        privacyDotViewController.uiExecutor = delayableExecutor;
    }

    public final void updateLayoutParams() {
        if (this.mOverlays != null) {
            for (int i = 0; i < 4; i++) {
                OverlayWindow[] overlayWindowArr = this.mOverlays;
                if (overlayWindowArr[i] != null) {
                    WindowManager windowManager = this.mWindowManager;
                    OverlayWindow overlayWindow = overlayWindowArr[i];
                    Objects.requireNonNull(overlayWindow);
                    windowManager.updateViewLayout(overlayWindow.rootView, getWindowLayoutParams(i));
                }
            }
        }
    }

    public final void updateOrientation() {
        boolean z;
        int i;
        Object obj;
        Throwable th;
        if (this.mHandler.getLooper().getThread() == Thread.currentThread()) {
            z = true;
        } else {
            z = false;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("must call on ");
        m.append(this.mHandler.getLooper().getThread());
        m.append(", but was ");
        m.append(Thread.currentThread());
        Preconditions.checkState(z, m.toString());
        int rotation = this.mContext.getDisplay().getRotation();
        if (this.mRotation != rotation) {
            PrivacyDotViewController privacyDotViewController = this.mDotViewController;
            Objects.requireNonNull(privacyDotViewController);
            Intrinsics.stringPlus("updateRotation: ", Integer.valueOf(rotation));
            synchronized (privacyDotViewController.lock) {
                ViewState viewState = privacyDotViewController.nextViewState;
                Objects.requireNonNull(viewState);
                if (rotation != viewState.rotation) {
                    ViewState viewState2 = privacyDotViewController.nextViewState;
                    Objects.requireNonNull(viewState2);
                    boolean z2 = viewState2.layoutRtl;
                    privacyDotViewController.setCornerVisibilities();
                    View selectDesignatedCorner = privacyDotViewController.selectDesignatedCorner(rotation, z2);
                    if (selectDesignatedCorner != null) {
                        i = privacyDotViewController.cornerForView(selectDesignatedCorner);
                    } else {
                        i = -1;
                    }
                    int statusBarPaddingTop = privacyDotViewController.contentInsetsProvider.getStatusBarPaddingTop(Integer.valueOf(rotation));
                    Object obj2 = privacyDotViewController.lock;
                    synchronized (obj2) {
                        try {
                            obj = obj2;
                        } catch (Throwable th2) {
                            th = th2;
                            obj = obj2;
                        }
                        try {
                            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, null, null, null, null, false, rotation, statusBarPaddingTop, i, selectDesignatedCorner, null, 8703));
                        } catch (Throwable th3) {
                            th = th3;
                            throw th;
                        }
                    }
                }
            }
        }
        if (!this.mPendingRotationChange && rotation != this.mRotation) {
            this.mRotation = rotation;
            ScreenDecorHwcLayer screenDecorHwcLayer = this.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.displayRotation = rotation;
                screenDecorHwcLayer.updateCutout();
                screenDecorHwcLayer.updateProtectionBoundingPath();
            }
            if (this.mOverlays != null) {
                updateLayoutParams();
                DisplayCutout cutout = getCutout();
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this.mOverlays[i2] != null) {
                        updateView(i2, cutout);
                    }
                }
            }
        }
    }

    public final void updateRoundedCornerDrawable() {
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Context context = this.mContext;
        String str = this.mDisplayUniqueId;
        Resources resources = context.getResources();
        int displayUniqueIdConfigIndex = DisplayUtils.getDisplayUniqueIdConfigIndex(resources, str);
        TypedArray obtainTypedArray = resources.obtainTypedArray(2130903100);
        if (displayUniqueIdConfigIndex < 0 || displayUniqueIdConfigIndex >= obtainTypedArray.length()) {
            drawable = context.getDrawable(2131232613);
        } else {
            drawable = obtainTypedArray.getDrawable(displayUniqueIdConfigIndex);
        }
        obtainTypedArray.recycle();
        this.mRoundedCornerDrawable = drawable;
        Context context2 = this.mContext;
        String str2 = this.mDisplayUniqueId;
        Resources resources2 = context2.getResources();
        int displayUniqueIdConfigIndex2 = DisplayUtils.getDisplayUniqueIdConfigIndex(resources2, str2);
        TypedArray obtainTypedArray2 = resources2.obtainTypedArray(2130903102);
        if (displayUniqueIdConfigIndex2 < 0 || displayUniqueIdConfigIndex2 >= obtainTypedArray2.length()) {
            drawable2 = context2.getDrawable(2131232621);
        } else {
            drawable2 = obtainTypedArray2.getDrawable(displayUniqueIdConfigIndex2);
        }
        obtainTypedArray2.recycle();
        this.mRoundedCornerDrawableTop = drawable2;
        Context context3 = this.mContext;
        String str3 = this.mDisplayUniqueId;
        Resources resources3 = context3.getResources();
        int displayUniqueIdConfigIndex3 = DisplayUtils.getDisplayUniqueIdConfigIndex(resources3, str3);
        TypedArray obtainTypedArray3 = resources3.obtainTypedArray(2130903099);
        if (displayUniqueIdConfigIndex3 < 0 || displayUniqueIdConfigIndex3 >= obtainTypedArray3.length()) {
            drawable3 = context3.getDrawable(2131232619);
        } else {
            drawable3 = obtainTypedArray3.getDrawable(displayUniqueIdConfigIndex3);
        }
        obtainTypedArray3.recycle();
        this.mRoundedCornerDrawableBottom = drawable3;
        updateRoundedCornerImageView();
    }

    public final void updateRoundedCornerImageView() {
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3 = this.mRoundedCornerDrawableTop;
        if (drawable3 == null) {
            drawable3 = this.mRoundedCornerDrawable;
        }
        Drawable drawable4 = this.mRoundedCornerDrawableBottom;
        if (drawable4 == null) {
            drawable4 = this.mRoundedCornerDrawable;
        }
        ScreenDecorHwcLayer screenDecorHwcLayer = this.mScreenDecorHwcLayer;
        if (screenDecorHwcLayer != null) {
            Objects.requireNonNull(screenDecorHwcLayer);
            screenDecorHwcLayer.roundedCornerDrawableTop = drawable3;
            screenDecorHwcLayer.roundedCornerDrawableBottom = drawable4;
            screenDecorHwcLayer.updateRoundedCornerDrawableBounds();
            screenDecorHwcLayer.invalidate();
        } else if (this.mOverlays != null) {
            ColorStateList valueOf = ColorStateList.valueOf(this.mTintColor);
            for (int i = 0; i < 4; i++) {
                OverlayWindow[] overlayWindowArr = this.mOverlays;
                if (overlayWindowArr[i] != null) {
                    OverlayWindow overlayWindow = overlayWindowArr[i];
                    Objects.requireNonNull(overlayWindow);
                    ViewGroup viewGroup = overlayWindow.rootView;
                    ((ImageView) viewGroup.findViewById(2131428242)).setImageTintList(valueOf);
                    ((ImageView) viewGroup.findViewById(2131428703)).setImageTintList(valueOf);
                    ImageView imageView = (ImageView) viewGroup.findViewById(2131428242);
                    if (isTopRoundedCorner(i, 2131428242)) {
                        drawable = drawable3;
                    } else {
                        drawable = drawable4;
                    }
                    imageView.setImageDrawable(drawable);
                    ImageView imageView2 = (ImageView) viewGroup.findViewById(2131428703);
                    if (isTopRoundedCorner(i, 2131428703)) {
                        drawable2 = drawable3;
                    } else {
                        drawable2 = drawable4;
                    }
                    imageView2.setImageDrawable(drawable2);
                }
            }
        }
    }

    public final void updateRoundedCornerRadii() {
        boolean z;
        int roundedCornerRadius = RoundedCorners.getRoundedCornerRadius(this.mContext.getResources(), this.mDisplayUniqueId);
        int roundedCornerTopRadius = RoundedCorners.getRoundedCornerTopRadius(this.mContext.getResources(), this.mDisplayUniqueId);
        int roundedCornerBottomRadius = RoundedCorners.getRoundedCornerBottomRadius(this.mContext.getResources(), this.mDisplayUniqueId);
        Point point = this.mRoundedDefault;
        if (point.x == roundedCornerRadius && this.mRoundedDefaultTop.x == roundedCornerTopRadius && this.mRoundedDefaultBottom.x == roundedCornerBottomRadius) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            if (this.mIsRoundedCornerMultipleRadius) {
                point.set(this.mRoundedCornerDrawable.getIntrinsicWidth(), this.mRoundedCornerDrawable.getIntrinsicHeight());
                this.mRoundedDefaultTop.set(this.mRoundedCornerDrawableTop.getIntrinsicWidth(), this.mRoundedCornerDrawableTop.getIntrinsicHeight());
                this.mRoundedDefaultBottom.set(this.mRoundedCornerDrawableBottom.getIntrinsicWidth(), this.mRoundedCornerDrawableBottom.getIntrinsicHeight());
            } else {
                point.set(roundedCornerRadius, roundedCornerRadius);
                this.mRoundedDefaultTop.set(roundedCornerTopRadius, roundedCornerTopRadius);
                this.mRoundedDefaultBottom.set(roundedCornerBottomRadius, roundedCornerBottomRadius);
            }
            onTuningChanged("sysui_rounded_size", null);
        }
    }

    public final void updateRoundedCornerSize(Point point, Point point2, Point point3) {
        Point point4;
        Point point5;
        if (point2.x == 0) {
            point2 = point;
        }
        if (point3.x != 0) {
            point = point3;
        }
        ScreenDecorHwcLayer screenDecorHwcLayer = this.mScreenDecorHwcLayer;
        if (screenDecorHwcLayer != null) {
            int i = point2.x;
            int i2 = point.x;
            Objects.requireNonNull(screenDecorHwcLayer);
            screenDecorHwcLayer.roundedCornerTopSize = i;
            screenDecorHwcLayer.roundedCornerBottomSize = i2;
            screenDecorHwcLayer.updateRoundedCornerDrawableBounds();
            screenDecorHwcLayer.invalidate();
        } else if (this.mOverlays != null) {
            for (int i3 = 0; i3 < 4; i3++) {
                OverlayWindow[] overlayWindowArr = this.mOverlays;
                if (overlayWindowArr[i3] != null) {
                    OverlayWindow overlayWindow = overlayWindowArr[i3];
                    Objects.requireNonNull(overlayWindow);
                    ViewGroup viewGroup = overlayWindow.rootView;
                    View findViewById = viewGroup.findViewById(2131428242);
                    if (isTopRoundedCorner(i3, 2131428242)) {
                        point4 = point2;
                    } else {
                        point4 = point;
                    }
                    setSize(findViewById, point4);
                    View findViewById2 = viewGroup.findViewById(2131428703);
                    if (isTopRoundedCorner(i3, 2131428703)) {
                        point5 = point2;
                    } else {
                        point5 = point;
                    }
                    setSize(findViewById2, point5);
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0062, code lost:
        if (r7 != false) goto L_0x007f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0071, code lost:
        if (r7 != false) goto L_0x0079;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0076, code lost:
        if (r7 != false) goto L_0x007d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x007b, code lost:
        if (r7 != false) goto L_0x007d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x007d, code lost:
        r5 = 51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x007f, code lost:
        r5 = 83;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateRoundedCornerView(int r6, int r7, android.view.DisplayCutout r8) {
        /*
            r5 = this;
            com.android.systemui.decor.OverlayWindow[] r0 = r5.mOverlays
            r0 = r0[r6]
            java.util.Objects.requireNonNull(r0)
            android.view.ViewGroup r0 = r0.rootView
            android.view.View r0 = r0.findViewById(r7)
            if (r0 != 0) goto L_0x0010
            return
        L_0x0010:
            r1 = 8
            r0.setVisibility(r1)
            android.graphics.Point r1 = r5.mRoundedDefault
            int r1 = r1.x
            r2 = 1
            r3 = 0
            if (r1 > 0) goto L_0x0030
            android.graphics.Point r1 = r5.mRoundedDefaultBottom
            int r1 = r1.x
            if (r1 > 0) goto L_0x0030
            android.graphics.Point r1 = r5.mRoundedDefaultTop
            int r1 = r1.x
            if (r1 > 0) goto L_0x0030
            boolean r1 = r5.mIsRoundedCornerMultipleRadius
            if (r1 == 0) goto L_0x002e
            goto L_0x0030
        L_0x002e:
            r1 = r3
            goto L_0x0031
        L_0x0030:
            r1 = r2
        L_0x0031:
            if (r1 == 0) goto L_0x003f
            boolean r8 = r5.isDefaultShownOverlayPos(r6, r8)
            if (r8 == 0) goto L_0x003f
            android.hardware.graphics.common.DisplayDecorationSupport r8 = r5.mHwcScreenDecorationSupport
            if (r8 != 0) goto L_0x003f
            r8 = r2
            goto L_0x0040
        L_0x003f:
            r8 = r3
        L_0x0040:
            if (r8 == 0) goto L_0x00ba
            r8 = 2131428242(0x7f0b0392, float:1.8478123E38)
            if (r7 != r8) goto L_0x0049
            r7 = r2
            goto L_0x004a
        L_0x0049:
            r7 = r3
        L_0x004a:
            int r5 = r5.mRotation
            int r5 = getBoundPositionFromRotation(r6, r5)
            r6 = 85
            r8 = 53
            r1 = 83
            r4 = 51
            if (r5 == 0) goto L_0x007b
            if (r5 == r2) goto L_0x0076
            r2 = 2
            if (r5 == r2) goto L_0x0071
            r2 = 3
            if (r5 != r2) goto L_0x0065
            if (r7 == 0) goto L_0x0074
            goto L_0x007f
        L_0x0065:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Incorrect position: "
            java.lang.String r5 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r7, r5)
            r6.<init>(r5)
            throw r6
        L_0x0071:
            if (r7 == 0) goto L_0x0074
            goto L_0x0079
        L_0x0074:
            r5 = r6
            goto L_0x0080
        L_0x0076:
            if (r7 == 0) goto L_0x0079
            goto L_0x007d
        L_0x0079:
            r5 = r8
            goto L_0x0080
        L_0x007b:
            if (r7 == 0) goto L_0x007f
        L_0x007d:
            r5 = r4
            goto L_0x0080
        L_0x007f:
            r5 = r1
        L_0x0080:
            android.view.ViewGroup$LayoutParams r7 = r0.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r7 = (android.widget.FrameLayout.LayoutParams) r7
            r7.gravity = r5
            r7 = 0
            r0.setRotation(r7)
            r7 = 1065353216(0x3f800000, float:1.0)
            r0.setScaleX(r7)
            r0.setScaleY(r7)
            if (r5 == r4) goto L_0x00b7
            r7 = -1082130432(0xffffffffbf800000, float:-1.0)
            if (r5 == r8) goto L_0x00b4
            if (r5 == r1) goto L_0x00b0
            if (r5 != r6) goto L_0x00a4
            r5 = 1127481344(0x43340000, float:180.0)
            r0.setRotation(r5)
            goto L_0x00b7
        L_0x00a4:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Unsupported gravity: "
            java.lang.String r5 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r7, r5)
            r6.<init>(r5)
            throw r6
        L_0x00b0:
            r0.setScaleY(r7)
            goto L_0x00b7
        L_0x00b4:
            r0.setScaleX(r7)
        L_0x00b7:
            r0.setVisibility(r3)
        L_0x00ba:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ScreenDecorations.updateRoundedCornerView(int, int, android.view.DisplayCutout):void");
    }

    public final void updateView(int i, DisplayCutout displayCutout) {
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr != null && overlayWindowArr[i] != null && this.mHwcScreenDecorationSupport == null) {
            updateRoundedCornerView(i, 2131428242, displayCutout);
            updateRoundedCornerView(i, 2131428703, displayCutout);
            updateRoundedCornerSize(this.mRoundedDefault, this.mRoundedDefaultTop, this.mRoundedDefaultBottom);
            updateRoundedCornerImageView();
            DisplayCutoutView[] displayCutoutViewArr = this.mCutoutViews;
            if (displayCutoutViewArr != null && displayCutoutViewArr[i] != null) {
                DisplayCutoutView displayCutoutView = displayCutoutViewArr[i];
                int i2 = this.mRotation;
                Objects.requireNonNull(displayCutoutView);
                displayCutoutView.mRotation = i2;
                displayCutoutView.updateCutout();
            }
        }
    }

    /* renamed from: -$$Nest$mupdateColorInversion  reason: not valid java name */
    public static void m27$$Nest$mupdateColorInversion(ScreenDecorations screenDecorations, int i) {
        int i2;
        Objects.requireNonNull(screenDecorations);
        if (i != 0) {
            i2 = -1;
        } else {
            i2 = -16777216;
        }
        screenDecorations.mTintColor = i2;
        if (DEBUG_COLOR) {
            screenDecorations.mTintColor = -65536;
        }
        if (screenDecorations.mOverlays != null && screenDecorations.mHwcScreenDecorationSupport == null) {
            ColorStateList valueOf = ColorStateList.valueOf(screenDecorations.mTintColor);
            for (int i3 = 0; i3 < 4; i3++) {
                OverlayWindow[] overlayWindowArr = screenDecorations.mOverlays;
                if (overlayWindowArr[i3] != null) {
                    OverlayWindow overlayWindow = overlayWindowArr[i3];
                    Objects.requireNonNull(overlayWindow);
                    ViewGroup viewGroup = overlayWindow.rootView;
                    int childCount = viewGroup.getChildCount();
                    for (int i4 = 0; i4 < childCount; i4++) {
                        View childAt = viewGroup.getChildAt(i4);
                        if (!(childAt.getId() == 2131428622 || childAt.getId() == 2131428623 || childAt.getId() == 2131428620 || childAt.getId() == 2131428621)) {
                            if (childAt instanceof ImageView) {
                                ((ImageView) childAt).setImageTintList(valueOf);
                            } else if (childAt instanceof DisplayCutoutView) {
                                DisplayCutoutView displayCutoutView = (DisplayCutoutView) childAt;
                                displayCutoutView.paint.setColor(screenDecorations.mTintColor);
                                displayCutoutView.invalidate();
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.ScreenDecorations$1] */
    /* JADX WARN: Type inference failed for: r2v8, types: [com.android.systemui.ScreenDecorations$6] */
    public ScreenDecorations(Context context, Executor executor, SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, TunerService tunerService, UserTracker userTracker, PrivacyDotViewController privacyDotViewController, ThreadFactory threadFactory, PrivacyDotDecorProviderFactory privacyDotDecorProviderFactory) {
        super(context);
        this.mMainExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTunerService = tunerService;
        this.mUserTracker = userTracker;
        this.mDotViewController = privacyDotViewController;
        this.mThreadFactory = threadFactory;
        this.mDotFactory = privacyDotDecorProviderFactory;
        AnonymousClass2 r1 = this.mPrivacyDotShowingListener;
        Objects.requireNonNull(privacyDotViewController);
        privacyDotViewController.showingListener = r1;
    }

    public static boolean isRoundedCornerMultipleRadius(Context context, String str) {
        boolean z;
        Resources resources = context.getResources();
        int displayUniqueIdConfigIndex = DisplayUtils.getDisplayUniqueIdConfigIndex(resources, str);
        TypedArray obtainTypedArray = resources.obtainTypedArray(2130903101);
        if (displayUniqueIdConfigIndex < 0 || displayUniqueIdConfigIndex >= obtainTypedArray.length()) {
            z = resources.getBoolean(2131034157);
        } else {
            z = obtainTypedArray.getBoolean(displayUniqueIdConfigIndex, false);
        }
        obtainTypedArray.recycle();
        return z;
    }

    public WindowManager.LayoutParams getWindowLayoutParams(int i) {
        int i2;
        String str;
        WindowManager.LayoutParams windowLayoutBaseParams = getWindowLayoutBaseParams();
        int boundPositionFromRotation = getBoundPositionFromRotation(i, this.mRotation);
        int i3 = -2;
        int i4 = 3;
        if (boundPositionFromRotation == 1 || boundPositionFromRotation == 3) {
            i2 = -1;
        } else {
            i2 = -2;
        }
        windowLayoutBaseParams.width = i2;
        int boundPositionFromRotation2 = getBoundPositionFromRotation(i, this.mRotation);
        if (!(boundPositionFromRotation2 == 1 || boundPositionFromRotation2 == 3)) {
            i3 = -1;
        }
        windowLayoutBaseParams.height = i3;
        if (i == 0) {
            str = "ScreenDecorOverlayLeft";
        } else if (i == 1) {
            str = "ScreenDecorOverlay";
        } else if (i == 2) {
            str = "ScreenDecorOverlayRight";
        } else if (i == 3) {
            str = "ScreenDecorOverlayBottom";
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("unknown bound position: ", i));
        }
        windowLayoutBaseParams.setTitle(str);
        int boundPositionFromRotation3 = getBoundPositionFromRotation(i, this.mRotation);
        if (boundPositionFromRotation3 != 0) {
            if (boundPositionFromRotation3 == 1) {
                i4 = 48;
            } else if (boundPositionFromRotation3 == 2) {
                i4 = 5;
            } else if (boundPositionFromRotation3 == 3) {
                i4 = 80;
            } else {
                throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("unknown bound position: ", i));
            }
        }
        windowLayoutBaseParams.gravity = i4;
        return windowLayoutBaseParams;
    }

    public void setSize(View view, Point point) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = point.x;
        layoutParams.height = point.y;
        view.setLayoutParams(layoutParams);
    }
}
