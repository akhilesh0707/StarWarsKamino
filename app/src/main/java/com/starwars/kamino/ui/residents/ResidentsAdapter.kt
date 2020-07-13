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

    /**
     * Create view holder using layout inflater
     * @param parent : Parent view
     * @param viewType : View type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ResidentViewHolder(view)
    }

    /**
     * Get number of item count
     */
    override fun getItemCount(): Int {
        return residentList.size
    }

    /**
     * Bind data to view holder from resident list
     * @param holder : View holder
     * @param position : item position
     */
    override fun onBindViewHolder(holder: ResidentViewHolder, position: Int) {
        holder.bind(residentList[position])
    }

    /**
     * Resident view holder inner class
     * @param itemView : Root view
     */
    inner class ResidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Bind resident image and name in UI
         * @param resident : Resident information
         */
        fun bind(resident: ResidentModel) {
            with(itemView) {
                textResident.text = resident.name
                requestManager?.load(resident.imageUrl)?.into(imageResident)
                setOnClickListener {
                    clickListener.onClick(resident)
                }
            }
        }
    }

    /**
     * Resident click call back interface
     */
    interface OnResidentClickListener {
        /**
         * Specific resident click listener
         */
        fun onClick(selectedResident: ResidentModel)
    }
}