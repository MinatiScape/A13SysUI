package androidx.slice.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.widget.SliceViewPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class TemplateView extends SliceChildView implements SliceViewPolicy.PolicyChangeListener {
    public SliceAdapter mAdapter;
    public final View mForeground;
    public ListContent mListContent;
    public SliceView mParent;
    public final RecyclerView mRecyclerView;
    public List<SliceContent> mDisplayedItems = new ArrayList();
    public int mDisplayedItemsHeight = 0;
    public int[] mLoc = new int[2];

    public final void resetView() {
        this.mDisplayedItemsHeight = 0;
        this.mDisplayedItems.clear();
        this.mAdapter.setSliceItems(null);
        this.mListContent = null;
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setAllowTwoLines(boolean z) {
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mAllowTwoLines = z;
        sliceAdapter.notifyHeaderChanged();
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setLastUpdated(long j) {
        this.mLastUpdated = j;
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        if (sliceAdapter.mLastUpdated != j) {
            sliceAdapter.mLastUpdated = j;
            sliceAdapter.notifyHeaderChanged();
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setLoadingActions(Set<SliceItem> set) {
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        if (set == null) {
            sliceAdapter.mLoadingActions.clear();
        } else {
            sliceAdapter.mLoadingActions = set;
        }
        sliceAdapter.notifyDataSetChanged();
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setPolicy(SliceViewPolicy sliceViewPolicy) {
        this.mViewPolicy = sliceViewPolicy;
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mPolicy = sliceViewPolicy;
        Objects.requireNonNull(sliceViewPolicy);
        sliceViewPolicy.mListener = this;
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setShowLastUpdated(boolean z) {
        this.mShowLastUpdated = z;
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        if (sliceAdapter.mShowLastUpdated != z) {
            sliceAdapter.mShowLastUpdated = z;
            sliceAdapter.notifyHeaderChanged();
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setSliceActions(List<SliceAction> list) {
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mSliceActions = list;
        sliceAdapter.notifyHeaderChanged();
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setStyle(SliceStyle sliceStyle, RowStyle rowStyle) {
        this.mSliceStyle = sliceStyle;
        this.mRowStyle = rowStyle;
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mSliceStyle = sliceStyle;
        sliceAdapter.notifyDataSetChanged();
        if (rowStyle.mDisableRecyclerViewItemAnimator) {
            RecyclerView recyclerView = this.mRecyclerView;
            Objects.requireNonNull(recyclerView);
            RecyclerView.ItemAnimator itemAnimator = recyclerView.mItemAnimator;
            if (itemAnimator != null) {
                itemAnimator.endAnimations();
                RecyclerView.ItemAnimator itemAnimator2 = recyclerView.mItemAnimator;
                Objects.requireNonNull(itemAnimator2);
                itemAnimator2.mListener = null;
            }
            recyclerView.mItemAnimator = null;
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setTint(int i) {
        this.mTintColor = i;
        getMeasuredHeight();
        updateDisplayedItems();
    }

    public final void updateDisplayedItems() {
        boolean z;
        ListContent listContent = this.mListContent;
        if (listContent == null || !listContent.isValid()) {
            resetView();
            return;
        }
        ListContent listContent2 = this.mListContent;
        SliceStyle sliceStyle = this.mSliceStyle;
        SliceViewPolicy sliceViewPolicy = this.mViewPolicy;
        Objects.requireNonNull(listContent2);
        Objects.requireNonNull(sliceViewPolicy);
        Objects.requireNonNull(sliceStyle);
        ArrayList<SliceContent> arrayList = listContent2.mRowItems;
        int size = arrayList.size();
        int i = 1;
        List<SliceContent> list = arrayList;
        if (size > 0) {
            boolean shouldSkipFirstListItem = sliceStyle.shouldSkipFirstListItem(arrayList);
            list = arrayList;
            if (shouldSkipFirstListItem) {
                list = arrayList.subList(1, arrayList.size());
            }
        }
        this.mDisplayedItems = list;
        this.mDisplayedItemsHeight = this.mSliceStyle.getListItemsHeight(list, this.mViewPolicy);
        SliceAdapter sliceAdapter = this.mAdapter;
        List<SliceContent> list2 = this.mDisplayedItems;
        Objects.requireNonNull(this.mViewPolicy);
        sliceAdapter.setSliceItems(list2);
        if (this.mDisplayedItemsHeight > getMeasuredHeight()) {
            z = true;
        } else {
            z = false;
        }
        RecyclerView recyclerView = this.mRecyclerView;
        Objects.requireNonNull(this.mViewPolicy);
        if (!z) {
            i = 2;
        }
        recyclerView.setOverScrollMode(i);
    }

    public TemplateView(Context context) {
        super(context);
        RecyclerView recyclerView = new RecyclerView(getContext());
        this.mRecyclerView = recyclerView;
        getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(1));
        SliceAdapter sliceAdapter = new SliceAdapter(context);
        this.mAdapter = sliceAdapter;
        recyclerView.setAdapter(sliceAdapter);
        SliceAdapter sliceAdapter2 = new SliceAdapter(context);
        this.mAdapter = sliceAdapter2;
        recyclerView.setAdapter(sliceAdapter2);
        addView(recyclerView);
        View view = new View(getContext());
        this.mForeground = view;
        view.setBackground(SliceViewUtil.getDrawable(getContext(), 16843534));
        addView(view);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        view.setLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        SliceView sliceView = (SliceView) getParent();
        this.mParent = sliceView;
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mParent = sliceView;
        sliceAdapter.mTemplateView = this;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        View.MeasureSpec.getSize(i2);
        Objects.requireNonNull(this.mViewPolicy);
        super.onMeasure(i, i2);
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setInsets(int i, int i2, int i3, int i4) {
        super.setInsets(i, i2, i3, i4);
        SliceAdapter sliceAdapter = this.mAdapter;
        Objects.requireNonNull(sliceAdapter);
        sliceAdapter.mInsetStart = i;
        sliceAdapter.mInsetTop = i2;
        sliceAdapter.mInsetEnd = i3;
        sliceAdapter.mInsetBottom = i4;
    }
}
