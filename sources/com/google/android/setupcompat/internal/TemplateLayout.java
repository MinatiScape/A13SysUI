package com.google.android.setupcompat.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.annotation.Keep;
import com.android.systemui.R$id;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.android.setupcompat.template.Mixin;
import java.util.HashMap;
/* loaded from: classes.dex */
public class TemplateLayout extends FrameLayout {
    public ViewGroup container;
    public final HashMap mixins = new HashMap();
    public AnonymousClass1 preDrawListener;
    public float xFraction;

    public TemplateLayout(Context context, int i, int i2) {
        super(context);
        init(i, i2, null, 2130969819);
    }

    public void onBeforeTemplateInflated(AttributeSet attributeSet, int i) {
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        return inflateTemplate(layoutInflater, 0, i);
    }

    public void onTemplateInflated() {
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        this.container.addView(view, i, layoutParams);
    }

    public final <M extends Mixin> M getMixin(Class<M> cls) {
        return (M) ((Mixin) this.mixins.get(cls));
    }

    public final View inflateTemplate(LayoutInflater layoutInflater, int i, int i2) {
        if (i2 != 0) {
            if (i != 0) {
                layoutInflater = LayoutInflater.from(new FallbackThemeWrapper(layoutInflater.getContext(), i));
            }
            return layoutInflater.inflate(i2, (ViewGroup) this, false);
        }
        throw new IllegalArgumentException("android:layout not specified for TemplateLayout");
    }

    public final <M extends Mixin> void registerMixin(Class<M> cls, M m) {
        this.mixins.put(cls, m);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.google.android.setupcompat.internal.TemplateLayout$1] */
    @Keep
    @TargetApi(QSTileImpl.H.STALE)
    public void setXFraction(float f) {
        this.xFraction = f;
        int width = getWidth();
        if (width != 0) {
            setTranslationX(width * f);
        } else if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.setupcompat.internal.TemplateLayout.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public final boolean onPreDraw() {
                    TemplateLayout.this.getViewTreeObserver().removeOnPreDrawListener(TemplateLayout.this.preDrawListener);
                    TemplateLayout templateLayout = TemplateLayout.this;
                    templateLayout.setXFraction(templateLayout.xFraction);
                    return true;
                }
            };
            getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }

    public ViewGroup findContainer(int i) {
        return (ViewGroup) findViewById(i);
    }

    public <T extends View> T findManagedViewById(int i) {
        return (T) findViewById(i);
    }

    public final void init(int i, int i2, AttributeSet attributeSet, int i3) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$id.SucTemplateLayout, i3, 0);
        if (i == 0) {
            i = obtainStyledAttributes.getResourceId(0, 0);
        }
        if (i2 == 0) {
            i2 = obtainStyledAttributes.getResourceId(1, 0);
        }
        onBeforeTemplateInflated(attributeSet, i3);
        super.addView(onInflateTemplate(LayoutInflater.from(getContext()), i), -1, generateDefaultLayoutParams());
        ViewGroup findContainer = findContainer(i2);
        this.container = findContainer;
        if (findContainer != null) {
            onTemplateInflated();
            obtainStyledAttributes.recycle();
            return;
        }
        throw new IllegalArgumentException("Container cannot be null in TemplateLayout");
    }

    public TemplateLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(0, 0, attributeSet, 2130969819);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public TemplateLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(0, 0, attributeSet, i);
    }

    @Keep
    @TargetApi(QSTileImpl.H.STALE)
    public float getXFraction() {
        return this.xFraction;
    }
}
