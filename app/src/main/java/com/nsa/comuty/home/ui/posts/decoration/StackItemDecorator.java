package com.nsa.comuty.home.ui.posts.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class StackItemDecorator extends RecyclerView.ItemDecoration {

    private final int mSpaceTop;
    private final int mSpaceLeft;

    public StackItemDecorator(int mSpaceTop, int mSpaceLeft) {
        this.mSpaceTop = mSpaceTop;
        this.mSpaceLeft = mSpaceLeft;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            outRect.top = mSpaceTop;
            outRect.left = mSpaceLeft;
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
