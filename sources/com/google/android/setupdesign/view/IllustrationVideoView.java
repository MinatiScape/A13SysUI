package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Animatable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.google.android.setupdesign.R$styleable;
import java.io.IOException;
import java.util.Map;
@TargetApi(14)
/* loaded from: classes.dex */
public class IllustrationVideoView extends TextureView implements Animatable, TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {
    public MediaPlayer mediaPlayer;
    public boolean prepared;
    public boolean shouldPauseVideoWhenFinished;
    public Surface surface;
    public int videoResId;
    public String videoResPackageName;
    public float aspectRatio = 1.0f;
    public int visibility = 0;
    public boolean isMediaPlayerLoading = false;

    @Override // android.media.MediaPlayer.OnInfoListener
    public final boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 3) {
            this.isMediaPlayerLoading = false;
            setVisibility(this.visibility);
        }
        return false;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public final void onPrepared(MediaPlayer mediaPlayer) {
        float f;
        this.prepared = true;
        mediaPlayer.setLooping(true);
        if (mediaPlayer.getVideoWidth() <= 0 || mediaPlayer.getVideoHeight() <= 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unexpected video size=");
            m.append(mediaPlayer.getVideoWidth());
            m.append("x");
            m.append(mediaPlayer.getVideoHeight());
            Log.w("IllustrationVideoView", m.toString());
            f = 0.0f;
        } else {
            f = mediaPlayer.getVideoHeight() / mediaPlayer.getVideoWidth();
        }
        if (Float.compare(this.aspectRatio, f) != 0) {
            this.aspectRatio = f;
            requestLayout();
        }
        if (getWindowVisibility() == 0) {
            start();
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public final void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.isMediaPlayerLoading = true;
        setVisibility(this.visibility);
        initVideo();
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public final void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public final void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public final void createMediaPlayer() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (this.surface != null && this.videoResId != 0) {
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mediaPlayer2;
            mediaPlayer2.setSurface(this.surface);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnSeekCompleteListener(this);
            this.mediaPlayer.setOnInfoListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            int i = this.videoResId;
            String str = this.videoResPackageName;
            try {
                this.mediaPlayer.setDataSource(getContext(), Uri.parse("android.resource://" + str + "/" + i), (Map<String, String>) null);
                this.mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.e("IllustrationVideoView", "Unable to set video data source: " + i, e);
            }
        }
    }

    @Override // android.graphics.drawable.Animatable
    public final boolean isRunning() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return false;
        }
        return true;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Log.w("IllustrationVideoView", "MediaPlayer error. what=" + i + " extra=" + i2);
        return false;
    }

    @Override // android.media.MediaPlayer.OnSeekCompleteListener
    public final void onSeekComplete(MediaPlayer mediaPlayer) {
        if (this.prepared) {
            mediaPlayer.start();
        } else {
            Log.e("IllustrationVideoView", "Seek complete but media player not prepared");
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public final boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.mediaPlayer = null;
            this.prepared = false;
        }
        Surface surface = this.surface;
        if (surface == null) {
            return true;
        }
        surface.release();
        this.surface = null;
        return true;
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        this.visibility = i;
        if (this.isMediaPlayerLoading && i == 0) {
            i = 4;
        }
        super.setVisibility(i);
    }

    @Override // android.graphics.drawable.Animatable
    public final void start() {
        MediaPlayer mediaPlayer;
        if (this.prepared && (mediaPlayer = this.mediaPlayer) != null && !mediaPlayer.isPlaying()) {
            this.mediaPlayer.start();
        }
    }

    @Override // android.graphics.drawable.Animatable
    public final void stop() {
        MediaPlayer mediaPlayer;
        if (this.shouldPauseVideoWhenFinished && this.prepared && (mediaPlayer = this.mediaPlayer) != null) {
            mediaPlayer.pause();
        }
    }

    public IllustrationVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.videoResId = 0;
        this.shouldPauseVideoWhenFinished = true;
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudIllustrationVideoView);
            int resourceId = obtainStyledAttributes.getResourceId(1, 0);
            this.shouldPauseVideoWhenFinished = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
            String packageName = getContext().getPackageName();
            if (resourceId != this.videoResId || (packageName != null && !packageName.equals(this.videoResPackageName))) {
                this.videoResId = resourceId;
                this.videoResPackageName = packageName;
                createMediaPlayer();
            }
            setScaleX(0.9999999f);
            setScaleX(0.9999999f);
            setSurfaceTextureListener(this);
        }
    }

    public final void initVideo() {
        if (getWindowVisibility() == 0) {
            Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
            SurfaceTexture surfaceTexture = getSurfaceTexture();
            if (surfaceTexture != null) {
                this.isMediaPlayerLoading = true;
                setVisibility(this.visibility);
                this.surface = new Surface(surfaceTexture);
            }
            if (this.surface != null) {
                createMediaPlayer();
            } else {
                Log.i("IllustrationVideoView", "Surface is null");
            }
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        float f = size2;
        float f2 = size;
        float f3 = this.aspectRatio;
        if (f < f2 * f3) {
            size = (int) (f / f3);
        } else {
            size2 = (int) (f2 * f3);
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
    }

    @Override // android.view.View
    public final void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            start();
        } else {
            stop();
        }
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                this.mediaPlayer = null;
                this.prepared = false;
            }
            Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
        } else if (this.surface == null) {
            initVideo();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }
}
