package com.rahmitufanoglu.mygames.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import androidx.core.widget.toast
import com.rahmitufanoglu.mygames.MainApplication
import com.rahmitufanoglu.mygames.R
import com.rahmitufanoglu.mygames.R.id.*
import com.rahmitufanoglu.mygames.compareReleaseGame
import com.rahmitufanoglu.mygames.compareTitleGame
import com.rahmitufanoglu.mygames.persistence.Game
import com.rahmitufanoglu.mygames.persistence.GameDatabase
import kotlinx.android.synthetic.main.dialog_delete_all.view.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import java.util.*

class FavoritesFragment : Fragment() {

    private var gameDatabase: GameDatabase? = null
    private var gamesList: ArrayList<Game>? = null
    private var sortOption: Int? = 1 // Default sorting by release date
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDatabase()
        setRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainApplication.getRefWatcher(activity!!).watch(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        val searchItem = menu?.findItem(action_menu_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.run {
            queryHint = getString(R.string.game_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        favoritesAdapter.setFilter(gamesList!!)
                    } else {
                        val tmp = ArrayList<Game>()
                        for (game in gamesList!!) {
                            val gameTitle = game.title!!.toLowerCase()
                            if (gameTitle.toLowerCase().contains(newText)) {
                                tmp.add(game)
                            }
                        }
                        favoritesAdapter.setFilter(tmp)
                    }
                    return false
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_delete_all -> deleteAllGamesFromDb()
            R.id.menu_release_date_sort -> {
                if (gamesList != null) {
                    Collections.sort(gamesList, compareReleaseGame())
                    favoritesAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 1
                } else {
                    context?.toast(getString(R.string.nothing_to_sort))
                }
            }
            R.id.menu_alphabetical_sort -> {
                if (gamesList != null) {
                    Collections.sort(gamesList, compareTitleGame())
                    favoritesAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 2
                } else {
                    context?.toast(getString(R.string.nothing_to_sort))
                }
            }
            R.id.menu_shuffle -> {
                if (gamesList != null) {
                    gamesList?.shuffle()
                    favoritesAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 3
                } else {
                    context?.toast(getString(R.string.nothing_to_sort))
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setDatabase() {
        gameDatabase = GameDatabase.getInstance(activity!!)
        gamesList = gameDatabase!!.userDao().getAllGames() as ArrayList<Game>?
        Collections.sort(gamesList, compareReleaseGame())

        if (gamesList!!.isEmpty()) {
            text_view_favorite.visibility = View.VISIBLE
        } else {
            text_view_favorite.visibility = View.GONE
        }
    }

    private fun setRecyclerView() {
        favoritesAdapter = FavoritesAdapter(activity!!, gamesList!!, this)
        recycler_view_favorites.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = favoritesAdapter
            setHasFixedSize(true)
            smoothScrollToPosition(0)
        }
    }

    @SuppressLint("InflateParams")
    private fun deleteAllGamesFromDb() {
        val dialog = LayoutInflater.from(activity)
        val dialogView = dialog.inflate(R.layout.dialog_delete_all, null)

        val btnNo = dialogView.btn_no_delete_all
        val btnYes = dialogView.btn_yes_delete_all

        val alertDialog = AlertDialog.Builder(activity!!)
                .setView(dialogView)
                .show()

        btnNo.setOnClickListener { _ ->
            alertDialog.dismiss()
        }

        btnYes.setOnClickListener { _ ->
            if (!gamesList!!.isEmpty()) {
                gameDatabase!!.userDao().deleteAllGames()
                recycler_view_favorites.run {
                    removeAllViewsInLayout()
                    invalidate()
                }
                alertDialog.dismiss()
                Snackbar.make(view!!, context!!.getString(R.string.delete_complete), Snackbar.LENGTH_SHORT).show()
                setDatabase()
            } else {
                alertDialog.dismiss()
                Snackbar.make(view!!, context!!.getString(R.string.nothing_to_delete), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
