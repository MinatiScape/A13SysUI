package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
/* loaded from: classes.dex */
public class BcSmartspaceCardLoyalty extends BcSmartspaceCardGenericImage {
    public TextView mCardPromptView;
    public ImageView mLoyaltyProgramLogoView;
    public TextView mLoyaltyProgramNameView;

    public BcSmartspaceCardLoyalty(Context context) {
        super(context);
    }

    public BcSmartspaceCardLoyalty(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mLoyaltyProgramLogoView = (ImageView) findViewById(2131428290);
        this.mLoyaltyProgramNameView = (TextView) findViewById(2131428291);
        this.mCardPromptView = (TextView) findViewById(2131427667);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage
    public final void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.mLoyaltyProgramLogoView.setImageBitmap(bitmap);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle bundle;
        super.setSmartspaceActions(smartspaceTarget, cardPagerAdapter$$ExternalSyntheticLambda0, bcSmartspaceCardLoggingInfo);
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            bundle = null;
        } else {
            bundle = baseAction.getExtras();
        }
        this.mImageView.setVisibility(8);
        this.mLoyaltyProgramLogoView.setVisibility(8);
        this.mLoyaltyProgramNameView.setVisibility(8);
        this.mCardPromptView.setVisibility(8);
        if (bundle == null) {
            return false;
        }
        boolean containsKey = bundle.containsKey("imageBitmap");
        if (bundle.containsKey("cardPrompt")) {
            String string = bundle.getString("cardPrompt");
            TextView textView = this.mCardPromptView;
            if (textView == null) {
                Log.w("BcSmartspaceCardLoyalty", "No card prompt view to update");
            } else {
                textView.setText(string);
            }
            this.mCardPromptView.setVisibility(0);
            if (containsKey) {
                this.mImageView.setVisibility(0);
            }
            return true;
        } else if (bundle.containsKey("loyaltyProgramName")) {
            String string2 = bundle.getString("loyaltyProgramName");
            TextView textView2 = this.mLoyaltyProgramNameView;
            if (textView2 == null) {
                Log.w("BcSmartspaceCardLoyalty", "No loyalty program name view to update");
            } else {
                textView2.setText(string2);
            }
            this.mLoyaltyProgramNameView.setVisibility(0);
            if (containsKey) {
                this.mLoyaltyProgramLogoView.setVisibility(0);
            }
            return true;
        } else {
            if (containsKey) {
                this.mLoyaltyProgramLogoView.setVisibility(0);
            }
            return containsKey;
        }
    }
}
