package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.customview.widget.FocusStrategy;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    public static final Rect INVALID_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    public static final AnonymousClass1 NODE_ADAPTER = new FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat>() { // from class: androidx.customview.widget.ExploreByTouchHelper.1
    };
    public static final AnonymousClass2 SPARSE_VALUES_ADAPTER = new Object() { // from class: androidx.customview.widget.ExploreByTouchHelper.2
    };
    public final View mHost;
    public final AccessibilityManager mManager;
    public MyNodeProvider mNodeProvider;
    public final Rect mTempScreenRect = new Rect();
    public final Rect mTempParentRect = new Rect();
    public final Rect mTempVisibleRect = new Rect();
    public final int[] mTempGlobalRect = new int[2];
    public int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
    public int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    public int mHoveredVirtualViewId = Integer.MIN_VALUE;

    /* loaded from: classes.dex */
    public class MyNodeProvider extends AccessibilityNodeProviderCompat {
        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public final AccessibilityNodeInfoCompat findFocus(int i) {
            int i2;
            if (i == 2) {
                i2 = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId;
            } else {
                i2 = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
            }
            if (i2 == Integer.MIN_VALUE) {
                return null;
            }
            return createAccessibilityNodeInfo(i2);
        }

        public MyNodeProvider() {
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public final AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i) {
            return new AccessibilityNodeInfoCompat(AccessibilityNodeInfo.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(i).mInfo));
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public final boolean performAction(int i, int i2, Bundle bundle) {
            int i3;
            ExploreByTouchHelper exploreByTouchHelper = ExploreByTouchHelper.this;
            Objects.requireNonNull(exploreByTouchHelper);
            if (i != -1) {
                boolean z = true;
                if (i2 == 1) {
                    return exploreByTouchHelper.requestKeyboardFocusForVirtualView(i);
                }
                if (i2 == 2) {
                    return exploreByTouchHelper.clearKeyboardFocusForVirtualView(i);
                }
                if (i2 == 64) {
                    if (exploreByTouchHelper.mManager.isEnabled() && exploreByTouchHelper.mManager.isTouchExplorationEnabled() && (i3 = exploreByTouchHelper.mAccessibilityFocusedVirtualViewId) != i) {
                        if (i3 != Integer.MIN_VALUE) {
                            exploreByTouchHelper.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
                            exploreByTouchHelper.mHost.invalidate();
                            exploreByTouchHelper.sendEventForVirtualView(i3, 65536);
                        }
                        exploreByTouchHelper.mAccessibilityFocusedVirtualViewId = i;
                        exploreByTouchHelper.mHost.invalidate();
                        exploreByTouchHelper.sendEventForVirtualView(i, 32768);
                        return z;
                    }
                    z = false;
                    return z;
                } else if (i2 != 128) {
                    return exploreByTouchHelper.onPerformActionForVirtualView(i, i2, bundle);
                } else {
                    if (exploreByTouchHelper.mAccessibilityFocusedVirtualViewId == i) {
                        exploreByTouchHelper.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
                        exploreByTouchHelper.mHost.invalidate();
                        exploreByTouchHelper.sendEventForVirtualView(i, 65536);
                        return z;
                    }
                    z = false;
                    return z;
                }
            } else {
                View view = exploreByTouchHelper.mHost;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                return ViewCompat.Api16Impl.performAccessibilityAction(view, i2, bundle);
            }
        }
    }

    public final AccessibilityEvent createEvent(int i, int i2) {
        if (i != -1) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
            AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo = obtainAccessibilityNodeInfo(i);
            obtain.getText().add(obtainAccessibilityNodeInfo.getText());
            obtain.setContentDescription(obtainAccessibilityNodeInfo.mInfo.getContentDescription());
            obtain.setScrollable(obtainAccessibilityNodeInfo.mInfo.isScrollable());
            obtain.setPassword(obtainAccessibilityNodeInfo.mInfo.isPassword());
            obtain.setEnabled(obtainAccessibilityNodeInfo.mInfo.isEnabled());
            obtain.setChecked(obtainAccessibilityNodeInfo.mInfo.isChecked());
            onPopulateEventForVirtualView(i, obtain);
            if (!obtain.getText().isEmpty() || obtain.getContentDescription() != null) {
                obtain.setClassName(obtainAccessibilityNodeInfo.mInfo.getClassName());
                obtain.setSource(this.mHost, i);
                obtain.setPackageName(this.mHost.getContext().getPackageName());
                return obtain;
            }
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        AccessibilityEvent obtain2 = AccessibilityEvent.obtain(i2);
        this.mHost.onInitializeAccessibilityEvent(obtain2);
        return obtain2;
    }

    public abstract int getVirtualViewAt(float f, float f2);

    public abstract void getVisibleVirtualViews(ArrayList arrayList);

    public final AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int i) {
        if (i != -1) {
            return createNodeForChild(i);
        }
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.mHost);
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(obtain);
        View view = this.mHost;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        view.onInitializeAccessibilityNodeInfo(obtain);
        ArrayList arrayList = new ArrayList();
        getVisibleVirtualViews(arrayList);
        if (obtain.getChildCount() <= 0 || arrayList.size() <= 0) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                accessibilityNodeInfoCompat.mInfo.addChild(this.mHost, ((Integer) arrayList.get(i2)).intValue());
            }
            return accessibilityNodeInfoCompat;
        }
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    public void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
    }

    public void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    public abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

    public void onVirtualViewKeyboardFocusChanged(int i, boolean z) {
    }

    public final boolean sendEventForVirtualView(int i, int i2) {
        ViewParent parent;
        if (i == Integer.MIN_VALUE || !this.mManager.isEnabled() || (parent = this.mHost.getParent()) == null) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.mHost, createEvent(i, i2));
    }

    public final boolean clearKeyboardFocusForVirtualView(int i) {
        if (this.mKeyboardFocusedVirtualViewId != i) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        onVirtualViewKeyboardFocusChanged(i, false);
        sendEventForVirtualView(i, 8);
        return true;
    }

    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        int i;
        if (!this.mManager.isEnabled() || !this.mManager.isTouchExplorationEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 7 || action == 9) {
            int virtualViewAt = getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
            int i2 = this.mHoveredVirtualViewId;
            if (i2 != virtualViewAt) {
                this.mHoveredVirtualViewId = virtualViewAt;
                sendEventForVirtualView(virtualViewAt, 128);
                sendEventForVirtualView(i2, 256);
            }
            if (virtualViewAt != Integer.MIN_VALUE) {
                return true;
            }
            return false;
        } else if (action != 10 || (i = this.mHoveredVirtualViewId) == Integer.MIN_VALUE) {
            return false;
        } else {
            if (i != Integer.MIN_VALUE) {
                this.mHoveredVirtualViewId = Integer.MIN_VALUE;
                sendEventForVirtualView(Integer.MIN_VALUE, 128);
                sendEventForVirtualView(i, 256);
            }
            return true;
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new MyNodeProvider();
        }
        return this.mNodeProvider;
    }

    public final void invalidateVirtualView(int i) {
        ViewParent parent;
        if (i != Integer.MIN_VALUE && this.mManager.isEnabled() && (parent = this.mHost.getParent()) != null) {
            AccessibilityEvent createEvent = createEvent(i, 2048);
            createEvent.setContentChangeTypes(0);
            parent.requestSendAccessibilityEvent(this.mHost, createEvent);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0144, code lost:
        if (r13 < ((r17 * r17) + ((r12 * 13) * r12))) goto L_0x0146;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0150 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean moveFocus(int r20, android.graphics.Rect r21) {
        /*
            Method dump skipped, instructions count: 479
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.customview.widget.ExploreByTouchHelper.moveFocus(int, android.graphics.Rect):boolean");
    }

    public final boolean requestKeyboardFocusForVirtualView(int i) {
        int i2;
        if ((!this.mHost.isFocused() && !this.mHost.requestFocus()) || (i2 = this.mKeyboardFocusedVirtualViewId) == i) {
            return false;
        }
        if (i2 != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i2);
        }
        if (i == Integer.MIN_VALUE) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = i;
        onVirtualViewKeyboardFocusChanged(i, true);
        sendEventForVirtualView(i, 8);
        return true;
    }

    public ExploreByTouchHelper(View view) {
        if (view != null) {
            this.mHost = view;
            this.mManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
            view.setFocusable(true);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api16Impl.getImportantForAccessibility(view) == 0) {
                ViewCompat.Api16Impl.setImportantForAccessibility(view, 1);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    public final AccessibilityNodeInfoCompat createNodeForChild(int i) {
        boolean z;
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(obtain);
        obtain.setEnabled(true);
        obtain.setFocusable(true);
        accessibilityNodeInfoCompat.setClassName("android.view.View");
        Rect rect = INVALID_BOUNDS;
        accessibilityNodeInfoCompat.setBoundsInParent(rect);
        obtain.setBoundsInScreen(rect);
        View view = this.mHost;
        accessibilityNodeInfoCompat.mParentVirtualDescendantId = -1;
        obtain.setParent(view);
        onPopulateNodeForVirtualView(i, accessibilityNodeInfoCompat);
        if (accessibilityNodeInfoCompat.getText() == null && obtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        obtain.getBoundsInParent(this.mTempParentRect);
        obtain.getBoundsInScreen(this.mTempScreenRect);
        if (!this.mTempParentRect.equals(rect) || !this.mTempScreenRect.equals(rect)) {
            int actions = obtain.getActions();
            if ((actions & 64) != 0) {
                throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else if ((actions & 128) == 0) {
                obtain.setPackageName(this.mHost.getContext().getPackageName());
                View view2 = this.mHost;
                accessibilityNodeInfoCompat.mVirtualDescendantId = i;
                obtain.setSource(view2, i);
                boolean z2 = false;
                if (this.mAccessibilityFocusedVirtualViewId == i) {
                    obtain.setAccessibilityFocused(true);
                    accessibilityNodeInfoCompat.addAction(128);
                } else {
                    obtain.setAccessibilityFocused(false);
                    accessibilityNodeInfoCompat.addAction(64);
                }
                if (this.mKeyboardFocusedVirtualViewId == i) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    accessibilityNodeInfoCompat.addAction(2);
                } else if (obtain.isFocusable()) {
                    accessibilityNodeInfoCompat.addAction(1);
                }
                obtain.setFocused(z);
                this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                if (this.mTempScreenRect.equals(rect)) {
                    Rect rect2 = this.mTempParentRect;
                    accessibilityNodeInfoCompat.setBoundsInParent(rect2);
                    Rect rect3 = new Rect();
                    rect3.set(rect2);
                    if (accessibilityNodeInfoCompat.mParentVirtualDescendantId != -1) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = new AccessibilityNodeInfoCompat(AccessibilityNodeInfo.obtain());
                        Rect rect4 = new Rect();
                        for (int i2 = accessibilityNodeInfoCompat.mParentVirtualDescendantId; i2 != -1; i2 = accessibilityNodeInfoCompat2.mParentVirtualDescendantId) {
                            View view3 = this.mHost;
                            accessibilityNodeInfoCompat2.mParentVirtualDescendantId = -1;
                            accessibilityNodeInfoCompat2.mInfo.setParent(view3, -1);
                            accessibilityNodeInfoCompat2.setBoundsInParent(INVALID_BOUNDS);
                            onPopulateNodeForVirtualView(i2, accessibilityNodeInfoCompat2);
                            accessibilityNodeInfoCompat2.mInfo.getBoundsInParent(rect4);
                            rect3.offset(rect4.left, rect4.top);
                        }
                        accessibilityNodeInfoCompat2.mInfo.recycle();
                    }
                    this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                    rect3.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                    accessibilityNodeInfoCompat.mInfo.setBoundsInScreen(rect3);
                    accessibilityNodeInfoCompat.mInfo.getBoundsInScreen(this.mTempScreenRect);
                }
                if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                    this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                    if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                        accessibilityNodeInfoCompat.mInfo.setBoundsInScreen(this.mTempScreenRect);
                        Rect rect5 = this.mTempScreenRect;
                        if (rect5 != null && !rect5.isEmpty() && this.mHost.getWindowVisibility() == 0) {
                            ViewParent parent = this.mHost.getParent();
                            while (true) {
                                if (parent instanceof View) {
                                    View view4 = (View) parent;
                                    if (view4.getAlpha() <= 0.0f || view4.getVisibility() != 0) {
                                        break;
                                    }
                                    parent = view4.getParent();
                                } else if (parent != null) {
                                    z2 = true;
                                }
                            }
                        }
                        if (z2) {
                            accessibilityNodeInfoCompat.mInfo.setVisibleToUser(true);
                        }
                    }
                }
                return accessibilityNodeInfoCompat;
            } else {
                throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
        } else {
            throw new RuntimeException("Callbacks must set parent bounds or screen bounds in populateNodeForVirtualViewId()");
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        onPopulateNodeForHost(accessibilityNodeInfoCompat);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }
}
