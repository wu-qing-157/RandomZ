package personal.wuqing.randomz.picker

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import personal.wuqing.randomz.R

class PickerItemView(context: Context, defaultName: String) : LinearLayout(context) {
    private var textItemName: EditText
    private var textItemWeight: EditText
    private var name = ""
    var weight: Int? = 1
    var chosen = false
        set(value) {
            field = value
            textItemName.setTextColor(if (value) Color.RED else Color.BLACK)
            textItemWeight.setTextColor(if (value) Color.RED else Color.BLACK)
        }

    constructor(context: Context) : this(context, context.getString(R.string.name_item_0))

    init {
        this.orientation = HORIZONTAL
        textItemName = EditText(context)
        textItemName.setHint(R.string.hint_item_name)
        textItemName.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        name = s.toString()
                        unselectAll()
                    }
                }
        )
        textItemName.setText(defaultName)
        textItemName.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus && textItemName.text.toString().matches(Regex("Item [0-9]+")))
                    textItemName.setText("")
            }
        }
        this.addView(textItemName, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f))
        textItemWeight = EditText(context)
        textItemWeight.inputType = EditorInfo.TYPE_CLASS_NUMBER
        textItemWeight.setText(weight.toString())
        textItemWeight.setHint(R.string.hint_item_weight)
        textItemWeight.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        weight = s.toString().toIntOrNull()
                        unselectAll()
                    }
                })
        this.addView(textItemWeight, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
    }

    override fun setOnClickListener(l: OnClickListener?) {
        textItemName.setOnClickListener(l)
        textItemWeight.setOnClickListener(l)
    }

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = this === other
}
