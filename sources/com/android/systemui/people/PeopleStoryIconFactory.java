package com.android.systemui.people;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable21;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public final class PeopleStoryIconFactory implements AutoCloseable {
    public int mAccentColor;
    public Context mContext;
    public float mDensity;
    public final int mIconBitmapSize;
    public float mIconSize;
    public int mImportantConversationColor;
    public final PackageManager mPackageManager;

    /* loaded from: classes.dex */
    public static class PeopleStoryIconDrawable extends Drawable {
        public RoundedBitmapDrawable mAvatar;
        public Drawable mBadgeIcon;
        public float mDensity;
        public float mFullIconSize;
        public int mIconSize;
        public Paint mPriorityRingPaint;
        public boolean mShowImportantRing;
        public boolean mShowStoryRing;
        public Paint mStoryPaint;

        @Override // android.graphics.drawable.Drawable
        public final int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public final void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public final void setColorFilter(ColorFilter colorFilter) {
            RoundedBitmapDrawable roundedBitmapDrawable = this.mAvatar;
            if (roundedBitmapDrawable != null) {
                roundedBitmapDrawable.setColorFilter(colorFilter);
            }
            Drawable drawable = this.mBadgeIcon;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }

        public PeopleStoryIconDrawable(RoundedBitmapDrawable21 roundedBitmapDrawable21, Drawable drawable, int i, int i2, boolean z, float f, float f2, int i3, boolean z2) {
            roundedBitmapDrawable21.mIsCircular = true;
            roundedBitmapDrawable21.mApplyGravity = true;
            roundedBitmapDrawable21.mCornerRadius = Math.min(roundedBitmapDrawable21.mBitmapHeight, roundedBitmapDrawable21.mBitmapWidth) / 2;
            roundedBitmapDrawable21.mPaint.setShader(roundedBitmapDrawable21.mBitmapShader);
            roundedBitmapDrawable21.invalidateSelf();
            this.mAvatar = roundedBitmapDrawable21;
            this.mBadgeIcon = drawable;
            this.mIconSize = i;
            this.mShowImportantRing = z;
            Paint paint = new Paint();
            this.mPriorityRingPaint = paint;
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.mPriorityRingPaint.setColor(i2);
            this.mShowStoryRing = z2;
            Paint paint2 = new Paint();
            this.mStoryPaint = paint2;
            paint2.setStyle(Paint.Style.STROKE);
            this.mStoryPaint.setColor(i3);
            this.mFullIconSize = f;
            this.mDensity = f2;
        }

        @Override // android.graphics.drawable.Drawable
        public final void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float min = Math.min(bounds.height(), bounds.width()) / this.mFullIconSize;
            float f = this.mDensity;
            int i = (int) (f * 2.0f);
            int i2 = (int) (f * 2.0f);
            float f2 = i2;
            this.mPriorityRingPaint.setStrokeWidth(f2);
            this.mStoryPaint.setStrokeWidth(f2);
            int i3 = (int) (this.mFullIconSize * min);
            int i4 = i3 - (i * 2);
            if (this.mAvatar != null) {
                int i5 = i4 + i;
                if (this.mShowStoryRing) {
                    float f3 = i3 / 2;
                    canvas.drawCircle(f3, f3, (i4 - i2) / 2, this.mStoryPaint);
                    int i6 = i2 + i;
                    i += i6;
                    i5 -= i6;
                }
                this.mAvatar.setBounds(i, i, i5, i5);
                this.mAvatar.draw(canvas);
            } else {
                Log.w("PeopleStoryIconFactory", "Null avatar icon");
            }
            int min2 = Math.min((int) (this.mDensity * 40.0f), (int) (i4 / 2.4d));
            if (this.mBadgeIcon != null) {
                int i7 = i3 - min2;
                if (this.mShowImportantRing) {
                    float f4 = (min2 / 2) + i7;
                    canvas.drawCircle(f4, f4, (min2 - i2) / 2, this.mPriorityRingPaint);
                    i7 += i2;
                    i3 -= i2;
                }
                this.mBadgeIcon.setBounds(i7, i7, i3, i3);
                this.mBadgeIcon.draw(canvas);
                return;
            }
            Log.w("PeopleStoryIconFactory", "Null badge icon");
        }

        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicHeight() {
            return this.mIconSize;
        }

        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicWidth() {
            return this.mIconSize;
        }
    }

    @Override // java.lang.AutoCloseable
    public final void close() {
    }

    public PeopleStoryIconFactory(Context context, PackageManager packageManager, int i) {
        context.setTheme(16974563);
        float f = i;
        this.mIconBitmapSize = (int) (context.getResources().getDisplayMetrics().density * f);
        float f2 = context.getResources().getDisplayMetrics().density;
        this.mDensity = f2;
        this.mIconSize = f2 * f;
        this.mPackageManager = packageManager;
        this.mImportantConversationColor = context.getColor(2131099884);
        this.mAccentColor = Utils.getColorAttr(context, 17956901).getDefaultColor();
        this.mContext = context;
    }
}
