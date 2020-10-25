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
    private var enableStatusListeners = mutableListOf<(Boolean) -> Unit>()
    private var propertiesChangeListeners = mutableListOf<(Settings?) -> Unit>()
    private var bandChangeListeners = mutableListOf<(band: Short, level: Short) -> Unit>()

    var currPreset: Short = currentPreset
        private set

    fun addEnableStatusChangeListener(listener: (Boolean) -> Unit) {
        enableStatusListeners.add(listener)
    }

    fun removeEnableStatusChangeListener(listener: (Boolean) -> Unit) {
        enableStatusListeners.remove(listener)
    }

    fun addPropertiesChangeListener(listener: (Settings?) -> Unit) {
        propertiesChangeListeners.add(listener)
    }

    fun removePropertiesChangeListener(listener: (Settings?) -> Unit) {
        propertiesChangeListeners.remove(listener)
    }

    fun addBandChangeListeners(listener: (band: Short, level: Short) -> Unit) {
        bandChangeListeners.add(listener)
    }

    fun removeBandChangeListeners(listener: (band: Short, level: Short) -> Unit) {
        bandChangeListeners.remove(listener)
    }

    override fun usePreset(preset: Short) {
        super.usePreset(preset)
        currPreset = preset
    }

    override fun release() {
        bandChangeListeners.clear()
        propertiesChangeListeners.clear()
        enableStatusListeners.clear()
        super.release()
    }

    override fun setEnabled(enabled: Boolean): Int {
        try {
            return super.setEnabled(enabled)
        } finally {
            enableStatusListeners.forEach {
                it(enabled)
            }
        }
    }

    override fun setProperties(settings: Settings?) {
        try {
            super.setProperties(settings)
        } finally {
            propertiesChangeListeners.forEach {
                it(settings)
            }
        }
    }

    override fun setBandLevel(band: Short, level: Short) {
        try {
            super.setBandLevel(band, level)
        } finally {
            bandChangeListeners.forEach {
                it(band, level)
            }
        }
    }
}

class XBassBoost(priority: Int, audioSessionId: Int) : BassBoost(priority, audioSessionId) {
    val maxRecommendedStrength = 19

    private var enableStatusListeners = mutableListOf<(Boolean) -> Unit>()
    private var propertiesChangeListeners = mutableListOf<(Settings?) -> Unit>()
    private var strengthChangeListeners = mutableListOf<(Short) -> Unit>()

    fun addEnableStatusChangeListener(listener: (Boolean) -> Unit) {
        enableStatusListeners.add(listener)
    }

    fun removeEnableStatusChangeListener(listener: (Boolean) -> Unit) {
        enableStatusListeners.remove(listener)
    }

    fun addPropertiesChangeListener(listener: (Settings?) -> Unit) {
        propertiesChangeListeners.add(listener)
    }

    fun removePropertiesChangeListener(listener: (Settings?) -> Unit) {
        propertiesChangeListeners.remove(listener)
    }

    fun addBandChangeListeners(listener: (Short) -> Unit) {
        strengthChangeListeners.add(listener)
    }

    fun removeBandChangeListeners(listener: (Short) -> Unit) {
        strengthChangeListeners.remove(listener)
    }

    override fun setStrength(strength: Short) {
        try {
            super.setStrength((1000F / maxRecommendedStrength * strength).toInt().toShort())
        } finally {
            strengthChangeListeners.forEach {
                it(strength)
            }
        }
    }

    override fun getRoundedStrength(): Short {
        return (super.getRoundedStrength() / (1000F / maxRecommendedStrength)).toInt().toShort()
    }

    override fun release() {
        strengthChangeListeners.clear()
        propertiesChangeListeners.clear()
        enableStatusListeners.clear()
        super.release()
    }

    override fun setEnabled(enabled: Boolean): Int {
        try {
            return super.setEnabled(enabled)
        } finally {
            enableStatusListeners.forEach {
                it(enabled)
            }
        }
    }

    override fun setProperties(settings: Settings?) {
        try {
            super.setProperties(settings)
        } finally {
            propertiesChangeListeners.forEach {
                it(settings)
            }
        }
    }
}
