package com.airbnb.lottie.manager;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.airbnb.lottie.model.MutablePair;
import com.airbnb.lottie.utils.Logger;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class FontAssetManager {
    public final AssetManager assetManager;
    public final MutablePair<String> tempPair = new MutablePair<>();
    public final HashMap fontMap = new HashMap();
    public final HashMap fontFamilies = new HashMap();
    public String defaultFontFileExtension = ".ttf";

    public FontAssetManager(Drawable.Callback callback) {
        if (!(callback instanceof View)) {
            Logger.warning("LottieDrawable must be inside of a view for images to work.");
            this.assetManager = null;
            return;
        }
        this.assetManager = ((View) callback).getContext().getAssets();
    }
}
