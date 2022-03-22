package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ListRowView extends LinearLayout {
    public ListRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        LayoutInflater.from(context).inflate(2131624205, this);
        HorizontalGridView horizontalGridView = (HorizontalGridView) findViewById(2131428721);
        Objects.requireNonNull(horizontalGridView);
        horizontalGridView.mHasFixedSize = false;
        setOrientation(1);
        setDescendantFocusability(262144);
    }
}
