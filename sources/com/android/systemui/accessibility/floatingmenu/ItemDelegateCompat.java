package com.android.systemui.accessibility.floatingmenu;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public final class ItemDelegateCompat extends RecyclerViewAccessibilityDelegate.ItemDelegate {
    public final WeakReference<AccessibilityFloatingMenuView> mMenuViewRef;

    @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate.ItemDelegate, androidx.core.view.AccessibilityDelegateCompat
    public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (this.mMenuViewRef.get() == null) {
            return super.performAccessibilityAction(view, i, bundle);
        }
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
        accessibilityFloatingMenuView.fadeIn();
        Rect availableBounds = accessibilityFloatingMenuView.getAvailableBounds();
        if (i == 2131427440) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.top);
            return true;
        } else if (i == 2131427441) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.top);
            return true;
        } else if (i == 2131427431) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.bottom);
            return true;
        } else if (i == 2131427432) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.bottom);
            return true;
        } else if (i == 2131427439) {
            accessibilityFloatingMenuView.setShapeType(1);
            return true;
        } else if (i != 2131427433) {
            return super.performAccessibilityAction(view, i, bundle);
        } else {
            accessibilityFloatingMenuView.setShapeType(0);
            return true;
        }
    }

    public ItemDelegateCompat(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(recyclerViewAccessibilityDelegate);
        this.mMenuViewRef = new WeakReference<>(accessibilityFloatingMenuView);
    }

    @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate.ItemDelegate, androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean z;
        int i;
        int i2;
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        if (this.mMenuViewRef.get() != null) {
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
            Resources resources = accessibilityFloatingMenuView.getResources();
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427440, resources.getString(2131951728)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427441, resources.getString(2131951729)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427431, resources.getString(2131951724)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427432, resources.getString(2131951725)));
            int i3 = accessibilityFloatingMenuView.mShapeType;
            boolean z2 = true;
            if (i3 == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i = 2131427439;
            } else {
                i = 2131427433;
            }
            if (i3 != 0) {
                z2 = false;
            }
            if (z2) {
                i2 = 2131951727;
            } else {
                i2 = 2131951726;
            }
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, resources.getString(i2)));
        }
    }
}
