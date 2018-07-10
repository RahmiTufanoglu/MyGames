package com.rahmitufanoglu.mygames.nintendo

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.widget.toast
import com.rahmitufanoglu.mygames.*
import com.rahmitufanoglu.mygames.R.id.action_menu_search
import com.rahmitufanoglu.mygames.api.GameClient
import com.rahmitufanoglu.mygames.api.GameService
import com.rahmitufanoglu.mygames.model.GameDataRepo
import com.rahmitufanoglu.mygames.model.Nintendo
import com.rahmitufanoglu.mygames.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_platform.*
import kotlinx.android.synthetic.main.app_bar_default.*
import kotlinx.android.synthetic.main.progress_bar_default.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NintendoActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var gamesList: ArrayList<Nintendo>? = null
    private var sortOption: Int = 1 // Default sorting by release date
    private lateinit var nintendoAdapter: NintendoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_platform)

        swipe_refresh_layout_platform.run {
            setOnRefreshListener(this@NintendoActivity)
            setColorSchemeResources(R.color.colorAccent)
        }

        setToolbar()
        setRecyclerView(this, recycler_view, NUM_COLUMNS)
        fetchJson()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_default)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.nintendo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_platform, menu)

        val searchItem = menu.findItem(action_menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.run {
            queryHint = getString(R.string.game_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        nintendoAdapter.setFilter(gamesList!!)
                    } else {
                        val tmp = ArrayList<Nintendo>()
                        for (game in gamesList!!) {
                            val gameTitle = game.title!!.toLowerCase()
                            if (gameTitle.contains(newText)) {
                                tmp.add(game)
                            }
                        }
                        nintendoAdapter.setFilter(tmp)
                    }
                    return false
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
            R.id.menu_release_date_sort -> {
                if (gamesList != null) {
                    Collections.sort(gamesList, compareRelease())
                    nintendoAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 1
                } else {
                    applicationContext?.toast(getString(R.string.nothing_to_sort))
                }
            }
            R.id.menu_alphabetical_sort -> {
                if (gamesList != null) {
                    Collections.sort(gamesList, compareTitle())
                    nintendoAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 2
                } else {
                    applicationContext?.toast(getString(R.string.nothing_to_sort))
                }
            }
            R.id.menu_shuffle -> {
                if (gamesList != null) {
                    gamesList?.shuffle()
                    nintendoAdapter.notifyDataSetChanged()
                    item.isChecked = true
                    sortOption = 3
                } else {
                    applicationContext?.toast(getString(R.string.nothing_to_sort))
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchJson() {
        val service = GameClient.createService(GameService::class.java)
        val call = service.getGames()

        call.enqueue(object : Callback<GameDataRepo> {
            override fun onResponse(call: Call<GameDataRepo>, response: Response<GameDataRepo>) {
                if (response.isSuccessful) {
                    gamesList = response.body()!!.nintendo as ArrayList<Nintendo>
                    nintendoAdapter = NintendoAdapter(this@NintendoActivity, gamesList!!)
                    when (sortOption) {
                        1 -> Collections.sort(gamesList, compareRelease())
                        2 -> Collections.sort(gamesList, compareTitle())
                        3 -> gamesList?.shuffle()
                    }
                    recycler_view.adapter = nintendoAdapter
                } else {
                    applicationContext?.toast(getString(R.string.status_code) + ": \$statusCode")
                }

                swipe_refresh_layout_platform.isRefreshing = false
                progress_bar_default.visibility = View.GONE
            }

            override fun onFailure(call: Call<GameDataRepo>, t: Throwable) {
                if (call.isCanceled) {
                    applicationContext?.toast(getString(R.string.failed_request))
                } else {
                    applicationContext?.toast(t.message!!)
                }

                swipe_refresh_layout_platform.isRefreshing = false
                recycler_view.adapter = EmptyAdapter()
                progress_bar_default.visibility = View.GONE
            }
        })
    }

    override fun onRefresh() {
        if (NetworkUtil.isNetworkConnected(this)) {
            applicationContext?.toast(getString(R.string.refreshing))
            fetchJson()
        } else {
            NetworkUtil.isNetworkConnected(this)
            applicationContext?.toast(getString(R.string.device_not_connected))
            swipe_refresh_layout_platform.isRefreshing = false
            progress_bar_default.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    companion object {
        private const val NUM_COLUMNS = 2
    }
}
