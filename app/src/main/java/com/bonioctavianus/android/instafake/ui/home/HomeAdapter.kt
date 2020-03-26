package com.bonioctavianus.android.instafake.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.utils.loadImage
import kotlinx.android.synthetic.main.row_item_home.view.*

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {

    private var mItems: List<Image> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(mItems[position])
    }

    fun submitList(items: List<Image>) {
        mItems = items
        notifyDataSetChanged()
    }
}

class HomeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Image) {
        with(item) {
            view.image_picture.loadImage(uri, R.drawable.ic_image)
            view.text_user_email.text = userEmail
            view.text_image_description.text = description
        }
    }
}