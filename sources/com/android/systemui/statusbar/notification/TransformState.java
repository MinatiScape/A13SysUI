package com.android.systemui.statusbar.notification;

import android.util.Pools;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.leanback.R$raw;
import com.android.internal.widget.MessagingImageMessage;
import com.android.internal.widget.MessagingPropertyAnimator;
import com.android.internal.widget.ViewClippingUtil;
import com.android.systemui.R$array;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
/* loaded from: classes.dex */
public class TransformState {
    public boolean mAlignEnd;
    public boolean mSameAsAny;
    public TransformInfo mTransformInfo;
    public View mTransformedView;
    public static Pools.SimplePool<TransformState> sInstancePool = new Pools.SimplePool<>(40);
    public static AnonymousClass1 CLIPPING_PARAMETERS = new ViewClippingUtil.ClippingParameters() { // from class: com.android.systemui.statusbar.notification.TransformState.1
        public final void onClippingStateChanged(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (z) {
                    expandableNotificationRow.setClipToActualHeight(true);
                } else if (expandableNotificationRow.isChildInGroup()) {
                    expandableNotificationRow.setClipToActualHeight(false);
                }
            }
        }

        public final boolean shouldFinish(View view) {
            if (view instanceof ExpandableNotificationRow) {
                return !((ExpandableNotificationRow) view).isChildInGroup();
            }
            return false;
        }
    };
    public int[] mOwnPosition = new int[2];
    public float mTransformationEndY = -1.0f;
    public float mTransformationEndX = -1.0f;
    public Interpolator mDefaultInterpolator = Interpolators.FAST_OUT_SLOW_IN;

    /* loaded from: classes.dex */
    public interface TransformInfo {
    }

    public void appear(float f, TransformableView transformableView) {
        if (f == 0.0f) {
            prepareFadeIn();
        }
        R$raw.fadeIn(this.mTransformedView, f, true);
    }

    public void reset() {
        this.mTransformedView = null;
        this.mTransformInfo = null;
        this.mSameAsAny = false;
        this.mTransformationEndX = -1.0f;
        this.mTransformationEndY = -1.0f;
        this.mAlignEnd = false;
        this.mDefaultInterpolator = Interpolators.FAST_OUT_SLOW_IN;
    }

    public void transformViewFrom(TransformState transformState, float f) {
        this.mTransformedView.animate().cancel();
        if (sameAs(transformState)) {
            ensureVisible();
        } else {
            R$raw.fadeIn(this.mTransformedView, f, true);
        }
        transformViewFullyFrom(transformState, f);
    }

    public boolean transformViewTo(TransformState transformState, float f) {
        this.mTransformedView.animate().cancel();
        if (!sameAs(transformState)) {
            R$raw.fadeOut(this.mTransformedView, f, true);
            transformViewFullyTo(transformState, f);
            return true;
        } else if (this.mTransformedView.getVisibility() != 0) {
            return false;
        } else {
            this.mTransformedView.setAlpha(0.0f);
            this.mTransformedView.setVisibility(4);
            return false;
        }
    }

    public static TransformState createFrom(View view, TransformInfo transformInfo) {
        if (view instanceof TextView) {
            TextViewTransformState textViewTransformState = (TextViewTransformState) TextViewTransformState.sInstancePool.acquire();
            if (textViewTransformState == null) {
                textViewTransformState = new TextViewTransformState();
            }
            textViewTransformState.initFrom(view, transformInfo);
            return textViewTransformState;
        } else if (view.getId() == 16908742) {
            ActionListTransformState actionListTransformState = (ActionListTransformState) ActionListTransformState.sInstancePool.acquire();
            if (actionListTransformState == null) {
                actionListTransformState = new ActionListTransformState();
            }
            actionListTransformState.initFrom(view, transformInfo);
            return actionListTransformState;
        } else if (view.getId() == 16909283) {
            MessagingLayoutTransformState messagingLayoutTransformState = (MessagingLayoutTransformState) MessagingLayoutTransformState.sInstancePool.acquire();
            if (messagingLayoutTransformState == null) {
                messagingLayoutTransformState = new MessagingLayoutTransformState();
            }
            messagingLayoutTransformState.initFrom(view, transformInfo);
            return messagingLayoutTransformState;
        } else if (view instanceof MessagingImageMessage) {
            MessagingImageTransformState messagingImageTransformState = (MessagingImageTransformState) MessagingImageTransformState.sInstancePool.acquire();
            if (messagingImageTransformState == null) {
                messagingImageTransformState = new MessagingImageTransformState();
            }
            messagingImageTransformState.initFrom(view, transformInfo);
            return messagingImageTransformState;
        } else if (view instanceof ImageView) {
            ImageTransformState imageTransformState = (ImageTransformState) ImageTransformState.sInstancePool.acquire();
            if (imageTransformState == null) {
                imageTransformState = new ImageTransformState();
            }
            imageTransformState.initFrom(view, transformInfo);
            return imageTransformState;
        } else if (view instanceof ProgressBar) {
            ProgressTransformState progressTransformState = (ProgressTransformState) ProgressTransformState.sInstancePool.acquire();
            if (progressTransformState == null) {
                progressTransformState = new ProgressTransformState();
            }
            progressTransformState.initFrom(view, transformInfo);
            return progressTransformState;
        } else {
            TransformState transformState = (TransformState) sInstancePool.acquire();
            if (transformState == null) {
                transformState = new TransformState();
            }
            transformState.initFrom(view, transformInfo);
            return transformState;
        }
    }

    public static void setClippingDeactivated(View view, boolean z) {
        ViewClippingUtil.setClippingDeactivated(view, z, CLIPPING_PARAMETERS);
    }

    public final void abortTransformation() {
        View view = this.mTransformedView;
        Float valueOf = Float.valueOf(-1.0f);
        view.setTag(2131429089, valueOf);
        this.mTransformedView.setTag(2131429090, valueOf);
        this.mTransformedView.setTag(2131429087, valueOf);
        this.mTransformedView.setTag(2131429088, valueOf);
    }

    public void disappear(float f, TransformableView transformableView) {
        R$raw.fadeOut(this.mTransformedView, f, true);
    }

    public final void ensureVisible() {
        if (this.mTransformedView.getVisibility() == 4 || this.mTransformedView.getAlpha() != 1.0f) {
            this.mTransformedView.setAlpha(1.0f);
            this.mTransformedView.setVisibility(0);
        }
    }

    public int getContentHeight() {
        return this.mTransformedView.getHeight();
    }

    public int getContentWidth() {
        return this.mTransformedView.getWidth();
    }

    public final int[] getLocationOnScreen() {
        this.mTransformedView.getLocationOnScreen(this.mOwnPosition);
        int[] iArr = this.mOwnPosition;
        iArr[0] = (int) (iArr[0] - (this.mTransformedView.getPivotX() * (1.0f - this.mTransformedView.getScaleX())));
        int[] iArr2 = this.mOwnPosition;
        iArr2[1] = (int) (iArr2[1] - (this.mTransformedView.getPivotY() * (1.0f - this.mTransformedView.getScaleY())));
        int[] iArr3 = this.mOwnPosition;
        iArr3[1] = iArr3[1] - (MessagingPropertyAnimator.getTop(this.mTransformedView) - MessagingPropertyAnimator.getLayoutTop(this.mTransformedView));
        return this.mOwnPosition;
    }

    public final float getTransformationStartScaleX() {
        Object tag = this.mTransformedView.getTag(2131429087);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public final float getTransformationStartScaleY() {
        Object tag = this.mTransformedView.getTag(2131429088);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public final float getTransformationStartX() {
        Object tag = this.mTransformedView.getTag(2131429089);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public final float getTransformationStartY() {
        Object tag = this.mTransformedView.getTag(2131429090);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public void initFrom(View view, TransformInfo transformInfo) {
        this.mTransformedView = view;
        this.mTransformInfo = transformInfo;
        this.mAlignEnd = Boolean.TRUE.equals(view.getTag(2131427474));
    }

    public void resetTransformedView() {
        this.mTransformedView.setTranslationX(0.0f);
        this.mTransformedView.setTranslationY(0.0f);
        this.mTransformedView.setScaleX(1.0f);
        this.mTransformedView.setScaleY(1.0f);
        setClippingDeactivated(this.mTransformedView, false);
        abortTransformation();
    }

    public final void setTransformationStartScaleX(float f) {
        this.mTransformedView.setTag(2131429087, Float.valueOf(f));
    }

    public final void setTransformationStartScaleY(float f) {
        this.mTransformedView.setTag(2131429088, Float.valueOf(f));
    }

    public final void setTransformationStartX(float f) {
        this.mTransformedView.setTag(2131429089, Float.valueOf(f));
    }

    public final void setTransformationStartY(float f) {
        this.mTransformedView.setTag(2131429090, Float.valueOf(f));
    }

    public void setVisible(boolean z, boolean z2) {
        float f;
        int i;
        if (z2 || this.mTransformedView.getVisibility() != 8) {
            if (this.mTransformedView.getVisibility() != 8) {
                View view = this.mTransformedView;
                if (z) {
                    i = 0;
                } else {
                    i = 4;
                }
                view.setVisibility(i);
            }
            this.mTransformedView.animate().cancel();
            View view2 = this.mTransformedView;
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            view2.setAlpha(f);
            resetTransformedView();
        }
    }

    public final boolean transformRightEdge(TransformState transformState) {
        boolean z;
        boolean z2 = true;
        if (!this.mAlignEnd || !transformState.mAlignEnd) {
            z = false;
        } else {
            z = true;
        }
        if (!this.mTransformedView.isLayoutRtl() || !transformState.mTransformedView.isLayoutRtl()) {
            z2 = false;
        }
        return z ^ z2;
    }

    public void transformViewFullyFrom(TransformState transformState, float f) {
        transformViewFrom(transformState, 17, null, f);
    }

    public void transformViewFullyTo(TransformState transformState, float f) {
        transformViewTo(transformState, 17, null, f);
    }

    public final int[] getLaidOutLocationOnScreen() {
        int[] locationOnScreen = getLocationOnScreen();
        locationOnScreen[0] = (int) (locationOnScreen[0] - this.mTransformedView.getTranslationX());
        locationOnScreen[1] = (int) (locationOnScreen[1] - this.mTransformedView.getTranslationY());
        return locationOnScreen;
    }

    public void recycle() {
        reset();
        if (getClass() == TransformState.class) {
            sInstancePool.release(this);
        }
    }

    public boolean transformScale(TransformState transformState) {
        return sameAs(transformState);
    }

    public void transformViewFrom(TransformState transformState, int i, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        Interpolator customInterpolator;
        Interpolator customInterpolator2;
        int[] iArr;
        float f2;
        View view = this.mTransformedView;
        boolean z = (i & 1) != 0;
        boolean z2 = (i & 16) != 0;
        int contentHeight = getContentHeight();
        int contentHeight2 = transformState.getContentHeight();
        boolean z3 = (contentHeight2 == contentHeight || contentHeight2 == 0 || contentHeight == 0) ? false : true;
        int contentWidth = getContentWidth();
        int contentWidth2 = transformState.getContentWidth();
        boolean z4 = (contentWidth2 == contentWidth || contentWidth2 == 0 || contentWidth == 0) ? false : true;
        boolean z5 = (z3 || z4) && transformScale(transformState);
        boolean transformRightEdge = transformRightEdge(transformState);
        int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i2 == 0 || ((z && getTransformationStartX() == -1.0f) || ((z2 && getTransformationStartY() == -1.0f) || ((z5 && getTransformationStartScaleX() == -1.0f && z4) || (z5 && getTransformationStartScaleY() == -1.0f && z3))))) {
            if (i2 != 0) {
                iArr = transformState.getLaidOutLocationOnScreen();
            } else {
                iArr = transformState.getLocationOnScreen();
            }
            int[] laidOutLocationOnScreen = getLaidOutLocationOnScreen();
            if (customTransformation == null || !customTransformation.initTransformation(this, transformState)) {
                if (z) {
                    if (transformRightEdge) {
                        setTransformationStartX((iArr[0] + transformState.mTransformedView.getWidth()) - (laidOutLocationOnScreen[0] + view.getWidth()));
                    } else {
                        setTransformationStartX(iArr[0] - laidOutLocationOnScreen[0]);
                    }
                }
                if (z2) {
                    setTransformationStartY(iArr[1] - laidOutLocationOnScreen[1]);
                }
                View view2 = transformState.mTransformedView;
                if (!z5 || !z4) {
                    setTransformationStartScaleX(-1.0f);
                } else {
                    setTransformationStartScaleX((view2.getScaleX() * contentWidth2) / contentWidth);
                    view.setPivotX(transformRightEdge ? view.getWidth() : 0.0f);
                }
                if (!z5 || !z3) {
                    f2 = -1.0f;
                    setTransformationStartScaleY(-1.0f);
                } else {
                    setTransformationStartScaleY((view2.getScaleY() * contentHeight2) / contentHeight);
                    view.setPivotY(0.0f);
                    f2 = -1.0f;
                }
            } else {
                f2 = -1.0f;
            }
            if (!z) {
                setTransformationStartX(f2);
            }
            if (!z2) {
                setTransformationStartY(f2);
            }
            if (!z5) {
                setTransformationStartScaleX(f2);
                setTransformationStartScaleY(f2);
            }
            setClippingDeactivated(view, true);
        }
        float interpolation = this.mDefaultInterpolator.getInterpolation(f);
        if (z) {
            view.setTranslationX(R$array.interpolate(getTransformationStartX(), 0.0f, (customTransformation == null || (customInterpolator2 = customTransformation.getCustomInterpolator(1, true)) == null) ? interpolation : customInterpolator2.getInterpolation(f)));
        }
        if (z2) {
            view.setTranslationY(R$array.interpolate(getTransformationStartY(), 0.0f, (customTransformation == null || (customInterpolator = customTransformation.getCustomInterpolator(16, true)) == null) ? interpolation : customInterpolator.getInterpolation(f)));
        }
        if (z5) {
            float transformationStartScaleX = getTransformationStartScaleX();
            if (transformationStartScaleX != -1.0f) {
                view.setScaleX(R$array.interpolate(transformationStartScaleX, 1.0f, interpolation));
            }
            float transformationStartScaleY = getTransformationStartScaleY();
            if (transformationStartScaleY != -1.0f) {
                view.setScaleY(R$array.interpolate(transformationStartScaleY, 1.0f, interpolation));
            }
        }
    }

    public final void transformViewTo(TransformState transformState, int i, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        float f2;
        int i2;
        boolean z;
        float f3;
        View view = this.mTransformedView;
        boolean z2 = (i & 1) != 0;
        boolean z3 = (i & 16) != 0;
        boolean transformScale = transformScale(transformState);
        boolean transformRightEdge = transformRightEdge(transformState);
        int contentWidth = getContentWidth();
        int contentWidth2 = transformState.getContentWidth();
        if (f == 0.0f) {
            if (z2) {
                float transformationStartX = getTransformationStartX();
                if (transformationStartX == -1.0f) {
                    transformationStartX = view.getTranslationX();
                }
                setTransformationStartX(transformationStartX);
            }
            if (z3) {
                float transformationStartY = getTransformationStartY();
                if (transformationStartY == -1.0f) {
                    transformationStartY = view.getTranslationY();
                }
                setTransformationStartY(transformationStartY);
            }
            if (!transformScale || contentWidth2 == contentWidth) {
                setTransformationStartScaleX(-1.0f);
            } else {
                setTransformationStartScaleX(view.getScaleX());
                view.setPivotX(transformRightEdge ? view.getWidth() : 0.0f);
            }
            if (!transformScale || transformState.getContentHeight() == getContentHeight()) {
                setTransformationStartScaleY(-1.0f);
            } else {
                setTransformationStartScaleY(view.getScaleY());
                view.setPivotY(0.0f);
            }
            setClippingDeactivated(view, true);
        }
        float interpolation = this.mDefaultInterpolator.getInterpolation(f);
        int[] laidOutLocationOnScreen = transformState.getLaidOutLocationOnScreen();
        int[] laidOutLocationOnScreen2 = getLaidOutLocationOnScreen();
        if (z2) {
            int width = view.getWidth();
            int width2 = transformState.mTransformedView.getWidth();
            if (transformRightEdge) {
                z = false;
                i2 = (laidOutLocationOnScreen[0] + width2) - (laidOutLocationOnScreen2[0] + width);
            } else {
                z = false;
                i2 = laidOutLocationOnScreen[0] - laidOutLocationOnScreen2[0];
            }
            float f4 = i2;
            if (customTransformation != null) {
                if (customTransformation.customTransformTarget(this, transformState)) {
                    f4 = this.mTransformationEndX;
                }
                Interpolator customInterpolator = customTransformation.getCustomInterpolator(1, z);
                if (customInterpolator != null) {
                    f3 = customInterpolator.getInterpolation(f);
                    view.setTranslationX(R$array.interpolate(getTransformationStartX(), f4, f3));
                }
            }
            f3 = interpolation;
            view.setTranslationX(R$array.interpolate(getTransformationStartX(), f4, f3));
        }
        if (z3) {
            float f5 = laidOutLocationOnScreen[1] - laidOutLocationOnScreen2[1];
            if (customTransformation != null) {
                if (customTransformation.customTransformTarget(this, transformState)) {
                    f5 = this.mTransformationEndY;
                }
                Interpolator customInterpolator2 = customTransformation.getCustomInterpolator(16, false);
                if (customInterpolator2 != null) {
                    f2 = customInterpolator2.getInterpolation(f);
                    view.setTranslationY(R$array.interpolate(getTransformationStartY(), f5, f2));
                }
            }
            f2 = interpolation;
            view.setTranslationY(R$array.interpolate(getTransformationStartY(), f5, f2));
        }
        if (transformScale) {
            float transformationStartScaleX = getTransformationStartScaleX();
            if (transformationStartScaleX != -1.0f) {
                view.setScaleX(R$array.interpolate(transformationStartScaleX, contentWidth2 / contentWidth, interpolation));
            }
            float transformationStartScaleY = getTransformationStartScaleY();
            if (transformationStartScaleY != -1.0f) {
                view.setScaleY(R$array.interpolate(transformationStartScaleY, transformState.getContentHeight() / getContentHeight(), interpolation));
            }
        }
    }

    public boolean sameAs(TransformState transformState) {
        return this.mSameAsAny;
    }

    public void prepareFadeIn() {
        resetTransformedView();
    }
}
