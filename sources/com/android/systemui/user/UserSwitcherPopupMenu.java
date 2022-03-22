package com.android.systemui.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.plugins.FalsingManager;
/* compiled from: UserSwitcherPopupMenu.kt */
/* loaded from: classes.dex */
public final class UserSwitcherPopupMenu extends ListPopupWindow {
    public ListAdapter adapter;
    public final Context context;
    public final FalsingManager falsingManager;
    public final Resources res;

    public UserSwitcherPopupMenu(UserSwitcherActivity userSwitcherActivity, FalsingManager falsingManager) {
        super(userSwitcherActivity);
        this.context = userSwitcherActivity;
        this.falsingManager = falsingManager;
        Resources resources = userSwitcherActivity.getResources();
        this.res = resources;
        setBackgroundDrawable(resources.getDrawable(2131231619, userSwitcherActivity.getTheme()));
        setModal(false);
        setOverlapAnchor(true);
    }

    @Override // android.widget.ListPopupWindow
    public final void setAdapter(ListAdapter listAdapter) {
        super.setAdapter(listAdapter);
        this.adapter = listAdapter;
    }

    @Override // android.widget.ListPopupWindow
    public final void show() {
        super.show();
        ListView listView = getListView();
        int i = 0;
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setAlpha(0);
        listView.setDivider(shapeDrawable);
        listView.setDividerHeight(this.res.getDimensionPixelSize(2131165406));
        final int dimensionPixelSize = this.res.getDimensionPixelSize(2131165407);
        final Context context = this.context;
        listView.addHeaderView(new View(context) { // from class: com.android.systemui.user.UserSwitcherPopupMenu$createSpacer$1
            @Override // android.view.View
            public final void draw(Canvas canvas) {
            }

            @Override // android.view.View
            public final void onMeasure(int i2, int i3) {
                setMeasuredDimension(1, dimensionPixelSize);
            }
        }, null, false);
        final Context context2 = this.context;
        listView.addFooterView(new View(context2) { // from class: com.android.systemui.user.UserSwitcherPopupMenu$createSpacer$1
            @Override // android.view.View
            public final void draw(Canvas canvas) {
            }

            @Override // android.view.View
            public final void onMeasure(int i2, int i3) {
                setMeasuredDimension(1, dimensionPixelSize);
            }
        }, null, false);
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (this.res.getDisplayMetrics().widthPixels * 0.25d), Integer.MIN_VALUE);
            int count = listAdapter.getCount();
            int i2 = 0;
            int i3 = 0;
            while (i2 < count) {
                int i4 = i2 + 1;
                View view = listAdapter.getView(i2, null, listView);
                view.measure(makeMeasureSpec, 0);
                i3 = Math.max(view.getMeasuredWidth(), i3);
                i2 = i4;
            }
            i = i3;
        }
        setWidth(i);
        super.show();
    }
}
