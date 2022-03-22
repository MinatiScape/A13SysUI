package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.widget.ViewDragHelper;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    public boolean interceptingEvents;
    public ViewDragHelper viewDragHelper;
    public int swipeDirection = 2;
    public float dragDismissThreshold = 0.5f;
    public float alphaStartSwipeDistance = 0.0f;
    public float alphaEndSwipeDistance = 0.5f;
    public final AnonymousClass1 dragCallback = new ViewDragHelper.Callback() { // from class: com.google.android.material.behavior.SwipeDismissBehavior.1
        public int activePointerId = -1;
        public int originalCapturedViewLeft;

        /* JADX WARN: Code restructure failed: missing block: B:21:0x0052, code lost:
            if (java.lang.Math.abs(r8.getLeft() - r7.originalCapturedViewLeft) >= java.lang.Math.round(r8.getWidth() * r7.this$0.dragDismissThreshold)) goto L_0x0054;
         */
        @Override // androidx.customview.widget.ViewDragHelper.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onViewReleased(android.view.View r8, float r9, float r10) {
            /*
                r7 = this;
                r10 = -1
                r7.activePointerId = r10
                int r10 = r8.getWidth()
                r0 = 0
                int r1 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                r2 = 1
                r3 = 0
                if (r1 == 0) goto L_0x0039
                java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r4 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
                int r4 = androidx.core.view.ViewCompat.Api17Impl.getLayoutDirection(r8)
                if (r4 != r2) goto L_0x0018
                r4 = r2
                goto L_0x0019
            L_0x0018:
                r4 = r3
            L_0x0019:
                com.google.android.material.behavior.SwipeDismissBehavior r5 = com.google.android.material.behavior.SwipeDismissBehavior.this
                int r5 = r5.swipeDirection
                r6 = 2
                if (r5 != r6) goto L_0x0021
                goto L_0x0054
            L_0x0021:
                if (r5 != 0) goto L_0x002d
                if (r4 == 0) goto L_0x002a
                int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                if (r9 >= 0) goto L_0x0056
                goto L_0x0054
            L_0x002a:
                if (r1 <= 0) goto L_0x0056
                goto L_0x0054
            L_0x002d:
                if (r5 != r2) goto L_0x0056
                if (r4 == 0) goto L_0x0034
                if (r1 <= 0) goto L_0x0056
                goto L_0x0054
            L_0x0034:
                int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                if (r9 >= 0) goto L_0x0056
                goto L_0x0054
            L_0x0039:
                int r9 = r8.getLeft()
                int r0 = r7.originalCapturedViewLeft
                int r9 = r9 - r0
                int r0 = r8.getWidth()
                float r0 = (float) r0
                com.google.android.material.behavior.SwipeDismissBehavior r1 = com.google.android.material.behavior.SwipeDismissBehavior.this
                float r1 = r1.dragDismissThreshold
                float r0 = r0 * r1
                int r0 = java.lang.Math.round(r0)
                int r9 = java.lang.Math.abs(r9)
                if (r9 < r0) goto L_0x0056
            L_0x0054:
                r9 = r2
                goto L_0x0057
            L_0x0056:
                r9 = r3
            L_0x0057:
                if (r9 == 0) goto L_0x0065
                int r9 = r8.getLeft()
                int r0 = r7.originalCapturedViewLeft
                if (r9 >= r0) goto L_0x0063
                int r0 = r0 - r10
                goto L_0x0068
            L_0x0063:
                int r0 = r0 + r10
                goto L_0x0068
            L_0x0065:
                int r0 = r7.originalCapturedViewLeft
                r2 = r3
            L_0x0068:
                com.google.android.material.behavior.SwipeDismissBehavior r9 = com.google.android.material.behavior.SwipeDismissBehavior.this
                androidx.customview.widget.ViewDragHelper r9 = r9.viewDragHelper
                int r10 = r8.getTop()
                boolean r9 = r9.settleCapturedViewAt(r0, r10)
                if (r9 == 0) goto L_0x0083
                com.google.android.material.behavior.SwipeDismissBehavior$SettleRunnable r9 = new com.google.android.material.behavior.SwipeDismissBehavior$SettleRunnable
                com.google.android.material.behavior.SwipeDismissBehavior r7 = com.google.android.material.behavior.SwipeDismissBehavior.this
                r9.<init>(r8, r2)
                java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r7 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
                androidx.core.view.ViewCompat.Api16Impl.postOnAnimation(r8, r9)
                goto L_0x008a
            L_0x0083:
                if (r2 == 0) goto L_0x008a
                com.google.android.material.behavior.SwipeDismissBehavior r7 = com.google.android.material.behavior.SwipeDismissBehavior.this
                java.util.Objects.requireNonNull(r7)
            L_0x008a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.behavior.SwipeDismissBehavior.AnonymousClass1.onViewReleased(android.view.View, float, float):void");
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int clampViewPositionHorizontal(View view, int i) {
            boolean z;
            int i2;
            int i3;
            int i4;
            int width;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api17Impl.getLayoutDirection(view) == 1) {
                z = true;
            } else {
                z = false;
            }
            int i5 = SwipeDismissBehavior.this.swipeDirection;
            if (i5 == 0) {
                if (z) {
                    i3 = this.originalCapturedViewLeft - view.getWidth();
                    i2 = this.originalCapturedViewLeft;
                } else {
                    i4 = this.originalCapturedViewLeft;
                    width = view.getWidth();
                    i3 = i4;
                    i2 = width + i3;
                }
            } else if (i5 != 1) {
                i3 = this.originalCapturedViewLeft - view.getWidth();
                i2 = this.originalCapturedViewLeft + view.getWidth();
            } else if (z) {
                i4 = this.originalCapturedViewLeft;
                width = view.getWidth();
                i3 = i4;
                i2 = width + i3;
            } else {
                i3 = this.originalCapturedViewLeft - view.getWidth();
                i2 = this.originalCapturedViewLeft;
            }
            return Math.min(Math.max(i3, i), i2);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewCaptured(View view, int i) {
            this.activePointerId = i;
            this.originalCapturedViewLeft = view.getLeft();
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewDragStateChanged(int i) {
            Objects.requireNonNull(SwipeDismissBehavior.this);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewPositionChanged(View view, int i, int i2) {
            float width = (view.getWidth() * SwipeDismissBehavior.this.alphaStartSwipeDistance) + this.originalCapturedViewLeft;
            float width2 = (view.getWidth() * SwipeDismissBehavior.this.alphaEndSwipeDistance) + this.originalCapturedViewLeft;
            float f = i;
            if (f <= width) {
                view.setAlpha(1.0f);
            } else if (f >= width2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(Math.min(Math.max(0.0f, 1.0f - ((f - width) / (width2 - width))), 1.0f));
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final boolean tryCaptureView(View view, int i) {
            int i2 = this.activePointerId;
            if ((i2 == -1 || i2 == i) && SwipeDismissBehavior.this.canSwipeDismissView(view)) {
                return true;
            }
            return false;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int clampViewPositionVertical(View view, int i) {
            return view.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }
    };

    /* loaded from: classes.dex */
    public interface OnDismissListener {
    }

    /* loaded from: classes.dex */
    public class SettleRunnable implements Runnable {
        public final boolean dismiss;
        public final View view;

        public SettleRunnable(View view, boolean z) {
            this.view = view;
            this.dismiss = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ViewDragHelper viewDragHelper = SwipeDismissBehavior.this.viewDragHelper;
            if (viewDragHelper != null && viewDragHelper.continueSettling()) {
                View view = this.view;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.postOnAnimation(view, this);
            } else if (this.dismiss) {
                Objects.requireNonNull(SwipeDismissBehavior.this);
            }
        }
    }

    public boolean canSwipeDismissView(View view) {
        return true;
    }

    public OnDismissListener getListener() {
        return null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean z = this.interceptingEvents;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            z = coordinatorLayout.isPointInChildBounds(v, (int) motionEvent.getX(), (int) motionEvent.getY());
            this.interceptingEvents = z;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.interceptingEvents = false;
        }
        if (!z) {
            return false;
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = new ViewDragHelper(coordinatorLayout.getContext(), coordinatorLayout, this.dragCallback);
        }
        return this.viewDragHelper.shouldInterceptTouchEvent(motionEvent);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api16Impl.getImportantForAccessibility(v) == 0) {
            ViewCompat.Api16Impl.setImportantForAccessibility(v, 1);
            ViewCompat.removeActionWithId(1048576, v);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
            if (canSwipeDismissView(v)) {
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, null, new AccessibilityViewCommand() { // from class: com.google.android.material.behavior.SwipeDismissBehavior.2
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        boolean z;
                        boolean z2 = false;
                        if (!SwipeDismissBehavior.this.canSwipeDismissView(view)) {
                            return false;
                        }
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        if (ViewCompat.Api17Impl.getLayoutDirection(view) == 1) {
                            z = true;
                        } else {
                            z = false;
                        }
                        int i2 = SwipeDismissBehavior.this.swipeDirection;
                        if ((i2 == 0 && z) || (i2 == 1 && !z)) {
                            z2 = true;
                        }
                        int width = view.getWidth();
                        if (z2) {
                            width = -width;
                        }
                        view.offsetLeftAndRight(width);
                        view.setAlpha(0.0f);
                        Objects.requireNonNull(SwipeDismissBehavior.this);
                        return true;
                    }
                });
            }
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper == null) {
            return false;
        }
        viewDragHelper.processTouchEvent(motionEvent);
        return true;
    }
}
