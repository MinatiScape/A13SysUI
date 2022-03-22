package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.leanback.R$drawable;
import androidx.leanback.R$fraction;
import com.google.android.material.shadow.ShadowRenderer;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapePath;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ShapeAppearancePathProvider {
    public final ShapePath[] cornerPaths = new ShapePath[4];
    public final Matrix[] cornerTransforms = new Matrix[4];
    public final Matrix[] edgeTransforms = new Matrix[4];
    public final PointF pointF = new PointF();
    public final Path overlappedEdgePath = new Path();
    public final Path boundsPath = new Path();
    public final ShapePath shapePath = new ShapePath();
    public final float[] scratch = new float[2];
    public final float[] scratch2 = new float[2];
    public final Path edgePath = new Path();
    public final Path cornerPath = new Path();
    public boolean edgeIntersectionCheckEnabled = true;

    /* loaded from: classes.dex */
    public static class Lazy {
        public static final ShapeAppearancePathProvider INSTANCE = new ShapeAppearancePathProvider();
    }

    public final void calculatePath(ShapeAppearanceModel shapeAppearanceModel, float f, RectF rectF, MaterialShapeDrawable.AnonymousClass1 r22, Path path) {
        int i;
        float[] fArr;
        float f2;
        R$fraction r$fraction;
        CornerSize cornerSize;
        R$drawable r$drawable;
        path.rewind();
        this.overlappedEdgePath.rewind();
        this.boundsPath.rewind();
        this.boundsPath.addRect(rectF, Path.Direction.CW);
        int i2 = 0;
        while (true) {
            if (i2 >= 4) {
                break;
            }
            if (i2 == 1) {
                Objects.requireNonNull(shapeAppearanceModel);
                cornerSize = shapeAppearanceModel.bottomRightCornerSize;
            } else if (i2 == 2) {
                Objects.requireNonNull(shapeAppearanceModel);
                cornerSize = shapeAppearanceModel.bottomLeftCornerSize;
            } else if (i2 != 3) {
                Objects.requireNonNull(shapeAppearanceModel);
                cornerSize = shapeAppearanceModel.topRightCornerSize;
            } else {
                Objects.requireNonNull(shapeAppearanceModel);
                cornerSize = shapeAppearanceModel.topLeftCornerSize;
            }
            if (i2 == 1) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$drawable = shapeAppearanceModel.bottomRightCorner;
            } else if (i2 == 2) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$drawable = shapeAppearanceModel.bottomLeftCorner;
            } else if (i2 != 3) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$drawable = shapeAppearanceModel.topRightCorner;
            } else {
                Objects.requireNonNull(shapeAppearanceModel);
                r$drawable = shapeAppearanceModel.topLeftCorner;
            }
            ShapePath shapePath = this.cornerPaths[i2];
            Objects.requireNonNull(r$drawable);
            r$drawable.getCornerPath(shapePath, f, cornerSize.getCornerSize(rectF));
            int i3 = i2 + 1;
            float f3 = i3 * 90;
            this.cornerTransforms[i2].reset();
            PointF pointF = this.pointF;
            if (i2 == 1) {
                pointF.set(rectF.right, rectF.bottom);
            } else if (i2 == 2) {
                pointF.set(rectF.left, rectF.bottom);
            } else if (i2 != 3) {
                pointF.set(rectF.right, rectF.top);
            } else {
                pointF.set(rectF.left, rectF.top);
            }
            Matrix matrix = this.cornerTransforms[i2];
            PointF pointF2 = this.pointF;
            matrix.setTranslate(pointF2.x, pointF2.y);
            this.cornerTransforms[i2].preRotate(f3);
            float[] fArr2 = this.scratch;
            ShapePath shapePath2 = this.cornerPaths[i2];
            Objects.requireNonNull(shapePath2);
            fArr2[0] = shapePath2.endX;
            float[] fArr3 = this.scratch;
            ShapePath shapePath3 = this.cornerPaths[i2];
            Objects.requireNonNull(shapePath3);
            fArr3[1] = shapePath3.endY;
            this.cornerTransforms[i2].mapPoints(this.scratch);
            this.edgeTransforms[i2].reset();
            Matrix matrix2 = this.edgeTransforms[i2];
            float[] fArr4 = this.scratch;
            matrix2.setTranslate(fArr4[0], fArr4[1]);
            this.edgeTransforms[i2].preRotate(f3);
            i2 = i3;
        }
        int i4 = 0;
        for (i = 4; i4 < i; i = 4) {
            float[] fArr5 = this.scratch;
            ShapePath shapePath4 = this.cornerPaths[i4];
            Objects.requireNonNull(shapePath4);
            fArr5[0] = shapePath4.startX;
            float[] fArr6 = this.scratch;
            ShapePath shapePath5 = this.cornerPaths[i4];
            Objects.requireNonNull(shapePath5);
            fArr6[1] = shapePath5.startY;
            this.cornerTransforms[i4].mapPoints(this.scratch);
            if (i4 == 0) {
                float[] fArr7 = this.scratch;
                path.moveTo(fArr7[0], fArr7[1]);
            } else {
                float[] fArr8 = this.scratch;
                path.lineTo(fArr8[0], fArr8[1]);
            }
            this.cornerPaths[i4].applyToPath(this.cornerTransforms[i4], path);
            if (r22 != null) {
                ShapePath shapePath6 = this.cornerPaths[i4];
                Matrix matrix3 = this.cornerTransforms[i4];
                BitSet bitSet = MaterialShapeDrawable.this.containsIncompatibleShadowOp;
                Objects.requireNonNull(shapePath6);
                bitSet.set(i4, false);
                ShapePath.ShadowCompatOperation[] shadowCompatOperationArr = MaterialShapeDrawable.this.cornerShadowOperation;
                shapePath6.addConnectingShadowIfNecessary(shapePath6.endShadowAngle);
                final Matrix matrix4 = new Matrix(matrix3);
                final ArrayList arrayList = new ArrayList(shapePath6.shadowCompatOperations);
                shadowCompatOperationArr[i4] = new ShapePath.ShadowCompatOperation() { // from class: com.google.android.material.shape.ShapePath.1
                    @Override // com.google.android.material.shape.ShapePath.ShadowCompatOperation
                    public final void draw(Matrix matrix5, ShadowRenderer shadowRenderer, int i5, Canvas canvas) {
                        for (ShadowCompatOperation shadowCompatOperation : arrayList) {
                            shadowCompatOperation.draw(matrix4, shadowRenderer, i5, canvas);
                        }
                    }
                };
            }
            int i5 = i4 + 1;
            int i6 = i5 % 4;
            float[] fArr9 = this.scratch;
            ShapePath shapePath7 = this.cornerPaths[i4];
            Objects.requireNonNull(shapePath7);
            fArr9[0] = shapePath7.endX;
            float[] fArr10 = this.scratch;
            ShapePath shapePath8 = this.cornerPaths[i4];
            Objects.requireNonNull(shapePath8);
            fArr10[1] = shapePath8.endY;
            this.cornerTransforms[i4].mapPoints(this.scratch);
            float[] fArr11 = this.scratch2;
            ShapePath shapePath9 = this.cornerPaths[i6];
            Objects.requireNonNull(shapePath9);
            fArr11[0] = shapePath9.startX;
            float[] fArr12 = this.scratch2;
            ShapePath shapePath10 = this.cornerPaths[i6];
            Objects.requireNonNull(shapePath10);
            fArr12[1] = shapePath10.startY;
            this.cornerTransforms[i6].mapPoints(this.scratch2);
            float f4 = this.scratch[0];
            float[] fArr13 = this.scratch2;
            float max = Math.max(((float) Math.hypot(f4 - fArr13[0], fArr[1] - fArr13[1])) - 0.001f, 0.0f);
            float[] fArr14 = this.scratch;
            ShapePath[] shapePathArr = this.cornerPaths;
            fArr14[0] = shapePathArr[i4].endX;
            fArr14[1] = shapePathArr[i4].endY;
            this.cornerTransforms[i4].mapPoints(fArr14);
            if (i4 == 1 || i4 == 3) {
                f2 = Math.abs(rectF.centerX() - this.scratch[0]);
            } else {
                f2 = Math.abs(rectF.centerY() - this.scratch[1]);
            }
            ShapePath shapePath11 = this.shapePath;
            Objects.requireNonNull(shapePath11);
            shapePath11.reset(0.0f, 0.0f, 270.0f, 0.0f);
            if (i4 == 1) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$fraction = shapeAppearanceModel.bottomEdge;
            } else if (i4 == 2) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$fraction = shapeAppearanceModel.leftEdge;
            } else if (i4 != 3) {
                Objects.requireNonNull(shapeAppearanceModel);
                r$fraction = shapeAppearanceModel.rightEdge;
            } else {
                Objects.requireNonNull(shapeAppearanceModel);
                r$fraction = shapeAppearanceModel.topEdge;
            }
            r$fraction.getEdgePath(max, f2, f, this.shapePath);
            this.edgePath.reset();
            this.shapePath.applyToPath(this.edgeTransforms[i4], this.edgePath);
            if (!this.edgeIntersectionCheckEnabled || (!r$fraction.forceIntersection() && !pathOverlapsCorner(this.edgePath, i4) && !pathOverlapsCorner(this.edgePath, i6))) {
                this.shapePath.applyToPath(this.edgeTransforms[i4], path);
            } else {
                Path path2 = this.edgePath;
                path2.op(path2, this.boundsPath, Path.Op.DIFFERENCE);
                float[] fArr15 = this.scratch;
                ShapePath shapePath12 = this.shapePath;
                Objects.requireNonNull(shapePath12);
                fArr15[0] = shapePath12.startX;
                float[] fArr16 = this.scratch;
                ShapePath shapePath13 = this.shapePath;
                Objects.requireNonNull(shapePath13);
                fArr16[1] = shapePath13.startY;
                this.edgeTransforms[i4].mapPoints(this.scratch);
                Path path3 = this.overlappedEdgePath;
                float[] fArr17 = this.scratch;
                path3.moveTo(fArr17[0], fArr17[1]);
                this.shapePath.applyToPath(this.edgeTransforms[i4], this.overlappedEdgePath);
            }
            if (r22 != null) {
                ShapePath shapePath14 = this.shapePath;
                Matrix matrix5 = this.edgeTransforms[i4];
                Objects.requireNonNull(shapePath14);
                MaterialShapeDrawable.this.containsIncompatibleShadowOp.set(i4 + 4, false);
                ShapePath.ShadowCompatOperation[] shadowCompatOperationArr2 = MaterialShapeDrawable.this.edgeShadowOperation;
                shapePath14.addConnectingShadowIfNecessary(shapePath14.endShadowAngle);
                final Matrix matrix6 = new Matrix(matrix5);
                final ArrayList arrayList2 = new ArrayList(shapePath14.shadowCompatOperations);
                shadowCompatOperationArr2[i4] = new ShapePath.ShadowCompatOperation() { // from class: com.google.android.material.shape.ShapePath.1
                    @Override // com.google.android.material.shape.ShapePath.ShadowCompatOperation
                    public final void draw(Matrix matrix52, ShadowRenderer shadowRenderer, int i52, Canvas canvas) {
                        for (ShadowCompatOperation shadowCompatOperation : arrayList2) {
                            shadowCompatOperation.draw(matrix6, shadowRenderer, i52, canvas);
                        }
                    }
                };
            }
            i4 = i5;
        }
        path.close();
        this.overlappedEdgePath.close();
        if (!this.overlappedEdgePath.isEmpty()) {
            path.op(this.overlappedEdgePath, Path.Op.UNION);
        }
    }

    public final boolean pathOverlapsCorner(Path path, int i) {
        this.cornerPath.reset();
        this.cornerPaths[i].applyToPath(this.cornerTransforms[i], this.cornerPath);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        this.cornerPath.computeBounds(rectF, true);
        path.op(this.cornerPath, Path.Op.INTERSECT);
        path.computeBounds(rectF, true);
        if (!rectF.isEmpty()) {
            return true;
        }
        if (rectF.width() <= 1.0f || rectF.height() <= 1.0f) {
            return false;
        }
        return true;
    }

    public ShapeAppearancePathProvider() {
        for (int i = 0; i < 4; i++) {
            this.cornerPaths[i] = new ShapePath();
            this.cornerTransforms[i] = new Matrix();
            this.edgeTransforms[i] = new Matrix();
        }
    }
}
