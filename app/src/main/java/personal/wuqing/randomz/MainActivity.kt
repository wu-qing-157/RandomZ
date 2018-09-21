package personal.wuqing.randomz

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_picker.*
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
            } catch (e: NullPointerException) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title_input_mismatch)
                builder.setMessage(e.toString())
                builder.show()
            }
        }
        button_picker_unselect_all.setOnClickListener {
            unselectAll()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener {
            scrollView_main.removeAllViews()
            scrollView_main.addView(when (it.itemId) {
                R.id.navigation_picker -> layout_picker
                else -> View(this)
            })
            true
        }
        initPickerView()
        navigation.selectedItemId = R.id.navigation_picker
    }
}
