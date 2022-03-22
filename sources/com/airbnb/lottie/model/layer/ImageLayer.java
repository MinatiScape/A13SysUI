package com.airbnb.lottie.model.layer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.manager.ImageAssetManager;
import com.airbnb.lottie.utils.LogcatLogger;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImageLayer extends BaseLayer {
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final LPaint paint = new LPaint(3);
    public final Rect src = new Rect();
    public final Rect dst = new Rect();

    public final Bitmap getBitmap() {
        LottieImageAsset lottieImageAsset;
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        String str = layer.refId;
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        ImageAssetManager imageAssetManager = lottieDrawable.getImageAssetManager();
        if (imageAssetManager == null || (lottieImageAsset = imageAssetManager.imageAssets.get(str)) == null) {
            return null;
        }
        Bitmap bitmap = lottieImageAsset.bitmap;
        if (bitmap != null) {
            return bitmap;
        }
        ImageAssetDelegate imageAssetDelegate = imageAssetManager.delegate;
        if (imageAssetDelegate != null) {
            Bitmap fetchBitmap = imageAssetDelegate.fetchBitmap();
            if (fetchBitmap == null) {
                return fetchBitmap;
            }
            imageAssetManager.putBitmap(str, fetchBitmap);
            return fetchBitmap;
        }
        String str2 = lottieImageAsset.fileName;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = 160;
        if (!str2.startsWith("data:") || str2.indexOf("base64,") <= 0) {
            try {
                if (!TextUtils.isEmpty(imageAssetManager.imagesFolder)) {
                    AssetManager assets = imageAssetManager.context.getAssets();
                    Bitmap decodeStream = BitmapFactory.decodeStream(assets.open(imageAssetManager.imagesFolder + str2), null, options);
                    int i = lottieImageAsset.width;
                    int i2 = lottieImageAsset.height;
                    PathMeasure pathMeasure = Utils.pathMeasure;
                    if (!(decodeStream.getWidth() == i && decodeStream.getHeight() == i2)) {
                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeStream, i, i2, true);
                        decodeStream.recycle();
                        decodeStream = createScaledBitmap;
                    }
                    imageAssetManager.putBitmap(str, decodeStream);
                    return decodeStream;
                }
                throw new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
            } catch (IOException e) {
                Objects.requireNonNull(Logger.INSTANCE);
                HashSet hashSet = LogcatLogger.loggedMessages;
                if (hashSet.contains("Unable to open asset.")) {
                    return null;
                }
                Log.w("LOTTIE", "Unable to open asset.", e);
                hashSet.add("Unable to open asset.");
                return null;
            }
        } else {
            try {
                byte[] decode = Base64.decode(str2.substring(str2.indexOf(44) + 1), 0);
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
                imageAssetManager.putBitmap(str, decodeByteArray);
                return decodeByteArray;
            } catch (IllegalArgumentException e2) {
                Objects.requireNonNull(Logger.INSTANCE);
                HashSet hashSet2 = LogcatLogger.loggedMessages;
                if (hashSet2.contains("data URL did not have correct base64 format.")) {
                    return null;
                }
                Log.w("LOTTIE", "data URL did not have correct base64 format.", e2);
                hashSet2.add("data URL did not have correct base64 format.");
                return null;
            }
        }
    }

    public ImageLayer(LottieDrawable lottieDrawable, Layer layer) {
        super(lottieDrawable, layer);
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t != LottieProperty.COLOR_FILTER) {
            return;
        }
        if (lottieValueCallback == null) {
            this.colorFilterAnimation = null;
        } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void drawLayer(Canvas canvas, Matrix matrix, int i) {
        Bitmap bitmap = getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            float dpScale = Utils.dpScale();
            this.paint.setAlpha(i);
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
            }
            canvas.save();
            canvas.concat(matrix);
            this.src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            this.dst.set(0, 0, (int) (bitmap.getWidth() * dpScale), (int) (bitmap.getHeight() * dpScale));
            canvas.drawBitmap(bitmap, this.src, this.dst, this.paint);
            canvas.restore();
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            rectF.set(0.0f, 0.0f, Utils.dpScale() * bitmap.getWidth(), Utils.dpScale() * bitmap.getHeight());
            this.boundsMatrix.mapRect(rectF);
        }
    }
}
