package personal.wuqing.randomz.picker

import java.util.*

val pickerItemList = mutableListOf<PickerItemView>()
var pickerItemCounter = 0

fun select() {
    val list = mutableListOf<PickerItemView>()
    for (view in pickerItemList) {
        if (!view.isChosen) {
            for (i in 0 until view.weight!!) {
                list.add(view)
                if (list.size > 10000)
                    throw SumOfWeightToLargeException()
            }
        }
    }
    if (list.isEmpty())
        throw NoAvailableItemException()
    list[Random().nextInt(list.size)].isChosen = true
}

fun unselectAll() {
    for (view in pickerItemList)
        view.isChosen = false
}

class NoAvailableItemException : Exception()

class SumOfWeightToLargeException : Exception()
