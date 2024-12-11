package com.example.nyam.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.nyam.R


class customDropDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {


    private var listAllergy: ArrayList<Int> = ArrayList()
    var arrayAllergy: Array<String> = resources.getStringArray(R.array.allergies)
    var selectedAlergy: ArrayList<String> = ArrayList()

    private var arrowDown: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_down) as Drawable

    init {

        setButtonDrawables()
        val selectedAllergy: BooleanArray = arrayAllergy.map { false }.toBooleanArray()

        setOnClickListener { // Initialize alert dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            builder.setTitle("Select Allergies")

            builder.setCancelable(false)

            builder.setMultiChoiceItems(
                arrayAllergy, selectedAllergy
            ) { _, position, checked ->
                if (checked) {
                    listAllergy.add(position)
                    listAllergy.sort()
                } else {
                    listAllergy.remove(position)
                }
            }

            builder.setPositiveButton(
                "OK"
            ) { _, _ ->
                val stringBuilder = StringBuilder()
                for (j in listAllergy.indices) {
                    stringBuilder.append(arrayAllergy[listAllergy[j]])
                    selectedAlergy.add(arrayAllergy[listAllergy[j]].replace(" ","-").lowercase() + "-free")
                    if (j != listAllergy.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                text = stringBuilder.toString()
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { _, _ ->

                for (j in selectedAllergy.indices) {

                    selectedAllergy[j] = false
                    selectedAlergy.clear()
                    listAllergy.clear()

                    text = ""
                }
            }

            builder.show()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = arrowDown,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }
}