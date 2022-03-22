package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.leanback.R$string;
import com.google.android.material.R$styleable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MaterialButtonToggleGroup extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int checkedId;
    public final CheckedStateTracker checkedStateTracker;
    public Integer[] childOrder;
    public final AnonymousClass1 childOrderComparator;
    public final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    public final ArrayList originalCornerData;
    public final PressedStateTracker pressedStateTracker;
    public boolean selectionRequired;
    public boolean singleSelection;
    public boolean skipCheckedStateTracker;

    /* loaded from: classes.dex */
    public class CheckedStateTracker implements MaterialButton.OnCheckedChangeListener {
        public CheckedStateTracker() {
        }

        @Override // com.google.android.material.button.MaterialButton.OnCheckedChangeListener
        public final void onCheckedChanged(MaterialButton materialButton, boolean z) {
            int i;
            MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
            if (!materialButtonToggleGroup.skipCheckedStateTracker) {
                if (materialButtonToggleGroup.singleSelection) {
                    if (z) {
                        i = materialButton.getId();
                    } else {
                        i = -1;
                    }
                    materialButtonToggleGroup.checkedId = i;
                }
                if (MaterialButtonToggleGroup.this.updateCheckedStates(materialButton.getId(), z)) {
                    MaterialButtonToggleGroup.this.dispatchOnButtonChecked(materialButton.getId(), materialButton.isChecked());
                }
                MaterialButtonToggleGroup.this.invalidate();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnButtonCheckedListener {
        void onButtonChecked();
    }

    /* loaded from: classes.dex */
    public class PressedStateTracker implements MaterialButton.OnPressedChangeListener {
        public PressedStateTracker() {
        }
    }

    public MaterialButtonToggleGroup(Context context) {
        this(context, null);
    }

    /* loaded from: classes.dex */
    public static class CornerData {
        public static final AbsoluteCornerSize noCorner = new AbsoluteCornerSize(0.0f);
        public CornerSize bottomLeft;
        public CornerSize bottomRight;
        public CornerSize topLeft;
        public CornerSize topRight;

        public CornerData(CornerSize cornerSize, CornerSize cornerSize2, CornerSize cornerSize3, CornerSize cornerSize4) {
            this.topLeft = cornerSize;
            this.topRight = cornerSize3;
            this.bottomRight = cornerSize4;
            this.bottomLeft = cornerSize2;
        }
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969415);
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof MaterialButton)) {
            Log.e("MaterialButtonToggleGroup", "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(view, i, layoutParams);
        MaterialButton materialButton = (MaterialButton) view;
        if (materialButton.getId() == -1) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            materialButton.setId(ViewCompat.Api17Impl.generateViewId());
        }
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        if (materialButton.isUsingOriginalBackground()) {
            MaterialButtonHelper materialButtonHelper = materialButton.materialButtonHelper;
            Objects.requireNonNull(materialButtonHelper);
            materialButtonHelper.checkable = true;
        }
        materialButton.onCheckedChangeListeners.add(this.checkedStateTracker);
        materialButton.onPressedChangeListenerInternal = this.pressedStateTracker;
        if (materialButton.isUsingOriginalBackground()) {
            MaterialButtonHelper materialButtonHelper2 = materialButton.materialButtonHelper;
            Objects.requireNonNull(materialButtonHelper2);
            materialButtonHelper2.shouldDrawSurfaceColorStroke = true;
            int i2 = 0;
            MaterialShapeDrawable materialShapeDrawable = materialButtonHelper2.getMaterialShapeDrawable(false);
            MaterialShapeDrawable materialShapeDrawable2 = materialButtonHelper2.getMaterialShapeDrawable(true);
            if (materialShapeDrawable != null) {
                ColorStateList colorStateList = materialButtonHelper2.strokeColor;
                materialShapeDrawable.drawableState.strokeWidth = materialButtonHelper2.strokeWidth;
                materialShapeDrawable.invalidateSelf();
                materialShapeDrawable.setStrokeColor(colorStateList);
                if (materialShapeDrawable2 != null) {
                    float f = materialButtonHelper2.strokeWidth;
                    if (materialButtonHelper2.shouldDrawSurfaceColorStroke) {
                        i2 = R$string.getColor(materialButtonHelper2.materialButton, 2130968847);
                    }
                    materialShapeDrawable2.drawableState.strokeWidth = f;
                    materialShapeDrawable2.invalidateSelf();
                    materialShapeDrawable2.setStrokeColor(ColorStateList.valueOf(i2));
                }
            }
        }
        if (materialButton.isChecked()) {
            updateCheckedStates(materialButton.getId(), true);
            int id = materialButton.getId();
            this.checkedId = id;
            dispatchOnButtonChecked(id, true);
        }
        if (materialButton.isUsingOriginalBackground()) {
            MaterialButtonHelper materialButtonHelper3 = materialButton.materialButtonHelper;
            Objects.requireNonNull(materialButtonHelper3);
            ShapeAppearanceModel shapeAppearanceModel = materialButtonHelper3.shapeAppearanceModel;
            ArrayList arrayList = this.originalCornerData;
            Objects.requireNonNull(shapeAppearanceModel);
            arrayList.add(new CornerData(shapeAppearanceModel.topLeftCornerSize, shapeAppearanceModel.bottomLeftCornerSize, shapeAppearanceModel.topRightCornerSize, shapeAppearanceModel.bottomRightCornerSize));
            ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() { // from class: com.google.android.material.button.MaterialButtonToggleGroup.2
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public final void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    int i3;
                    super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfoCompat);
                    MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
                    int i4 = MaterialButtonToggleGroup.$r8$clinit;
                    Objects.requireNonNull(materialButtonToggleGroup);
                    if (view2 instanceof MaterialButton) {
                        i3 = 0;
                        for (int i5 = 0; i5 < materialButtonToggleGroup.getChildCount(); i5++) {
                            if (materialButtonToggleGroup.getChildAt(i5) == view2) {
                                break;
                            }
                            if ((materialButtonToggleGroup.getChildAt(i5) instanceof MaterialButton) && materialButtonToggleGroup.isChildVisible(i5)) {
                                i3++;
                            }
                        }
                    }
                    i3 = -1;
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, i3, 1, ((MaterialButton) view2).isChecked()));
                }
            });
            return;
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        TreeMap treeMap = new TreeMap(this.childOrderComparator);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            treeMap.put(getChildButton(i), Integer.valueOf(i));
        }
        this.childOrder = (Integer[]) treeMap.values().toArray(new Integer[0]);
        super.dispatchDraw(canvas);
    }

    public final void dispatchOnButtonChecked(int i, boolean z) {
        Iterator<OnButtonCheckedListener> it = this.onButtonCheckedListeners.iterator();
        while (it.hasNext()) {
            it.next().onButtonChecked();
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    @Override // android.view.ViewGroup
    public final int getChildDrawingOrder(int i, int i2) {
        Integer[] numArr = this.childOrder;
        if (numArr != null && i2 < numArr.length) {
            return numArr[i2].intValue();
        }
        Log.w("MaterialButtonToggleGroup", "Child order wasn't updated");
        return i2;
    }

    public final boolean updateCheckedStates(int i, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            MaterialButton childButton = getChildButton(i2);
            if (childButton.isChecked()) {
                arrayList.add(Integer.valueOf(childButton.getId()));
            }
        }
        if (!this.selectionRequired || !arrayList.isEmpty()) {
            if (z && this.singleSelection) {
                arrayList.remove(Integer.valueOf(i));
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    int intValue = ((Integer) it.next()).intValue();
                    View findViewById = findViewById(intValue);
                    if (findViewById instanceof MaterialButton) {
                        this.skipCheckedStateTracker = true;
                        ((MaterialButton) findViewById).setChecked(false);
                        this.skipCheckedStateTracker = false;
                    }
                    dispatchOnButtonChecked(intValue, false);
                }
            }
            return true;
        }
        View findViewById2 = findViewById(i);
        if (findViewById2 instanceof MaterialButton) {
            this.skipCheckedStateTracker = true;
            ((MaterialButton) findViewById2).setChecked(true);
            this.skipCheckedStateTracker = false;
        }
        this.checkedId = i;
        return false;
    }

    /* JADX WARN: Type inference failed for: r7v6, types: [com.google.android.material.button.MaterialButtonToggleGroup$1] */
    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2132018665), attributeSet, i);
        this.originalCornerData = new ArrayList();
        this.checkedStateTracker = new CheckedStateTracker();
        this.pressedStateTracker = new PressedStateTracker();
        this.onButtonCheckedListeners = new LinkedHashSet<>();
        this.childOrderComparator = new Comparator<MaterialButton>() { // from class: com.google.android.material.button.MaterialButtonToggleGroup.1
            @Override // java.util.Comparator
            public final int compare(MaterialButton materialButton, MaterialButton materialButton2) {
                MaterialButton materialButton3 = materialButton;
                MaterialButton materialButton4 = materialButton2;
                int compareTo = Boolean.valueOf(materialButton3.isChecked()).compareTo(Boolean.valueOf(materialButton4.isChecked()));
                if (compareTo != 0) {
                    return compareTo;
                }
                int compareTo2 = Boolean.valueOf(materialButton3.isPressed()).compareTo(Boolean.valueOf(materialButton4.isPressed()));
                if (compareTo2 != 0) {
                    return compareTo2;
                }
                return Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton3)).compareTo(Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton4)));
            }
        };
        this.skipCheckedStateTracker = false;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(getContext(), attributeSet, R$styleable.MaterialButtonToggleGroup, i, 2132018665, new int[0]);
        boolean z = obtainStyledAttributes.getBoolean(2, false);
        if (this.singleSelection != z) {
            this.singleSelection = z;
            this.skipCheckedStateTracker = true;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                MaterialButton childButton = getChildButton(i2);
                childButton.setChecked(false);
                dispatchOnButtonChecked(childButton.getId(), false);
            }
            this.skipCheckedStateTracker = false;
            this.checkedId = -1;
            dispatchOnButtonChecked(-1, true);
        }
        this.checkedId = obtainStyledAttributes.getResourceId(0, -1);
        this.selectionRequired = obtainStyledAttributes.getBoolean(1, false);
        setChildrenDrawingOrderEnabled(true);
        obtainStyledAttributes.recycle();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
    }

    public final void adjustChildMarginsAndUpdateLayout() {
        int i;
        int i2;
        LinearLayout.LayoutParams layoutParams;
        int childCount = getChildCount();
        int i3 = 0;
        while (true) {
            if (i3 >= childCount) {
                i3 = -1;
                break;
            } else if (isChildVisible(i3)) {
                break;
            } else {
                i3++;
            }
        }
        if (i3 != -1) {
            for (int i4 = i3 + 1; i4 < getChildCount(); i4++) {
                MaterialButton childButton = getChildButton(i4);
                MaterialButton childButton2 = getChildButton(i4 - 1);
                Objects.requireNonNull(childButton);
                if (childButton.isUsingOriginalBackground()) {
                    MaterialButtonHelper materialButtonHelper = childButton.materialButtonHelper;
                    Objects.requireNonNull(materialButtonHelper);
                    i = materialButtonHelper.strokeWidth;
                } else {
                    i = 0;
                }
                Objects.requireNonNull(childButton2);
                if (childButton2.isUsingOriginalBackground()) {
                    MaterialButtonHelper materialButtonHelper2 = childButton2.materialButtonHelper;
                    Objects.requireNonNull(materialButtonHelper2);
                    i2 = materialButtonHelper2.strokeWidth;
                } else {
                    i2 = 0;
                }
                int min = Math.min(i, i2);
                ViewGroup.LayoutParams layoutParams2 = childButton.getLayoutParams();
                if (layoutParams2 instanceof LinearLayout.LayoutParams) {
                    layoutParams = (LinearLayout.LayoutParams) layoutParams2;
                } else {
                    layoutParams = new LinearLayout.LayoutParams(layoutParams2.width, layoutParams2.height);
                }
                if (getOrientation() == 0) {
                    layoutParams.setMarginEnd(0);
                    layoutParams.setMarginStart(-min);
                    layoutParams.topMargin = 0;
                } else {
                    layoutParams.bottomMargin = 0;
                    layoutParams.topMargin = -min;
                    layoutParams.setMarginStart(0);
                }
                childButton.setLayoutParams(layoutParams);
            }
            if (!(getChildCount() == 0 || i3 == -1)) {
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) getChildButton(i3).getLayoutParams();
                if (getOrientation() == 1) {
                    layoutParams3.topMargin = 0;
                    layoutParams3.bottomMargin = 0;
                    return;
                }
                layoutParams3.setMarginEnd(0);
                layoutParams3.setMarginStart(0);
                layoutParams3.leftMargin = 0;
                layoutParams3.rightMargin = 0;
            }
        }
    }

    public final MaterialButton getChildButton(int i) {
        return (MaterialButton) getChildAt(i);
    }

    public final boolean isChildVisible(int i) {
        if (getChildAt(i).getVisibility() != 8) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        MaterialButton materialButton;
        super.onFinishInflate();
        int i = this.checkedId;
        if (i != -1 && (materialButton = (MaterialButton) findViewById(i)) != null) {
            materialButton.setChecked(true);
        }
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        int i2 = 0;
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            if ((getChildAt(i3) instanceof MaterialButton) && isChildVisible(i3)) {
                i2++;
            }
        }
        if (this.singleSelection) {
            i = 1;
        } else {
            i = 2;
        }
        accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo) AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, i2, i).mInfo);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
        super.onMeasure(i, i2);
    }

    @Override // android.view.ViewGroup
    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof MaterialButton) {
            MaterialButton materialButton = (MaterialButton) view;
            CheckedStateTracker checkedStateTracker = this.checkedStateTracker;
            Objects.requireNonNull(materialButton);
            materialButton.onCheckedChangeListeners.remove(checkedStateTracker);
            materialButton.onPressedChangeListenerInternal = null;
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= 0) {
            this.originalCornerData.remove(indexOfChild);
        }
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
    }

    public void updateChildShapes() {
        int i;
        boolean z;
        CornerData cornerData;
        int childCount = getChildCount();
        int childCount2 = getChildCount();
        int i2 = 0;
        while (true) {
            i = -1;
            if (i2 >= childCount2) {
                i2 = -1;
                break;
            } else if (isChildVisible(i2)) {
                break;
            } else {
                i2++;
            }
        }
        int childCount3 = getChildCount() - 1;
        while (true) {
            if (childCount3 < 0) {
                break;
            } else if (isChildVisible(childCount3)) {
                i = childCount3;
                break;
            } else {
                childCount3--;
            }
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            MaterialButton childButton = getChildButton(i3);
            if (childButton.getVisibility() != 8) {
                if (childButton.isUsingOriginalBackground()) {
                    MaterialButtonHelper materialButtonHelper = childButton.materialButtonHelper;
                    Objects.requireNonNull(materialButtonHelper);
                    ShapeAppearanceModel shapeAppearanceModel = materialButtonHelper.shapeAppearanceModel;
                    Objects.requireNonNull(shapeAppearanceModel);
                    ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
                    CornerData cornerData2 = (CornerData) this.originalCornerData.get(i3);
                    if (i2 != i) {
                        if (getOrientation() == 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (i3 == i2) {
                            if (!z) {
                                CornerSize cornerSize = cornerData2.topLeft;
                                AbsoluteCornerSize absoluteCornerSize = CornerData.noCorner;
                                cornerData = new CornerData(cornerSize, absoluteCornerSize, cornerData2.topRight, absoluteCornerSize);
                            } else if (ViewUtils.isLayoutRtl(this)) {
                                AbsoluteCornerSize absoluteCornerSize2 = CornerData.noCorner;
                                cornerData = new CornerData(absoluteCornerSize2, absoluteCornerSize2, cornerData2.topRight, cornerData2.bottomRight);
                            } else {
                                CornerSize cornerSize2 = cornerData2.topLeft;
                                CornerSize cornerSize3 = cornerData2.bottomLeft;
                                AbsoluteCornerSize absoluteCornerSize3 = CornerData.noCorner;
                                cornerData = new CornerData(cornerSize2, cornerSize3, absoluteCornerSize3, absoluteCornerSize3);
                            }
                        } else if (i3 != i) {
                            cornerData2 = null;
                        } else if (!z) {
                            AbsoluteCornerSize absoluteCornerSize4 = CornerData.noCorner;
                            cornerData = new CornerData(absoluteCornerSize4, cornerData2.bottomLeft, absoluteCornerSize4, cornerData2.bottomRight);
                        } else if (ViewUtils.isLayoutRtl(this)) {
                            CornerSize cornerSize4 = cornerData2.topLeft;
                            CornerSize cornerSize5 = cornerData2.bottomLeft;
                            AbsoluteCornerSize absoluteCornerSize5 = CornerData.noCorner;
                            cornerData = new CornerData(cornerSize4, cornerSize5, absoluteCornerSize5, absoluteCornerSize5);
                        } else {
                            AbsoluteCornerSize absoluteCornerSize6 = CornerData.noCorner;
                            cornerData = new CornerData(absoluteCornerSize6, absoluteCornerSize6, cornerData2.topRight, cornerData2.bottomRight);
                        }
                        cornerData2 = cornerData;
                    }
                    if (cornerData2 == null) {
                        builder.setAllCornerSizes(0.0f);
                    } else {
                        builder.topLeftCornerSize = cornerData2.topLeft;
                        builder.bottomLeftCornerSize = cornerData2.bottomLeft;
                        builder.topRightCornerSize = cornerData2.topRight;
                        builder.bottomRightCornerSize = cornerData2.bottomRight;
                    }
                    childButton.setShapeAppearanceModel(new ShapeAppearanceModel(builder));
                } else {
                    throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
                }
            }
        }
    }
}
