package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.view.CheckableLinearLayout;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ExpandableSwitchItem extends SwitchItem implements View.OnClickListener {
    public CharSequence collapsedSummary;
    public CharSequence expandedSummary;
    public boolean isExpanded = false;
    public final AnonymousClass1 accessibilityDelegate = new AccessibilityDelegateCompat() { // from class: com.google.android.setupdesign.items.ExpandableSwitchItem.1
        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 262144 && i != 524288) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            ExpandableSwitchItem expandableSwitchItem = ExpandableSwitchItem.this;
            Objects.requireNonNull(expandableSwitchItem);
            boolean z = expandableSwitchItem.isExpanded;
            boolean z2 = !z;
            if (z == z2) {
                return true;
            }
            expandableSwitchItem.isExpanded = z2;
            expandableSwitchItem.notifyItemRangeChanged(0, 1);
            return true;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            ExpandableSwitchItem expandableSwitchItem = ExpandableSwitchItem.this;
            Objects.requireNonNull(expandableSwitchItem);
            if (expandableSwitchItem.isExpanded) {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE;
            } else {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND;
            }
            accessibilityNodeInfoCompat.addAction(accessibilityActionCompat);
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.setupdesign.items.ExpandableSwitchItem$1] */
    public ExpandableSwitchItem() {
        this.iconGravity = 48;
    }

    @Override // com.google.android.setupdesign.items.SwitchItem, com.google.android.setupdesign.items.Item
    public final int getDefaultLayoutResource() {
        return 2131624560;
    }

    @Override // com.google.android.setupdesign.items.Item
    public final CharSequence getSummary() {
        if (this.isExpanded) {
            return this.expandedSummary;
        }
        return this.collapsedSummary;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        boolean z = this.isExpanded;
        boolean z2 = !z;
        if (z != z2) {
            this.isExpanded = z2;
            notifyItemRangeChanged(0, 1);
        }
    }

    @Override // com.google.android.setupdesign.items.SwitchItem, com.google.android.setupdesign.items.Item, com.google.android.setupdesign.items.AbstractItem
    public final void onBindView(View view) {
        Drawable[] compoundDrawables;
        Drawable[] compoundDrawablesRelative;
        super.onBindView(view);
        View findViewById = view.findViewById(2131428963);
        findViewById.setOnClickListener(this);
        if (findViewById instanceof CheckableLinearLayout) {
            CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) findViewById;
            checkableLinearLayout.setChecked(this.isExpanded);
            boolean z = this.isExpanded;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api19Impl.setAccessibilityLiveRegion(checkableLinearLayout, z ? 1 : 0);
            ViewCompat.setAccessibilityDelegate(checkableLinearLayout, this.accessibilityDelegate);
        }
        TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(new int[]{16842806});
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(0);
        obtainStyledAttributes.recycle();
        if (colorStateList != null) {
            TextView textView = (TextView) view.findViewById(2131428969);
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
                }
            }
            for (Drawable drawable2 : textView.getCompoundDrawablesRelative()) {
                if (drawable2 != null) {
                    drawable2.setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
                }
            }
        }
        view.setFocusable(false);
        LayoutStyler.applyPartnerCustomizationLayoutPaddingStyle(findViewById);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.google.android.setupdesign.items.ExpandableSwitchItem$1] */
    public ExpandableSwitchItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudExpandableSwitchItem);
        this.collapsedSummary = obtainStyledAttributes.getText(0);
        this.expandedSummary = obtainStyledAttributes.getText(1);
        this.iconGravity = obtainStyledAttributes.getInt(7, 48);
        obtainStyledAttributes.recycle();
    }
}
