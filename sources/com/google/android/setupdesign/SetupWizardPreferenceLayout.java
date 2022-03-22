package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RecyclerMixin;
/* loaded from: classes.dex */
public class SetupWizardPreferenceLayout extends SetupWizardRecyclerLayout {
    @Override // com.google.android.setupdesign.SetupWizardRecyclerLayout, com.google.android.setupdesign.SetupWizardLayout, com.google.android.setupcompat.internal.TemplateLayout
    public final ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 2131428973;
        }
        return super.findContainer(i);
    }

    @Override // com.google.android.setupdesign.SetupWizardRecyclerLayout, com.google.android.setupdesign.SetupWizardLayout, com.google.android.setupcompat.internal.TemplateLayout
    public final View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624582;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    @Override // com.google.android.setupdesign.SetupWizardRecyclerLayout, com.google.android.setupcompat.internal.TemplateLayout
    public final void onTemplateInflated() {
        this.recyclerMixin = new RecyclerMixin(this, (RecyclerView) LayoutInflater.from(getContext()).inflate(2131624579, (ViewGroup) this, false));
    }

    public SetupWizardPreferenceLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
