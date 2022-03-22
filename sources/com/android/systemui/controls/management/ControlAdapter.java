package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlAdapter extends RecyclerView.Adapter<Holder> {
    public final float elevation;
    public ControlsModel model;
    public final ControlAdapter$spanSizeLookup$1 spanSizeLookup = new GridLayoutManager.SpanSizeLookup() { // from class: com.android.systemui.controls.management.ControlAdapter$spanSizeLookup$1
        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public final int getSpanSize(int i) {
            if (ControlAdapter.this.getItemViewType(i) != 1) {
                return 2;
            }
            return 1;
        }
    };

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(Holder holder, int i) {
        Holder holder2 = holder;
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            holder2.bindData(controlsModel.getElements().get(i));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        List<ElementWrapper> elements;
        ControlsModel controlsModel = this.model;
        if (controlsModel == null || (elements = controlsModel.getElements()) == null) {
            return 0;
        }
        return elements.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemViewType(int i) {
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ZoneNameWrapper) {
                return 0;
            }
            if ((elementWrapper instanceof ControlStatusWrapper) || (elementWrapper instanceof ControlInfoWrapper)) {
                return 1;
            }
            if (elementWrapper instanceof DividerWrapper) {
                return 2;
            }
            throw new NoWhenBranchMatchedException();
        }
        throw new IllegalStateException("Getting item type for null model");
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.management.ControlAdapter$spanSizeLookup$1] */
    public ControlAdapter(float f) {
        this.elevation = f;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        ControlsModel.MoveHelper moveHelper;
        LayoutInflater from = LayoutInflater.from(recyclerView.getContext());
        if (i == 0) {
            return new ZoneHolder(from.inflate(2131624061, (ViewGroup) recyclerView, false));
        }
        if (i == 1) {
            View inflate = from.inflate(2131624044, (ViewGroup) recyclerView, false);
            ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.width = -1;
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
            inflate.setElevation(this.elevation);
            inflate.setBackground(recyclerView.getContext().getDrawable(2131231667));
            ControlsModel controlsModel = this.model;
            if (controlsModel == null) {
                moveHelper = null;
            } else {
                moveHelper = controlsModel.getMoveHelper();
            }
            return new ControlHolder(inflate, moveHelper, new ControlAdapter$onCreateViewHolder$2(this));
        } else if (i == 2) {
            return new DividerHolder(from.inflate(2131624049, (ViewGroup) recyclerView, false));
        } else {
            throw new IllegalStateException(Intrinsics.stringPlus("Wrong viewType: ", Integer.valueOf(i)));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(Holder holder, int i, List list) {
        Holder holder2 = holder;
        if (list.isEmpty()) {
            onBindViewHolder(holder2, i);
            return;
        }
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ControlInterface) {
                holder2.updateFavorite(((ControlInterface) elementWrapper).getFavorite());
            }
        }
    }
}
