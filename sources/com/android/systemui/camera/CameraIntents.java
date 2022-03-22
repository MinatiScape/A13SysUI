package com.android.systemui.camera;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
/* compiled from: CameraIntents.kt */
/* loaded from: classes.dex */
public final class CameraIntents {
    public static final Intent getInsecureCameraIntent(Context context) {
        Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
        String string = context.getResources().getString(2131952131);
        if (string == null || TextUtils.isEmpty(string)) {
            string = null;
        }
        if (string != null) {
            intent.setPackage(string);
        }
        return intent;
    }
}
