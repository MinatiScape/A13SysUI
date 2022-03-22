package com.android.systemui.animation;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.GhostView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GhostedViewLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public class GhostedViewLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    public WrappedDrawable backgroundDrawable;
    public final Lazy backgroundInsets$delegate;
    public FrameLayout backgroundView;
    public final Integer cujType;
    public GhostView ghostView;
    public final Matrix ghostViewMatrix;
    public final View ghostedView;
    public final int[] ghostedViewLocation;
    public final LaunchAnimator.State ghostedViewState;
    public final float[] initialGhostViewMatrixValues;
    public InteractionJankMonitor interactionJankMonitor;
    public ViewGroup launchContainer;
    public final int[] launchContainerLocation;
    public int startBackgroundAlpha;

    /* compiled from: GhostedViewLaunchAnimatorController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static GradientDrawable findGradientDrawable(Drawable drawable) {
            if (drawable instanceof GradientDrawable) {
                return (GradientDrawable) drawable;
            }
            if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 == null) {
                    return null;
                }
                return findGradientDrawable(drawable2);
            }
            if (drawable instanceof LayerDrawable) {
                int i = 0;
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                while (i < numberOfLayers) {
                    int i2 = i + 1;
                    Drawable drawable3 = layerDrawable.getDrawable(i);
                    if (drawable3 instanceof GradientDrawable) {
                        return (GradientDrawable) drawable3;
                    }
                    i = i2;
                }
            }
            return null;
        }
    }

    /* compiled from: GhostedViewLaunchAnimatorController.kt */
    /* loaded from: classes.dex */
    public static final class WrappedDrawable extends Drawable {
        public float[] cornerRadii;
        public int currentAlpha = 255;
        public Rect previousBounds = new Rect();
        public float[] previousCornerRadii;
        public final Drawable wrapped;

        public static void applyBackgroundRadii(Drawable drawable, float[] fArr) {
            GradientDrawable gradientDrawable;
            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setCornerRadii(fArr);
            } else if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 != null) {
                    applyBackgroundRadii(drawable2, fArr);
                }
            } else if (drawable instanceof LayerDrawable) {
                int i = 0;
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                while (i < numberOfLayers) {
                    int i2 = i + 1;
                    Drawable drawable3 = layerDrawable.getDrawable(i);
                    if (drawable3 instanceof GradientDrawable) {
                        gradientDrawable = (GradientDrawable) drawable3;
                    } else {
                        gradientDrawable = null;
                    }
                    if (gradientDrawable != null) {
                        gradientDrawable.setCornerRadii(fArr);
                    }
                    i = i2;
                }
            }
        }

        @Override // android.graphics.drawable.Drawable
        public final void draw(Canvas canvas) {
            Drawable drawable;
            Drawable drawable2;
            Drawable drawable3 = this.wrapped;
            if (drawable3 != null) {
                drawable3.copyBounds(this.previousBounds);
                drawable3.setAlpha(this.currentAlpha);
                drawable3.setBounds(getBounds());
                if (this.cornerRadii[0] >= 0.0f && (drawable2 = this.wrapped) != null) {
                    GradientDrawable findGradientDrawable = Companion.findGradientDrawable(drawable2);
                    if (findGradientDrawable != null) {
                        float[] cornerRadii = findGradientDrawable.getCornerRadii();
                        if (cornerRadii != null) {
                            System.arraycopy(cornerRadii, 0, this.previousCornerRadii, 0, cornerRadii.length - 0);
                        } else {
                            float cornerRadius = findGradientDrawable.getCornerRadius();
                            float[] fArr = this.previousCornerRadii;
                            fArr[0] = cornerRadius;
                            fArr[1] = cornerRadius;
                            fArr[2] = cornerRadius;
                            fArr[3] = cornerRadius;
                            fArr[4] = cornerRadius;
                            fArr[5] = cornerRadius;
                            fArr[6] = cornerRadius;
                            fArr[7] = cornerRadius;
                        }
                    }
                    applyBackgroundRadii(this.wrapped, this.cornerRadii);
                }
                drawable3.draw(canvas);
                drawable3.setAlpha(0);
                drawable3.setBounds(this.previousBounds);
                if (this.cornerRadii[0] >= 0.0f && (drawable = this.wrapped) != null) {
                    applyBackgroundRadii(drawable, this.previousCornerRadii);
                }
            }
        }

        @Override // android.graphics.drawable.Drawable
        public final int getOpacity() {
            Drawable drawable = this.wrapped;
            if (drawable == null) {
                return -2;
            }
            int alpha = drawable.getAlpha();
            drawable.setAlpha(this.currentAlpha);
            int opacity = drawable.getOpacity();
            drawable.setAlpha(alpha);
            return opacity;
        }

        @Override // android.graphics.drawable.Drawable
        public final void setAlpha(int i) {
            if (i != this.currentAlpha) {
                this.currentAlpha = i;
                invalidateSelf();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public final void setColorFilter(ColorFilter colorFilter) {
            Drawable drawable = this.wrapped;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }

        public WrappedDrawable(Drawable drawable) {
            this.wrapped = drawable;
            float[] fArr = new float[8];
            for (int i = 0; i < 8; i++) {
                fArr[i] = -1.0f;
            }
            this.cornerRadii = fArr;
            this.previousCornerRadii = new float[8];
        }

        @Override // android.graphics.drawable.Drawable
        public final int getAlpha() {
            return this.currentAlpha;
        }
    }

    public /* synthetic */ GhostedViewLaunchAnimatorController(View view, Integer num, int i) {
        this(view, (i & 2) != 0 ? null : num, (InteractionJankMonitor) null);
    }

    public GhostedViewLaunchAnimatorController(View view, Integer num, InteractionJankMonitor interactionJankMonitor) {
        this.ghostedView = view;
        this.cujType = num;
        this.interactionJankMonitor = interactionJankMonitor;
        View rootView = view.getRootView();
        Objects.requireNonNull(rootView, "null cannot be cast to non-null type android.view.ViewGroup");
        this.launchContainer = (ViewGroup) rootView;
        this.launchContainerLocation = new int[2];
        float[] fArr = new float[9];
        for (int i = 0; i < 9; i++) {
            fArr[i] = 0.0f;
        }
        this.initialGhostViewMatrixValues = fArr;
        this.ghostViewMatrix = new Matrix();
        this.backgroundInsets$delegate = LazyKt__LazyJVMKt.lazy(new GhostedViewLaunchAnimatorController$backgroundInsets$2(this));
        this.startBackgroundAlpha = 255;
        this.ghostedViewLocation = new int[2];
        this.ghostedViewState = new LaunchAnimator.State(0, 0, 0, 0, 0.0f, 0.0f, 63);
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final LaunchAnimator.State createAnimatorState() {
        LaunchAnimator.State state = new LaunchAnimator.State(0, 0, 0, 0, getCurrentTopCornerRadius(), getCurrentBottomCornerRadius(), 15);
        fillGhostedViewState(state);
        return state;
    }

    public final void fillGhostedViewState(LaunchAnimator.State state) {
        this.ghostedView.getLocationOnScreen(this.ghostedViewLocation);
        Insets insets = (Insets) this.backgroundInsets$delegate.getValue();
        int[] iArr = this.ghostedViewLocation;
        state.top = iArr[1] + insets.top;
        state.bottom = (this.ghostedView.getHeight() + iArr[1]) - insets.bottom;
        int[] iArr2 = this.ghostedViewLocation;
        state.left = iArr2[0] + insets.left;
        state.right = (this.ghostedView.getWidth() + iArr2[0]) - insets.right;
    }

    public float getCurrentBottomCornerRadius() {
        GradientDrawable findGradientDrawable;
        Float f;
        Drawable background = this.ghostedView.getBackground();
        if (background == null || (findGradientDrawable = Companion.findGradientDrawable(background)) == null) {
            return 0.0f;
        }
        float[] cornerRadii = findGradientDrawable.getCornerRadii();
        if (cornerRadii == null) {
            f = null;
        } else {
            f = Float.valueOf(cornerRadii[4]);
        }
        if (f == null) {
            return findGradientDrawable.getCornerRadius();
        }
        return f.floatValue();
    }

    public float getCurrentTopCornerRadius() {
        GradientDrawable findGradientDrawable;
        Float f;
        Drawable background = this.ghostedView.getBackground();
        if (background == null || (findGradientDrawable = Companion.findGradientDrawable(background)) == null) {
            return 0.0f;
        }
        float[] cornerRadii = findGradientDrawable.getCornerRadii();
        if (cornerRadii == null) {
            f = null;
        } else {
            f = Float.valueOf(cornerRadii[0]);
        }
        if (f == null) {
            return findGradientDrawable.getCornerRadius();
        }
        return f.floatValue();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public void onLaunchAnimationEnd(boolean z) {
        Drawable drawable;
        if (this.ghostView != null) {
            Integer num = this.cujType;
            if (num != null) {
                int intValue = num.intValue();
                InteractionJankMonitor interactionJankMonitor = this.interactionJankMonitor;
                if (interactionJankMonitor != null) {
                    interactionJankMonitor.end(intValue);
                }
            }
            WrappedDrawable wrappedDrawable = this.backgroundDrawable;
            if (wrappedDrawable == null) {
                drawable = null;
            } else {
                drawable = wrappedDrawable.wrapped;
            }
            if (drawable != null) {
                drawable.setAlpha(this.startBackgroundAlpha);
            }
            GhostView.removeGhost(this.ghostedView);
            this.launchContainer.getOverlay().remove(this.backgroundView);
            this.ghostedView.setVisibility(4);
            this.ghostedView.setVisibility(0);
            this.ghostedView.invalidate();
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
        GhostView ghostView = this.ghostView;
        if (ghostView != null) {
            FrameLayout frameLayout = this.backgroundView;
            Intrinsics.checkNotNull(frameLayout);
            if (state.visible) {
                if (ghostView.getVisibility() == 4) {
                    ghostView.setVisibility(0);
                    frameLayout.setVisibility(0);
                }
                fillGhostedViewState(this.ghostedViewState);
                int i = state.left;
                LaunchAnimator.State state2 = this.ghostedViewState;
                Objects.requireNonNull(state2);
                int i2 = i - state2.left;
                int i3 = state.right;
                LaunchAnimator.State state3 = this.ghostedViewState;
                Objects.requireNonNull(state3);
                int i4 = i3 - state3.right;
                int i5 = state.top;
                LaunchAnimator.State state4 = this.ghostedViewState;
                Objects.requireNonNull(state4);
                int i6 = i5 - state4.top;
                int i7 = state.bottom;
                LaunchAnimator.State state5 = this.ghostedViewState;
                Objects.requireNonNull(state5);
                int i8 = i7 - state5.bottom;
                LaunchAnimator.State state6 = this.ghostedViewState;
                Objects.requireNonNull(state6);
                LaunchAnimator.State state7 = this.ghostedViewState;
                Objects.requireNonNull(state7);
                float min = Math.min((state.right - state.left) / (state6.right - state6.left), (state.bottom - state.top) / (state7.bottom - state7.top));
                if (this.ghostedView.getParent() instanceof ViewGroup) {
                    GhostView.calculateMatrix(this.ghostedView, this.launchContainer, this.ghostViewMatrix);
                }
                this.launchContainer.getLocationOnScreen(this.launchContainerLocation);
                Matrix matrix = this.ghostViewMatrix;
                LaunchAnimator.State state8 = this.ghostedViewState;
                Objects.requireNonNull(state8);
                int i9 = state8.left;
                float f3 = (((state8.right - i9) / 2.0f) + i9) - this.launchContainerLocation[0];
                LaunchAnimator.State state9 = this.ghostedViewState;
                Objects.requireNonNull(state9);
                int i10 = state9.top;
                matrix.postScale(min, min, f3, (((state9.bottom - i10) / 2.0f) + i10) - this.launchContainerLocation[1]);
                this.ghostViewMatrix.postTranslate((i2 + i4) / 2.0f, (i6 + i8) / 2.0f);
                ghostView.setAnimationMatrix(this.ghostViewMatrix);
                Insets insets = (Insets) this.backgroundInsets$delegate.getValue();
                int i11 = state.top - insets.top;
                int i12 = state.left - insets.left;
                int i13 = state.right + insets.right;
                int i14 = state.bottom + insets.bottom;
                frameLayout.setTop(i11 - this.launchContainerLocation[1]);
                frameLayout.setBottom(i14 - this.launchContainerLocation[1]);
                frameLayout.setLeft(i12 - this.launchContainerLocation[0]);
                frameLayout.setRight(i13 - this.launchContainerLocation[0]);
                WrappedDrawable wrappedDrawable = this.backgroundDrawable;
                Intrinsics.checkNotNull(wrappedDrawable);
                Drawable drawable = wrappedDrawable.wrapped;
                if (drawable != null) {
                    setBackgroundCornerRadius(drawable, state.topCornerRadius, state.bottomCornerRadius);
                }
            } else if (ghostView.getVisibility() == 0) {
                ghostView.setVisibility(4);
                this.ghostedView.setTransitionVisibility(4);
                frameLayout.setVisibility(4);
            }
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationStart(boolean z) {
        int i;
        Matrix matrix;
        if (!(this.ghostedView.getParent() instanceof ViewGroup)) {
            Log.w("GhostedViewLaunchAnimatorController", "Skipping animation as ghostedView is not attached to a ViewGroup");
            return;
        }
        this.backgroundView = new FrameLayout(this.launchContainer.getContext());
        this.launchContainer.getOverlay().add(this.backgroundView);
        Drawable background = this.ghostedView.getBackground();
        if (background == null) {
            i = 255;
        } else {
            i = background.getAlpha();
        }
        this.startBackgroundAlpha = i;
        WrappedDrawable wrappedDrawable = new WrappedDrawable(background);
        this.backgroundDrawable = wrappedDrawable;
        FrameLayout frameLayout = this.backgroundView;
        if (frameLayout != null) {
            frameLayout.setBackground(wrappedDrawable);
        }
        GhostView addGhost = GhostView.addGhost(this.ghostedView, this.launchContainer);
        this.ghostView = addGhost;
        if (addGhost == null) {
            matrix = null;
        } else {
            matrix = addGhost.getAnimationMatrix();
        }
        if (matrix == null) {
            matrix = Matrix.IDENTITY_MATRIX;
        }
        matrix.getValues(this.initialGhostViewMatrixValues);
        Integer num = this.cujType;
        if (num != null) {
            int intValue = num.intValue();
            InteractionJankMonitor interactionJankMonitor = this.interactionJankMonitor;
            if (interactionJankMonitor != null) {
                interactionJankMonitor.begin(this.ghostedView, intValue);
            }
        }
    }

    public void setBackgroundCornerRadius(Drawable drawable, float f, float f2) {
        WrappedDrawable wrappedDrawable = this.backgroundDrawable;
        if (wrappedDrawable != null) {
            float[] fArr = wrappedDrawable.cornerRadii;
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            fArr[4] = f2;
            fArr[5] = f2;
            fArr[6] = f2;
            fArr[7] = f2;
            wrappedDrawable.invalidateSelf();
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void setLaunchContainer(ViewGroup viewGroup) {
        this.launchContainer = viewGroup;
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final ViewGroup getLaunchContainer() {
        return this.launchContainer;
    }
}
