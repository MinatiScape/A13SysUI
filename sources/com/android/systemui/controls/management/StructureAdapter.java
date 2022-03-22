package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Objects;
/* compiled from: StructureAdapter.kt */
/* loaded from: classes.dex */
public final class StructureAdapter extends RecyclerView.Adapter<StructureHolder> {
    public final List<StructureContainer> models;

    /* compiled from: StructureAdapter.kt */
    /* loaded from: classes.dex */
    public static final class StructureHolder extends RecyclerView.ViewHolder {
        public final ControlAdapter controlAdapter;

        public StructureHolder(View view) {
            super(view);
            RecyclerView recyclerView = (RecyclerView) view.requireViewById(2131428260);
            ControlAdapter controlAdapter = new ControlAdapter(view.getContext().getResources().getFloat(2131165530));
            this.controlAdapter = controlAdapter;
            int dimensionPixelSize = view.getContext().getResources().getDimensionPixelSize(2131165556);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            recyclerView.setAdapter(controlAdapter);
            recyclerView.getContext();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(2);
            gridLayoutManager.mSpanSizeLookup = controlAdapter.spanSizeLookup;
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(marginItemDecorator);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.models.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(StructureHolder structureHolder, int i) {
        StructureContainer structureContainer = this.models.get(i);
        Objects.requireNonNull(structureContainer);
        ControlsModel controlsModel = structureContainer.model;
        ControlAdapter controlAdapter = structureHolder.controlAdapter;
        Objects.requireNonNull(controlAdapter);
        controlAdapter.model = controlsModel;
        controlAdapter.notifyDataSetChanged();
    }

    public StructureAdapter(List<StructureContainer> list) {
        this.models = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        return new StructureHolder(LayoutInflater.from(recyclerView.getContext()).inflate(2131624059, (ViewGroup) recyclerView, false));
    }
}
