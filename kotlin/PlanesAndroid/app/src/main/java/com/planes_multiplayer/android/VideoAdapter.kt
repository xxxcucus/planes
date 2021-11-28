package com.planes_multiplayer.android
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class VideoAdapter(private val onItemClicked: (position: Int) -> Unit, moviesList: List<VideoModel>) : RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    private val m_VideosList: List<VideoModel>
    private var m_SelectedPosition = 0

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
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(m_SelectedPosition);
            m_SelectedPosition = adapterPosition
            notifyItemChanged(m_SelectedPosition);
            onItemClicked(m_SelectedPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.videolayout, parent, false)
        return MyViewHolder(itemView, onItemClicked)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setSelected(m_SelectedPosition == position)

        val movie: VideoModel = m_VideosList[position]
        holder.title.setText(movie.getVideoName())
        holder.duration.setText("Duration: " + movie.getVideoDuration())
    }

    override fun getItemCount(): Int {
        return m_VideosList.size
    }

    fun setCurrentVideo(currentVideo: Int) {
        m_SelectedPosition = currentVideo
    }


}