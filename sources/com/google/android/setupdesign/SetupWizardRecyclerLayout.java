package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RecyclerMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import java.util.Objects;
/* loaded from: classes.dex */
public class SetupWizardRecyclerLayout extends SetupWizardLayout {
    public RecyclerMixin recyclerMixin;

    @Override // com.google.android.setupdesign.SetupWizardLayout, com.google.android.setupcompat.internal.TemplateLayout
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 2131428992;
        }
        return super.findContainer(i);
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public final <T extends View> T findManagedViewById(int i) {
        T t;
        RecyclerMixin recyclerMixin = this.recyclerMixin;
        Objects.requireNonNull(recyclerMixin);
        View view = recyclerMixin.header;
        if (view == null || (t = (T) view.findViewById(i)) == null) {
            return (T) super.findViewById(i);
        }
        return t;
    }

    @Override // com.google.android.setupdesign.SetupWizardLayout, com.google.android.setupcompat.internal.TemplateLayout
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624587;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    public SetupWizardRecyclerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            this.recyclerMixin.parseAttributes(attributeSet);
            registerMixin(RecyclerMixin.class, this.recyclerMixin);
            Objects.requireNonNull(this.recyclerMixin);
            Objects.requireNonNull((RequireScrollMixin) getMixin(RequireScrollMixin.class));
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        RecyclerMixin recyclerMixin = this.recyclerMixin;
        Objects.requireNonNull(recyclerMixin);
        if (recyclerMixin.divider == null) {
            recyclerMixin.updateDivider();
        }
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public void onTemplateInflated() {
        View findViewById = findViewById(2131428992);
        if (findViewById instanceof RecyclerView) {
            this.recyclerMixin = new RecyclerMixin(this, (RecyclerView) findViewById);
            return;
        }
        throw new IllegalStateException("SetupWizardRecyclerLayout should use a template with recycler view");
    }
}
