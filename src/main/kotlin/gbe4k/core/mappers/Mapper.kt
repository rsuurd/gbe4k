package gbe4k.core.mappers

import gbe4k.core.Addressable

interface Mapper : Addressable

interface BatteryPowered {
    fun save()

    fun load()
}
