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
    val list = pickerHashSet.map { Pair(it.name, it.weight) }
    val id = System.currentTimeMillis().toString()
    val initialListList = try {
        val listListInputStream = ObjectInputStream(context.openFileInput("id_list"))
        val ret = listListInputStream.readObject() as? MutableList<*>
                ?: mutableListOf<String>()
        listListInputStream.close()
        ret
    } catch (e: IOException) {
        mutableListOf<String>()
    }
    val listList = mutableListOf<String>()
    for (item in initialListList)
        if (item is String)
            listList.add(item)
    listList.add(id)
    val listListOutputStream = ObjectOutputStream(context.openFileOutput("id_list", Context.MODE_PRIVATE))
    listListOutputStream.writeObject(listList)
    listListOutputStream.close()
    val nameOutputStream = context.openFileOutput("_$id", Context.MODE_PRIVATE)
    nameOutputStream.write(name.toByteArray())
    nameOutputStream.close()
    val outputStream = context.openFileOutput(id, Context.MODE_PRIVATE)
    val objectOutputStream = ObjectOutputStream(outputStream)
    objectOutputStream.writeObject(list)
    objectOutputStream.close()
}

val listList = mutableListOf<List<*>>()
fun pickerRead(context: Context, updatePickerView: () -> Unit) {
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
    listList.clear()
    for (id in idList)
        if (id is String)
            try {
                val radioButton = RadioButton(context)
                radioButton.id = listList.size
                val inputStream = context.openFileInput(id)
                listList.add(ObjectInputStream(inputStream).readObject() as List<*>)
                inputStream.close()
                val nameInputStream = context.openFileInput("_$id")
                val nameBytes = ByteArray(nameInputStream.available())
                nameInputStream.read(nameBytes)
                nameInputStream.close()
                val name = String(nameBytes)
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
            pickerHashSet.clear()
            val list = listList[group.checkedRadioButtonId]
            for (item in list)
                if (item is Pair<*, *> && item.first is String && item.second is Int?)
                    pickerHashSet.add(PickerItemView(context, item.first as String, item.second as Int?))
            updatePickerView()
        }
    }
    builder.show()
}
