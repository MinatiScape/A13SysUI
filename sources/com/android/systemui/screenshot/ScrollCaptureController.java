package com.android.systemui.screenshot;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.util.Log;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ScrollCaptureController {
    public final Executor mBgExecutor;
    public volatile boolean mCancelled;
    public CallbackToFutureAdapter.Completer<LongScreenshot> mCaptureCompleter;
    public final ScrollCaptureClient mClient;
    public final Context mContext;
    public ListenableFuture<Void> mEndFuture;
    public final UiEventLogger mEventLogger;
    public boolean mFinishOnBoundary;
    public final ImageTileSet mImageTileSet;
    public boolean mScrollingUp = true;
    public ScrollCaptureClient.Session mSession;
    public CallbackToFutureAdapter.SafeFuture mSessionFuture;
    public ListenableFuture<ScrollCaptureClient.CaptureResult> mTileFuture;
    public String mWindowOwner;

    /* loaded from: classes.dex */
    public static class LongScreenshot {
        public final ImageTileSet mImageTileSet;
        public final ScrollCaptureClient.Session mSession;

        public final Bitmap toBitmap() {
            ImageTileSet imageTileSet = this.mImageTileSet;
            Objects.requireNonNull(imageTileSet);
            Rect rect = new Rect(0, 0, imageTileSet.getWidth(), imageTileSet.getHeight());
            if (imageTileSet.mTiles.isEmpty()) {
                return null;
            }
            RenderNode renderNode = new RenderNode("Bitmap Export");
            renderNode.setPosition(0, 0, rect.width(), rect.height());
            RecordingCanvas beginRecording = renderNode.beginRecording();
            TiledImageDrawable tiledImageDrawable = new TiledImageDrawable(imageTileSet);
            tiledImageDrawable.setBounds(rect);
            tiledImageDrawable.draw(beginRecording);
            renderNode.endRecording();
            return HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LongScreenshot{w=");
            m.append(this.mImageTileSet.getWidth());
            m.append(", h=");
            m.append(this.mImageTileSet.getHeight());
            m.append("}");
            return m.toString();
        }

        public LongScreenshot(ScrollCaptureClient.Session session, ImageTileSet imageTileSet) {
            this.mSession = session;
            this.mImageTileSet = imageTileSet;
        }
    }

    @VisibleForTesting
    public float getTargetTopSizeRatio() {
        return 0.4f;
    }

    public final void finishCapture() {
        if (this.mImageTileSet.getHeight() > 0) {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_COMPLETED, 0, this.mWindowOwner);
        } else {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
        }
        CallbackToFutureAdapter.SafeFuture end = this.mSession.end();
        this.mEndFuture = end;
        BubbleExpandedView$1$$ExternalSyntheticLambda0 bubbleExpandedView$1$$ExternalSyntheticLambda0 = new BubbleExpandedView$1$$ExternalSyntheticLambda0(this, 5);
        Executor mainExecutor = this.mContext.getMainExecutor();
        Objects.requireNonNull(end);
        end.delegate.addListener(bubbleExpandedView$1$$ExternalSyntheticLambda0, mainExecutor);
    }

    public final void onCaptureResult(ScrollCaptureClient.CaptureResult captureResult) {
        boolean z;
        int i;
        int i2;
        int i3;
        if (captureResult.captured.height() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            ImageTileSet imageTileSet = this.mImageTileSet;
            Objects.requireNonNull(imageTileSet);
            int size = imageTileSet.mTiles.size() + 1;
            this.mSession.getMaxTiles();
            if (size >= 30) {
                finishCapture();
                return;
            } else if (this.mScrollingUp && !this.mFinishOnBoundary) {
                if (captureResult.captured.height() + this.mImageTileSet.getHeight() >= this.mSession.getTargetHeight() * 0.4f) {
                    this.mImageTileSet.clear();
                    this.mScrollingUp = false;
                }
            }
        } else if (this.mFinishOnBoundary) {
            finishCapture();
            return;
        } else {
            this.mImageTileSet.clear();
            this.mFinishOnBoundary = true;
            this.mScrollingUp = !this.mScrollingUp;
        }
        if (!z) {
            this.mImageTileSet.addTile(new ImageTile(captureResult.image, captureResult.captured));
        }
        ImageTileSet imageTileSet2 = this.mImageTileSet;
        Objects.requireNonNull(imageTileSet2);
        Region region = new Region();
        region.op(imageTileSet2.mRegion.getBounds(), imageTileSet2.mRegion, Region.Op.DIFFERENCE);
        Rect bounds = region.getBounds();
        if (!bounds.isEmpty()) {
            requestNextTile(bounds.top);
        } else if (this.mImageTileSet.getHeight() >= this.mSession.getTargetHeight()) {
            finishCapture();
        } else if (z) {
            if (this.mScrollingUp) {
                i2 = captureResult.requested.top;
                i3 = this.mSession.getTileHeight();
                i = i2 - i3;
                requestNextTile(i);
            }
            i = captureResult.requested.bottom;
            requestNextTile(i);
        } else if (this.mScrollingUp) {
            ImageTileSet imageTileSet3 = this.mImageTileSet;
            Objects.requireNonNull(imageTileSet3);
            i2 = imageTileSet3.mRegion.getBounds().top;
            i3 = this.mSession.getTileHeight();
            i = i2 - i3;
            requestNextTile(i);
        } else {
            ImageTileSet imageTileSet4 = this.mImageTileSet;
            Objects.requireNonNull(imageTileSet4);
            i = imageTileSet4.mRegion.getBounds().bottom;
            requestNextTile(i);
        }
    }

    public final void requestNextTile(int i) {
        if (this.mCancelled) {
            Log.d("Screenshot", "requestNextTile: CANCELLED");
            return;
        }
        CallbackToFutureAdapter.SafeFuture requestTile = this.mSession.requestTile(i);
        this.mTileFuture = requestTile;
        BubbleStackView$$ExternalSyntheticLambda17 bubbleStackView$$ExternalSyntheticLambda17 = new BubbleStackView$$ExternalSyntheticLambda17(this, 6);
        Executor executor = this.mBgExecutor;
        Objects.requireNonNull(requestTile);
        requestTile.delegate.addListener(bubbleStackView$$ExternalSyntheticLambda17, executor);
    }

    public ScrollCaptureController(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, ImageTileSet imageTileSet, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mBgExecutor = executor;
        this.mClient = scrollCaptureClient;
        this.mImageTileSet = imageTileSet;
        this.mEventLogger = uiEventLogger;
    }
}
