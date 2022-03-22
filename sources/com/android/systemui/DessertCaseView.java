package com.android.systemui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public class DessertCaseView extends FrameLayout {
    public static final int NUM_PASTRIES;
    public static final int[] PASTRIES;
    public static final int[] RARE_PASTRIES;
    public static final int[] XRARE_PASTRIES;
    public static final int[] XXRARE_PASTRIES;
    public int mCellSize;
    public View[] mCells;
    public int mColumns;
    public int mHeight;
    public int mRows;
    public int mWidth;
    public static final float[] MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public static final float[] ALPHA_MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    public SparseArray<Drawable> mDrawables = new SparseArray<>(NUM_PASTRIES);
    public final HashSet mFreeList = new HashSet();
    public final Handler mHandler = new Handler();
    public final AnonymousClass1 mJuggle = new Runnable() { // from class: com.android.systemui.DessertCaseView.1
        @Override // java.lang.Runnable
        public final void run() {
            int childCount = DessertCaseView.this.getChildCount();
            for (int i = 0; i < 1; i++) {
                View childAt = DessertCaseView.this.getChildAt((int) (Math.random() * childCount));
                DessertCaseView dessertCaseView = DessertCaseView.this;
                Objects.requireNonNull(dessertCaseView);
                float f = 0;
                dessertCaseView.place(childAt, new Point((int) MotionController$$ExternalSyntheticOutline0.m(dessertCaseView.mColumns, f, (float) Math.random(), f), (int) MotionController$$ExternalSyntheticOutline0.m(dessertCaseView.mRows, f, (float) Math.random(), f)), true);
            }
            DessertCaseView dessertCaseView2 = DessertCaseView.this;
            Objects.requireNonNull(dessertCaseView2);
            dessertCaseView2.fillFreeList(500);
            DessertCaseView dessertCaseView3 = DessertCaseView.this;
            if (dessertCaseView3.mStarted) {
                dessertCaseView3.mHandler.postDelayed(dessertCaseView3.mJuggle, 2000L);
            }
        }
    };
    public float[] hsv = {0.0f, 1.0f, 0.85f};
    public final HashSet<View> tmpSet = new HashSet<>();
    public boolean mStarted = false;

    public final synchronized void fillFreeList(int i) {
        Drawable drawable;
        Context context = getContext();
        int i2 = this.mCellSize;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i2, i2);
        while (!this.mFreeList.isEmpty()) {
            Point point = (Point) this.mFreeList.iterator().next();
            this.mFreeList.remove(point);
            if (this.mCells[(point.y * this.mColumns) + point.x] == null) {
                final ImageView imageView = new ImageView(context);
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.DessertCaseView.2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        DessertCaseView dessertCaseView = DessertCaseView.this;
                        ImageView imageView2 = imageView;
                        Objects.requireNonNull(dessertCaseView);
                        float f = 0;
                        dessertCaseView.place(imageView2, new Point((int) MotionController$$ExternalSyntheticOutline0.m(dessertCaseView.mColumns, f, (float) Math.random(), f), (int) MotionController$$ExternalSyntheticOutline0.m(dessertCaseView.mRows, f, (float) Math.random(), f)), true);
                        DessertCaseView.this.postDelayed(new Runnable() { // from class: com.android.systemui.DessertCaseView.2.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                DessertCaseView dessertCaseView2 = DessertCaseView.this;
                                Objects.requireNonNull(dessertCaseView2);
                                dessertCaseView2.fillFreeList(500);
                            }
                        }, 250L);
                    }
                });
                float f = 0;
                this.hsv[0] = ((int) MotionController$$ExternalSyntheticOutline0.m(12, f, (float) Math.random(), f)) * 30.0f;
                imageView.setBackgroundColor(Color.HSVToColor(this.hsv));
                float random = (float) Math.random();
                if (random < 5.0E-4f) {
                    drawable = this.mDrawables.get(XXRARE_PASTRIES[(int) (Math.random() * 3)]);
                } else if (random < 0.005f) {
                    drawable = this.mDrawables.get(XRARE_PASTRIES[(int) (Math.random() * 4)]);
                } else if (random < 0.5f) {
                    drawable = this.mDrawables.get(RARE_PASTRIES[(int) (Math.random() * 8)]);
                } else if (random < 0.7f) {
                    drawable = this.mDrawables.get(PASTRIES[(int) (Math.random() * 2)]);
                } else {
                    drawable = null;
                }
                if (drawable != null) {
                    imageView.getOverlay().add(drawable);
                }
                int i3 = this.mCellSize;
                layoutParams.height = i3;
                layoutParams.width = i3;
                addView(imageView, layoutParams);
                place(imageView, point, false);
                if (i > 0) {
                    float intValue = ((Integer) imageView.getTag(33554434)).intValue();
                    float f2 = 0.5f * intValue;
                    imageView.setScaleX(f2);
                    imageView.setScaleY(f2);
                    imageView.setAlpha(0.0f);
                    imageView.animate().withLayer().scaleX(intValue).scaleY(intValue).alpha(1.0f).setDuration(i);
                }
            }
        }
    }

    @Override // android.view.View
    public final synchronized void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!(this.mWidth == i && this.mHeight == i2)) {
            boolean z = this.mStarted;
            if (z) {
                this.mStarted = false;
                this.mHandler.removeCallbacks(this.mJuggle);
            }
            this.mWidth = i;
            this.mHeight = i2;
            this.mCells = null;
            removeAllViewsInLayout();
            this.mFreeList.clear();
            int i5 = this.mHeight;
            int i6 = this.mCellSize;
            int i7 = i5 / i6;
            this.mRows = i7;
            int i8 = this.mWidth / i6;
            this.mColumns = i8;
            this.mCells = new View[i7 * i8];
            setScaleX(0.25f);
            setScaleY(0.25f);
            setTranslationX((this.mWidth - (this.mCellSize * this.mColumns)) * 0.5f * 0.25f);
            setTranslationY((this.mHeight - (this.mCellSize * this.mRows)) * 0.5f * 0.25f);
            for (int i9 = 0; i9 < this.mRows; i9++) {
                for (int i10 = 0; i10 < this.mColumns; i10++) {
                    this.mFreeList.add(new Point(i10, i9));
                }
            }
            if (z) {
                start();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class RescalingContainer extends FrameLayout {
        public DessertCaseView mView;

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
            float f = i3 - i;
            float f2 = i4 - i2;
            int i5 = (int) ((f / 0.25f) / 2.0f);
            int i6 = (int) ((f2 / 0.25f) / 2.0f);
            int i7 = (int) ((f * 0.5f) + i);
            int i8 = (int) ((f2 * 0.5f) + i2);
            this.mView.layout(i7 - i5, i8 - i6, i7 + i5, i8 + i6);
        }

        public RescalingContainer(Context context) {
            super(context);
            setSystemUiVisibility(5638);
        }
    }

    static {
        int[] iArr = {2131231694, 2131231680};
        PASTRIES = iArr;
        int[] iArr2 = {2131231681, 2131231683, 2131231685, 2131231687, 2131231688, 2131231689, 2131231690, 2131231692};
        RARE_PASTRIES = iArr2;
        int[] iArr3 = {2131231695, 2131231684, 2131231686, 2131231693};
        XRARE_PASTRIES = iArr3;
        int[] iArr4 = {2131231696, 2131231682, 2131231691};
        XXRARE_PASTRIES = iArr4;
        NUM_PASTRIES = iArr.length + iArr2.length + iArr3.length + iArr4.length;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.DessertCaseView$1] */
    public DessertCaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        int i = 0;
        Resources resources = getResources();
        this.mCellSize = resources.getDimensionPixelSize(2131165655);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (this.mCellSize < 512) {
            options.inSampleSize = 2;
        }
        options.inMutable = true;
        Bitmap bitmap = null;
        int[][] iArr = {PASTRIES, RARE_PASTRIES, XRARE_PASTRIES, XXRARE_PASTRIES};
        int i2 = 0;
        for (int i3 = 4; i2 < i3; i3 = 4) {
            int[] iArr2 = iArr[i2];
            int length = iArr2.length;
            int i4 = i;
            while (i4 < length) {
                int i5 = iArr2[i4];
                options.inBitmap = bitmap;
                bitmap = BitmapFactory.decodeResource(resources, i5, options);
                Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(MASK));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, createBitmap);
                bitmapDrawable.setColorFilter(new ColorMatrixColorFilter(ALPHA_MASK));
                int i6 = this.mCellSize;
                bitmapDrawable.setBounds(0, 0, i6, i6);
                this.mDrawables.append(i5, bitmapDrawable);
                i4++;
                i = 0;
            }
            i2++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x008e A[Catch: all -> 0x01ff, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x001a, B:8:0x0022, B:11:0x0042, B:13:0x0047, B:18:0x0055, B:20:0x005a, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a6, B:38:0x00ac, B:40:0x00b2, B:42:0x00c0, B:44:0x00d8, B:46:0x00e0, B:47:0x0113, B:48:0x0117, B:50:0x011b, B:51:0x0131, B:54:0x0145, B:55:0x01db), top: B:60:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b2 A[Catch: all -> 0x01ff, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x001a, B:8:0x0022, B:11:0x0042, B:13:0x0047, B:18:0x0055, B:20:0x005a, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a6, B:38:0x00ac, B:40:0x00b2, B:42:0x00c0, B:44:0x00d8, B:46:0x00e0, B:47:0x0113, B:48:0x0117, B:50:0x011b, B:51:0x0131, B:54:0x0145, B:55:0x01db), top: B:60:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x011b A[Catch: all -> 0x01ff, LOOP:4: B:49:0x0119->B:50:0x011b, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x001a, B:8:0x0022, B:11:0x0042, B:13:0x0047, B:18:0x0055, B:20:0x005a, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a6, B:38:0x00ac, B:40:0x00b2, B:42:0x00c0, B:44:0x00d8, B:46:0x00e0, B:47:0x0113, B:48:0x0117, B:50:0x011b, B:51:0x0131, B:54:0x0145, B:55:0x01db), top: B:60:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0145 A[Catch: all -> 0x01ff, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x001a, B:8:0x0022, B:11:0x0042, B:13:0x0047, B:18:0x0055, B:20:0x005a, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a6, B:38:0x00ac, B:40:0x00b2, B:42:0x00c0, B:44:0x00d8, B:46:0x00e0, B:47:0x0113, B:48:0x0117, B:50:0x011b, B:51:0x0131, B:54:0x0145, B:55:0x01db), top: B:60:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01db A[Catch: all -> 0x01ff, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x001a, B:8:0x0022, B:11:0x0042, B:13:0x0047, B:18:0x0055, B:20:0x005a, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a6, B:38:0x00ac, B:40:0x00b2, B:42:0x00c0, B:44:0x00d8, B:46:0x00e0, B:47:0x0113, B:48:0x0117, B:50:0x011b, B:51:0x0131, B:54:0x0145, B:55:0x01db), top: B:60:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized void place(final android.view.View r17, android.graphics.Point r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 514
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.DessertCaseView.place(android.view.View, android.graphics.Point, boolean):void");
    }

    public final void start() {
        if (!this.mStarted) {
            this.mStarted = true;
            fillFreeList(2000);
        }
        this.mHandler.postDelayed(this.mJuggle, 5000L);
    }

    public static Point[] getOccupied(View view) {
        int intValue = ((Integer) view.getTag(33554434)).intValue();
        Point point = (Point) view.getTag(33554433);
        if (point == null || intValue == 0) {
            return new Point[0];
        }
        Point[] pointArr = new Point[intValue * intValue];
        int i = 0;
        for (int i2 = 0; i2 < intValue; i2++) {
            int i3 = 0;
            while (i3 < intValue) {
                pointArr[i] = new Point(point.x + i2, point.y + i3);
                i3++;
                i++;
            }
        }
        return pointArr;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
