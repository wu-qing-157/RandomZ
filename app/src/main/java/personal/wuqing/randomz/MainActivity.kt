package personal.wuqing.randomz

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import personal.wuqing.randomz.picker.*

class MainActivity : AppCompatActivity() {
    private fun updatePickerView() {
        layout_picker_items.removeAllViews()
        pickerItemList.forEach {
            (it.parent as? LinearLayout)?.removeView(it)
            layout_picker_items.addView(it)
        }
    }

    private fun initPickerView() {
        button_picker_add_item.setOnClickListener {
            val view = PickerItemView(this, "Item ${++pickerItemCounter}")
            pickerItemList.add(view)
            layout_picker_items.addView(view)
        }
        button_picker_remove_all.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.alert_title_picker_remove_all)
            builder.setPositiveButton(R.string.name_confirm) {_, _-> run {
                pickerItemList.clear()
                layout_picker_items.removeAllViews()
                pickerItemCounter = 0
            }}
            builder.setNegativeButton(R.string.name_cancel, null)
            builder.show()
        }
        button_picker_select_one.setOnClickListener {
            try {
                select()
            } catch (e: NoAvailableItemException) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title_picker_no_available_item)
                builder.setMessage(R.string.alert_description_picker_no_available_item)
                builder.show()
            } catch (e: SumOfWeightToLargeException) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title_picker_sum_of_weight_too_large)
                builder.setMessage(R.string.alert_description_picker_sum_of_weight_too_large)
                builder.show()
            } catch (e: NullPointerException) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title_picker_input_mismatch)
                builder.setMessage(R.string.alert_description_picker_input_mismatch)
                builder.show()
            }
        }
        checkBox_picker_freeze.setOnCheckedChangeListener { _, isChecked ->
            run {
                pickerItemList.forEach { it.isFrozen = isChecked }
                button_picker_add_item.visibility = if (isChecked) View.INVISIBLE else View.VISIBLE
            }
        }
        button_picker_unselect_all.setOnClickListener { unselectAll() }
        button_picker_save.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.alert_title_picker_save)
            val editTextName = EditText(this)
            editTextName.setHint(R.string.alert_hint_picker_save)
            editTextName.setSingleLine()
            builder.setView(editTextName)
            builder.setPositiveButton(R.string.name_confirm) { _, _ -> pickerSave(editTextName.text.toString(), this) }
            builder.setNegativeButton(R.string.name_cancel, null)
            builder.show()
        }
        button_picker_read.setOnClickListener { pickerRead(this, { checkBox_picker_freeze.isChecked = true }, this::updatePickerView) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener {
            scrollView_picker.visibility = if (it.itemId == R.id.navigation_picker) View.VISIBLE else View.GONE
            true
        }
        initPickerView()
    }

    override fun onResume() {
        super.onResume()
        updatePickerView()
    }
}
