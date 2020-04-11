package com.maxk.marvy.characters

import android.R
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.maxk.marvy.api.Complete
import com.maxk.marvy.api.Loading
import com.maxk.marvy.characters.view.MarvelCharacterInfoView
import com.maxk.marvy.characters.viewmodels.MarvelCharacterViewModel
import com.maxk.marvy.databinding.ActivityMarvelCharacterBinding
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.extensions.resolveAttribute
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterActivity : AppCompatActivity() {
    companion object {
        const val VIEW_NAME_CHARACTER_NAME: String = "marvel_character:name"

        private const val CHARACTER_EXTRA = "marvel_character_extra"

        fun start(context: Context?, character: MarvelCharacter, options: Bundle? = null) {
            context?.let {
                val intent = Intent(it, MarvelCharacterActivity::class.java)
                intent.putExtra(CHARACTER_EXTRA, character)
                ActivityCompat.startActivity(it, intent, options)
            }
        }
    }

    private val viewModel: MarvelCharacterViewModel by viewModels(
        factoryProducer = { MarvelCharacterViewModel.Factory(character) }
    )

    private val character: MarvelCharacter?
        get() = intent.getParcelableExtra(CHARACTER_EXTRA)

    private val binding: ActivityMarvelCharacterBinding by lazy {
        ActivityMarvelCharacterBinding.inflate(LayoutInflater.from(this))
    }

    private var isCharacterNameCardHidden: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.imageView.image = viewModel.image
        binding.descriptionSection.content<TextView> { text = viewModel.description }

        ViewCompat.setTransitionName(binding.characterName, VIEW_NAME_CHARACTER_NAME)
        setupActionBar()
        setupObservers()

        binding.appBar.addOnOffsetChangedListener(
            OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val appBarVerticalOffset = AppBarVerticalOffset(
                    verticalOffset,
                    maxVerticalOffset = appBarLayout.height - binding.toolbar.height
                )

                fadeInOutImage(appBarVerticalOffset)
                updateToolbar(appBarVerticalOffset)
                updateCharacterNameCard(appBarVerticalOffset)
            })

        binding.root.post { adjustScrollViewPadding(animated = false) }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        binding.collapsingToolbar.title = null
    }

    private fun setupObservers() {
        viewModel.additionalCharacterInfo.observe({ this.lifecycle }) { requestStatus ->
            binding.progressBar.isVisible = requestStatus is Loading

            if (requestStatus is Complete) {
                handleAdditionalCharacterInfoResult(requestStatus.result)
            }
        }
    }

    private fun handleAdditionalCharacterInfoResult(result: Result<List<CharacterInfo>>) {
        result.onSuccess { displayAdditionalCharacterInfo(it) }
    }

    private fun displayAdditionalCharacterInfo(characterInfo: List<CharacterInfo>) {
        characterInfo
            .map {MarvelCharacterInfoView(this, it) }
            .onEach {
                binding.characterInfoContainer.addView(it, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ))
            }
    }

    private fun adjustScrollViewPadding(animated: Boolean = true) = with(binding.nestedScrollView) {
        val additionalPaddingTop = binding.characterName.height / 2

        val animator = if (isCharacterNameCardHidden) {
            ValueAnimator.ofInt(paddingTop, paddingTop - additionalPaddingTop)
        } else {
            ValueAnimator.ofInt(paddingTop, paddingTop + additionalPaddingTop)
        }

        animator.addUpdateListener { valueAnimator ->
            setPadding(
                paddingLeft,
                valueAnimator.animatedValue as Int,
                paddingRight,
                paddingBottom
            )
        }

        if (!animated) {
            animator.duration = 0
        }

        animator.start()
    }

    private fun fadeInOutImage(offset: AppBarVerticalOffset) {
        binding.imageView.alpha = 1 - offset.delta
    }

    private fun updateToolbar(offset: AppBarVerticalOffset) {
        val backgroundColor = theme.resolveAttribute(R.attr.colorPrimary)
        val backgroundDrawable = ColorDrawable(backgroundColor).mutate()
        val alpha = (255 * offset.delta).toInt()
        backgroundDrawable.alpha = alpha
        binding.toolbar.background = backgroundDrawable
    }

    private fun updateCharacterNameCard(offset: AppBarVerticalOffset) {
        val animator = ViewCompat.animate(binding.characterNameCard)

        if (offset.delta >= 0.85) {
            if (!isCharacterNameCardHidden) {
                isCharacterNameCardHidden = true

                adjustScrollViewPadding()

                animator
                    .scaleY(0f)
                    .scaleX(0f)
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationEnd(view: View?) {
                            binding.collapsingToolbar.title = viewModel.title
                            animator.setListener(null)
                        }
                    })
                    .start()
            }
        } else {
            if (isCharacterNameCardHidden) {
                isCharacterNameCardHidden = false
                binding.collapsingToolbar.title = null

                adjustScrollViewPadding()

                ViewCompat.animate(binding.characterNameCard)
                    .scaleY(1f)
                    .scaleX(1f)
                    .start()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finishAfterTransition()
        }
        return true
    }
}

private class AppBarVerticalOffset(verticalOffset: Int, maxVerticalOffset: Int) {
    val delta: Float = 1 - ((verticalOffset + maxVerticalOffset) / maxVerticalOffset.toFloat())
}
