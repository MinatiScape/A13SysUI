package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.qs.FgsManagerController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AdapterListUpdateCallback implements ListUpdateCallback {
    public final RecyclerView.Adapter mAdapter;

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public final void onChanged(int i, int i2, Object obj) {
        RecyclerView.Adapter adapter = this.mAdapter;
        Objects.requireNonNull(adapter);
        adapter.mObservable.notifyItemRangeChanged(i, i2, obj);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public final void onInserted(int i, int i2) {
        this.mAdapter.notifyItemRangeInserted(i, i2);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public final void onMoved(int i, int i2) {
        this.mAdapter.notifyItemMoved(i, i2);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public final void onRemoved(int i, int i2) {
        this.mAdapter.notifyItemRangeRemoved(i, i2);
    }

    public AdapterListUpdateCallback(FgsManagerController.AppListAdapter appListAdapter) {
        this.mAdapter = appListAdapter;
    }
}
