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
import com.google.android.exoplayer2.upstream.RawResourceDataSource


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
        setDimension(isHorizontal())

        // set the absolute path of the video file which is going to be played

        var uri = Uri.parse("android.resource://"
                + (activity as MainActivity).packageName + "/" + R.raw.multiplayer)
        m_VideoView!!.setVideoURI(uri)


        return  rootview
    }

    private fun prepareVideoList() {
        var videoModel_guessing = VideoModel("Guessing Planes Positions", R.raw.guessing)
        var videoModel_positioning = VideoModel("Positioning of Planes", R.raw.positioning)
        var videoModel_single = VideoModel("Single Player Tutorial", R.raw.singleplayer)
        var videoModel_multi = VideoModel("Multi-Player Tutorial", R.raw.multiplayer)

        m_MovieList = arrayListOf(videoModel_guessing, videoModel_positioning, videoModel_single, videoModel_multi)
        m_VideosAdapter = VideoAdapter(m_MovieList)
        m_VideosAdapter.notifyDataSetChanged();
    }

    private fun isHorizontal(): Boolean {
        return (activity as MainActivity).isHorizontal()
    }

    private fun setDimension(isHorizontal: Boolean) {
        val videoProportion: Float = 1.77F
        val screenWidth = if (!isHorizontal)  resources.displayMetrics.widthPixels else resources.displayMetrics.widthPixels * 3 / 4
        val screenHeight = if (!isHorizontal) resources.displayMetrics.heightPixels else resources.displayMetrics.heightPixels / 2

        val screenProportion = screenHeight.toFloat() / screenWidth.toFloat()
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


}

/*
   // declaring a null variable for VideoView
    var simpleVideoView: VideoView? = null
    var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        simpleVideoView = findViewById<View>(R.id.videoView) as VideoView


        var isHorizontal = false
        val linearLayout = findViewById<View>(R.id.rootView) as LinearLayoutCompat

        if (linearLayout.tag.toString().contains("horizontal")) {
            isHorizontal = true
        }

        setDimension(isHorizontal)

        // set the absolute path of the video file which is going to be played
        simpleVideoView!!.setVideoURI(Uri.parse("android.resource://"
                + packageName + "/" + R.raw.gp))


        simpleVideoView!!.setOnCompletionListener { simpleVideoView!!.start() }

        if (savedInstanceState != null) {
            var position = savedInstanceState.getInt("position")
            simpleVideoView!!.seekTo(position)
        }

    }

    override fun onStart() {
        super.onStart()
        if (currentPosition != 0)
            simpleVideoView!!.seekTo(currentPosition)

        simpleVideoView!!.start()

    }

    override fun onResume() {
        super.onResume()
        if (!simpleVideoView!!.isPlaying) {
            if (currentPosition != 0)
                simpleVideoView!!.seekTo(currentPosition)

            simpleVideoView!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        simpleVideoView!!.pause()
        currentPosition = simpleVideoView!!.currentPosition
    }

    override fun onStop() {
        simpleVideoView!!.pause()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("position", currentPosition)
        super.onSaveInstanceState(outState)
    }

    private fun setDimension(isHorizontal: Boolean) {
        // Adjust the size of the video
        // so it fits on the screen
        val videoProportion: Float = 1.77F
        val screenWidth = if (!isHorizontal)  resources.displayMetrics.widthPixels else resources.displayMetrics.widthPixels * 3 / 4
        val screenHeight = if (!isHorizontal) resources.displayMetrics.heightPixels else resources.displayMetrics.heightPixels * 3 / 4



        val screenProportion = screenHeight.toFloat() / screenWidth.toFloat()
        val lp: ViewGroup.LayoutParams = simpleVideoView!!.getLayoutParams()
        if (videoProportion < screenProportion) {
            lp.height = screenHeight
            lp.width = (screenHeight.toFloat() / videoProportion).toInt()
        } else {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() * videoProportion).toInt()
        }
        simpleVideoView!!.setLayoutParams(lp)
    }

 */