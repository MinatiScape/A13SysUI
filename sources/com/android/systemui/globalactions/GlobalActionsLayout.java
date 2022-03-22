package com.android.systemui.globalactions;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.preference.R$id;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.HardwareBgDrawable;
import com.android.systemui.MultiListLayout;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class GlobalActionsLayout extends MultiListLayout {
    public boolean mBackgroundsSet;

    public abstract boolean shouldReverseListItems();

    public void addToListView(View view, boolean z) {
        if (z) {
            getListView().addView(view, 0);
        } else {
            getListView().addView(view);
        }
    }

    public HardwareBgDrawable getBackgroundDrawable(int i) {
        HardwareBgDrawable hardwareBgDrawable = new HardwareBgDrawable(getContext());
        hardwareBgDrawable.setTint(i);
        return hardwareBgDrawable;
    }

    @VisibleForTesting
    public int getCurrentRotation() {
        return R$id.getRotation(((LinearLayout) this).mContext);
    }

    @VisibleForTesting
    public int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }

    @Override // com.android.systemui.MultiListLayout
    public ViewGroup getListView() {
        return (ViewGroup) findViewById(16908298);
    }

    @Override // com.android.systemui.MultiListLayout
    public final ViewGroup getSeparatedView() {
        return (ViewGroup) findViewById(2131428835);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        HardwareBgDrawable backgroundDrawable;
        super.onMeasure(i, i2);
        if (getListView() != null && !this.mBackgroundsSet) {
            ViewGroup listView = getListView();
            HardwareBgDrawable backgroundDrawable2 = getBackgroundDrawable(getResources().getColor(2131099872, null));
            if (backgroundDrawable2 != null) {
                listView.setBackground(backgroundDrawable2);
            }
            if (!(getSeparatedView() == null || (backgroundDrawable = getBackgroundDrawable(getResources().getColor(2131099878, null))) == null)) {
                getSeparatedView().setBackground(backgroundDrawable);
            }
            this.mBackgroundsSet = true;
        }
    }

    @Override // com.android.systemui.MultiListLayout
    public void onUpdateList() {
        super.onUpdateList();
        getSeparatedView();
        ViewGroup listView = getListView();
        for (int i = 0; i < ((GlobalActionsDialogLite.MyAdapter) this.mAdapter).getCount(); i++) {
            GlobalActionsDialogLite.MyAdapter myAdapter = (GlobalActionsDialogLite.MyAdapter) this.mAdapter;
            Objects.requireNonNull(myAdapter);
            myAdapter.getItem(i).shouldBeSeparated();
            addToListView(((GlobalActionsDialogLite.MyAdapter) this.mAdapter).getView(i, null, listView), shouldReverseListItems());
        }
    }

    public GlobalActionsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
