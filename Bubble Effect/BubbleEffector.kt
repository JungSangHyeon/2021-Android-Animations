
class BubbleEffector(private val targetView: ViewGroup){

    // Animation Attribute
    private val minMoveTime = 3000 // ms
    private val maxMoveTime = 10000
    private val bubbleViewNum = 12

    // View Attribute
    private val minWidthHeight = 400
    private val maxWidthHeight = 700
    private val minAlpha = 1 // 1~10
    private val maxAlpha = 5
    private val drawableId = R.drawable.oval
    private val colorId = R.color.kati_main

    init{
        targetView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        for (i in 1..bubbleViewNum) addNewBubble()
                        targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
        )
    }

    fun addNewBubble() {
        val bubbleView = BubbleView(targetView)
        targetView.addView(bubbleView)
        startMove(bubbleView)
    }

    private fun startMove(view: View) {
        val parentView = view.parent as View
        val path = Path().apply {
            setLastPoint(view.x, view.y)
            lineTo(
                    parentView.x + (-view.width..parentView.width).random(),
                    parentView.y+ (-view.height..parentView.height).random()
            )
        }
        ObjectAnimator.ofFloat(view, View.X, View.Y, path).apply {
            duration = (minMoveTime..maxMoveTime).random().toLong()
            addListener(AnimationFinishedListener(view))
            start()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    inner class BubbleView(parent: ViewGroup): View(parent.context){
        init {
            background = parent.context.getDrawable(drawableId)
            background.setTint(parent.context.getColor(colorId))
            val wh = (minWidthHeight..maxWidthHeight).random()
            layoutParams = ViewGroup.LayoutParams(wh, wh)
            alpha = ((minAlpha..maxAlpha).random()).toFloat()/10
            elevation = -1F
            x = (-wh..parent.width).random().toFloat()
            y = (-wh..parent.height).random().toFloat()
        }
    }

    inner class AnimationFinishedListener(private val v: View) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            startMove(v)
        }
    }
}
