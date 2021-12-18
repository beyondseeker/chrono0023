package com.objectfanatics.chrono0023

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        adapter = GridViewAdapter()
        layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = (adapter as GridViewAdapter).items[position].span
            }
        }
        addItemDecoration(HeaderAndFooterAwareGridItemSpacingDecoration(SPACING_PX))
    }

    private class GridViewAdapter : RecyclerView.Adapter<ViewHolder>() {
        val items: List<Item> = listOf(
            Item.Header("HEADER"),
            Item.Cell("A"),
            Item.Cell("B"),
            Item.Cell("C"),
            Item.Cell("D"),
            Item.Cell("E"),
            Item.Cell("F"),
            Item.Cell("G"),
            Item.Cell("H"),
            Item.Cell("I"),
            Item.Footer("FOOTER"),
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            object : ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)) {}

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as TextView).text = items[position].text
        }

        override fun getItemCount(): Int = items.size
    }

    private sealed class Item(val text: String, val span: Int) {
        class Cell(text: String) : Item(text, 1)
        class Header(text: String) : Item(text, SPAN_COUNT)
        class Footer(text: String) : Item(text, SPAN_COUNT)
    }

    companion object {
        private const val SPAN_COUNT = 5
        private const val SPACING_PX = 20
    }
}