package com.google.android.systemui.smartspace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.savedstate.R$id;
import androidx.viewpager.widget.ViewPager;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda7;
import com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.CardPagerAdapter;
import com.google.android.systemui.smartspace.uitemplate.BaseTemplateCard;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class BcSmartspaceView extends FrameLayout implements BcSmartspaceDataPlugin.SmartspaceTargetListener, BcSmartspaceDataPlugin.SmartspaceView {
    public static ArraySet<String> mLastReceivedTargets = new ArraySet<>();
    public static int sLastSurface = -1;
    public BcSmartspaceDataPlugin mDataProvider;
    public PageIndicator mPageIndicator;
    public List<? extends Parcelable> mPendingTargets;
    public Animator mRunningAnimation;
    public ViewPager mViewPager;
    public boolean mIsAodEnabled = false;
    public int mCardPosition = 0;
    public boolean mAnimateSmartspaceUpdate = false;
    public int mScrollState = 0;
    public final AnonymousClass1 mAodObserver = new ContentObserver(new Handler()) { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.1
        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
            ArraySet<String> arraySet = BcSmartspaceView.mLastReceivedTargets;
            Objects.requireNonNull(bcSmartspaceView);
            Context context = bcSmartspaceView.getContext();
            boolean z2 = false;
            if (Settings.Secure.getIntForUser(context.getContentResolver(), "doze_always_on", 0, context.getUserId()) == 1) {
                z2 = true;
            }
            bcSmartspaceView.mIsAodEnabled = z2;
        }
    };
    public final CardPagerAdapter mAdapter = new CardPagerAdapter(this);
    public final AnonymousClass2 mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.2
        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public final void onPageScrollStateChanged(int i) {
            List<? extends Parcelable> list;
            BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
            bcSmartspaceView.mScrollState = i;
            if (i == 0 && (list = bcSmartspaceView.mPendingTargets) != null) {
                bcSmartspaceView.onSmartspaceTargetsUpdated(list);
                BcSmartspaceView.this.mPendingTargets = null;
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public final void onPageScrolled(int i, float f, int i2) {
            int i3;
            PageIndicator pageIndicator = BcSmartspaceView.this.mPageIndicator;
            if (pageIndicator != null) {
                int i4 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i4 != 0 || i != pageIndicator.mCurrentPageIndex) {
                    if (i < 0) {
                        Objects.requireNonNull(pageIndicator);
                    } else if (i < pageIndicator.getChildCount() - 1) {
                        ImageView imageView = (ImageView) pageIndicator.getChildAt(i);
                        int i5 = i + 1;
                        ImageView imageView2 = (ImageView) pageIndicator.getChildAt(i5);
                        if (imageView != null && imageView2 != null) {
                            imageView.setAlpha(((1.0f - f) * 0.6f) + 0.4f);
                            imageView2.setAlpha((0.6f * f) + 0.4f);
                            Context context = pageIndicator.getContext();
                            Object[] objArr = new Object[2];
                            if (f < 0.5d) {
                                i3 = i5;
                            } else {
                                i3 = i + 2;
                            }
                            objArr[0] = Integer.valueOf(i3);
                            objArr[1] = Integer.valueOf(pageIndicator.mNumPages);
                            pageIndicator.setContentDescription(context.getString(2131951811, objArr));
                            if (i4 == 0 || f >= 0.99f) {
                                if (i4 != 0) {
                                    i = i5;
                                }
                                pageIndicator.mCurrentPageIndex = i;
                            }
                        }
                    }
                }
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public final void onPageSelected(int i) {
            BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
            SmartspaceTarget targetAtPosition = bcSmartspaceView.mAdapter.getTargetAtPosition(bcSmartspaceView.mCardPosition);
            BcSmartspaceView bcSmartspaceView2 = BcSmartspaceView.this;
            bcSmartspaceView2.mCardPosition = i;
            SmartspaceTarget targetAtPosition2 = bcSmartspaceView2.mAdapter.getTargetAtPosition(i);
            BcSmartspaceView bcSmartspaceView3 = BcSmartspaceView.this;
            bcSmartspaceView3.logSmartspaceEvent(targetAtPosition2, bcSmartspaceView3.mCardPosition, BcSmartspaceEvent.SMARTSPACE_CARD_SEEN);
            if (BcSmartspaceView.this.mDataProvider == null) {
                Log.w("BcSmartspaceView", "Cannot notify target hidden/shown smartspace events: data provider null");
                return;
            }
            if (targetAtPosition == null) {
                Log.w("BcSmartspaceView", "Cannot notify target hidden smartspace event: previous target is null.");
            } else {
                SmartspaceTargetEvent.Builder builder = new SmartspaceTargetEvent.Builder(3);
                builder.setSmartspaceTarget(targetAtPosition);
                SmartspaceAction baseAction = targetAtPosition.getBaseAction();
                if (baseAction != null) {
                    builder.setSmartspaceActionId(baseAction.getId());
                }
                BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder.build());
            }
            SmartspaceTargetEvent.Builder builder2 = new SmartspaceTargetEvent.Builder(2);
            builder2.setSmartspaceTarget(targetAtPosition2);
            SmartspaceAction baseAction2 = targetAtPosition2.getBaseAction();
            if (baseAction2 != null) {
                builder2.setSmartspaceActionId(baseAction2.getId());
            }
            BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder2.build());
        }
    };

    public final void animateSmartspaceUpdate(final LinearLayout linearLayout) {
        if (this.mRunningAnimation == null && linearLayout.getParent() == null) {
            final ViewGroup viewGroup = (ViewGroup) this.mViewPager.getParent();
            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(this.mViewPager.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mViewPager.getHeight(), 1073741824));
            linearLayout.layout(this.mViewPager.getLeft(), this.mViewPager.getTop(), this.mViewPager.getRight(), this.mViewPager.getBottom());
            AnimatorSet animatorSet = new AnimatorSet();
            float dimension = getContext().getResources().getDimension(2131165708);
            animatorSet.play(ObjectAnimator.ofFloat(linearLayout, View.TRANSLATION_Y, 0.0f, (-getHeight()) - dimension));
            animatorSet.play(ObjectAnimator.ofFloat(linearLayout, View.ALPHA, 1.0f, 0.0f));
            animatorSet.play(ObjectAnimator.ofFloat(this.mViewPager, View.TRANSLATION_Y, getHeight() + dimension, 0.0f));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    viewGroup.getOverlay().remove(linearLayout);
                    BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                    bcSmartspaceView.mRunningAnimation = null;
                    bcSmartspaceView.mAnimateSmartspaceUpdate = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    viewGroup.getOverlay().add(linearLayout);
                }
            });
            this.mRunningAnimation = animatorSet;
            animatorSet.start();
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final int getSelectedPage() {
        ViewPager viewPager = this.mViewPager;
        Objects.requireNonNull(viewPager);
        return viewPager.mCurItem;
    }

    public final void logSmartspaceEvent(SmartspaceTarget smartspaceTarget, int i, BcSmartspaceEvent bcSmartspaceEvent) {
        int i2;
        if (bcSmartspaceEvent == BcSmartspaceEvent.SMARTSPACE_CARD_RECEIVED) {
            try {
                i2 = (int) Instant.now().minusMillis(smartspaceTarget.getCreationTimeMillis()).toEpochMilli();
            } catch (ArithmeticException | DateTimeException e) {
                Log.e("BcSmartspaceView", "received_latency_millis will be -1 due to exception ", e);
                i2 = -1;
            }
        } else {
            i2 = 0;
        }
        BcSmartspaceCardLoggingInfo.Builder builder = new BcSmartspaceCardLoggingInfo.Builder();
        builder.mInstanceId = R$id.create(smartspaceTarget).intValue();
        builder.mFeatureType = smartspaceTarget.getFeatureType();
        String packageName = getContext().getPackageName();
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        builder.mDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(packageName, cardPagerAdapter.mDozeAmount);
        builder.mRank = i;
        builder.mCardinality = this.mAdapter.getCount();
        builder.mReceivedLatency = i2;
        BcSmartspaceLogger.log(bcSmartspaceEvent, new BcSmartspaceCardLoggingInfo(builder));
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewPager.setAdapter(this.mAdapter);
        ViewPager viewPager = this.mViewPager;
        AnonymousClass2 r2 = this.mOnPageChangeListener;
        Objects.requireNonNull(viewPager);
        if (viewPager.mOnPageChangeListeners == null) {
            viewPager.mOnPageChangeListeners = new ArrayList();
        }
        viewPager.mOnPageChangeListeners.add(r2);
        this.mPageIndicator.setNumPages(this.mAdapter.getCount());
        try {
            boolean z = false;
            getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("doze_always_on"), false, this.mAodObserver, -1);
            Context context = getContext();
            if (Settings.Secure.getIntForUser(context.getContentResolver(), "doze_always_on", 0, context.getUserId()) == 1) {
                z = true;
            }
            this.mIsAodEnabled = z;
        } catch (Exception e) {
            Log.w("BcSmartspaceView", "Unable to register Doze Always on content observer.", e);
        }
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            registerDataProvider(bcSmartspaceDataPlugin);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public final void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        boolean z;
        int i;
        BaseTemplateCard baseTemplateCard;
        BcSmartspaceCard bcSmartspaceCard;
        boolean z2 = true;
        if (this.mScrollState == 0 || this.mAdapter.getCount() <= 1) {
            if (getLayoutDirection() == 1) {
                z = true;
            } else {
                z = false;
            }
            ViewPager viewPager = this.mViewPager;
            Objects.requireNonNull(viewPager);
            int i2 = viewPager.mCurItem;
            if (z) {
                i = this.mAdapter.getCount() - i2;
                ArrayList arrayList = new ArrayList(list);
                Collections.reverse(arrayList);
                list = arrayList;
            } else {
                i = i2;
            }
            CardPagerAdapter cardPagerAdapter = this.mAdapter;
            Objects.requireNonNull(cardPagerAdapter);
            CardPagerAdapter.ViewHolder viewHolder = cardPagerAdapter.mViewHolders.get(i2);
            if (viewHolder == null) {
                baseTemplateCard = null;
            } else {
                baseTemplateCard = viewHolder.mCard;
            }
            CardPagerAdapter cardPagerAdapter2 = this.mAdapter;
            Objects.requireNonNull(cardPagerAdapter2);
            CardPagerAdapter.ViewHolder viewHolder2 = cardPagerAdapter2.mViewHolders.get(i2);
            if (viewHolder2 == null) {
                bcSmartspaceCard = null;
            } else {
                bcSmartspaceCard = viewHolder2.mLegacyCard;
            }
            CardPagerAdapter cardPagerAdapter3 = this.mAdapter;
            Objects.requireNonNull(cardPagerAdapter3);
            cardPagerAdapter3.mTargetsExcludingMediaAndHolidayAlarms.clear();
            cardPagerAdapter3.mHolidayAlarmsTarget = null;
            list.forEach(new WMShell$$ExternalSyntheticLambda7(cardPagerAdapter3, 4));
            if (cardPagerAdapter3.mTargetsExcludingMediaAndHolidayAlarms.isEmpty()) {
                cardPagerAdapter3.mTargetsExcludingMediaAndHolidayAlarms.add(new SmartspaceTarget.Builder("date_card_794317_92634", new ComponentName(cardPagerAdapter3.mRoot.getContext(), CardPagerAdapter.class), cardPagerAdapter3.mRoot.getContext().getUser()).setFeatureType(1).build());
            }
            if (!(cardPagerAdapter3.mTargetsExcludingMediaAndHolidayAlarms.size() == 1 && ((SmartspaceTarget) cardPagerAdapter3.mTargetsExcludingMediaAndHolidayAlarms.get(0)).getFeatureType() == 1)) {
                z2 = false;
            }
            cardPagerAdapter3.mHasOnlyDefaultDateCard = z2;
            cardPagerAdapter3.updateTargetVisibility();
            cardPagerAdapter3.notifyDataSetChanged();
            int count = this.mAdapter.getCount();
            if (z) {
                this.mViewPager.setCurrentItem(Math.max(0, Math.min(count - 1, count - i)), false);
            }
            PageIndicator pageIndicator = this.mPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(count);
            }
            if (this.mAnimateSmartspaceUpdate) {
                if (baseTemplateCard != null) {
                    animateSmartspaceUpdate(baseTemplateCard);
                } else if (bcSmartspaceCard != null) {
                    animateSmartspaceUpdate(bcSmartspaceCard);
                }
            }
            for (int i3 = 0; i3 < count; i3++) {
                SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(i3);
                if (!mLastReceivedTargets.contains(targetAtPosition.getSmartspaceTargetId())) {
                    logSmartspaceEvent(targetAtPosition, i3, BcSmartspaceEvent.SMARTSPACE_CARD_RECEIVED);
                    SmartspaceTargetEvent.Builder builder = new SmartspaceTargetEvent.Builder(8);
                    builder.setSmartspaceTarget(targetAtPosition);
                    SmartspaceAction baseAction = targetAtPosition.getBaseAction();
                    if (baseAction != null) {
                        builder.setSmartspaceActionId(baseAction.getId());
                    }
                    this.mDataProvider.notifySmartspaceEvent(builder.build());
                }
            }
            mLastReceivedTargets.clear();
            ArraySet<String> arraySet = mLastReceivedTargets;
            CardPagerAdapter cardPagerAdapter4 = this.mAdapter;
            Objects.requireNonNull(cardPagerAdapter4);
            arraySet.addAll((Collection) cardPagerAdapter4.mSmartspaceTargets.stream().map(NotificationMediaManager$$ExternalSyntheticLambda0.INSTANCE$1).collect(Collectors.toList()));
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mPendingTargets = list;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void registerDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin) {
        this.mDataProvider = bcSmartspaceDataPlugin;
        bcSmartspaceDataPlugin.registerListener(this);
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.mDataProvider;
        Objects.requireNonNull(cardPagerAdapter);
        cardPagerAdapter.mDataProvider = bcSmartspaceDataPlugin2;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setDnd(Drawable drawable, String str) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        cardPagerAdapter.mDndImage = drawable;
        cardPagerAdapter.mDndDescription = str;
        for (int i = 0; i < cardPagerAdapter.mViewHolders.size(); i++) {
            cardPagerAdapter.onBindViewHolder(cardPagerAdapter.mViewHolders.get(i));
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setDozeAmount(float f) {
        SmartspaceAction headerAction;
        this.mPageIndicator.setAlpha(1.0f - f);
        this.mAdapter.setDozeAmount(f);
        String packageName = getContext().getPackageName();
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        int loggingDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(packageName, cardPagerAdapter.mDozeAmount);
        if (loggingDisplaySurface != -1 && loggingDisplaySurface != sLastSurface) {
            sLastSurface = loggingDisplaySurface;
            if (loggingDisplaySurface != 3 || this.mIsAodEnabled) {
                BcSmartspaceEvent bcSmartspaceEvent = BcSmartspaceEvent.SMARTSPACE_CARD_SEEN;
                SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(this.mCardPosition);
                if (targetAtPosition == null) {
                    Log.w("BcSmartspaceView", "Current card is not present in the Adapter; cannot log.");
                } else {
                    logSmartspaceEvent(targetAtPosition, this.mCardPosition, bcSmartspaceEvent);
                }
                CardPagerAdapter cardPagerAdapter2 = this.mAdapter;
                Objects.requireNonNull(cardPagerAdapter2);
                if (cardPagerAdapter2.mNextAlarmImage != null) {
                    logSmartspaceEvent(new SmartspaceTarget.Builder("upcoming_alarm_card_94510_12684", new ComponentName(getContext(), getClass()), getContext().getUser()).setFeatureType(23).build(), 0, bcSmartspaceEvent);
                    CardPagerAdapter cardPagerAdapter3 = this.mAdapter;
                    Objects.requireNonNull(cardPagerAdapter3);
                    SmartspaceTarget smartspaceTarget = cardPagerAdapter3.mHolidayAlarmsTarget;
                    if (smartspaceTarget != null) {
                        CardPagerAdapter cardPagerAdapter4 = this.mAdapter;
                        Objects.requireNonNull(cardPagerAdapter4);
                        SmartspaceTarget smartspaceTarget2 = cardPagerAdapter4.mHolidayAlarmsTarget;
                        CharSequence charSequence = null;
                        if (!(smartspaceTarget2 == null || (headerAction = smartspaceTarget2.getHeaderAction()) == null)) {
                            charSequence = headerAction.getTitle();
                        }
                        if (!TextUtils.isEmpty(charSequence)) {
                            logSmartspaceEvent(smartspaceTarget, 0, bcSmartspaceEvent);
                        }
                    }
                }
            }
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setMediaTarget(SmartspaceTarget smartspaceTarget) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        cardPagerAdapter.mMediaTargets.clear();
        if (smartspaceTarget != null) {
            cardPagerAdapter.mMediaTargets.add(smartspaceTarget);
        }
        cardPagerAdapter.updateTargetVisibility();
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setNextAlarm(Drawable drawable, String str) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        cardPagerAdapter.mNextAlarmImage = drawable;
        cardPagerAdapter.mNextAlarmDescription = str;
        for (int i = 0; i < cardPagerAdapter.mViewHolders.size(); i++) {
            cardPagerAdapter.onBindViewHolder(cardPagerAdapter.mViewHolders.get(i));
        }
    }

    @Override // android.view.View
    public final void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mViewPager.setOnLongClickListener(onLongClickListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setPrimaryTextColor(int i) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        Objects.requireNonNull(cardPagerAdapter);
        cardPagerAdapter.mPrimaryTextColor = i;
        cardPagerAdapter.setDozeAmount(cardPagerAdapter.mDozeAmount);
        PageIndicator pageIndicator = this.mPageIndicator;
        Objects.requireNonNull(pageIndicator);
        pageIndicator.mPrimaryColor = i;
        for (int i2 = 0; i2 < pageIndicator.getChildCount(); i2++) {
            ((ImageView) pageIndicator.getChildAt(i2)).getDrawable().setTint(pageIndicator.mPrimaryColor);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.systemui.smartspace.BcSmartspaceView$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.systemui.smartspace.BcSmartspaceView$2] */
    public BcSmartspaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().getContentResolver().unregisterContentObserver(this.mAodObserver);
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.unregisterListener(this);
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mViewPager = (ViewPager) findViewById(2131428879);
        this.mPageIndicator = (PageIndicator) findViewById(2131428881);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i2);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(2131165709);
        if (size <= 0 || size >= dimensionPixelSize) {
            super.onMeasure(i, i2);
            setScaleX(1.0f);
            setScaleY(1.0f);
            resetPivot();
            return;
        }
        float f = size;
        float f2 = dimensionPixelSize;
        float f3 = f / f2;
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(Math.round(View.MeasureSpec.getSize(i) / f3), 1073741824), View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824));
        setScaleX(f3);
        setScaleY(f3);
        setPivotX(0.0f);
        setPivotY(f2 / 2.0f);
    }

    @Override // android.view.View
    public final void onVisibilityAggregated(boolean z) {
        int i;
        super.onVisibilityAggregated(z);
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            if (z) {
                i = 6;
            } else {
                i = 7;
            }
            bcSmartspaceDataPlugin.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(i).build());
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setFalsingManager(FalsingManager falsingManager) {
        BcSmartSpaceUtil.sFalsingManager = falsingManager;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setIntentStarter(BcSmartspaceDataPlugin.IntentStarter intentStarter) {
        BcSmartSpaceUtil.sIntentStarter = intentStarter;
    }
}
