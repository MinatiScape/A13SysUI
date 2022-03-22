package com.android.systemui.screenshot;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.util.Log;
import android.view.IScrollCaptureCallbacks;
import android.view.IScrollCaptureConnection;
import android.view.IWindowManager;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.battery.BatteryMeterViewController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ScrollCaptureClient {
    @VisibleForTesting
    public static final int MATCH_ANY_TASK = -1;
    public final Executor mBgExecutor;
    public IBinder mHostWindowToken;
    public final IWindowManager mWindowManagerService;

    /* loaded from: classes.dex */
    public static class CaptureResult {
        public final Rect captured;
        public final Image image;
        public final Rect requested;

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CaptureResult{requested=");
            m.append(this.requested);
            m.append(" (");
            m.append(this.requested.width());
            m.append("x");
            m.append(this.requested.height());
            m.append("), captured=");
            m.append(this.captured);
            m.append(" (");
            m.append(this.captured.width());
            m.append("x");
            m.append(this.captured.height());
            m.append("), image=");
            m.append(this.image);
            m.append('}');
            return m.toString();
        }

        public CaptureResult(Image image, Rect rect, Rect rect2) {
            this.image = image;
            this.requested = rect;
            this.captured = rect2;
        }
    }

    /* loaded from: classes.dex */
    public interface Session {
        CallbackToFutureAdapter.SafeFuture end();

        void getMaxTiles();

        int getPageHeight();

        int getTargetHeight();

        int getTileHeight();

        void release();

        CallbackToFutureAdapter.SafeFuture requestTile(int i);
    }

    /* loaded from: classes.dex */
    public static class SessionWrapper extends IScrollCaptureCallbacks.Stub implements Session, IBinder.DeathRecipient, ImageReader.OnImageAvailableListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final Executor mBgExecutor;
        public final Rect mBoundsInWindow;
        public ICancellationSignal mCancellationSignal;
        public Rect mCapturedArea;
        public Image mCapturedImage;
        public IScrollCaptureConnection mConnection;
        public CallbackToFutureAdapter.Completer<Void> mEndCompleter;
        public final Object mLock = new Object();
        public ImageReader mReader;
        public Rect mRequestRect;
        public CallbackToFutureAdapter.Completer<Session> mStartCompleter;
        public boolean mStarted;
        public final int mTargetHeight;
        public final int mTileHeight;
        public CallbackToFutureAdapter.Completer<CaptureResult> mTileRequestCompleter;
        public final int mTileWidth;

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final void getMaxTiles() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            Log.d("Screenshot", "binderDied! The target process just crashed :-(");
            this.mConnection = null;
            CallbackToFutureAdapter.Completer<Session> completer = this.mStartCompleter;
            if (completer != null) {
                completer.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<CaptureResult> completer2 = this.mTileRequestCompleter;
            if (completer2 != null) {
                completer2.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<Void> completer3 = this.mEndCompleter;
            if (completer3 != null) {
                completer3.setException(new DeadObjectException("The remote process died"));
            }
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final CallbackToFutureAdapter.SafeFuture end() {
            Log.d("Screenshot", "end()");
            return CallbackToFutureAdapter.getFuture(new StatusBar$$ExternalSyntheticLambda2(this));
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final int getPageHeight() {
            return this.mBoundsInWindow.height();
        }

        public final void onCaptureEnded() {
            try {
                this.mConnection.close();
            } catch (RemoteException unused) {
            }
            this.mConnection = null;
            this.mEndCompleter.set(null);
        }

        public final void onCaptureStarted() {
            Log.d("Screenshot", "onCaptureStarted");
            this.mStartCompleter.set(this);
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public final void onImageAvailable(ImageReader imageReader) {
            synchronized (this.mLock) {
                Image acquireLatestImage = this.mReader.acquireLatestImage();
                this.mCapturedImage = acquireLatestImage;
                Rect rect = this.mCapturedArea;
                if (rect != null) {
                    CaptureResult captureResult = new CaptureResult(acquireLatestImage, this.mRequestRect, rect);
                    this.mCapturedImage = null;
                    this.mRequestRect = null;
                    this.mCapturedArea = null;
                    this.mTileRequestCompleter.set(captureResult);
                }
            }
        }

        public final void onImageRequestCompleted(int i, Rect rect) {
            synchronized (this.mLock) {
                this.mCapturedArea = rect;
                if (this.mCapturedImage != null || rect == null || rect.isEmpty()) {
                    CaptureResult captureResult = new CaptureResult(this.mCapturedImage, this.mRequestRect, this.mCapturedArea);
                    this.mCapturedImage = null;
                    this.mRequestRect = null;
                    this.mCapturedArea = null;
                    this.mTileRequestCompleter.set(captureResult);
                }
            }
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final void release() {
            this.mReader.close();
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final CallbackToFutureAdapter.SafeFuture requestTile(int i) {
            this.mRequestRect = new Rect(0, i, this.mTileWidth, this.mTileHeight + i);
            return CallbackToFutureAdapter.getFuture(new BatteryMeterViewController$$ExternalSyntheticLambda0(this));
        }

        public SessionWrapper(IScrollCaptureConnection iScrollCaptureConnection, Rect rect, Rect rect2, float f, Executor executor) {
            this.mConnection = iScrollCaptureConnection;
            iScrollCaptureConnection.asBinder().linkToDeath(this, 0);
            Objects.requireNonNull(rect);
            Objects.requireNonNull(rect2);
            this.mBoundsInWindow = rect2;
            int min = Math.min(4194304, (rect2.height() * rect2.width()) / 2);
            this.mTileWidth = rect2.width();
            this.mTileHeight = min / rect2.width();
            this.mTargetHeight = (int) (rect2.height() * f);
            this.mBgExecutor = executor;
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final int getTargetHeight() {
            return this.mTargetHeight;
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public final int getTileHeight() {
            return this.mTileHeight;
        }
    }

    public ScrollCaptureClient(IWindowManager iWindowManager, Executor executor, Context context) {
        Objects.requireNonNull(context.getDisplay(), "context must be associated with a Display!");
        this.mBgExecutor = executor;
        this.mWindowManagerService = iWindowManager;
    }
}
