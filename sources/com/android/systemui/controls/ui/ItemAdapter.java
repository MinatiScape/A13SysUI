package com.android.systemui.controls.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ItemAdapter extends ArrayAdapter<SelectionItem> {
    public final int resource = 2131624058;
    public final LayoutInflater layoutInflater = LayoutInflater.from(getContext());

    public ItemAdapter(Context context) {
        super(context, 2131624058);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        SelectionItem item = getItem(i);
        if (view == null) {
            view = this.layoutInflater.inflate(this.resource, viewGroup, false);
        }
        ((TextView) view.requireViewById(2131427771)).setText(item.getTitle());
        ((ImageView) view.requireViewById(2131427500)).setImageDrawable(item.icon);
        return view;
    }
}
