package com.oe.saleclient.ui.fragments

import android.view.View

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, index: Int)
}