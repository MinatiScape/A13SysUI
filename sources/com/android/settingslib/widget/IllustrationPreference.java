package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
/* loaded from: classes.dex */
public class IllustrationPreference extends Preference {
    public int mImageResId;
    public int mMaxHeight = -1;
    public final AnonymousClass1 mAnimationCallback = new Animatable2.AnimationCallback() { // from class: com.android.settingslib.widget.IllustrationPreference.1
        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public final void onAnimationEnd(Drawable drawable) {
            ((Animatable) drawable).start();
        }
    };
    public final AnonymousClass2 mAnimationCallbackCompat = new Animatable2Compat.AnimationCallback() { // from class: com.android.settingslib.widget.IllustrationPreference.2
        @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
        public final void onAnimationEnd(Drawable drawable) {
            ((Animatable) drawable).start();
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.settingslib.widget.IllustrationPreference$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.settingslib.widget.IllustrationPreference$2] */
    public IllustrationPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutResId = 2131624135;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView, 0, 0);
            this.mImageResId = obtainStyledAttributes.getResourceId(9, 0);
            obtainStyledAttributes.recycle();
        }
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(2131427547);
        FrameLayout frameLayout = (FrameLayout) preferenceViewHolder.findViewById(2131428370);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) preferenceViewHolder.findViewById(2131428287);
        int i = this.mContext.getResources().getDisplayMetrics().widthPixels;
        int i2 = this.mContext.getResources().getDisplayMetrics().heightPixels;
        FrameLayout frameLayout2 = (FrameLayout) preferenceViewHolder.findViewById(2131428114);
        ViewGroup.LayoutParams layoutParams = frameLayout2.getLayoutParams();
        if (i >= i2) {
            i = i2;
        }
        layoutParams.width = i;
        frameLayout2.setLayoutParams(layoutParams);
        if (this.mImageResId > 0) {
            Drawable drawable = lottieAnimationView.getDrawable();
            if (drawable instanceof Animatable) {
                if (drawable instanceof Animatable2) {
                    ((Animatable2) drawable).clearAnimationCallbacks();
                } else if (drawable instanceof Animatable2Compat) {
                    ((Animatable2Compat) drawable).clearAnimationCallbacks();
                }
                ((Animatable) drawable).stop();
            }
            lottieAnimationView.cancelAnimation();
            lottieAnimationView.setImageResource(this.mImageResId);
            Drawable drawable2 = lottieAnimationView.getDrawable();
            if (drawable2 == null) {
                final int i3 = this.mImageResId;
                lottieAnimationView.setFailureListener(new LottieListener() { // from class: com.android.settingslib.widget.IllustrationPreference$$ExternalSyntheticLambda0
                    @Override // com.airbnb.lottie.LottieListener
                    public final void onResult(Object obj) {
                        int i4 = i3;
                        Log.w("IllustrationPreference", "Invalid illustration resource id: " + i4, (Throwable) obj);
                    }
                });
                lottieAnimationView.setAnimation(i3);
                lottieAnimationView.setRepeatCount(-1);
                lottieAnimationView.playAnimation();
            } else if (drawable2 instanceof Animatable) {
                if (drawable2 instanceof Animatable2) {
                    ((Animatable2) drawable2).registerAnimationCallback(this.mAnimationCallback);
                } else if (drawable2 instanceof Animatable2Compat) {
                    ((Animatable2Compat) drawable2).registerAnimationCallback(this.mAnimationCallbackCompat);
                } else if (drawable2 instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable2).setOneShot(false);
                }
                ((Animatable) drawable2).start();
            }
        }
        if (this.mMaxHeight != -1) {
            Resources resources = imageView.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(2131166985);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(2131166983);
            int min = Math.min(this.mMaxHeight, dimensionPixelSize2);
            imageView.setMaxHeight(min);
            lottieAnimationView.setMaxHeight(min);
            lottieAnimationView.setMaxWidth((int) (min * (dimensionPixelSize / dimensionPixelSize2)));
        }
        frameLayout.removeAllViews();
        frameLayout.setVisibility(8);
    }
}
