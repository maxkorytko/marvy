package com.maxk.marvy.characters

import android.R
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.appbar.AppBarLayout
import com.maxk.marvy.api.Complete
import com.maxk.marvy.api.Loading
import com.maxk.marvy.characters.view.MarvelCharacterExpandableInfoView
import com.maxk.marvy.characters.view.MarvelCharacterInfoView
import com.maxk.marvy.characters.viewmodels.MarvelCharacterViewModel
import com.maxk.marvy.databinding.MarvelCharacterBinding
import com.maxk.marvy.extensions.resolveAttribute
import com.maxk.marvy.model.CharacterInfo
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharacterFragment : Fragment() {
    companion object {
        private const val CHARACTER = "marvel_character"

        fun newInstance(character: MarvelCharacter?): MarvelCharacterFragment {
            return MarvelCharacterFragment().apply {
                arguments = Bundle().apply { putParcelable(CHARACTER, character) }
            }
        }
    }

    private lateinit var binding: MarvelCharacterBinding

    private val character: MarvelCharacter? get() = arguments?.run {
        getParcelable(CHARACTER) ?: MarvelCharacterFragmentArgs.fromBundle(this).character
    }

    private val viewModel: MarvelCharacterViewModel by viewModels(
        factoryProducer = { MarvelCharacterViewModel.Factory(character) }
    )

    private var isCharacterNameCardHidden: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarvelCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.imageView.image = viewModel.image
        binding.descriptionSection.content<TextView> { text = viewModel.description }

        setupActionBar()
        setupObservers()

        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
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
        val marvelCharacterActivity = activity as? MarvelCharacterActivity

        if (marvelCharacterActivity != null) {
            setupActionBarWithActivity(marvelCharacterActivity)
        } else {
            binding.collapsingToolbar.setupWithNavController(
                binding.toolbar,
                findNavController()
            )
        }

        binding.collapsingToolbar.title = null
    }

    private fun  setupActionBarWithActivity(activity: MarvelCharacterActivity) = activity.run {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
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
        val characterInfoViews = mutableListOf<View>()

        if (characterInfo.size == 1) {
            characterInfoViews.add(MarvelCharacterInfoView(requireActivity(), characterInfo[0]))
        } else {
            characterInfoViews.addAll(
                characterInfo.map { MarvelCharacterExpandableInfoView(requireActivity(), it) }
            )
        }

        with(binding.characterInfoContainer) {
            TransitionManager.beginDelayedTransition(this, TransitionSet().apply {
                addTransition(Slide(Gravity.BOTTOM))
                addTransition(Fade())
            })

            characterInfoViews.onEach {
                addView(it, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ))
            }
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
        val backgroundColor = requireActivity().theme.resolveAttribute(R.attr.colorPrimary)
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
                            if (isCharacterNameCardHidden) {
                                binding.collapsingToolbar.title = viewModel.title
                            }
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


}

private class AppBarVerticalOffset(verticalOffset: Int, maxVerticalOffset: Int) {
    val delta: Float = 1 - ((verticalOffset + maxVerticalOffset) / maxVerticalOffset.toFloat())
}
