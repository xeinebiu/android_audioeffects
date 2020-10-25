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
    private var parameterListeners = mutableListOf<OnParameterChangeListener>()
    private var controlStatusListeners = mutableListOf<OnControlStatusChangeListener>()
    private var enableStatusListeners = mutableListOf<OnEnableStatusChangeListener>()

    init {
        setParameterListener { effect, status, param1, param2, value ->
            parameterListeners.forEach {
                it.onParameterChange(effect, status, param1, param2, value)
            }
        }
        setControlStatusListener { effect, controlGranted ->
            controlStatusListeners.forEach {
                it.onControlStatusChange(effect, controlGranted)
            }
        }
        setEnableStatusListener { effect, enabled ->
            enableStatusListeners.forEach {
                it.onEnableStatusChange(effect, enabled)
            }
        }
    }

    fun addParameterChangeListener(listener: OnParameterChangeListener) {
        parameterListeners.add(listener)
    }

    fun removeParameterChangeListener(listener: OnParameterChangeListener) {
        parameterListeners.remove(listener)
    }

    fun addControlStatusChangeListener(listener: OnControlStatusChangeListener) {
        controlStatusListeners.add(listener)
    }

    fun removeControlStatusChangeListener(listener: OnControlStatusChangeListener) {
        controlStatusListeners.remove(listener)
    }

    fun addEnableStatusChangeListener(listener: OnEnableStatusChangeListener) {
        enableStatusListeners.add(listener)
    }

    fun removeEnableStatusChangeListener(listener: OnEnableStatusChangeListener) {
        enableStatusListeners.remove(listener)
    }

    var currPreset: Short = currentPreset
        private set

    override fun usePreset(preset: Short) {
        super.usePreset(preset)
        currPreset = preset
    }

    override fun release() {
        parameterListeners.clear()
        controlStatusListeners.clear()
        enableStatusListeners.clear()
        super.release()
    }
}

class XBassBoost(priority: Int, audioSessionId: Int) : BassBoost(priority, audioSessionId) {
    val maxRecommendedStrength = 19
    private var parameterListeners = mutableListOf<OnParameterChangeListener>()
    private var controlStatusListeners = mutableListOf<OnControlStatusChangeListener>()
    private var enableStatusListeners = mutableListOf<OnEnableStatusChangeListener>()

    init {
        setParameterListener { effect, status, param, value ->
            parameterListeners.forEach {
                it.onParameterChange(effect, status, param, value)
            }
        }
        setControlStatusListener { effect, controlGranted ->
            controlStatusListeners.forEach {
                it.onControlStatusChange(effect, controlGranted)
            }
        }
        setEnableStatusListener { effect, enabled ->
            enableStatusListeners.forEach {
                it.onEnableStatusChange(effect, enabled)
            }
        }
    }

    fun addParameterChangeListener(listener: OnParameterChangeListener) {
        parameterListeners.add(listener)
    }

    fun removeParameterChangeListener(listener: OnParameterChangeListener) {
        parameterListeners.remove(listener)
    }

    fun addControlStatusChangeListener(listener: OnControlStatusChangeListener) {
        controlStatusListeners.add(listener)
    }

    fun removeControlStatusChangeListener(listener: OnControlStatusChangeListener) {
        controlStatusListeners.remove(listener)
    }

    fun addEnableStatusChangeListener(listener: OnEnableStatusChangeListener) {
        enableStatusListeners.add(listener)
    }

    fun removeEnableStatusChangeListener(listener: OnEnableStatusChangeListener) {
        enableStatusListeners.remove(listener)
    }

    override fun setStrength(strength: Short) {
        super.setStrength((1000F / maxRecommendedStrength * strength).toShort())
    }

    override fun getRoundedStrength(): Short {
        return (super.getRoundedStrength() / (1000F / maxRecommendedStrength)).toShort()
    }

    override fun release() {
        parameterListeners.clear()
        controlStatusListeners.clear()
        enableStatusListeners.clear()
        super.release()
    }
}
