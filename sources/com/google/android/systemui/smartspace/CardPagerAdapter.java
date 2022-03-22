package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.savedstate.R$id;
import androidx.viewpager.widget.PagerAdapter;
import com.android.internal.graphics.ColorUtils;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.uitemplate.BaseTemplateCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CardPagerAdapter extends PagerAdapter {
    public int mCurrentTextColor;
    public BcSmartspaceDataPlugin mDataProvider;
    public int mPrimaryTextColor;
    public final View mRoot;
    public ArrayList mSmartspaceTargets = new ArrayList();
    public final ArrayList mTargetsExcludingMediaAndHolidayAlarms = new ArrayList();
    public final ArrayList mMediaTargets = new ArrayList();
    public boolean mHasOnlyDefaultDateCard = false;
    public final SparseArray<ViewHolder> mViewHolders = new SparseArray<>();
    public float mDozeAmount = 0.0f;
    public int mDozeColor = -1;
    public String mDndDescription = null;
    public Drawable mDndImage = null;
    public String mNextAlarmDescription = null;
    public Drawable mNextAlarmImage = null;
    public SmartspaceTarget mHolidayAlarmsTarget = null;

    /* loaded from: classes.dex */
    public static class ViewHolder {
        public final BaseTemplateCard mCard = null;
        public final BcSmartspaceCard mLegacyCard;
        public final int mPosition;
        public SmartspaceTarget mTarget;

        public ViewHolder(int i, BcSmartspaceCard bcSmartspaceCard, SmartspaceTarget smartspaceTarget) {
            this.mPosition = i;
            this.mLegacyCard = bcSmartspaceCard;
            this.mTarget = smartspaceTarget;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        if (viewHolder != null) {
            BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
            if (bcSmartspaceCard != null) {
                viewGroup.removeView(bcSmartspaceCard);
            }
            BaseTemplateCard baseTemplateCard = viewHolder.mCard;
            if (baseTemplateCard != null) {
                viewGroup.removeView(baseTemplateCard);
            }
            if (this.mViewHolders.get(i) == viewHolder) {
                this.mViewHolders.remove(i);
            }
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final int getCount() {
        return this.mSmartspaceTargets.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final int getItemPosition(Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        Objects.requireNonNull(viewHolder);
        SmartspaceTarget targetAtPosition = getTargetAtPosition(viewHolder.mPosition);
        if (viewHolder.mTarget == targetAtPosition) {
            return -1;
        }
        if (targetAtPosition == null || getFeatureType(targetAtPosition) != getFeatureType(viewHolder.mTarget) || !Objects.equals(targetAtPosition.getSmartspaceTargetId(), viewHolder.mTarget.getSmartspaceTargetId())) {
            return -2;
        }
        viewHolder.mTarget = targetAtPosition;
        onBindViewHolder(viewHolder);
        return -1;
    }

    public final SmartspaceTarget getTargetAtPosition(int i) {
        if (this.mSmartspaceTargets.isEmpty() || i < 0 || i >= this.mSmartspaceTargets.size()) {
            return null;
        }
        return (SmartspaceTarget) this.mSmartspaceTargets.get(i);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final Object instantiateItem(ViewGroup viewGroup, int i) {
        int i2;
        int i3;
        SmartspaceTarget smartspaceTarget = (SmartspaceTarget) this.mSmartspaceTargets.get(i);
        smartspaceTarget.getFeatureType();
        int i4 = BcSmartspaceTemplateDataUtils.$r8$clinit;
        int featureType = getFeatureType(smartspaceTarget);
        if (featureType == -2) {
            i2 = 2131624495;
        } else if (featureType != 1) {
            i2 = 2131624494;
        } else {
            i2 = 2131624498;
        }
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        BcSmartspaceCard bcSmartspaceCard = (BcSmartspaceCard) from.inflate(i2, viewGroup, false);
        if (featureType == -2) {
            i3 = 2131624497;
        } else if (featureType == -1) {
            i3 = 2131624496;
        } else if (featureType == 3) {
            i3 = 2131624502;
        } else if (featureType == 4) {
            i3 = 2131624500;
        } else if (featureType == 9) {
            i3 = 2131624506;
        } else if (featureType == 10) {
            i3 = 2131624507;
        } else if (featureType == 13) {
            i3 = 2131624505;
        } else if (featureType == 14) {
            i3 = 2131624504;
        } else if (featureType != 30) {
            i3 = 0;
        } else {
            i3 = 2131624499;
        }
        if (i3 != 0) {
            BcSmartspaceCardSecondary bcSmartspaceCardSecondary = (BcSmartspaceCardSecondary) from.inflate(i3, (ViewGroup) bcSmartspaceCard, false);
            Objects.requireNonNull(bcSmartspaceCard);
            bcSmartspaceCard.mSecondaryCard = bcSmartspaceCardSecondary;
            if (bcSmartspaceCard.getChildAt(1) != null) {
                bcSmartspaceCard.removeViewAt(1);
            }
            if (bcSmartspaceCardSecondary != null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, bcSmartspaceCard.getResources().getDimensionPixelSize(2131165709));
                layoutParams.weight = 3.0f;
                layoutParams.setMarginStart(bcSmartspaceCard.getResources().getDimensionPixelSize(2131165716));
                layoutParams.setMarginEnd(bcSmartspaceCard.getResources().getDimensionPixelSize(2131165715));
                bcSmartspaceCard.addView(bcSmartspaceCardSecondary, 1, layoutParams);
            }
        }
        ViewHolder viewHolder = new ViewHolder(i, bcSmartspaceCard, smartspaceTarget);
        viewGroup.addView(bcSmartspaceCard);
        onBindViewHolder(viewHolder);
        this.mViewHolders.put(i, viewHolder);
        return viewHolder;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final boolean isViewFromObject(View view, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        Objects.requireNonNull(viewHolder);
        if (view == viewHolder.mLegacyCard || view == viewHolder.mCard) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0] */
    public final void onBindViewHolder(ViewHolder viewHolder) {
        CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0;
        boolean z;
        String str;
        SmartspaceAction headerAction;
        ArrayList arrayList = this.mSmartspaceTargets;
        Objects.requireNonNull(viewHolder);
        SmartspaceTarget smartspaceTarget = (SmartspaceTarget) arrayList.get(viewHolder.mPosition);
        BcSmartspaceCardLoggingInfo.Builder builder = new BcSmartspaceCardLoggingInfo.Builder();
        builder.mInstanceId = R$id.create(smartspaceTarget).intValue();
        builder.mFeatureType = smartspaceTarget.getFeatureType();
        builder.mDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(this.mRoot.getContext().getPackageName(), this.mDozeAmount);
        builder.mRank = viewHolder.mPosition;
        builder.mCardinality = this.mSmartspaceTargets.size();
        BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo = new BcSmartspaceCardLoggingInfo(builder);
        smartspaceTarget.getFeatureType();
        int i = BcSmartspaceTemplateDataUtils.$r8$clinit;
        BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
        if (bcSmartspaceCard == null) {
            Log.w("SsCardPagerAdapter", "No legacy card view can be binded");
            return;
        }
        final BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        CharSequence charSequence = null;
        if (bcSmartspaceDataPlugin == null) {
            cardPagerAdapter$$ExternalSyntheticLambda0 = null;
        } else {
            cardPagerAdapter$$ExternalSyntheticLambda0 = new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                    BcSmartspaceDataPlugin.this.notifySmartspaceEvent(smartspaceTargetEvent);
                }
            };
        }
        if (this.mSmartspaceTargets.size() > 1) {
            z = true;
        } else {
            z = false;
        }
        bcSmartspaceCard.setSmartspaceTarget(smartspaceTarget, cardPagerAdapter$$ExternalSyntheticLambda0, bcSmartspaceCardLoggingInfo, z);
        bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
        float f = this.mDozeAmount;
        bcSmartspaceCard.mDozeAmount = f;
        BcSmartspaceCardSecondary bcSmartspaceCardSecondary = bcSmartspaceCard.mSecondaryCard;
        if (bcSmartspaceCardSecondary != null) {
            bcSmartspaceCardSecondary.setAlpha(1.0f - f);
        }
        ImageView imageView = bcSmartspaceCard.mDndImageView;
        if (imageView != null) {
            imageView.setAlpha(bcSmartspaceCard.mDozeAmount);
        }
        Drawable drawable = this.mDndImage;
        String str2 = this.mDndDescription;
        ImageView imageView2 = bcSmartspaceCard.mDndImageView;
        if (imageView2 != null) {
            if (drawable == null) {
                imageView2.setVisibility(8);
            } else {
                imageView2.setImageDrawable(new DoubleShadowIconDrawable(drawable.mutate(), bcSmartspaceCard.getContext()));
                bcSmartspaceCard.mDndImageView.setContentDescription(str2);
                bcSmartspaceCard.mDndImageView.setVisibility(0);
            }
            bcSmartspaceCard.updateZenVisibility();
        }
        Drawable drawable2 = this.mNextAlarmImage;
        SmartspaceTarget smartspaceTarget2 = this.mHolidayAlarmsTarget;
        if (!(smartspaceTarget2 == null || (headerAction = smartspaceTarget2.getHeaderAction()) == null)) {
            charSequence = headerAction.getTitle();
        }
        if (!TextUtils.isEmpty(charSequence)) {
            str = this.mNextAlarmDescription + " · " + ((Object) charSequence);
        } else {
            str = this.mNextAlarmDescription;
        }
        ImageView imageView3 = bcSmartspaceCard.mNextAlarmImageView;
        if (imageView3 != null && bcSmartspaceCard.mNextAlarmTextView != null) {
            if (drawable2 == null) {
                imageView3.setVisibility(8);
                bcSmartspaceCard.mNextAlarmTextView.setVisibility(8);
            } else {
                imageView3.setImageDrawable(new DoubleShadowIconDrawable(drawable2.mutate(), bcSmartspaceCard.getContext()));
                bcSmartspaceCard.mNextAlarmImageView.setVisibility(0);
                bcSmartspaceCard.mNextAlarmTextView.setContentDescription(bcSmartspaceCard.getContext().getString(2131951760, str));
                bcSmartspaceCard.mNextAlarmTextView.setText(str);
                bcSmartspaceCard.mNextAlarmTextView.setVisibility(0);
            }
            bcSmartspaceCard.updateZenVisibility();
        }
    }

    public final void setDozeAmount(float f) {
        this.mCurrentTextColor = ColorUtils.blendARGB(this.mPrimaryTextColor, this.mDozeColor, f);
        this.mDozeAmount = f;
        updateTargetVisibility();
        for (int i = 0; i < this.mViewHolders.size(); i++) {
            ViewHolder viewHolder = this.mViewHolders.get(i);
            Objects.requireNonNull(viewHolder);
            BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
            if (bcSmartspaceCard != null) {
                bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
                float f2 = this.mDozeAmount;
                bcSmartspaceCard.mDozeAmount = f2;
                BcSmartspaceCardSecondary bcSmartspaceCardSecondary = bcSmartspaceCard.mSecondaryCard;
                if (bcSmartspaceCardSecondary != null) {
                    bcSmartspaceCardSecondary.setAlpha(1.0f - f2);
                }
                ImageView imageView = bcSmartspaceCard.mDndImageView;
                if (imageView != null) {
                    imageView.setAlpha(bcSmartspaceCard.mDozeAmount);
                }
            }
            BaseTemplateCard baseTemplateCard = this.mViewHolders.get(i).mCard;
            if (baseTemplateCard != null) {
                int i2 = this.mCurrentTextColor;
                baseTemplateCard.mIconTintColor = i2;
                DoubleShadowTextView doubleShadowTextView = baseTemplateCard.mTitleTextView;
                if (doubleShadowTextView != null) {
                    doubleShadowTextView.setTextColor(i2);
                }
                IcuDateTextView icuDateTextView = baseTemplateCard.mDateView;
                if (icuDateTextView != null) {
                    icuDateTextView.setTextColor(i2);
                }
                DoubleShadowTextView doubleShadowTextView2 = baseTemplateCard.mSubtitleTextView;
                if (doubleShadowTextView2 != null) {
                    doubleShadowTextView2.setTextColor(i2);
                }
                DoubleShadowTextView doubleShadowTextView3 = baseTemplateCard.mSubtitleSupplementalView;
                if (doubleShadowTextView3 != null) {
                    doubleShadowTextView3.setTextColor(i2);
                }
                DoubleShadowTextView doubleShadowTextView4 = baseTemplateCard.mNextAlarmTextView;
                if (doubleShadowTextView4 != null) {
                    doubleShadowTextView4.setTextColor(baseTemplateCard.mIconTintColor);
                }
                ImageView imageView2 = baseTemplateCard.mNextAlarmImageView;
                if (!(imageView2 == null || imageView2.getDrawable() == null)) {
                    imageView2.getDrawable().setTint(baseTemplateCard.mIconTintColor);
                }
                ImageView imageView3 = baseTemplateCard.mDndImageView;
                if (!(imageView3 == null || imageView3.getDrawable() == null)) {
                    imageView3.getDrawable().setTint(baseTemplateCard.mIconTintColor);
                }
                float f3 = this.mDozeAmount;
                baseTemplateCard.mDozeAmount = f3;
                ImageView imageView4 = baseTemplateCard.mDndImageView;
                if (imageView4 != null) {
                    imageView4.setAlpha(f3);
                }
                ImageView imageView5 = baseTemplateCard.mDndImageView;
                if (imageView5 != null) {
                    imageView5.setAlpha(baseTemplateCard.mDozeAmount);
                }
            }
        }
    }

    public final void updateTargetVisibility() {
        boolean z;
        if (this.mMediaTargets.isEmpty()) {
            this.mSmartspaceTargets = this.mTargetsExcludingMediaAndHolidayAlarms;
            notifyDataSetChanged();
            return;
        }
        float f = this.mDozeAmount;
        if (f == 0.0f || !(z = this.mHasOnlyDefaultDateCard)) {
            this.mSmartspaceTargets = this.mTargetsExcludingMediaAndHolidayAlarms;
            notifyDataSetChanged();
        } else if (f == 1.0f && z) {
            this.mSmartspaceTargets = this.mMediaTargets;
            notifyDataSetChanged();
        }
    }

    public CardPagerAdapter(View view) {
        this.mRoot = view;
        int attrColor = GraphicsUtils.getAttrColor(view.getContext(), 16842806);
        this.mPrimaryTextColor = attrColor;
        this.mCurrentTextColor = attrColor;
    }

    public static int getFeatureType(SmartspaceTarget smartspaceTarget) {
        List actionChips = smartspaceTarget.getActionChips();
        int featureType = smartspaceTarget.getFeatureType();
        if (actionChips == null || actionChips.isEmpty()) {
            return featureType;
        }
        if (featureType == 13 && actionChips.size() == 1) {
            return -2;
        }
        return -1;
    }
}
