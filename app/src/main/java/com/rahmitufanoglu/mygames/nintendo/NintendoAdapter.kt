package com.rahmitufanoglu.mygames.nintendo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rahmitufanoglu.mygames.GameDetailActivity
import com.rahmitufanoglu.mygames.R
import com.rahmitufanoglu.mygames.getDateGerman
import com.rahmitufanoglu.mygames.model.Nintendo
import kotlinx.android.synthetic.main.dialog_info.view.*
import kotlinx.android.synthetic.main.list_item_games.view.*
import kotlinx.android.synthetic.main.progress_bar_default.view.*

class NintendoAdapter(private val context: Context,
                      private var gamesList: MutableList<Nintendo>)
    : RecyclerView.Adapter<NintendoAdapter.NintendoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NintendoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_games, parent, false)
        return NintendoViewHolder(view)
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: NintendoViewHolder, position: Int) {
        val (_, title, description, release, image, wiki, shop, cheat1, cheat2, cheat3) = gamesList[position]

        holder.tvCardGameTitle.text = title

        Glide.with(context)
                .load(image)
                .apply(RequestOptions()
                        .error(R.mipmap.ic_launcher)
                        .fallback(R.mipmap.ic_launcher))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>,
                                              isFirstResource: Boolean): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable>,
                                                 dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.ivCardGame)

        holder.cardView.run {
            setOnClickListener { view ->
                val intent = Intent(view.context, GameDetailActivity::class.java)
                intent.run {
                    putExtra(STRING_EXTRA_TITLE, title)
                    putExtra(STRING_EXTRA_DESCRIPTION, description)
                    putExtra(STRING_EXTRA_RELEASE, release)
                    putExtra(STRING_EXTRA_IMAGE, image)
                    putExtra(STRING_EXTRA_WIKI, wiki)
                    putExtra(STRING_EXTRA_SHOP, shop)
                    putExtra(STRING_EXTRA_CHEAT_1, cheat1)
                    putExtra(STRING_EXTRA_CHEAT_2, cheat2)
                    putExtra(STRING_EXTRA_CHEAT_3, cheat3)
                }
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }
            setOnLongClickListener { view ->
                val dialog = LayoutInflater.from(view.context)
                val dialogView = dialog.inflate(R.layout.dialog_info, null)

                val tvTitle = dialogView.tv_title_dialog
                val tvReleaseDate = dialogView.tv_release_date_dialog

                tvTitle.text = title
                tvReleaseDate.text = getDateGerman(release!!)

                AlertDialog.Builder(view.context)
                        .setView(dialogView)
                        .show()

                true
            }
        }
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    fun setFilter(gamesList: ArrayList<Nintendo>) {
        this.gamesList = gamesList
        notifyDataSetChanged()
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

    inner class NintendoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.list_item_card
        val ivCardGame: ImageView = view.iv_card_game
        val tvCardGameTitle: TextView = view.tv_card_game_title
        val progressBar: ProgressBar = view.progress_bar_default
    }
}
