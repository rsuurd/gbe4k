package gbe4k

import gbe4k.core.Bus
import gbe4k.core.Cart
import gbe4k.core.Cpu
import gbe4k.core.Ppu
import gbe4k.core.boot.BootRom
import gbe4k.core.io.Dma
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Io
import gbe4k.core.io.Joypad
import gbe4k.core.io.Lcd
import gbe4k.core.io.Serial
import gbe4k.core.io.Timer
import java.io.ByteArrayOutputStream
import java.time.Duration

class Gbe4k(private val settings: Settings, private val cart: Cart) {
    val bootRom = BootRom()

    val joypad = Joypad()
    val interrupts = Interrupts()
    val serial = Serial(ByteArrayOutputStream())
    val timer = Timer(interrupts)
    val dma = Dma()
    val lcd = Lcd(dma, interrupts)
    val io = Io(joypad, serial, timer, lcd, interrupts)
    val bus = Bus(cart, io, bootRom)
    val cpu = Cpu(bus, timer, interrupts)
    val ppu = Ppu(bus, lcd, interrupts, settings.palette)

    private var emulating = true

    fun emulate() {
        cart.load()

        boot()

        var lastUpdateTime = System.nanoTime()

        while (emulating) {
            val now = System.nanoTime()

            while ((now - lastUpdateTime) >= DELAY.nano) {
                var cycles = 0
                while (cycles < CYCLES_PER_FRAME) {
                    val elapsed = timer.track {
                        cpu.step()
                    }

                    dma.transfer(bus, elapsed)
                    ppu.update(elapsed)

                    cycles += elapsed
                }

                lastUpdateTime += DELAY.nano;
            }

            Thread.sleep(DELAY)
        }
    }

    private fun boot() {
        if (settings.skipBootRom) {
            cpu.apply {
                pc = 0x0100

                registers.apply {
                    af = 0x01b0
                    bc = 0x0013
                    de = 0x00d8
                    hl = 0x014d
                    sp = 0xfffe
                }
            }

            lcd.apply {
                control.value = 0x91.toByte()
                stat.value = 0x85.toByte()
                bgPalette = 0xfc
                objPalette0 = 0xff
                objPalette1 = 0xff
            }
        }
    }

    fun stop() {
        emulating = false
    }

    companion object {
        private val DELAY: Duration = Duration.ofNanos(16600000) // 16.6ms
        private const val CYCLES_PER_FRAME = (4 * 1024 * 1024) / 60 // 4mhz for 60 fps
    }
}

fun Cpu.dump() =
    "A:%02x F:%02x B:%02x C:%02x D:%02x E:%02x H:%02x L:%02x SP:%04x PC:%04x PCMEM:%02x,%02x,%02x,%02x".format(
        this.registers.a,
        this.registers.f,
        this.registers.b,
        this.registers.c,
        this.registers.d,
        this.registers.e,
        this.registers.h,
        this.registers.l,
        this.registers.sp,
        this.pc,
        bus[pc],
        bus[pc + 1],
        bus[pc + 2],
        bus[pc + 3]
    )
