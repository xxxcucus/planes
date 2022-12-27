package com.planes.android.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestWithSimpleFinalizeCommObj
import com.planes.multiplayer_engine.responses.NoRobotResponse
import io.reactivex.Observable
import retrofit2.Response

class NoRobotFragment : Fragment() {

    private lateinit var m_PhotosList: List<PhotoModel>
    private lateinit var m_PhotosAdapter: PhotoAdapter

    var m_RequestId: Long = 0
    lateinit var m_Question: String
    lateinit var m_Images: Array<String>
    lateinit var m_Selection: Array<Boolean>

    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_NoRobotCommObj: SimpleRequestWithSimpleFinalizeCommObj<NoRobotResponse>


    private var m_ImagesMapping = mapOf("2b36ea33-9c99-46dd-9f80-3bc648881c9b" to R.raw.image1,
        "2e2379c7-cfcf-49a2-b47b-ed8d1a0c353a" to R.raw.image2,
        "3debe3a5-cb77-4074-8759-908944b4ad5b" to R.raw.image3,
        "4a8032e2-8fa0-41ee-8e02-4c40079a9dd8" to R.raw.image4,
        "6c1c61b5-f752-45d3-9f3e-0f8184643b68" to R.raw.image5,
        "7c315ae2-507b-4e51-96c7-e6f4b10597f9" to R.raw.image6,
        "7ce5271d-c063-444a-82f2-068631f1f4a1" to R.raw.image7,
        "7da77307-7e60-418d-b210-1845a1c657f3" to R.raw.image8,
        "7f1ac6e1-cfc5-47d7-854f-bde3f3a5f53b" to R.raw.image9,
        "8bd7d84a-45c8-4621-a2a7-750a4c6b80d4" to R.raw.image10,
        "9f9a49c3-6c7c-4915-a206-9eb3603d6728" to R.raw.image11,
        "31d94e7f-ecb8-4787-a579-21485e489fd7" to R.raw.image12,
        "043adddf-90a7-4fde-88a1-a25e92c4532f" to R.raw.image13,
        "65fd5538-e6eb-4970-8a7e-b2645df7c868" to R.raw.image14,
        "085cf88e-6403-405d-8f9a-237861df3e49" to R.raw.image15,
        "90f20a72-72f5-49a6-872b-6db44cd1368e" to R.raw.image16,
        "517faf00-09a0-4f3d-ac77-c0397938d185" to R.raw.image17,
        "06010c92-529a-4a72-8172-c56a7acd1622" to R.raw.image18,
        "22635c95-5503-4c7b-ab32-2e678f935a43" to R.raw.image19,
        "56969e76-ab31-47ca-864e-5052258d7bf2" to R.raw.image20,
        "75823d2e-2591-4771-a9ae-ea14fa26ca58" to R.raw.image21,
        "2264189b-c409-4ad3-9ede-872845dee031" to R.raw.image22,
        "6530047e-8e6a-4cc6-8001-b55df824e8ef" to R.raw.image23,
        "7269506c-ad95-4e63-934e-b1ae0c0b7a7e" to R.raw.image24,
        "a1c3d89c-f15c-4876-a44d-3e85aa75e0fa" to R.raw.image25,
        "a2b1982e-3928-4efe-b6a5-4ae447871e70" to R.raw.image26,
        "a0198cf8-efb2-4c10-af74-e25745edebac" to R.raw.image27,
        "b1f25a04-707a-4e73-92c4-effba955f6a0" to R.raw.image28,
        "b4d8d197-e349-4667-88b1-604b245a7cf8" to R.raw.image29,
        "baf9cdc5-dfe9-44ea-ba9a-0ba85d629fdc" to R.raw.image30,
        "bbd38b59-ca0c-472c-a27a-ba69daeec260" to R.raw.image31,
        "c02a5abb-61a6-4e5f-9f8d-633ef7849671" to R.raw.image32,
        "c74dbcd5-f037-4c35-b6ff-8d5e28f83030" to R.raw.image33,
        "d03ced07-e5b5-4a5a-89d3-1f54c26fa48f" to R.raw.image34,
        "e923cf20-e4e7-4e48-ab4d-1c1e427645fa" to R.raw.image35,
        "e8392d42-aa0c-4af3-89ea-37a563cb9214" to R.raw.image36,
        "ebfc22ba-3117-49e2-9910-6b390610b2af" to R.raw.image37,
        "fa093da7-546a-4627-ac80-6adcfd1431e9" to R.raw.image38
        )


    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializePhotos()  //TODO: should it be here ?
        preparePhotosList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_norobot, container, false)
        val recyclerView: RecyclerView = rootview.findViewById(R.id.recyclerView)

        val questionTextView = rootview.findViewById(R.id.question_text) as TextView

        val questionDogText = getString(R.string.norobot_dog)
        val questionCatText = getString(R.string.norobot_cat)
        val question = when (m_Question.lowercase()) {
            "dog" -> questionDogText
            "cat" -> questionCatText
            else -> m_Question
        }
        questionTextView.text = getString(R.string.norobot_question, question)

        val allmarkedButton = rootview.findViewById(R.id.allmarked_button) as Button
        allmarkedButton.setOnClickListener { sendNoRobotData() }

        val mLayoutManager = if (isHorizontal()) StaggeredGridLayoutManager(3, 1)
            else StaggeredGridLayoutManager(2, 1) //TODO to check tablets
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_PhotosAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.register))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.NoRobot)
        }

        return  rootview
    }


    private fun preparePhotosList() {
        m_PhotosList = m_Images.map { imageid ->
            val idx = m_Images.indexOf(imageid)
            PhotoModel(m_ImagesMapping[imageid]!!, m_Selection[idx])
        }
        m_PhotosAdapter = PhotoAdapter(m_PhotosList)
        m_PhotosAdapter.notifyDataSetChanged()
    }

    private fun isHorizontal(): Boolean {
        if (activity is MainActivity)
            return (activity as MainActivity).isHorizontal()
        else
            return false
    }

    override fun onPause() {
        super.onPause()
        writeToNoRobotSettingsService()
    }


    override fun onDetach () {
        super.onDetach()
        writeToNoRobotSettingsService()
        if (this::m_NoRobotCommObj.isInitialized)
            m_NoRobotCommObj.disposeSubscription()
        hideLoading()
    }


    private fun initializePhotos() {
        m_RequestId = requireArguments().getString("norobot/requestid")!!.toLong()
        m_Images = requireArguments().getSerializable("norobot/images") as Array<String>
        m_Question = requireArguments().getString("norobot/question")!!
        m_Selection = requireArguments().getSerializable(("norobot/selection")) as Array<Boolean>
    }

    private fun writeToNoRobotSettingsService() {
        m_Selection = m_PhotosList.map {
            photo -> photo.m_Selected
        }.toTypedArray()
        if (activity is MainActivity)
            (activity as MainActivity).setNorobotSettings(m_RequestId, m_Images, m_Question, m_Selection)
    }

    private fun createObservableNoRobot() : Observable<Response<NoRobotResponse>> {
        m_Selection = m_PhotosList.map {
                photo -> photo.m_Selected
        }.toTypedArray()

        var answer = ""
        for (i in m_Selection.indices) {
            answer += if (m_Selection[i]) "1" else "0"
        }
        return m_MultiplayerRound.norobot(m_RequestId, answer)
    }

    private fun sendNoRobotData() {

        m_NoRobotCommObj = SimpleRequestWithSimpleFinalizeCommObj(::createObservableNoRobot,
            getString(R.string.registererror), getString(R.string.unknownerror),  getString(R.string.norobot_success), requireActivity())

        m_NoRobotCommObj.makeRequest()

    }

    private fun hideLoading() {
        if (activity is MainActivity)
            (activity as MainActivity).stopProgressDialog()
    }

}
