package com.android.systemui.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/* loaded from: classes.dex */
public final class PeopleSpaceTileView extends LinearLayout {
    public TextView mNameView;
    public ImageView mPersonIconView;
    public View mTileView;

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.mTileView.setOnClickListener(onClickListener);
    }

    public PeopleSpaceTileView(Context context, ViewGroup viewGroup, String str, boolean z) {
        super(context);
        View findViewWithTag = viewGroup.findViewWithTag(str);
        this.mTileView = findViewWithTag;
        if (findViewWithTag == null) {
            LayoutInflater from = LayoutInflater.from(context);
            View inflate = from.inflate(2131624358, viewGroup, false);
            this.mTileView = inflate;
            viewGroup.addView(inflate, -1, -1);
            this.mTileView.setTag(str);
            if (!z) {
                from.inflate(2131624354, viewGroup, true);
            }
        }
        this.mNameView = (TextView) this.mTileView.findViewById(2131429050);
        this.mPersonIconView = (ImageView) this.mTileView.findViewById(2131429051);
    }
}
