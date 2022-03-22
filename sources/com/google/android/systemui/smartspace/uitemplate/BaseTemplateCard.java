package com.google.android.systemui.smartspace.uitemplate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.launcher3.icons.GraphicsUtils;
import com.google.android.systemui.smartspace.DoubleShadowTextView;
import com.google.android.systemui.smartspace.IcuDateTextView;
/* loaded from: classes.dex */
public class BaseTemplateCard extends LinearLayout {
    public IcuDateTextView mDateView;
    public ImageView mDndImageView;
    public float mDozeAmount;
    public ViewGroup mExtrasGroup;
    public int mIconTintColor;
    public ImageView mNextAlarmImageView;
    public DoubleShadowTextView mNextAlarmTextView;
    public DoubleShadowTextView mSubtitleSupplementalView;
    public DoubleShadowTextView mSubtitleTextView;
    public DoubleShadowTextView mTitleTextView;
    public int mTopPadding;

    public BaseTemplateCard(Context context) {
        this(context, null);
    }

    public BaseTemplateCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIconTintColor = GraphicsUtils.getAttrColor(getContext(), 16842806);
        this.mDateView = null;
        this.mTitleTextView = null;
        this.mSubtitleTextView = null;
        this.mSubtitleSupplementalView = null;
        this.mExtrasGroup = null;
        this.mDndImageView = null;
        this.mNextAlarmImageView = null;
        this.mNextAlarmTextView = null;
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
        this.mTitleTextView = (DoubleShadowTextView) findViewById(2131429064);
        this.mSubtitleTextView = (DoubleShadowTextView) findViewById(2131428948);
        this.mSubtitleSupplementalView = (DoubleShadowTextView) findViewById(2131427569);
        this.mExtrasGroup = (ViewGroup) findViewById(2131428880);
        this.mTopPadding = getPaddingTop();
        ViewGroup viewGroup = this.mExtrasGroup;
        if (viewGroup != null) {
            this.mDndImageView = (ImageView) viewGroup.findViewById(2131427860);
            this.mNextAlarmImageView = (ImageView) this.mExtrasGroup.findViewById(2131427466);
            this.mNextAlarmTextView = (DoubleShadowTextView) this.mExtrasGroup.findViewById(2131427467);
        }
    }
}
