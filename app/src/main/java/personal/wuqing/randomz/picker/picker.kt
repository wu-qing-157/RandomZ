package personal.wuqing.randomz.picker

import android.widget.LinearLayout
import java.util.*

val pickerHashSet = HashSet<PickerItemView>()
var pickerItemCounter = 0

fun select() {
    val list = mutableListOf<PickerItemView>()
    for (view in pickerHashSet) {
        if (!view.chosen) {
            for (i in 0 until view.weight!!) {
                list.add(view)
                if (list.size > 10000)
                    throw SumOfWeightToLargeException()
            }
        }
    }
    if (list.isEmpty())
        throw NoAvailableItemException()
    list[Random().nextInt(list.size)].chosen = true
}

fun unselectAll() {
    for (view in pickerHashSet)
        view.chosen = false
}

class NoAvailableItemException : Exception()

class SumOfWeightToLargeException : Exception()
