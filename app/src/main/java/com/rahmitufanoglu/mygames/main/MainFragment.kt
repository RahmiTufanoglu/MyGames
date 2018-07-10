package com.rahmitufanoglu.mygames.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.rahmitufanoglu.mygames.MainApplication
import com.rahmitufanoglu.mygames.R
import com.rahmitufanoglu.mygames.nintendo.NintendoActivity
import com.rahmitufanoglu.mygames.pc.PcActivity
import com.rahmitufanoglu.mygames.playstation.PlaystationActivity
import com.rahmitufanoglu.mygames.xbox.XboxActivity
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_pc.setOnClickListener(this)
        btn_xbox.setOnClickListener(this)
        btn_playstation.setOnClickListener(this)
        btn_nintendo.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainApplication.getRefWatcher(activity!!).watch(this)
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_info -> {
                val dialog = LayoutInflater.from(activity)
                val dialogView = dialog.inflate(R.layout.dialog_made_by, null)

                AlertDialog.Builder(activity!!)
                        .setView(dialogView)
                        .show()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     *  Intent events for the material buttons
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_pc -> setIntent(PcActivity::class.java)
            R.id.btn_xbox -> setIntent(XboxActivity::class.java)
            R.id.btn_playstation -> setIntent(PlaystationActivity::class.java)
            R.id.btn_nintendo -> setIntent(NintendoActivity::class.java)
        }
    }

    private fun setIntent(cls: Class<*>) {
        val intent = Intent(activity, cls)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }
}
