package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class NavigationMenuItemView extends ForegroundLinearLayout implements MenuView.ItemView {
    public static final int[] CHECKED_STATE_SET = {16842912};
    public final AnonymousClass1 accessibilityDelegate;
    public FrameLayout actionArea;
    public boolean checkable;
    public Drawable emptyDrawable;
    public boolean hasIconTintList;
    public int iconSize;
    public ColorStateList iconTintList;
    public MenuItemImpl itemData;
    public boolean needsEmptyIcon;
    public final CheckedTextView textView;

    public NavigationMenuItemView(Context context) {
        this(context, null);
    }

    public final void setIcon(Drawable drawable) {
        if (drawable != null) {
            if (this.hasIconTintList) {
                Drawable.ConstantState constantState = drawable.getConstantState();
                if (constantState != null) {
                    drawable = constantState.newDrawable();
                }
                drawable = drawable.mutate();
                drawable.setTintList(this.iconTintList);
            }
            int i = this.iconSize;
            drawable.setBounds(0, 0, i, i);
        } else if (this.needsEmptyIcon) {
            if (this.emptyDrawable == null) {
                Resources resources = getResources();
                Resources.Theme theme = getContext().getTheme();
                ThreadLocal<TypedValue> threadLocal = ResourcesCompat.sTempTypedValue;
                Drawable drawable2 = resources.getDrawable(2131232511, theme);
                this.emptyDrawable = drawable2;
                if (drawable2 != null) {
                    int i2 = this.iconSize;
                    drawable2.setBounds(0, 0, i2, i2);
                }
            }
            drawable = this.emptyDrawable;
        }
        this.textView.setCompoundDrawablesRelative(drawable, null, null, null);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public final void initialize(MenuItemImpl menuItemImpl) {
        int i;
        StateListDrawable stateListDrawable;
        this.itemData = menuItemImpl;
        Objects.requireNonNull(menuItemImpl);
        int i2 = menuItemImpl.mId;
        if (i2 > 0) {
            setId(i2);
        }
        if (menuItemImpl.isVisible()) {
            i = 0;
        } else {
            i = 8;
        }
        setVisibility(i);
        boolean z = true;
        if (getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(2130968819, typedValue, true)) {
                stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(CHECKED_STATE_SET, new ColorDrawable(typedValue.data));
                stateListDrawable.addState(ViewGroup.EMPTY_STATE_SET, new ColorDrawable(0));
            } else {
                stateListDrawable = null;
            }
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.setBackground(this, stateListDrawable);
        }
        boolean isCheckable = menuItemImpl.isCheckable();
        refreshDrawableState();
        if (this.checkable != isCheckable) {
            this.checkable = isCheckable;
            sendAccessibilityEvent(this.textView, 2048);
        }
        boolean isChecked = menuItemImpl.isChecked();
        refreshDrawableState();
        this.textView.setChecked(isChecked);
        setEnabled(menuItemImpl.isEnabled());
        this.textView.setText(menuItemImpl.mTitle);
        setIcon(menuItemImpl.getIcon());
        View actionView = menuItemImpl.getActionView();
        if (actionView != null) {
            if (this.actionArea == null) {
                this.actionArea = (FrameLayout) ((ViewStub) findViewById(2131427820)).inflate();
            }
            this.actionArea.removeAllViews();
            this.actionArea.addView(actionView);
        }
        setContentDescription(menuItemImpl.mContentDescription);
        setTooltipText(menuItemImpl.mTooltipText);
        MenuItemImpl menuItemImpl2 = this.itemData;
        Objects.requireNonNull(menuItemImpl2);
        if (!(menuItemImpl2.mTitle == null && this.itemData.getIcon() == null && this.itemData.getActionView() != null)) {
            z = false;
        }
        if (z) {
            this.textView.setVisibility(8);
            FrameLayout frameLayout = this.actionArea;
            if (frameLayout != null) {
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) frameLayout.getLayoutParams();
                ((LinearLayout.LayoutParams) layoutParams).width = -1;
                this.actionArea.setLayoutParams(layoutParams);
                return;
            }
            return;
        }
        this.textView.setVisibility(0);
        FrameLayout frameLayout2 = this.actionArea;
        if (frameLayout2 != null) {
            LinearLayoutCompat.LayoutParams layoutParams2 = (LinearLayoutCompat.LayoutParams) frameLayout2.getLayoutParams();
            ((LinearLayout.LayoutParams) layoutParams2).width = -2;
            this.actionArea.setLayoutParams(layoutParams2);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked()) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.google.android.material.internal.NavigationMenuItemView$1, androidx.core.view.AccessibilityDelegateCompat] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NavigationMenuItemView(android.content.Context r3, android.util.AttributeSet r4, int r5) {
        /*
            r2 = this;
            r2.<init>(r3, r4, r5)
            com.google.android.material.internal.NavigationMenuItemView$1 r4 = new com.google.android.material.internal.NavigationMenuItemView$1
            r4.<init>()
            r2.accessibilityDelegate = r4
            int r5 = r2.mOrientation
            if (r5 == 0) goto L_0x0014
            r5 = 0
            r2.mOrientation = r5
            r2.requestLayout()
        L_0x0014:
            android.view.LayoutInflater r5 = android.view.LayoutInflater.from(r3)
            r0 = 2131624079(0x7f0e008f, float:1.8875328E38)
            r1 = 1
            r5.inflate(r0, r2, r1)
            android.content.res.Resources r3 = r3.getResources()
            r5 = 2131165632(0x7f0701c0, float:1.7945487E38)
            int r3 = r3.getDimensionPixelSize(r5)
            r2.iconSize = r3
            r3 = 2131427821(0x7f0b01ed, float:1.847727E38)
            android.view.View r3 = r2.findViewById(r3)
            android.widget.CheckedTextView r3 = (android.widget.CheckedTextView) r3
            r2.textView = r3
            r3.setDuplicateParentStateEnabled(r1)
            androidx.core.view.ViewCompat.setAccessibilityDelegate(r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.NavigationMenuItemView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public final MenuItemImpl getItemData() {
        return this.itemData;
    }
}
