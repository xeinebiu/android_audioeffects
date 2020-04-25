package com.xeinebiu.audioeffects

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer

class AudioEffectManager constructor(audioSessionId: Int) {
    var equalizer: Equalizer = Equalizer(Integer.MAX_VALUE, audioSessionId)
        private set

    var bassBoost: BassBoost = BassBoost(Integer.MAX_VALUE, audioSessionId)
        private set

    init {
        bassBoost.enabled = true
        equalizer.enabled = true
    }

    fun release() {
        equalizer.release()
        bassBoost.release()
    }
}
