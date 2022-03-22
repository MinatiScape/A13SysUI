package com.android.wm.shell;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.util.Property;
import android.view.View;
import com.android.systemui.glwallpaper.ImageGLProgram;
import com.android.systemui.glwallpaper.ImageGLWallpaper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.wm.shell.recents.RecentTasksController;
import java.nio.Buffer;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellTaskOrganizer$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShellTaskOrganizer$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                RecentTasksController recentTasksController = (RecentTasksController) obj;
                int i = ShellTaskOrganizer.$r8$clinit;
                Objects.requireNonNull(recentTasksController);
                recentTasksController.notifyRecentTasksChanged();
                return;
            case 1:
                ImageWallpaperRenderer imageWallpaperRenderer = (ImageWallpaperRenderer) this.f$0;
                Bitmap bitmap = (Bitmap) obj;
                if (bitmap == null) {
                    Objects.requireNonNull(imageWallpaperRenderer);
                    Log.w("ImageWallpaperRenderer", "reload texture failed!");
                } else {
                    Consumer<Bitmap> consumer = imageWallpaperRenderer.mOnBitmapUpdated;
                    if (consumer != null) {
                        consumer.accept(bitmap);
                    }
                }
                ImageGLWallpaper imageGLWallpaper = imageWallpaperRenderer.mWallpaper;
                Objects.requireNonNull(imageGLWallpaper);
                ImageGLProgram imageGLProgram = imageGLWallpaper.mProgram;
                Objects.requireNonNull(imageGLProgram);
                imageGLWallpaper.mAttrPosition = GLES20.glGetAttribLocation(imageGLProgram.mProgramHandle, "aPosition");
                imageGLWallpaper.mVertexBuffer.position(0);
                GLES20.glVertexAttribPointer(imageGLWallpaper.mAttrPosition, 2, 5126, false, 0, (Buffer) imageGLWallpaper.mVertexBuffer);
                GLES20.glEnableVertexAttribArray(imageGLWallpaper.mAttrPosition);
                ImageGLProgram imageGLProgram2 = imageGLWallpaper.mProgram;
                Objects.requireNonNull(imageGLProgram2);
                imageGLWallpaper.mAttrTextureCoordinates = GLES20.glGetAttribLocation(imageGLProgram2.mProgramHandle, "aTextureCoordinates");
                imageGLWallpaper.mTextureBuffer.position(0);
                GLES20.glVertexAttribPointer(imageGLWallpaper.mAttrTextureCoordinates, 2, 5126, false, 0, (Buffer) imageGLWallpaper.mTextureBuffer);
                GLES20.glEnableVertexAttribArray(imageGLWallpaper.mAttrTextureCoordinates);
                ImageGLProgram imageGLProgram3 = imageGLWallpaper.mProgram;
                Objects.requireNonNull(imageGLProgram3);
                imageGLWallpaper.mUniTexture = GLES20.glGetUniformLocation(imageGLProgram3.mProgramHandle, "uTexture");
                int[] iArr = new int[1];
                if (bitmap == null || bitmap.isRecycled()) {
                    Log.w("ImageGLWallpaper", "setupTexture: invalid bitmap");
                    return;
                }
                GLES20.glGenTextures(1, iArr, 0);
                if (iArr[0] == 0) {
                    Log.w("ImageGLWallpaper", "setupTexture: glGenTextures() failed");
                    return;
                }
                try {
                    GLES20.glBindTexture(3553, iArr[0]);
                    GLUtils.texImage2D(3553, 0, bitmap, 0);
                    GLES20.glTexParameteri(3553, 10241, 9729);
                    GLES20.glTexParameteri(3553, 10240, 9729);
                    imageGLWallpaper.mTextureId = iArr[0];
                    return;
                } catch (IllegalArgumentException e) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed uploading texture: ");
                    m.append(e.getLocalizedMessage());
                    Log.w("ImageGLWallpaper", m.toString());
                    return;
                }
            case 2:
                EdgeBackGestureHandler edgeBackGestureHandler = (EdgeBackGestureHandler) this.f$0;
                Objects.requireNonNull(edgeBackGestureHandler);
                edgeBackGestureHandler.mNavBarOverlayExcludedBounds.set((Rect) obj);
                return;
            case 3:
                NotificationIconContainer.IconState iconState = (NotificationIconContainer.IconState) this.f$0;
                Property property = (Property) obj;
                int i2 = NotificationIconContainer.IconState.$r8$clinit;
                Objects.requireNonNull(iconState);
                if (property == View.TRANSLATION_Y && iconState.iconAppearAmount == 0.0f && iconState.mView.getVisibility() == 0) {
                    iconState.mView.setVisibility(4);
                    return;
                }
                return;
            case 4:
                TvNotificationPanelActivity tvNotificationPanelActivity = (TvNotificationPanelActivity) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                int i3 = TvNotificationPanelActivity.$r8$clinit;
                Objects.requireNonNull(tvNotificationPanelActivity);
                if (booleanValue) {
                    int dimensionPixelSize = tvNotificationPanelActivity.getResources().getDimensionPixelSize(2131167246);
                    tvNotificationPanelActivity.getWindow().setBackgroundDrawable(new ColorDrawable(tvNotificationPanelActivity.getColor(2131100758)));
                    tvNotificationPanelActivity.getWindow().setBackgroundBlurRadius(dimensionPixelSize);
                    return;
                }
                tvNotificationPanelActivity.getWindow().setBackgroundDrawable(new ColorDrawable(tvNotificationPanelActivity.getColor(2131100759)));
                tvNotificationPanelActivity.getWindow().setBackgroundBlurRadius(0);
                return;
            default:
                RecentTasksController.IRecentTasksImpl iRecentTasksImpl = (RecentTasksController.IRecentTasksImpl) this.f$0;
                RecentTasksController recentTasksController2 = (RecentTasksController) obj;
                int i4 = RecentTasksController.IRecentTasksImpl.$r8$clinit;
                Objects.requireNonNull(iRecentTasksImpl);
                iRecentTasksImpl.mListener.unregister();
                return;
        }
    }
}
