package com.rahmitufanoglu.mygames.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahmitufanoglu.mygames.GameDetailActivity
import com.rahmitufanoglu.mygames.R
import com.rahmitufanoglu.mygames.getDateGerman
import com.rahmitufanoglu.mygames.persistence.Game
import com.rahmitufanoglu.mygames.persistence.GameDatabase
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.dialog_info.view.*
import kotlinx.android.synthetic.main.list_item_favorites.view.*

class FavoritesAdapter(private val context: Context,
                       private var gamesList: MutableList<Game>,
                       private var fragment: FavoritesFragment)
    : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private lateinit var gameDatabase: GameDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_favorites, parent, false)
        return FavoritesViewHolder(view)
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: FavoritesAdapter.FavoritesViewHolder, position: Int) {
        val games = gamesList[position]

        gameDatabase = GameDatabase.getInstance(context)

        holder.tvCardGameTitle.text = games.title

        Glide.with(context)
                .load(games.image)
                .apply(RequestOptions()
                        .error(R.mipmap.ic_launcher)
                        .fallback(R.mipmap.ic_launcher))
                .into(holder.ivCardGame)

        holder.cardView.run {
            setOnClickListener { view ->
                val intent = Intent(view.context, GameDetailActivity::class.java)
                intent.run {
                    putExtra(STRING_EXTRA_TITLE, games.title)
                    putExtra(STRING_EXTRA_DESCRIPTION, games.description)
                    putExtra(STRING_EXTRA_RELEASE, games.release)
                    putExtra(STRING_EXTRA_IMAGE, games.image)
                    putExtra(STRING_EXTRA_WIKI, games.wiki)
                    putExtra(STRING_EXTRA_SHOP, games.shop)
                    putExtra(STRING_EXTRA_CHEAT_1, games.cheat1)
                    putExtra(STRING_EXTRA_CHEAT_2, games.cheat2)
                    putExtra(STRING_EXTRA_CHEAT_3, games.cheat3)
                }
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }
            setOnLongClickListener { view ->
                val dialog = LayoutInflater.from(view.context)
                val dialogView = dialog.inflate(R.layout.dialog_info, null)

                val tvTitle = dialogView.tv_title_dialog
                val tvReleaseDate = dialogView.tv_release_date_dialog

                tvTitle.text = games.title
                tvReleaseDate.text = getDateGerman(games.release!!)

                AlertDialog.Builder(view.context)
                        .setView(dialogView)
                        .show()

                true
            }
        }

        holder.icCardDelete.run {
            setOnClickListener { view ->
                val dialog = LayoutInflater.from(view.context)
                val dialogView = dialog.inflate(R.layout.dialog_delete, null)

                val btnNo = dialogView.btn_no_delete_game
                val btnYes = dialogView.btn_yes_delete_game

                val alertDialog = AlertDialog.Builder(view.context)
                        .setView(dialogView)
                        .show()

                btnNo.setOnClickListener { _ ->
                    alertDialog.dismiss()
                }

                btnYes.setOnClickListener { _ ->
                    deleteGame(view, games)
                    alertDialog.dismiss()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    fun setFilter(gamesList: ArrayList<Game>) {
        this.gamesList = gamesList
        notifyDataSetChanged()
    }

    private fun deleteGame(view: View?, games: Game?) {
        gameDatabase.userDao().deleteGame(games)
        val currentPosition = gamesList.indexOf(games)
        gamesList.removeAt(currentPosition)
        notifyItemRemoved(currentPosition)
        notifyItemRangeChanged(currentPosition, gamesList.size)
        Snackbar.make(view!!, games!!.title + " " + context.getString(R.string.deleted_from_favorites), Snackbar.LENGTH_SHORT).show()
        fragment.setDatabase()
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

    inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.list_item_card
        val ivCardGame: ImageView = view.iv_card_game
        val icCardDelete: ImageView = view.iv_card_delete
        val tvCardGameTitle: TextView = view.tv_card_game_title
    }
}
