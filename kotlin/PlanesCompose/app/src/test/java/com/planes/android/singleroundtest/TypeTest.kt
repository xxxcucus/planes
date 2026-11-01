package com.planes.android.singleroundtest
import com.google.common.truth.Truth
import com.planes.singleplayerengine.Type
import org.junit.Test

class TypeTest {
    @Test
    fun Type_Values() {
        Truth.assertThat(Type.entries[0] === Type.Miss).isTrue()
        Truth.assertThat(Type.entries[1] === Type.Hit).isTrue()
        Truth.assertThat(Type.entries[2] === Type.Dead).isTrue()
    }
}