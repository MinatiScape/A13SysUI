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
public class BcSmartspaceCardSports extends BcSmartspaceCardSecondary {
    public BcSmartspaceCardSports(Context context) {
        super(context);
    }

    public BcSmartspaceCardSports(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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
        boolean z = false;
        if (bundle == null) {
            return false;
        }
        if (bundle.containsKey("matchTimeSummary")) {
            String string = bundle.getString("matchTimeSummary");
            TextView textView = (TextView) findViewById(2131428301);
            if (textView == null) {
                Log.w("BcSmartspaceCardSports", "No match time summary view to update");
            } else {
                textView.setText(string);
            }
            z = true;
        }
        if (bundle.containsKey("firstCompetitorScore")) {
            String string2 = bundle.getString("firstCompetitorScore");
            TextView textView2 = (TextView) findViewById(2131427974);
            if (textView2 == null) {
                Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
            } else {
                textView2.setText(string2);
            }
            z = true;
        }
        if (bundle.containsKey("secondCompetitorScore")) {
            String string3 = bundle.getString("secondCompetitorScore");
            TextView textView3 = (TextView) findViewById(2131428813);
            if (textView3 == null) {
                Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
            } else {
                textView3.setText(string3);
            }
            z = true;
        }
        if (bundle.containsKey("firstCompetitorLogo")) {
            Bitmap bitmap = (Bitmap) bundle.get("firstCompetitorLogo");
            ImageView imageView = (ImageView) findViewById(2131427973);
            if (imageView == null) {
                Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
            } else {
                imageView.setImageBitmap(bitmap);
            }
            z = true;
        }
        if (!bundle.containsKey("secondCompetitorLogo")) {
            return z;
        }
        Bitmap bitmap2 = (Bitmap) bundle.get("secondCompetitorLogo");
        ImageView imageView2 = (ImageView) findViewById(2131428812);
        if (imageView2 == null) {
            Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
        } else {
            imageView2.setImageBitmap(bitmap2);
        }
        return true;
    }
}
