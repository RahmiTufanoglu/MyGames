package com.rahmitufanoglu.mygames

import android.app.Activity
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import com.rahmitufanoglu.mygames.model.Platform
import com.rahmitufanoglu.mygames.persistence.Game
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun setRecyclerView(activity: Activity, recyclerView: RecyclerView, num_columns: Int) {
    recyclerView.run {
        layoutManager = GridLayoutManager(activity, num_columns)
        setHasFixedSize(true)
        smoothScrollToPosition(0)
    }
}

fun animateFabSlideInRight(activity: Activity, fab: FloatingActionButton) {
    val fadeIn = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
    fab.startAnimation(fadeIn)
}

fun animateFabSlideInLeft(activity: Activity, fab: FloatingActionButton) {
    val fadeIn = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left)
    fab.startAnimation(fadeIn)
}

fun getDateGerman(release: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val date = simpleDateFormat.parse(release)
    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN)
    return dateFormat.format(date)
}

fun compareTitle(): Comparator<Platform> {
    return Comparator { game, otherGame ->
        game.title!!.compareTo(otherGame.title!!)
    }
}

fun compareTitleGame(): Comparator<Game> {
    return Comparator { game, otherGame ->
        game.title!!.compareTo(otherGame.title!!)
    }
}

fun compareRelease(): Comparator<Platform> {
    return Comparator { game, otherGame ->
        val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(game.release)
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(otherGame.release)
        date1.compareTo(date2)
    }
}

fun compareReleaseGame(): Comparator<Game> {
    return Comparator { game, otherGame ->
        val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(game.release)
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(otherGame.release)
        date1.compareTo(date2)
    }
}
