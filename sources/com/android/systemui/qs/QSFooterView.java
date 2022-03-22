package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.systemui.qs.TouchAnimator;
/* loaded from: classes.dex */
public class QSFooterView extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public TextView mBuildText;
    public final AnonymousClass1 mDeveloperSettingsObserver = new ContentObserver(new Handler(((FrameLayout) this).mContext.getMainLooper())) { // from class: com.android.systemui.qs.QSFooterView.1
        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            QSFooterView qSFooterView = QSFooterView.this;
            int i = QSFooterView.$r8$clinit;
            qSFooterView.setBuildText();
        }
    };
    public View mEditButton;
    public View.OnClickListener mExpandClickListener;
    public boolean mExpanded;
    public float mExpansionAmount;
    public TouchAnimator mFooterAnimator;
    public PageIndicator mPageIndicator;
    public boolean mQsDisabled;
    public boolean mShouldShowBuildText;

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        ((FrameLayout) this).mContext.getContentResolver().unregisterContentObserver(this.mDeveloperSettingsObserver);
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public final boolean performAccessibilityAction(int i, Bundle bundle) {
        View.OnClickListener onClickListener;
        if (i != 262144 || (onClickListener = this.mExpandClickListener) == null) {
            return super.performAccessibilityAction(i, bundle);
        }
        onClickListener.onClick(null);
        return true;
    }

    public final void setBuildText() {
        Object[] objArr;
        Object[] objArr2;
        if (this.mBuildText != null) {
            Context context = ((FrameLayout) this).mContext;
            UserManager userManager = (UserManager) context.getSystemService("user");
            if (Settings.Global.getInt(context.getContentResolver(), "development_settings_enabled", Build.TYPE.equals("eng") ? 1 : 0) != 0) {
                objArr = 1;
            } else {
                objArr = null;
            }
            boolean hasUserRestriction = userManager.hasUserRestriction("no_debugging_features");
            if (!userManager.isAdminUser() || hasUserRestriction || objArr == null) {
                objArr2 = null;
            } else {
                objArr2 = 1;
            }
            if (objArr2 != null) {
                this.mBuildText.setText(((FrameLayout) this).mContext.getString(17039810, Build.VERSION.RELEASE_OR_CODENAME, Build.ID));
                this.mBuildText.setSelected(true);
                this.mShouldShowBuildText = true;
                return;
            }
            this.mBuildText.setText((CharSequence) null);
            this.mShouldShowBuildText = false;
            this.mBuildText.setSelected(false);
        }
    }

    public final void updateResources() {
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        builder.addFloat(this.mPageIndicator, "alpha", 0.0f, 1.0f);
        builder.addFloat(this.mBuildText, "alpha", 0.0f, 1.0f);
        builder.addFloat(this.mEditButton, "alpha", 0.0f, 1.0f);
        builder.mStartDelay = 0.9f;
        this.mFooterAnimator = builder.build();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(2131166867);
        setLayoutParams(marginLayoutParams);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.qs.QSFooterView$1] */
    public QSFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((FrameLayout) this).mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("development_settings_enabled"), false, this.mDeveloperSettingsObserver, -1);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mPageIndicator = (PageIndicator) findViewById(2131427984);
        this.mBuildText = (TextView) findViewById(2131427636);
        this.mEditButton = findViewById(16908291);
        updateResources();
        setImportantForAccessibility(1);
        setBuildText();
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
    }
}
