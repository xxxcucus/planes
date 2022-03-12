package com.planes.android.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import com.planes.android.R


class PhotoAdapter(private val onItemClicked: (position: Int) -> Unit, photosList: List<PhotoModel>) : RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {
    private val m_PhotosList: List<PhotoModel>
    private var m_SelectedPosition = mutableListOf<Int>()

    init {
        this.m_PhotosList = photosList
    }

    inner class MyViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit, context: Context) :
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        var m_Photo: ImageView
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
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            /*notifyItemChanged(m_SelectedPosition);
            m_SelectedPosition = adapterPosition
            notifyItemChanged(m_SelectedPosition);
            onItemClicked(m_SelectedPosition)*/
        }

        override fun onLongClick(v: View): Boolean {
            /*var youtubeLink = m_VideosList[m_Position].getYoutubeLink()
            Tools.openLink(m_Context, youtubeLink)*/
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
        return MyViewHolder(itemView, onItemClicked, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.itemView.setSelected(m_SelectedPosition == position)

        val photo: PhotoModel = m_PhotosList[position]
        holder.setPhoto(photo.m_ImageId)
        holder.m_Position = position

    }

    override fun getItemCount(): Int {
        return m_PhotosList.size
    }
}