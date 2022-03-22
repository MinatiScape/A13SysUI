package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.OptimizedPriorityGoalRow;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LinearSystem {
    public static int POOL_SIZE = 1000;
    public final Cache mCache;
    public OptimizedPriorityGoalRow mGoal;
    public ArrayRow[] mRows;
    public final ArrayRow mTempGoal;
    public int mVariablesID = 0;
    public int TABLE_SIZE = 32;
    public int mMaxColumns = 32;
    public boolean newgraphOptimizer = false;
    public boolean[] mAlreadyTestedCandidates = new boolean[32];
    public int mNumColumns = 1;
    public int mNumRows = 0;
    public int mMaxRows = 32;
    public SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
    public int mPoolVariablesCount = 0;

    /* loaded from: classes.dex */
    public interface Row {
        SolverVariable getPivotCandidate(boolean[] zArr);
    }

    public final ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            createRow.constantValue = i;
        }
        if (!z) {
            createRow.variables.put(solverVariable, -1.0f);
            createRow.variables.put(solverVariable2, 1.0f);
        } else {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable2, -1.0f);
        }
        if (i2 != 7) {
            createRow.addError(this, i2);
        }
        addConstraint(createRow);
        return createRow;
    }

    public final SolverVariable createObjectVariable(Object obj) {
        SolverVariable solverVariable = null;
        if (obj == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        if (obj instanceof ConstraintAnchor) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) obj;
            solverVariable = constraintAnchor.mSolverVariable;
            if (solverVariable == null) {
                constraintAnchor.resetSolverVariable();
                solverVariable = constraintAnchor.mSolverVariable;
            }
            int i = solverVariable.id;
            if (i == -1 || i > this.mVariablesID || this.mCache.mIndexedVariables[i] == null) {
                if (i != -1) {
                    solverVariable.reset();
                }
                int i2 = this.mVariablesID + 1;
                this.mVariablesID = i2;
                this.mNumColumns++;
                solverVariable.id = i2;
                solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
                this.mCache.mIndexedVariables[i2] = solverVariable;
            }
        }
        return solverVariable;
    }

    public final void reset() {
        Cache cache;
        int i = 0;
        while (true) {
            cache = this.mCache;
            SolverVariable[] solverVariableArr = cache.mIndexedVariables;
            if (i >= solverVariableArr.length) {
                break;
            }
            SolverVariable solverVariable = solverVariableArr[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
            i++;
        }
        Pools$SimplePool pools$SimplePool = cache.solverVariablePool;
        SolverVariable[] solverVariableArr2 = this.mPoolVariables;
        int i2 = this.mPoolVariablesCount;
        Objects.requireNonNull(pools$SimplePool);
        if (i2 > solverVariableArr2.length) {
            i2 = solverVariableArr2.length;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            SolverVariable solverVariable2 = solverVariableArr2[i3];
            int i4 = pools$SimplePool.mPoolSize;
            Object[] objArr = pools$SimplePool.mPool;
            if (i4 < objArr.length) {
                objArr[i4] = solverVariable2;
                pools$SimplePool.mPoolSize = i4 + 1;
            }
        }
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, (Object) null);
        this.mVariablesID = 0;
        OptimizedPriorityGoalRow optimizedPriorityGoalRow = this.mGoal;
        Objects.requireNonNull(optimizedPriorityGoalRow);
        optimizedPriorityGoalRow.numGoals = 0;
        optimizedPriorityGoalRow.constantValue = 0.0f;
        this.mNumColumns = 1;
        for (int i5 = 0; i5 < this.mNumRows; i5++) {
            Objects.requireNonNull(this.mRows[i5]);
        }
        int i6 = 0;
        while (true) {
            ArrayRow[] arrayRowArr = this.mRows;
            if (i6 < arrayRowArr.length) {
                ArrayRow arrayRow = arrayRowArr[i6];
                if (arrayRow != null) {
                    Pools$SimplePool pools$SimplePool2 = this.mCache.arrayRowPool;
                    Objects.requireNonNull(pools$SimplePool2);
                    int i7 = pools$SimplePool2.mPoolSize;
                    Object[] objArr2 = pools$SimplePool2.mPool;
                    if (i7 < objArr2.length) {
                        objArr2[i7] = arrayRow;
                        pools$SimplePool2.mPoolSize = i7 + 1;
                    }
                }
                this.mRows[i6] = null;
                i6++;
            } else {
                this.mNumRows = 0;
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.constraintlayout.solver.SolverVariable acquireSolverVariable(androidx.constraintlayout.solver.SolverVariable.Type r6) {
        /*
            r5 = this;
            androidx.constraintlayout.solver.Cache r0 = r5.mCache
            androidx.constraintlayout.solver.Pools$SimplePool r0 = r0.solverVariablePool
            java.util.Objects.requireNonNull(r0)
            int r1 = r0.mPoolSize
            r2 = 0
            if (r1 <= 0) goto L_0x0017
            int r1 = r1 + (-1)
            java.lang.Object[] r3 = r0.mPool
            r4 = r3[r1]
            r3[r1] = r2
            r0.mPoolSize = r1
            r2 = r4
        L_0x0017:
            androidx.constraintlayout.solver.SolverVariable r2 = (androidx.constraintlayout.solver.SolverVariable) r2
            if (r2 != 0) goto L_0x0023
            androidx.constraintlayout.solver.SolverVariable r2 = new androidx.constraintlayout.solver.SolverVariable
            r2.<init>(r6)
            r2.mType = r6
            goto L_0x0028
        L_0x0023:
            r2.reset()
            r2.mType = r6
        L_0x0028:
            int r6 = r5.mPoolVariablesCount
            int r0 = androidx.constraintlayout.solver.LinearSystem.POOL_SIZE
            if (r6 < r0) goto L_0x003c
            int r0 = r0 * 2
            androidx.constraintlayout.solver.LinearSystem.POOL_SIZE = r0
            androidx.constraintlayout.solver.SolverVariable[] r6 = r5.mPoolVariables
            java.lang.Object[] r6 = java.util.Arrays.copyOf(r6, r0)
            androidx.constraintlayout.solver.SolverVariable[] r6 = (androidx.constraintlayout.solver.SolverVariable[]) r6
            r5.mPoolVariables = r6
        L_0x003c:
            androidx.constraintlayout.solver.SolverVariable[] r6 = r5.mPoolVariables
            int r0 = r5.mPoolVariablesCount
            int r1 = r0 + 1
            r5.mPoolVariablesCount = r1
            r6[r0] = r2
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.LinearSystem.acquireSolverVariable(androidx.constraintlayout.solver.SolverVariable$Type):androidx.constraintlayout.solver.SolverVariable");
    }

    /* JADX WARN: Code restructure failed: missing block: B:69:0x012c, code lost:
        if (r4.usageInRowCount <= 1) goto L_0x0138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0136, code lost:
        if (r4.usageInRowCount <= 1) goto L_0x0138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0138, code lost:
        r18 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x013b, code lost:
        r18 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x015b, code lost:
        if (r4.usageInRowCount <= 1) goto L_0x0167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0165, code lost:
        if (r4.usageInRowCount <= 1) goto L_0x0167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0167, code lost:
        r18 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x016a, code lost:
        r18 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:174:0x017f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0123  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void addConstraint(androidx.constraintlayout.solver.ArrayRow r20) {
        /*
            Method dump skipped, instructions count: 595
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.LinearSystem.addConstraint(androidx.constraintlayout.solver.ArrayRow):void");
    }

    public final void addRow(ArrayRow arrayRow) {
        ArrayRow[] arrayRowArr = this.mRows;
        int i = this.mNumRows;
        if (arrayRowArr[i] != null) {
            Pools$SimplePool pools$SimplePool = this.mCache.arrayRowPool;
            ArrayRow arrayRow2 = arrayRowArr[i];
            Objects.requireNonNull(pools$SimplePool);
            int i2 = pools$SimplePool.mPoolSize;
            Object[] objArr = pools$SimplePool.mPool;
            if (i2 < objArr.length) {
                objArr[i2] = arrayRow2;
                pools$SimplePool.mPoolSize = i2 + 1;
            }
        }
        ArrayRow[] arrayRowArr2 = this.mRows;
        int i3 = this.mNumRows;
        arrayRowArr2[i3] = arrayRow;
        SolverVariable solverVariable = arrayRow.variable;
        solverVariable.definitionId = i3;
        this.mNumRows = i3 + 1;
        solverVariable.updateReferencesWithNewDefinition(arrayRow);
    }

    public final SolverVariable createErrorVariable(int i) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(SolverVariable.Type.ERROR);
        int i2 = this.mVariablesID + 1;
        this.mVariablesID = i2;
        this.mNumColumns++;
        acquireSolverVariable.id = i2;
        acquireSolverVariable.strength = i;
        this.mCache.mIndexedVariables[i2] = acquireSolverVariable;
        OptimizedPriorityGoalRow optimizedPriorityGoalRow = this.mGoal;
        Objects.requireNonNull(optimizedPriorityGoalRow);
        OptimizedPriorityGoalRow.GoalVariableAccessor goalVariableAccessor = optimizedPriorityGoalRow.accessor;
        Objects.requireNonNull(goalVariableAccessor);
        goalVariableAccessor.variable = acquireSolverVariable;
        OptimizedPriorityGoalRow.GoalVariableAccessor goalVariableAccessor2 = optimizedPriorityGoalRow.accessor;
        Objects.requireNonNull(goalVariableAccessor2);
        Arrays.fill(goalVariableAccessor2.variable.goalStrengthVector, 0.0f);
        acquireSolverVariable.goalStrengthVector[acquireSolverVariable.strength] = 1.0f;
        optimizedPriorityGoalRow.addToGoal(acquireSolverVariable);
        return acquireSolverVariable;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.constraintlayout.solver.ArrayRow createRow() {
        /*
            r5 = this;
            androidx.constraintlayout.solver.Cache r0 = r5.mCache
            androidx.constraintlayout.solver.Pools$SimplePool r0 = r0.arrayRowPool
            java.util.Objects.requireNonNull(r0)
            int r1 = r0.mPoolSize
            r2 = 0
            if (r1 <= 0) goto L_0x0017
            int r1 = r1 + (-1)
            java.lang.Object[] r3 = r0.mPool
            r4 = r3[r1]
            r3[r1] = r2
            r0.mPoolSize = r1
            goto L_0x0018
        L_0x0017:
            r4 = r2
        L_0x0018:
            androidx.constraintlayout.solver.ArrayRow r4 = (androidx.constraintlayout.solver.ArrayRow) r4
            if (r4 != 0) goto L_0x0024
            androidx.constraintlayout.solver.ArrayRow r4 = new androidx.constraintlayout.solver.ArrayRow
            androidx.constraintlayout.solver.Cache r5 = r5.mCache
            r4.<init>(r5)
            goto L_0x0031
        L_0x0024:
            r4.variable = r2
            androidx.constraintlayout.solver.ArrayLinkedVariables r5 = r4.variables
            r5.clear()
            r5 = 0
            r4.constantValue = r5
            r5 = 0
            r4.isSimpleDefinition = r5
        L_0x0031:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.LinearSystem.createRow():androidx.constraintlayout.solver.ArrayRow");
    }

    public final SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable = acquireSolverVariable(SolverVariable.Type.SLACK);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable.id = i;
        this.mCache.mIndexedVariables[i] = acquireSolverVariable;
        return acquireSolverVariable;
    }

    public final void increaseTableSize() {
        int i = this.TABLE_SIZE * 2;
        this.TABLE_SIZE = i;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, i);
        Cache cache = this.mCache;
        cache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
        int i2 = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[i2];
        this.mMaxColumns = i2;
        this.mMaxRows = i2;
    }

    public final void minimizeGoal(OptimizedPriorityGoalRow optimizedPriorityGoalRow) throws Exception {
        float f;
        int i;
        boolean z;
        SolverVariable.Type type = SolverVariable.Type.UNRESTRICTED;
        int i2 = 0;
        while (true) {
            f = 0.0f;
            i = 1;
            if (i2 >= this.mNumRows) {
                z = false;
                break;
            }
            ArrayRow[] arrayRowArr = this.mRows;
            if (arrayRowArr[i2].variable.mType != type && arrayRowArr[i2].constantValue < 0.0f) {
                z = true;
                break;
            }
            i2++;
        }
        if (z) {
            boolean z2 = false;
            int i3 = 0;
            while (!z2) {
                i3 += i;
                float f2 = Float.MAX_VALUE;
                int i4 = -1;
                int i5 = -1;
                int i6 = 0;
                int i7 = 0;
                while (i6 < this.mNumRows) {
                    ArrayRow arrayRow = this.mRows[i6];
                    if (arrayRow.variable.mType != type && !arrayRow.isSimpleDefinition && arrayRow.constantValue < f) {
                        int i8 = i;
                        while (i8 < this.mNumColumns) {
                            SolverVariable solverVariable = this.mCache.mIndexedVariables[i8];
                            float f3 = arrayRow.variables.get(solverVariable);
                            if (f3 > f) {
                                for (int i9 = 0; i9 < 8; i9++) {
                                    float f4 = solverVariable.strengthVector[i9] / f3;
                                    if ((f4 < f2 && i9 == i7) || i9 > i7) {
                                        i7 = i9;
                                        f2 = f4;
                                        i4 = i6;
                                        i5 = i8;
                                    }
                                }
                            }
                            i8++;
                            f = 0.0f;
                        }
                    }
                    i6++;
                    f = 0.0f;
                    i = 1;
                }
                if (i4 != -1) {
                    ArrayRow arrayRow2 = this.mRows[i4];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(this.mCache.mIndexedVariables[i5]);
                    SolverVariable solverVariable2 = arrayRow2.variable;
                    solverVariable2.definitionId = i4;
                    solverVariable2.updateReferencesWithNewDefinition(arrayRow2);
                } else {
                    z2 = true;
                }
                if (i3 > this.mNumColumns / 2) {
                    z2 = true;
                }
                f = 0.0f;
                i = 1;
            }
        }
        optimize(optimizedPriorityGoalRow);
        for (int i10 = 0; i10 < this.mNumRows; i10++) {
            ArrayRow arrayRow3 = this.mRows[i10];
            arrayRow3.variable.computedValue = arrayRow3.constantValue;
        }
    }

    public final int optimize(ArrayRow arrayRow) {
        boolean z;
        int i = 0;
        for (int i2 = 0; i2 < this.mNumColumns; i2++) {
            this.mAlreadyTestedCandidates[i2] = false;
        }
        boolean z2 = false;
        int i3 = 0;
        while (!z2) {
            i3++;
            if (i3 >= this.mNumColumns * 2) {
                return i3;
            }
            Objects.requireNonNull(arrayRow);
            SolverVariable solverVariable = arrayRow.variable;
            if (solverVariable != null) {
                this.mAlreadyTestedCandidates[solverVariable.id] = true;
            }
            SolverVariable pivotCandidate = arrayRow.getPivotCandidate(this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                boolean[] zArr = this.mAlreadyTestedCandidates;
                int i4 = pivotCandidate.id;
                if (zArr[i4]) {
                    return i3;
                }
                zArr[i4] = true;
            }
            if (pivotCandidate != null) {
                float f = Float.MAX_VALUE;
                int i5 = i;
                int i6 = -1;
                while (i5 < this.mNumRows) {
                    ArrayRow arrayRow2 = this.mRows[i5];
                    if (arrayRow2.variable.mType != SolverVariable.Type.UNRESTRICTED && !arrayRow2.isSimpleDefinition) {
                        ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                        Objects.requireNonNull(arrayLinkedVariables);
                        int i7 = arrayLinkedVariables.mHead;
                        if (i7 != -1) {
                            for (int i8 = i; i7 != -1 && i8 < arrayLinkedVariables.currentSize; i8++) {
                                if (arrayLinkedVariables.mArrayIndices[i7] == pivotCandidate.id) {
                                    z = true;
                                    break;
                                }
                                i7 = arrayLinkedVariables.mArrayNextIndices[i7];
                            }
                        }
                        z = false;
                        if (z) {
                            float f2 = arrayRow2.variables.get(pivotCandidate);
                            if (f2 < 0.0f) {
                                float f3 = (-arrayRow2.constantValue) / f2;
                                if (f3 < f) {
                                    i6 = i5;
                                    f = f3;
                                }
                            }
                        }
                    }
                    i5++;
                    i = 0;
                }
                if (i6 > -1) {
                    ArrayRow arrayRow3 = this.mRows[i6];
                    arrayRow3.variable.definitionId = -1;
                    arrayRow3.pivot(pivotCandidate);
                    SolverVariable solverVariable2 = arrayRow3.variable;
                    solverVariable2.definitionId = i6;
                    solverVariable2.updateReferencesWithNewDefinition(arrayRow3);
                }
            } else {
                z2 = true;
            }
            i = 0;
        }
        return i3;
    }

    public LinearSystem() {
        int i = 0;
        this.mRows = null;
        this.mRows = new ArrayRow[32];
        while (true) {
            ArrayRow[] arrayRowArr = this.mRows;
            if (i < arrayRowArr.length) {
                ArrayRow arrayRow = arrayRowArr[i];
                if (arrayRow != null) {
                    Pools$SimplePool pools$SimplePool = this.mCache.arrayRowPool;
                    Objects.requireNonNull(pools$SimplePool);
                    int i2 = pools$SimplePool.mPoolSize;
                    Object[] objArr = pools$SimplePool.mPool;
                    if (i2 < objArr.length) {
                        objArr[i2] = arrayRow;
                        pools$SimplePool.mPoolSize = i2 + 1;
                    }
                }
                this.mRows[i] = null;
                i++;
            } else {
                Cache cache = new Cache();
                this.mCache = cache;
                this.mGoal = new OptimizedPriorityGoalRow(cache);
                this.mTempGoal = new ArrayRow(cache);
                return;
            }
        }
    }

    public static int getObjectVariableValue(ConstraintAnchor constraintAnchor) {
        Objects.requireNonNull(constraintAnchor);
        SolverVariable solverVariable = constraintAnchor.mSolverVariable;
        if (solverVariable != null) {
            return (int) (solverVariable.computedValue + 0.5f);
        }
        return 0;
    }

    public final void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int i, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int i2, int i3) {
        ArrayRow createRow = createRow();
        if (solverVariable2 == solverVariable3) {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable4, 1.0f);
            createRow.variables.put(solverVariable2, -2.0f);
        } else if (f == 0.5f) {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable2, -1.0f);
            createRow.variables.put(solverVariable3, -1.0f);
            createRow.variables.put(solverVariable4, 1.0f);
            if (i > 0 || i2 > 0) {
                createRow.constantValue = (-i) + i2;
            }
        } else if (f <= 0.0f) {
            createRow.variables.put(solverVariable, -1.0f);
            createRow.variables.put(solverVariable2, 1.0f);
            createRow.constantValue = i;
        } else if (f >= 1.0f) {
            createRow.variables.put(solverVariable4, -1.0f);
            createRow.variables.put(solverVariable3, 1.0f);
            createRow.constantValue = -i2;
        } else {
            float f2 = 1.0f - f;
            createRow.variables.put(solverVariable, f2 * 1.0f);
            createRow.variables.put(solverVariable2, f2 * (-1.0f));
            createRow.variables.put(solverVariable3, (-1.0f) * f);
            createRow.variables.put(solverVariable4, 1.0f * f);
            if (i > 0 || i2 > 0) {
                createRow.constantValue = (i2 * f) + ((-i) * f2);
            }
        }
        if (i3 != 7) {
            createRow.addError(this, i3);
        }
        addConstraint(createRow);
    }

    public final void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowGreaterThan(solverVariable, solverVariable2, createSlackVariable, i);
        if (i2 != 7) {
            createRow.variables.put(createErrorVariable(i2), (int) (createRow.variables.get(createSlackVariable) * (-1.0f)));
        }
        addConstraint(createRow);
    }

    public final void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowLowerThan(solverVariable, solverVariable2, createSlackVariable, i);
        if (i2 != 7) {
            createRow.variables.put(createErrorVariable(i2), (int) (createRow.variables.get(createSlackVariable) * (-1.0f)));
        }
        addConstraint(createRow);
    }

    public final void addEquality(SolverVariable solverVariable, int i) {
        int i2 = solverVariable.definitionId;
        if (i2 != -1) {
            ArrayRow arrayRow = this.mRows[i2];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = i;
            } else if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
                arrayRow.constantValue = i;
            } else {
                ArrayRow createRow = createRow();
                if (i < 0) {
                    createRow.constantValue = i * (-1);
                    createRow.variables.put(solverVariable, 1.0f);
                } else {
                    createRow.constantValue = i;
                    createRow.variables.put(solverVariable, -1.0f);
                }
                addConstraint(createRow);
            }
        } else {
            ArrayRow createRow2 = createRow();
            createRow2.variable = solverVariable;
            float f = i;
            solverVariable.computedValue = f;
            createRow2.constantValue = f;
            createRow2.isSimpleDefinition = true;
            addConstraint(createRow2);
        }
    }
}
