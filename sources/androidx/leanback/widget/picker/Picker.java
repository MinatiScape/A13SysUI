package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.GridLayoutManager;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.VerticalGridView;
import androidx.leanback.widget.WindowAlignment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class Picker extends FrameLayout {
    public int mAlphaAnimDuration;
    public final AnonymousClass1 mColumnChangeListener;
    public final ArrayList mColumnViews;
    public ArrayList<PickerColumn> mColumns;
    public DecelerateInterpolator mDecelerateInterpolator;
    public float mFocusedAlpha;
    public int mPickerItemLayoutId;
    public int mPickerItemTextViewId;
    public ViewGroup mPickerView;
    public int mSelectedColumn;
    public ArrayList mSeparators;
    public float mUnfocusedAlpha;
    public float mVisibleColumnAlpha;
    public float mVisibleItemsActivated;

    /* loaded from: classes.dex */
    public class PickerScrollArrayAdapter extends RecyclerView.Adapter<ViewHolder> {
        public final int mColIndex;
        public PickerColumn mData;
        public final int mResource;
        public final int mTextViewResourceId;

        public PickerScrollArrayAdapter(int i, int i2, int i3) {
            this.mResource = i;
            this.mColIndex = i3;
            this.mTextViewResourceId = i2;
            this.mData = Picker.this.mColumns.get(i3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            PickerColumn pickerColumn = this.mData;
            if (pickerColumn == null) {
                return 0;
            }
            Objects.requireNonNull(pickerColumn);
            return (pickerColumn.mMaxValue - pickerColumn.mMinValue) + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(ViewHolder viewHolder, int i) {
            PickerColumn pickerColumn;
            CharSequence charSequence;
            ViewHolder viewHolder2 = viewHolder;
            TextView textView = viewHolder2.textView;
            boolean z = true;
            if (!(textView == null || (pickerColumn = this.mData) == null)) {
                int i2 = pickerColumn.mMinValue + i;
                CharSequence[] charSequenceArr = pickerColumn.mStaticLabels;
                if (charSequenceArr == null) {
                    charSequence = String.format(pickerColumn.mLabelFormat, Integer.valueOf(i2));
                } else {
                    charSequence = charSequenceArr[i2];
                }
                textView.setText(charSequence);
            }
            Picker picker = Picker.this;
            View view = viewHolder2.itemView;
            VerticalGridView verticalGridView = (VerticalGridView) picker.mColumnViews.get(this.mColIndex);
            Objects.requireNonNull(verticalGridView);
            GridLayoutManager gridLayoutManager = verticalGridView.mLayoutManager;
            Objects.requireNonNull(gridLayoutManager);
            if (gridLayoutManager.mFocusPosition != i) {
                z = false;
            }
            picker.setOrAnimateAlpha(view, z, this.mColIndex, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onViewAttachedToWindow(ViewHolder viewHolder) {
            viewHolder.itemView.setFocusable(Picker.this.isActivated());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            TextView textView;
            View inflate = LayoutInflater.from(recyclerView.getContext()).inflate(this.mResource, (ViewGroup) recyclerView, false);
            int i2 = this.mTextViewResourceId;
            if (i2 != 0) {
                textView = (TextView) inflate.findViewById(i2);
            } else {
                textView = (TextView) inflate;
            }
            return new ViewHolder(inflate, textView);
        }
    }

    public Picker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969579);
    }

    public final void setOrAnimateAlpha(View view, boolean z, int i, boolean z2) {
        boolean z3 = i == this.mSelectedColumn || !hasFocus();
        if (z) {
            if (z3) {
                setOrAnimateAlpha(view, z2, this.mFocusedAlpha, this.mDecelerateInterpolator);
            } else {
                setOrAnimateAlpha(view, z2, this.mUnfocusedAlpha, this.mDecelerateInterpolator);
            }
        } else if (z3) {
            setOrAnimateAlpha(view, z2, this.mVisibleColumnAlpha, this.mDecelerateInterpolator);
        } else {
            setOrAnimateAlpha(view, z2, 0.0f, this.mDecelerateInterpolator);
        }
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View view, TextView textView) {
            super(view);
            this.textView = textView;
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [androidx.leanback.widget.picker.Picker$1] */
    @SuppressLint({"CustomViewStyleable"})
    public Picker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColumnViews = new ArrayList();
        this.mVisibleItemsActivated = 3.0f;
        this.mSelectedColumn = 0;
        this.mSeparators = new ArrayList();
        this.mColumnChangeListener = new OnChildViewHolderSelectedListener() { // from class: androidx.leanback.widget.picker.Picker.1
            @Override // androidx.leanback.widget.OnChildViewHolderSelectedListener
            public final void onChildViewHolderSelected(BaseGridView baseGridView, RecyclerView.ViewHolder viewHolder, int i2) {
                int indexOf = Picker.this.mColumnViews.indexOf((VerticalGridView) baseGridView);
                Picker.this.updateColumnAlpha(indexOf);
                if (viewHolder != null) {
                    PickerColumn pickerColumn = Picker.this.mColumns.get(indexOf);
                    Objects.requireNonNull(pickerColumn);
                    Picker.this.onColumnValueChanged(indexOf, pickerColumn.mMinValue + i2);
                }
            }
        };
        int[] iArr = R$styleable.lbPicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, 0);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, 0);
        this.mPickerItemLayoutId = obtainStyledAttributes.getResourceId(0, 2131624212);
        this.mPickerItemTextViewId = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        setEnabled(true);
        setDescendantFocusability(262144);
        this.mFocusedAlpha = 1.0f;
        this.mUnfocusedAlpha = 1.0f;
        this.mVisibleColumnAlpha = 0.5f;
        this.mAlphaAnimDuration = 200;
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.5f);
        this.mPickerView = (ViewGroup) ((ViewGroup) LayoutInflater.from(getContext()).inflate(2131624210, (ViewGroup) this, true)).findViewById(2131428587);
    }

    public final int getColumnsCount() {
        ArrayList<PickerColumn> arrayList = this.mColumns;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public void onColumnValueChanged(int i, int i2) {
        PickerColumn pickerColumn = this.mColumns.get(i);
        Objects.requireNonNull(pickerColumn);
        if (pickerColumn.mCurrentValue != i2) {
            pickerColumn.mCurrentValue = i2;
        }
    }

    @Override // android.view.ViewGroup
    public final boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2 = this.mSelectedColumn;
        if (i2 < 0 || i2 >= this.mColumnViews.size()) {
            return false;
        }
        return ((VerticalGridView) this.mColumnViews.get(i2)).requestFocus(i, rect);
    }

    public final void setColumnAt(int i, PickerColumn pickerColumn) {
        this.mColumns.set(i, pickerColumn);
        VerticalGridView verticalGridView = (VerticalGridView) this.mColumnViews.get(i);
        Objects.requireNonNull(verticalGridView);
        PickerScrollArrayAdapter pickerScrollArrayAdapter = (PickerScrollArrayAdapter) verticalGridView.mAdapter;
        if (pickerScrollArrayAdapter != null) {
            pickerScrollArrayAdapter.notifyDataSetChanged();
        }
        Objects.requireNonNull(pickerColumn);
        GridLayoutManager gridLayoutManager = verticalGridView.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        gridLayoutManager.setSelection(pickerColumn.mCurrentValue - pickerColumn.mMinValue, false);
    }

    public final void setColumnValue(int i, int i2, boolean z) {
        PickerColumn pickerColumn = this.mColumns.get(i);
        Objects.requireNonNull(pickerColumn);
        if (pickerColumn.mCurrentValue != i2) {
            pickerColumn.mCurrentValue = i2;
            VerticalGridView verticalGridView = (VerticalGridView) this.mColumnViews.get(i);
            if (verticalGridView != null) {
                PickerColumn pickerColumn2 = this.mColumns.get(i);
                Objects.requireNonNull(pickerColumn2);
                int i3 = i2 - pickerColumn2.mMinValue;
                if (z) {
                    GridLayoutManager gridLayoutManager = verticalGridView.mLayoutManager;
                    Objects.requireNonNull(gridLayoutManager);
                    gridLayoutManager.setSelection(i3, true);
                    return;
                }
                GridLayoutManager gridLayoutManager2 = verticalGridView.mLayoutManager;
                Objects.requireNonNull(gridLayoutManager2);
                gridLayoutManager2.setSelection(i3, false);
            }
        }
    }

    public final void setColumns(ArrayList arrayList) {
        if (this.mSeparators.size() != 0) {
            if (this.mSeparators.size() == 1) {
                CharSequence charSequence = (CharSequence) this.mSeparators.get(0);
                this.mSeparators.clear();
                this.mSeparators.add("");
                for (int i = 0; i < arrayList.size() - 1; i++) {
                    this.mSeparators.add(charSequence);
                }
                this.mSeparators.add("");
            } else if (this.mSeparators.size() != arrayList.size() + 1) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Separators size: ");
                m.append(this.mSeparators.size());
                m.append(" mustequal the size of columns: ");
                m.append(arrayList.size());
                m.append(" + 1");
                throw new IllegalStateException(m.toString());
            }
            this.mColumnViews.clear();
            this.mPickerView.removeAllViews();
            ArrayList<PickerColumn> arrayList2 = new ArrayList<>(arrayList);
            this.mColumns = arrayList2;
            if (this.mSelectedColumn > arrayList2.size() - 1) {
                this.mSelectedColumn = this.mColumns.size() - 1;
            }
            LayoutInflater from = LayoutInflater.from(getContext());
            int columnsCount = getColumnsCount();
            if (!TextUtils.isEmpty((CharSequence) this.mSeparators.get(0))) {
                TextView textView = (TextView) from.inflate(2131624213, this.mPickerView, false);
                textView.setText((CharSequence) this.mSeparators.get(0));
                this.mPickerView.addView(textView);
            }
            int i2 = 0;
            while (i2 < columnsCount) {
                VerticalGridView verticalGridView = (VerticalGridView) from.inflate(2131624211, this.mPickerView, false);
                updateColumnSize(verticalGridView);
                GridLayoutManager gridLayoutManager = verticalGridView.mLayoutManager;
                Objects.requireNonNull(gridLayoutManager);
                WindowAlignment windowAlignment = gridLayoutManager.mWindowAlignment;
                Objects.requireNonNull(windowAlignment);
                WindowAlignment.Axis axis = windowAlignment.mMainAxis;
                Objects.requireNonNull(axis);
                axis.mWindowAlignment = 0;
                verticalGridView.requestLayout();
                verticalGridView.mHasFixedSize = false;
                verticalGridView.setFocusable(isActivated());
                RecyclerView.Recycler recycler = verticalGridView.mRecycler;
                Objects.requireNonNull(recycler);
                recycler.mRequestedCacheMax = 0;
                recycler.updateViewCacheSize();
                this.mColumnViews.add(verticalGridView);
                this.mPickerView.addView(verticalGridView);
                int i3 = i2 + 1;
                if (!TextUtils.isEmpty((CharSequence) this.mSeparators.get(i3))) {
                    TextView textView2 = (TextView) from.inflate(2131624213, this.mPickerView, false);
                    textView2.setText((CharSequence) this.mSeparators.get(i3));
                    this.mPickerView.addView(textView2);
                }
                verticalGridView.setAdapter(new PickerScrollArrayAdapter(this.mPickerItemLayoutId, this.mPickerItemTextViewId, i2));
                AnonymousClass1 r2 = this.mColumnChangeListener;
                GridLayoutManager gridLayoutManager2 = verticalGridView.mLayoutManager;
                Objects.requireNonNull(gridLayoutManager2);
                if (r2 == null) {
                    gridLayoutManager2.mChildViewHolderSelectedListeners = null;
                } else {
                    ArrayList<OnChildViewHolderSelectedListener> arrayList3 = gridLayoutManager2.mChildViewHolderSelectedListeners;
                    if (arrayList3 == null) {
                        gridLayoutManager2.mChildViewHolderSelectedListeners = new ArrayList<>();
                    } else {
                        arrayList3.clear();
                    }
                    gridLayoutManager2.mChildViewHolderSelectedListeners.add(r2);
                }
                i2 = i3;
            }
            return;
        }
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Separators size is: ");
        m2.append(this.mSeparators.size());
        m2.append(". At least one separator must be provided");
        throw new IllegalStateException(m2.toString());
    }

    public final void setSelectedColumn(int i) {
        if (this.mSelectedColumn != i) {
            this.mSelectedColumn = i;
            for (int i2 = 0; i2 < this.mColumnViews.size(); i2++) {
                updateColumnAlpha(i2);
            }
        }
        VerticalGridView verticalGridView = (VerticalGridView) this.mColumnViews.get(i);
        if (hasFocus() && !verticalGridView.hasFocus()) {
            verticalGridView.requestFocus();
        }
    }

    public final void updateColumnAlpha(int i) {
        boolean z;
        VerticalGridView verticalGridView = (VerticalGridView) this.mColumnViews.get(i);
        Objects.requireNonNull(verticalGridView);
        GridLayoutManager gridLayoutManager = verticalGridView.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        int i2 = gridLayoutManager.mFocusPosition;
        for (int i3 = 0; i3 < verticalGridView.mAdapter.getItemCount(); i3++) {
            View findViewByPosition = verticalGridView.mLayout.findViewByPosition(i3);
            if (findViewByPosition != null) {
                if (i2 == i3) {
                    z = true;
                } else {
                    z = false;
                }
                setOrAnimateAlpha(findViewByPosition, z, i, true);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!isActivated()) {
            return super.dispatchKeyEvent(keyEvent);
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != 23 && keyCode != 66) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (keyEvent.getAction() == 1) {
            performClick();
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        for (int i = 0; i < this.mColumnViews.size(); i++) {
            if (((VerticalGridView) this.mColumnViews.get(i)).hasFocus()) {
                setSelectedColumn(i);
            }
        }
    }

    @Override // android.view.View
    public final void setActivated(boolean z) {
        if (z == isActivated()) {
            super.setActivated(z);
            return;
        }
        super.setActivated(z);
        boolean hasFocus = hasFocus();
        int i = this.mSelectedColumn;
        setDescendantFocusability(131072);
        if (!z && hasFocus && isFocusable()) {
            requestFocus();
        }
        for (int i2 = 0; i2 < getColumnsCount(); i2++) {
            ((VerticalGridView) this.mColumnViews.get(i2)).setFocusable(z);
        }
        for (int i3 = 0; i3 < getColumnsCount(); i3++) {
            updateColumnSize((VerticalGridView) this.mColumnViews.get(i3));
        }
        boolean isActivated = isActivated();
        for (int i4 = 0; i4 < getColumnsCount(); i4++) {
            VerticalGridView verticalGridView = (VerticalGridView) this.mColumnViews.get(i4);
            for (int i5 = 0; i5 < verticalGridView.getChildCount(); i5++) {
                verticalGridView.getChildAt(i5).setFocusable(isActivated);
            }
        }
        if (z && hasFocus && i >= 0) {
            ((VerticalGridView) this.mColumnViews.get(i)).requestFocus();
        }
        setDescendantFocusability(262144);
    }

    public final void updateColumnSize(VerticalGridView verticalGridView) {
        float f;
        GridLayoutManager gridLayoutManager;
        ViewGroup.LayoutParams layoutParams = verticalGridView.getLayoutParams();
        if (isActivated()) {
            f = this.mVisibleItemsActivated;
        } else {
            f = 1.0f;
        }
        Objects.requireNonNull(verticalGridView.mLayoutManager);
        layoutParams.height = (int) MotionController$$ExternalSyntheticOutline0.m(f, 1.0f, gridLayoutManager.mVerticalSpacing, getContext().getResources().getDimensionPixelSize(2131166771) * f);
        verticalGridView.setLayoutParams(layoutParams);
    }

    public final void setOrAnimateAlpha(View view, boolean z, float f, DecelerateInterpolator decelerateInterpolator) {
        view.animate().cancel();
        if (!z) {
            view.setAlpha(f);
        } else {
            view.animate().alpha(f).setDuration(this.mAlphaAnimDuration).setInterpolator(decelerateInterpolator).start();
        }
    }
}
