package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.leanback.R$raw;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.TransformState;
import java.util.Objects;
/* loaded from: classes.dex */
public class HybridNotificationView extends AlphaOptimizedLinearLayout implements TransformableView, NotificationFadeAware {
    public TextView mTextView;
    public TextView mTitleView;
    public final ViewTransformationHelper mTransformationHelper;

    public HybridNotificationView(Context context) {
        this(context, null);
    }

    public void setNotificationFaded(boolean z) {
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformFrom(TransformableView transformableView) {
        this.mTransformationHelper.transformFrom(transformableView);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformTo(TransformableView transformableView, Runnable runnable) {
        this.mTransformationHelper.transformTo(transformableView, runnable);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void bind(CharSequence charSequence, CharSequence charSequence2, View view) {
        int i;
        this.mTitleView.setText(charSequence);
        TextView textView = this.mTitleView;
        if (TextUtils.isEmpty(charSequence)) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        if (TextUtils.isEmpty(charSequence2)) {
            this.mTextView.setVisibility(8);
            this.mTextView.setText((CharSequence) null);
        } else {
            this.mTextView.setVisibility(0);
            this.mTextView.setText(charSequence2.toString());
        }
        requestLayout();
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final TransformState getCurrentState(int i) {
        return this.mTransformationHelper.getCurrentState(i);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void setVisible(boolean z) {
        int i;
        if (z) {
            i = 0;
        } else {
            i = 4;
        }
        setVisibility(i);
        this.mTransformationHelper.setVisible(z);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformFrom(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformFrom(transformableView, f);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformTo(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformTo(transformableView, f);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(2131428523);
        this.mTextView = (TextView) findViewById(2131428522);
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        ViewTransformationHelper.CustomTransformation customTransformation = new ViewTransformationHelper.CustomTransformation() { // from class: com.android.systemui.statusbar.notification.row.HybridNotificationView.1
            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public final boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                TransformState currentState = transformableView.getCurrentState(1);
                R$raw.fadeIn((View) HybridNotificationView.this.mTextView, f, true);
                if (currentState != null) {
                    transformState.transformViewFrom(currentState, 16, null, f);
                    currentState.recycle();
                }
                return true;
            }

            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public final boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                TransformState currentState = transformableView.getCurrentState(1);
                R$raw.fadeOut((View) HybridNotificationView.this.mTextView, f, true);
                if (currentState != null) {
                    transformState.transformViewTo(currentState, 16, null, f);
                    currentState.recycle();
                }
                return true;
            }
        };
        Objects.requireNonNull(viewTransformationHelper);
        viewTransformationHelper.mCustomTransformations.put(2, customTransformation);
        this.mTransformationHelper.addTransformedView(1, this.mTitleView);
        this.mTransformationHelper.addTransformedView(2, this.mTextView);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTransformationHelper = new ViewTransformationHelper();
    }
}
