package personal.wuqing.randomz

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import personal.wuqing.randomz.picker.*

class MainActivity : AppCompatActivity() {
    private fun initPickerView() {
        button_picker_add_item.setOnClickListener {
            val view = PickerItemView(this, "Item ${++pickerItemCounter}")
            pickerHashSet.add(view)
            layout_picker_items.addView(view)
        }
        button_picker_remove_all.setOnClickListener {
            pickerHashSet.clear()
            layout_picker_items.removeAllViews()
            pickerItemCounter = 0
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
                builder.setMessage(e.toString())
                builder.show()
            }
        }

        button_picker_unselect_all.setOnClickListener { unselectAll() }
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
        layout_picker_items.removeAllViews()
        for (view in pickerHashSet)
            layout_picker_items.addView(view)
    }
}
