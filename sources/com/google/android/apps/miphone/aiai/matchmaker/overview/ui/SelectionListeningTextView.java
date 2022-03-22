package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
class SelectionListeningTextView extends TextView {
    public SelectionListeningTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0, 0);
        setTextIsSelectable(true);
    }

    @Override // android.widget.TextView
    public final void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
    }
}
