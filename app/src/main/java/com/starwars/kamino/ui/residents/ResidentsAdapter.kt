package com.starwars.kamino.ui.residents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.ui.residents.model.ResidentModel
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class ResidentsAdapter(
    private val residentList: ArrayList<ResidentModel>,
    private val requestManager: RequestManager,
    private val clickListener: OnResidentClickListener
) : RecyclerView.Adapter<ResidentsAdapter.ResidentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ResidentViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return residentList.size
    }

    override fun onBindViewHolder(holder: ResidentViewHolder, position: Int) {
        holder.bind(residentList[position])
    }

    inner class ResidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(resident: ResidentModel) {
            with(itemView) {
                textResident.text = resident.name
                requestManager.load(resident.imageUrl).into(imageResident)
                setOnClickListener {
                    clickListener.onClick(resident)
                }
            }
        }
    }

    interface OnResidentClickListener {
        fun onClick(selectedResident: ResidentModel)
    }
}