package com.android.keyguard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.plugins.FalsingManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardUserSwitcherPopupMenu extends ListPopupWindow {
    public Context mContext;
    public FalsingManager mFalsingManager;

    public KeyguardUserSwitcherPopupMenu(Context context, FalsingManager falsingManager) {
        super(context);
        this.mContext = context;
        this.mFalsingManager = falsingManager;
        setBackgroundDrawable(context.getResources().getDrawable(2131231619, context.getTheme()));
        setModal(true);
        setOverlapAnchor(true);
    }

    @Override // android.widget.ListPopupWindow
    public final void show() {
        super.show();
        ListView listView = getListView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setAlpha(0);
        listView.setDivider(shapeDrawable);
        listView.setDividerHeight(this.mContext.getResources().getDimensionPixelSize(2131165406));
        final int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131165407);
        listView.addHeaderView(new View(this.mContext) { // from class: com.android.keyguard.KeyguardUserSwitcherPopupMenu.1
            @Override // android.view.View
            public final void draw(Canvas canvas) {
            }

            @Override // android.view.View
            public final void onMeasure(int i, int i2) {
                setMeasuredDimension(1, dimensionPixelSize);
            }
        }, null, false);
        listView.addFooterView(new View(this.mContext) { // from class: com.android.keyguard.KeyguardUserSwitcherPopupMenu.1
            @Override // android.view.View
            public final void draw(Canvas canvas) {
            }

            @Override // android.view.View
            public final void onMeasure(int i, int i2) {
                setMeasuredDimension(1, dimensionPixelSize);
            }
        }, null, false);
        listView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardUserSwitcherPopupMenu$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                KeyguardUserSwitcherPopupMenu keyguardUserSwitcherPopupMenu = KeyguardUserSwitcherPopupMenu.this;
                Objects.requireNonNull(keyguardUserSwitcherPopupMenu);
                if (motionEvent.getActionMasked() == 0) {
                    return keyguardUserSwitcherPopupMenu.mFalsingManager.isFalseTap(1);
                }
                return false;
            }
        });
        super.show();
    }
}
