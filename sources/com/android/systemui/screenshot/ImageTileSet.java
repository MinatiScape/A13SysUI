package com.android.systemui.screenshot;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import com.android.internal.util.CallbackRegistry;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda3;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImageTileSet {
    public CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> mContentListeners;
    public final Handler mHandler;
    public final ArrayList mTiles = new ArrayList();
    public final Region mRegion = new Region();

    /* loaded from: classes.dex */
    public interface OnContentChangedListener {
        void onContentChanged();
    }

    public final void addTile(ImageTile imageTile) {
        if (!this.mHandler.getLooper().isCurrentThread()) {
            this.mHandler.post(new PipController$$ExternalSyntheticLambda3(this, imageTile, 2));
            return;
        }
        this.mTiles.add(imageTile);
        Region region = this.mRegion;
        Objects.requireNonNull(imageTile);
        region.op(imageTile.mLocation, this.mRegion, Region.Op.UNION);
        CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> callbackRegistry = this.mContentListeners;
        if (callbackRegistry != null) {
            callbackRegistry.notifyCallbacks(this, 0, (Object) null);
        }
    }

    public final void clear() {
        if (!this.mTiles.isEmpty()) {
            this.mRegion.setEmpty();
            Iterator it = this.mTiles.iterator();
            while (it.hasNext()) {
                ((ImageTile) it.next()).close();
                it.remove();
            }
            CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> callbackRegistry = this.mContentListeners;
            if (callbackRegistry != null) {
                callbackRegistry.notifyCallbacks(this, 0, (Object) null);
            }
        }
    }

    public final int getHeight() {
        return this.mRegion.getBounds().height();
    }

    public final int getWidth() {
        return this.mRegion.getBounds().width();
    }

    public ImageTileSet(Handler handler) {
        this.mHandler = handler;
    }
}
