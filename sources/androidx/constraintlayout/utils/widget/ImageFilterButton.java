package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.R$styleable;
/* loaded from: classes.dex */
public class ImageFilterButton extends AppCompatImageButton {
    public float mCrossfade;
    public ImageFilterView.ImageMatrix mImageMatrix = new ImageFilterView.ImageMatrix();
    public LayerDrawable mLayer;
    public Drawable[] mLayers;
    public boolean mOverlay;
    public Path mPath;
    public RectF mRect;
    public float mRound;
    public float mRoundPercent;
    public ViewOutlineProvider mViewOutlineProvider;

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
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterButton.1
                    @Override // android.view.ViewOutlineProvider
                    public final void getOutline(View view, Outline outline) {
                        int width = ImageFilterButton.this.getWidth();
                        int height = ImageFilterButton.this.getHeight();
                        outline.setRoundRect(0, 0, width, height, (Math.min(width, height) * ImageFilterButton.this.mRoundPercent) / 2.0f);
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

    public ImageFilterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean z;
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.mOverlay = true;
        setPadding(0, 0, 0, 0);
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
                    ImageFilterView.ImageMatrix imageMatrix = this.mImageMatrix;
                    imageMatrix.mWarmth = f;
                    imageMatrix.updateMatrix(this);
                } else if (index == 7) {
                    float f2 = obtainStyledAttributes.getFloat(index, 0.0f);
                    ImageFilterView.ImageMatrix imageMatrix2 = this.mImageMatrix;
                    imageMatrix2.mSaturation = f2;
                    imageMatrix2.updateMatrix(this);
                } else if (index == 2) {
                    float f3 = obtainStyledAttributes.getFloat(index, 0.0f);
                    ImageFilterView.ImageMatrix imageMatrix3 = this.mImageMatrix;
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
                                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterButton.2
                                    @Override // android.view.ViewOutlineProvider
                                    public final void getOutline(View view, Outline outline) {
                                        outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), ImageFilterButton.this.mRound);
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
