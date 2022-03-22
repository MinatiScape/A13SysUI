package androidx.core.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressLint({"PrivateConstructorForUtilityClass"})
/* loaded from: classes.dex */
public final class ViewCompat {
    public static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;
    public static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS = {2131427369, 2131427370, 2131427381, 2131427392, 2131427395, 2131427396, 2131427397, 2131427398, 2131427399, 2131427400, 2131427371, 2131427372, 2131427373, 2131427374, 2131427375, 2131427376, 2131427377, 2131427378, 2131427379, 2131427380, 2131427382, 2131427383, 2131427384, 2131427385, 2131427386, 2131427387, 2131427388, 2131427389, 2131427390, 2131427391, 2131427393, 2131427394};
    public static final ViewCompat$$ExternalSyntheticLambda0 NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = ViewCompat$$ExternalSyntheticLambda0.INSTANCE;
    public static final AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();

    /* renamed from: androidx.core.view.ViewCompat$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 extends AccessibilityViewProperty<CharSequence> {
        @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
        public final void frameworkSet(View view, CharSequence charSequence) {
            Api30Impl.setStateDescription(view, charSequence);
        }

        @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
        public final boolean shouldUpdate(CharSequence charSequence, CharSequence charSequence2) {
            return !TextUtils.equals(charSequence, charSequence2);
        }

        public AnonymousClass3(Class cls) {
            super(2131429013, cls, 64, 30);
        }

        @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
        public final CharSequence frameworkGet(View view) {
            return Api30Impl.getStateDescription(view);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AccessibilityViewProperty<T> {
        public final int mContentChangeType;
        public final int mFrameworkMinimumSdk;
        public final int mTagKey;
        public final Class<T> mType;

        public static boolean booleanNullToFalseEquals(Boolean bool, Boolean bool2) {
            boolean z;
            boolean z2;
            if (bool == null || !bool.booleanValue()) {
                z = false;
            } else {
                z = true;
            }
            if (bool2 == null || !bool2.booleanValue()) {
                z2 = false;
            } else {
                z2 = true;
            }
            return z == z2;
        }

        public abstract T frameworkGet(View view);

        public abstract void frameworkSet(View view, T t);

        public abstract boolean shouldUpdate(T t, T t2);

        public final T get(View view) {
            boolean z;
            if (Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return frameworkGet(view);
            }
            T t = (T) view.getTag(this.mTagKey);
            if (this.mType.isInstance(t)) {
                return t;
            }
            return null;
        }

        public final void set(View view, T t) {
            boolean z;
            AccessibilityDelegateCompat accessibilityDelegateCompat;
            if (Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                frameworkSet(view, t);
            } else if (shouldUpdate(get(view), t)) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                View.AccessibilityDelegate accessibilityDelegate = Api29Impl.getAccessibilityDelegate(view);
                if (accessibilityDelegate == null) {
                    accessibilityDelegateCompat = null;
                } else if (accessibilityDelegate instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
                    accessibilityDelegateCompat = ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) accessibilityDelegate).mCompat;
                } else {
                    accessibilityDelegateCompat = new AccessibilityDelegateCompat(accessibilityDelegate);
                }
                if (accessibilityDelegateCompat == null) {
                    accessibilityDelegateCompat = new AccessibilityDelegateCompat();
                }
                ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
                view.setTag(this.mTagKey, t);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, this.mContentChangeType);
            }
        }

        public AccessibilityViewProperty(int i, Class<T> cls, int i2, int i3) {
            this.mTagKey = i;
            this.mType = cls;
            this.mContentChangeType = i2;
            this.mFrameworkMinimumSdk = i3;
        }
    }

    /* loaded from: classes.dex */
    public static class Api21Impl {
        public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) view.getTag(2131429017));
            } else {
                view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: androidx.core.view.ViewCompat.Api21Impl.1
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public WindowInsets onApplyWindowInsets(View view2, WindowInsets windowInsets) {
                        return onApplyWindowInsetsListener.onApplyWindowInsets(view2, WindowInsetsCompat.toWindowInsetsCompat(windowInsets, view2)).toWindowInsets();
                    }
                });
            }
        }

        public static WindowInsetsCompat computeSystemWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, Rect rect) {
            WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
            if (windowInsets != null) {
                return WindowInsetsCompat.toWindowInsetsCompat(view.computeSystemWindowInsets(windowInsets, rect), view);
            }
            rect.setEmpty();
            return windowInsetsCompat;
        }

        public static ColorStateList getBackgroundTintList(View view) {
            return view.getBackgroundTintList();
        }

        public static PorterDuff.Mode getBackgroundTintMode(View view) {
            return view.getBackgroundTintMode();
        }

        public static float getElevation(View view) {
            return view.getElevation();
        }

        public static String getTransitionName(View view) {
            return view.getTransitionName();
        }

        public static float getTranslationZ(View view) {
            return view.getTranslationZ();
        }

        public static float getZ(View view) {
            return view.getZ();
        }

        public static boolean isNestedScrollingEnabled(View view) {
            return view.isNestedScrollingEnabled();
        }

        public static void stopNestedScroll(View view) {
            view.stopNestedScroll();
        }

        public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
            view.setBackgroundTintList(colorStateList);
        }

        public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            view.setBackgroundTintMode(mode);
        }

        public static void setElevation(View view, float f) {
            view.setElevation(f);
        }

        public static void setTransitionName(View view, String str) {
            view.setTransitionName(str);
        }

        public static void setTranslationZ(View view, float f) {
            view.setTranslationZ(f);
        }

        public static void setZ(View view, float f) {
            view.setZ(f);
        }
    }

    public static void replaceAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, String str, AccessibilityViewCommand accessibilityViewCommand) {
        AccessibilityDelegateCompat accessibilityDelegateCompat;
        if (accessibilityViewCommand == null && str == null) {
            removeActionWithId(accessibilityActionCompat.getId(), view);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
            return;
        }
        Objects.requireNonNull(accessibilityActionCompat);
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat2 = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(null, accessibilityActionCompat.mId, str, accessibilityViewCommand, accessibilityActionCompat.mViewCommandArgumentClass);
        View.AccessibilityDelegate accessibilityDelegate = Api29Impl.getAccessibilityDelegate(view);
        if (accessibilityDelegate == null) {
            accessibilityDelegateCompat = null;
        } else if (accessibilityDelegate instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            accessibilityDelegateCompat = ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) accessibilityDelegate).mCompat;
        } else {
            accessibilityDelegateCompat = new AccessibilityDelegateCompat(accessibilityDelegate);
        }
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateCompat = new AccessibilityDelegateCompat();
        }
        setAccessibilityDelegate(view, accessibilityDelegateCompat);
        removeActionWithId(accessibilityActionCompat2.getId(), view);
        getActionList(view).add(accessibilityActionCompat2);
        notifyViewAccessibilityStateChangedIfNeeded(view, 0);
    }

    /* loaded from: classes.dex */
    public static class AccessibilityPaneVisibilityManager implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {
        public final WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap<>();

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    /* loaded from: classes.dex */
    public static class Api15Impl {
        public static boolean hasOnClickListeners(View view) {
            return view.hasOnClickListeners();
        }
    }

    /* loaded from: classes.dex */
    public static class Api16Impl {
        public static boolean getFitsSystemWindows(View view) {
            return view.getFitsSystemWindows();
        }

        public static int getImportantForAccessibility(View view) {
            return view.getImportantForAccessibility();
        }

        public static int getMinimumHeight(View view) {
            return view.getMinimumHeight();
        }

        public static int getMinimumWidth(View view) {
            return view.getMinimumWidth();
        }

        public static int getWindowSystemUiVisibility(View view) {
            return view.getWindowSystemUiVisibility();
        }

        public static boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }

        public static boolean hasTransientState(View view) {
            return view.hasTransientState();
        }

        public static boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            return view.performAccessibilityAction(i, bundle);
        }

        public static void postInvalidateOnAnimation(View view) {
            view.postInvalidateOnAnimation();
        }

        public static void postOnAnimation(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        public static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        public static void setBackground(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        public static void setHasTransientState(View view, boolean z) {
            view.setHasTransientState(z);
        }

        public static void setImportantForAccessibility(View view, int i) {
            view.setImportantForAccessibility(i);
        }

        public static void postOnAnimationDelayed(View view, Runnable runnable, long j) {
            view.postOnAnimationDelayed(runnable, j);
        }
    }

    /* loaded from: classes.dex */
    public static class Api17Impl {
        public static int generateViewId() {
            return View.generateViewId();
        }

        public static Display getDisplay(View view) {
            return view.getDisplay();
        }

        public static int getLayoutDirection(View view) {
            return view.getLayoutDirection();
        }

        public static int getPaddingEnd(View view) {
            return view.getPaddingEnd();
        }

        public static int getPaddingStart(View view) {
            return view.getPaddingStart();
        }

        public static boolean isPaddingRelative(View view) {
            return view.isPaddingRelative();
        }

        public static void setPaddingRelative(View view, int i, int i2, int i3, int i4) {
            view.setPaddingRelative(i, i2, i3, i4);
        }
    }

    /* loaded from: classes.dex */
    public static class Api18Impl {
        public static Rect getClipBounds(View view) {
            return view.getClipBounds();
        }

        public static void setClipBounds(View view, Rect rect) {
            view.setClipBounds(rect);
        }
    }

    /* loaded from: classes.dex */
    public static class Api19Impl {
        public static int getAccessibilityLiveRegion(View view) {
            return view.getAccessibilityLiveRegion();
        }

        public static boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        public static boolean isLaidOut(View view) {
            return view.isLaidOut();
        }

        public static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, i);
        }

        public static void setAccessibilityLiveRegion(View view, int i) {
            view.setAccessibilityLiveRegion(i);
        }

        public static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
            accessibilityEvent.setContentChangeTypes(i);
        }
    }

    /* loaded from: classes.dex */
    public static class Api20Impl {
        public static WindowInsets dispatchApplyWindowInsets(View view, WindowInsets windowInsets) {
            return view.dispatchApplyWindowInsets(windowInsets);
        }

        public static WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            return view.onApplyWindowInsets(windowInsets);
        }

        public static void requestApplyInsets(View view) {
            view.requestApplyInsets();
        }
    }

    /* loaded from: classes.dex */
    public static class Api23Impl {
        public static WindowInsetsCompat getRootWindowInsets(View view) {
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return null;
            }
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets, null);
            windowInsetsCompat.mImpl.setRootWindowInsets(windowInsetsCompat);
            windowInsetsCompat.mImpl.copyRootViewBounds(view.getRootView());
            return windowInsetsCompat;
        }

        public static void setScrollIndicators(View view, int i, int i2) {
            view.setScrollIndicators(i, i2);
        }
    }

    /* loaded from: classes.dex */
    public static class Api26Impl {
        public static int getImportantForAutofill(View view) {
            return view.getImportantForAutofill();
        }

        public static void setImportantForAutofill(View view, int i) {
            view.setImportantForAutofill(i);
        }
    }

    /* loaded from: classes.dex */
    public static class Api28Impl {
        public static CharSequence getAccessibilityPaneTitle(View view) {
            return view.getAccessibilityPaneTitle();
        }

        public static boolean isAccessibilityHeading(View view) {
            return view.isAccessibilityHeading();
        }

        public static boolean isScreenReaderFocusable(View view) {
            return view.isScreenReaderFocusable();
        }

        public static void setAccessibilityHeading(View view, boolean z) {
            view.setAccessibilityHeading(z);
        }

        public static void setAccessibilityPaneTitle(View view, CharSequence charSequence) {
            view.setAccessibilityPaneTitle(charSequence);
        }
    }

    /* loaded from: classes.dex */
    public static class Api29Impl {
        public static View.AccessibilityDelegate getAccessibilityDelegate(View view) {
            return view.getAccessibilityDelegate();
        }

        public static void saveAttributeDataForStyleable(View view, Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i, int i2) {
            view.saveAttributeDataForStyleable(context, iArr, attributeSet, typedArray, i, i2);
        }
    }

    /* loaded from: classes.dex */
    public static class Api30Impl {
        public static CharSequence getStateDescription(View view) {
            return view.getStateDescription();
        }

        public static void setStateDescription(View view, CharSequence charSequence) {
            view.setStateDescription(charSequence);
        }
    }

    static {
        new AtomicInteger(1);
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        if (sViewPropertyAnimatorMap == null) {
            sViewPropertyAnimatorMap = new WeakHashMap<>();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = sViewPropertyAnimatorMap.get(view);
        if (viewPropertyAnimatorCompat != null) {
            return viewPropertyAnimatorCompat;
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = new ViewPropertyAnimatorCompat(view);
        sViewPropertyAnimatorMap.put(view, viewPropertyAnimatorCompat2);
        return viewPropertyAnimatorCompat2;
    }

    public static void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        AccessibilityDelegateCompat.AccessibilityDelegateAdapter accessibilityDelegateAdapter;
        if (accessibilityDelegateCompat == null && (Api29Impl.getAccessibilityDelegate(view) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter)) {
            accessibilityDelegateCompat = new AccessibilityDelegateCompat();
        }
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateAdapter = null;
        } else {
            accessibilityDelegateAdapter = accessibilityDelegateCompat.mBridge;
        }
        view.setAccessibilityDelegate(accessibilityDelegateAdapter);
    }

    public static void setAccessibilityPaneTitle(View view, CharSequence charSequence) {
        boolean z;
        new AccessibilityViewProperty<CharSequence>(CharSequence.class) { // from class: androidx.core.view.ViewCompat.2
            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public final void frameworkSet(View view2, CharSequence charSequence2) {
                Api28Impl.setAccessibilityPaneTitle(view2, charSequence2);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public final boolean shouldUpdate(CharSequence charSequence2, CharSequence charSequence3) {
                return !TextUtils.equals(charSequence2, charSequence3);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public final CharSequence frameworkGet(View view2) {
                return Api28Impl.getAccessibilityPaneTitle(view2);
            }
        }.set(view, charSequence);
        if (charSequence != null) {
            AccessibilityPaneVisibilityManager accessibilityPaneVisibilityManager = sAccessibilityPaneVisibilityManager;
            Objects.requireNonNull(accessibilityPaneVisibilityManager);
            WeakHashMap<View, Boolean> weakHashMap = accessibilityPaneVisibilityManager.mPanesToVisible;
            if (view.getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            weakHashMap.put(view, Boolean.valueOf(z));
            view.addOnAttachStateChangeListener(accessibilityPaneVisibilityManager);
            if (Api19Impl.isAttachedToWindow(view)) {
                view.getViewTreeObserver().addOnGlobalLayoutListener(accessibilityPaneVisibilityManager);
                return;
            }
            return;
        }
        AccessibilityPaneVisibilityManager accessibilityPaneVisibilityManager2 = sAccessibilityPaneVisibilityManager;
        Objects.requireNonNull(accessibilityPaneVisibilityManager2);
        accessibilityPaneVisibilityManager2.mPanesToVisible.remove(view);
        view.removeOnAttachStateChangeListener(accessibilityPaneVisibilityManager2);
        Api16Impl.removeOnGlobalLayoutListener(view.getViewTreeObserver(), accessibilityPaneVisibilityManager2);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
        if (windowInsets != null) {
            WindowInsets dispatchApplyWindowInsets = Api20Impl.dispatchApplyWindowInsets(view, windowInsets);
            if (!dispatchApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(dispatchApplyWindowInsets, view);
            }
        }
        return windowInsetsCompat;
    }

    public static ArrayList getActionList(View view) {
        ArrayList arrayList = (ArrayList) view.getTag(2131429005);
        if (arrayList != null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        view.setTag(2131429005, arrayList2);
        return arrayList2;
    }

    public static void notifyViewAccessibilityStateChangedIfNeeded(View view, int i) {
        boolean z;
        AccessibilityManager accessibilityManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            if (Api28Impl.getAccessibilityPaneTitle(view) == null || view.getVisibility() != 0) {
                z = false;
            } else {
                z = true;
            }
            int i2 = 32;
            if (Api19Impl.getAccessibilityLiveRegion(view) != 0 || z) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain();
                if (!z) {
                    i2 = 2048;
                }
                obtain.setEventType(i2);
                Api19Impl.setContentChangeTypes(obtain, i);
                if (z) {
                    obtain.getText().add(Api28Impl.getAccessibilityPaneTitle(view));
                    if (Api16Impl.getImportantForAccessibility(view) == 0) {
                        Api16Impl.setImportantForAccessibility(view, 1);
                    }
                    ViewParent parent = view.getParent();
                    while (true) {
                        if (!(parent instanceof View)) {
                            break;
                        } else if (Api16Impl.getImportantForAccessibility((View) parent) == 4) {
                            Api16Impl.setImportantForAccessibility(view, 2);
                            break;
                        } else {
                            parent = parent.getParent();
                        }
                    }
                }
                view.sendAccessibilityEventUnchecked(obtain);
            } else if (i == 32) {
                AccessibilityEvent obtain2 = AccessibilityEvent.obtain();
                view.onInitializeAccessibilityEvent(obtain2);
                obtain2.setEventType(32);
                Api19Impl.setContentChangeTypes(obtain2, i);
                obtain2.setSource(view);
                view.onPopulateAccessibilityEvent(obtain2);
                obtain2.getText().add(Api28Impl.getAccessibilityPaneTitle(view));
                accessibilityManager.sendAccessibilityEvent(obtain2);
            } else if (view.getParent() != null) {
                try {
                    Api19Impl.notifySubtreeAccessibilityStateChanged(view.getParent(), view, view, i);
                } catch (AbstractMethodError e) {
                    Log.e("ViewCompat", view.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e);
                }
            }
        }
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
        if (windowInsets != null) {
            WindowInsets onApplyWindowInsets = Api20Impl.onApplyWindowInsets(view, windowInsets);
            if (!onApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(onApplyWindowInsets, view);
            }
        }
        return windowInsetsCompat;
    }

    public static void removeActionWithId(int i, View view) {
        ArrayList actionList = getActionList(view);
        for (int i2 = 0; i2 < actionList.size(); i2++) {
            if (((AccessibilityNodeInfoCompat.AccessibilityActionCompat) actionList.get(i2)).getId() == i) {
                actionList.remove(i2);
                return;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Api24Impl {
        public static void setPointerIcon(View view, PointerIcon pointerIcon) {
            view.setPointerIcon(pointerIcon);
        }
    }
}
