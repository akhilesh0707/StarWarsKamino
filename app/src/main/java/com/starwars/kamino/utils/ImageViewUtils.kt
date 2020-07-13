package com.starwars.kamino.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * Set planet image zoom and animation
 * Ref: https://developer.android.com/training/animation/zoom
 * @param shortAnimationDuration : Animation duration
 * @param root : root view
 * @param imageExpand : expand ImageView reference
 */
fun ImageView.zoomImageFromThumb(
    shortAnimationDuration: Int,
    root: View,
    imageExpand: ImageView
) {
    // If there's an animation in progress, cancel it
    // immediately and proceed with this one.
    var currentAnimator: Animator? = null
    val currentImage: ImageView = this
    currentAnimator?.cancel()

    // Load the high-resolution "zoomed-in" image.
    imageExpand.setImageDrawable(currentImage.drawable)

    // Calculate the starting and ending bounds for the zoomed-in image.
    // This step involves lots of math. Yay, math.
    val startBoundsInt = Rect()
    val finalBoundsInt = Rect()
    val globalOffset = Point()

    // The start bounds are the global visible rectangle of the thumbnail,
    // and the final bounds are the global visible rectangle of the container
    // view. Also set the container view's offset as the origin for the
    // bounds, since that's the origin for the positioning animation
    // properties (X, Y).
    currentImage.getGlobalVisibleRect(startBoundsInt)
    root.getGlobalVisibleRect(finalBoundsInt, globalOffset)
    startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
    finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

    val startBounds = RectF(startBoundsInt)
    val finalBounds = RectF(finalBoundsInt)

    // Adjust the start bounds to be the same aspect ratio as the final
    // bounds using the "center crop" technique. This prevents undesirable
    // stretching during the animation. Also calculate the start scaling
    // factor (the end scaling factor is always 1.0).
    val startScale: Float
    if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
        // Extend start bounds horizontally
        startScale = startBounds.height() / finalBounds.height()
        val startWidth: Float = startScale * finalBounds.width()
        val deltaWidth: Float = (startWidth - startBounds.width()) / 2
        startBounds.left -= deltaWidth.toInt()
        startBounds.right += deltaWidth.toInt()
    } else {
        // Extend start bounds vertically
        startScale = startBounds.width() / finalBounds.width()
        val startHeight: Float = startScale * finalBounds.height()
        val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
        startBounds.top -= deltaHeight.toInt()
        startBounds.bottom += deltaHeight.toInt()
    }

    // Hide the thumbnail and show the zoomed-in view. When the animation
    // begins, it will position the zoomed-in view in the place of the
    // thumbnail.
    currentImage.alpha = 0f
    imageExpand.makeVisible()

    // Set the pivot point for SCALE_X and SCALE_Y transformations
    // to the top-left corner of the zoomed-in view (the default
    // is the center of the view).
    imageExpand.pivotX = 0f
    imageExpand.pivotY = 0f

    // Construct and run the parallel animation of the four translation and
    // scale properties (X, Y, SCALE_X, and SCALE_Y).
    currentAnimator = AnimatorSet().apply {
        play(
            ObjectAnimator.ofFloat(imageExpand, View.X, startBounds.left, finalBounds.left)
        ).apply {
            with(
                ObjectAnimator.ofFloat(imageExpand, View.Y, startBounds.top, finalBounds.top)
            )
            with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_X, startScale, 1f))
            with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_Y, startScale, 1f))
        }
        duration = shortAnimationDuration.toLong()
        interpolator = DecelerateInterpolator()
        addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                currentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                currentAnimator = null
            }
        })
        start()
    }

    // Upon clicking the zoomed-in image, it should zoom back down
    // to the original bounds and show the thumbnail instead of
    // the expanded image.
    imageExpand.setOnClickListener {
        currentAnimator?.cancel()

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(imageExpand, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(imageExpand, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_Y, startScale))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentImage.alpha = 1f
                    imageExpand.makeGone()
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentImage.alpha = 1f
                    imageExpand.makeGone()
                    currentAnimator = null
                }
            })
            start()
        }
    }
}