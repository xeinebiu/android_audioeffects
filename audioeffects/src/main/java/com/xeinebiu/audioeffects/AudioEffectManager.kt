package com.xeinebiu.audioeffects

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer

class AudioEffectManager constructor(
    audioSessionId: Int,
    initialState: Boolean = true
) {
    var equalizer = XEqualizer(Integer.MAX_VALUE, audioSessionId)
        private set

    var bassBoost = XBassBoost(Integer.MAX_VALUE, audioSessionId)
        private set

    init {
        bassBoost.enabled = initialState
        equalizer.enabled = initialState
    }

    fun release() {
        equalizer.release()
        bassBoost.release()
    }
}

class XEqualizer(priority: Int, audioSessionId: Int) : Equalizer(priority, audioSessionId) {
    var currPreset: Short = currentPreset
        private set

    override fun usePreset(preset: Short) {
        super.usePreset(preset)
        currPreset = preset
    }
}

class XBassBoost(priority: Int, audioSessionId: Int) : BassBoost(priority, audioSessionId) {
    val maxRecommendedStrength = 19

    override fun setStrength(strength: Short) {
        super.setStrength((1000F / maxRecommendedStrength * strength).toShort())
    }

    override fun getRoundedStrength(): Short {
        return (super.getRoundedStrength() / (1000F / maxRecommendedStrength)).toShort()
    }
}
