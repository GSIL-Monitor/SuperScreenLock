package com.hzp.superscreenlock.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * @author hoholiday on 2016/9/1.
 * @email hoholiday@hotmail.com
 */

public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public LinearSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) != 0)
            outRect.bottom = space;
    }
}
