package com.maxk.marvy.characters.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharacterInfoSectionViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.extensions.resolveAttribute

class MarvelCharacterInfoSectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        MarvelCharacterInfoSectionViewBinding.inflate(layoutInflater, this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MarvelCharacterInfoSectionView,
            defStyleAttr,
            0
        ).apply {
            try {
                binding.sectionView.title = getString(R.styleable.MarvelCharacterInfoSectionView_title)
            } finally {
                recycle()
            }
        }
    }

    fun addTextRow(@StringRes title: Int, text: String) {
        addRow(TextRowView(context).also {
            it.title = resources.getString(title)
            it.text = text
        })
    }

    private fun addRow(row: View) {
        binding.sectionView.content<LinearLayout> {
            addView(row, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        }
    }
}

// region TextRowView Class

private class TextRowView(context: Context) : LinearLayout(context) {
    private val titleTextView = TextView(context).apply {
        gravity = Gravity.START
        setTextAppearance(context.theme.resolveAttribute(R.attr.textAppearanceBody1))
    }

    private val textView = TextView(context).apply {
        gravity = Gravity.END
        setTextAppearance(context.theme.resolveAttribute(R.attr.textAppearanceBody1))
    }

    var title: CharSequence
        get() = titleTextView.text
        set(value) { titleTextView.text = value }

    var text: CharSequence
        get() = textView.text
        set(value) { textView.text = value }

    init {
        orientation = HORIZONTAL

        addView(titleTextView, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            1f
        ))

        addView(textView, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            2f
        ))
    }
}

// endregion
