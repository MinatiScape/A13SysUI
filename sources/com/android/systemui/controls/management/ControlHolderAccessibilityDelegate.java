package com.android.systemui.controls.management;

import android.os.Bundle;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlHolderAccessibilityDelegate extends AccessibilityDelegateCompat {
    public boolean isFavorite;
    public final ControlsModel.MoveHelper moveHelper;
    public final Function0<Integer> positionRetriever;
    public final Function1<Boolean, CharSequence> stateRetriever;

    /* JADX WARN: Multi-variable type inference failed */
    public ControlHolderAccessibilityDelegate(Function1<? super Boolean, ? extends CharSequence> function1, Function0<Integer> function0, ControlsModel.MoveHelper moveHelper) {
        this.stateRetriever = function1;
        this.positionRetriever = function0;
        this.moveHelper = moveHelper;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        String str;
        boolean z;
        boolean z2;
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.mInfo.setContextClickable(false);
        if (this.isFavorite) {
            str = view.getContext().getString(2131951691);
        } else {
            str = view.getContext().getString(2131951690);
        }
        accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, str));
        ControlsModel.MoveHelper moveHelper = this.moveHelper;
        if (moveHelper == null) {
            z = false;
        } else {
            z = moveHelper.canMoveBefore(this.positionRetriever.invoke().intValue());
        }
        if (z) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427359, view.getContext().getString(2131951694, Integer.valueOf((this.positionRetriever.invoke().intValue() + 1) - 1))));
            accessibilityNodeInfoCompat.mInfo.setContextClickable(true);
        }
        ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
        if (moveHelper2 == null) {
            z2 = false;
        } else {
            z2 = moveHelper2.canMoveAfter(this.positionRetriever.invoke().intValue());
        }
        if (z2) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131427358, view.getContext().getString(2131951694, Integer.valueOf(this.positionRetriever.invoke().intValue() + 1 + 1))));
            accessibilityNodeInfoCompat.mInfo.setContextClickable(true);
        }
        accessibilityNodeInfoCompat.mInfo.setStateDescription(this.stateRetriever.invoke(Boolean.valueOf(this.isFavorite)));
        accessibilityNodeInfoCompat.setCollectionItemInfo(null);
        accessibilityNodeInfoCompat.setClassName("android.widget.Switch");
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        if (i == 2131427359) {
            ControlsModel.MoveHelper moveHelper = this.moveHelper;
            if (moveHelper == null) {
                return true;
            }
            moveHelper.moveBefore(this.positionRetriever.invoke().intValue());
            return true;
        } else if (i != 2131427358) {
            return false;
        } else {
            ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
            if (moveHelper2 == null) {
                return true;
            }
            moveHelper2.moveAfter(this.positionRetriever.invoke().intValue());
            return true;
        }
    }
}
