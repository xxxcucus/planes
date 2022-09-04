package com.planes.android.videos
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R
import com.planes.android.Tools


class VideoAdapter(private val onItemClicked: (position: Int) -> Unit, moviesList: List<VideoModel>, context: Context) : RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    private val m_VideosList: List<VideoModel> = moviesList
    private var m_SelectedPosition = 0
    private var m_Context = context

    inner class MyViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit, context: Context) :
            RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        var m_Title: TextView
        var m_Duration: TextView
        var m_Context: Context
        var m_Position: Int = 0

        init {
            m_Title = view.findViewById(R.id.title)
            m_Duration = view.findViewById(R.id.duration)
            m_Context = context
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (adapterPosition == RecyclerView.NO_POSITION) return

            // Updating old as well as new positions
            notifyItemChanged(m_SelectedPosition)
            m_SelectedPosition = adapterPosition
            notifyItemChanged(m_SelectedPosition)
            onItemClicked(m_SelectedPosition)
        }

        override fun onLongClick(v: View): Boolean {
            val youtubeLink = m_VideosList[adapterPosition].getYoutubeLink()
            Tools.openLink(m_Context, youtubeLink)
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.videolayout, parent, false)
        return MyViewHolder(itemView, onItemClicked, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.isSelected = m_SelectedPosition == position

        val movie: VideoModel = m_VideosList[position]
        holder.m_Title.text = movie.getVideoName()
        holder.m_Duration.text = m_Context.getString(R.string.video_duration, movie.getVideoDuration())
        holder.m_Position = position

    }

    override fun getItemCount(): Int {
        return m_VideosList.size
    }

    fun setCurrentVideo(currentVideo: Int) {
        m_SelectedPosition = currentVideo
    }
}