package com.objectfanatics.chrono0023

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(
        private val left: Int = 0,
        private val top: Int = 0,
        private val right: Int = 0,
        private val bottom: Int = 0,
        private val isEnabled: (outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) -> Boolean = { _, _, _, _ -> true }
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (isEnabled(outRect, view, parent, state)) {
            outRect.left = left
            outRect.top = top
            outRect.right = right
            outRect.bottom = bottom
        }
    }
}