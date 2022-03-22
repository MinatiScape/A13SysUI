package com.android.systemui.screenshot;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.media.Image;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImageTile implements AutoCloseable {
    public static final ColorSpace COLOR_SPACE = ColorSpace.get(ColorSpace.Named.SRGB);
    public final Image mImage;
    public final Rect mLocation;
    public RenderNode mNode;

    @Override // java.lang.AutoCloseable
    public final synchronized void close() {
        this.mImage.close();
        RenderNode renderNode = this.mNode;
        if (renderNode != null) {
            renderNode.discardDisplayList();
        }
    }

    public ImageTile(Image image, Rect rect) {
        Objects.requireNonNull(image, "image");
        this.mImage = image;
        Objects.requireNonNull(rect);
        this.mLocation = rect;
        Objects.requireNonNull(image.getHardwareBuffer(), "image must be a hardware image");
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("{location=");
        m.append(this.mLocation);
        m.append(", source=");
        m.append(this.mImage);
        m.append(", buffer=");
        m.append(this.mImage.getHardwareBuffer());
        m.append("}");
        return m.toString();
    }
}
