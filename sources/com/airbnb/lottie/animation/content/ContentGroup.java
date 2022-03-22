package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ContentGroup implements DrawingContent, PathContent, BaseKeyframeAnimation.AnimationListener, KeyPathElement {
    public final List<Content> contents;
    public final boolean hidden;
    public final LottieDrawable lottieDrawable;
    public final Matrix matrix;
    public final String name;
    public LPaint offScreenPaint;
    public RectF offScreenRectF;
    public final Path path;
    public ArrayList pathContents;
    public final RectF rect;
    public TransformKeyframeAnimation transformAnimation;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ContentGroup(com.airbnb.lottie.LottieDrawable r8, com.airbnb.lottie.model.layer.BaseLayer r9, com.airbnb.lottie.model.content.ShapeGroup r10) {
        /*
            r7 = this;
            java.util.Objects.requireNonNull(r10)
            java.lang.String r3 = r10.name
            boolean r4 = r10.hidden
            java.util.List<com.airbnb.lottie.model.content.ContentModel> r0 = r10.items
            java.util.ArrayList r5 = new java.util.ArrayList
            int r1 = r0.size()
            r5.<init>(r1)
            r1 = 0
            r2 = r1
        L_0x0014:
            int r6 = r0.size()
            if (r2 >= r6) goto L_0x002c
            java.lang.Object r6 = r0.get(r2)
            com.airbnb.lottie.model.content.ContentModel r6 = (com.airbnb.lottie.model.content.ContentModel) r6
            com.airbnb.lottie.animation.content.Content r6 = r6.toContent(r8, r9)
            if (r6 == 0) goto L_0x0029
            r5.add(r6)
        L_0x0029:
            int r2 = r2 + 1
            goto L_0x0014
        L_0x002c:
            java.util.List<com.airbnb.lottie.model.content.ContentModel> r10 = r10.items
        L_0x002e:
            int r0 = r10.size()
            if (r1 >= r0) goto L_0x0045
            java.lang.Object r0 = r10.get(r1)
            com.airbnb.lottie.model.content.ContentModel r0 = (com.airbnb.lottie.model.content.ContentModel) r0
            boolean r2 = r0 instanceof com.airbnb.lottie.model.animatable.AnimatableTransform
            if (r2 == 0) goto L_0x0042
            com.airbnb.lottie.model.animatable.AnimatableTransform r0 = (com.airbnb.lottie.model.animatable.AnimatableTransform) r0
            r6 = r0
            goto L_0x0047
        L_0x0042:
            int r1 = r1 + 1
            goto L_0x002e
        L_0x0045:
            r10 = 0
            r6 = r10
        L_0x0047:
            r0 = r7
            r1 = r8
            r2 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.animation.content.ContentGroup.<init>(com.airbnb.lottie.LottieDrawable, com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.content.ShapeGroup):void");
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        TransformKeyframeAnimation transformKeyframeAnimation = this.transformAnimation;
        if (transformKeyframeAnimation != null) {
            transformKeyframeAnimation.applyValueCallback(t, lottieValueCallback);
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        boolean z;
        int i2;
        if (!this.hidden) {
            this.matrix.set(matrix);
            TransformKeyframeAnimation transformKeyframeAnimation = this.transformAnimation;
            if (transformKeyframeAnimation != null) {
                this.matrix.preConcat(transformKeyframeAnimation.getMatrix());
                TransformKeyframeAnimation transformKeyframeAnimation2 = this.transformAnimation;
                Objects.requireNonNull(transformKeyframeAnimation2);
                if (transformKeyframeAnimation2.opacity == null) {
                    i2 = 100;
                } else {
                    TransformKeyframeAnimation transformKeyframeAnimation3 = this.transformAnimation;
                    Objects.requireNonNull(transformKeyframeAnimation3);
                    i2 = transformKeyframeAnimation3.opacity.getValue().intValue();
                }
                i = (int) ((((i2 / 100.0f) * i) / 255.0f) * 255.0f);
            }
            LottieDrawable lottieDrawable = this.lottieDrawable;
            Objects.requireNonNull(lottieDrawable);
            boolean z2 = false;
            if (lottieDrawable.isApplyingOpacityToLayersEnabled) {
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    if (i3 >= this.contents.size()) {
                        z = false;
                        break;
                    }
                    if ((this.contents.get(i3) instanceof DrawingContent) && (i4 = i4 + 1) >= 2) {
                        z = true;
                        break;
                    }
                    i3++;
                }
                if (z && i != 255) {
                    z2 = true;
                }
            }
            if (z2) {
                this.offScreenRectF.set(0.0f, 0.0f, 0.0f, 0.0f);
                getBounds(this.offScreenRectF, this.matrix, true);
                this.offScreenPaint.setAlpha(i);
                RectF rectF = this.offScreenRectF;
                LPaint lPaint = this.offScreenPaint;
                PathMeasure pathMeasure = Utils.pathMeasure;
                canvas.saveLayer(rectF, lPaint);
                L.endSection();
            }
            if (z2) {
                i = 255;
            }
            for (int size = this.contents.size() - 1; size >= 0; size--) {
                Content content = this.contents.get(size);
                if (content instanceof DrawingContent) {
                    ((DrawingContent) content).draw(canvas, this.matrix, i);
                }
            }
            if (z2) {
                canvas.restore();
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.matrix.set(matrix);
        TransformKeyframeAnimation transformKeyframeAnimation = this.transformAnimation;
        if (transformKeyframeAnimation != null) {
            this.matrix.preConcat(transformKeyframeAnimation.getMatrix());
        }
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        int size = this.contents.size();
        while (true) {
            size--;
            if (size >= 0) {
                Content content = this.contents.get(size);
                if (content instanceof DrawingContent) {
                    ((DrawingContent) content).getBounds(this.rect, this.matrix, z);
                    rectF.union(this.rect);
                }
            } else {
                return;
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        this.matrix.reset();
        TransformKeyframeAnimation transformKeyframeAnimation = this.transformAnimation;
        if (transformKeyframeAnimation != null) {
            this.matrix.set(transformKeyframeAnimation.getMatrix());
        }
        this.path.reset();
        if (this.hidden) {
            return this.path;
        }
        for (int size = this.contents.size() - 1; size >= 0; size--) {
            Content content = this.contents.get(size);
            if (content instanceof PathContent) {
                this.path.addPath(((PathContent) content).getPath(), this.matrix);
            }
        }
        return this.path;
    }

    public final List<PathContent> getPathList() {
        if (this.pathContents == null) {
            this.pathContents = new ArrayList();
            for (int i = 0; i < this.contents.size(); i++) {
                Content content = this.contents.get(i);
                if (content instanceof PathContent) {
                    this.pathContents.add((PathContent) content);
                }
            }
        }
        return this.pathContents;
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final void resolveKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        if (keyPath.matches(this.name, i)) {
            if (!"__container".equals(this.name)) {
                String str = this.name;
                Objects.requireNonNull(keyPath2);
                KeyPath keyPath3 = new KeyPath(keyPath2);
                keyPath3.keys.add(str);
                if (keyPath.fullyResolvesTo(this.name, i)) {
                    KeyPath keyPath4 = new KeyPath(keyPath3);
                    keyPath4.resolvedElement = this;
                    arrayList.add(keyPath4);
                }
                keyPath2 = keyPath3;
            }
            if (keyPath.propagateToChildren(this.name, i)) {
                int incrementDepthBy = keyPath.incrementDepthBy(this.name, i) + i;
                for (int i2 = 0; i2 < this.contents.size(); i2++) {
                    Content content = this.contents.get(i2);
                    if (content instanceof KeyPathElement) {
                        ((KeyPathElement) content).resolveKeyPath(keyPath, incrementDepthBy, arrayList, keyPath2);
                    }
                }
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        ArrayList arrayList = new ArrayList(this.contents.size() + list.size());
        arrayList.addAll(list);
        for (int size = this.contents.size() - 1; size >= 0; size--) {
            Content content = this.contents.get(size);
            content.setContents(arrayList, this.contents.subList(0, size));
            arrayList.add(content);
        }
    }

    public ContentGroup(LottieDrawable lottieDrawable, BaseLayer baseLayer, String str, boolean z, ArrayList arrayList, AnimatableTransform animatableTransform) {
        this.offScreenPaint = new LPaint();
        this.offScreenRectF = new RectF();
        this.matrix = new Matrix();
        this.path = new Path();
        this.rect = new RectF();
        this.name = str;
        this.lottieDrawable = lottieDrawable;
        this.hidden = z;
        this.contents = arrayList;
        if (animatableTransform != null) {
            TransformKeyframeAnimation transformKeyframeAnimation = new TransformKeyframeAnimation(animatableTransform);
            this.transformAnimation = transformKeyframeAnimation;
            transformKeyframeAnimation.addAnimationsToLayer(baseLayer);
            this.transformAnimation.addListener(this);
        }
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            Content content = (Content) arrayList.get(size);
            if (content instanceof GreedyContent) {
                arrayList2.add((GreedyContent) content);
            }
        }
        int size2 = arrayList2.size();
        while (true) {
            size2--;
            if (size2 >= 0) {
                ((GreedyContent) arrayList2.get(size2)).absorbContent(arrayList.listIterator(arrayList.size()));
            } else {
                return;
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final String getName() {
        return this.name;
    }
}
