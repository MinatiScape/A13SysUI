package com.android.systemui.wallet.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import java.util.Objects;
/* loaded from: classes.dex */
public class WalletView extends FrameLayout implements WalletCardCarousel.OnCardScrollListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Button mActionButton;
    public final float mAnimationTranslationX;
    public final Button mAppButton;
    public final WalletCardCarousel mCardCarousel;
    public final ViewGroup mCardCarouselContainer;
    public final TextView mCardLabel;
    public View.OnClickListener mDeviceLockedActionOnClickListener;
    public final ViewGroup mEmptyStateView;
    public final TextView mErrorView;
    public FalsingCollector mFalsingCollector;
    public final ImageView mIcon;
    public boolean mIsDeviceLocked;
    public boolean mIsUdfpsEnabled;
    public final Interpolator mOutInterpolator;
    public View.OnClickListener mShowWalletAppOnClickListener;
    public final Button mToolbarAppButton;

    public WalletView(Context context) {
        this(context, null);
    }

    public WalletView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsDeviceLocked = false;
        this.mIsUdfpsEnabled = false;
        View.inflate(context, 2131624656, this);
        this.mCardCarouselContainer = (ViewGroup) requireViewById(2131427665);
        WalletCardCarousel walletCardCarousel = (WalletCardCarousel) requireViewById(2131427664);
        this.mCardCarousel = walletCardCarousel;
        Objects.requireNonNull(walletCardCarousel);
        walletCardCarousel.mCardScrollListener = this;
        this.mIcon = (ImageView) requireViewById(2131428102);
        this.mCardLabel = (TextView) requireViewById(2131428201);
        this.mAppButton = (Button) requireViewById(2131429238);
        this.mToolbarAppButton = (Button) requireViewById(2131429242);
        this.mActionButton = (Button) requireViewById(2131429237);
        this.mErrorView = (TextView) requireViewById(2131427939);
        this.mEmptyStateView = (ViewGroup) requireViewById(2131429240);
        this.mOutInterpolator = AnimationUtils.loadInterpolator(context, 17563650);
        Objects.requireNonNull(walletCardCarousel);
        this.mAnimationTranslationX = walletCardCarousel.mCardWidthPx / 4.0f;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        FalsingCollector falsingCollector = this.mFalsingCollector;
        if (falsingCollector != null) {
            falsingCollector.onTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        FalsingCollector falsingCollector2 = this.mFalsingCollector;
        if (falsingCollector2 != null) {
            falsingCollector2.onMotionEventComplete();
        }
        return dispatchTouchEvent;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mCardCarousel.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0166 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void showCardCarousel(java.util.List<com.android.systemui.wallet.ui.WalletCardViewInfo> r10, int r11, boolean r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 359
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wallet.ui.WalletView.showCardCarousel(java.util.List, int, boolean, boolean):void");
    }

    public final void showEmptyStateView(Drawable drawable, CharSequence charSequence, CharSequence charSequence2, WalletScreenController$$ExternalSyntheticLambda0 walletScreenController$$ExternalSyntheticLambda0) {
        this.mEmptyStateView.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mCardCarousel.setVisibility(8);
        this.mIcon.setImageDrawable(drawable);
        this.mIcon.setContentDescription(charSequence);
        this.mCardLabel.setText(2131953524);
        ((ImageView) this.mEmptyStateView.requireViewById(2131427913)).setImageDrawable(((FrameLayout) this).mContext.getDrawable(2131232231));
        ((TextView) this.mEmptyStateView.requireViewById(2131427914)).setText(charSequence2);
        this.mEmptyStateView.setOnClickListener(walletScreenController$$ExternalSyntheticLambda0);
    }

    public final void onCardScroll(WalletCardViewInfo walletCardViewInfo, WalletCardViewInfo walletCardViewInfo2, float f) {
        CharSequence charSequence;
        CharSequence[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        if (split.length == 2) {
            charSequence = split[0];
        } else {
            charSequence = walletCardViewInfo.getLabel();
        }
        Context context = ((FrameLayout) this).mContext;
        Drawable icon = walletCardViewInfo.getIcon();
        if (icon != null) {
            icon.setTint(Utils.getColorAttrDefaultColor(context, 17956900));
        }
        renderActionButton(walletCardViewInfo, this.mIsDeviceLocked, this.mIsUdfpsEnabled);
        if (walletCardViewInfo.isUiEquivalent(walletCardViewInfo2)) {
            this.mCardLabel.setAlpha(1.0f);
            this.mIcon.setAlpha(1.0f);
            this.mActionButton.setAlpha(1.0f);
            return;
        }
        this.mCardLabel.setText(charSequence);
        this.mIcon.setImageDrawable(icon);
        this.mCardLabel.setAlpha(f);
        this.mIcon.setAlpha(f);
        this.mActionButton.setAlpha(f);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        WalletCardCarousel walletCardCarousel = this.mCardCarousel;
        int width = getWidth();
        Objects.requireNonNull(walletCardCarousel);
        if (walletCardCarousel.mExpectedViewWidth != width) {
            walletCardCarousel.mExpectedViewWidth = width;
            Resources resources = walletCardCarousel.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int round = Math.round(Math.min(width, Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels)) * 0.69f);
            walletCardCarousel.mCardWidthPx = round;
            walletCardCarousel.mCardHeightPx = Math.round(round / 1.5909091f);
            float f = walletCardCarousel.mCardWidthPx;
            walletCardCarousel.mCornerRadiusPx = 0.035714287f * f;
            walletCardCarousel.mCardMarginPx = Math.round(f * (-0.03f));
            int dimensionPixelSize = (resources.getDimensionPixelSize(2131165476) * 2) + walletCardCarousel.mCardWidthPx;
            walletCardCarousel.mTotalCardWidth = dimensionPixelSize;
            walletCardCarousel.mCardEdgeToCenterDistance = dimensionPixelSize / 2.0f;
            walletCardCarousel.updatePadding(width);
            WalletCardCarousel.OnSelectionListener onSelectionListener = walletCardCarousel.mSelectionListener;
            if (onSelectionListener != null) {
                ((WalletScreenController) onSelectionListener).queryWalletCards();
            }
        }
    }

    public final void renderActionButton(WalletCardViewInfo walletCardViewInfo, boolean z, boolean z2) {
        String str;
        View.OnClickListener onClickListener;
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        if (split.length == 2) {
            str = split[1];
        } else {
            str = null;
        }
        if (z2 || str == null) {
            this.mActionButton.setVisibility(8);
            return;
        }
        this.mActionButton.setVisibility(0);
        this.mActionButton.setText(str);
        Button button = this.mActionButton;
        if (z) {
            onClickListener = this.mDeviceLockedActionOnClickListener;
        } else {
            onClickListener = new WalletView$$ExternalSyntheticLambda0(walletCardViewInfo, 0);
        }
        button.setOnClickListener(onClickListener);
    }

    @VisibleForTesting
    public ViewGroup getCardCarouselContainer() {
        return this.mCardCarouselContainer;
    }

    @VisibleForTesting
    public TextView getCardLabel() {
        return this.mCardLabel;
    }

    @VisibleForTesting
    public ViewGroup getEmptyStateView() {
        return this.mEmptyStateView;
    }

    @VisibleForTesting
    public TextView getErrorView() {
        return this.mErrorView;
    }
}
