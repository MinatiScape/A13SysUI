package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
/* loaded from: classes.dex */
public class TitleView extends FrameLayout {
    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968721);
    }

    public TitleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        View inflate = LayoutInflater.from(context).inflate(2131624232, this);
        ImageView imageView = (ImageView) inflate.findViewById(2131429059);
        TextView textView = (TextView) inflate.findViewById(2131429064);
        SearchOrbView searchOrbView = (SearchOrbView) inflate.findViewById(2131429062);
        setClipToPadding(false);
        setClipChildren(false);
    }
}
