package com.maxk.marvy.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginBottom
import com.maxk.marvy.extensions.rect
import com.maxk.marvy.extensions.rectInParentCoordinates

class CenteringLayoutBehavior<V: View>(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {

        // Dependency view rect must be in the parent coordinate system, because child view is
        // a direct child of the CoordinatorLayout.
        val dependencyRect = dependency.rectInParentCoordinates(parent)

        val availableSpaceRect = Rect(
            0,
            dependencyRect.bottom + dependency.marginBottom,
            parent.rect.width(),
            parent.rect.height() - parent.paddingBottom
        )

        child.x = availableSpaceRect.centerX() - child.width.toFloat() / 2
        child.y = availableSpaceRect.centerY() - child.height.toFloat() / 2

        return super.onDependentViewChanged(parent, child, dependency)
    }
}