package com.mindinventory

import android.content.Context
import android.graphics.PorterDuff
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.view_auto_scroll_container.view.*

class AutoScrollCircularPagerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var selectedDotColor = ContextCompat.getColor(context, android.R.color.black)
    private var unSelectedDotColor = ContextCompat.getColor(context, android.R.color.white)
    private var autoScrollDelay = 3000L
    private var scrollHandler = Handler(Looper.getMainLooper())
    private var slidingImageDots = ArrayList<AppCompatImageView>()
    private var centerItemPosition = Int.MAX_VALUE / 2
    private var circularAdapter: CircularAdapter<*>? = null
    private var isAutoScrollEnabled = true
    private var marginDots = -1
    private var dotGravity = DotGravity.BOTTOM.value

    private val runnable = Runnable {
        autoScrollViewpager()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_auto_scroll_container, this, true)
        attrs?.let(this::setupAttrs)
        initRecyclerView()
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.AutoScrollCircularPagerView,
                0,
                0
            )
        try {
            with(typedArray) {
                unSelectedDotColor = getColor(
                    R.styleable.AutoScrollCircularPagerView_unselected_dot_color,
                    ContextCompat.getColor(context, android.R.color.white)
                )
                selectedDotColor = getColor(
                    R.styleable.AutoScrollCircularPagerView_selected_dot_color,
                    ContextCompat.getColor(context, android.R.color.black)
                )
                autoScrollDelay = getInteger(
                    R.styleable.AutoScrollCircularPagerView_auto_scroll_delay,
                    3000
                ).toLong()

                isAutoScrollEnabled = getBoolean(
                    R.styleable.AutoScrollCircularPagerView_is_auto_scroll,
                    true
                )
                marginDots = getDimensionPixelSize(
                    R.styleable.AutoScrollCircularPagerView_dot_margin,
                    -1
                )

                dotGravity = getInt(
                    R.styleable.AutoScrollCircularPagerView_dot_gravity,
                    DotGravity.BOTTOM.value
                )
                if (autoScrollDelay < 500L) {
                    autoScrollDelay = 500L
                }
            }
        } catch (exception: Exception) {
            Log.e("AutoScrollContainer", exception.toString())
        } finally {
            typedArray.recycle()
        }
    }

    fun setAutoScrollDelay(autoScrollDelay: Long) {
        if (autoScrollDelay < 500) {
            this.autoScrollDelay = autoScrollDelay
            stopAutoScrollIfRequired()
            startAutoScrollIfRequired()
        }
    }

    private fun initRecyclerView() {
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvAutoScroll)
        rvAutoScroll.addOnScrollListener(
            CenterItemFinder(
                context,
                rvAutoScroll.layoutManager as LinearLayoutManager,
                object : CenterItemCallback {
                    override fun onScrollFinished(middleElement: Int) {
                        centerItemPosition = middleElement
                        onPageSelected(middleElement)
                    }

                    override fun onScrolled(dx: Int) {

                    }
                },
                RecyclerView.SCROLL_STATE_IDLE
            )
        )
    }

    fun setAdapter(circularAdapter: CircularAdapter<*>) {
        this.circularAdapter = circularAdapter
        rvAutoScroll.adapter = circularAdapter
    }

    @Suppress("UNCHECKED_CAST")
    fun <E> setItems(items: ArrayList<E>, clearPreviousElements: Boolean = false) {
        if (rvAutoScroll.adapter is CircularAdapter<*>) {
            val circularAdapter = rvAutoScroll.adapter as CircularAdapter<E>
            circularAdapter.setItems(items, clearPreviousElements)

            //scroll and dots are not required for just 1 item in the list
            if (items.size > 1) {
                //To manage selection of first item when auto scroll start
                centerItemPosition += if (items.size % 2 == 0) {
                    1
                } else {
                    2
                }
                rvAutoScroll.layoutManager?.scrollToPosition(centerItemPosition)
                setDots(items)
            }
        }
        startAutoScrollIfRequired()
    }

    private fun <E> setDots(items: ArrayList<E>) {
        slidingImageDots.clear()
        llSliderDots.removeAllViews()
        val llSliderDotsParams = llSliderDots.layoutParams as LayoutParams
        llSliderDotsParams.setMargins(marginDots)
        if (dotGravity == DotGravity.TOP.value) {
            llSliderDotsParams.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        } else {
            llSliderDotsParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }

        llSliderDots.layoutParams = llSliderDotsParams
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(8, 0, 8, 0)
        items.indices.forEach {
            slidingImageDots.add(it, AppCompatImageView(context))
            llSliderDots.addView(slidingImageDots[it], params)
            onPageSelected(0)
        }
    }

    /**
     * Manage dot selection on scroll
     */
    private fun onPageSelected(middleElementPosition: Int) {
        if (slidingImageDots.size > 0) {
            val position = middleElementPosition % slidingImageDots.size
            slidingImageDots.indices.forEach { i ->
                if (i == position) {
                    slidingImageDots[i].setImageResource(R.drawable.ic_active_dot)
                    slidingImageDots[i].setColorFilter(selectedDotColor, PorterDuff.Mode.SRC_IN)
                } else {
                    slidingImageDots[i].setImageResource(R.drawable.ic_non_active_dot)
                    slidingImageDots[i].setColorFilter(unSelectedDotColor, PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

    private fun requiredAutoScroll(): Boolean {
        return circularAdapter?.getActualItemCount() ?: 0 > 1
    }

    private fun startAutoScrollIfRequired() {
        if (requiredAutoScroll() && !scrollHandler.hasCallbacks(runnable) && isAutoScrollEnabled)
            scrollHandler.postDelayed(runnable, autoScrollDelay)
    }

    private fun stopAutoScrollIfRequired() {
        if (scrollHandler.hasCallbacks(runnable)) {
            scrollHandler.removeCallbacks(runnable)
        }
    }

    /**
     * smooth scroll item to next position
     */
    private fun autoScrollViewpager() {
        rvAutoScroll.adapter?.let {
            rvAutoScroll.smoothScrollToPosition(centerItemPosition + 1)
        }
        stopAutoScrollIfRequired()
        startAutoScrollIfRequired()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScrollIfRequired()
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) {
            startAutoScrollIfRequired()
        } else {
            stopAutoScrollIfRequired()
        }
    }

    enum class DotGravity(val value: Int) {
        BOTTOM(0),
        TOP(1)
    }
}

