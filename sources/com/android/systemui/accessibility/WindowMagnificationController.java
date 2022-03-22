package com.android.systemui.accessibility;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.Range;
import android.view.Display;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.model.SysUiState;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import com.android.wm.shell.pip.phone.PipMotionHelper$1$$ExternalSyntheticLambda0;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WindowMagnificationController implements View.OnTouchListener, SurfaceHolder.Callback, MagnificationGestureDetector.OnGestureListener, ComponentCallbacks {
    public static final Range<Float> A11Y_ACTION_SCALE_RANGE;
    public static final boolean DEBUG;
    public final WindowMagnificationAnimationController mAnimationController;
    public int mBorderDragSize;
    public View mBottomDrag;
    public float mBounceEffectAnimationScale;
    public final int mBounceEffectDuration;
    public final Configuration mConfiguration;
    public final Context mContext;
    public final int mDisplayId;
    public View mDragView;
    public int mDragViewSize;
    public final MagnificationGestureDetector mGestureDetector;
    public final Handler mHandler;
    public View mLeftDrag;
    public Locale mLocale;
    public SurfaceControl mMirrorSurface;
    public int mMirrorSurfaceMargin;
    public SurfaceView mMirrorSurfaceView;
    public View mMirrorView;
    public int mOuterBorderSize;
    public boolean mOverlapWithGestureInsets;
    public NumberFormat mPercentFormat;
    public final Resources mResources;
    public View mRightDrag;
    @VisibleForTesting
    public int mRotation;
    public float mScale;
    public final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    public SysUiState mSysUiState;
    public View mTopDrag;
    public final SurfaceControl.Transaction mTransaction;
    public Rect mWindowBounds;
    public final WindowMagnifierCallback mWindowMagnifierCallback;
    public final WindowManager mWm;
    public final Rect mMagnificationFrame = new Rect();
    public final Rect mTmpRect = new Rect();
    public final Rect mMirrorViewBounds = new Rect();
    public final Rect mSourceBounds = new Rect();
    public int mMagnificationFrameOffsetX = 0;
    public int mMagnificationFrameOffsetY = 0;
    public final Rect mMagnificationFrameBoundary = new Rect();
    public int mSystemGestureTop = -1;
    public final DozeScreenState$$ExternalSyntheticLambda0 mMirrorViewRunnable = new DozeScreenState$$ExternalSyntheticLambda0(this, 2);
    public final WindowMagnificationController$$ExternalSyntheticLambda0 mMirrorViewLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            Objects.requireNonNull(windowMagnificationController);
            if (!windowMagnificationController.mHandler.hasCallbacks(windowMagnificationController.mMirrorViewRunnable)) {
                windowMagnificationController.mHandler.post(windowMagnificationController.mMirrorViewRunnable);
            }
        }
    };
    public final WindowMagnificationController$$ExternalSyntheticLambda1 mMirrorSurfaceViewLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda1
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            Objects.requireNonNull(windowMagnificationController);
            int i9 = windowMagnificationController.mBorderDragSize;
            Region region = new Region(i9, i9, windowMagnificationController.mMirrorView.getWidth() - windowMagnificationController.mBorderDragSize, windowMagnificationController.mMirrorView.getHeight() - windowMagnificationController.mBorderDragSize);
            region.op(new Rect((windowMagnificationController.mMirrorView.getWidth() - windowMagnificationController.mDragViewSize) - windowMagnificationController.mBorderDragSize, (windowMagnificationController.mMirrorView.getHeight() - windowMagnificationController.mDragViewSize) - windowMagnificationController.mBorderDragSize, windowMagnificationController.mMirrorView.getWidth(), windowMagnificationController.mMirrorView.getHeight()), Region.Op.DIFFERENCE);
            try {
                WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(IWindow.Stub.asInterface(windowMagnificationController.mMirrorView.getWindowToken()), region);
            } catch (RemoteException unused) {
            }
        }
    };
    public PipMotionHelper$1$$ExternalSyntheticLambda0 mMirrorViewGeometryVsyncCallback = new PipMotionHelper$1$$ExternalSyntheticLambda0(this, 1);
    public final BubbleStackView$$ExternalSyntheticLambda17 mUpdateStateDescriptionRunnable = new BubbleStackView$$ExternalSyntheticLambda17(this, 1);
    public final BubbleExpandedView$1$$ExternalSyntheticLambda0 mWindowInsetChangeRunnable = new BubbleExpandedView$1$$ExternalSyntheticLambda0(this, 2);

    /* loaded from: classes.dex */
    public class MirrorWindowA11yDelegate extends View.AccessibilityDelegate {
        public MirrorWindowA11yDelegate() {
        }

        /* JADX WARN: Removed duplicated region for block: B:41:0x00df A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00e0  */
        @Override // android.view.View.AccessibilityDelegate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean performAccessibilityAction(android.view.View r8, int r9, android.os.Bundle r10) {
            /*
                Method dump skipped, instructions count: 229
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.WindowMagnificationController.MirrorWindowA11yDelegate.performAccessibilityAction(android.view.View, int, android.os.Bundle):boolean");
        }

        @Override // android.view.View.AccessibilityDelegate
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            float f;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427366, WindowMagnificationController.this.mContext.getString(2131951700)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427367, WindowMagnificationController.this.mContext.getString(2131951701)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427363, WindowMagnificationController.this.mContext.getString(2131951698)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427360, WindowMagnificationController.this.mContext.getString(2131951695)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427361, WindowMagnificationController.this.mContext.getString(2131951696)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427362, WindowMagnificationController.this.mContext.getString(2131951697)));
            accessibilityNodeInfo.setContentDescription(WindowMagnificationController.this.mContext.getString(2131952705));
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            Objects.requireNonNull(windowMagnificationController);
            if (windowMagnificationController.isWindowVisible()) {
                f = windowMagnificationController.mScale;
            } else {
                f = Float.NaN;
            }
            accessibilityNodeInfo.setStateDescription(windowMagnificationController.formatStateDescription(f));
        }
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public final boolean onFinish() {
        return false;
    }

    @Override // android.content.ComponentCallbacks
    public final void onLowMemory() {
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public final void onStart() {
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    static {
        boolean z;
        if (Log.isLoggable("WindowMagnificationController", 3) || Build.IS_DEBUGGABLE) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
        A11Y_ACTION_SCALE_RANGE = new Range<>(Float.valueOf(2.0f), Float.valueOf(8.0f));
    }

    public final void calculateMagnificationFrameBoundary() {
        int width = this.mMagnificationFrame.width() / 2;
        int height = this.mMagnificationFrame.height() / 2;
        float f = this.mScale;
        int i = width - ((int) (width / f));
        int max = Math.max(i - this.mMagnificationFrameOffsetX, 0);
        int max2 = Math.max(i + this.mMagnificationFrameOffsetX, 0);
        int i2 = height - ((int) (height / f));
        this.mMagnificationFrameBoundary.set(-max, -Math.max(i2 - this.mMagnificationFrameOffsetY, 0), this.mWindowBounds.width() + max2, this.mWindowBounds.height() + Math.max(i2 + this.mMagnificationFrameOffsetY, 0));
    }

    public final void computeBounceAnimationScale() {
        float width = (this.mMirrorSurfaceMargin * 2) + this.mMagnificationFrame.width();
        this.mBounceEffectAnimationScale = Math.min(width / (width - (this.mOuterBorderSize * 2)), 1.05f);
    }

    public final void enableWindowMagnificationInternal(float f, float f2, float f3, float f4, float f5) {
        int i;
        int i2;
        float f6;
        if (Float.compare(f, 1.0f) <= 0) {
            deleteWindowMagnification$1();
            return;
        }
        if (!isWindowVisible()) {
            onConfigurationChanged(this.mResources.getConfiguration());
            this.mContext.registerComponentCallbacks(this);
        }
        if (Float.isNaN(f4)) {
            i = this.mMagnificationFrameOffsetX;
        } else {
            i = (int) ((this.mMagnificationFrame.width() / 2) * f4);
        }
        this.mMagnificationFrameOffsetX = i;
        if (Float.isNaN(f5)) {
            i2 = this.mMagnificationFrameOffsetY;
        } else {
            i2 = (int) ((this.mMagnificationFrame.height() / 2) * f5);
        }
        this.mMagnificationFrameOffsetY = i2;
        float f7 = this.mMagnificationFrameOffsetX + f2;
        float f8 = i2 + f3;
        float f9 = 0.0f;
        if (Float.isNaN(f2)) {
            f6 = 0.0f;
        } else {
            f6 = f7 - this.mMagnificationFrame.exactCenterX();
        }
        if (!Float.isNaN(f3)) {
            f9 = f8 - this.mMagnificationFrame.exactCenterY();
        }
        if (Float.isNaN(f)) {
            f = this.mScale;
        }
        this.mScale = f;
        calculateMagnificationFrameBoundary();
        updateMagnificationFramePosition((int) f6, (int) f9);
        if (!isWindowVisible()) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams((this.mMirrorSurfaceMargin * 2) + this.mMagnificationFrame.width(), (this.mMirrorSurfaceMargin * 2) + this.mMagnificationFrame.height(), 2039, 40, -2);
            layoutParams.gravity = 51;
            Rect rect = this.mMagnificationFrame;
            int i3 = rect.left;
            int i4 = this.mMirrorSurfaceMargin;
            layoutParams.x = i3 - i4;
            layoutParams.y = rect.top - i4;
            layoutParams.layoutInDisplayCutoutMode = 1;
            layoutParams.receiveInsetsIgnoringZOrder = true;
            layoutParams.setTitle(this.mContext.getString(2131952705));
            layoutParams.accessibilityTitle = this.mResources.getString(17039658);
            View inflate = LayoutInflater.from(this.mContext).inflate(2131624657, (ViewGroup) null);
            this.mMirrorView = inflate;
            SurfaceView surfaceView = (SurfaceView) inflate.findViewById(2131428996);
            this.mMirrorSurfaceView = surfaceView;
            surfaceView.addOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
            this.mMirrorView.setSystemUiVisibility(5894);
            this.mMirrorView.addOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
            this.mMirrorView.setAccessibilityDelegate(new MirrorWindowA11yDelegate());
            this.mMirrorView.setOnApplyWindowInsetsListener(new BubbleController$$ExternalSyntheticLambda0(this, 1));
            this.mWm.addView(this.mMirrorView, layoutParams);
            SurfaceHolder holder = this.mMirrorSurfaceView.getHolder();
            holder.addCallback(this);
            holder.setFormat(1);
            this.mDragView = this.mMirrorView.findViewById(2131427876);
            this.mLeftDrag = this.mMirrorView.findViewById(2131428248);
            this.mTopDrag = this.mMirrorView.findViewById(2131429071);
            this.mRightDrag = this.mMirrorView.findViewById(2131428708);
            this.mBottomDrag = this.mMirrorView.findViewById(2131427590);
            this.mDragView.setOnTouchListener(this);
            this.mLeftDrag.setOnTouchListener(this);
            this.mTopDrag.setOnTouchListener(this);
            this.mRightDrag.setOnTouchListener(this);
            this.mBottomDrag.setOnTouchListener(this);
            return;
        }
        modifyWindowMagnification();
    }

    public final String formatStateDescription(float f) {
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        return this.mPercentFormat.format(f);
    }

    public final boolean isWindowVisible() {
        if (this.mMirrorView != null) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void modifyWindowMagnification() {
        /*
            r6 = this;
            com.android.internal.graphics.SfVsyncFrameCallbackProvider r0 = r6.mSfVsyncFrameProvider
            com.android.wm.shell.pip.phone.PipMotionHelper$1$$ExternalSyntheticLambda0 r1 = r6.mMirrorViewGeometryVsyncCallback
            r0.postFrameCallback(r1)
            boolean r0 = r6.isWindowVisible()
            if (r0 != 0) goto L_0x000e
            goto L_0x007c
        L_0x000e:
            android.graphics.Rect r0 = r6.mWindowBounds
            int r0 = r0.width()
            android.view.View r1 = r6.mMirrorView
            int r1 = r1.getWidth()
            int r0 = r0 - r1
            android.graphics.Rect r1 = r6.mWindowBounds
            int r1 = r1.height()
            android.view.View r2 = r6.mMirrorView
            int r2 = r2.getHeight()
            int r1 = r1 - r2
            android.view.View r2 = r6.mMirrorView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.view.WindowManager$LayoutParams r2 = (android.view.WindowManager.LayoutParams) r2
            android.graphics.Rect r3 = r6.mMagnificationFrame
            int r4 = r3.left
            int r5 = r6.mMirrorSurfaceMargin
            int r4 = r4 - r5
            r2.x = r4
            int r3 = r3.top
            int r3 = r3 - r5
            r2.y = r3
            r3 = 0
            if (r4 >= 0) goto L_0x0049
            int r0 = r6.mOuterBorderSize
            int r0 = -r0
            int r0 = java.lang.Math.max(r4, r0)
            goto L_0x0052
        L_0x0049:
            if (r4 <= r0) goto L_0x0054
            int r4 = r4 - r0
            int r0 = r6.mOuterBorderSize
            int r0 = java.lang.Math.min(r4, r0)
        L_0x0052:
            float r0 = (float) r0
            goto L_0x0055
        L_0x0054:
            r0 = r3
        L_0x0055:
            int r4 = r2.y
            if (r4 >= 0) goto L_0x0061
            int r1 = r6.mOuterBorderSize
            int r1 = -r1
            int r1 = java.lang.Math.max(r4, r1)
            goto L_0x006a
        L_0x0061:
            if (r4 <= r1) goto L_0x006b
            int r4 = r4 - r1
            int r1 = r6.mOuterBorderSize
            int r1 = java.lang.Math.min(r4, r1)
        L_0x006a:
            float r3 = (float) r1
        L_0x006b:
            android.view.View r1 = r6.mMirrorView
            r1.setTranslationX(r0)
            android.view.View r0 = r6.mMirrorView
            r0.setTranslationY(r3)
            android.view.WindowManager r0 = r6.mWm
            android.view.View r6 = r6.mMirrorView
            r0.updateViewLayout(r6, r2)
        L_0x007c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.WindowMagnificationController.modifyWindowMagnification():void");
    }

    public final void moveWindowMagnifier(float f, float f2) {
        WindowMagnificationAnimationController windowMagnificationAnimationController = this.mAnimationController;
        Objects.requireNonNull(windowMagnificationAnimationController);
        if (!windowMagnificationAnimationController.mValueAnimator.isRunning() && this.mMirrorSurfaceView != null && updateMagnificationFramePosition((int) f, (int) f2)) {
            modifyWindowMagnification();
        }
    }

    @Override // android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
        boolean z;
        float f;
        int diff = configuration.diff(this.mConfiguration);
        this.mConfiguration.setTo(configuration);
        boolean z2 = DEBUG;
        if (z2) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onConfigurationChanged = ");
            m.append(Configuration.configurationDiffToString(diff));
            Log.d("WindowMagnificationController", m.toString());
        }
        if (diff != 0) {
            if ((diff & 128) != 0) {
                Display display = this.mContext.getDisplay();
                int i = this.mRotation;
                int rotation = display.getRotation();
                this.mRotation = rotation;
                int i2 = (((i - rotation) + 4) % 4) * 90;
                if (i2 == 0 || i2 == 180) {
                    Log.w("WindowMagnificationController", "onRotate -- rotate with the device. skip it");
                } else {
                    Rect rect = new Rect(this.mWm.getCurrentWindowMetrics().getBounds());
                    if (rect.width() == this.mWindowBounds.height() && rect.height() == this.mWindowBounds.width()) {
                        this.mWindowBounds.set(rect);
                        calculateMagnificationFrameBoundary();
                        if (isWindowVisible()) {
                            Matrix matrix = new Matrix();
                            matrix.setRotate(i2);
                            if (i2 == 90) {
                                matrix.postTranslate(this.mWindowBounds.width(), 0.0f);
                            } else if (i2 == 270) {
                                matrix.postTranslate(0.0f, this.mWindowBounds.height());
                            }
                            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
                            Rect rect2 = this.mTmpRect;
                            int i3 = layoutParams.x;
                            int i4 = layoutParams.y;
                            rect2.set(i3, i4, layoutParams.width + i3, layoutParams.height + i4);
                            RectF rectF = new RectF(this.mTmpRect);
                            matrix.mapRect(rectF);
                            float f2 = rectF.left;
                            Rect rect3 = this.mTmpRect;
                            moveWindowMagnifier(f2 - rect3.left, rectF.top - rect3.top);
                        }
                    } else {
                        Log.w("WindowMagnificationController", "onRotate -- unexpected window height/width");
                    }
                }
            }
            if ((diff & 4) != 0 && isWindowVisible()) {
                WindowManager.LayoutParams layoutParams2 = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
                layoutParams2.accessibilityTitle = this.mResources.getString(17039658);
                this.mWm.updateViewLayout(this.mMirrorView, layoutParams2);
            }
            boolean z3 = true;
            if ((diff & 4096) != 0) {
                updateDimensions();
                computeBounceAnimationScale();
                z = true;
            } else {
                z = false;
            }
            if ((diff & 1024) != 0) {
                Rect rect4 = new Rect(this.mWindowBounds);
                Rect bounds = this.mWm.getCurrentWindowMetrics().getBounds();
                if (bounds.equals(rect4)) {
                    if (z2) {
                        Log.d("WindowMagnificationController", "handleScreenSizeChanged -- window bounds is not changed");
                    }
                    z3 = false;
                } else {
                    this.mWindowBounds.set(bounds);
                    float f3 = Float.NaN;
                    if (isWindowVisible()) {
                        f = this.mMagnificationFrame.exactCenterX();
                    } else {
                        f = Float.NaN;
                    }
                    float width = (f * this.mWindowBounds.width()) / rect4.width();
                    if (isWindowVisible()) {
                        f3 = this.mMagnificationFrame.exactCenterY();
                    }
                    setMagnificationFrameWith(this.mWindowBounds, (int) width, (int) ((f3 * this.mWindowBounds.height()) / rect4.height()));
                    calculateMagnificationFrameBoundary();
                }
                z |= z3;
            }
            if (isWindowVisible() && z) {
                deleteWindowMagnification$1();
                enableWindowMagnificationInternal(Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN);
            }
        }
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public final void onSingleTap() {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mMirrorView, PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, this.mBounceEffectAnimationScale, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, this.mBounceEffectAnimationScale, 1.0f));
        ofPropertyValuesHolder.setDuration(this.mBounceEffectDuration);
        ofPropertyValuesHolder.start();
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.mDragView || view == this.mLeftDrag || view == this.mTopDrag || view == this.mRightDrag || view == this.mBottomDrag) {
            return this.mGestureDetector.onTouch(motionEvent);
        }
        return false;
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        SurfaceControl surfaceControl;
        WindowManagerWrapper windowManagerWrapper = WindowManagerWrapper.sInstance;
        int i = this.mDisplayId;
        Objects.requireNonNull(windowManagerWrapper);
        try {
            surfaceControl = new SurfaceControl();
            WindowManagerGlobal.getWindowManagerService().mirrorDisplay(i, surfaceControl);
        } catch (RemoteException e) {
            Log.e("WindowManagerWrapper", "Unable to reach window manager", e);
            surfaceControl = null;
        }
        this.mMirrorSurface = surfaceControl;
        if (surfaceControl.isValid()) {
            this.mTransaction.show(this.mMirrorSurface).reparent(this.mMirrorSurface, this.mMirrorSurfaceView.getSurfaceControl());
            modifyWindowMagnification();
        }
    }

    public final void updateDimensions() {
        this.mMirrorSurfaceMargin = this.mResources.getDimensionPixelSize(2131166287);
        this.mBorderDragSize = this.mResources.getDimensionPixelSize(2131166280);
        this.mDragViewSize = this.mResources.getDimensionPixelSize(2131166282);
        this.mOuterBorderSize = this.mResources.getDimensionPixelSize(2131166288);
    }

    public final boolean updateMagnificationFramePosition(int i, int i2) {
        this.mTmpRect.set(this.mMagnificationFrame);
        this.mTmpRect.offset(i, i2);
        Rect rect = this.mTmpRect;
        int i3 = rect.left;
        Rect rect2 = this.mMagnificationFrameBoundary;
        int i4 = rect2.left;
        if (i3 < i4) {
            rect.offsetTo(i4, rect.top);
        } else {
            int i5 = rect.right;
            int i6 = rect2.right;
            if (i5 > i6) {
                int width = i6 - this.mMagnificationFrame.width();
                Rect rect3 = this.mTmpRect;
                rect3.offsetTo(width, rect3.top);
            }
        }
        Rect rect4 = this.mTmpRect;
        int i7 = rect4.top;
        Rect rect5 = this.mMagnificationFrameBoundary;
        int i8 = rect5.top;
        if (i7 < i8) {
            rect4.offsetTo(rect4.left, i8);
        } else {
            int i9 = rect4.bottom;
            int i10 = rect5.bottom;
            if (i9 > i10) {
                int height = i10 - this.mMagnificationFrame.height();
                Rect rect6 = this.mTmpRect;
                rect6.offsetTo(rect6.left, height);
            }
        }
        if (this.mTmpRect.equals(this.mMagnificationFrame)) {
            return false;
        }
        this.mMagnificationFrame.set(this.mTmpRect);
        return true;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda1] */
    public WindowMagnificationController(Context context, Handler handler, WindowMagnificationAnimationController windowMagnificationAnimationController, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, SurfaceControl.Transaction transaction, WindowMagnifierCallback windowMagnifierCallback, SysUiState sysUiState) {
        this.mContext = context;
        this.mHandler = handler;
        this.mAnimationController = windowMagnificationAnimationController;
        windowMagnificationAnimationController.mController = this;
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mWindowMagnifierCallback = windowMagnifierCallback;
        this.mSysUiState = sysUiState;
        this.mConfiguration = new Configuration(context.getResources().getConfiguration());
        Display display = context.getDisplay();
        this.mDisplayId = context.getDisplayId();
        this.mRotation = display.getRotation();
        WindowManager windowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mWm = windowManager;
        this.mWindowBounds = new Rect(windowManager.getCurrentWindowMetrics().getBounds());
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mScale = resources.getInteger(2131492993);
        this.mBounceEffectDuration = resources.getInteger(17694720);
        updateDimensions();
        Rect rect = this.mWindowBounds;
        setMagnificationFrameWith(rect, rect.width() / 2, this.mWindowBounds.height() / 2);
        computeBounceAnimationScale();
        this.mTransaction = transaction;
        this.mGestureDetector = new MagnificationGestureDetector(context, handler, this);
    }

    public final void deleteWindowMagnification$1() {
        if (isWindowVisible()) {
            SurfaceControl surfaceControl = this.mMirrorSurface;
            if (surfaceControl != null) {
                this.mTransaction.remove(surfaceControl).apply();
                this.mMirrorSurface = null;
            }
            SurfaceView surfaceView = this.mMirrorSurfaceView;
            if (surfaceView != null) {
                surfaceView.removeOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
            }
            if (this.mMirrorView != null) {
                this.mHandler.removeCallbacks(this.mMirrorViewRunnable);
                this.mMirrorView.removeOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
                this.mWm.removeView(this.mMirrorView);
                this.mMirrorView = null;
            }
            this.mMirrorViewBounds.setEmpty();
            updateSysUIState(false);
            this.mContext.unregisterComponentCallbacks(this);
        }
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public final void onDrag(float f, float f2) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback;
        moveWindowMagnifier(f, f2);
        WindowMagnifierCallback windowMagnifierCallback = this.mWindowMagnifierCallback;
        int i = this.mDisplayId;
        WindowMagnification windowMagnification = (WindowMagnification) windowMagnifierCallback;
        Objects.requireNonNull(windowMagnification);
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = windowMagnification.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null && (iWindowMagnificationConnectionCallback = windowMagnificationConnectionImpl.mConnectionCallback) != null) {
            try {
                iWindowMagnificationConnectionCallback.onDrag(i);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform taking control by a user", e);
            }
        }
    }

    public final void setMagnificationFrameWith(Rect rect, int i, int i2) {
        int min = (this.mMirrorSurfaceMargin * 2) + Math.min(this.mResources.getDimensionPixelSize(2131166286), Math.min(rect.width(), rect.height()) / 2);
        int i3 = min / 2;
        int i4 = i - i3;
        int i5 = i2 - i3;
        this.mMagnificationFrame.set(i4, i5, i4 + min, min + i5);
    }

    public final void updateSysUIState(boolean z) {
        boolean z2;
        int i;
        if (!isWindowVisible() || (i = this.mSystemGestureTop) <= 0 || this.mMirrorViewBounds.bottom <= i) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z || z2 != this.mOverlapWithGestureInsets) {
            this.mOverlapWithGestureInsets = z2;
            SysUiState sysUiState = this.mSysUiState;
            sysUiState.setFlag(524288, z2);
            sysUiState.commitUpdate(this.mDisplayId);
        }
    }
}
