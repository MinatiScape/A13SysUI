package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.ArrayList;
import java.util.Collections;
/* loaded from: classes.dex */
public final class ShapeLayer extends BaseLayer {
    public final ContentGroup contentGroup;

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void drawLayer(Canvas canvas, Matrix matrix, int i) {
        this.contentGroup.draw(canvas, matrix, i);
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void resolveChildKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        this.contentGroup.resolveKeyPath(keyPath, i, arrayList, keyPath2);
    }

    public ShapeLayer(LottieDrawable lottieDrawable, Layer layer) {
        super(lottieDrawable, layer);
        ContentGroup contentGroup = new ContentGroup(lottieDrawable, this, new ShapeGroup("__container", layer.shapes, false));
        this.contentGroup = contentGroup;
        contentGroup.setContents(Collections.emptyList(), Collections.emptyList());
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        this.contentGroup.getBounds(rectF, this.boundsMatrix, z);
    }
}
