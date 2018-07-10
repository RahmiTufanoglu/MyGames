package com.rahmitufanoglu.mygames.main

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import androidx.core.widget.toast
import com.rahmitufanoglu.mygames.DepthPageTransformer
import com.rahmitufanoglu.mygames.R
import com.rahmitufanoglu.mygames.R.id.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        setViewPager()
        setTabLayout()
    }

    /**
     *  not best practise
     */
    override fun onRestart() {
        super.onRestart()
        recreate()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main)
        supportActionBar?.title = getString(R.string.platforms)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (view_pager_main.currentItem == 0) {
            menu?.findItem(action_menu_info)?.isVisible = true
            menu?.findItem(action_menu_delete_all)?.isVisible = false
            menu?.findItem(action_menu_search)?.isVisible = false
            menu?.findItem(action_menu_sort)?.isVisible = false
        } else if (view_pager_main.currentItem == 1) {
            menu?.findItem(action_menu_info)?.isVisible = false
            menu?.findItem(action_menu_delete_all)?.isVisible = true
            menu?.findItem(action_menu_search)?.isVisible = true
            menu?.findItem(action_menu_sort)?.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setViewPager() {
        val screenSliderAdapter = ScreenSliderAdapter(supportFragmentManager)
        screenSliderAdapter.run {
            addFragment(MainFragment())
            addFragment(FavoritesFragment())
        }

        view_pager_main.run {
            adapter = screenSliderAdapter
            pageMargin = 6
            setPageMarginDrawable(R.color.colorAccent)
            setPageTransformer(true, DepthPageTransformer())
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    invalidateOptionsMenu()
                    //val params = toolbar_main.layoutParams as AppBarLayout.LayoutParams
                    when (position) {
                        0 -> {
                            supportActionBar?.title = getString(R.string.platforms)
                            //params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                        }
                        1 -> {
                            supportActionBar?.title = getString(R.string.favorites)
                            //params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        }
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }
    }

    private fun setTabLayout() {
        tabs_main.run {
            setupWithViewPager(view_pager_main)
            getTabAt(0)?.setIcon(R.drawable.ic_videogame_asset_white)
            getTabAt(1)?.setIcon(R.drawable.ic_favorite_white)
        }
    }

    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()

        when {
            view_pager_main.currentItem == 1 -> view_pager_main.setCurrentItem(0, true)
            currentTimeMillis - backPressedTime > 2000 -> {
                backPressedTime = currentTimeMillis
                applicationContext.toast(getString(R.string.back_to_homescreen))
            }
            else -> super.onBackPressed()
        }
    }
}
