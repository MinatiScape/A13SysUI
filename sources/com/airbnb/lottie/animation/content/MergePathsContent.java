package com.airbnb.lottie.animation.content;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.Path;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.content.MergePaths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
@TargetApi(19)
/* loaded from: classes.dex */
public final class MergePathsContent implements PathContent, GreedyContent {
    public final MergePaths mergePaths;
    public final Path firstPath = new Path();
    public final Path remainderPath = new Path();
    public final Path path = new Path();
    public final ArrayList pathContents = new ArrayList();

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < this.pathContents.size(); i++) {
            ((PathContent) this.pathContents.get(i)).setContents(list, list2);
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        this.path.reset();
        MergePaths mergePaths = this.mergePaths;
        Objects.requireNonNull(mergePaths);
        if (mergePaths.hidden) {
            return this.path;
        }
        MergePaths mergePaths2 = this.mergePaths;
        Objects.requireNonNull(mergePaths2);
        int ordinal = mergePaths2.mode.ordinal();
        if (ordinal == 0) {
            for (int i = 0; i < this.pathContents.size(); i++) {
                this.path.addPath(((PathContent) this.pathContents.get(i)).getPath());
            }
        } else if (ordinal == 1) {
            opFirstPathWithRest(Path.Op.UNION);
        } else if (ordinal == 2) {
            opFirstPathWithRest(Path.Op.REVERSE_DIFFERENCE);
        } else if (ordinal == 3) {
            opFirstPathWithRest(Path.Op.INTERSECT);
        } else if (ordinal == 4) {
            opFirstPathWithRest(Path.Op.XOR);
        }
        return this.path;
    }

    @TargetApi(19)
    public final void opFirstPathWithRest(Path.Op op) {
        Matrix matrix;
        Matrix matrix2;
        this.remainderPath.reset();
        this.firstPath.reset();
        for (int size = this.pathContents.size() - 1; size >= 1; size--) {
            PathContent pathContent = (PathContent) this.pathContents.get(size);
            if (pathContent instanceof ContentGroup) {
                ContentGroup contentGroup = (ContentGroup) pathContent;
                ArrayList arrayList = (ArrayList) contentGroup.getPathList();
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    Path path = ((PathContent) arrayList.get(size2)).getPath();
                    TransformKeyframeAnimation transformKeyframeAnimation = contentGroup.transformAnimation;
                    if (transformKeyframeAnimation != null) {
                        matrix2 = transformKeyframeAnimation.getMatrix();
                    } else {
                        contentGroup.matrix.reset();
                        matrix2 = contentGroup.matrix;
                    }
                    path.transform(matrix2);
                    this.remainderPath.addPath(path);
                }
            } else {
                this.remainderPath.addPath(pathContent.getPath());
            }
        }
        int i = 0;
        PathContent pathContent2 = (PathContent) this.pathContents.get(0);
        if (pathContent2 instanceof ContentGroup) {
            ContentGroup contentGroup2 = (ContentGroup) pathContent2;
            List<PathContent> pathList = contentGroup2.getPathList();
            while (true) {
                ArrayList arrayList2 = (ArrayList) pathList;
                if (i >= arrayList2.size()) {
                    break;
                }
                Path path2 = ((PathContent) arrayList2.get(i)).getPath();
                TransformKeyframeAnimation transformKeyframeAnimation2 = contentGroup2.transformAnimation;
                if (transformKeyframeAnimation2 != null) {
                    matrix = transformKeyframeAnimation2.getMatrix();
                } else {
                    contentGroup2.matrix.reset();
                    matrix = contentGroup2.matrix;
                }
                path2.transform(matrix);
                this.firstPath.addPath(path2);
                i++;
            }
        } else {
            this.firstPath.set(pathContent2.getPath());
        }
        this.path.op(this.firstPath, this.remainderPath, op);
    }

    public MergePathsContent(MergePaths mergePaths) {
        Objects.requireNonNull(mergePaths);
        this.mergePaths = mergePaths;
    }

    @Override // com.airbnb.lottie.animation.content.GreedyContent
    public final void absorbContent(ListIterator<Content> listIterator) {
        while (listIterator.hasPrevious() && listIterator.previous() != this) {
        }
        while (listIterator.hasPrevious()) {
            Content previous = listIterator.previous();
            if (previous instanceof PathContent) {
                this.pathContents.add((PathContent) previous);
                listIterator.remove();
            }
        }
    }
}
