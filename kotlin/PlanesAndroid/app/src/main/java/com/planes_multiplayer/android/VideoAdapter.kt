package com.planes_multiplayer.android
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class VideoAdapter(private val onItemClicked: (position: Int) -> Unit, moviesList: List<VideoModel>) : RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    private val m_VideosList: List<VideoModel>

    init {
        this.m_VideosList = moviesList
    }

    inner class MyViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var title: TextView
        var duration: TextView

        init {
            title = view.findViewById(R.id.title)
            duration = view.findViewById(R.id.duration)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.videolayout, parent, false)
        return MyViewHolder(itemView, onItemClicked)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie: VideoModel = m_VideosList[position]
        holder.title.setText(movie.getVideoName())
        holder.duration.setText("Duration: " + movie.getVideoDuration())
    }

    override fun getItemCount(): Int {
        return m_VideosList.size
    }


}