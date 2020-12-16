package io.github.corzani.nep

import kotlin.test.Test

class BusTest {

    @Test
    fun `Rom Memory Mirroring (16k ROM)`() = test(
        nesRomWithHeader(0xe8u)
    ) {
        assertMemoryEquals(0xe8u, bus.read(0x8000u)) // 0x8000 Initial PC
        assertMemoryEquals(0xe8u, bus.read(0xC000u)) // after 0x4000 (16kb) is mirrored
    }
}