package com.maxk.marvy.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import com.maxk.marvy.R
import com.maxk.marvy.databinding.ActivityMarvelCharacterBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharacterActivity : AppCompatActivity() {
    companion object {
        private const val CHARACTER_EXTRA = "marvel_character_extra"

        fun start(context: Context?, character: MarvelCharacter) {
            context?.let {
                val intent = Intent(it, MarvelCharacterActivity::class.java)
                intent.putExtra(CHARACTER_EXTRA, character)
                ActivityCompat.startActivity(it, intent, null)
            }
        }
    }

    private val binding: ActivityMarvelCharacterBinding by lazy {
        ActivityMarvelCharacterBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        intent.getParcelableExtra<MarvelCharacter>(CHARACTER_EXTRA)?.let(::bind)
    }

    private fun bind(character: MarvelCharacter) {
        binding.collapsingToolbar.title = character.name
    }
}
