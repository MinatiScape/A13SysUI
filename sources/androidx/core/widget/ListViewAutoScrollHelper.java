package androidx.core.widget;

import android.widget.ListView;
/* loaded from: classes.dex */
public final class ListViewAutoScrollHelper extends AutoScrollHelper {
    public final ListView mTarget;

    public ListViewAutoScrollHelper(ListView listView) {
        super(listView);
        this.mTarget = listView;
    }
}
