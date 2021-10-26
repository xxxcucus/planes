package com.planes_multiplayer.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class ExoplayerFragment : Fragment() {

    private lateinit var player: SimpleExoPlayer

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        player = SimpleExoPlayer.Builder(context).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootview = inflater.inflate(R.layout.fragment_exoplayer, container, false)
        playerView = rootview.findViewById(R.id.video_view)

        val position = requireArguments().getInt("position")
        var mediaItems = createMediaItems()
        //for (mediaItem in mediaItems)
            //player.addMediaItem(mediaItem)
        player.setMediaItem(mediaItems[position])
        return  rootview
    }

    public override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    public override fun onPause() {
        super.onPause()
        playWhenReady = false
        setPlayerPlayOnReady(playWhenReady)
    }

    public override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun setPlayerPlayOnReady(value: Boolean) {
        player.playWhenReady = value
    }

    private fun initializePlayer() {
        playerView.player = player
        playerView.useController = true
        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        player.prepare()
    }

    private fun releasePlayer() {
        player.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
    }

    private fun createMediaItems(): Array<MediaItem> {

        var multiplayer_mi = createMediaItem(R.raw.multiplayer)
        var singleplayer_mi = createMediaItem(R.raw.singleplayer)
        var guessingplanes_mi = createMediaItem(R.raw.guessing)
        var positioningplanes_mi = createMediaItem(R.raw.positioning)

        var retVal : Array<MediaItem> = arrayOf(singleplayer_mi, multiplayer_mi, positioningplanes_mi, guessingplanes_mi)
        return retVal
    }

    private fun createMediaItem(id: Int): MediaItem {
        val uri = RawResourceDataSource.buildRawResourceUri(id)
        return MediaItem.Builder().setUri(uri).build()
    }
}