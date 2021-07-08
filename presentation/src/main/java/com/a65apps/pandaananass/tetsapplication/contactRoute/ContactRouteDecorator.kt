package com.a65apps.pandaananass.tetsapplication.contactRoute

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ContactRouteDecorator(private val topPaddingDP: Int, private val sidePaddingDP: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val topPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            topPaddingDP.toFloat(),
            view.context.resources.displayMetrics
        ).toInt()
        val sidePadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sidePaddingDP.toFloat(),
            view.context.resources.displayMetrics
        ).toInt()
        outRect.top = topPadding
        outRect.left = sidePadding
        outRect.right = sidePadding
        if (position == state.itemCount - 1) {
            outRect.bottom = topPadding
        }
    }
}
