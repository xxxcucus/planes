package com.planes.android.register

class NoRobotViewModel(question: String, image1: String, image2: String, image3: String,
                       image4: String, image5: String, image6: String, image7: String, image8: String, image9: String) {

    var m_Question: String
    var m_Image1: String
    var m_Image2: String
    var m_Image3: String
    var m_Image4: String
    var m_Image5: String
    var m_Image6: String
    var m_Image7: String
    var m_Image8: String
    var m_Image9: String

    init {
        m_Question = question
        m_Image1 = image1
        m_Image2 = image2
        m_Image3 = image3
        m_Image4 = image4
        m_Image5 = image5
        m_Image6 = image6
        m_Image7 = image7
        m_Image8 = image8
        m_Image9 = image9
    }
}
