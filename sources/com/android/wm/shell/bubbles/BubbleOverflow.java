package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.PathParser;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.bubbles.BadgedImageView;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleOverflow.kt */
/* loaded from: classes.dex */
public final class BubbleOverflow implements BubbleViewProvider {
    public Bitmap bitmap;
    public final Context context;
    public int dotColor;
    public Path dotPath;
    public final LayoutInflater inflater;
    public int overflowIconInset;
    public final BubblePositioner positioner;
    public boolean showDot;
    public BubbleExpandedView expandedView = null;
    public BadgedImageView overflowBtn = null;

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Bitmap getAppBadge() {
        return null;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final String getKey() {
        return "Overflow";
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final void setTaskViewVisibility() {
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Bitmap getBubbleIcon() {
        Bitmap bitmap = this.bitmap;
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Path getDotPath() {
        Path path = this.dotPath;
        if (path == null) {
            return null;
        }
        return path;
    }

    /* renamed from: getIconView */
    public final BadgedImageView getIconView$1() {
        if (this.overflowBtn == null) {
            View inflate = this.inflater.inflate(2131624022, (ViewGroup) null, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.wm.shell.bubbles.BadgedImageView");
            BadgedImageView badgedImageView = (BadgedImageView) inflate;
            this.overflowBtn = badgedImageView;
            badgedImageView.initialize(this.positioner);
            BadgedImageView badgedImageView2 = this.overflowBtn;
            if (badgedImageView2 != null) {
                badgedImageView2.setContentDescription(this.context.getResources().getString(2131952065));
            }
            BubblePositioner bubblePositioner = this.positioner;
            Objects.requireNonNull(bubblePositioner);
            int i = bubblePositioner.mBubbleSize;
            BadgedImageView badgedImageView3 = this.overflowBtn;
            if (badgedImageView3 != null) {
                badgedImageView3.setLayoutParams(new FrameLayout.LayoutParams(i, i));
            }
            updateBtnTheme();
        }
        return this.overflowBtn;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView == null) {
            return -1;
        }
        Intrinsics.checkNotNull(bubbleExpandedView);
        return bubbleExpandedView.mTaskId;
    }

    public final void updateBtnTheme() {
        Drawable drawable;
        Drawable drawable2;
        Resources resources = this.context.getResources();
        TypedValue typedValue = new TypedValue();
        this.context.getTheme().resolveAttribute(17956900, typedValue, true);
        Path path = null;
        int color = resources.getColor(typedValue.resourceId, null);
        this.dotColor = color;
        int color2 = resources.getColor(17170499);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (!(badgedImageView == null || (drawable2 = badgedImageView.mBubbleIcon.getDrawable()) == null)) {
            drawable2.setTint(color2);
        }
        BubbleIconFactory bubbleIconFactory = new BubbleIconFactory(this.context);
        BadgedImageView badgedImageView2 = this.overflowBtn;
        if (badgedImageView2 == null) {
            drawable = null;
        } else {
            drawable = badgedImageView2.mBubbleIcon.getDrawable();
        }
        this.bitmap = bubbleIconFactory.createBadgedIconBitmap(new AdaptiveIconDrawable(new ColorDrawable(color), new InsetDrawable(drawable, this.overflowIconInset)), null).icon;
        this.dotPath = PathParser.createPathFromPathData(resources.getString(17039972));
        IconNormalizer normalizer = bubbleIconFactory.getNormalizer();
        BadgedImageView iconView = getIconView$1();
        Intrinsics.checkNotNull(iconView);
        float scale = normalizer.getScale(iconView.mBubbleIcon.getDrawable(), null, null, null);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale, 50.0f, 50.0f);
        Path path2 = this.dotPath;
        if (path2 != null) {
            path = path2;
        }
        path.transform(matrix);
        BadgedImageView badgedImageView3 = this.overflowBtn;
        if (badgedImageView3 != null) {
            badgedImageView3.setRenderedBubble(this);
        }
        BadgedImageView badgedImageView4 = this.overflowBtn;
        if (badgedImageView4 != null) {
            badgedImageView4.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        }
    }

    public final void updateResources() {
        this.overflowIconInset = this.context.getResources().getDimensionPixelSize(2131165443);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            BubblePositioner bubblePositioner = this.positioner;
            Objects.requireNonNull(bubblePositioner);
            int i = bubblePositioner.mBubbleSize;
            BubblePositioner bubblePositioner2 = this.positioner;
            Objects.requireNonNull(bubblePositioner2);
            badgedImageView.setLayoutParams(new FrameLayout.LayoutParams(i, bubblePositioner2.mBubbleSize));
        }
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.updateDimensions();
        }
    }

    public BubbleOverflow(Context context, BubblePositioner bubblePositioner) {
        this.context = context;
        this.positioner = bubblePositioner;
        this.inflater = LayoutInflater.from(context);
        updateResources();
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final int getDotColor() {
        return this.dotColor;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final BubbleExpandedView getExpandedView() {
        return this.expandedView;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final boolean showDot() {
        return this.showDot;
    }
}
