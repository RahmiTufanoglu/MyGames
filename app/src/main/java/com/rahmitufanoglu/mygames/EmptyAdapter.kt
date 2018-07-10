package com.rahmitufanoglu.mygames

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EmptyAdapter : RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_games, parent, false)
        return EmptyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 0
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
