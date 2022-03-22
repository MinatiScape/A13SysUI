package com.android.systemui.wallet.ui;

import android.content.Context;
import android.graphics.Rect;
import android.service.quickaccesswallet.WalletCard;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import com.android.systemui.wallet.ui.WalletScreenController;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class WalletCardCarousel extends RecyclerView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public float mCardCenterToScreenCenterDistancePx;
    public float mCardEdgeToCenterDistance;
    public int mCardHeightPx;
    public int mCardMarginPx;
    public OnCardScrollListener mCardScrollListener;
    public int mCardWidthPx;
    public int mCenteredAdapterPosition;
    public float mCornerRadiusPx;
    public float mEdgeToCenterDistance;
    public int mExpectedViewWidth;
    public OnSelectionListener mSelectionListener;
    public final Rect mSystemGestureExclusionZone;
    public int mTotalCardWidth;
    public final WalletCardCarouselAdapter mWalletCardCarouselAdapter;

    /* loaded from: classes.dex */
    public class CardCarouselAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
        public CardCarouselAccessibilityDelegate(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32768) {
                WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
                Objects.requireNonNull(walletCardCarousel);
                walletCardCarousel.scrollToPosition(RecyclerView.getChildAdapterPosition(view));
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    /* loaded from: classes.dex */
    public class CardCarouselScrollListener extends RecyclerView.OnScrollListener {
        public int mOldState = -1;

        public CardCarouselScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public final void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0 && i != this.mOldState) {
                WalletCardCarousel.this.performHapticFeedback(1);
            }
            this.mOldState = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public final void onScrolled(RecyclerView recyclerView, int i, int i2) {
            WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
            int i3 = -1;
            walletCardCarousel.mCenteredAdapterPosition = -1;
            walletCardCarousel.mEdgeToCenterDistance = Float.MAX_VALUE;
            walletCardCarousel.mCardCenterToScreenCenterDistancePx = Float.MAX_VALUE;
            for (int i4 = 0; i4 < WalletCardCarousel.this.getChildCount(); i4++) {
                WalletCardCarousel walletCardCarousel2 = WalletCardCarousel.this;
                walletCardCarousel2.updateCardView(walletCardCarousel2.getChildAt(i4));
            }
            WalletCardCarousel walletCardCarousel3 = WalletCardCarousel.this;
            int i5 = walletCardCarousel3.mCenteredAdapterPosition;
            if (!(i5 == -1 || i == 0)) {
                if (walletCardCarousel3.mEdgeToCenterDistance > 0.0f) {
                    i3 = 1;
                }
                int i6 = i5 + i3;
                if (i6 >= 0 && i6 < walletCardCarousel3.mWalletCardCarouselAdapter.mData.size()) {
                    WalletCardCarousel walletCardCarousel4 = WalletCardCarousel.this;
                    float abs = Math.abs(WalletCardCarousel.this.mEdgeToCenterDistance);
                    WalletCardCarousel walletCardCarousel5 = WalletCardCarousel.this;
                    ((WalletView) walletCardCarousel5.mCardScrollListener).onCardScroll(walletCardCarousel4.mWalletCardCarouselAdapter.mData.get(walletCardCarousel4.mCenteredAdapterPosition), WalletCardCarousel.this.mWalletCardCarouselAdapter.mData.get(i6), abs / walletCardCarousel5.mCardEdgeToCenterDistance);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class CarouselSnapHelper extends PagerSnapHelper {
        public CarouselSnapHelper() {
        }

        @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
        public final RecyclerView.SmoothScroller createScroller(final RecyclerView.LayoutManager layoutManager) {
            return new LinearSmoothScroller(WalletCardCarousel.this.getContext()) { // from class: com.android.systemui.wallet.ui.WalletCardCarousel.CarouselSnapHelper.1
                @Override // androidx.recyclerview.widget.LinearSmoothScroller
                public final float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 200.0f / displayMetrics.densityDpi;
                }

                @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
                public final void onTargetFound(View view, RecyclerView.SmoothScroller.Action action) {
                    int[] calculateDistanceToFinalSnap = CarouselSnapHelper.this.calculateDistanceToFinalSnap(layoutManager, view);
                    int i = calculateDistanceToFinalSnap[0];
                    int i2 = calculateDistanceToFinalSnap[1];
                    int calculateTimeForDeceleration = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(i2)));
                    if (calculateTimeForDeceleration > 0) {
                        action.update(i, i2, calculateTimeForDeceleration, this.mDecelerateInterpolator);
                    }
                }

                @Override // androidx.recyclerview.widget.LinearSmoothScroller
                public final int calculateTimeForScrolling(int i) {
                    return Math.min(80, super.calculateTimeForScrolling(i));
                }
            };
        }

        @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
        public final View findSnapView(RecyclerView.LayoutManager layoutManager) {
            View findSnapView = super.findSnapView(layoutManager);
            if (findSnapView == null) {
                return null;
            }
            WalletCardViewInfo walletCardViewInfo = ((WalletCardViewHolder) findSnapView.getTag()).mCardViewInfo;
            ((WalletScreenController) WalletCardCarousel.this.mSelectionListener).onCardSelected(walletCardViewInfo);
            ((WalletView) WalletCardCarousel.this.mCardScrollListener).onCardScroll(walletCardViewInfo, walletCardViewInfo, 0.0f);
            return findSnapView;
        }
    }

    /* loaded from: classes.dex */
    public interface OnCardScrollListener {
    }

    /* loaded from: classes.dex */
    public interface OnSelectionListener {
    }

    /* loaded from: classes.dex */
    public class WalletCardCarouselAdapter extends RecyclerView.Adapter<WalletCardViewHolder> {
        public List<WalletCardViewInfo> mData = Collections.EMPTY_LIST;

        public WalletCardCarouselAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            return this.mData.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final long getItemId(int i) {
            return this.mData.get(i).getCardId().hashCode();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(WalletCardViewHolder walletCardViewHolder, final int i) {
            WalletCardViewHolder walletCardViewHolder2 = walletCardViewHolder;
            final WalletCardViewInfo walletCardViewInfo = this.mData.get(i);
            walletCardViewHolder2.mCardViewInfo = walletCardViewInfo;
            if (walletCardViewInfo.getCardId().isEmpty()) {
                walletCardViewHolder2.mImageView.setScaleType(ImageView.ScaleType.CENTER);
            }
            walletCardViewHolder2.mImageView.setImageDrawable(walletCardViewInfo.getCardDrawable());
            walletCardViewHolder2.mCardView.setContentDescription(walletCardViewInfo.getContentDescription());
            walletCardViewHolder2.mCardView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.wallet.ui.WalletCardCarousel$WalletCardCarouselAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    WalletCard walletCard;
                    WalletCardCarousel.WalletCardCarouselAdapter walletCardCarouselAdapter = WalletCardCarousel.WalletCardCarouselAdapter.this;
                    int i2 = i;
                    WalletCardViewInfo walletCardViewInfo2 = walletCardViewInfo;
                    Objects.requireNonNull(walletCardCarouselAdapter);
                    WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
                    if (i2 != walletCardCarousel.mCenteredAdapterPosition) {
                        walletCardCarousel.smoothScrollToPosition(i2);
                        return;
                    }
                    WalletScreenController walletScreenController = (WalletScreenController) walletCardCarousel.mSelectionListener;
                    Objects.requireNonNull(walletScreenController);
                    if ((walletScreenController.mKeyguardStateController.isUnlocked() || !walletScreenController.mFalsingManager.isFalseTap(1)) && (walletCardViewInfo2 instanceof WalletScreenController.QAWalletCardViewInfo) && (walletCard = ((WalletScreenController.QAWalletCardViewInfo) walletCardViewInfo2).mWalletCard) != null && walletCard.getPendingIntent() != null) {
                        if (!walletScreenController.mKeyguardStateController.isUnlocked()) {
                            walletScreenController.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_CARD_CLICK);
                        }
                        walletScreenController.mUiEventLogger.log(WalletUiEvent.QAW_CLICK_CARD);
                        walletScreenController.mActivityStarter.startPendingIntentDismissingKeyguard(walletCardViewInfo2.getPendingIntent());
                    }
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            View inflate = LayoutInflater.from(recyclerView.getContext()).inflate(2131624654, (ViewGroup) recyclerView, false);
            WalletCardViewHolder walletCardViewHolder = new WalletCardViewHolder(inflate);
            CardView cardView = walletCardViewHolder.mCardView;
            cardView.setRadius(WalletCardCarousel.this.mCornerRadiusPx);
            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
            WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
            Objects.requireNonNull(walletCardCarousel);
            layoutParams.width = walletCardCarousel.mCardWidthPx;
            WalletCardCarousel walletCardCarousel2 = WalletCardCarousel.this;
            Objects.requireNonNull(walletCardCarousel2);
            layoutParams.height = walletCardCarousel2.mCardHeightPx;
            inflate.setTag(walletCardViewHolder);
            return walletCardViewHolder;
        }
    }

    public WalletCardCarousel(Context context) {
        this(context, null);
    }

    public WalletCardCarousel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSystemGestureExclusionZone = new Rect();
        this.mCenteredAdapterPosition = -1;
        this.mEdgeToCenterDistance = Float.MAX_VALUE;
        this.mCardCenterToScreenCenterDistancePx = Float.MAX_VALUE;
        setLayoutManager(new LinearLayoutManager(0));
        addOnScrollListener(new CardCarouselScrollListener());
        new CarouselSnapHelper().attachToRecyclerView(this);
        WalletCardCarouselAdapter walletCardCarouselAdapter = new WalletCardCarouselAdapter();
        this.mWalletCardCarouselAdapter = walletCardCarouselAdapter;
        walletCardCarouselAdapter.setHasStableIds(true);
        setAdapter(walletCardCarouselAdapter);
        ViewCompat.setAccessibilityDelegate(this, new CardCarouselAccessibilityDelegate(this));
        addItemDecoration(new DotIndicatorDecoration(getContext()));
    }

    public final void updatePadding(int i) {
        int i2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int max = Math.max(0, ((i - this.mTotalCardWidth) / 2) - this.mCardMarginPx);
        setPadding(max, getPaddingTop(), max, getPaddingBottom());
        WalletCardCarouselAdapter walletCardCarouselAdapter = this.mWalletCardCarouselAdapter;
        if (walletCardCarouselAdapter != null && walletCardCarouselAdapter.getItemCount() > 0 && (i2 = this.mCenteredAdapterPosition) != -1 && (findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(i2)) != null) {
            View view = findViewHolderForAdapterPosition.itemView;
            scrollBy(((view.getRight() + view.getLeft()) / 2) - ((getRight() + getLeft()) / 2), 0);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int width = getWidth();
        if (this.mWalletCardCarouselAdapter.getItemCount() > 1 && width < this.mTotalCardWidth * 1.5d) {
            this.mSystemGestureExclusionZone.set(0, 0, width, getHeight());
            setSystemGestureExclusionRects(Collections.singletonList(this.mSystemGestureExclusionZone));
        }
        if (width != this.mExpectedViewWidth) {
            updatePadding(width);
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(final View view) {
        super.onViewAdded(view);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int i = this.mCardMarginPx;
        ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = i;
        ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = i;
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.wallet.ui.WalletCardCarousel$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
                View view3 = view;
                int i10 = WalletCardCarousel.$r8$clinit;
                Objects.requireNonNull(walletCardCarousel);
                walletCardCarousel.updateCardView(view3);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void scrollToPosition(int i) {
        super.scrollToPosition(i);
        ((WalletScreenController) this.mSelectionListener).onCardSelected(this.mWalletCardCarouselAdapter.mData.get(i));
    }

    public final void updateCardView(View view) {
        int i;
        CardView cardView = ((WalletCardViewHolder) view.getTag()).mCardView;
        float width = getWidth() / 2.0f;
        float left = (view.getLeft() + view.getRight()) / 2.0f;
        float f = left - width;
        float max = Math.max(0.83f, 1.0f - Math.abs(f / view.getWidth()));
        cardView.setScaleX(max);
        cardView.setScaleY(max);
        if (left < width) {
            i = view.getRight() + this.mCardMarginPx;
        } else {
            i = view.getLeft() - this.mCardMarginPx;
        }
        if (Math.abs(f) < this.mCardCenterToScreenCenterDistancePx && RecyclerView.getChildAdapterPosition(view) != -1) {
            this.mCenteredAdapterPosition = RecyclerView.getChildAdapterPosition(view);
            this.mEdgeToCenterDistance = i - width;
            this.mCardCenterToScreenCenterDistancePx = Math.abs(f);
        }
    }
}
