package personal.wuqing.randomz.picker

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import personal.wuqing.randomz.R

class PickerItemView(context: Context, defaultName: String = "", defaultWeight: Int? = 1) : LinearLayout(context) {
    // EditText is used when unfrozen, while TextView is used when frozen
    private val editTextItemName = EditText(context)
    private val editTextItemWeight = EditText(context)
    private val layoutUnfrozen = LinearLayout(context)
    private val textViewItemName = TextView(context)
    private val textViewItemWeight = TextView(context)
    private val layoutFrozen = LinearLayout(context)
    var isChosen = false
        // when set, modify the text color of EditText and TextView
        set(value) {
            field = value

            editTextItemName.setTextColor(if (value) Color.RED else Color.BLACK)
            editTextItemWeight.setTextColor(if (value) Color.RED else Color.BLACK)
            textViewItemName.setTextColor(if (value) Color.RED else Color.BLACK)
            textViewItemWeight.setTextColor(if (value) Color.RED else Color.BLACK)
        }
    var isFrozen = false
        set(value) {
            field = value
            layoutFrozen.visibility = if (field) View.VISIBLE else View.GONE
            layoutUnfrozen.visibility = if (field) View.GONE else View.VISIBLE
        }
    var name: String = ""
        // when set, modify the text of EditText and TextView
        set(value) {
            field = value

            // set a tag to prevent endless loop
            editTextItemName.tag = Any()
            editTextItemName.setText(field)
            editTextItemName.tag = null

            textViewItemName.text = field
        }
    var weight: Int? = 1
        set(value) {
            field = value

            // set a tag to prevent endless loop
            editTextItemWeight.tag = Any()
            editTextItemWeight.setText(field?.toString() ?: "")
            editTextItemWeight.tag = null

            textViewItemWeight.text = field?.toString() ?: ""
        }

    // For Java interpol, meeting the requirement of android.view.View
    constructor(context: Context) : this(context, "")

    init {
        // set the properties of EditText and TextView, add it to layout
        textViewItemName.setTextColor(Color.BLACK)
        textViewItemWeight.setTextColor(Color.BLACK)

        layoutFrozen.addView(textViewItemName, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f))
        layoutFrozen.addView(textViewItemWeight, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

        editTextItemName.setHint(R.string.hint_item_name)
        editTextItemName.setSelectAllOnFocus(true)
        editTextItemName.setSingleLine()
        editTextItemName.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        if (editTextItemName.tag == null) {
                            name = s.toString()
                            unselectAll()
                        }
                    }
                }
        )

        editTextItemWeight.inputType = EditorInfo.TYPE_CLASS_NUMBER
        editTextItemWeight.setHint(R.string.hint_item_weight)
        editTextItemWeight.setSelectAllOnFocus(true)
        editTextItemWeight.setSingleLine()
        editTextItemWeight.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        if (editTextItemWeight.tag == null) {
                            weight = s.toString().toIntOrNull()
                            unselectAll()
                        }
                    }
                })

        layoutUnfrozen.addView(editTextItemName, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f))
        layoutUnfrozen.addView(editTextItemWeight, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        addView(layoutFrozen, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(layoutUnfrozen, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        name = defaultName
        weight = defaultWeight

        isFrozen = false
    }

    override fun setOnClickListener(l: OnClickListener?) {
        editTextItemName.setOnClickListener(l)
        editTextItemWeight.setOnClickListener(l)
    }
}
