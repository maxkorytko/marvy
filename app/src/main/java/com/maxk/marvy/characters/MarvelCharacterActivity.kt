package com.maxk.marvy.characters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.maxk.marvy.R
import com.maxk.marvy.databinding.ActivityMarvelCharacterBinding
import com.maxk.marvy.extensions.overrideEnterTransition
import com.maxk.marvy.extensions.overrideExitTransition
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharacterActivity : AppCompatActivity() {
    companion object {
        private const val CHARACTER_EXTRA = "marvel_character_extra"

        fun start(context: Context?, character: MarvelCharacter, options: Bundle? = null) {
            context?.let {
                val intent = Intent(it, MarvelCharacterActivity::class.java)
                intent.putExtra(CHARACTER_EXTRA, character)
                ActivityCompat.startActivity(it, intent, options)
            }

            (context as? Activity)?.let {
                it.overrideEnterTransition()
            }
        }
    }

    private val character: MarvelCharacter?
        get() = intent.getParcelableExtra(CHARACTER_EXTRA)

    private val characterFragment: MarvelCharacterFragment by lazy {
        MarvelCharacterFragment.newInstance(character)
    }

    private val binding: ActivityMarvelCharacterBinding by lazy {
        ActivityMarvelCharacterBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.marvelCharacterFragmentContainer, characterFragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun finish() {
        super.finish()
        overrideExitTransition()
    }
}
