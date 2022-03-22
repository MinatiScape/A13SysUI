package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class PathParser {

    /* loaded from: classes.dex */
    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        public static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean z, boolean z2) {
            double d;
            double d2;
            boolean z3;
            double radians = Math.toRadians(f7);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            double d3 = f;
            double d4 = f2;
            double d5 = (d4 * sin) + (d3 * cos);
            double d6 = d3;
            double d7 = f5;
            double d8 = d5 / d7;
            double d9 = f6;
            double d10 = ((d4 * cos) + ((-f) * sin)) / d9;
            double d11 = d4;
            double d12 = f4;
            double d13 = ((d12 * sin) + (f3 * cos)) / d7;
            double d14 = ((d12 * cos) + ((-f3) * sin)) / d9;
            double d15 = d8 - d13;
            double d16 = d10 - d14;
            double d17 = (d8 + d13) / 2.0d;
            double d18 = (d10 + d14) / 2.0d;
            double d19 = (d16 * d16) + (d15 * d15);
            if (d19 == 0.0d) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            double d20 = (1.0d / d19) - 0.25d;
            if (d20 < 0.0d) {
                Log.w("PathParser", "Points are too far apart " + d19);
                float sqrt = (float) (Math.sqrt(d19) / 1.99999d);
                drawArc(path, f, f2, f3, f4, f5 * sqrt, f6 * sqrt, f7, z, z2);
                return;
            }
            double sqrt2 = Math.sqrt(d20);
            double d21 = d15 * sqrt2;
            double d22 = sqrt2 * d16;
            if (z == z2) {
                d2 = d17 - d22;
                d = d18 + d21;
            } else {
                d2 = d17 + d22;
                d = d18 - d21;
            }
            double atan2 = Math.atan2(d10 - d, d8 - d2);
            double atan22 = Math.atan2(d14 - d, d13 - d2) - atan2;
            int i = (atan22 > 0.0d ? 1 : (atan22 == 0.0d ? 0 : -1));
            if (i >= 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z2 != z3) {
                if (i > 0) {
                    atan22 -= 6.283185307179586d;
                } else {
                    atan22 += 6.283185307179586d;
                }
            }
            double d23 = d2 * d7;
            double d24 = d * d9;
            double d25 = (d23 * cos) - (d24 * sin);
            double d26 = (d24 * cos) + (d23 * sin);
            int ceil = (int) Math.ceil(Math.abs((atan22 * 4.0d) / 3.141592653589793d));
            double cos2 = Math.cos(radians);
            double sin2 = Math.sin(radians);
            double cos3 = Math.cos(atan2);
            double sin3 = Math.sin(atan2);
            double d27 = -d7;
            double d28 = d27 * cos2;
            double d29 = d9 * sin2;
            double d30 = (d28 * sin3) - (d29 * cos3);
            double d31 = d27 * sin2;
            double d32 = d9 * cos2;
            double d33 = (cos3 * d32) + (sin3 * d31);
            double d34 = d32;
            double d35 = atan22 / ceil;
            int i2 = 0;
            while (i2 < ceil) {
                double d36 = atan2 + d35;
                double sin4 = Math.sin(d36);
                double cos4 = Math.cos(d36);
                double d37 = (((d7 * cos2) * cos4) + d25) - (d29 * sin4);
                double d38 = (d34 * sin4) + (d7 * sin2 * cos4) + d26;
                double d39 = (d28 * sin4) - (d29 * cos4);
                double d40 = (cos4 * d34) + (sin4 * d31);
                double d41 = d36 - atan2;
                double tan = Math.tan(d41 / 2.0d);
                double sqrt3 = ((Math.sqrt(((tan * 3.0d) * tan) + 4.0d) - 1.0d) * Math.sin(d41)) / 3.0d;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float) ((d30 * sqrt3) + d6), (float) ((d33 * sqrt3) + d11), (float) (d37 - (sqrt3 * d39)), (float) (d38 - (sqrt3 * d40)), (float) d37, (float) d38);
                i2++;
                atan2 = d36;
                d31 = d31;
                cos2 = cos2;
                ceil = ceil;
                d33 = d40;
                d7 = d7;
                d30 = d39;
                d6 = d37;
                d11 = d38;
                d25 = d25;
                d35 = d35;
                d34 = d34;
            }
        }

        public static void nodesToPath(PathDataNode[] pathDataNodeArr, Path path) {
            int i;
            int i2;
            int i3;
            float[] fArr;
            char c;
            boolean z;
            boolean z2;
            float f;
            float f2;
            boolean z3;
            boolean z4;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11;
            float f12;
            float f13;
            float f14;
            float f15;
            float f16;
            float f17;
            float f18;
            float f19;
            PathDataNode[] pathDataNodeArr2 = pathDataNodeArr;
            int i4 = 6;
            float[] fArr2 = new float[6];
            char c2 = 'm';
            int i5 = 0;
            char c3 = 'm';
            int i6 = 0;
            while (i6 < pathDataNodeArr2.length) {
                char c4 = pathDataNodeArr2[i6].mType;
                float[] fArr3 = pathDataNodeArr2[i6].mParams;
                float f20 = fArr2[i5];
                float f21 = fArr2[1];
                float f22 = fArr2[2];
                float f23 = fArr2[3];
                float f24 = fArr2[4];
                float f25 = fArr2[5];
                switch (c4) {
                    case 'A':
                    case 'a':
                        i = 7;
                        break;
                    case 'C':
                    case 'c':
                        i = i4;
                        break;
                    case 'H':
                    case 'V':
                    case 'h':
                    case 'v':
                        i = 1;
                        break;
                    case 'Q':
                    case 'S':
                    case 'q':
                    case 's':
                        i = 4;
                        break;
                    case 'Z':
                    case 'z':
                        path.close();
                        path.moveTo(f24, f25);
                        f20 = f24;
                        f22 = f20;
                        f21 = f25;
                        f23 = f21;
                    default:
                        i = 2;
                        break;
                }
                float f26 = f24;
                float f27 = f25;
                float f28 = f20;
                float f29 = f21;
                int i7 = i5;
                while (i7 < fArr3.length) {
                    if (c4 != 'A') {
                        if (c4 != 'C') {
                            if (c4 == 'H') {
                                i2 = i7;
                                fArr = fArr3;
                                c = c4;
                                i3 = i6;
                                int i8 = i2 + 0;
                                path.lineTo(fArr[i8], f29);
                                f28 = fArr[i8];
                            } else if (c4 == 'Q') {
                                i2 = i7;
                                fArr = fArr3;
                                c = c4;
                                i3 = i6;
                                int i9 = i2 + 0;
                                int i10 = i2 + 1;
                                int i11 = i2 + 2;
                                int i12 = i2 + 3;
                                path.quadTo(fArr[i9], fArr[i10], fArr[i11], fArr[i12]);
                                f2 = fArr[i9];
                                f = fArr[i10];
                                f28 = fArr[i11];
                                f29 = fArr[i12];
                            } else if (c4 == 'V') {
                                i2 = i7;
                                fArr = fArr3;
                                c = c4;
                                i3 = i6;
                                int i13 = i2 + 0;
                                path.lineTo(f28, fArr[i13]);
                                f29 = fArr[i13];
                            } else if (c4 != 'a') {
                                if (c4 != 'c') {
                                    if (c4 == 'h') {
                                        i2 = i7;
                                        int i14 = i2 + 0;
                                        path.rLineTo(fArr3[i14], 0.0f);
                                        f28 += fArr3[i14];
                                    } else if (c4 != 'q') {
                                        if (c4 == 'v') {
                                            i2 = i7;
                                            f10 = f29;
                                            int i15 = i2 + 0;
                                            path.rLineTo(0.0f, fArr3[i15]);
                                            f11 = fArr3[i15];
                                        } else if (c4 == 'L') {
                                            i2 = i7;
                                            int i16 = i2 + 0;
                                            int i17 = i2 + 1;
                                            path.lineTo(fArr3[i16], fArr3[i17]);
                                            f28 = fArr3[i16];
                                            f29 = fArr3[i17];
                                        } else if (c4 == 'M') {
                                            i2 = i7;
                                            int i18 = i2 + 0;
                                            float f30 = fArr3[i18];
                                            int i19 = i2 + 1;
                                            float f31 = fArr3[i19];
                                            if (i2 > 0) {
                                                path.lineTo(fArr3[i18], fArr3[i19]);
                                                f28 = f30;
                                                f29 = f31;
                                            } else {
                                                path.moveTo(fArr3[i18], fArr3[i19]);
                                                f26 = f30;
                                                f27 = f31;
                                                f28 = f26;
                                                f29 = f27;
                                            }
                                        } else if (c4 == 'S') {
                                            i2 = i7;
                                            if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                                                f12 = (f29 * 2.0f) - f23;
                                                f13 = (f28 * 2.0f) - f22;
                                            } else {
                                                f13 = f28;
                                                f12 = f29;
                                            }
                                            int i20 = i2 + 0;
                                            int i21 = i2 + 1;
                                            int i22 = i2 + 2;
                                            int i23 = i2 + 3;
                                            path.cubicTo(f13, f12, fArr3[i20], fArr3[i21], fArr3[i22], fArr3[i23]);
                                            float f32 = fArr3[i20];
                                            float f33 = fArr3[i21];
                                            f4 = fArr3[i22];
                                            f3 = fArr3[i23];
                                            f22 = f32;
                                            f23 = f33;
                                            f28 = f4;
                                            f29 = f3;
                                        } else if (c4 == 'T') {
                                            i2 = i7;
                                            if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                                f14 = (f28 * 2.0f) - f22;
                                                f15 = (f29 * 2.0f) - f23;
                                            } else {
                                                f14 = f28;
                                                f15 = f29;
                                            }
                                            int i24 = i2 + 0;
                                            int i25 = i2 + 1;
                                            path.quadTo(f14, f15, fArr3[i24], fArr3[i25]);
                                            f23 = f15;
                                            f22 = f14;
                                            fArr = fArr3;
                                            c = c4;
                                            i3 = i6;
                                            f28 = fArr3[i24];
                                            f29 = fArr3[i25];
                                        } else if (c4 == 'l') {
                                            i2 = i7;
                                            f10 = f29;
                                            int i26 = i2 + 0;
                                            int i27 = i2 + 1;
                                            path.rLineTo(fArr3[i26], fArr3[i27]);
                                            f28 += fArr3[i26];
                                            f11 = fArr3[i27];
                                        } else if (c4 == c2) {
                                            i2 = i7;
                                            int i28 = i2 + 0;
                                            f28 += fArr3[i28];
                                            int i29 = i2 + 1;
                                            f29 += fArr3[i29];
                                            if (i2 > 0) {
                                                path.rLineTo(fArr3[i28], fArr3[i29]);
                                            } else {
                                                path.rMoveTo(fArr3[i28], fArr3[i29]);
                                                f27 = f29;
                                                f26 = f28;
                                                f28 = f26;
                                                f29 = f27;
                                            }
                                        } else if (c4 != 's') {
                                            if (c4 == 't') {
                                                if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                                    f18 = f28 - f22;
                                                    f19 = f29 - f23;
                                                } else {
                                                    f19 = 0.0f;
                                                    f18 = 0.0f;
                                                }
                                                int i30 = i7 + 0;
                                                int i31 = i7 + 1;
                                                path.rQuadTo(f18, f19, fArr3[i30], fArr3[i31]);
                                                float f34 = f18 + f28;
                                                float f35 = f19 + f29;
                                                f28 += fArr3[i30];
                                                f29 += fArr3[i31];
                                                f23 = f35;
                                                f22 = f34;
                                            }
                                            i2 = i7;
                                        } else {
                                            if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                                                f16 = f29 - f23;
                                                f17 = f28 - f22;
                                            } else {
                                                f17 = 0.0f;
                                                f16 = 0.0f;
                                            }
                                            int i32 = i7 + 0;
                                            int i33 = i7 + 1;
                                            int i34 = i7 + 2;
                                            int i35 = i7 + 3;
                                            i2 = i7;
                                            f5 = f29;
                                            path.rCubicTo(f17, f16, fArr3[i32], fArr3[i33], fArr3[i34], fArr3[i35]);
                                            f8 = fArr3[i32] + f28;
                                            f7 = fArr3[i33] + f5;
                                            f6 = f28 + fArr3[i34];
                                            f9 = fArr3[i35];
                                        }
                                        f29 = f10 + f11;
                                    } else {
                                        i2 = i7;
                                        f5 = f29;
                                        int i36 = i2 + 0;
                                        int i37 = i2 + 1;
                                        int i38 = i2 + 2;
                                        int i39 = i2 + 3;
                                        path.rQuadTo(fArr3[i36], fArr3[i37], fArr3[i38], fArr3[i39]);
                                        f8 = fArr3[i36] + f28;
                                        f7 = fArr3[i37] + f5;
                                        float f36 = f28 + fArr3[i38];
                                        float f37 = fArr3[i39];
                                        f6 = f36;
                                        f9 = f37;
                                    }
                                    fArr = fArr3;
                                    c = c4;
                                    i3 = i6;
                                } else {
                                    i2 = i7;
                                    f5 = f29;
                                    int i40 = i2 + 2;
                                    int i41 = i2 + 3;
                                    int i42 = i2 + 4;
                                    int i43 = i2 + 5;
                                    path.rCubicTo(fArr3[i2 + 0], fArr3[i2 + 1], fArr3[i40], fArr3[i41], fArr3[i42], fArr3[i43]);
                                    f8 = fArr3[i40] + f28;
                                    f7 = fArr3[i41] + f5;
                                    f6 = f28 + fArr3[i42];
                                    f9 = fArr3[i43];
                                }
                                f3 = f5 + f9;
                                f22 = f8;
                                f23 = f7;
                                f4 = f6;
                                f28 = f4;
                                f29 = f3;
                                fArr = fArr3;
                                c = c4;
                                i3 = i6;
                            } else {
                                i2 = i7;
                                int i44 = i2 + 5;
                                float f38 = fArr3[i44] + f28;
                                int i45 = i2 + 6;
                                float f39 = fArr3[i45] + f29;
                                float f40 = fArr3[i2 + 0];
                                float f41 = fArr3[i2 + 1];
                                float f42 = fArr3[i2 + 2];
                                if (fArr3[i2 + 3] != 0.0f) {
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                                if (fArr3[i2 + 4] != 0.0f) {
                                    z4 = true;
                                } else {
                                    z4 = false;
                                }
                                fArr = fArr3;
                                c = c4;
                                i3 = i6;
                                drawArc(path, f28, f29, f38, f39, f40, f41, f42, z3, z4);
                                f28 += fArr[i44];
                                f29 += fArr[i45];
                            }
                            i7 = i2 + i;
                            c3 = c;
                            c4 = c3;
                            fArr3 = fArr;
                            i6 = i3;
                            c2 = 'm';
                            i5 = 0;
                        } else {
                            i2 = i7;
                            fArr = fArr3;
                            c = c4;
                            i3 = i6;
                            int i46 = i2 + 2;
                            int i47 = i2 + 3;
                            int i48 = i2 + 4;
                            int i49 = i2 + 5;
                            path.cubicTo(fArr[i2 + 0], fArr[i2 + 1], fArr[i46], fArr[i47], fArr[i48], fArr[i49]);
                            float f43 = fArr[i48];
                            float f44 = fArr[i49];
                            f2 = fArr[i46];
                            f28 = f43;
                            f29 = f44;
                            f = fArr[i47];
                        }
                        f22 = f2;
                        f23 = f;
                        i7 = i2 + i;
                        c3 = c;
                        c4 = c3;
                        fArr3 = fArr;
                        i6 = i3;
                        c2 = 'm';
                        i5 = 0;
                    } else {
                        i2 = i7;
                        fArr = fArr3;
                        c = c4;
                        i3 = i6;
                        int i50 = i2 + 5;
                        float f45 = fArr[i50];
                        int i51 = i2 + 6;
                        float f46 = fArr[i51];
                        float f47 = fArr[i2 + 0];
                        float f48 = fArr[i2 + 1];
                        float f49 = fArr[i2 + 2];
                        if (fArr[i2 + 3] != 0.0f) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (fArr[i2 + 4] != 0.0f) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        drawArc(path, f28, f29, f45, f46, f47, f48, f49, z, z2);
                        f28 = fArr[i50];
                        f29 = fArr[i51];
                    }
                    f23 = f29;
                    f22 = f28;
                    i7 = i2 + i;
                    c3 = c;
                    c4 = c3;
                    fArr3 = fArr;
                    i6 = i3;
                    c2 = 'm';
                    i5 = 0;
                }
                fArr2[i5] = f28;
                fArr2[1] = f29;
                fArr2[2] = f22;
                fArr2[3] = f23;
                fArr2[4] = f26;
                fArr2[5] = f27;
                i6++;
                i4 = 6;
                c2 = 'm';
                c3 = pathDataNodeArr[i6].mType;
                pathDataNodeArr2 = pathDataNodeArr;
            }
        }

        public PathDataNode(char c, float[] fArr) {
            this.mType = c;
            this.mParams = fArr;
        }
    }

    public static float[] copyOfRange(float[] fArr, int i) {
        if (i >= 0) {
            int length = fArr.length;
            if (length >= 0) {
                int i2 = i - 0;
                int min = Math.min(i2, length - 0);
                float[] fArr2 = new float[i2];
                System.arraycopy(fArr, 0, fArr2, 0, min);
                return fArr2;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
        throw new IllegalArgumentException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x008a, code lost:
        if (r13 == 0) goto L_0x008c;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0097 A[Catch: NumberFormatException -> 0x00bc, LOOP:3: B:26:0x006c->B:46:0x0097, LOOP_END, TryCatch #0 {NumberFormatException -> 0x00bc, blocks: (B:23:0x0059, B:26:0x006c, B:28:0x0072, B:33:0x0080, B:46:0x0097, B:48:0x009c, B:51:0x00ac, B:53:0x00b1), top: B:68:0x0059 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x009c A[Catch: NumberFormatException -> 0x00bc, TryCatch #0 {NumberFormatException -> 0x00bc, blocks: (B:23:0x0059, B:26:0x006c, B:28:0x0072, B:33:0x0080, B:46:0x0097, B:48:0x009c, B:51:0x00ac, B:53:0x00b1), top: B:68:0x0059 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ac A[Catch: NumberFormatException -> 0x00bc, TryCatch #0 {NumberFormatException -> 0x00bc, blocks: (B:23:0x0059, B:26:0x006c, B:28:0x0072, B:33:0x0080, B:46:0x0097, B:48:0x009c, B:51:0x00ac, B:53:0x00b1), top: B:68:0x0059 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00d9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0096 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static androidx.core.graphics.PathParser.PathDataNode[] createNodesFromPathData(java.lang.String r17) {
        /*
            Method dump skipped, instructions count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.PathParser.createNodesFromPathData(java.lang.String):androidx.core.graphics.PathParser$PathDataNode[]");
    }

    public static Path createPathFromPathData(String str) {
        Path path = new Path();
        PathDataNode[] createNodesFromPathData = createNodesFromPathData(str);
        if (createNodesFromPathData == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(createNodesFromPathData, path);
            return path;
        } catch (RuntimeException e) {
            throw new RuntimeException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Error in parsing ", str), e);
        }
    }
}
