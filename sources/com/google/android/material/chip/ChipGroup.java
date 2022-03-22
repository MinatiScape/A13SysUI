package com.google.android.material.chip;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.FlowLayout;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ChipGroup extends FlowLayout {
    public int checkedId;
    public final CheckedStateTracker checkedStateTracker;
    public int chipSpacingHorizontal;
    public int chipSpacingVertical;
    public PassThroughHierarchyChangeListener passThroughListener;
    public boolean protectFromCheckedChange;
    public boolean selectionRequired;
    public boolean singleSelection;

    /* loaded from: classes.dex */
    public class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public CheckedStateTracker() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            ChipGroup chipGroup = ChipGroup.this;
            if (!chipGroup.protectFromCheckedChange) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    View childAt = chipGroup.getChildAt(i);
                    if ((childAt instanceof Chip) && ((Chip) childAt).isChecked()) {
                        arrayList.add(Integer.valueOf(childAt.getId()));
                        if (chipGroup.singleSelection) {
                            break;
                        }
                    }
                }
                if (arrayList.isEmpty()) {
                    ChipGroup chipGroup2 = ChipGroup.this;
                    if (chipGroup2.selectionRequired) {
                        chipGroup2.setCheckedStateForView(compoundButton.getId(), true);
                        ChipGroup chipGroup3 = ChipGroup.this;
                        int id = compoundButton.getId();
                        Objects.requireNonNull(chipGroup3);
                        chipGroup3.checkedId = id;
                        return;
                    }
                }
                int id2 = compoundButton.getId();
                if (z) {
                    ChipGroup chipGroup4 = ChipGroup.this;
                    int i2 = chipGroup4.checkedId;
                    if (!(i2 == -1 || i2 == id2 || !chipGroup4.singleSelection)) {
                        chipGroup4.setCheckedStateForView(i2, false);
                    }
                    ChipGroup chipGroup5 = ChipGroup.this;
                    Objects.requireNonNull(chipGroup5);
                    chipGroup5.checkedId = id2;
                    return;
                }
                ChipGroup chipGroup6 = ChipGroup.this;
                if (chipGroup6.checkedId == id2) {
                    chipGroup6.checkedId = -1;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams() {
            super(-2, -2);
        }
    }

    /* loaded from: classes.dex */
    public class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        public ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener;

        public PassThroughHierarchyChangeListener() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public final void onChildViewAdded(View view, View view2) {
            if (view == ChipGroup.this && (view2 instanceof Chip)) {
                if (view2.getId() == -1) {
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    view2.setId(ViewCompat.Api17Impl.generateViewId());
                }
                Chip chip = (Chip) view2;
                if (chip.isChecked()) {
                    ChipGroup chipGroup = (ChipGroup) view;
                    int id = chip.getId();
                    Objects.requireNonNull(chipGroup);
                    int i = chipGroup.checkedId;
                    if (id != i) {
                        if (i != -1 && chipGroup.singleSelection) {
                            chipGroup.setCheckedStateForView(i, false);
                        }
                        if (id != -1) {
                            chipGroup.setCheckedStateForView(id, true);
                        }
                        chipGroup.checkedId = id;
                    }
                }
                chip.onCheckedChangeListenerInternal = ChipGroup.this.checkedStateTracker;
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.onHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public final void onChildViewRemoved(View view, View view2) {
            if (view == ChipGroup.this && (view2 instanceof Chip)) {
                Chip chip = (Chip) view2;
                Objects.requireNonNull(chip);
                chip.onCheckedChangeListenerInternal = null;
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.onHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    public ChipGroup(Context context) {
        this(context, null);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public ChipGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968768);
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof Chip) {
            Chip chip = (Chip) view;
            if (chip.isChecked()) {
                int i2 = this.checkedId;
                if (i2 != -1 && this.singleSelection) {
                    setCheckedStateForView(i2, false);
                }
                this.checkedId = chip.getId();
            }
        }
        super.addView(view, i, layoutParams);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    public final void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.passThroughListener.onHierarchyChangeListener = onHierarchyChangeListener;
    }

    public ChipGroup(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2132018651), attributeSet, i);
        this.checkedStateTracker = new CheckedStateTracker();
        this.passThroughListener = new PassThroughHierarchyChangeListener();
        this.checkedId = -1;
        this.protectFromCheckedChange = false;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(getContext(), attributeSet, R$styleable.ChipGroup, i, 2132018651, new int[0]);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(1, 0);
        int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(2, dimensionPixelOffset);
        if (this.chipSpacingHorizontal != dimensionPixelOffset2) {
            this.chipSpacingHorizontal = dimensionPixelOffset2;
            this.itemSpacing = dimensionPixelOffset2;
            requestLayout();
        }
        int dimensionPixelOffset3 = obtainStyledAttributes.getDimensionPixelOffset(3, dimensionPixelOffset);
        if (this.chipSpacingVertical != dimensionPixelOffset3) {
            this.chipSpacingVertical = dimensionPixelOffset3;
            this.lineSpacing = dimensionPixelOffset3;
            requestLayout();
        }
        this.singleLine = obtainStyledAttributes.getBoolean(5, false);
        boolean z = obtainStyledAttributes.getBoolean(6, false);
        if (this.singleSelection != z) {
            this.singleSelection = z;
            this.protectFromCheckedChange = true;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                View childAt = getChildAt(i2);
                if (childAt instanceof Chip) {
                    ((Chip) childAt).setChecked(false);
                }
            }
            this.protectFromCheckedChange = false;
            this.checkedId = -1;
        }
        this.selectionRequired = obtainStyledAttributes.getBoolean(4, false);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        if (resourceId != -1) {
            this.checkedId = resourceId;
        }
        obtainStyledAttributes.recycle();
        super.setOnHierarchyChangeListener(this.passThroughListener);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (!super.checkLayoutParams(layoutParams) || !(layoutParams instanceof LayoutParams)) {
            return false;
        }
        return true;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        int i = this.checkedId;
        if (i != -1) {
            setCheckedStateForView(i, true);
            this.checkedId = this.checkedId;
        }
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        int i2;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.singleLine) {
            i = 0;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                if (getChildAt(i3) instanceof Chip) {
                    i++;
                }
            }
        } else {
            i = -1;
        }
        int i4 = this.rowCount;
        if (this.singleSelection) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo) AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(i4, i, i2).mInfo);
    }

    public final void setCheckedStateForView(int i, boolean z) {
        View findViewById = findViewById(i);
        if (findViewById instanceof Chip) {
            this.protectFromCheckedChange = true;
            ((Chip) findViewById).setChecked(z);
            this.protectFromCheckedChange = false;
        }
    }

    @Override // com.google.android.material.internal.FlowLayout
    public final boolean isSingleLine() {
        return this.singleLine;
    }
}
