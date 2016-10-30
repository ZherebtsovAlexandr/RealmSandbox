package mansonheart.com.realmsandbox

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by alexandr on 30.10.16.
 */
class LoggerView : View {

    var canvas: Canvas? = null
    val canvasPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        this.canvas = canvas
        canvasPaint.color = Color.parseColor("#3C3F51B5")
        canvas?.drawRect(0f, 0f, canvas?.width.toFloat(), canvas?.height.toFloat(), canvasPaint);
        super.onDraw(canvas)

        canvasPaint.color = Color.RED
        canvasPaint.textSize = 32f

        //canvas?.drawText("Hello logger", 50f, 200f, canvasPaint)

    }

    fun drawText(text: String) {
        canvas?.drawText(text, 50f, 200f, canvasPaint)
    }


}
