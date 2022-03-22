package androidx.leanback.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class PagingIndicator extends View {
    public static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    public static final AnonymousClass1 DOT_ALPHA = new Property<Dot, Float>() { // from class: androidx.leanback.widget.PagingIndicator.1
        @Override // android.util.Property
        public final Float get(Dot dot) {
            Dot dot2 = dot;
            Objects.requireNonNull(dot2);
            return Float.valueOf(dot2.mAlpha);
        }

        @Override // android.util.Property
        public final void set(Dot dot, Float f) {
            Dot dot2 = dot;
            float floatValue = f.floatValue();
            Objects.requireNonNull(dot2);
            dot2.mAlpha = floatValue;
            Math.round(floatValue * 255.0f);
            throw null;
        }
    };
    public static final AnonymousClass2 DOT_DIAMETER = new Property<Dot, Float>() { // from class: androidx.leanback.widget.PagingIndicator.2
        @Override // android.util.Property
        public final Float get(Dot dot) {
            Dot dot2 = dot;
            Objects.requireNonNull(dot2);
            return Float.valueOf(dot2.mDiameter);
        }

        @Override // android.util.Property
        public final void set(Dot dot, Float f) {
            Dot dot2 = dot;
            float floatValue = f.floatValue();
            Objects.requireNonNull(dot2);
            dot2.mDiameter = floatValue;
            throw null;
        }
    };
    public static final AnonymousClass3 DOT_TRANSLATION_X = new Property<Dot, Float>() { // from class: androidx.leanback.widget.PagingIndicator.3
        @Override // android.util.Property
        public final Float get(Dot dot) {
            Dot dot2 = dot;
            Objects.requireNonNull(dot2);
            return Float.valueOf(dot2.mTranslationX);
        }

        @Override // android.util.Property
        public final void set(Dot dot, Float f) {
            Dot dot2 = dot;
            float floatValue = f.floatValue();
            Objects.requireNonNull(dot2);
            dot2.mTranslationX = floatValue * 0.0f * 0.0f;
            throw null;
        }
    };
    public Bitmap mArrow;
    public final int mArrowDiameter;
    public final int mArrowGap;
    public Paint mArrowPaint;
    public final int mArrowRadius;
    public final int mDotGap;
    public final int mDotRadius;
    public int[] mDotSelectedNextX;
    public int[] mDotSelectedPrevX;
    public int[] mDotSelectedX;
    public boolean mIsLtr;
    public final int mShadowRadius;

    /* loaded from: classes.dex */
    public class Dot {
        public float mAlpha;
        public float mDiameter;
        public float mTranslationX;
    }

    public PagingIndicator(Context context) {
        this(context, null, 0);
    }

    public int getPageCount() {
        return 0;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
    }

    public PagingIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final ObjectAnimator createDotTranslationXAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, DOT_TRANSLATION_X, (-this.mArrowGap) + this.mDotGap, 0.0f);
        ofFloat.setDuration(417L);
        ofFloat.setInterpolator(DECELERATE_INTERPOLATOR);
        return ofFloat;
    }

    public PagingIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        AnimatorSet animatorSet = new AnimatorSet();
        Resources resources = getResources();
        int[] iArr = R$styleable.PagingIndicator;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, 0);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, 0);
        int dimensionFromTypedArray = getDimensionFromTypedArray(obtainStyledAttributes, 6, 2131166035);
        this.mDotRadius = dimensionFromTypedArray;
        int i2 = dimensionFromTypedArray * 2;
        int dimensionFromTypedArray2 = getDimensionFromTypedArray(obtainStyledAttributes, 2, 2131166031);
        this.mArrowRadius = dimensionFromTypedArray2;
        int i3 = dimensionFromTypedArray2 * 2;
        this.mArrowDiameter = i3;
        this.mDotGap = getDimensionFromTypedArray(obtainStyledAttributes, 5, 2131166034);
        this.mArrowGap = getDimensionFromTypedArray(obtainStyledAttributes, 4, 2131166030);
        new Paint(1).setColor(obtainStyledAttributes.getColor(3, getResources().getColor(2131099929)));
        obtainStyledAttributes.getColor(0, getResources().getColor(2131099927));
        if (this.mArrowPaint == null && obtainStyledAttributes.hasValue(1)) {
            int color = obtainStyledAttributes.getColor(1, 0);
            if (this.mArrowPaint == null) {
                this.mArrowPaint = new Paint();
            }
            this.mArrowPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        obtainStyledAttributes.recycle();
        this.mIsLtr = resources.getConfiguration().getLayoutDirection() == 0;
        int color2 = resources.getColor(2131099928);
        int dimensionPixelSize = resources.getDimensionPixelSize(2131166033);
        this.mShadowRadius = dimensionPixelSize;
        Paint paint = new Paint(1);
        float dimensionPixelSize2 = resources.getDimensionPixelSize(2131166032);
        paint.setShadowLayer(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize2, color2);
        this.mArrow = loadArrow();
        new Rect(0, 0, this.mArrow.getWidth(), this.mArrow.getHeight());
        this.mArrow.getWidth();
        float f = i3;
        AnimatorSet animatorSet2 = new AnimatorSet();
        AnonymousClass1 r9 = DOT_ALPHA;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, r9, 0.0f, 1.0f);
        ofFloat.setDuration(167L);
        DecelerateInterpolator decelerateInterpolator = DECELERATE_INTERPOLATOR;
        ofFloat.setInterpolator(decelerateInterpolator);
        float f2 = i2;
        AnonymousClass2 r11 = DOT_DIAMETER;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object) null, r11, f2, f);
        ofFloat2.setDuration(417L);
        ofFloat2.setInterpolator(decelerateInterpolator);
        animatorSet2.playTogether(ofFloat, ofFloat2, createDotTranslationXAnimator());
        AnimatorSet animatorSet3 = new AnimatorSet();
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object) null, r9, 1.0f, 0.0f);
        ofFloat3.setDuration(167L);
        ofFloat3.setInterpolator(decelerateInterpolator);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object) null, r11, f, f2);
        ofFloat4.setDuration(417L);
        ofFloat4.setInterpolator(decelerateInterpolator);
        animatorSet3.playTogether(ofFloat3, ofFloat4, createDotTranslationXAnimator());
        animatorSet.playTogether(animatorSet2, animatorSet3);
        setLayerType(1, null);
    }

    public final void calculateDotPositions() {
        int paddingLeft = getPaddingLeft();
        getPaddingTop();
        int width = getWidth() - getPaddingRight();
        int i = this.mDotRadius;
        int i2 = this.mArrowGap;
        int i3 = this.mDotGap;
        int i4 = (i3 * (-3)) + (i2 * 2) + (i * 2);
        int i5 = (paddingLeft + width) / 2;
        int[] iArr = new int[0];
        this.mDotSelectedX = iArr;
        int[] iArr2 = new int[0];
        this.mDotSelectedPrevX = iArr2;
        int[] iArr3 = new int[0];
        this.mDotSelectedNextX = iArr3;
        if (this.mIsLtr) {
            int i6 = (i5 - (i4 / 2)) + i;
            iArr[0] = (i6 - i3) + i2;
            iArr2[0] = i6;
            iArr3[0] = (i2 * 2) + (i6 - (i3 * 2));
        } else {
            int i7 = ((i4 / 2) + i5) - i;
            iArr[0] = (i7 + i3) - i2;
            iArr2[0] = i7;
            iArr3[0] = ((i3 * 2) + i7) - (i2 * 2);
        }
        throw null;
    }

    public final int getDimensionFromTypedArray(TypedArray typedArray, int i, int i2) {
        return typedArray.getDimensionPixelOffset(i, getResources().getDimensionPixelOffset(i2));
    }

    public final Bitmap loadArrow() {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), 2131232373);
        if (this.mIsLtr) {
            return decodeResource;
        }
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(decodeResource, 0, 0, decodeResource.getWidth(), decodeResource.getHeight(), matrix, false);
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int paddingBottom = getPaddingBottom() + getPaddingTop() + this.mArrowDiameter + this.mShadowRadius;
        int mode = View.MeasureSpec.getMode(i2);
        if (mode == Integer.MIN_VALUE) {
            paddingBottom = Math.min(paddingBottom, View.MeasureSpec.getSize(i2));
        } else if (mode == 1073741824) {
            paddingBottom = View.MeasureSpec.getSize(i2);
        }
        int paddingRight = getPaddingRight() + (this.mDotGap * (-3)) + (this.mArrowGap * 2) + (this.mDotRadius * 2) + getPaddingLeft();
        int mode2 = View.MeasureSpec.getMode(i);
        if (mode2 == Integer.MIN_VALUE) {
            paddingRight = Math.min(paddingRight, View.MeasureSpec.getSize(i));
        } else if (mode2 == 1073741824) {
            paddingRight = View.MeasureSpec.getSize(i);
        }
        setMeasuredDimension(paddingRight, paddingBottom);
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
        boolean z;
        super.onRtlPropertiesChanged(i);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.mIsLtr != z) {
            this.mIsLtr = z;
            this.mArrow = loadArrow();
            calculateDotPositions();
            throw null;
        }
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        setMeasuredDimension(i, i2);
        calculateDotPositions();
        throw null;
    }

    public int[] getDotSelectedLeftX() {
        return this.mDotSelectedPrevX;
    }

    public int[] getDotSelectedRightX() {
        return this.mDotSelectedNextX;
    }

    public int[] getDotSelectedX() {
        return this.mDotSelectedX;
    }
}
