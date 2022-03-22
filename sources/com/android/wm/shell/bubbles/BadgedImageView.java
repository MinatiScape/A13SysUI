package com.android.wm.shell.bubbles;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.PathParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.launcher3.icons.DotRenderer;
import com.android.wm.shell.animation.Interpolators;
import java.util.EnumSet;
import java.util.Objects;
/* loaded from: classes.dex */
public class BadgedImageView extends ConstraintLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public float mAnimatingToDotScale;
    public final ImageView mAppIcon;
    public BubbleViewProvider mBubble;
    public final ImageView mBubbleIcon;
    public int mDotColor;
    public boolean mDotIsAnimating;
    public DotRenderer mDotRenderer;
    public float mDotScale;
    public final EnumSet<SuppressionFlag> mDotSuppressionFlags;
    public DotRenderer.DrawParams mDrawParams;
    public boolean mOnLeft;
    public BubblePositioner mPositioner;
    public Rect mTempBounds;

    /* loaded from: classes.dex */
    public enum SuppressionFlag {
        FLYOUT_VISIBLE,
        BEHIND_STACK
    }

    public BadgedImageView(Context context) {
        this(context, null);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final float[] getDotCenter() {
        float[] fArr;
        if (this.mOnLeft) {
            DotRenderer dotRenderer = this.mDotRenderer;
            Objects.requireNonNull(dotRenderer);
            fArr = dotRenderer.mLeftDotPosition;
        } else {
            DotRenderer dotRenderer2 = this.mDotRenderer;
            Objects.requireNonNull(dotRenderer2);
            fArr = dotRenderer2.mRightDotPosition;
        }
        getDrawingRect(this.mTempBounds);
        return new float[]{this.mTempBounds.width() * fArr[0], this.mTempBounds.height() * fArr[1]};
    }

    public final void hideDotAndBadge(boolean z) {
        if (this.mDotSuppressionFlags.add(SuppressionFlag.BEHIND_STACK)) {
            updateDotVisibility(true);
        }
        this.mOnLeft = z;
        this.mAppIcon.setVisibility(8);
    }

    public final void initialize(BubblePositioner bubblePositioner) {
        this.mPositioner = bubblePositioner;
        Path createPathFromPathData = PathParser.createPathFromPathData(getResources().getString(17039972));
        BubblePositioner bubblePositioner2 = this.mPositioner;
        Objects.requireNonNull(bubblePositioner2);
        this.mDotRenderer = new DotRenderer(bubblePositioner2.mBubbleSize, createPathFromPathData);
    }

    public final void removeDotSuppressionFlag(SuppressionFlag suppressionFlag) {
        boolean z;
        if (this.mDotSuppressionFlags.remove(suppressionFlag)) {
            if (suppressionFlag == SuppressionFlag.BEHIND_STACK) {
                z = true;
            } else {
                z = false;
            }
            updateDotVisibility(z);
        }
    }

    public final void setRenderedBubble(BubbleViewProvider bubbleViewProvider) {
        this.mBubble = bubbleViewProvider;
        this.mBubbleIcon.setImageBitmap(bubbleViewProvider.getBubbleIcon());
        this.mAppIcon.setImageBitmap(bubbleViewProvider.getAppBadge());
        if (this.mDotSuppressionFlags.contains(SuppressionFlag.BEHIND_STACK)) {
            this.mAppIcon.setVisibility(8);
        } else {
            showBadge();
        }
        this.mDotColor = bubbleViewProvider.getDotColor();
        Path dotPath = bubbleViewProvider.getDotPath();
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        this.mDotRenderer = new DotRenderer(bubblePositioner.mBubbleSize, dotPath);
        invalidate();
    }

    public final boolean shouldDrawDot() {
        if (this.mDotIsAnimating || (this.mBubble.showDot() && this.mDotSuppressionFlags.isEmpty())) {
            return true;
        }
        return false;
    }

    public final void showBadge() {
        int i;
        if (this.mBubble.getAppBadge() == null) {
            this.mAppIcon.setVisibility(8);
            return;
        }
        if (this.mOnLeft) {
            i = -(this.mBubbleIcon.getWidth() - this.mAppIcon.getWidth());
        } else {
            i = 0;
        }
        this.mAppIcon.setTranslationX(i);
        this.mAppIcon.setVisibility(0);
    }

    public final void showDotAndBadge(boolean z) {
        removeDotSuppressionFlag(SuppressionFlag.BEHIND_STACK);
        this.mOnLeft = z;
        showBadge();
    }

    @Override // android.view.View
    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BadgedImageView{");
        m.append(this.mBubble);
        m.append("}");
        return m.toString();
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        float[] fArr;
        float f;
        super.onDraw(canvas);
        if (shouldDrawDot()) {
            getDrawingRect(this.mTempBounds);
            DotRenderer.DrawParams drawParams = this.mDrawParams;
            drawParams.color = this.mDotColor;
            drawParams.iconBounds = this.mTempBounds;
            drawParams.leftAlign = this.mOnLeft;
            drawParams.scale = this.mDotScale;
            DotRenderer dotRenderer = this.mDotRenderer;
            Objects.requireNonNull(dotRenderer);
            if (drawParams == null) {
                Log.e("DotRenderer", "Invalid null argument(s) passed in call to draw.");
                return;
            }
            canvas.save();
            Rect rect = drawParams.iconBounds;
            if (drawParams.leftAlign) {
                fArr = dotRenderer.mLeftDotPosition;
            } else {
                fArr = dotRenderer.mRightDotPosition;
            }
            float width = (rect.width() * fArr[0]) + rect.left;
            float height = (rect.height() * fArr[1]) + rect.top;
            Rect clipBounds = canvas.getClipBounds();
            if (drawParams.leftAlign) {
                f = Math.max(0.0f, clipBounds.left - (dotRenderer.mBitmapOffset + width));
            } else {
                f = Math.min(0.0f, clipBounds.right - (width - dotRenderer.mBitmapOffset));
            }
            canvas.translate(width + f, height + Math.max(0.0f, clipBounds.top - (dotRenderer.mBitmapOffset + height)));
            float f2 = drawParams.scale;
            canvas.scale(f2, f2);
            dotRenderer.mCirclePaint.setColor(-16777216);
            Bitmap bitmap = dotRenderer.mBackgroundWithShadow;
            float f3 = dotRenderer.mBitmapOffset;
            canvas.drawBitmap(bitmap, f3, f3, dotRenderer.mCirclePaint);
            dotRenderer.mCirclePaint.setColor(drawParams.color);
            canvas.drawCircle(0.0f, 0.0f, dotRenderer.mCircleRadius, dotRenderer.mCirclePaint);
            canvas.restore();
        }
    }

    public final void updateDotVisibility(boolean z) {
        float f;
        if (shouldDrawDot()) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        if (z) {
            final boolean z2 = true;
            this.mDotIsAnimating = true;
            if (this.mAnimatingToDotScale == f || !shouldDrawDot()) {
                this.mDotIsAnimating = false;
                return;
            }
            this.mAnimatingToDotScale = f;
            if (f <= 0.0f) {
                z2 = false;
            }
            clearAnimation();
            animate().setDuration(200L).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.bubbles.BadgedImageView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BadgedImageView badgedImageView = BadgedImageView.this;
                    boolean z3 = z2;
                    int i = BadgedImageView.$r8$clinit;
                    Objects.requireNonNull(badgedImageView);
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    if (!z3) {
                        animatedFraction = 1.0f - animatedFraction;
                    }
                    badgedImageView.mDotScale = animatedFraction;
                    badgedImageView.invalidate();
                }
            }).withEndAction(new Runnable() { // from class: com.android.wm.shell.bubbles.BadgedImageView$$ExternalSyntheticLambda1
                public final /* synthetic */ Runnable f$2 = null;

                @Override // java.lang.Runnable
                public final void run() {
                    float f2;
                    BadgedImageView badgedImageView = BadgedImageView.this;
                    boolean z3 = z2;
                    Runnable runnable = this.f$2;
                    int i = BadgedImageView.$r8$clinit;
                    Objects.requireNonNull(badgedImageView);
                    if (z3) {
                        f2 = 1.0f;
                    } else {
                        f2 = 0.0f;
                    }
                    badgedImageView.mDotScale = f2;
                    badgedImageView.invalidate();
                    badgedImageView.mDotIsAnimating = false;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }).start();
            return;
        }
        this.mDotScale = f;
        this.mAnimatingToDotScale = f;
        invalidate();
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDotSuppressionFlags = EnumSet.of(SuppressionFlag.FLYOUT_VISIBLE);
        this.mDotScale = 0.0f;
        this.mAnimatingToDotScale = 0.0f;
        this.mDotIsAnimating = false;
        this.mTempBounds = new Rect();
        setLayoutDirection(0);
        LayoutInflater.from(context).inflate(2131624014, this);
        ImageView imageView = (ImageView) findViewById(2131428109);
        this.mBubbleIcon = imageView;
        this.mAppIcon = (ImageView) findViewById(2131427503);
        TypedArray obtainStyledAttributes = ((ViewGroup) this).mContext.obtainStyledAttributes(attributeSet, new int[]{16843033}, i, i2);
        imageView.setImageResource(obtainStyledAttributes.getResourceId(0, 0));
        obtainStyledAttributes.recycle();
        this.mDrawParams = new DotRenderer.DrawParams();
        setFocusable(true);
        setClickable(true);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.wm.shell.bubbles.BadgedImageView.1
            @Override // android.view.ViewOutlineProvider
            public final void getOutline(View view, Outline outline) {
                BadgedImageView badgedImageView = BadgedImageView.this;
                int i3 = BadgedImageView.$r8$clinit;
                Objects.requireNonNull(badgedImageView);
                BubblePositioner bubblePositioner = badgedImageView.mPositioner;
                Objects.requireNonNull(bubblePositioner);
                int i4 = bubblePositioner.mBubbleSize;
                int round = (int) Math.round(Math.sqrt((((i4 * i4) * 0.6597222f) * 4.0f) / 3.141592653589793d));
                int i5 = (i4 - round) / 2;
                int i6 = round + i5;
                outline.setOval(i5, i5, i6, i6);
            }
        });
    }
}
