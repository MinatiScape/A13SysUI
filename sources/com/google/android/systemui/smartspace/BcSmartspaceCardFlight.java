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
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: classes.dex */
public class BcSmartspaceCardFlight extends BcSmartspaceCardSecondary {
    public ConstraintLayout mBoardingPassUI;
    public ImageView mCardPromptLogoView;
    public TextView mCardPromptView;
    public TextView mGateValueView;
    public ImageView mQrCodeView;
    public TextView mSeatValueView;

    public BcSmartspaceCardFlight(Context context) {
        super(context);
    }

    public BcSmartspaceCardFlight(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mCardPromptView = (TextView) findViewById(2131427667);
        this.mCardPromptLogoView = (ImageView) findViewById(2131427669);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(2131427584);
        this.mBoardingPassUI = constraintLayout;
        if (constraintLayout != null) {
            this.mGateValueView = (TextView) constraintLayout.findViewById(2131428019);
            this.mSeatValueView = (TextView) this.mBoardingPassUI.findViewById(2131428811);
            this.mQrCodeView = (ImageView) this.mBoardingPassUI.findViewById(2131427978);
        }
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle bundle;
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            bundle = null;
        } else {
            bundle = baseAction.getExtras();
        }
        boolean z = true;
        if (bundle == null) {
            return false;
        }
        this.mBoardingPassUI.setVisibility(8);
        this.mCardPromptView.setVisibility(8);
        this.mCardPromptLogoView.setVisibility(8);
        if (bundle.containsKey("cardPrompt") || bundle.containsKey("cardPromptBitmap")) {
            if (bundle.containsKey("cardPrompt")) {
                String string = bundle.getString("cardPrompt");
                TextView textView = this.mCardPromptView;
                if (textView == null) {
                    Log.w("BcSmartspaceCardFlight", "No card prompt view to update");
                } else {
                    textView.setText(string);
                }
                this.mCardPromptView.setVisibility(0);
            }
            if (!bundle.containsKey("cardPromptBitmap")) {
                return true;
            }
            Bitmap bitmap = (Bitmap) bundle.get("cardPromptBitmap");
            ImageView imageView = this.mCardPromptLogoView;
            if (imageView == null) {
                Log.w("BcSmartspaceCardFlight", "No card prompt logo view to update");
            } else {
                imageView.setImageBitmap(bitmap);
            }
            this.mCardPromptLogoView.setVisibility(0);
            return true;
        }
        if (bundle.containsKey("qrCodeBitmap")) {
            Bitmap bitmap2 = (Bitmap) bundle.get("qrCodeBitmap");
            ImageView imageView2 = this.mQrCodeView;
            if (imageView2 == null) {
                Log.w("BcSmartspaceCardFlight", "No flight QR code view to update");
            } else {
                imageView2.setImageBitmap(bitmap2);
            }
            this.mBoardingPassUI.setVisibility(0);
        } else {
            z = false;
        }
        if (bundle.containsKey("gate")) {
            String string2 = bundle.getString("gate");
            TextView textView2 = this.mGateValueView;
            if (textView2 == null) {
                Log.w("BcSmartspaceCardFlight", "No flight gate value view to update");
            } else {
                textView2.setText(string2);
            }
        } else {
            TextView textView3 = this.mGateValueView;
            if (textView3 == null) {
                Log.w("BcSmartspaceCardFlight", "No flight gate value view to update");
            } else {
                textView3.setText("-");
            }
        }
        if (bundle.containsKey("seat")) {
            String string3 = bundle.getString("seat");
            TextView textView4 = this.mSeatValueView;
            if (textView4 == null) {
                Log.w("BcSmartspaceCardFlight", "No flight seat value view to update");
                return z;
            }
            textView4.setText(string3);
            return z;
        }
        TextView textView5 = this.mSeatValueView;
        if (textView5 == null) {
            Log.w("BcSmartspaceCardFlight", "No flight seat value view to update");
            return z;
        }
        textView5.setText("-");
        return z;
    }
}
