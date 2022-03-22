package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.android.internal.util.CallbackRegistry;
import com.android.systemui.screenshot.ImageTileSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TiledImageDrawable extends Drawable {
    public RenderNode mNode;
    public final ImageTileSet mTiles;

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        ImageTileSet imageTileSet;
        ImageTileSet imageTileSet2;
        RenderNode renderNode;
        RenderNode renderNode2 = this.mNode;
        if (renderNode2 == null || !renderNode2.hasDisplayList()) {
            if (this.mNode == null) {
                this.mNode = new RenderNode("TiledImageDrawable");
            }
            this.mNode.setPosition(0, 0, this.mTiles.getWidth(), this.mTiles.getHeight());
            RecordingCanvas beginRecording = this.mNode.beginRecording();
            Objects.requireNonNull(this.mTiles);
            Objects.requireNonNull(this.mTiles);
            beginRecording.translate(-imageTileSet.mRegion.getBounds().left, -imageTileSet2.mRegion.getBounds().top);
            int i = 0;
            while (true) {
                ImageTileSet imageTileSet3 = this.mTiles;
                Objects.requireNonNull(imageTileSet3);
                if (i >= imageTileSet3.mTiles.size()) {
                    break;
                }
                ImageTileSet imageTileSet4 = this.mTiles;
                Objects.requireNonNull(imageTileSet4);
                ImageTile imageTile = (ImageTile) imageTileSet4.mTiles.get(i);
                beginRecording.save();
                Objects.requireNonNull(imageTile);
                Rect rect = imageTile.mLocation;
                beginRecording.translate(rect.left, rect.top);
                synchronized (imageTile) {
                    if (imageTile.mNode == null) {
                        imageTile.mNode = new RenderNode("Tile{" + Integer.toHexString(imageTile.mImage.hashCode()) + "}");
                    }
                    if (imageTile.mNode.hasDisplayList()) {
                        renderNode = imageTile.mNode;
                    } else {
                        int min = Math.min(imageTile.mImage.getWidth(), imageTile.mLocation.width());
                        int min2 = Math.min(imageTile.mImage.getHeight(), imageTile.mLocation.height());
                        imageTile.mNode.setPosition(0, 0, min, min2);
                        RecordingCanvas beginRecording2 = imageTile.mNode.beginRecording(min, min2);
                        beginRecording2.save();
                        beginRecording2.clipRect(0, 0, imageTile.mLocation.width(), imageTile.mLocation.height());
                        beginRecording2.drawBitmap(Bitmap.wrapHardwareBuffer(imageTile.mImage.getHardwareBuffer(), ImageTile.COLOR_SPACE), 0.0f, 0.0f, (Paint) null);
                        beginRecording2.restore();
                        imageTile.mNode.endRecording();
                        renderNode = imageTile.mNode;
                    }
                }
                beginRecording.drawRenderNode(renderNode);
                beginRecording.restore();
                i++;
            }
            this.mNode.endRecording();
        }
        if (canvas.isHardwareAccelerated()) {
            Rect bounds = getBounds();
            canvas.save();
            canvas.clipRect(0, 0, bounds.width(), bounds.height());
            canvas.translate(-bounds.left, -bounds.top);
            canvas.drawRenderNode(this.mNode);
            canvas.restore();
            return;
        }
        Log.d("TiledImageDrawable", "Canvas is not hardware accelerated. Skipping draw!");
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return this.mTiles.getHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return this.mTiles.getWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        if (this.mNode.setAlpha(i / 255.0f)) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        throw new IllegalArgumentException("not implemented");
    }

    public TiledImageDrawable(ImageTileSet imageTileSet) {
        this.mTiles = imageTileSet;
        ImageTileSet.OnContentChangedListener tiledImageDrawable$$ExternalSyntheticLambda0 = new ImageTileSet.OnContentChangedListener() { // from class: com.android.systemui.screenshot.TiledImageDrawable$$ExternalSyntheticLambda0
            @Override // com.android.systemui.screenshot.ImageTileSet.OnContentChangedListener
            public final void onContentChanged() {
                TiledImageDrawable tiledImageDrawable = TiledImageDrawable.this;
                Objects.requireNonNull(tiledImageDrawable);
                RenderNode renderNode = tiledImageDrawable.mNode;
                if (renderNode != null && renderNode.hasDisplayList()) {
                    tiledImageDrawable.mNode.discardDisplayList();
                }
                tiledImageDrawable.invalidateSelf();
            }
        };
        if (imageTileSet.mContentListeners == null) {
            imageTileSet.mContentListeners = new CallbackRegistry<>(new CallbackRegistry.NotifierCallback<ImageTileSet.OnContentChangedListener, ImageTileSet, Rect>() { // from class: com.android.systemui.screenshot.ImageTileSet.1
                public final void onNotifyCallback(Object obj, Object obj2, int i, Object obj3) {
                    ImageTileSet imageTileSet2 = (ImageTileSet) obj2;
                    Rect rect = (Rect) obj3;
                    ((OnContentChangedListener) obj).onContentChanged();
                }
            });
        }
        imageTileSet.mContentListeners.add(tiledImageDrawable$$ExternalSyntheticLambda0);
    }
}
