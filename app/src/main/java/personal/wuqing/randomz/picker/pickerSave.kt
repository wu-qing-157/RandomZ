package personal.wuqing.randomz.picker

import android.app.AlertDialog
import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import personal.wuqing.randomz.R
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun pickerSave(name: String, context: Context) {
    val list = pickerItemList.map { Pair(it.name, it.weight) }
    val id = System.currentTimeMillis().toString()

    // read current list
    val initialListList = try {
        val listListInputStream = ObjectInputStream(context.openFileInput("id_list"))
        val ret = listListInputStream.readObject() as? MutableList<*>
                ?: mutableListOf<String>()
        listListInputStream.close()
        ret
    } catch (e: IOException) {
        mutableListOf<String>()
    }

    // generate the result list
    val listList = mutableListOf<String>()
    for (item in initialListList)
        if (item is String)
            listList.add(item)
    listList.add(id)

    // output the result list
    val listListOutputStream = ObjectOutputStream(context.openFileOutput("id_list", Context.MODE_PRIVATE))
    listListOutputStream.writeObject(listList)
    listListOutputStream.close()

    // output the name
    val nameOutputStream = context.openFileOutput("name_$id", Context.MODE_PRIVATE)
    nameOutputStream.write(name.toByteArray())
    nameOutputStream.close()

    // output the content of the item
    val outputStream = context.openFileOutput("content_$id", Context.MODE_PRIVATE)
    val objectOutputStream = ObjectOutputStream(outputStream)
    objectOutputStream.writeObject(list)
    objectOutputStream.close()
}

fun pickerRead(context: Context, updatePickerView: () -> Unit) {
    // read the idList from file
    val idList = try {
        val idListInputStream = ObjectInputStream(context.openFileInput("id_list"))
        val ret = idListInputStream.readObject() as? MutableList<*> ?: mutableListOf<String>()
        idListInputStream.close()
        ret
    } catch (e: Exception) {
        mutableListOf<String>()
    }

    val group = RadioGroup(context)
    group.orientation = RadioGroup.VERTICAL
    val pickerList = mutableListOf<List<*>>()
    for (id in idList)
        if (id is String)
            try {
                // read the content of this item
                val inputStream = context.openFileInput("content_$id")
                pickerList.add(ObjectInputStream(inputStream).readObject() as List<*>)
                inputStream.close()

                // read the name of this item
                val nameInputStream = context.openFileInput("name_$id")
                val nameBytes = ByteArray(nameInputStream.available())
                nameInputStream.read(nameBytes)
                nameInputStream.close()
                val name = String(nameBytes)

                val radioButton = RadioButton(context)
                radioButton.id = pickerList.size - 1

                radioButton.text = name
                group.addView(radioButton)
            } catch (e: IOException) {
                // do nothing
            }

    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.alert_title_picker_read)
    builder.setView(group)
    builder.setPositiveButton(R.string.name_confirm) { _, _ ->
        run {
            if (group.checkedRadioButtonId < 0)
                return@run
            pickerItemList.clear()
            val list = pickerList[group.checkedRadioButtonId]
            for (item in list)
                if (item is Pair<*, *> && item.first is String && item.second is Int?)
                    pickerItemList.add(PickerItemView(context, item.first as String, item.second as Int?))
            updatePickerView()
        }
    }
    builder.show()
}
