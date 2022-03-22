package com.android.systemui.globalactions;

import android.content.Context;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
/* loaded from: classes.dex */
public final class GlobalActionsPopupMenu extends ListPopupWindow {
    public ListAdapter mAdapter;
    public Context mContext;
    public int mGlobalActionsSidePadding;
    public boolean mIsDropDownMode;
    public int mMaximumWidthThresholdDp = 800;
    public int mMenuVerticalPadding;
    public AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    @Override // android.widget.ListPopupWindow
    public final void setAdapter(ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        super.setAdapter(listAdapter);
    }

    public GlobalActionsPopupMenu(ContextThemeWrapper contextThemeWrapper, boolean z) {
        super(contextThemeWrapper);
        this.mMenuVerticalPadding = 0;
        this.mGlobalActionsSidePadding = 0;
        this.mContext = contextThemeWrapper;
        Resources resources = contextThemeWrapper.getResources();
        setBackgroundDrawable(resources.getDrawable(2131231723, contextThemeWrapper.getTheme()));
        this.mIsDropDownMode = z;
        setInputMethodMode(2);
        setModal(true);
        this.mGlobalActionsSidePadding = resources.getDimensionPixelSize(2131165778);
        if (!z) {
            this.mMenuVerticalPadding = resources.getDimensionPixelSize(2131165541);
        }
    }

    @Override // android.widget.ListPopupWindow
    public final void show() {
        super.show();
        if (this.mOnItemLongClickListener != null) {
            getListView().setOnItemLongClickListener(this.mOnItemLongClickListener);
        }
        ListView listView = getListView();
        Resources resources = this.mContext.getResources();
        setVerticalOffset((-getAnchorView().getHeight()) / 2);
        if (this.mIsDropDownMode) {
            listView.setDividerHeight(resources.getDimensionPixelSize(2131165537));
            listView.setDivider(resources.getDrawable(2131231673));
        } else if (this.mAdapter != null) {
            int i = Resources.getSystem().getDisplayMetrics().widthPixels;
            float f = i / Resources.getSystem().getDisplayMetrics().density;
            double d = i;
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (0.9d * d), Integer.MIN_VALUE);
            int i2 = 0;
            for (int i3 = 0; i3 < this.mAdapter.getCount(); i3++) {
                View view = this.mAdapter.getView(i3, null, listView);
                view.measure(makeMeasureSpec, 0);
                i2 = Math.max(view.getMeasuredWidth(), i2);
            }
            if (f < this.mMaximumWidthThresholdDp) {
                i2 = Math.max(i2, (int) (d * 0.5d));
            }
            int i4 = this.mMenuVerticalPadding;
            listView.setPadding(0, i4, 0, i4);
            setWidth(i2);
            if (getAnchorView().getLayoutDirection() == 0) {
                setHorizontalOffset((getAnchorView().getWidth() - this.mGlobalActionsSidePadding) - i2);
            } else {
                setHorizontalOffset(this.mGlobalActionsSidePadding);
            }
        } else {
            return;
        }
        super.show();
    }
}
