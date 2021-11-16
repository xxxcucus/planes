package com.planes_multiplayer.android

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class VideoFragment1 : Fragment() {

    private lateinit var m_MovieList: List<VideoModel>
    private lateinit var m_VideosAdapter: VideoAdapter

    private lateinit var m_VideoView: VideoView
    private var m_CurrentVideo = 0
    private var m_CurrentPositionInVideo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prepareVideoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootview = inflater.inflate(R.layout.fragment_videos1, container, false)
        val recyclerView: RecyclerView = rootview.findViewById(R.id.recyclerView)

        val mLayoutManager = LinearLayoutManager(activity)
        if (isHorizontal()) {
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        } else {
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        }
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_VideosAdapter


        m_VideoView = rootview.findViewById(R.id.video_view)
        onVideoItemClick(0)

        return  rootview
    }

    private fun prepareVideoList() {
        var videoModel_guessing = VideoModel("Guessing Planes Positions", R.raw.guessing, "00:01:49", 1.42f)
        var videoModel_positioning = VideoModel("Positioning of Planes", R.raw.positioning, "00:01:22", 1.42f)
        var videoModel_single = VideoModel("Single Player Tutorial", R.raw.singleplayer, "00:02:00", 1.36f)
        var videoModel_multi = VideoModel("Multi-Player Tutorial", R.raw.multiplayer, "00:05:44", 1.77f)

        m_MovieList = arrayListOf(videoModel_guessing, videoModel_positioning, videoModel_single, videoModel_multi)
        m_VideosAdapter = VideoAdapter( { position -> onVideoItemClick(position)}, m_MovieList)
        m_VideosAdapter.notifyDataSetChanged();
    }

    private fun isHorizontal(): Boolean {
        return (activity as MainActivity).isHorizontal()
    }

    private fun setDimension(isHorizontal: Boolean, videoRatio: Float) {
        val videoProportion: Float = videoRatio
        val screenWidth = if (!isHorizontal)  resources.displayMetrics.widthPixels else resources.displayMetrics.widthPixels * 6 / 10
        val screenHeight = if (!isHorizontal) resources.displayMetrics.heightPixels else resources.displayMetrics.heightPixels

        //val screenWidth = resources.displayMetrics.widthPixels
        //val screenHeight = resources.displayMetrics.heightPixels

        val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()
        val lp: ViewGroup.LayoutParams = m_VideoView!!.getLayoutParams()
        if (videoProportion < screenProportion) {
            lp.height = screenHeight
            lp.width = (screenHeight.toFloat() / videoProportion).toInt()
        } else {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() * videoProportion).toInt()
        }
        m_VideoView!!.setLayoutParams(lp)
    }

    override fun onStart() {
        super.onStart()
        if (m_CurrentPositionInVideo != 0)
            m_VideoView!!.seekTo(m_CurrentPositionInVideo)

        m_VideoView!!.start()

    }

    override fun onResume() {
        super.onResume()
        if (!m_VideoView!!.isPlaying) {
            if (m_CurrentPositionInVideo != 0)
                m_VideoView!!.seekTo(m_CurrentPositionInVideo)

            m_VideoView!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        m_VideoView!!.pause()
        m_CurrentPositionInVideo = m_VideoView!!.currentPosition
    }

    override fun onStop() {
        m_VideoView!!.pause()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("position", m_CurrentPositionInVideo)
        super.onSaveInstanceState(outState)
    }

    private fun onVideoItemClick(position: Int) {
        setDimension(isHorizontal(), m_MovieList[position].getVideoRatio())
        var uri = Uri.parse("android.resource://"
                + (activity as MainActivity).packageName + "/" + m_MovieList[position].getVideoId())
        m_VideoView!!.setVideoURI(uri)
    }
}
