package com.google.android.material.slider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.SeekBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.leanback.R$drawable;
import androidx.mediarouter.R$bool;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewOverlayApi18;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.slider.BaseOnChangeListener;
import com.google.android.material.slider.BaseOnSliderTouchListener;
import com.google.android.material.slider.BaseSlider;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import com.google.android.material.tooltip.TooltipDrawable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
abstract class BaseSlider<S extends BaseSlider<S, L, T>, L extends BaseOnChangeListener<S>, T extends BaseOnSliderTouchListener<S>> extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BaseSlider<S, L, T>.AccessibilityEventSender accessibilityEventSender;
    public final AccessibilityHelper accessibilityHelper;
    public final AccessibilityManager accessibilityManager;
    public int activeThumbIdx;
    public final Paint activeTicksPaint;
    public final Paint activeTrackPaint;
    public final ArrayList changeListeners;
    public int defaultThumbRadius;
    public boolean dirtyConfig;
    public int focusedThumbIdx;
    public boolean forceDrawCompatHalo;
    public ColorStateList haloColor;
    public final Paint haloPaint;
    public int haloRadius;
    public final Paint inactiveTicksPaint;
    public final Paint inactiveTrackPaint;
    public boolean isLongPress;
    public int labelBehavior;
    public final AnonymousClass1 labelMaker;
    public int labelPadding;
    public final ArrayList labels;
    public boolean labelsAreAnimatedIn;
    public ValueAnimator labelsInAnimator;
    public ValueAnimator labelsOutAnimator;
    public MotionEvent lastEvent;
    public int minTrackSidePadding;
    public final int scaledTouchSlop;
    public int separationUnit;
    public float stepSize;
    public final MaterialShapeDrawable thumbDrawable;
    public boolean thumbIsPressed;
    public final Paint thumbPaint;
    public int thumbRadius;
    public ColorStateList tickColorActive;
    public ColorStateList tickColorInactive;
    public boolean tickVisible;
    public float[] ticksCoordinates;
    public float touchDownX;
    public final ArrayList touchListeners;
    public float touchPosition;
    public ColorStateList trackColorActive;
    public ColorStateList trackColorInactive;
    public int trackHeight;
    public int trackSidePadding;
    public int trackTop;
    public int trackWidth;
    public float valueFrom;
    public float valueTo;
    public ArrayList<Float> values;
    public int widgetHeight;

    /* renamed from: com.google.android.material.slider.BaseSlider$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 {
        public final /* synthetic */ AttributeSet val$attrs;
        public final /* synthetic */ int val$defStyleAttr = 2130969744;

        public AnonymousClass1(AttributeSet attributeSet) {
            this.val$attrs = attributeSet;
        }
    }

    /* loaded from: classes.dex */
    public class AccessibilityEventSender implements Runnable {
        public int virtualViewId = -1;

        public AccessibilityEventSender() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            BaseSlider.this.accessibilityHelper.sendEventForVirtualView(this.virtualViewId, 4);
        }
    }

    /* loaded from: classes.dex */
    public static class AccessibilityHelper extends ExploreByTouchHelper {
        public final BaseSlider<?, ?, ?> slider;
        public Rect virtualViewBounds = new Rect();

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final int getVirtualViewAt(float f, float f2) {
            for (int i = 0; i < this.slider.getValues().size(); i++) {
                this.slider.updateBoundsForVirturalViewId(i, this.virtualViewBounds);
                if (this.virtualViewBounds.contains((int) f, (int) f2)) {
                    return i;
                }
            }
            return -1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void getVisibleVirtualViews(ArrayList arrayList) {
            for (int i = 0; i < this.slider.getValues().size(); i++) {
                arrayList.add(Integer.valueOf(i));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            float f;
            float f2;
            if (!this.slider.isEnabled()) {
                return false;
            }
            if (i2 == 4096 || i2 == 8192) {
                BaseSlider<?, ?, ?> baseSlider = this.slider;
                int i3 = BaseSlider.$r8$clinit;
                Objects.requireNonNull(baseSlider);
                float f3 = baseSlider.stepSize;
                if (f3 == 0.0f) {
                    f3 = 1.0f;
                }
                if ((baseSlider.valueTo - baseSlider.valueFrom) / f3 > 20) {
                    f3 *= Math.round(f / f2);
                }
                if (i2 == 8192) {
                    f3 = -f3;
                }
                if (this.slider.isRtl()) {
                    f3 = -f3;
                }
                float floatValue = ((Float) this.slider.getValues().get(i)).floatValue() + f3;
                BaseSlider<?, ?, ?> baseSlider2 = this.slider;
                Objects.requireNonNull(baseSlider2);
                float f4 = baseSlider2.valueFrom;
                BaseSlider<?, ?, ?> baseSlider3 = this.slider;
                Objects.requireNonNull(baseSlider3);
                if (!this.slider.snapThumbToValue(i, AtomicFU.clamp(floatValue, f4, baseSlider3.valueTo))) {
                    return false;
                }
                this.slider.updateHaloHotspot();
                this.slider.postInvalidate();
                invalidateVirtualView(i);
                return true;
            }
            if (i2 == 16908349 && bundle != null && bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) {
                float f5 = bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE");
                BaseSlider<?, ?, ?> baseSlider4 = this.slider;
                int i4 = BaseSlider.$r8$clinit;
                if (baseSlider4.snapThumbToValue(i, f5)) {
                    this.slider.updateHaloHotspot();
                    this.slider.postInvalidate();
                    invalidateVirtualView(i);
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            String str;
            String str2;
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SET_PROGRESS);
            ArrayList values = this.slider.getValues();
            float floatValue = ((Float) values.get(i)).floatValue();
            BaseSlider<?, ?, ?> baseSlider = this.slider;
            Objects.requireNonNull(baseSlider);
            float f = baseSlider.valueFrom;
            BaseSlider<?, ?, ?> baseSlider2 = this.slider;
            Objects.requireNonNull(baseSlider2);
            float f2 = baseSlider2.valueTo;
            if (this.slider.isEnabled()) {
                if (floatValue > f) {
                    accessibilityNodeInfoCompat.addAction(8192);
                }
                if (floatValue < f2) {
                    accessibilityNodeInfoCompat.addAction(4096);
                }
            }
            accessibilityNodeInfoCompat.mInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(1, f, f2, floatValue));
            accessibilityNodeInfoCompat.setClassName(SeekBar.class.getName());
            StringBuilder sb = new StringBuilder();
            if (this.slider.getContentDescription() != null) {
                sb.append(this.slider.getContentDescription());
                sb.append(",");
            }
            if (values.size() > 1) {
                if (i == this.slider.getValues().size() - 1) {
                    str = this.slider.getContext().getString(2131952722);
                } else if (i == 0) {
                    str = this.slider.getContext().getString(2131952723);
                } else {
                    str = "";
                }
                sb.append(str);
                Objects.requireNonNull(this.slider);
                if (((int) floatValue) == floatValue) {
                    str2 = "%.0f";
                } else {
                    str2 = "%.2f";
                }
                sb.append(String.format(str2, Float.valueOf(floatValue)));
            }
            accessibilityNodeInfoCompat.setContentDescription(sb.toString());
            this.slider.updateBoundsForVirturalViewId(i, this.virtualViewBounds);
            accessibilityNodeInfoCompat.setBoundsInParent(this.virtualViewBounds);
        }

        public AccessibilityHelper(BaseSlider<?, ?, ?> baseSlider) {
            super(baseSlider);
            this.slider = baseSlider;
        }
    }

    /* loaded from: classes.dex */
    public static class SliderState extends View.BaseSavedState {
        public static final Parcelable.Creator<SliderState> CREATOR = new Parcelable.Creator<SliderState>() { // from class: com.google.android.material.slider.BaseSlider.SliderState.1
            @Override // android.os.Parcelable.Creator
            public final SliderState createFromParcel(Parcel parcel) {
                return new SliderState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SliderState[] newArray(int i) {
                return new SliderState[i];
            }
        };
        public boolean hasFocus;
        public float stepSize;
        public float valueFrom;
        public float valueTo;
        public ArrayList<Float> values;

        public SliderState(Parcelable parcelable) {
            super(parcelable);
        }

        public SliderState(Parcel parcel) {
            super(parcel);
            this.valueFrom = parcel.readFloat();
            this.valueTo = parcel.readFloat();
            ArrayList<Float> arrayList = new ArrayList<>();
            this.values = arrayList;
            parcel.readList(arrayList, Float.class.getClassLoader());
            this.stepSize = parcel.readFloat();
            this.hasFocus = parcel.createBooleanArray()[0];
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.valueFrom);
            parcel.writeFloat(this.valueTo);
            parcel.writeList(this.values);
            parcel.writeFloat(this.stepSize);
            parcel.writeBooleanArray(new boolean[]{this.hasFocus});
        }
    }

    public BaseSlider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final ValueAnimator createLabelAnimator(boolean z) {
        float f;
        ValueAnimator valueAnimator;
        long j;
        TimeInterpolator timeInterpolator;
        float f2 = 0.0f;
        if (z) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        if (z) {
            valueAnimator = this.labelsOutAnimator;
        } else {
            valueAnimator = this.labelsInAnimator;
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {
            f = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            valueAnimator.cancel();
        }
        if (z) {
            f2 = 1.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, f2);
        if (z) {
            j = 83;
        } else {
            j = 117;
        }
        ofFloat.setDuration(j);
        if (z) {
            timeInterpolator = AnimationUtils.DECELERATE_INTERPOLATOR;
        } else {
            timeInterpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
        }
        ofFloat.setInterpolator(timeInterpolator);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.slider.BaseSlider.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                float floatValue = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                Iterator it = BaseSlider.this.labels.iterator();
                while (it.hasNext()) {
                    TooltipDrawable tooltipDrawable = (TooltipDrawable) it.next();
                    Objects.requireNonNull(tooltipDrawable);
                    tooltipDrawable.tooltipPivotY = 1.2f;
                    tooltipDrawable.tooltipScaleX = floatValue;
                    tooltipDrawable.tooltipScaleY = floatValue;
                    tooltipDrawable.labelOpacity = AnimationUtils.lerp(0.0f, 1.0f, 0.19f, 1.0f, floatValue);
                    tooltipDrawable.invalidateSelf();
                }
                BaseSlider baseSlider = BaseSlider.this;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.postInvalidateOnAnimation(baseSlider);
            }
        });
        return ofFloat;
    }

    public float getMinSeparation() {
        return 0.0f;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        this.isLongPress = false;
        return super.onKeyUp(i, keyEvent);
    }

    public final void setValueForLabel(TooltipDrawable tooltipDrawable, float f) {
        String str;
        ViewOverlayApi18 viewOverlayApi18;
        if (((int) f) == f) {
            str = "%.0f";
        } else {
            str = "%.2f";
        }
        String format = String.format(str, Float.valueOf(f));
        Objects.requireNonNull(tooltipDrawable);
        if (!TextUtils.equals(tooltipDrawable.text, format)) {
            tooltipDrawable.text = format;
            TextDrawableHelper textDrawableHelper = tooltipDrawable.textDrawableHelper;
            Objects.requireNonNull(textDrawableHelper);
            textDrawableHelper.textWidthDirty = true;
            tooltipDrawable.invalidateSelf();
        }
        int normalizeValue = (this.trackSidePadding + ((int) (normalizeValue(f) * this.trackWidth))) - (tooltipDrawable.getIntrinsicWidth() / 2);
        int calculateTop = calculateTop() - (this.labelPadding + this.thumbRadius);
        tooltipDrawable.setBounds(normalizeValue, calculateTop - tooltipDrawable.getIntrinsicHeight(), tooltipDrawable.getIntrinsicWidth() + normalizeValue, calculateTop);
        Rect rect = new Rect(tooltipDrawable.getBounds());
        DescendantOffsetUtils.offsetDescendantRect(ViewUtils.getContentView(this), this, rect);
        tooltipDrawable.setBounds(rect);
        ViewGroup contentView = ViewUtils.getContentView(this);
        if (contentView == null) {
            viewOverlayApi18 = null;
        } else {
            viewOverlayApi18 = new ViewOverlayApi18(contentView);
        }
        Objects.requireNonNull(viewOverlayApi18);
        viewOverlayApi18.viewOverlay.add(tooltipDrawable);
    }

    public BaseSlider(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130969744, 2132018702), attributeSet, 2130969744);
        Paint paint;
        this.labels = new ArrayList();
        this.changeListeners = new ArrayList();
        this.touchListeners = new ArrayList();
        this.labelsAreAnimatedIn = false;
        this.thumbIsPressed = false;
        this.values = new ArrayList<>();
        this.activeThumbIdx = -1;
        this.focusedThumbIdx = -1;
        this.stepSize = 0.0f;
        this.tickVisible = true;
        this.isLongPress = false;
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.thumbDrawable = materialShapeDrawable;
        this.separationUnit = 0;
        Context context2 = getContext();
        Paint paint2 = new Paint();
        this.inactiveTrackPaint = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        Paint paint3 = new Paint();
        this.activeTrackPaint = paint3;
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeCap(Paint.Cap.ROUND);
        Paint paint4 = new Paint(1);
        this.thumbPaint = paint4;
        paint4.setStyle(Paint.Style.FILL);
        paint4.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Paint paint5 = new Paint(1);
        this.haloPaint = paint5;
        paint5.setStyle(Paint.Style.FILL);
        Paint paint6 = new Paint();
        this.inactiveTicksPaint = paint6;
        paint6.setStyle(Paint.Style.STROKE);
        paint6.setStrokeCap(Paint.Cap.ROUND);
        Paint paint7 = new Paint();
        this.activeTicksPaint = paint7;
        paint7.setStyle(Paint.Style.STROKE);
        paint7.setStrokeCap(Paint.Cap.ROUND);
        Resources resources = context2.getResources();
        this.widgetHeight = resources.getDimensionPixelSize(2131166556);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(2131166554);
        this.minTrackSidePadding = dimensionPixelOffset;
        this.trackSidePadding = dimensionPixelOffset;
        this.defaultThumbRadius = resources.getDimensionPixelSize(2131166552);
        this.trackTop = resources.getDimensionPixelOffset(2131166555);
        this.labelPadding = resources.getDimensionPixelSize(2131166548);
        this.labelMaker = new AnonymousClass1(attributeSet);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.Slider, 2130969744, 2132018702, new int[0]);
        this.valueFrom = obtainStyledAttributes.getFloat(3, 0.0f);
        this.valueTo = obtainStyledAttributes.getFloat(4, 1.0f);
        setValues(Float.valueOf(this.valueFrom));
        this.stepSize = obtainStyledAttributes.getFloat(2, 0.0f);
        int i2 = 18;
        boolean hasValue = obtainStyledAttributes.hasValue(18);
        int i3 = hasValue ? 18 : 20;
        i2 = !hasValue ? 19 : i2;
        ColorStateList colorStateList = MaterialResources.getColorStateList(context2, obtainStyledAttributes, i3);
        colorStateList = colorStateList == null ? AppCompatResources.getColorStateList(context2, 2131100373) : colorStateList;
        if (!colorStateList.equals(this.trackColorInactive)) {
            this.trackColorInactive = colorStateList;
            paint2.setColor(getColorForState(colorStateList));
            invalidate();
        }
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, i2);
        colorStateList2 = colorStateList2 == null ? AppCompatResources.getColorStateList(context2, 2131100370) : colorStateList2;
        if (!colorStateList2.equals(this.trackColorActive)) {
            this.trackColorActive = colorStateList2;
            paint3.setColor(getColorForState(colorStateList2));
            invalidate();
        }
        materialShapeDrawable.setFillColor(MaterialResources.getColorStateList(context2, obtainStyledAttributes, 9));
        if (obtainStyledAttributes.hasValue(12)) {
            materialShapeDrawable.setStrokeColor(MaterialResources.getColorStateList(context2, obtainStyledAttributes, 12));
            postInvalidate();
        }
        materialShapeDrawable.drawableState.strokeWidth = obtainStyledAttributes.getDimension(13, 0.0f);
        materialShapeDrawable.invalidateSelf();
        postInvalidate();
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 5);
        colorStateList3 = colorStateList3 == null ? AppCompatResources.getColorStateList(context2, 2131100371) : colorStateList3;
        if (!colorStateList3.equals(this.haloColor)) {
            this.haloColor = colorStateList3;
            Drawable background = getBackground();
            if (shouldDrawCompatHalo() || !(background instanceof RippleDrawable)) {
                paint5.setColor(getColorForState(colorStateList3));
                paint5.setAlpha(63);
                invalidate();
            } else {
                ((RippleDrawable) background).setColor(colorStateList3);
            }
        }
        this.tickVisible = obtainStyledAttributes.getBoolean(17, true);
        int i4 = 14;
        boolean hasValue2 = obtainStyledAttributes.hasValue(14);
        int i5 = hasValue2 ? 14 : 16;
        i4 = !hasValue2 ? 15 : i4;
        ColorStateList colorStateList4 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, i5);
        colorStateList4 = colorStateList4 == null ? AppCompatResources.getColorStateList(context2, 2131100372) : colorStateList4;
        if (!colorStateList4.equals(this.tickColorInactive)) {
            this.tickColorInactive = colorStateList4;
            paint6.setColor(getColorForState(colorStateList4));
            invalidate();
        }
        ColorStateList colorStateList5 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, i4);
        colorStateList5 = colorStateList5 == null ? AppCompatResources.getColorStateList(context2, 2131100369) : colorStateList5;
        if (colorStateList5.equals(this.tickColorActive)) {
            paint = paint7;
        } else {
            this.tickColorActive = colorStateList5;
            paint = paint7;
            paint.setColor(getColorForState(colorStateList5));
            invalidate();
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(11, 0);
        if (dimensionPixelSize != this.thumbRadius) {
            this.thumbRadius = dimensionPixelSize;
            this.trackSidePadding = this.minTrackSidePadding + Math.max(dimensionPixelSize - this.defaultThumbRadius, 0);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api19Impl.isLaidOut(this)) {
                this.trackWidth = Math.max(getWidth() - (this.trackSidePadding * 2), 0);
                maybeCalculateTicksCoordinates();
            }
            ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder();
            R$drawable createCornerTreatment = R$bool.createCornerTreatment(0);
            builder.topLeftCorner = createCornerTreatment;
            ShapeAppearanceModel.Builder.compatCornerTreatmentSize(createCornerTreatment);
            builder.topRightCorner = createCornerTreatment;
            ShapeAppearanceModel.Builder.compatCornerTreatmentSize(createCornerTreatment);
            builder.bottomRightCorner = createCornerTreatment;
            ShapeAppearanceModel.Builder.compatCornerTreatmentSize(createCornerTreatment);
            builder.bottomLeftCorner = createCornerTreatment;
            ShapeAppearanceModel.Builder.compatCornerTreatmentSize(createCornerTreatment);
            builder.setAllCornerSizes(this.thumbRadius);
            materialShapeDrawable.setShapeAppearanceModel(new ShapeAppearanceModel(builder));
            int i6 = this.thumbRadius * 2;
            materialShapeDrawable.setBounds(0, 0, i6, i6);
            postInvalidate();
        }
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(6, 0);
        if (dimensionPixelSize2 != this.haloRadius) {
            this.haloRadius = dimensionPixelSize2;
            Drawable background2 = getBackground();
            if (shouldDrawCompatHalo() || !(background2 instanceof RippleDrawable)) {
                postInvalidate();
            } else {
                ((RippleDrawable) background2).setRadius(this.haloRadius);
            }
        }
        materialShapeDrawable.setElevation(obtainStyledAttributes.getDimension(10, 0.0f));
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(21, 0);
        if (this.trackHeight != dimensionPixelSize3) {
            this.trackHeight = dimensionPixelSize3;
            paint2.setStrokeWidth(dimensionPixelSize3);
            paint3.setStrokeWidth(this.trackHeight);
            paint6.setStrokeWidth(this.trackHeight / 2.0f);
            paint.setStrokeWidth(this.trackHeight / 2.0f);
            postInvalidate();
        }
        this.labelBehavior = obtainStyledAttributes.getInt(7, 0);
        if (!obtainStyledAttributes.getBoolean(0, true)) {
            setEnabled(false);
        }
        obtainStyledAttributes.recycle();
        setFocusable(true);
        setClickable(true);
        materialShapeDrawable.setShadowCompatibilityMode();
        this.scaledTouchSlop = ViewConfiguration.get(context2).getScaledTouchSlop();
        AccessibilityHelper accessibilityHelper = new AccessibilityHelper(this);
        this.accessibilityHelper = accessibilityHelper;
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);
        this.accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
    }

    public final int calculateTop() {
        int i = this.trackTop;
        int i2 = 0;
        if (this.labelBehavior == 1) {
            i2 = ((TooltipDrawable) this.labels.get(0)).getIntrinsicHeight();
        }
        return i + i2;
    }

    @Override // android.view.View
    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.accessibilityHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    public final void ensureLabelsRemoved() {
        if (this.labelsAreAnimatedIn) {
            this.labelsAreAnimatedIn = false;
            ValueAnimator createLabelAnimator = createLabelAnimator(false);
            this.labelsOutAnimator = createLabelAnimator;
            this.labelsInAnimator = null;
            createLabelAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.slider.BaseSlider.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ViewOverlayApi18 viewOverlayApi18;
                    super.onAnimationEnd(animator);
                    Iterator it = BaseSlider.this.labels.iterator();
                    while (it.hasNext()) {
                        TooltipDrawable tooltipDrawable = (TooltipDrawable) it.next();
                        ViewGroup contentView = ViewUtils.getContentView(BaseSlider.this);
                        if (contentView == null) {
                            viewOverlayApi18 = null;
                        } else {
                            viewOverlayApi18 = new ViewOverlayApi18(contentView);
                        }
                        Objects.requireNonNull(viewOverlayApi18);
                        viewOverlayApi18.viewOverlay.remove(tooltipDrawable);
                    }
                }
            });
            this.labelsOutAnimator.start();
        }
    }

    @Override // android.view.View
    public final CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        AccessibilityHelper accessibilityHelper = this.accessibilityHelper;
        Objects.requireNonNull(accessibilityHelper);
        return accessibilityHelper.mAccessibilityFocusedVirtualViewId;
    }

    public ArrayList getValues() {
        return new ArrayList(this.values);
    }

    public final boolean isMultipleOfStepSize(float f) {
        double doubleValue = new BigDecimal(Float.toString(f)).divide(new BigDecimal(Float.toString(this.stepSize)), MathContext.DECIMAL64).doubleValue();
        if (Math.abs(Math.round(doubleValue) - doubleValue) < 1.0E-4d) {
            return true;
        }
        return false;
    }

    public final boolean isRtl() {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api17Impl.getLayoutDirection(this) == 1) {
            return true;
        }
        return false;
    }

    public final void maybeCalculateTicksCoordinates() {
        if (this.stepSize > 0.0f) {
            validateConfigurationIfDirty();
            int min = Math.min((int) (((this.valueTo - this.valueFrom) / this.stepSize) + 1.0f), (this.trackWidth / (this.trackHeight * 2)) + 1);
            float[] fArr = this.ticksCoordinates;
            if (fArr == null || fArr.length != min * 2) {
                this.ticksCoordinates = new float[min * 2];
            }
            float f = this.trackWidth / (min - 1);
            for (int i = 0; i < min * 2; i += 2) {
                float[] fArr2 = this.ticksCoordinates;
                fArr2[i] = ((i / 2) * f) + this.trackSidePadding;
                fArr2[i + 1] = calculateTop();
            }
        }
    }

    public final boolean moveFocus(int i) {
        int i2 = this.focusedThumbIdx;
        long j = i2 + i;
        long size = this.values.size() - 1;
        if (j < 0) {
            j = 0;
        } else if (j > size) {
            j = size;
        }
        int i3 = (int) j;
        this.focusedThumbIdx = i3;
        if (i3 == i2) {
            return false;
        }
        if (this.activeThumbIdx != -1) {
            this.activeThumbIdx = i3;
        }
        updateHaloHotspot();
        postInvalidate();
        return true;
    }

    public final float normalizeValue(float f) {
        float f2 = this.valueFrom;
        float f3 = (f - f2) / (this.valueTo - f2);
        if (isRtl()) {
            return 1.0f - f3;
        }
        return f3;
    }

    @Override // android.view.View
    public final void onDetachedFromWindow() {
        ViewOverlayApi18 viewOverlayApi18;
        BaseSlider<S, L, T>.AccessibilityEventSender accessibilityEventSender = this.accessibilityEventSender;
        if (accessibilityEventSender != null) {
            removeCallbacks(accessibilityEventSender);
        }
        this.labelsAreAnimatedIn = false;
        Iterator it = this.labels.iterator();
        while (it.hasNext()) {
            TooltipDrawable tooltipDrawable = (TooltipDrawable) it.next();
            ViewGroup contentView = ViewUtils.getContentView(this);
            if (contentView == null) {
                viewOverlayApi18 = null;
            } else {
                viewOverlayApi18 = new ViewOverlayApi18(contentView);
            }
            if (viewOverlayApi18 != null) {
                viewOverlayApi18.viewOverlay.remove(tooltipDrawable);
                ViewGroup contentView2 = ViewUtils.getContentView(this);
                if (contentView2 == null) {
                    Objects.requireNonNull(tooltipDrawable);
                } else {
                    contentView2.removeOnLayoutChangeListener(tooltipDrawable.attachedViewLayoutChangeListener);
                }
            }
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        if (this.dirtyConfig) {
            validateConfigurationIfDirty();
            maybeCalculateTicksCoordinates();
        }
        super.onDraw(canvas);
        int calculateTop = calculateTop();
        int i = this.trackWidth;
        float[] activeRange = getActiveRange();
        int i2 = this.trackSidePadding;
        float f = i;
        float f2 = (activeRange[1] * f) + i2;
        float f3 = i2 + i;
        if (f2 < f3) {
            float f4 = calculateTop;
            canvas.drawLine(f2, f4, f3, f4, this.inactiveTrackPaint);
        }
        float f5 = this.trackSidePadding;
        float f6 = (activeRange[0] * f) + f5;
        if (f6 > f5) {
            float f7 = calculateTop;
            canvas.drawLine(f5, f7, f6, f7, this.inactiveTrackPaint);
        }
        if (((Float) Collections.max(getValues())).floatValue() > this.valueFrom) {
            int i3 = this.trackWidth;
            float[] activeRange2 = getActiveRange();
            float f8 = this.trackSidePadding;
            float f9 = i3;
            float f10 = calculateTop;
            canvas.drawLine((activeRange2[0] * f9) + f8, f10, (activeRange2[1] * f9) + f8, f10, this.activeTrackPaint);
        }
        if (this.tickVisible && this.stepSize > 0.0f) {
            float[] activeRange3 = getActiveRange();
            int round = Math.round(activeRange3[0] * ((this.ticksCoordinates.length / 2) - 1));
            int round2 = Math.round(activeRange3[1] * ((this.ticksCoordinates.length / 2) - 1));
            int i4 = round * 2;
            canvas.drawPoints(this.ticksCoordinates, 0, i4, this.inactiveTicksPaint);
            int i5 = round2 * 2;
            canvas.drawPoints(this.ticksCoordinates, i4, i5 - i4, this.activeTicksPaint);
            float[] fArr = this.ticksCoordinates;
            canvas.drawPoints(fArr, i5, fArr.length - i5, this.inactiveTicksPaint);
        }
        if ((this.thumbIsPressed || isFocused()) && isEnabled()) {
            int i6 = this.trackWidth;
            if (shouldDrawCompatHalo()) {
                canvas.drawCircle((int) ((normalizeValue(this.values.get(this.focusedThumbIdx).floatValue()) * i6) + this.trackSidePadding), calculateTop, this.haloRadius, this.haloPaint);
            }
            if (!(this.activeThumbIdx == -1 || this.labelBehavior == 2)) {
                if (!this.labelsAreAnimatedIn) {
                    this.labelsAreAnimatedIn = true;
                    ValueAnimator createLabelAnimator = createLabelAnimator(true);
                    this.labelsInAnimator = createLabelAnimator;
                    this.labelsOutAnimator = null;
                    createLabelAnimator.start();
                }
                Iterator it = this.labels.iterator();
                for (int i7 = 0; i7 < this.values.size() && it.hasNext(); i7++) {
                    if (i7 != this.focusedThumbIdx) {
                        setValueForLabel((TooltipDrawable) it.next(), this.values.get(i7).floatValue());
                    }
                }
                if (it.hasNext()) {
                    setValueForLabel((TooltipDrawable) it.next(), this.values.get(this.focusedThumbIdx).floatValue());
                } else {
                    throw new IllegalStateException(String.format("Not enough labels(%d) to display all the values(%d)", Integer.valueOf(this.labels.size()), Integer.valueOf(this.values.size())));
                }
            }
        }
        int i8 = this.trackWidth;
        if (!isEnabled()) {
            Iterator<Float> it2 = this.values.iterator();
            while (it2.hasNext()) {
                canvas.drawCircle((normalizeValue(it2.next().floatValue()) * i8) + this.trackSidePadding, calculateTop, this.thumbRadius, this.thumbPaint);
            }
        }
        Iterator<Float> it3 = this.values.iterator();
        while (it3.hasNext()) {
            canvas.save();
            int normalizeValue = this.trackSidePadding + ((int) (normalizeValue(it3.next().floatValue()) * i8));
            int i9 = this.thumbRadius;
            canvas.translate(normalizeValue - i9, calculateTop - i9);
            this.thumbDrawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int i3 = this.widgetHeight;
        int i4 = 0;
        if (this.labelBehavior == 1) {
            i4 = ((TooltipDrawable) this.labels.get(0)).getIntrinsicHeight();
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3 + i4, 1073741824));
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SliderState sliderState = (SliderState) parcelable;
        super.onRestoreInstanceState(sliderState.getSuperState());
        this.valueFrom = sliderState.valueFrom;
        this.valueTo = sliderState.valueTo;
        setValuesInternal(sliderState.values);
        this.stepSize = sliderState.stepSize;
        if (sliderState.hasFocus) {
            requestFocus();
        }
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        this.trackWidth = Math.max(i - (this.trackSidePadding * 2), 0);
        maybeCalculateTicksCoordinates();
        updateHaloHotspot();
    }

    public final void onStartTrackingTouch() {
        Iterator it = this.touchListeners.iterator();
        while (it.hasNext()) {
            ((BaseOnSliderTouchListener) it.next()).onStartTrackingTouch();
        }
    }

    public boolean pickActiveThumb() {
        boolean z;
        if (this.activeThumbIdx != -1) {
            return true;
        }
        float f = this.touchPosition;
        if (isRtl()) {
            f = 1.0f - f;
        }
        float f2 = this.valueTo;
        float f3 = this.valueFrom;
        float m = MotionController$$ExternalSyntheticOutline0.m(f2, f3, f, f3);
        float normalizeValue = (normalizeValue(m) * this.trackWidth) + this.trackSidePadding;
        this.activeThumbIdx = 0;
        float abs = Math.abs(this.values.get(0).floatValue() - m);
        for (int i = 1; i < this.values.size(); i++) {
            float abs2 = Math.abs(this.values.get(i).floatValue() - m);
            float normalizeValue2 = (normalizeValue(this.values.get(i).floatValue()) * this.trackWidth) + this.trackSidePadding;
            if (Float.compare(abs2, abs) > 1) {
                break;
            }
            if (!isRtl() ? normalizeValue2 - normalizeValue >= 0.0f : normalizeValue2 - normalizeValue <= 0.0f) {
                z = false;
            } else {
                z = true;
            }
            if (Float.compare(abs2, abs) < 0) {
                this.activeThumbIdx = i;
            } else {
                if (Float.compare(abs2, abs) != 0) {
                    continue;
                } else if (Math.abs(normalizeValue2 - normalizeValue) < this.scaledTouchSlop) {
                    this.activeThumbIdx = -1;
                    return false;
                } else if (z) {
                    this.activeThumbIdx = i;
                }
            }
            abs = abs2;
        }
        if (this.activeThumbIdx != -1) {
            return true;
        }
        return false;
    }

    public void setValues(Float... fArr) {
        ArrayList<Float> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, fArr);
        setValuesInternal(arrayList);
    }

    public final boolean shouldDrawCompatHalo() {
        if (this.forceDrawCompatHalo || !(getBackground() instanceof RippleDrawable)) {
            return true;
        }
        return false;
    }

    public final boolean snapThumbToValue(int i, float f) {
        float f2;
        float f3;
        this.focusedThumbIdx = i;
        if (Math.abs(f - this.values.get(i).floatValue()) < 1.0E-4d) {
            return false;
        }
        float minSeparation = getMinSeparation();
        if (this.separationUnit == 0) {
            if (minSeparation == 0.0f) {
                minSeparation = 0.0f;
            } else {
                float f4 = this.valueFrom;
                minSeparation = MotionController$$ExternalSyntheticOutline0.m(f4, this.valueTo, (minSeparation - this.trackSidePadding) / this.trackWidth, f4);
            }
        }
        if (isRtl()) {
            minSeparation = -minSeparation;
        }
        int i2 = i + 1;
        if (i2 >= this.values.size()) {
            f2 = this.valueTo;
        } else {
            f2 = this.values.get(i2).floatValue() - minSeparation;
        }
        int i3 = i - 1;
        if (i3 < 0) {
            f3 = this.valueFrom;
        } else {
            f3 = minSeparation + this.values.get(i3).floatValue();
        }
        this.values.set(i, Float.valueOf(AtomicFU.clamp(f, f3, f2)));
        Iterator it = this.changeListeners.iterator();
        while (it.hasNext()) {
            this.values.get(i).floatValue();
            ((BaseOnChangeListener) it.next()).onValueChange();
        }
        AccessibilityManager accessibilityManager = this.accessibilityManager;
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return true;
        }
        BaseSlider<S, L, T>.AccessibilityEventSender accessibilityEventSender = this.accessibilityEventSender;
        if (accessibilityEventSender == null) {
            this.accessibilityEventSender = new AccessibilityEventSender();
        } else {
            removeCallbacks(accessibilityEventSender);
        }
        BaseSlider<S, L, T>.AccessibilityEventSender accessibilityEventSender2 = this.accessibilityEventSender;
        Objects.requireNonNull(accessibilityEventSender2);
        accessibilityEventSender2.virtualViewId = i;
        postDelayed(this.accessibilityEventSender, 200L);
        return true;
    }

    public final boolean snapTouchPosition() {
        double d;
        float f;
        int i;
        float f2 = this.touchPosition;
        float f3 = this.stepSize;
        if (f3 > 0.0f) {
            d = Math.round(f2 * i) / ((int) ((this.valueTo - this.valueFrom) / f3));
        } else {
            d = f2;
        }
        if (isRtl()) {
            d = 1.0d - d;
        }
        float f4 = this.valueTo;
        return snapThumbToValue(this.activeThumbIdx, (float) ((d * (f4 - f)) + this.valueFrom));
    }

    public final void updateBoundsForVirturalViewId(int i, Rect rect) {
        int normalizeValue = this.trackSidePadding + ((int) (normalizeValue(((Float) getValues().get(i)).floatValue()) * this.trackWidth));
        int calculateTop = calculateTop();
        int i2 = this.thumbRadius;
        rect.set(normalizeValue - i2, calculateTop - i2, normalizeValue + i2, calculateTop + i2);
    }

    public final void validateConfigurationIfDirty() {
        if (this.dirtyConfig) {
            float f = this.valueFrom;
            float f2 = this.valueTo;
            if (f >= f2) {
                throw new IllegalStateException(String.format("valueFrom(%s) must be smaller than valueTo(%s)", Float.valueOf(this.valueFrom), Float.valueOf(this.valueTo)));
            } else if (f2 <= f) {
                throw new IllegalStateException(String.format("valueTo(%s) must be greater than valueFrom(%s)", Float.valueOf(this.valueTo), Float.valueOf(this.valueFrom)));
            } else if (this.stepSize <= 0.0f || isMultipleOfStepSize(f2 - f)) {
                Iterator<Float> it = this.values.iterator();
                while (it.hasNext()) {
                    Float next = it.next();
                    if (next.floatValue() < this.valueFrom || next.floatValue() > this.valueTo) {
                        throw new IllegalStateException(String.format("Slider value(%s) must be greater or equal to valueFrom(%s), and lower or equal to valueTo(%s)", next, Float.valueOf(this.valueFrom), Float.valueOf(this.valueTo)));
                    } else if (this.stepSize > 0.0f && !isMultipleOfStepSize(next.floatValue() - this.valueFrom)) {
                        throw new IllegalStateException(String.format("Value(%s) must be equal to valueFrom(%s) plus a multiple of stepSize(%s) when using stepSize(%s)", next, Float.valueOf(this.valueFrom), Float.valueOf(this.stepSize), Float.valueOf(this.stepSize)));
                    }
                }
                float minSeparation = getMinSeparation();
                if (minSeparation >= 0.0f) {
                    float f3 = this.stepSize;
                    if (f3 > 0.0f && minSeparation > 0.0f) {
                        if (this.separationUnit != 1) {
                            throw new IllegalStateException(String.format("minSeparation(%s) cannot be set as a dimension when using stepSize(%s)", Float.valueOf(minSeparation), Float.valueOf(this.stepSize)));
                        } else if (minSeparation < f3 || !isMultipleOfStepSize(minSeparation)) {
                            throw new IllegalStateException(String.format("minSeparation(%s) must be greater or equal and a multiple of stepSize(%s) when using stepSize(%s)", Float.valueOf(minSeparation), Float.valueOf(this.stepSize), Float.valueOf(this.stepSize)));
                        }
                    }
                    float f4 = this.stepSize;
                    if (f4 != 0.0f) {
                        if (((int) f4) != f4) {
                            Log.w("BaseSlider", String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "stepSize", Float.valueOf(f4)));
                        }
                        float f5 = this.valueFrom;
                        if (((int) f5) != f5) {
                            Log.w("BaseSlider", String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "valueFrom", Float.valueOf(f5)));
                        }
                        float f6 = this.valueTo;
                        if (((int) f6) != f6) {
                            Log.w("BaseSlider", String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "valueTo", Float.valueOf(f6)));
                        }
                    }
                    this.dirtyConfig = false;
                    return;
                }
                throw new IllegalStateException(String.format("minSeparation(%s) must be greater or equal to 0", Float.valueOf(minSeparation)));
            } else {
                throw new IllegalStateException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", Float.valueOf(this.stepSize), Float.valueOf(this.valueFrom), Float.valueOf(this.valueTo)));
            }
        }
    }

    @Override // android.view.View
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        this.inactiveTrackPaint.setColor(getColorForState(this.trackColorInactive));
        this.activeTrackPaint.setColor(getColorForState(this.trackColorActive));
        this.inactiveTicksPaint.setColor(getColorForState(this.tickColorInactive));
        this.activeTicksPaint.setColor(getColorForState(this.tickColorActive));
        Iterator it = this.labels.iterator();
        while (it.hasNext()) {
            TooltipDrawable tooltipDrawable = (TooltipDrawable) it.next();
            if (tooltipDrawable.isStateful()) {
                tooltipDrawable.setState(getDrawableState());
            }
        }
        if (this.thumbDrawable.isStateful()) {
            this.thumbDrawable.setState(getDrawableState());
        }
        this.haloPaint.setColor(getColorForState(this.haloColor));
        this.haloPaint.setAlpha(63);
    }

    public final float[] getActiveRange() {
        float floatValue = ((Float) Collections.max(getValues())).floatValue();
        float floatValue2 = ((Float) Collections.min(getValues())).floatValue();
        if (this.values.size() == 1) {
            floatValue2 = this.valueFrom;
        }
        float normalizeValue = normalizeValue(floatValue2);
        float normalizeValue2 = normalizeValue(floatValue);
        return isRtl() ? new float[]{normalizeValue2, normalizeValue} : new float[]{normalizeValue, normalizeValue2};
    }

    public final int getColorForState(ColorStateList colorStateList) {
        return colorStateList.getColorForState(getDrawableState(), colorStateList.getDefaultColor());
    }

    public final boolean isInVerticalScrollingContainer() {
        ViewParent parent = getParent();
        while (true) {
            boolean z = false;
            if (!(parent instanceof ViewGroup)) {
                return false;
            }
            ViewGroup viewGroup = (ViewGroup) parent;
            if (viewGroup.canScrollVertically(1) || viewGroup.canScrollVertically(-1)) {
                z = true;
            }
            if (z && viewGroup.shouldDelayChildPressedState()) {
                return true;
            }
            parent = parent.getParent();
        }
    }

    public final boolean moveFocusInAbsoluteDirection(int i) {
        if (isRtl()) {
            if (i == Integer.MIN_VALUE) {
                i = Integer.MAX_VALUE;
            } else {
                i = -i;
            }
        }
        return moveFocus(i);
    }

    @Override // android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        Iterator it = this.labels.iterator();
        while (it.hasNext()) {
            TooltipDrawable tooltipDrawable = (TooltipDrawable) it.next();
            ViewGroup contentView = ViewUtils.getContentView(this);
            if (contentView == null) {
                Objects.requireNonNull(tooltipDrawable);
            } else {
                Objects.requireNonNull(tooltipDrawable);
                int[] iArr = new int[2];
                contentView.getLocationOnScreen(iArr);
                tooltipDrawable.locationOnScreenX = iArr[0];
                contentView.getWindowVisibleDisplayFrame(tooltipDrawable.displayFrame);
                contentView.addOnLayoutChangeListener(tooltipDrawable.attachedViewLayoutChangeListener);
            }
        }
    }

    @Override // android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        if (!z) {
            this.activeThumbIdx = -1;
            ensureLabelsRemoved();
            this.accessibilityHelper.clearKeyboardFocusForVirtualView(this.focusedThumbIdx);
            return;
        }
        if (i == 1) {
            moveFocus(Integer.MAX_VALUE);
        } else if (i == 2) {
            moveFocus(Integer.MIN_VALUE);
        } else if (i == 17) {
            moveFocusInAbsoluteDirection(Integer.MAX_VALUE);
        } else if (i == 66) {
            moveFocusInAbsoluteDirection(Integer.MIN_VALUE);
        }
        this.accessibilityHelper.requestKeyboardFocusForVirtualView(this.focusedThumbIdx);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        float f;
        float f2;
        if (!isEnabled()) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.values.size() == 1) {
            this.activeThumbIdx = 0;
        }
        Float f3 = null;
        Boolean bool = null;
        if (this.activeThumbIdx == -1) {
            if (i != 61) {
                if (i != 66) {
                    if (i != 81) {
                        if (i != 69) {
                            if (i != 70) {
                                switch (i) {
                                    case 21:
                                        moveFocusInAbsoluteDirection(-1);
                                        bool = Boolean.TRUE;
                                        break;
                                    case 22:
                                        moveFocusInAbsoluteDirection(1);
                                        bool = Boolean.TRUE;
                                        break;
                                }
                            }
                        } else {
                            moveFocus(-1);
                            bool = Boolean.TRUE;
                        }
                    }
                    moveFocus(1);
                    bool = Boolean.TRUE;
                }
                this.activeThumbIdx = this.focusedThumbIdx;
                postInvalidate();
                bool = Boolean.TRUE;
            } else if (keyEvent.hasNoModifiers()) {
                bool = Boolean.valueOf(moveFocus(1));
            } else if (keyEvent.isShiftPressed()) {
                bool = Boolean.valueOf(moveFocus(-1));
            } else {
                bool = Boolean.FALSE;
            }
            if (bool != null) {
                return bool.booleanValue();
            }
            return super.onKeyDown(i, keyEvent);
        }
        boolean isLongPress = this.isLongPress | keyEvent.isLongPress();
        this.isLongPress = isLongPress;
        float f4 = 1.0f;
        if (isLongPress) {
            float f5 = this.stepSize;
            if (f5 != 0.0f) {
                f4 = f5;
            }
            if ((this.valueTo - this.valueFrom) / f4 > 20) {
                f4 *= Math.round(f / f2);
            }
        } else {
            float f6 = this.stepSize;
            if (f6 != 0.0f) {
                f4 = f6;
            }
        }
        if (i == 21) {
            if (!isRtl()) {
                f4 = -f4;
            }
            f3 = Float.valueOf(f4);
        } else if (i == 22) {
            if (isRtl()) {
                f4 = -f4;
            }
            f3 = Float.valueOf(f4);
        } else if (i == 69) {
            f3 = Float.valueOf(-f4);
        } else if (i == 70 || i == 81) {
            f3 = Float.valueOf(f4);
        }
        if (f3 != null) {
            if (snapThumbToValue(this.activeThumbIdx, f3.floatValue() + this.values.get(this.activeThumbIdx).floatValue())) {
                updateHaloHotspot();
                postInvalidate();
            }
            return true;
        }
        if (i != 23) {
            if (i != 61) {
                if (i != 66) {
                    return super.onKeyDown(i, keyEvent);
                }
            } else if (keyEvent.hasNoModifiers()) {
                return moveFocus(1);
            } else {
                if (keyEvent.isShiftPressed()) {
                    return moveFocus(-1);
                }
                return false;
            }
        }
        this.activeThumbIdx = -1;
        ensureLabelsRemoved();
        postInvalidate();
        return true;
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SliderState sliderState = new SliderState(super.onSaveInstanceState());
        sliderState.valueFrom = this.valueFrom;
        sliderState.valueTo = this.valueTo;
        sliderState.values = new ArrayList<>(this.values);
        sliderState.stepSize = this.stepSize;
        sliderState.hasFocus = hasFocus();
        return sliderState;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        float x = motionEvent.getX();
        float f = (x - this.trackSidePadding) / this.trackWidth;
        this.touchPosition = f;
        float max = Math.max(0.0f, f);
        this.touchPosition = max;
        this.touchPosition = Math.min(1.0f, max);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.touchDownX = x;
            if (!isInVerticalScrollingContainer()) {
                getParent().requestDisallowInterceptTouchEvent(true);
                if (pickActiveThumb()) {
                    requestFocus();
                    this.thumbIsPressed = true;
                    snapTouchPosition();
                    updateHaloHotspot();
                    invalidate();
                    onStartTrackingTouch();
                }
            }
        } else if (actionMasked == 1) {
            this.thumbIsPressed = false;
            MotionEvent motionEvent2 = this.lastEvent;
            if (motionEvent2 != null && motionEvent2.getActionMasked() == 0 && Math.abs(this.lastEvent.getX() - motionEvent.getX()) <= this.scaledTouchSlop && Math.abs(this.lastEvent.getY() - motionEvent.getY()) <= this.scaledTouchSlop && pickActiveThumb()) {
                onStartTrackingTouch();
            }
            if (this.activeThumbIdx != -1) {
                snapTouchPosition();
                this.activeThumbIdx = -1;
                Iterator it = this.touchListeners.iterator();
                while (it.hasNext()) {
                    ((BaseOnSliderTouchListener) it.next()).onStopTrackingTouch();
                }
            }
            ensureLabelsRemoved();
            invalidate();
        } else if (actionMasked == 2) {
            if (!this.thumbIsPressed) {
                if (isInVerticalScrollingContainer() && Math.abs(x - this.touchDownX) < this.scaledTouchSlop) {
                    return false;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                onStartTrackingTouch();
            }
            if (pickActiveThumb()) {
                this.thumbIsPressed = true;
                snapTouchPosition();
                updateHaloHotspot();
                invalidate();
            }
        }
        setPressed(this.thumbIsPressed);
        this.lastEvent = MotionEvent.obtain(motionEvent);
        return true;
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        int i;
        super.setEnabled(z);
        if (z) {
            i = 0;
        } else {
            i = 2;
        }
        setLayerType(i, null);
    }

    public final void setValuesInternal(ArrayList<Float> arrayList) {
        TextAppearance textAppearance;
        ViewGroup contentView;
        int resourceId;
        ViewOverlayApi18 viewOverlayApi18;
        if (!arrayList.isEmpty()) {
            Collections.sort(arrayList);
            if (this.values.size() != arrayList.size() || !this.values.equals(arrayList)) {
                this.values = arrayList;
                int i = 1;
                this.dirtyConfig = true;
                this.focusedThumbIdx = 0;
                updateHaloHotspot();
                if (this.labels.size() > this.values.size()) {
                    List<TooltipDrawable> subList = this.labels.subList(this.values.size(), this.labels.size());
                    for (TooltipDrawable tooltipDrawable : subList) {
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        if (ViewCompat.Api19Impl.isAttachedToWindow(this)) {
                            ViewGroup contentView2 = ViewUtils.getContentView(this);
                            if (contentView2 == null) {
                                viewOverlayApi18 = null;
                            } else {
                                viewOverlayApi18 = new ViewOverlayApi18(contentView2);
                            }
                            if (viewOverlayApi18 != null) {
                                viewOverlayApi18.viewOverlay.remove(tooltipDrawable);
                                ViewGroup contentView3 = ViewUtils.getContentView(this);
                                if (contentView3 == null) {
                                    Objects.requireNonNull(tooltipDrawable);
                                } else {
                                    contentView3.removeOnLayoutChangeListener(tooltipDrawable.attachedViewLayoutChangeListener);
                                }
                            }
                        }
                    }
                    subList.clear();
                }
                while (this.labels.size() < this.values.size()) {
                    AnonymousClass1 r1 = this.labelMaker;
                    Objects.requireNonNull(r1);
                    TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(BaseSlider.this.getContext(), r1.val$attrs, R$styleable.Slider, r1.val$defStyleAttr, 2132018702, new int[0]);
                    Context context = BaseSlider.this.getContext();
                    int resourceId2 = obtainStyledAttributes.getResourceId(8, 2132018736);
                    TooltipDrawable tooltipDrawable2 = new TooltipDrawable(context, resourceId2);
                    TypedArray obtainStyledAttributes2 = ThemeEnforcement.obtainStyledAttributes(tooltipDrawable2.context, null, R$styleable.Tooltip, 0, resourceId2, new int[0]);
                    tooltipDrawable2.arrowSize = tooltipDrawable2.context.getResources().getDimensionPixelSize(2131166574);
                    ShapeAppearanceModel shapeAppearanceModel = tooltipDrawable2.drawableState.shapeAppearanceModel;
                    Objects.requireNonNull(shapeAppearanceModel);
                    ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
                    builder.bottomEdge = tooltipDrawable2.createMarkerEdge();
                    tooltipDrawable2.setShapeAppearanceModel(new ShapeAppearanceModel(builder));
                    CharSequence text = obtainStyledAttributes2.getText(6);
                    if (!TextUtils.equals(tooltipDrawable2.text, text)) {
                        tooltipDrawable2.text = text;
                        TextDrawableHelper textDrawableHelper = tooltipDrawable2.textDrawableHelper;
                        Objects.requireNonNull(textDrawableHelper);
                        textDrawableHelper.textWidthDirty = true;
                        tooltipDrawable2.invalidateSelf();
                    }
                    Context context2 = tooltipDrawable2.context;
                    if (!obtainStyledAttributes2.hasValue(0) || (resourceId = obtainStyledAttributes2.getResourceId(0, 0)) == 0) {
                        textAppearance = null;
                    } else {
                        textAppearance = new TextAppearance(context2, resourceId);
                    }
                    if (textAppearance != null && obtainStyledAttributes2.hasValue(1)) {
                        textAppearance.textColor = MaterialResources.getColorStateList(tooltipDrawable2.context, obtainStyledAttributes2, 1);
                    }
                    tooltipDrawable2.textDrawableHelper.setTextAppearance(textAppearance, tooltipDrawable2.context);
                    tooltipDrawable2.setFillColor(ColorStateList.valueOf(obtainStyledAttributes2.getColor(7, ColorUtils.compositeColors(ColorUtils.setAlphaComponent(MaterialAttributes.resolveOrThrow(tooltipDrawable2.context, 2130968823, TooltipDrawable.class.getCanonicalName()), 153), ColorUtils.setAlphaComponent(MaterialAttributes.resolveOrThrow(tooltipDrawable2.context, 16842801, TooltipDrawable.class.getCanonicalName()), 229)))));
                    tooltipDrawable2.setStrokeColor(ColorStateList.valueOf(MaterialAttributes.resolveOrThrow(tooltipDrawable2.context, 2130968847, TooltipDrawable.class.getCanonicalName())));
                    tooltipDrawable2.padding = obtainStyledAttributes2.getDimensionPixelSize(2, 0);
                    tooltipDrawable2.minWidth = obtainStyledAttributes2.getDimensionPixelSize(4, 0);
                    tooltipDrawable2.minHeight = obtainStyledAttributes2.getDimensionPixelSize(5, 0);
                    tooltipDrawable2.layoutMargin = obtainStyledAttributes2.getDimensionPixelSize(3, 0);
                    obtainStyledAttributes2.recycle();
                    obtainStyledAttributes.recycle();
                    this.labels.add(tooltipDrawable2);
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                    if (ViewCompat.Api19Impl.isAttachedToWindow(this) && (contentView = ViewUtils.getContentView(this)) != null) {
                        int[] iArr = new int[2];
                        contentView.getLocationOnScreen(iArr);
                        tooltipDrawable2.locationOnScreenX = iArr[0];
                        contentView.getWindowVisibleDisplayFrame(tooltipDrawable2.displayFrame);
                        contentView.addOnLayoutChangeListener(tooltipDrawable2.attachedViewLayoutChangeListener);
                    }
                }
                if (this.labels.size() == 1) {
                    i = 0;
                }
                Iterator it = this.labels.iterator();
                while (it.hasNext()) {
                    TooltipDrawable tooltipDrawable3 = (TooltipDrawable) it.next();
                    Objects.requireNonNull(tooltipDrawable3);
                    tooltipDrawable3.drawableState.strokeWidth = i;
                    tooltipDrawable3.invalidateSelf();
                }
                Iterator it2 = this.changeListeners.iterator();
                while (it2.hasNext()) {
                    BaseOnChangeListener baseOnChangeListener = (BaseOnChangeListener) it2.next();
                    Iterator<Float> it3 = this.values.iterator();
                    while (it3.hasNext()) {
                        it3.next().floatValue();
                        baseOnChangeListener.onValueChange();
                    }
                }
                postInvalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("At least one value must be set");
    }

    public final void updateHaloHotspot() {
        if (!shouldDrawCompatHalo() && getMeasuredWidth() > 0) {
            Drawable background = getBackground();
            if (background instanceof RippleDrawable) {
                int normalizeValue = (int) ((normalizeValue(this.values.get(this.focusedThumbIdx).floatValue()) * this.trackWidth) + this.trackSidePadding);
                int calculateTop = calculateTop();
                int i = this.haloRadius;
                background.setHotspotBounds(normalizeValue - i, calculateTop - i, normalizeValue + i, calculateTop + i);
            }
        }
    }

    public void forceDrawCompatHalo(boolean z) {
        this.forceDrawCompatHalo = z;
    }
}
