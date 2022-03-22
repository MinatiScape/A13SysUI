package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.graphics.ColorUtils;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class LockIconView extends FrameLayout implements Dumpable {
    public boolean mAod;
    public ImageView mBgView;
    public int mIconType;
    public ImageView mLockIcon;
    public int mLockIconColor;
    public int mRadius;
    public PointF mLockIconCenter = new PointF(0.0f, 0.0f);
    public boolean mUseBackground = false;
    public float mDozeAmount = 0.0f;
    public final RectF mSensorRect = new RectF();

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "Lock Icon View Parameters:", "    Center in px (x, y)= (");
        m.append(this.mLockIconCenter.x);
        m.append(", ");
        m.append(this.mLockIconCenter.y);
        m.append(")");
        printWriter.println(m.toString());
        printWriter.println("    Radius in pixels: " + this.mRadius);
        StringBuilder sb = new StringBuilder();
        sb.append("    mIconType=");
        int i = this.mIconType;
        if (i == -1) {
            str = "none";
        } else if (i == 0) {
            str = "lock";
        } else if (i == 1) {
            str = "fingerprint";
        } else if (i != 2) {
            str = "invalid";
        } else {
            str = "unlock";
        }
        sb.append(str);
        printWriter.println(sb.toString());
        printWriter.println("    mAod=" + this.mAod);
        printWriter.println("Lock Icon View actual measurements:");
        printWriter.println("    topLeft= (" + getX() + ", " + getY() + ")");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("    width=");
        sb2.append(getWidth());
        sb2.append(" height=");
        sb2.append(getHeight());
        printWriter.println(sb2.toString());
    }

    public void setCenterLocation(PointF pointF, int i) {
        this.mLockIconCenter = pointF;
        this.mRadius = i;
        RectF rectF = this.mSensorRect;
        float f = pointF.x;
        float f2 = pointF.y;
        rectF.set(f - i, f2 - i, f + i, f2 + i);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        RectF rectF2 = this.mSensorRect;
        float f3 = rectF2.right;
        float f4 = rectF2.left;
        layoutParams.width = (int) (f3 - f4);
        float f5 = rectF2.bottom;
        float f6 = rectF2.top;
        layoutParams.height = (int) (f5 - f6);
        layoutParams.topMargin = (int) f6;
        layoutParams.setMarginStart((int) f4);
        setLayoutParams(layoutParams);
    }

    public final void updateColorAndBackgroundVisibility() {
        if (!this.mUseBackground || this.mLockIcon.getDrawable() == null) {
            this.mLockIconColor = ColorUtils.blendARGB(Utils.getColorAttrDefaultColor(getContext(), 2130970104), -1, this.mDozeAmount);
            this.mBgView.setVisibility(8);
        } else {
            this.mLockIconColor = ColorUtils.blendARGB(Utils.getColorAttrDefaultColor(getContext(), 16842806), -1, this.mDozeAmount);
            this.mBgView.setBackground(getContext().getDrawable(2131231707));
            this.mBgView.setAlpha(1.0f - this.mDozeAmount);
            this.mBgView.setVisibility(0);
        }
        this.mLockIcon.setImageTintList(ColorStateList.valueOf(this.mLockIconColor));
    }

    public final void updateIcon(int i, boolean z) {
        int[] iArr;
        this.mIconType = i;
        this.mAod = z;
        ImageView imageView = this.mLockIcon;
        if (i == -1) {
            iArr = new int[0];
        } else {
            int[] iArr2 = new int[2];
            if (i == 0) {
                iArr2[0] = 16842916;
            } else if (i == 1) {
                iArr2[0] = 16842917;
            } else if (i == 2) {
                iArr2[0] = 16842918;
            }
            if (z) {
                iArr2[1] = 16842915;
            } else {
                iArr2[1] = -16842915;
            }
            iArr = iArr2;
        }
        imageView.setImageState(iArr, true);
    }

    public LockIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mLockIcon = (ImageView) findViewById(2131428274);
        this.mBgView = (ImageView) findViewById(2131428275);
    }
}
