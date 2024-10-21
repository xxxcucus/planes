package com.planes.android.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import com.planes.android.R


class PhotoAdapter(photosList: List<PhotoModel>) : RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {
    private val m_PhotosList: List<PhotoModel>
    private var m_SelectedPositions = mutableListOf<Int>()

    init {
        this.m_PhotosList = photosList
        for(i in m_PhotosList.indices) {
            if (m_PhotosList[i].m_Selected)
                m_SelectedPositions.add(i)
        }
    }

    inner class MyViewHolder(view: View, context: Context) :
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        private var m_Photo: ImageView
        var m_Position: Int = 0
        var m_Context: Context

        init {
            m_Photo = view.findViewById(R.id.image)
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
            m_Context = context
       }

        override fun onClick(v: View) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (adapterPosition == RecyclerView.NO_POSITION) return

            val clickedPosition = m_Position
            notifyItemChanged(adapterPosition)

            if (m_SelectedPositions.contains(clickedPosition)) {
                m_SelectedPositions.remove(clickedPosition)
                m_PhotosList[clickedPosition].m_Selected = false
            }
            else {
                m_SelectedPositions.add(clickedPosition)
                m_PhotosList[clickedPosition].m_Selected = true
            }
        }

        override fun onLongClick(v: View): Boolean {
            //TODO: zoom in on image
            return true
        }

        fun setPhoto(imageid : Int) {
            val bm = BitmapFactory.decodeResource(m_Context.resources, imageid)
            m_Photo.setImageBitmap(bm)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.norobotlayout, parent, false)
        return MyViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.isSelected = m_SelectedPositions.contains(position)
        val photo: PhotoModel = m_PhotosList[position]
        holder.setPhoto(photo.m_ImageId)
        holder.m_Position = holder.adapterPosition
    }

    override fun getItemCount(): Int {
        return m_PhotosList.size
    }
}