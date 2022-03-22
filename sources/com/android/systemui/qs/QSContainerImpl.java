package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSContainerImpl extends FrameLayout implements Dumpable {
    public boolean mClippingEnabled;
    public int mFancyClippingBottom;
    public int mFancyClippingTop;
    public QuickStatusBarHeader mHeader;
    public QSCustomizer mQSCustomizer;
    public NonInterceptingScrollView mQSPanelContainer;
    public boolean mQsDisabled;
    public float mQsExpansion;
    public int mSideMargins;
    public final float[] mFancyClippingRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public final Path mFancyClippingPath = new Path();
    public int mHeightOverride = -1;
    public int mContentPadding = -1;

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public final boolean performClick() {
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        if (!this.mFancyClippingPath.isEmpty()) {
            canvas.translate(0.0f, -getTranslationY());
            canvas.clipOutPath(this.mFancyClippingPath);
            canvas.translate(0.0f, getTranslationY());
        }
        super.dispatchDraw(canvas);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + " updateClippingPath: top(" + this.mFancyClippingTop + ") bottom(" + this.mFancyClippingBottom + ") mClippingEnabled(" + this.mClippingEnabled + ")");
    }

    public final boolean isTransformedTouchPointInView(float f, float f2, View view, PointF pointF) {
        if (!this.mClippingEnabled || getTranslationY() + f2 <= this.mFancyClippingTop) {
            return super.isTransformedTouchPointInView(f, f2, view, pointF);
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public final void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        if (view != this.mQSPanelContainer) {
            super.measureChildWithMargins(view, i, i2, i3, i4);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelContainer.getLayoutParams();
        int size = View.MeasureSpec.getSize(i2);
        int paddingBottom = ((size - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin) - getPaddingBottom();
        int i3 = ((FrameLayout) this).mPaddingLeft + ((FrameLayout) this).mPaddingRight + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        this.mQSPanelContainer.measure(ViewGroup.getChildMeasureSpec(i, i3, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(paddingBottom, Integer.MIN_VALUE));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mQSPanelContainer.getMeasuredWidth() + i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        this.mQSCustomizer.measure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    public final void updateClippingPath() {
        this.mFancyClippingPath.reset();
        if (!this.mClippingEnabled) {
            invalidate();
            return;
        }
        this.mFancyClippingPath.addRoundRect(0.0f, this.mFancyClippingTop, getWidth(), this.mFancyClippingBottom, this.mFancyClippingRadii, Path.Direction.CW);
        invalidate();
    }

    public final void updateExpansion() {
        int i;
        int i2 = this.mHeightOverride;
        if (i2 == -1) {
            i2 = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            i = this.mQSCustomizer.getHeight();
        } else {
            i = Math.round(this.mQsExpansion * (i2 - this.mHeader.getHeight())) + this.mHeader.getHeight();
        }
        int i3 = this.mHeightOverride;
        if (i3 == -1) {
            i3 = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            this.mQSCustomizer.getHeight();
        } else {
            Math.round(this.mQsExpansion * (((this.mQSPanelContainer.getScrollRange() + i3) - this.mQSPanelContainer.getScrollY()) - this.mHeader.getHeight()));
            this.mHeader.getHeight();
        }
        setBottom(getTop() + i);
    }

    public final void updateResources(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        int i;
        boolean z;
        NonInterceptingScrollView nonInterceptingScrollView = this.mQSPanelContainer;
        int paddingStart = nonInterceptingScrollView.getPaddingStart();
        Context context = ((FrameLayout) this).mContext;
        Resources resources = context.getResources();
        if (Utils.shouldUseSplitNotificationShade(resources)) {
            i = resources.getDimensionPixelSize(2131166872);
        } else {
            i = SystemBarUtils.getQuickQsOffsetHeight(context);
        }
        nonInterceptingScrollView.setPaddingRelative(paddingStart, i, this.mQSPanelContainer.getPaddingEnd(), this.mQSPanelContainer.getPaddingBottom());
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131166677);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(2131166674);
        if (dimensionPixelSize2 == this.mContentPadding && dimensionPixelSize == this.mSideMargins) {
            z = false;
        } else {
            z = true;
        }
        this.mContentPadding = dimensionPixelSize2;
        this.mSideMargins = dimensionPixelSize;
        if (z) {
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                View childAt = getChildAt(i2);
                if (childAt != this.mQSCustomizer) {
                    if (!(childAt instanceof FooterActionsView)) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int i3 = this.mSideMargins;
                        layoutParams.rightMargin = i3;
                        layoutParams.leftMargin = i3;
                    }
                    if (childAt == this.mQSPanelContainer) {
                        int i4 = this.mContentPadding;
                        Objects.requireNonNull(qSPanelController);
                        QSPanel qSPanel = (QSPanel) qSPanelController.mView;
                        UniqueObjectHostView hostView = qSPanelController.mMediaHost.getHostView();
                        Objects.requireNonNull(qSPanel);
                        qSPanel.mContentMarginEnd = i4;
                        qSPanel.updateMediaHostContentMargins(hostView);
                        int i5 = this.mSideMargins;
                        QSPanel qSPanel2 = (QSPanel) qSPanelController.mView;
                        Objects.requireNonNull(qSPanel2);
                        QSPanel.QSTileLayout qSTileLayout = qSPanel2.mTileLayout;
                        if (qSTileLayout instanceof PagedTileLayout) {
                            PagedTileLayout pagedTileLayout = (PagedTileLayout) qSTileLayout;
                            Objects.requireNonNull(pagedTileLayout);
                            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) pagedTileLayout.getLayoutParams();
                            int i6 = -i5;
                            marginLayoutParams.setMarginStart(i6);
                            marginLayoutParams.setMarginEnd(i6);
                            pagedTileLayout.setLayoutParams(marginLayoutParams);
                            int size = pagedTileLayout.mPages.size();
                            for (int i7 = 0; i7 < size; i7++) {
                                TileLayout tileLayout = pagedTileLayout.mPages.get(i7);
                                tileLayout.setPadding(i5, tileLayout.getPaddingTop(), i5, tileLayout.getPaddingBottom());
                            }
                        }
                    } else if (childAt == this.mHeader) {
                        int i8 = this.mContentPadding;
                        Objects.requireNonNull(quickStatusBarHeaderController);
                        QuickQSPanelController quickQSPanelController = quickStatusBarHeaderController.mQuickQSPanelController;
                        Objects.requireNonNull(quickQSPanelController);
                        QuickQSPanel quickQSPanel = (QuickQSPanel) quickQSPanelController.mView;
                        UniqueObjectHostView hostView2 = quickQSPanelController.mMediaHost.getHostView();
                        Objects.requireNonNull(quickQSPanel);
                        quickQSPanel.mContentMarginEnd = i8;
                        quickQSPanel.updateMediaHostContentMargins(hostView2);
                    } else {
                        childAt.setPaddingRelative(this.mContentPadding, childAt.getPaddingTop(), this.mContentPadding, childAt.getPaddingBottom());
                    }
                }
            }
        }
    }

    public QSContainerImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mQSPanelContainer = (NonInterceptingScrollView) findViewById(2131427951);
        this.mHeader = (QuickStatusBarHeader) findViewById(2131428079);
        this.mQSCustomizer = (QSCustomizer) findViewById(2131428647);
        setImportantForAccessibility(2);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateExpansion();
        updateClippingPath();
    }
}
