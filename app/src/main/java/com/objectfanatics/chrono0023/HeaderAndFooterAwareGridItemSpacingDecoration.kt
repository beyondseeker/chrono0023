package com.objectfanatics.chrono0023

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.ceil

/**
 * Cell と cell の間に [spacingPx] で指定された間隔を空ける [ItemDecoration] です。
 *
 * 先頭と末尾の cell に限り、header や footer (spanSize == spanCount の cell) として扱うことができます。
 *
 * Cell の spanSize は以下のみが許されます：
 * - [RecyclerView.getChildAdapterPosition] の位置が先頭及び末尾の cell は 1 もしくは [GridLayoutManager.getSpanCount].
 * - 上記以外は全て 1.
 */
class HeaderAndFooterAwareGridItemSpacingDecoration(private val spacingPx: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = getSpanCount(parent)
        val adapterPosition = parent.getChildAdapterPosition(view)
        val spanSize = getSpanSize(parent, adapterPosition, spanCount)
        val minUnit: Int = ceil(spacingPx.toDouble() / spanCount).toInt()
        val hasHeader = hasHeader(parent, spanCount)

        if (spanSize == 1) {
            val columnPosition = getColumnPosition(spanCount, adapterPosition, hasHeader)
            outRect.left = columnPosition * minUnit
            outRect.right = ((spanCount - 1) - columnPosition) * minUnit
        }

        if (!isTopRow(adapterPosition, spanCount, hasHeader)) {
            outRect.top = minUnit * spanCount
        }
    }

    private fun getColumnPosition(spanCount: Int, adapterPosition: Int, hasHeader: Boolean): Int {
        val adapterPositionExcludingHeader = when {
            hasHeader -> adapterPosition - 1
            else      -> adapterPosition
        }

        return adapterPositionExcludingHeader % spanCount
    }

    private fun getSpanCount(parent: RecyclerView) =
        getGridLayoutManager(parent).spanCount

    private fun getSpanSize(parent: RecyclerView, adapterPosition: Int, spanCount: Int): Int =
        getSpanSize(parent, adapterPosition).also { spanSize ->
            if (spanSize != 1) {
                if (spanSize != spanCount) {
                    throw IllegalStateException("spanSize must be 1 or spanCount. [spanSize = $spanSize, spanCount = $spanCount]")
                }
                if (adapterPosition != 0 && adapterPosition < parent.adapter!!.itemCount - 1) {
                    throw IllegalStateException("spanSize must be 1. [adapterPosition = $adapterPosition, spanSize = $spanSize]")
                }
            }
        }

    private fun hasHeader(parent: RecyclerView, spanCount: Int): Boolean =
        getSpanSize(parent, 0) == spanCount

    private fun isTopRow(adapterPosition: Int, spanCount: Int, hasHeader: Boolean): Boolean =
        when {
            adapterPosition == 0         -> true
            adapterPosition >= spanCount -> false
            else                         -> !hasHeader
        }

    private fun getSpanSize(parent: RecyclerView, position: Int) =
        getGridLayoutManager(parent).spanSizeLookup.getSpanSize(position)

    private fun getGridLayoutManager(parent: RecyclerView) =
        (parent.layoutManager as GridLayoutManager)
}