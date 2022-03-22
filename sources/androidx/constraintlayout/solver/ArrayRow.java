package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.LinearSystem;
import java.util.Objects;
/* loaded from: classes.dex */
public class ArrayRow implements LinearSystem.Row {
    public final ArrayLinkedVariables variables;
    public SolverVariable variable = null;
    public float constantValue = 0.0f;
    public boolean isSimpleDefinition = false;

    public final ArrayRow createRowGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i) {
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = i;
        }
        if (!z) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
        } else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }

    public final ArrayRow createRowLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i) {
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = i;
        }
        if (!z) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        } else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        return this;
    }

    public final ArrayRow addError(LinearSystem linearSystem, int i) {
        this.variables.put(linearSystem.createErrorVariable(i), 1.0f);
        this.variables.put(linearSystem.createErrorVariable(i), -1.0f);
        return this;
    }

    @Override // androidx.constraintlayout.solver.LinearSystem.Row
    public SolverVariable getPivotCandidate(boolean[] zArr) {
        return this.variables.getPivotCandidate(zArr, null);
    }

    public final void pivot(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.variable;
        if (solverVariable2 != null) {
            this.variables.put(solverVariable2, -1.0f);
            this.variable = null;
        }
        float remove = this.variables.remove(solverVariable, true) * (-1.0f);
        this.variable = solverVariable;
        if (remove != 1.0f) {
            this.constantValue /= remove;
            ArrayLinkedVariables arrayLinkedVariables = this.variables;
            Objects.requireNonNull(arrayLinkedVariables);
            int i = arrayLinkedVariables.mHead;
            for (int i2 = 0; i != -1 && i2 < arrayLinkedVariables.currentSize; i2++) {
                float[] fArr = arrayLinkedVariables.mArrayValues;
                fArr[i] = fArr[i] / remove;
                i = arrayLinkedVariables.mArrayNextIndices[i];
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0095  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String toString() {
        /*
            r11 = this;
            androidx.constraintlayout.solver.SolverVariable r0 = r11.variable
            if (r0 != 0) goto L_0x0007
            java.lang.String r0 = "0"
            goto L_0x0016
        L_0x0007:
            java.lang.String r0 = ""
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            androidx.constraintlayout.solver.SolverVariable r1 = r11.variable
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L_0x0016:
            java.lang.String r1 = " = "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            float r1 = r11.constantValue
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L_0x0034
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            float r1 = r11.constantValue
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = r4
            goto L_0x0035
        L_0x0034:
            r1 = r3
        L_0x0035:
            androidx.constraintlayout.solver.ArrayLinkedVariables r5 = r11.variables
            int r5 = r5.currentSize
            r6 = r3
        L_0x003a:
            if (r6 >= r5) goto L_0x00b0
            androidx.constraintlayout.solver.ArrayLinkedVariables r7 = r11.variables
            androidx.constraintlayout.solver.SolverVariable r7 = r7.getVariable(r6)
            if (r7 != 0) goto L_0x0046
            goto L_0x00ad
        L_0x0046:
            androidx.constraintlayout.solver.ArrayLinkedVariables r7 = r11.variables
            java.util.Objects.requireNonNull(r7)
            int r8 = r7.mHead
            r9 = r3
        L_0x004e:
            r10 = -1
            if (r8 == r10) goto L_0x0063
            int r10 = r7.currentSize
            if (r9 >= r10) goto L_0x0063
            if (r9 != r6) goto L_0x005c
            float[] r7 = r7.mArrayValues
            r7 = r7[r8]
            goto L_0x0064
        L_0x005c:
            int[] r10 = r7.mArrayNextIndices
            r8 = r10[r8]
            int r9 = r9 + 1
            goto L_0x004e
        L_0x0063:
            r7 = r2
        L_0x0064:
            int r8 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r8 != 0) goto L_0x0069
            goto L_0x00ad
        L_0x0069:
            java.lang.String r9 = "null"
            r10 = -1082130432(0xffffffffbf800000, float:-1.0)
            if (r1 != 0) goto L_0x007a
            int r1 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r1 >= 0) goto L_0x008a
            java.lang.String r1 = "- "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            goto L_0x0089
        L_0x007a:
            if (r8 <= 0) goto L_0x0083
            java.lang.String r1 = " + "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            goto L_0x008a
        L_0x0083:
            java.lang.String r1 = " - "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
        L_0x0089:
            float r7 = r7 * r10
        L_0x008a:
            r1 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x0095
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r9)
            goto L_0x00ac
        L_0x0095:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r7)
            java.lang.String r0 = " "
            r1.append(r0)
            r1.append(r9)
            java.lang.String r0 = r1.toString()
        L_0x00ac:
            r1 = r4
        L_0x00ad:
            int r6 = r6 + 1
            goto L_0x003a
        L_0x00b0:
            if (r1 != 0) goto L_0x00b8
            java.lang.String r11 = "0.0"
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r11)
        L_0x00b8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.ArrayRow.toString():java.lang.String");
    }

    public void updateFromRow(ArrayRow arrayRow) {
        ArrayLinkedVariables arrayLinkedVariables = this.variables;
        Objects.requireNonNull(arrayLinkedVariables);
        int i = arrayLinkedVariables.mHead;
        while (true) {
            for (int i2 = 0; i != -1 && i2 < arrayLinkedVariables.currentSize; i2++) {
                int i3 = arrayLinkedVariables.mArrayIndices[i];
                SolverVariable solverVariable = arrayRow.variable;
                if (i3 == solverVariable.id) {
                    float f = arrayLinkedVariables.mArrayValues[i];
                    arrayLinkedVariables.remove(solverVariable, false);
                    ArrayLinkedVariables arrayLinkedVariables2 = arrayRow.variables;
                    int i4 = arrayLinkedVariables2.mHead;
                    for (int i5 = 0; i4 != -1 && i5 < arrayLinkedVariables2.currentSize; i5++) {
                        arrayLinkedVariables.add(arrayLinkedVariables.mCache.mIndexedVariables[arrayLinkedVariables2.mArrayIndices[i4]], arrayLinkedVariables2.mArrayValues[i4] * f, false);
                        i4 = arrayLinkedVariables2.mArrayNextIndices[i4];
                    }
                    this.constantValue = (arrayRow.constantValue * f) + this.constantValue;
                    i = arrayLinkedVariables.mHead;
                } else {
                    i = arrayLinkedVariables.mArrayNextIndices[i];
                }
            }
            return;
        }
    }

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }
}
