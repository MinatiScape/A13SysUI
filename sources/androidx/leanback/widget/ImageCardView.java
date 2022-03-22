package androidx.leanback.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ImageCardView extends BaseCardView {
    public boolean mAttachedToWindow;
    public ImageView mBadgeImage;
    public TextView mContentView;
    public ObjectAnimator mFadeInAnimator;
    public ImageView mImageView;
    public ViewGroup mInfoArea;
    public TextView mTitleView;

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // androidx.leanback.widget.BaseCardView, android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        this.mFadeInAnimator.cancel();
        this.mImageView.setAlpha(1.0f);
        super.onDetachedFromWindow();
    }

    public ImageCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130969227);
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        ViewGroup viewGroup;
        setFocusable(true);
        setFocusableInTouchMode(true);
        LayoutInflater from = LayoutInflater.from(getContext());
        from.inflate(2131624200, this);
        Context context2 = getContext();
        int[] iArr = R$styleable.lbImageCardView;
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, iArr, 2130969227, 2132018428);
        Context context3 = getContext();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context3, iArr, attributeSet, obtainStyledAttributes, 2130969227, 2132018428);
        int i = obtainStyledAttributes.getInt(1, 0);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        if ((i & 1) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((i & 2) == 2) {
            z3 = true;
        } else {
            z3 = false;
        }
        if ((i & 4) == 4) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (z4 || (i & 8) != 8) {
            z5 = false;
        } else {
            z5 = true;
        }
        ImageView imageView = (ImageView) findViewById(2131428296);
        this.mImageView = imageView;
        if (imageView.getDrawable() == null) {
            this.mImageView.setVisibility(4);
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mImageView, "alpha", 1.0f);
        this.mFadeInAnimator = ofFloat;
        ofFloat.setDuration(this.mImageView.getResources().getInteger(17694720));
        ViewGroup viewGroup2 = (ViewGroup) findViewById(2131428125);
        this.mInfoArea = viewGroup2;
        if (z) {
            removeView(viewGroup2);
            obtainStyledAttributes.recycle();
            return;
        }
        if (z2) {
            TextView textView = (TextView) from.inflate(2131624204, viewGroup2, false);
            this.mTitleView = textView;
            this.mInfoArea.addView(textView);
        }
        if (z3) {
            TextView textView2 = (TextView) from.inflate(2131624203, this.mInfoArea, false);
            this.mContentView = textView2;
            this.mInfoArea.addView(textView2);
        }
        if (z4 || z5) {
            ImageView imageView2 = (ImageView) from.inflate(z5 ? 2131624201 : 2131624202, this.mInfoArea, false);
            this.mBadgeImage = imageView2;
            this.mInfoArea.addView(imageView2);
        }
        if (z2 && !z3 && this.mBadgeImage != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
            if (z5) {
                layoutParams.addRule(17, this.mBadgeImage.getId());
            } else {
                layoutParams.addRule(16, this.mBadgeImage.getId());
            }
            this.mTitleView.setLayoutParams(layoutParams);
        }
        if (z3) {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mContentView.getLayoutParams();
            if (!z2) {
                layoutParams2.addRule(10);
            }
            if (z5) {
                layoutParams2.removeRule(16);
                layoutParams2.removeRule(20);
                layoutParams2.addRule(17, this.mBadgeImage.getId());
            }
            this.mContentView.setLayoutParams(layoutParams2);
        }
        ImageView imageView3 = this.mBadgeImage;
        if (imageView3 != null) {
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) imageView3.getLayoutParams();
            if (z3) {
                layoutParams3.addRule(8, this.mContentView.getId());
            } else if (z2) {
                layoutParams3.addRule(8, this.mTitleView.getId());
            }
            this.mBadgeImage.setLayoutParams(layoutParams3);
        }
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        if (!(drawable == null || (viewGroup = this.mInfoArea) == null)) {
            viewGroup.setBackground(drawable);
        }
        ImageView imageView4 = this.mBadgeImage;
        if (imageView4 != null && imageView4.getDrawable() == null) {
            this.mBadgeImage.setVisibility(8);
        }
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        if (this.mImageView.getAlpha() == 0.0f) {
            this.mImageView.setAlpha(0.0f);
            if (this.mAttachedToWindow) {
                this.mFadeInAnimator.start();
            }
        }
    }
}
