package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.launcher3.icons.GraphicsUtils;
/* loaded from: classes.dex */
public class BcSmartspaceCard extends LinearLayout {
    public DoubleShadowTextView mBaseActionIconSubtitleView;
    public IcuDateTextView mDateView;
    public ImageView mDndImageView;
    public float mDozeAmount;
    public ViewGroup mExtrasGroup;
    public DoubleShadowIconDrawable mIconDrawable;
    public int mIconTintColor;
    public ImageView mNextAlarmImageView;
    public TextView mNextAlarmTextView;
    public BcSmartspaceCardSecondary mSecondaryCard;
    public TextView mSubtitleTextView;
    public SmartspaceTarget mTarget;
    public TextView mTitleTextView;
    public int mTopPadding;
    public boolean mUsePageIndicatorUi;

    public BcSmartspaceCard(Context context) {
        this(context, null);
    }

    public BcSmartspaceCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSecondaryCard = null;
        this.mIconTintColor = GraphicsUtils.getAttrColor(getContext(), 16842806);
        this.mDateView = null;
        this.mTitleTextView = null;
        this.mSubtitleTextView = null;
        this.mBaseActionIconSubtitleView = null;
        this.mExtrasGroup = null;
        this.mDndImageView = null;
        this.mNextAlarmImageView = null;
        this.mNextAlarmTextView = null;
    }

    public final void setPrimaryTextColor(int i) {
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
        IcuDateTextView icuDateTextView = this.mDateView;
        if (icuDateTextView != null) {
            icuDateTextView.setTextColor(i);
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null) {
            textView2.setTextColor(i);
        }
        DoubleShadowTextView doubleShadowTextView = this.mBaseActionIconSubtitleView;
        if (doubleShadowTextView != null) {
            doubleShadowTextView.setTextColor(i);
        }
        this.mIconTintColor = i;
        TextView textView3 = this.mNextAlarmTextView;
        if (textView3 != null) {
            textView3.setTextColor(i);
        }
        ImageView imageView = this.mNextAlarmImageView;
        if (!(imageView == null || imageView.getDrawable() == null)) {
            imageView.getDrawable().setTint(this.mIconTintColor);
        }
        ImageView imageView2 = this.mDndImageView;
        if (!(imageView2 == null || imageView2.getDrawable() == null)) {
            imageView2.getDrawable().setTint(this.mIconTintColor);
        }
        updateIconTint();
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setSmartspaceTarget(android.app.smartspace.SmartspaceTarget r17, com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0 r18, com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 548
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.BcSmartspaceCard.setSmartspaceTarget(android.app.smartspace.SmartspaceTarget, com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0, com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo, boolean):void");
    }

    public final void updateIconTint() {
        SmartspaceTarget smartspaceTarget = this.mTarget;
        if (smartspaceTarget != null && this.mIconDrawable != null) {
            boolean z = true;
            if (smartspaceTarget.getFeatureType() == 1) {
                z = false;
            }
            if (z) {
                this.mIconDrawable.setTint(this.mIconTintColor);
            } else {
                this.mIconDrawable.setTintList(null);
            }
        }
    }

    public final void updateZenVisibility() {
        boolean z;
        boolean z2;
        if (this.mExtrasGroup != null) {
            ImageView imageView = this.mDndImageView;
            boolean z3 = true;
            int i = 0;
            if (imageView == null || imageView.getVisibility() != 0) {
                z = false;
            } else {
                z = true;
            }
            ImageView imageView2 = this.mNextAlarmImageView;
            if (imageView2 == null || imageView2.getVisibility() != 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            if ((!z && !z2) || (this.mUsePageIndicatorUi && this.mTarget.getFeatureType() != 1)) {
                z3 = false;
            }
            int i2 = this.mTopPadding;
            if (!z3) {
                this.mExtrasGroup.setVisibility(4);
                i = i2;
            } else {
                this.mExtrasGroup.setVisibility(0);
                TextView textView = this.mNextAlarmTextView;
                if (textView != null) {
                    textView.setTextColor(this.mIconTintColor);
                }
                ImageView imageView3 = this.mNextAlarmImageView;
                if (!(imageView3 == null || imageView3.getDrawable() == null)) {
                    imageView3.getDrawable().setTint(this.mIconTintColor);
                }
                ImageView imageView4 = this.mDndImageView;
                if (!(imageView4 == null || imageView4.getDrawable() == null)) {
                    imageView4.getDrawable().setTint(this.mIconTintColor);
                }
            }
            setPadding(getPaddingLeft(), i, getPaddingRight(), getPaddingBottom());
        }
    }

    @Override // android.view.View
    public final AccessibilityNodeInfo createAccessibilityNodeInfo() {
        AccessibilityNodeInfo createAccessibilityNodeInfo = super.createAccessibilityNodeInfo();
        createAccessibilityNodeInfo.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", " ");
        return createAccessibilityNodeInfo;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mDateView = (IcuDateTextView) findViewById(2131427797);
        this.mTitleTextView = (TextView) findViewById(2131429064);
        this.mSubtitleTextView = (TextView) findViewById(2131428948);
        this.mBaseActionIconSubtitleView = (DoubleShadowTextView) findViewById(2131427569);
        this.mExtrasGroup = (ViewGroup) findViewById(2131428880);
        this.mTopPadding = getPaddingTop();
        ViewGroup viewGroup = this.mExtrasGroup;
        if (viewGroup != null) {
            this.mDndImageView = (ImageView) viewGroup.findViewById(2131427860);
            this.mNextAlarmImageView = (ImageView) this.mExtrasGroup.findViewById(2131427466);
            this.mNextAlarmTextView = (TextView) this.mExtrasGroup.findViewById(2131427467);
        }
    }

    public final void setFormattedContentDescription(TextView textView, CharSequence charSequence, CharSequence charSequence2) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = charSequence2;
        } else if (!TextUtils.isEmpty(charSequence2)) {
            charSequence = ((LinearLayout) this).mContext.getString(2131952404, charSequence2, charSequence);
        }
        textView.setContentDescription(charSequence);
    }
}
