package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.R$styleable;
/* loaded from: classes.dex */
public class ImageFilterView extends AppCompatImageView {
    public float mCrossfade;
    public ImageMatrix mImageMatrix = new ImageMatrix();
    public LayerDrawable mLayer;
    public Drawable[] mLayers;
    public boolean mOverlay;
    public Path mPath;
    public RectF mRect;
    public float mRound;
    public float mRoundPercent;
    public ViewOutlineProvider mViewOutlineProvider;

    /* loaded from: classes.dex */
    public static class ImageMatrix {
        public float[] m = new float[20];
        public ColorMatrix mColorMatrix = new ColorMatrix();
        public ColorMatrix mTmpColorMatrix = new ColorMatrix();
        public float mBrightness = 1.0f;
        public float mSaturation = 1.0f;
        public float mContrast = 1.0f;
        public float mWarmth = 1.0f;

        public final void updateMatrix(ImageView imageView) {
            boolean z;
            float f;
            float f2;
            float f3;
            float f4;
            this.mColorMatrix.reset();
            float f5 = this.mSaturation;
            boolean z2 = true;
            if (f5 != 1.0f) {
                float f6 = 1.0f - f5;
                float f7 = 0.2999f * f6;
                float f8 = 0.587f * f6;
                float f9 = f6 * 0.114f;
                float[] fArr = this.m;
                fArr[0] = f7 + f5;
                fArr[1] = f8;
                fArr[2] = f9;
                fArr[3] = 0.0f;
                fArr[4] = 0.0f;
                fArr[5] = f7;
                fArr[6] = f8 + f5;
                fArr[7] = f9;
                fArr[8] = 0.0f;
                fArr[9] = 0.0f;
                fArr[10] = f7;
                fArr[11] = f8;
                fArr[12] = f9 + f5;
                fArr[13] = 0.0f;
                fArr[14] = 0.0f;
                fArr[15] = 0.0f;
                fArr[16] = 0.0f;
                fArr[17] = 0.0f;
                f = 1.0f;
                fArr[18] = 1.0f;
                fArr[19] = 0.0f;
                this.mColorMatrix.set(fArr);
                z = true;
            } else {
                f = 1.0f;
                z = false;
            }
            float f10 = this.mContrast;
            if (f10 != f) {
                this.mTmpColorMatrix.setScale(f10, f10, f10, f);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                z = true;
            }
            float f11 = this.mWarmth;
            if (f11 != f) {
                if (f11 <= 0.0f) {
                    f11 = 0.01f;
                }
                float f12 = (5000.0f / f11) / 100.0f;
                if (f12 > 66.0f) {
                    double d = f12 - 60.0f;
                    f2 = ((float) Math.pow(d, -0.13320475816726685d)) * 329.69873f;
                    f3 = ((float) Math.pow(d, 0.07551484555006027d)) * 288.12216f;
                } else {
                    f3 = (((float) Math.log(f12)) * 99.4708f) - 161.11957f;
                    f2 = 255.0f;
                }
                if (f12 >= 66.0f) {
                    f4 = 255.0f;
                } else if (f12 > 19.0f) {
                    f4 = (((float) Math.log(f12 - 10.0f)) * 138.51773f) - 305.0448f;
                } else {
                    f4 = 0.0f;
                }
                float min = Math.min(255.0f, Math.max(f2, 0.0f));
                float min2 = Math.min(255.0f, Math.max(f3, 0.0f));
                float min3 = Math.min(255.0f, Math.max(f4, 0.0f));
                float min4 = Math.min(255.0f, Math.max(255.0f, 0.0f));
                float min5 = Math.min(255.0f, Math.max((((float) Math.log(50.0f)) * 99.4708f) - 161.11957f, 0.0f));
                float min6 = min3 / Math.min(255.0f, Math.max((((float) Math.log(40.0f)) * 138.51773f) - 305.0448f, 0.0f));
                float[] fArr2 = this.m;
                fArr2[0] = min / min4;
                fArr2[1] = 0.0f;
                fArr2[2] = 0.0f;
                fArr2[3] = 0.0f;
                fArr2[4] = 0.0f;
                fArr2[5] = 0.0f;
                fArr2[6] = min2 / min5;
                fArr2[7] = 0.0f;
                fArr2[8] = 0.0f;
                fArr2[9] = 0.0f;
                fArr2[10] = 0.0f;
                fArr2[11] = 0.0f;
                fArr2[12] = min6;
                fArr2[13] = 0.0f;
                fArr2[14] = 0.0f;
                fArr2[15] = 0.0f;
                fArr2[16] = 0.0f;
                fArr2[17] = 0.0f;
                fArr2[18] = 1.0f;
                fArr2[19] = 0.0f;
                this.mTmpColorMatrix.set(fArr2);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                z = true;
            }
            float f13 = this.mBrightness;
            if (f13 != 1.0f) {
                float[] fArr3 = this.m;
                fArr3[0] = f13;
                fArr3[1] = 0.0f;
                fArr3[2] = 0.0f;
                fArr3[3] = 0.0f;
                fArr3[4] = 0.0f;
                fArr3[5] = 0.0f;
                fArr3[6] = f13;
                fArr3[7] = 0.0f;
                fArr3[8] = 0.0f;
                fArr3[9] = 0.0f;
                fArr3[10] = 0.0f;
                fArr3[11] = 0.0f;
                fArr3[12] = f13;
                fArr3[13] = 0.0f;
                fArr3[14] = 0.0f;
                fArr3[15] = 0.0f;
                fArr3[16] = 0.0f;
                fArr3[17] = 0.0f;
                fArr3[18] = 1.0f;
                fArr3[19] = 0.0f;
                this.mTmpColorMatrix.set(fArr3);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
            } else {
                z2 = z;
            }
            if (z2) {
                imageView.setColorFilter(new ColorMatrixColorFilter(this.mColorMatrix));
            } else {
                imageView.clearColorFilter();
            }
        }
    }

    public final void setRoundPercent(float f) {
        boolean z;
        if (this.mRoundPercent != f) {
            z = true;
        } else {
            z = false;
        }
        this.mRoundPercent = f;
        if (f != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (this.mViewOutlineProvider == null) {
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterView.1
                    @Override // android.view.ViewOutlineProvider
                    public final void getOutline(View view, Outline outline) {
                        int width = ImageFilterView.this.getWidth();
                        int height = ImageFilterView.this.getHeight();
                        outline.setRoundRect(0, 0, width, height, (Math.min(width, height) * ImageFilterView.this.mRoundPercent) / 2.0f);
                    }
                };
                this.mViewOutlineProvider = viewOutlineProvider;
                setOutlineProvider(viewOutlineProvider);
            }
            setClipToOutline(true);
            int width = getWidth();
            int height = getHeight();
            float min = (Math.min(width, height) * this.mRoundPercent) / 2.0f;
            this.mRect.set(0.0f, 0.0f, width, height);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, min, min, Path.Direction.CW);
        } else {
            setClipToOutline(false);
        }
        if (z) {
            invalidateOutline();
        }
    }

    public ImageFilterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean z;
        this.mOverlay = true;
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ImageFilterView);
            int indexCount = obtainStyledAttributes.getIndexCount();
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == 3) {
                    this.mCrossfade = obtainStyledAttributes.getFloat(index, 0.0f);
                } else if (index == 8) {
                    float f = obtainStyledAttributes.getFloat(index, 0.0f);
                    ImageMatrix imageMatrix = this.mImageMatrix;
                    imageMatrix.mWarmth = f;
                    imageMatrix.updateMatrix(this);
                } else if (index == 7) {
                    float f2 = obtainStyledAttributes.getFloat(index, 0.0f);
                    ImageMatrix imageMatrix2 = this.mImageMatrix;
                    imageMatrix2.mSaturation = f2;
                    imageMatrix2.updateMatrix(this);
                } else if (index == 2) {
                    float f3 = obtainStyledAttributes.getFloat(index, 0.0f);
                    ImageMatrix imageMatrix3 = this.mImageMatrix;
                    imageMatrix3.mContrast = f3;
                    imageMatrix3.updateMatrix(this);
                } else if (index == 5) {
                    float dimension = obtainStyledAttributes.getDimension(index, 0.0f);
                    if (Float.isNaN(dimension)) {
                        this.mRound = dimension;
                        float f4 = this.mRoundPercent;
                        this.mRoundPercent = -1.0f;
                        setRoundPercent(f4);
                    } else {
                        if (this.mRound != dimension) {
                            z = true;
                        } else {
                            z = false;
                        }
                        this.mRound = dimension;
                        if (dimension != 0.0f) {
                            if (this.mPath == null) {
                                this.mPath = new Path();
                            }
                            if (this.mRect == null) {
                                this.mRect = new RectF();
                            }
                            if (this.mViewOutlineProvider == null) {
                                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterView.2
                                    @Override // android.view.ViewOutlineProvider
                                    public final void getOutline(View view, Outline outline) {
                                        outline.setRoundRect(0, 0, ImageFilterView.this.getWidth(), ImageFilterView.this.getHeight(), ImageFilterView.this.mRound);
                                    }
                                };
                                this.mViewOutlineProvider = viewOutlineProvider;
                                setOutlineProvider(viewOutlineProvider);
                            }
                            setClipToOutline(true);
                            this.mRect.set(0.0f, 0.0f, getWidth(), getHeight());
                            this.mPath.reset();
                            Path path = this.mPath;
                            RectF rectF = this.mRect;
                            float f5 = this.mRound;
                            path.addRoundRect(rectF, f5, f5, Path.Direction.CW);
                        } else {
                            setClipToOutline(false);
                        }
                        if (z) {
                            invalidateOutline();
                        }
                    }
                } else if (index == 6) {
                    setRoundPercent(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == 4) {
                    this.mOverlay = obtainStyledAttributes.getBoolean(index, this.mOverlay);
                }
            }
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                Drawable[] drawableArr = new Drawable[2];
                this.mLayers = drawableArr;
                drawableArr[0] = getDrawable();
                this.mLayers[1] = drawable;
                LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
                this.mLayer = layerDrawable;
                layerDrawable.getDrawable(1).setAlpha((int) (this.mCrossfade * 255.0f));
                super.setImageDrawable(this.mLayer);
            }
        }
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
