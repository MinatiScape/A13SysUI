package com.android.systemui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.ViewProvider;
import com.android.systemui.shared.plugins.PluginManager;
/* loaded from: classes.dex */
public class PluginInflateContainer extends AutoReinflateContainer implements PluginListener<ViewProvider> {
    public Class<ViewProvider> mClass;
    public View mPluginView;

    @Override // com.android.systemui.AutoReinflateContainer
    public final void inflateLayoutImpl() {
        View view = this.mPluginView;
        if (view != null) {
            addView(view);
        } else {
            super.inflateLayoutImpl();
        }
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(ViewProvider viewProvider, Context context) {
        this.mPluginView = viewProvider.getView();
        inflateLayout();
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(ViewProvider viewProvider) {
        this.mPluginView = null;
        inflateLayout();
    }

    public PluginInflateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PluginInflateContainer);
        String string = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
        try {
            this.mClass = Class.forName(string);
        } catch (Exception e) {
            Log.d("PluginInflateContainer", "Problem getting class info " + string, e);
            this.mClass = null;
        }
    }

    @Override // com.android.systemui.AutoReinflateContainer, android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mClass != null) {
            ((PluginManager) Dependency.get(PluginManager.class)).addPluginListener(this, this.mClass);
        }
    }

    @Override // com.android.systemui.AutoReinflateContainer, android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mClass != null) {
            ((PluginManager) Dependency.get(PluginManager.class)).removePluginListener(this);
        }
    }
}
