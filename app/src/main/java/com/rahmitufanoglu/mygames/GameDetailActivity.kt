package com.rahmitufanoglu.mygames

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rahmitufanoglu.mygames.persistence.Game
import com.rahmitufanoglu.mygames.persistence.GameDatabase
import kotlinx.android.synthetic.main.activity_game_detail.*
import kotlinx.android.synthetic.main.progress_bar_default.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GameDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private lateinit var gameTitle: String
    private lateinit var gameDescription: String
    private lateinit var gameRelease: String
    private lateinit var gameImage: String
    private lateinit var gameWiki: String
    private lateinit var gameShop: String
    private lateinit var gameCheat1: String
    private lateinit var gameCheat2: String
    private lateinit var gameCheat3: String
    private lateinit var gameDatabase: GameDatabase

    private var scrollRange = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        gameDatabase = GameDatabase.getInstance(this)

        gameTitle = intent.getStringExtra(STRING_EXTRA_TITLE)
        gameDescription = intent.getStringExtra(STRING_EXTRA_DESCRIPTION)
        gameRelease = intent.getStringExtra(STRING_EXTRA_RELEASE)
        gameImage = intent.getStringExtra(STRING_EXTRA_IMAGE)
        gameWiki = intent.getStringExtra(STRING_EXTRA_WIKI)
        gameShop = intent.getStringExtra(STRING_EXTRA_SHOP)
        gameCheat1 = intent.getStringExtra(STRING_EXTRA_CHEAT_1)
        gameCheat2 = intent.getStringExtra(STRING_EXTRA_CHEAT_2)
        gameCheat3 = intent.getStringExtra(STRING_EXTRA_CHEAT_3)

        fab_favorites.setOnClickListener(this)
        fab_share.setOnClickListener(this)
        card_view_wiki.setOnClickListener(this)
        card_view_shop.setOnClickListener(this)

        setActionBar()
        setCollapsingToolbar()
        setContent()
    }

    override fun onDestroy() {
        gameDatabase.destroyInstance()
        super.onDestroy()

    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_game_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        animateFabSlideInLeft(this, fab_share)
        animateFabSlideInRight(this, fab_favorites)
    }

    private fun setCollapsingToolbar() {
        app_bar_game_detail.addOnOffsetChangedListener(this)

        Glide.with(this)
                .load(gameImage)
                .apply(RequestOptions()
                        .error(R.mipmap.ic_launcher)
                        .fallback(R.mipmap.ic_launcher))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>,
                                              isFirstResource: Boolean): Boolean {
                        progress_bar_default.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>,
                                                 dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        progress_bar_default.visibility = View.GONE
                        return false
                    }
                })
                .into(iv_game_detail)
    }

    private fun setContent() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val date = simpleDateFormat.parse(gameRelease)
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN)
        val relaseDate = dateFormat.format(date)

        tv_game_description.text = gameDescription
        tv_release_date.text = relaseDate
        tv_ceat_1.text = getString(R.string.cheat_1, ":\t", gameCheat1)
        tv_ceat_2.text = getString(R.string.cheat_2, ":\t", gameCheat2)
        tv_ceat_3.text = getString(R.string.cheat_3, ":\t", gameCheat3)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            scrollRange == -1 -> scrollRange = appBarLayout.totalScrollRange
            scrollRange + verticalOffset == 0 -> collapsing_toolbar_game_detail.title = gameTitle
            else -> collapsing_toolbar_game_detail.title = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab_favorites -> {
                addGameToFavorites(view, gameDatabase, Game(gameTitle, gameDescription,
                        gameRelease, gameImage, gameWiki, gameShop, gameCheat1, gameCheat2, gameCheat3))
            }
            R.id.fab_share -> {
                val sendIntent = Intent()
                sendIntent.run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, gameTitle + "\n" + gameShop)
                    type = "text/plain"
                }
                startActivity(sendIntent)
            }
            R.id.card_view_wiki -> setIntent(view, WikiActivity::class.java, STRING_EXTRA_WIKI, gameWiki)
            R.id.card_view_shop -> setIntent(view, ShopActivity::class.java, STRING_EXTRA_SHOP, gameShop)
        }
    }

    /**
     *  Add game to the SQLite database
     */
    private fun addGameToFavorites(view: View?, db: GameDatabase?, game: Game) {
        if (db!!.userDao().findGameTitle(game.title)!!.isEmpty()) {
            gameDatabase.userDao().insertGame(Game(game.title, game.description, game.release, game.image,
                    game.wiki, game.shop, game.cheat1, game.cheat2, game.cheat3))
            Snackbar.make(view!!, game.title + " " + getString(R.string.saved_to_favorites), Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(view!!, game.title + " " + getString(R.string.already_in_list), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setIntent(view: View, cls: Class<*>, extra: String, s: String) {
        val intent = Intent(view.context, cls)
        intent.putExtra(extra, s)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    companion object {
        private const val STRING_EXTRA_TITLE = "title"
        private const val STRING_EXTRA_DESCRIPTION = "description"
        private const val STRING_EXTRA_RELEASE = "release"
        private const val STRING_EXTRA_IMAGE = "image"
        private const val STRING_EXTRA_WIKI = "wiki"
        private const val STRING_EXTRA_SHOP = "shop"
        private const val STRING_EXTRA_CHEAT_1 = "cheat1"
        private const val STRING_EXTRA_CHEAT_2 = "cheat2"
        private const val STRING_EXTRA_CHEAT_3 = "cheat3"
    }
}
