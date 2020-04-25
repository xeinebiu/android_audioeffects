package com.xeinebiu.audioeffects.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Dialog which represents an AudioEffects Page
 * @author xeinebiu
 */
interface AudioEffectDialog {

    var onCreateViewListener: ((LayoutInflater, ViewGroup?) -> View)?

    /**
     * Close Dialog
     * @author xeinebiu
     */
    fun close()
}
