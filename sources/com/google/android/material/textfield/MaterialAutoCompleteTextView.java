package com.google.android.material.textfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.ListAdapter;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.ListPopupWindow;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public class MaterialAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    public final AccessibilityManager accessibilityManager;
    public final ListPopupWindow modalListPopup;
    public final Rect tempRect;

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130968643, 0), attributeSet, 2130968643);
        this.tempRect = new Rect();
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialAutoCompleteTextView, 2130968643, 2132018316, new int[0]);
        if (obtainStyledAttributes.hasValue(0) && obtainStyledAttributes.getInt(0, 0) == 0) {
            setKeyListener(null);
        }
        this.accessibilityManager = (AccessibilityManager) context2.getSystemService("accessibility");
        ListPopupWindow listPopupWindow = new ListPopupWindow(context2);
        this.modalListPopup = listPopupWindow;
        listPopupWindow.mModal = true;
        listPopupWindow.mPopup.setFocusable(true);
        listPopupWindow.mDropDownAnchorView = this;
        listPopupWindow.mPopup.setInputMethodMode(2);
        listPopupWindow.setAdapter(getAdapter());
        listPopupWindow.mItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.google.android.material.textfield.MaterialAutoCompleteTextView.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                Object obj;
                int i3;
                View view2 = null;
                if (i2 < 0) {
                    ListPopupWindow listPopupWindow2 = MaterialAutoCompleteTextView.this.modalListPopup;
                    Objects.requireNonNull(listPopupWindow2);
                    if (!listPopupWindow2.isShowing()) {
                        obj = null;
                    } else {
                        obj = listPopupWindow2.mDropDownList.getSelectedItem();
                    }
                } else {
                    obj = MaterialAutoCompleteTextView.this.getAdapter().getItem(i2);
                }
                MaterialAutoCompleteTextView.access$100(MaterialAutoCompleteTextView.this, obj);
                AdapterView.OnItemClickListener onItemClickListener = MaterialAutoCompleteTextView.this.getOnItemClickListener();
                if (onItemClickListener != null) {
                    if (view == null || i2 < 0) {
                        ListPopupWindow listPopupWindow3 = MaterialAutoCompleteTextView.this.modalListPopup;
                        Objects.requireNonNull(listPopupWindow3);
                        if (listPopupWindow3.isShowing()) {
                            view2 = listPopupWindow3.mDropDownList.getSelectedView();
                        }
                        view = view2;
                        ListPopupWindow listPopupWindow4 = MaterialAutoCompleteTextView.this.modalListPopup;
                        Objects.requireNonNull(listPopupWindow4);
                        if (!listPopupWindow4.isShowing()) {
                            i3 = -1;
                        } else {
                            i3 = listPopupWindow4.mDropDownList.getSelectedItemPosition();
                        }
                        i2 = i3;
                        ListPopupWindow listPopupWindow5 = MaterialAutoCompleteTextView.this.modalListPopup;
                        Objects.requireNonNull(listPopupWindow5);
                        if (!listPopupWindow5.isShowing()) {
                            j = Long.MIN_VALUE;
                        } else {
                            j = listPopupWindow5.mDropDownList.getSelectedItemId();
                        }
                    }
                    ListPopupWindow listPopupWindow6 = MaterialAutoCompleteTextView.this.modalListPopup;
                    Objects.requireNonNull(listPopupWindow6);
                    onItemClickListener.onItemClick(listPopupWindow6.mDropDownList, view, i2, j);
                }
                MaterialAutoCompleteTextView.this.modalListPopup.dismiss();
            }
        };
        obtainStyledAttributes.recycle();
    }

    @Override // android.widget.AutoCompleteTextView
    public final void showDropDown() {
        AccessibilityManager accessibilityManager = this.accessibilityManager;
        if (accessibilityManager == null || !accessibilityManager.isTouchExplorationEnabled()) {
            super.showDropDown();
        } else {
            this.modalListPopup.show();
        }
    }

    public static void access$100(MaterialAutoCompleteTextView materialAutoCompleteTextView, Object obj) {
        Objects.requireNonNull(materialAutoCompleteTextView);
        materialAutoCompleteTextView.setText(materialAutoCompleteTextView.convertSelectionToString(obj), false);
    }

    public final TextInputLayout findTextInputLayoutAncestor() {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
        }
        return null;
    }

    @Override // android.widget.TextView
    public final CharSequence getHint() {
        TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
        if (findTextInputLayoutAncestor == null || !findTextInputLayoutAncestor.isProvidingHint) {
            return super.getHint();
        }
        return findTextInputLayoutAncestor.getHint();
    }

    @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
        if (findTextInputLayoutAncestor != null && findTextInputLayoutAncestor.isProvidingHint && super.getHint() == null && Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("meizu")) {
            setHint("");
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            int measuredWidth = getMeasuredWidth();
            ListAdapter adapter = getAdapter();
            TextInputLayout findTextInputLayoutAncestor = findTextInputLayoutAncestor();
            int i4 = 0;
            if (!(adapter == null || findTextInputLayoutAncestor == null)) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
                ListPopupWindow listPopupWindow = this.modalListPopup;
                Objects.requireNonNull(listPopupWindow);
                if (!listPopupWindow.isShowing()) {
                    i3 = -1;
                } else {
                    i3 = listPopupWindow.mDropDownList.getSelectedItemPosition();
                }
                int min = Math.min(adapter.getCount(), Math.max(0, i3) + 15);
                View view = null;
                int i5 = 0;
                for (int max = Math.max(0, min - 15); max < min; max++) {
                    int itemViewType = adapter.getItemViewType(max);
                    if (itemViewType != i4) {
                        view = null;
                        i4 = itemViewType;
                    }
                    view = adapter.getView(max, view, findTextInputLayoutAncestor);
                    if (view.getLayoutParams() == null) {
                        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                    }
                    view.measure(makeMeasureSpec, makeMeasureSpec2);
                    i5 = Math.max(i5, view.getMeasuredWidth());
                }
                Drawable background = this.modalListPopup.getBackground();
                if (background != null) {
                    background.getPadding(this.tempRect);
                    Rect rect = this.tempRect;
                    i5 += rect.left + rect.right;
                }
                i4 = findTextInputLayoutAncestor.endIconView.getMeasuredWidth() + i5;
            }
            setMeasuredDimension(Math.min(Math.max(measuredWidth, i4), View.MeasureSpec.getSize(i)), getMeasuredHeight());
        }
    }

    @Override // android.widget.AutoCompleteTextView
    public final <T extends ListAdapter & Filterable> void setAdapter(T t) {
        super.setAdapter(t);
        this.modalListPopup.setAdapter(getAdapter());
    }
}
