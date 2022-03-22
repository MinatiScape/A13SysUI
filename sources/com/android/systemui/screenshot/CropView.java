package com.android.systemui.screenshot;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.widget.SeekBar;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.R$styleable;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class CropView extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mActivePointerId;
    public final Paint mContainerBackgroundPaint;
    public RectF mCrop;
    public CropInteractionListener mCropInteractionListener;
    public final float mCropTouchMargin;
    public CropBoundary mCurrentDraggingBoundary;
    public float mEntranceInterpolation;
    public final AccessibilityHelper mExploreByTouchHelper;
    public int mExtraBottomPadding;
    public int mExtraTopPadding;
    public final Paint mHandlePaint;
    public int mImageWidth;
    public Range<Float> mMotionRange;
    public float mMovementStartValue;
    public final Paint mShadePaint;
    public float mStartingX;
    public float mStartingY;

    /* loaded from: classes.dex */
    public class AccessibilityHelper extends ExploreByTouchHelper {
        public static CropBoundary viewIdToBoundary(int i) {
            if (i == 1) {
                return CropBoundary.TOP;
            }
            if (i == 2) {
                return CropBoundary.BOTTOM;
            }
            if (i == 3) {
                return CropBoundary.LEFT;
            }
            if (i != 4) {
                return CropBoundary.NONE;
            }
            return CropBoundary.RIGHT;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void getVisibleVirtualViews(ArrayList arrayList) {
            arrayList.add(1);
            arrayList.add(3);
            arrayList.add(4);
            arrayList.add(2);
        }

        public AccessibilityHelper() {
            super(CropView.this);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final int getVirtualViewAt(float f, float f2) {
            CropView cropView = CropView.this;
            float abs = Math.abs(f2 - cropView.fractionToVerticalPixels(cropView.mCrop.top));
            CropView cropView2 = CropView.this;
            if (abs < cropView2.mCropTouchMargin) {
                return 1;
            }
            float abs2 = Math.abs(f2 - cropView2.fractionToVerticalPixels(cropView2.mCrop.bottom));
            CropView cropView3 = CropView.this;
            if (abs2 < cropView3.mCropTouchMargin) {
                return 2;
            }
            if (f2 <= cropView3.fractionToVerticalPixels(cropView3.mCrop.top)) {
                return -1;
            }
            CropView cropView4 = CropView.this;
            if (f2 >= cropView4.fractionToVerticalPixels(cropView4.mCrop.bottom)) {
                return -1;
            }
            CropView cropView5 = CropView.this;
            float abs3 = Math.abs(f - cropView5.fractionToHorizontalPixels(cropView5.mCrop.left));
            CropView cropView6 = CropView.this;
            if (abs3 < cropView6.mCropTouchMargin) {
                return 3;
            }
            if (Math.abs(f - cropView6.fractionToHorizontalPixels(cropView6.mCrop.right)) < CropView.this.mCropTouchMargin) {
                return 4;
            }
            return -1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 != 4096 && i2 != 8192) {
                return false;
            }
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            CropView cropView = CropView.this;
            float pixelDistanceToFraction = cropView.pixelDistanceToFraction(cropView.mCropTouchMargin, viewIdToBoundary);
            if (i2 == 4096) {
                pixelDistanceToFraction = -pixelDistanceToFraction;
            }
            CropView cropView2 = CropView.this;
            cropView2.setBoundaryPosition(viewIdToBoundary, cropView2.getBoundaryPosition(viewIdToBoundary) + pixelDistanceToFraction);
            invalidateVirtualView(i);
            sendEventForVirtualView(i, 4);
            return true;
        }

        public final String getBoundaryContentDescription(CropBoundary cropBoundary) {
            int i;
            int ordinal = cropBoundary.ordinal();
            if (ordinal == 1) {
                i = 2131953236;
            } else if (ordinal == 2) {
                i = 2131953220;
            } else if (ordinal == 3) {
                i = 2131953229;
            } else if (ordinal != 4) {
                return "";
            } else {
                i = 2131953231;
            }
            Resources resources = CropView.this.getResources();
            CropView cropView = CropView.this;
            int i2 = CropView.$r8$clinit;
            return resources.getString(i, Integer.valueOf(Math.round(cropView.getBoundaryPosition(cropBoundary) * 100.0f)));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getBoundaryContentDescription(viewIdToBoundary(i)));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            Rect rect;
            CropView cropView;
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            accessibilityNodeInfoCompat.setContentDescription(getBoundaryContentDescription(viewIdToBoundary));
            if (CropView.isVertical(viewIdToBoundary)) {
                CropView cropView2 = CropView.this;
                float fractionToVerticalPixels = cropView2.fractionToVerticalPixels(cropView2.getBoundaryPosition(viewIdToBoundary));
                CropView cropView3 = CropView.this;
                rect = new Rect(0, (int) (fractionToVerticalPixels - cropView3.mCropTouchMargin), cropView3.getWidth(), (int) (fractionToVerticalPixels + CropView.this.mCropTouchMargin));
                int i2 = rect.top;
                if (i2 < 0) {
                    rect.offset(0, -i2);
                }
            } else {
                CropView cropView4 = CropView.this;
                float fractionToHorizontalPixels = cropView4.fractionToHorizontalPixels(cropView4.getBoundaryPosition(viewIdToBoundary));
                CropView cropView5 = CropView.this;
                float f = CropView.this.mCropTouchMargin;
                rect = new Rect((int) (fractionToHorizontalPixels - cropView5.mCropTouchMargin), (int) (cropView5.fractionToVerticalPixels(cropView5.mCrop.top) + f), (int) (fractionToHorizontalPixels + f), (int) (cropView.fractionToVerticalPixels(cropView.mCrop.bottom) - CropView.this.mCropTouchMargin));
            }
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            int[] iArr = new int[2];
            CropView.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            accessibilityNodeInfoCompat.mInfo.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setClassName(SeekBar.class.getName());
            accessibilityNodeInfoCompat.addAction(4096);
            accessibilityNodeInfoCompat.addAction(8192);
        }
    }

    /* loaded from: classes.dex */
    public enum CropBoundary {
        NONE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    /* loaded from: classes.dex */
    public interface CropInteractionListener {
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.android.systemui.screenshot.CropView.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public RectF mCrop;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mCrop = (RectF) parcel.readParcelable(ClassLoader.getSystemClassLoader());
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mCrop, 0);
        }
    }

    public CropView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCrop = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.mCurrentDraggingBoundary = CropBoundary.NONE;
        this.mEntranceInterpolation = 1.0f;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.CropView, 0, 0);
        Paint paint = new Paint();
        this.mShadePaint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(4, 0), obtainStyledAttributes.getInteger(3, 255)));
        Paint paint2 = new Paint();
        this.mContainerBackgroundPaint = paint2;
        paint2.setColor(obtainStyledAttributes.getColor(0, 0));
        Paint paint3 = new Paint();
        this.mHandlePaint = paint3;
        paint3.setColor(obtainStyledAttributes.getColor(1, -16777216));
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth(obtainStyledAttributes.getDimensionPixelSize(2, 20));
        obtainStyledAttributes.recycle();
        this.mCropTouchMargin = getResources().getDisplayMetrics().density * 24.0f;
        AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
        this.mExploreByTouchHelper = accessibilityHelper;
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);
    }

    public static boolean isVertical(CropBoundary cropBoundary) {
        if (cropBoundary == CropBoundary.TOP || cropBoundary == CropBoundary.BOTTOM) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.mExploreByTouchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean z;
        AccessibilityHelper accessibilityHelper = this.mExploreByTouchHelper;
        Objects.requireNonNull(accessibilityHelper);
        if (keyEvent.getAction() != 1) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 61) {
                int i = 66;
                if (keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            if (keyEvent.hasNoModifiers()) {
                                if (keyCode == 19) {
                                    i = 33;
                                } else if (keyCode == 21) {
                                    i = 17;
                                } else if (keyCode != 22) {
                                    i = 130;
                                }
                                int repeatCount = keyEvent.getRepeatCount() + 1;
                                int i2 = 0;
                                z = false;
                                while (i2 < repeatCount && accessibilityHelper.moveFocus(i, null)) {
                                    i2++;
                                    z = true;
                                }
                            }
                            break;
                    }
                }
                if (keyEvent.hasNoModifiers() && keyEvent.getRepeatCount() == 0) {
                    int i3 = accessibilityHelper.mKeyboardFocusedVirtualViewId;
                    if (i3 != Integer.MIN_VALUE) {
                        accessibilityHelper.onPerformActionForVirtualView(i3, 16, null);
                    }
                    z = true;
                }
            } else if (keyEvent.hasNoModifiers()) {
                z = accessibilityHelper.moveFocus(2, null);
            } else if (keyEvent.hasModifiers(1)) {
                z = accessibilityHelper.moveFocus(1, null);
            }
            if (!z || super.dispatchKeyEvent(keyEvent)) {
                return true;
            }
            return false;
        }
        z = false;
        if (!z) {
        }
        return true;
    }

    public final int fractionToVerticalPixels(float f) {
        return (int) ((f * ((getHeight() - this.mExtraTopPadding) - this.mExtraBottomPadding)) + this.mExtraTopPadding);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCrop = savedState.mCrop;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        CropBoundary cropBoundary;
        float f;
        float f2;
        CropBoundary cropBoundary2 = CropBoundary.NONE;
        int fractionToVerticalPixels = fractionToVerticalPixels(this.mCrop.top);
        int fractionToVerticalPixels2 = fractionToVerticalPixels(this.mCrop.bottom);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 5) {
                            if (actionMasked == 6 && this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != cropBoundary2) {
                                updateListener(1, motionEvent.getX(motionEvent.getActionIndex()));
                                return true;
                            }
                        } else if (this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != cropBoundary2) {
                            updateListener(0, motionEvent.getX(motionEvent.getActionIndex()));
                            return true;
                        }
                    }
                } else if (this.mCurrentDraggingBoundary != cropBoundary2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex >= 0) {
                        if (isVertical(this.mCurrentDraggingBoundary)) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = this.mStartingY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = this.mStartingX;
                        }
                        setBoundaryPosition(this.mCurrentDraggingBoundary, this.mMotionRange.clamp(Float.valueOf(this.mMovementStartValue + pixelDistanceToFraction((int) (f2 - f), this.mCurrentDraggingBoundary))).floatValue());
                        updateListener(2, motionEvent.getX(findPointerIndex));
                        invalidate();
                    }
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
            if (this.mCurrentDraggingBoundary != cropBoundary2) {
                int i = this.mActivePointerId;
                if (i == motionEvent.getPointerId(i)) {
                    updateListener(1, motionEvent.getX(0));
                    return true;
                }
            }
            return super.onTouchEvent(motionEvent);
        }
        int fractionToHorizontalPixels = fractionToHorizontalPixels(this.mCrop.left);
        int fractionToHorizontalPixels2 = fractionToHorizontalPixels(this.mCrop.right);
        float f3 = fractionToVerticalPixels;
        if (Math.abs(motionEvent.getY() - f3) < this.mCropTouchMargin) {
            cropBoundary = CropBoundary.TOP;
        } else {
            float f4 = fractionToVerticalPixels2;
            if (Math.abs(motionEvent.getY() - f4) < this.mCropTouchMargin) {
                cropBoundary = CropBoundary.BOTTOM;
            } else {
                if (motionEvent.getY() > f3 || motionEvent.getY() < f4) {
                    if (Math.abs(motionEvent.getX() - fractionToHorizontalPixels) < this.mCropTouchMargin) {
                        cropBoundary = CropBoundary.LEFT;
                    } else if (Math.abs(motionEvent.getX() - fractionToHorizontalPixels2) < this.mCropTouchMargin) {
                        cropBoundary = CropBoundary.RIGHT;
                    }
                }
                cropBoundary = cropBoundary2;
            }
        }
        this.mCurrentDraggingBoundary = cropBoundary;
        if (cropBoundary != cropBoundary2) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mStartingY = motionEvent.getY();
            this.mStartingX = motionEvent.getX();
            this.mMovementStartValue = getBoundaryPosition(this.mCurrentDraggingBoundary);
            updateListener(0, motionEvent.getX());
            this.mMotionRange = getAllowedValues(this.mCurrentDraggingBoundary);
        }
        return true;
    }

    public final void updateListener(int i, float f) {
        float f2;
        boolean z;
        boolean z2;
        if (this.mCropInteractionListener != null && isVertical(this.mCurrentDraggingBoundary)) {
            float boundaryPosition = getBoundaryPosition(this.mCurrentDraggingBoundary);
            boolean z3 = false;
            float f3 = 0.0f;
            boolean z4 = true;
            if (i == 0) {
                CropInteractionListener cropInteractionListener = this.mCropInteractionListener;
                CropBoundary cropBoundary = this.mCurrentDraggingBoundary;
                int fractionToVerticalPixels = fractionToVerticalPixels(boundaryPosition);
                RectF rectF = this.mCrop;
                MagnifierView magnifierView = (MagnifierView) cropInteractionListener;
                Objects.requireNonNull(magnifierView);
                magnifierView.mCropBoundary = cropBoundary;
                magnifierView.mLastCenter = (rectF.left + rectF.right) / 2.0f;
                if (f <= magnifierView.getParentWidth() / 2) {
                    z4 = false;
                }
                if (z4) {
                    f2 = 0.0f;
                } else {
                    f2 = magnifierView.getParentWidth() - magnifierView.getWidth();
                }
                magnifierView.mLastCropPosition = boundaryPosition;
                magnifierView.setTranslationY(fractionToVerticalPixels - (magnifierView.getHeight() / 2));
                magnifierView.setPivotX(magnifierView.getWidth() / 2);
                magnifierView.setPivotY(magnifierView.getHeight() / 2);
                magnifierView.setScaleX(0.2f);
                magnifierView.setScaleY(0.2f);
                magnifierView.setAlpha(0.0f);
                magnifierView.setTranslationX((magnifierView.getParentWidth() - magnifierView.getWidth()) / 2);
                magnifierView.setVisibility(0);
                ViewPropertyAnimator scaleY = magnifierView.animate().alpha(1.0f).translationX(f2).scaleX(1.0f).scaleY(1.0f);
                magnifierView.mTranslationAnimator = scaleY;
                scaleY.setListener(magnifierView.mTranslationAnimatorListener);
                magnifierView.mTranslationAnimator.start();
            } else if (i == 1) {
                MagnifierView magnifierView2 = (MagnifierView) this.mCropInteractionListener;
                Objects.requireNonNull(magnifierView2);
                magnifierView2.animate().alpha(0.0f).translationX((magnifierView2.getParentWidth() - magnifierView2.getWidth()) / 2).scaleX(0.2f).scaleY(0.2f).withEndAction(new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(magnifierView2, 5)).start();
            } else if (i == 2) {
                CropInteractionListener cropInteractionListener2 = this.mCropInteractionListener;
                int fractionToVerticalPixels2 = fractionToVerticalPixels(boundaryPosition);
                float f4 = this.mCrop.left;
                MagnifierView magnifierView3 = (MagnifierView) cropInteractionListener2;
                Objects.requireNonNull(magnifierView3);
                if (f > magnifierView3.getParentWidth() / 2) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    f3 = magnifierView3.getParentWidth() - magnifierView3.getWidth();
                }
                if (Math.abs(f - (magnifierView3.getParentWidth() / 2)) < magnifierView3.getParentWidth() / 10.0f) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (magnifierView3.getTranslationX() < (magnifierView3.getParentWidth() - magnifierView3.getWidth()) / 2) {
                    z3 = true;
                }
                if (!z2 && z3 != z && magnifierView3.mTranslationAnimator == null) {
                    ViewPropertyAnimator translationX = magnifierView3.animate().translationX(f3);
                    magnifierView3.mTranslationAnimator = translationX;
                    translationX.setListener(magnifierView3.mTranslationAnimatorListener);
                    magnifierView3.mTranslationAnimator.start();
                }
                magnifierView3.mLastCropPosition = boundaryPosition;
                magnifierView3.setTranslationY(fractionToVerticalPixels2 - (magnifierView3.getHeight() / 2));
                magnifierView3.invalidate();
            }
        }
    }

    public final void drawHorizontalHandle(Canvas canvas, float f, boolean z) {
        float f2;
        float fractionToVerticalPixels = fractionToVerticalPixels(f);
        canvas.drawLine(fractionToHorizontalPixels(this.mCrop.left), fractionToVerticalPixels, fractionToHorizontalPixels(this.mCrop.right), fractionToVerticalPixels, this.mHandlePaint);
        float f3 = getResources().getDisplayMetrics().density * 8.0f;
        float fractionToHorizontalPixels = (fractionToHorizontalPixels(this.mCrop.right) + fractionToHorizontalPixels(this.mCrop.left)) / 2;
        float f4 = fractionToHorizontalPixels - f3;
        float f5 = fractionToVerticalPixels - f3;
        float f6 = fractionToHorizontalPixels + f3;
        float f7 = fractionToVerticalPixels + f3;
        if (z) {
            f2 = 180.0f;
        } else {
            f2 = 0.0f;
        }
        canvas.drawArc(f4, f5, f6, f7, f2, 180.0f, true, this.mHandlePaint);
    }

    public final void drawShade(Canvas canvas, float f, float f2, float f3, float f4) {
        canvas.drawRect(fractionToHorizontalPixels(f), fractionToVerticalPixels(f2), fractionToHorizontalPixels(f3), fractionToVerticalPixels(f4), this.mShadePaint);
    }

    public final void drawVerticalHandle(Canvas canvas, float f, boolean z) {
        float f2;
        float fractionToHorizontalPixels = fractionToHorizontalPixels(f);
        canvas.drawLine(fractionToHorizontalPixels, fractionToVerticalPixels(this.mCrop.top), fractionToHorizontalPixels, fractionToVerticalPixels(this.mCrop.bottom), this.mHandlePaint);
        float f3 = getResources().getDisplayMetrics().density * 8.0f;
        float f4 = fractionToHorizontalPixels - f3;
        float fractionToVerticalPixels = (fractionToVerticalPixels(getBoundaryPosition(CropBoundary.BOTTOM)) + fractionToVerticalPixels(getBoundaryPosition(CropBoundary.TOP))) / 2;
        float f5 = fractionToVerticalPixels - f3;
        float f6 = fractionToHorizontalPixels + f3;
        float f7 = fractionToVerticalPixels + f3;
        if (z) {
            f2 = 90.0f;
        } else {
            f2 = 270.0f;
        }
        canvas.drawArc(f4, f5, f6, f7, f2, 180.0f, true, this.mHandlePaint);
    }

    public final int fractionToHorizontalPixels(float f) {
        int width = getWidth();
        int i = this.mImageWidth;
        return (int) ((f * i) + ((width - i) / 2));
    }

    public final Range getAllowedValues(CropBoundary cropBoundary) {
        int ordinal = cropBoundary.ordinal();
        if (ordinal == 1) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.bottom - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.BOTTOM)));
        }
        if (ordinal == 2) {
            return new Range(Float.valueOf(pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.TOP) + this.mCrop.top), Float.valueOf(1.0f));
        } else if (ordinal == 3) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.right - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.RIGHT)));
        } else {
            if (ordinal != 4) {
                return null;
            }
            return new Range(Float.valueOf(pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.LEFT) + this.mCrop.left), Float.valueOf(1.0f));
        }
    }

    public final float getBoundaryPosition(CropBoundary cropBoundary) {
        int ordinal = cropBoundary.ordinal();
        if (ordinal == 1) {
            return this.mCrop.top;
        }
        if (ordinal == 2) {
            return this.mCrop.bottom;
        }
        if (ordinal == 3) {
            return this.mCrop.left;
        }
        if (ordinal != 4) {
            return 0.0f;
        }
        return this.mCrop.right;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lerp = MathUtils.lerp(this.mCrop.top, 0.0f, this.mEntranceInterpolation);
        float lerp2 = MathUtils.lerp(this.mCrop.bottom, 1.0f, this.mEntranceInterpolation);
        drawShade(canvas, 0.0f, lerp, 1.0f, this.mCrop.top);
        drawShade(canvas, 0.0f, this.mCrop.bottom, 1.0f, lerp2);
        RectF rectF = this.mCrop;
        drawShade(canvas, 0.0f, rectF.top, rectF.left, rectF.bottom);
        RectF rectF2 = this.mCrop;
        drawShade(canvas, rectF2.right, rectF2.top, 1.0f, rectF2.bottom);
        canvas.drawRect(fractionToHorizontalPixels(0.0f), fractionToVerticalPixels(0.0f), fractionToHorizontalPixels(1.0f), fractionToVerticalPixels(lerp), this.mContainerBackgroundPaint);
        canvas.drawRect(fractionToHorizontalPixels(0.0f), fractionToVerticalPixels(lerp2), fractionToHorizontalPixels(1.0f), fractionToVerticalPixels(1.0f), this.mContainerBackgroundPaint);
        this.mHandlePaint.setAlpha((int) (this.mEntranceInterpolation * 255.0f));
        drawHorizontalHandle(canvas, this.mCrop.top, true);
        drawHorizontalHandle(canvas, this.mCrop.bottom, false);
        drawVerticalHandle(canvas, this.mCrop.left, true);
        drawVerticalHandle(canvas, this.mCrop.right, false);
    }

    @Override // android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        AccessibilityHelper accessibilityHelper = this.mExploreByTouchHelper;
        Objects.requireNonNull(accessibilityHelper);
        int i2 = accessibilityHelper.mKeyboardFocusedVirtualViewId;
        if (i2 != Integer.MIN_VALUE) {
            accessibilityHelper.clearKeyboardFocusForVirtualView(i2);
        }
        if (z) {
            accessibilityHelper.moveFocus(i, rect);
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mCrop = this.mCrop;
        return savedState;
    }

    public final float pixelDistanceToFraction(float f, CropBoundary cropBoundary) {
        int i;
        if (isVertical(cropBoundary)) {
            i = (getHeight() - this.mExtraTopPadding) - this.mExtraBottomPadding;
        } else {
            i = this.mImageWidth;
        }
        return f / i;
    }

    public final void setBoundaryPosition(CropBoundary cropBoundary, float f) {
        float floatValue = ((Float) getAllowedValues(cropBoundary).clamp(Float.valueOf(f))).floatValue();
        int ordinal = cropBoundary.ordinal();
        if (ordinal == 0) {
            Log.w("CropView", "No boundary selected");
        } else if (ordinal == 1) {
            this.mCrop.top = floatValue;
        } else if (ordinal == 2) {
            this.mCrop.bottom = floatValue;
        } else if (ordinal == 3) {
            this.mCrop.left = floatValue;
        } else if (ordinal == 4) {
            this.mCrop.right = floatValue;
        }
        invalidate();
    }
}
