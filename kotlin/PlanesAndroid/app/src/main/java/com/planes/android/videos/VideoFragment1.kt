package com.planes.android.videos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R


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

        var mLayoutManager = if (isHorizontal()) LinearLayoutManager(activity) else GridLayoutManager(activity,2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_VideosAdapter

        m_VideoView = rootview.findViewById(R.id.video_view)
        initializeVideo()
        (activity as MainActivity).setActionBarTitle(getString(R.string.videos))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Videos)

        return  rootview
    }

    private fun prepareVideoList() {
        var videoModel_guessing = VideoModel(getString(R.string.guessingplanestutorial), R.raw.guessing, "00:01:49",
            0,1.42f, "https://youtu.be/CAxSPp2h_Vo")
        var videoModel_positioning = VideoModel(getString(R.string.positioningplanestutorial), R.raw.positioning, "00:01:22",
            0,1.42f, "https://youtu.be/qgL0RdwqBRY")
        var videoModel_single = VideoModel(getString(R.string.singleplayertutorial), R.raw.singleplayer, "00:02:00",
            0, 1.36f, "https://youtu.be/N2Cg8eflCxM")
        var videoModel_multi = VideoModel(getString(R.string.multiplayertutorial), R.raw.multiplayer, "00:05:44",
            0,1.77f, "https://youtu.be/VqYK1T91-YE")

        m_MovieList = arrayListOf(videoModel_guessing, videoModel_positioning, videoModel_single, videoModel_multi)
        m_VideosAdapter = VideoAdapter( { position -> onVideoItemClick(position)}, m_MovieList)
        m_VideosAdapter.notifyDataSetChanged()
    }

    private fun isHorizontal(): Boolean {
        return (activity as MainActivity).isHorizontal()
    }

    private fun isTablet(): Boolean {
        return (activity as MainActivity).isTablet()
    }

    //TODO: currently not used ?
    private fun setDimension(isHorizontal: Boolean, videoRatio: Float) {
        val videoProportion: Float = videoRatio
        val screenWidth = if (!isHorizontal)  resources.displayMetrics.widthPixels else resources.displayMetrics.widthPixels * 6 / 10
        val screenHeight = resources.displayMetrics.heightPixels

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
        m_MovieList[m_CurrentVideo].setCurrentPosition(m_CurrentPositionInVideo)
        writeToVideoSettingsService()
    }

    override fun onStop() {
        m_VideoView!!.pause()
        super.onStop()
    }

    private fun initializeVideo() {
        m_CurrentVideo = requireArguments().getInt("videosettings/currentVideo")
        val videoPlaybackPositions = requireArguments().getSerializable("videosettings/videoPlaybackPositions") as IntArray
        m_MovieList.mapIndexed{ idx, value -> value.setCurrentPosition(videoPlaybackPositions[idx]) }
        m_CurrentPositionInVideo = m_MovieList[m_CurrentVideo].getCurentPosition()
        m_VideosAdapter.setCurrentVideo(m_CurrentVideo)

        //setDimension(isHorizontal(), /*m_MovieList[m_CurrentVideo].getVideoRatio()*/ 1.77f)
        var uri = Uri.parse("android.resource://"
                + (activity as MainActivity).packageName + "/" + m_MovieList[m_CurrentVideo].getVideoId())
        m_VideoView!!.setVideoURI(uri)
        m_VideoView!!.seekTo(m_CurrentPositionInVideo)
        m_VideoView!!.start()
    }

    private fun onVideoItemClick(position: Int) {
        var restartVideo = m_CurrentVideo == position

        m_VideoView!!.pause()
        m_MovieList[m_CurrentVideo].setCurrentPosition(if (restartVideo) 0 else m_VideoView!!.currentPosition)
        m_CurrentVideo = position
        m_CurrentPositionInVideo = if (restartVideo) 0 else m_MovieList[m_CurrentVideo].getCurentPosition()

        //setDimension(isHorizontal(), m_MovieList[position].getVideoRatio())
        var uri = Uri.parse("android.resource://"
                + (activity as MainActivity).packageName + "/" + m_MovieList[position].getVideoId())
        m_VideoView!!.setVideoURI(uri)
        m_VideoView!!.seekTo(m_CurrentPositionInVideo)
        m_VideoView!!.start()
    }

    override fun onDetach () {
        writeToVideoSettingsService()
        super.onDetach()
    }

    fun writeToVideoSettingsService() {
        (activity as MainActivity).setVideoSettings(m_CurrentVideo, m_MovieList.map  { it.getCurentPosition()}.toIntArray())
    }
}
