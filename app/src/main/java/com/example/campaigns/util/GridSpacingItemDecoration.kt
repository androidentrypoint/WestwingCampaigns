package com.example.campaigns.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * For spacing items in a grid. Gotten from https://stackoverflow.com/a/30701422/8406639
 */
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val horizontalEdgeSpacing: Int,
    private val verticalEdgeSpacing: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        val row = ceil((position + 1) / spanCount.toFloat()).toInt()
        when (column) {
            0 -> {
                outRect.left = horizontalEdgeSpacing
                outRect.right = horizontalSpacing / 2
            }
            spanCount - 1 -> {
                outRect.right = horizontalEdgeSpacing
                outRect.left = horizontalSpacing / 2
            }
            else -> {
                outRect.left = horizontalSpacing / 2
                outRect.right = horizontalSpacing / 2
            }
        }

        when (row) {
            1 -> {
                outRect.top = verticalEdgeSpacing
                outRect.bottom = verticalSpacing / 2
            }
//            spanCount -> {
//                outRect.bottom = verticalEdgeSpacing
//                outRect.top = verticalSpacing / 2
//            }
            else -> {
                outRect.top = verticalSpacing / 2
                outRect.bottom = verticalSpacing / 2
            }
        }
    }
}