package com.example.test

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// 각 날씨 리싸이클러 뷰에 데코

class MyItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect?.set(10, 10, 10, 10)
        view?.setBackgroundColor(-0x776d6f70)
    }
}

// 데코레이션 적용하면 각 날씨에 데코가 생김