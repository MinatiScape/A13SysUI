package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DisplayCutout;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.FrameLayout;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.widget.floatingtoolbar.FloatingToolbar;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$styleable;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationShadeWindowView extends FrameLayout {
    public ActionMode mFloatingActionMode;
    public View mFloatingActionModeOriginatingView;
    public FloatingToolbar mFloatingToolbar;
    public AnonymousClass1 mFloatingToolbarPreDrawListener;
    public InteractionEventHandler mInteractionEventHandler;
    public int mRightInset = 0;
    public int mLeftInset = 0;
    public AnonymousClass2 mFakeWindow = new Window(((FrameLayout) this).mContext) { // from class: com.android.systemui.statusbar.phone.NotificationShadeWindowView.2
        @Override // android.view.Window
        public final void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        }

        public final void alwaysReadCloseOnTouchAttr() {
        }

        public final void clearContentView() {
        }

        @Override // android.view.Window
        public final void closeAllPanels() {
        }

        @Override // android.view.Window
        public final void closePanel(int i) {
        }

        @Override // android.view.Window
        public final View getCurrentFocus() {
            return null;
        }

        @Override // android.view.Window
        public final WindowInsetsController getInsetsController() {
            return null;
        }

        @Override // android.view.Window
        public final LayoutInflater getLayoutInflater() {
            return null;
        }

        @Override // android.view.Window
        public final int getNavigationBarColor() {
            return 0;
        }

        @Override // android.view.Window
        public final int getStatusBarColor() {
            return 0;
        }

        @Override // android.view.Window
        public final int getVolumeControlStream() {
            return 0;
        }

        @Override // android.view.Window
        public final void invalidatePanelMenu(int i) {
        }

        @Override // android.view.Window
        public final boolean isFloating() {
            return false;
        }

        @Override // android.view.Window
        public final boolean isShortcutKey(int i, KeyEvent keyEvent) {
            return false;
        }

        @Override // android.view.Window
        public final void onActive() {
        }

        @Override // android.view.Window
        public final void onConfigurationChanged(Configuration configuration) {
        }

        public final void onMultiWindowModeChanged() {
        }

        public final void onPictureInPictureModeChanged(boolean z) {
        }

        @Override // android.view.Window
        public final void openPanel(int i, KeyEvent keyEvent) {
        }

        @Override // android.view.Window
        public final View peekDecorView() {
            return null;
        }

        @Override // android.view.Window
        public final boolean performContextMenuIdentifierAction(int i, int i2) {
            return false;
        }

        @Override // android.view.Window
        public final boolean performPanelIdentifierAction(int i, int i2, int i3) {
            return false;
        }

        @Override // android.view.Window
        public final boolean performPanelShortcut(int i, int i2, KeyEvent keyEvent, int i3) {
            return false;
        }

        public final void reportActivityRelaunched() {
        }

        @Override // android.view.Window
        public final void restoreHierarchyState(Bundle bundle) {
        }

        @Override // android.view.Window
        public final Bundle saveHierarchyState() {
            return null;
        }

        @Override // android.view.Window
        public final void setBackgroundDrawable(Drawable drawable) {
        }

        @Override // android.view.Window
        public final void setChildDrawable(int i, Drawable drawable) {
        }

        @Override // android.view.Window
        public final void setChildInt(int i, int i2) {
        }

        @Override // android.view.Window
        public final void setContentView(int i) {
        }

        @Override // android.view.Window
        public final void setContentView(View view) {
        }

        @Override // android.view.Window
        public final void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        }

        @Override // android.view.Window
        public final void setDecorCaptionShade(int i) {
        }

        @Override // android.view.Window
        public final void setFeatureDrawable(int i, Drawable drawable) {
        }

        @Override // android.view.Window
        public final void setFeatureDrawableAlpha(int i, int i2) {
        }

        @Override // android.view.Window
        public final void setFeatureDrawableResource(int i, int i2) {
        }

        @Override // android.view.Window
        public final void setFeatureDrawableUri(int i, Uri uri) {
        }

        @Override // android.view.Window
        public final void setFeatureInt(int i, int i2) {
        }

        @Override // android.view.Window
        public final void setNavigationBarColor(int i) {
        }

        @Override // android.view.Window
        public final void setResizingCaptionDrawable(Drawable drawable) {
        }

        @Override // android.view.Window
        public final void setStatusBarColor(int i) {
        }

        @Override // android.view.Window
        public final void setTitle(CharSequence charSequence) {
        }

        @Override // android.view.Window
        public final void setTitleColor(int i) {
        }

        @Override // android.view.Window
        public final void setVolumeControlStream(int i) {
        }

        @Override // android.view.Window
        public final boolean superDispatchGenericMotionEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.Window
        public final boolean superDispatchKeyEvent(KeyEvent keyEvent) {
            return false;
        }

        @Override // android.view.Window
        public final boolean superDispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return false;
        }

        @Override // android.view.Window
        public final boolean superDispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.Window
        public final boolean superDispatchTrackballEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.Window
        public final void takeInputQueue(InputQueue.Callback callback) {
        }

        @Override // android.view.Window
        public final void takeKeyEvents(boolean z) {
        }

        @Override // android.view.Window
        public final void takeSurface(SurfaceHolder.Callback2 callback2) {
        }

        @Override // android.view.Window
        public final void togglePanel(int i, KeyEvent keyEvent) {
        }

        @Override // android.view.Window
        public final View getDecorView() {
            return NotificationShadeWindowView.this;
        }
    };

    /* loaded from: classes.dex */
    public class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
        public final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // android.view.ActionMode.Callback
        public final boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public final boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public final void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            NotificationShadeWindowView notificationShadeWindowView = NotificationShadeWindowView.this;
            if (actionMode == notificationShadeWindowView.mFloatingActionMode) {
                notificationShadeWindowView.cleanupFloatingActionModeViews();
                NotificationShadeWindowView.this.mFloatingActionMode = null;
            }
            NotificationShadeWindowView.this.requestFitSystemWindows();
        }

        @Override // android.view.ActionMode.Callback2
        public final void onGetContentRect(ActionMode actionMode, View view, Rect rect) {
            ActionMode.Callback callback = this.mWrapped;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(actionMode, view, rect);
            } else {
                super.onGetContentRect(actionMode, view, rect);
            }
        }

        @Override // android.view.ActionMode.Callback
        public final boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            NotificationShadeWindowView.this.requestFitSystemWindows();
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }

    /* loaded from: classes.dex */
    public interface InteractionEventHandler {
    }

    /* loaded from: classes.dex */
    public class LayoutParams extends FrameLayout.LayoutParams {
        public boolean ignoreRightInset;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.StatusBarWindowView_Layout);
            this.ignoreRightInset = obtainStyledAttributes.getBoolean(0, false);
            obtainStyledAttributes.recycle();
        }
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.phone.NotificationShadeWindowView$1] */
    @Override // android.view.ViewGroup, android.view.ViewParent
    public final ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i) {
        if (i != 1) {
            return super.startActionModeForChild(view, callback, i);
        }
        ActionModeCallback2Wrapper actionModeCallback2Wrapper = new ActionModeCallback2Wrapper(callback);
        ActionMode actionMode = this.mFloatingActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        cleanupFloatingActionModeViews();
        this.mFloatingToolbar = new FloatingToolbar(this.mFakeWindow);
        final FloatingActionMode floatingActionMode = new FloatingActionMode(((FrameLayout) this).mContext, actionModeCallback2Wrapper, view, this.mFloatingToolbar);
        this.mFloatingActionModeOriginatingView = view;
        this.mFloatingToolbarPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.phone.NotificationShadeWindowView.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                floatingActionMode.updateViewLocationInWindow();
                return true;
            }
        };
        if (!actionModeCallback2Wrapper.onCreateActionMode(floatingActionMode, floatingActionMode.getMenu())) {
            return null;
        }
        this.mFloatingActionMode = floatingActionMode;
        floatingActionMode.invalidate();
        this.mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
        return floatingActionMode;
    }

    public final void cleanupFloatingActionModeViews() {
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        View view = this.mFloatingActionModeOriginatingView;
        if (view != null) {
            if (this.mFloatingToolbarPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
                this.mFloatingToolbarPreDrawListener = null;
            }
            this.mFloatingActionModeOriginatingView = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00eb  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean dispatchKeyEvent(android.view.KeyEvent r7) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationShadeWindowView.dispatchKeyEvent(android.view.KeyEvent):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        NotificationShadeWindowViewController.AnonymousClass2 r3 = (NotificationShadeWindowViewController.AnonymousClass2) this.mInteractionEventHandler;
        Objects.requireNonNull(r3);
        StatusBar statusBar = NotificationShadeWindowViewController.this.mService;
        Objects.requireNonNull(statusBar);
        if (keyEvent.getKeyCode() != 4) {
            return false;
        }
        boolean z = true;
        if (statusBar.mState != 1) {
            return false;
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        KeyguardBouncer keyguardBouncer = statusBarKeyguardViewManager.mBouncer;
        Objects.requireNonNull(keyguardBouncer);
        keyguardBouncer.ensureView();
        KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
        Objects.requireNonNull(keyguardHostViewController);
        KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController.mKeyguardSecurityContainerController;
        Objects.requireNonNull(keyguardSecurityContainerController);
        if (keyguardSecurityContainerController.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.Password) {
            z = false;
        }
        if (z) {
            return statusBar.onBackPressed();
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x005a, code lost:
        if (r12.mIgnoreTouchWhilePulsing == false) goto L_0x005c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0178, code lost:
        if (r12 != 10) goto L_0x0234;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0069  */
    /* JADX WARN: Type inference failed for: r14v3, types: [android.view.View] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean dispatchTouchEvent(android.view.MotionEvent r23) {
        /*
            Method dump skipped, instructions count: 821
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationShadeWindowView.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0029, code lost:
        if (r0.this$0.mDockManager.isDocked() == false) goto L_0x0041;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0085  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            com.android.systemui.statusbar.phone.NotificationShadeWindowView$InteractionEventHandler r0 = r4.mInteractionEventHandler
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$2 r0 = (com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.AnonymousClass2) r0
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.SysuiStatusBarStateController r1 = r1.mStatusBarStateController
            boolean r1 = r1.isDozing()
            if (r1 == 0) goto L_0x002c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBar r1 = r1.mService
            java.util.Objects.requireNonNull(r1)
            com.android.systemui.statusbar.phone.DozeServiceHost r1 = r1.mDozeServiceHost
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1.mPulsing
            if (r1 != 0) goto L_0x002c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.dock.DockManager r1 = r1.mDockManager
            boolean r1 = r1.isDocked()
            if (r1 != 0) goto L_0x002c
            goto L_0x0041
        L_0x002c:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r1 = r1.mStatusBarKeyguardViewManager
            boolean r1 = r1.isShowingAlternateAuthOrAnimating()
            if (r1 == 0) goto L_0x0037
            goto L_0x0041
        L_0x0037:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.keyguard.LockIconViewController r1 = r1.mLockIconViewController
            boolean r1 = r1.onInterceptTouchEvent(r5)
            if (r1 == 0) goto L_0x0043
        L_0x0041:
            r0 = 1
            goto L_0x007d
        L_0x0043:
            r1 = 0
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r2 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.NotificationPanelViewController r2 = r2.mNotificationPanelViewController
            boolean r2 = r2.isFullyExpanded()
            if (r2 == 0) goto L_0x007c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r2 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.DragDownHelper r2 = r2.mDragDownHelper
            java.util.Objects.requireNonNull(r2)
            com.android.systemui.statusbar.LockscreenShadeTransitionController r2 = r2.dragDownCallback
            r3 = 0
            boolean r2 = r2.isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(r3)
            if (r2 == 0) goto L_0x007c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r2 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBar r2 = r2.mService
            java.util.Objects.requireNonNull(r2)
            boolean r2 = r2.mBouncerShowing
            if (r2 != 0) goto L_0x007c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r2 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.SysuiStatusBarStateController r2 = r2.mStatusBarStateController
            boolean r2 = r2.isDozing()
            if (r2 != 0) goto L_0x007c
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r0 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.DragDownHelper r0 = r0.mDragDownHelper
            boolean r0 = r0.onInterceptTouchEvent(r5)
            goto L_0x007d
        L_0x007c:
            r0 = r1
        L_0x007d:
            if (r0 != 0) goto L_0x0083
            boolean r0 = super.onInterceptTouchEvent(r5)
        L_0x0083:
            if (r0 == 0) goto L_0x00aa
            com.android.systemui.statusbar.phone.NotificationShadeWindowView$InteractionEventHandler r4 = r4.mInteractionEventHandler
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$2 r4 = (com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.AnonymousClass2) r4
            java.util.Objects.requireNonNull(r4)
            android.view.MotionEvent r5 = android.view.MotionEvent.obtain(r5)
            r1 = 3
            r5.setAction(r1)
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r1.mStackScrollLayout
            r1.onInterceptTouchEvent(r5)
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r4 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.NotificationPanelViewController r4 = r4.mNotificationPanelViewController
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.phone.PanelView r4 = r4.mView
            r4.onInterceptTouchEvent(r5)
            r5.recycle()
        L_0x00aa:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationShadeWindowView.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x004a, code lost:
        if (r4.isDraggingDown != false) goto L_0x004c;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            com.android.systemui.statusbar.phone.NotificationShadeWindowView$InteractionEventHandler r0 = r6.mInteractionEventHandler
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$2 r0 = (com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.AnonymousClass2) r0
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.SysuiStatusBarStateController r1 = r1.mStatusBarStateController
            boolean r1 = r1.isDozing()
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x0023
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r1 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBar r1 = r1.mService
            java.util.Objects.requireNonNull(r1)
            com.android.systemui.statusbar.phone.DozeServiceHost r1 = r1.mDozeServiceHost
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1.mPulsing
            r1 = r1 ^ r3
            goto L_0x0024
        L_0x0023:
            r1 = r2
        L_0x0024:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r4 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r4 = r4.mStatusBarKeyguardViewManager
            boolean r4 = r4.isShowingAlternateAuthOrAnimating()
            if (r4 == 0) goto L_0x002f
            r1 = r3
        L_0x002f:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r4 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.DragDownHelper r4 = r4.mDragDownHelper
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.LockscreenShadeTransitionController r4 = r4.dragDownCallback
            r5 = 0
            boolean r4 = r4.isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(r5)
            if (r4 == 0) goto L_0x0041
            if (r1 == 0) goto L_0x004c
        L_0x0041:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r4 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.DragDownHelper r4 = r4.mDragDownHelper
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.isDraggingDown
            if (r4 == 0) goto L_0x0054
        L_0x004c:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r0 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.DragDownHelper r0 = r0.mDragDownHelper
            boolean r1 = r0.onTouchEvent(r7)
        L_0x0054:
            if (r1 != 0) goto L_0x005a
            boolean r1 = super.onTouchEvent(r7)
        L_0x005a:
            if (r1 != 0) goto L_0x0073
            com.android.systemui.statusbar.phone.NotificationShadeWindowView$InteractionEventHandler r6 = r6.mInteractionEventHandler
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$2 r6 = (com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.AnonymousClass2) r6
            java.util.Objects.requireNonNull(r6)
            int r7 = r7.getActionMasked()
            if (r7 == r3) goto L_0x006c
            r0 = 3
            if (r7 != r0) goto L_0x0073
        L_0x006c:
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r6 = com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.this
            com.android.systemui.statusbar.phone.StatusBar r6 = r6.mService
            r6.setInteracting(r3, r2)
        L_0x0073:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationShadeWindowView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.phone.NotificationShadeWindowView$2] */
    public NotificationShadeWindowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setMotionEventSplittingEnabled(false);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        boolean z = true;
        if (getFitsSystemWindows()) {
            if (insetsIgnoringVisibility.top == getPaddingTop() && insetsIgnoringVisibility.bottom == getPaddingBottom()) {
                z = false;
            }
            if (z) {
                setPadding(0, 0, 0, 0);
            }
        } else {
            if (getPaddingLeft() == 0 && getPaddingRight() == 0 && getPaddingTop() == 0 && getPaddingBottom() == 0) {
                z = false;
            }
            if (z) {
                setPadding(0, 0, 0, 0);
            }
        }
        this.mLeftInset = 0;
        this.mRightInset = 0;
        DisplayCutout displayCutout = getRootWindowInsets().getDisplayCutout();
        if (displayCutout != null) {
            this.mLeftInset = displayCutout.getSafeInsetLeft();
            this.mRightInset = displayCutout.getSafeInsetRight();
        }
        this.mLeftInset = Math.max(insetsIgnoringVisibility.left, this.mLeftInset);
        this.mRightInset = Math.max(insetsIgnoringVisibility.right, this.mRightInset);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getLayoutParams() instanceof LayoutParams) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (!layoutParams.ignoreRightInset) {
                    int i2 = ((FrameLayout.LayoutParams) layoutParams).rightMargin;
                    int i3 = this.mRightInset;
                    if (i2 != i3 || ((FrameLayout.LayoutParams) layoutParams).leftMargin != this.mLeftInset) {
                        ((FrameLayout.LayoutParams) layoutParams).rightMargin = i3;
                        ((FrameLayout.LayoutParams) layoutParams).leftMargin = this.mLeftInset;
                        childAt.requestLayout();
                    }
                }
            }
        }
        return windowInsets;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        setWillNotDraw(true);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
