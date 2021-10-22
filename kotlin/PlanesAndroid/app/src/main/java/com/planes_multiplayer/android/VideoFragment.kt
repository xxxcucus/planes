package com.planes_multiplayer.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment



class VideoFragment : Fragment() {

    private lateinit var m_VideosListView: ListView
    private lateinit var m_VideoTitlesList: Array<String>
    private lateinit var m_VideosAdapter: CustomVideosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_VideoTitlesList = arrayOf(context.getString(R.string.singleplayertutorial), context.getString(R.string.multiplayertutorial),
            context.getString(R.string.positioningplanestutorial), context.getString(R.string.guessingplanestutorial))
        m_VideosAdapter = CustomVideosAdapter(context, m_VideoTitlesList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootview = inflater.inflate(R.layout.fragment_videos1, container, false)

        m_VideosListView = rootview.findViewById(R.id.list_view)
        m_VideosListView.adapter = m_VideosAdapter

        return  rootview
    }

}


