package com.google.android.setupdesign.items;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemHierarchy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RecyclerItemAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ItemHierarchy.Observer {
    public final boolean applyPartnerHeavyThemeResource;
    public final ItemHierarchy itemHierarchy;
    public final boolean useFullDynamicColor;

    /* loaded from: classes.dex */
    public static class PatchedLayerDrawable extends LayerDrawable {
        @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
        public final boolean getPadding(Rect rect) {
            if (!super.getPadding(rect) || (rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0)) {
                return false;
            }
            return true;
        }

        public PatchedLayerDrawable(Drawable[] drawableArr) {
            super(drawableArr);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.itemHierarchy.getCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final long getItemId(int i) {
        AbstractItem itemAt = this.itemHierarchy.getItemAt(i);
        if (!(itemAt instanceof AbstractItem)) {
            return -1L;
        }
        Objects.requireNonNull(itemAt);
        int i2 = itemAt.id;
        if (i2 > 0) {
            return i2;
        }
        return -1L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemViewType(int i) {
        return this.itemHierarchy.getItemAt(i).getLayoutResource();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        ItemViewHolder itemViewHolder2 = itemViewHolder;
        AbstractItem itemAt = this.itemHierarchy.getItemAt(i);
        boolean isEnabled = itemAt.isEnabled();
        itemViewHolder2.isEnabled = isEnabled;
        itemViewHolder2.itemView.setClickable(isEnabled);
        itemViewHolder2.itemView.setEnabled(isEnabled);
        itemViewHolder2.itemView.setFocusable(isEnabled);
        itemViewHolder2.item = itemAt;
        itemAt.onBindView(itemViewHolder2.itemView);
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeChanged(ItemHierarchy itemHierarchy, int i) {
        RecyclerView.AdapterDataObservable adapterDataObservable = this.mObservable;
        Objects.requireNonNull(adapterDataObservable);
        adapterDataObservable.notifyItemRangeChanged(i, 1, null);
    }

    public RecyclerItemAdapter(ItemHierarchy itemHierarchy, boolean z, boolean z2) {
        this.applyPartnerHeavyThemeResource = z;
        this.useFullDynamicColor = z2;
        this.itemHierarchy = itemHierarchy;
        itemHierarchy.registerObserver(this);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        Drawable drawable;
        View inflate = LayoutInflater.from(recyclerView.getContext()).inflate(i, (ViewGroup) recyclerView, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(inflate);
        if (!"noBackground".equals(inflate.getTag())) {
            TypedArray obtainStyledAttributes = recyclerView.getContext().obtainStyledAttributes(R$styleable.SudRecyclerItemAdapter);
            Drawable drawable2 = obtainStyledAttributes.getDrawable(1);
            if (drawable2 == null) {
                drawable2 = obtainStyledAttributes.getDrawable(2);
                drawable = null;
            } else {
                drawable = inflate.getBackground();
                if (drawable == null) {
                    if (!this.applyPartnerHeavyThemeResource || this.useFullDynamicColor) {
                        drawable = obtainStyledAttributes.getDrawable(0);
                    } else {
                        drawable = new ColorDrawable(PartnerConfigHelper.get(inflate.getContext()).getColor(inflate.getContext(), PartnerConfig.CONFIG_LAYOUT_BACKGROUND_COLOR));
                    }
                }
            }
            if (drawable2 == null || drawable == null) {
                Log.e("RecyclerItemAdapter", "Cannot resolve required attributes. selectableItemBackground=" + drawable2 + " background=" + drawable);
            } else {
                inflate.setBackgroundDrawable(new PatchedLayerDrawable(new Drawable[]{drawable, drawable2}));
            }
            obtainStyledAttributes.recycle();
        }
        inflate.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.setupdesign.items.RecyclerItemAdapter.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Objects.requireNonNull(itemViewHolder);
                Objects.requireNonNull(RecyclerItemAdapter.this);
            }
        });
        return itemViewHolder;
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeInserted(ItemHierarchy itemHierarchy, int i, int i2) {
        notifyItemRangeInserted(i, i2);
    }
}
