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
    var selectedAlergy :List<String> = listOf()

    private var arrowDown: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down) as Drawable

    init {

        setButtonDrawables()
        val selectedAllergy: BooleanArray = arrayAllergy.map { false }.toBooleanArray()

        setOnClickListener { // Initialize alert dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            // set title
            builder.setTitle("Select Allergies")

            // set dialog non cancelable
            builder.setCancelable(false)

            builder.setMultiChoiceItems(
                arrayAllergy, selectedAllergy
            ) { _, position, checked ->
                // check condition
                if (checked) {
                    // when checkbox selected
                    // Add position  in lang list
                    listAllergy.add(position)
                    // Sort array list
                    listAllergy.sort()
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    listAllergy.remove(position)
                }
            }

            builder.setPositiveButton(
                "OK"
            ) { _, _ -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in listAllergy.indices) {
                    // concat array value
                    stringBuilder.append(arrayAllergy[listAllergy[j]])
                    // check condition
                    if (j != listAllergy.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                text = stringBuilder.toString()
                selectedAlergy = arrayAllergy.toList()

//                // set text on textView
//                textView.setText(stringBuilder.toString())
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, _ -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { _, _ ->
                // use for loop
                for (j in selectedAllergy.indices) {
                    // remove all selection
                    selectedAllergy[j] = false
                    // clear language list
                    listAllergy.clear()
                    // clear text view value
                    text = ""
                }
            }
            // show dialog
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