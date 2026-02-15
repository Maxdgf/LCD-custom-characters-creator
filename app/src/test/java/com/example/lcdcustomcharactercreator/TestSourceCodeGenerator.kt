package com.example.lcdcustomcharactercreator

import com.example.lcdcustomcharactercreator.utils.SourceCodeGenerator
import org.junit.Test
import org.junit.Assert.*
import java.util.BitSet

/**Source code generator Unit test.*/
class TestSourceCodeGenerator {
    val sourceCodeGenerator = SourceCodeGenerator()

    @Test
    fun sourceCppByteArrayCodeBinaryModeGenerationIsCorrect() {
        val pixelsMap = BitSet(40).apply {
            set(1)
            set(3)
        }

        // required result
        val name = "test" // pattern name
        val required = "byte $name[8] = {\n\t0b01010,\n\t0b00000,\n\t0b00000,\n\t0b00000,\n\t0b00000,\n\t0b00000,\n\t0b00000,\n\t0b00000\n};"
        val sourceCode = sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, name, "binary")

        assertEquals(required, sourceCode.text)
    }

    @Test
    fun sourceCppByteArrayCodeHexModeGenerationIsCorrect() {
        val pixelsMap = BitSet(40).apply {
            set(1)
            set(3)
        }

        // required result
        val name = "test" // pattern name
        val required = "byte $name[8] = {\n\t0x0A,\n\t0x00,\n\t0x00,\n\t0x00,\n\t0x00,\n\t0x00,\n\t0x00,\n\t0x00\n};"
        val sourceCode = sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, name, "hex")

        assertEquals(required, sourceCode.text)
    }
}