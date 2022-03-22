package com.android.systemui.screenrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
/* loaded from: classes.dex */
public final class ScreenRecordingAdapter extends ArrayAdapter<ScreenRecordingAudioSource> {
    public LinearLayout mMicAndInternalOption;
    public LinearLayout mMicOption;
    public LinearLayout mSelectedInternal = getSelected(2131953207);
    public LinearLayout mSelectedMic = getSelected(2131953208);
    public LinearLayout mSelectedMicAndInternal = getSelected(2131953205);
    public LinearLayout mInternalOption = getOption(2131953207, 2131953206);

    public ScreenRecordingAdapter(Context context, List list) {
        super(context, 17367049, list);
        LinearLayout option = getOption(2131953208, 0);
        this.mMicOption = option;
        option.removeViewAt(1);
        LinearLayout option2 = getOption(2131953205, 0);
        this.mMicAndInternalOption = option2;
        option2.removeViewAt(1);
    }

    @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public final View getDropDownView(int i, View view, ViewGroup viewGroup) {
        int ordinal = getItem(i).ordinal();
        if (ordinal == 1) {
            return this.mInternalOption;
        }
        if (ordinal == 2) {
            return this.mMicOption;
        }
        if (ordinal != 3) {
            return super.getDropDownView(i, view, viewGroup);
        }
        return this.mMicAndInternalOption;
    }

    public final LinearLayout getOption(int i, int i2) {
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(2131624461, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(2131428761)).setText(i);
        if (i2 != 0) {
            ((TextView) linearLayout.findViewById(2131428760)).setText(i2);
        }
        return linearLayout;
    }

    public final LinearLayout getSelected(int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(2131624462, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(2131428761)).setText(i);
        return linearLayout;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        int ordinal = getItem(i).ordinal();
        if (ordinal == 1) {
            return this.mSelectedInternal;
        }
        if (ordinal == 2) {
            return this.mSelectedMic;
        }
        if (ordinal != 3) {
            return super.getView(i, view, viewGroup);
        }
        return this.mSelectedMicAndInternal;
    }
}
