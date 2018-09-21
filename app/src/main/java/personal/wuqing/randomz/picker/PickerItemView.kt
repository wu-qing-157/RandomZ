package personal.wuqing.randomz.picker

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import personal.wuqing.randomz.R

class PickerItemView(context: Context, defaultName: String = "", defaultWeight: Int = 1) : LinearLayout(context) {
    private var editTextItemName: EditText
    private var editTextItemWeight: EditText
    private var textViewItemName: TextView
    private var textViewItemWeight: TextView
    private var name = ""
    var weight: Int? = 1
    var chosen = false
        set(value) {
            field = value
            editTextItemName.setTextColor(if (value) Color.RED else Color.BLACK)
            editTextItemWeight.setTextColor(if (value) Color.RED else Color.BLACK)
            textViewItemName.setTextColor(if (value) Color.RED else Color.BLACK)
            textViewItemWeight.setTextColor(if (value) Color.RED else Color.BLACK)
        }

    constructor(context: Context) : this(context, "")

    fun freeze() {
        this.removeAllViews()
        this.addView(textViewItemName, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f))
        this.addView(textViewItemWeight, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        textViewItemName.text = name
        textViewItemWeight.text = weight?.toString() ?: ""
    }

    fun unfreeze() {
        this.removeAllViews()
        this.addView(editTextItemName, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f))
        this.addView(editTextItemWeight, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        editTextItemName.setText(name)
        editTextItemWeight.setText(weight?.toString() ?: "")
    }

    init {
        name = defaultName
        weight = defaultWeight
        textViewItemName = TextView(context)
        textViewItemName.setTextColor(Color.BLACK)
        textViewItemWeight = TextView(context)
        textViewItemWeight.setTextColor(Color.BLACK)
        this.orientation = HORIZONTAL
        editTextItemName = EditText(context)
        editTextItemName.setHint(R.string.hint_item_name)
        editTextItemName.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        name = s.toString()
                        unselectAll()
                    }
                }
        )
        editTextItemName.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus && editTextItemName.text.toString().matches(Regex("Item [0-9]+")))
                    editTextItemName.setText("")
            }
        }
        editTextItemWeight = EditText(context)
        editTextItemWeight.inputType = EditorInfo.TYPE_CLASS_NUMBER
        editTextItemWeight.setHint(R.string.hint_item_weight)
        editTextItemWeight.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        weight = s.toString().toIntOrNull()
                        unselectAll()
                    }
                })
        unfreeze()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        editTextItemName.setOnClickListener(l)
        editTextItemWeight.setOnClickListener(l)
    }

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = this === other
}
