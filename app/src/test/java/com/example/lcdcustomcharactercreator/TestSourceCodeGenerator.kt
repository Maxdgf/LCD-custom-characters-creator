package com.example.lcdcustomcharactercreator

import com.example.lcdcustomcharactercreator.utils.SourceCodeGenerator
import org.junit.Test
import org.junit.Assert.*
import java.util.BitSet

/**Source code generator Unit test.*/
class TestSourceCodeGenerator {
    val sourceCodeGenerator = SourceCodeGenerator()

    @Test
    fun sourceCodeGenerationIsCorrect() {
        val pixelsMap = BitSet(40).apply {
            set(1)
            set(3)
        }

        val required = "byte my_character[8] = {\n\t0b01010\n\t0b00000\n\t0b00000\n\t0b00000\n\t0b00000\n\t0b00000\n\t0b00000\n\t0b00000\n};"
        val sourceCode = sourceCodeGenerator.generateSourceCppCode(pixelsMap)

        assertEquals(required, sourceCode.text)
    }
}