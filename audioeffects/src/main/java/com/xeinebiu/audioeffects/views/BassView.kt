package com.xeinebiu.audioeffects.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import com.xeinebiu.audioeffects.AudioEffectManager
import com.xeinebiu.audioeffects.R
import com.xeinebiu.audioeffects.XBassBoost

/**
 * Bass View to work with given [audioEffectManager]
 * @author xeinebiu
 */
class BassView(
    private val context: Context,
    private val parent: ViewGroup?,
    private val audioEffectManager: AudioEffectManager
) {
    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val bassBoost: XBassBoost
        get() = audioEffectManager.bassBoost

    /**
     * Create [View] from given [audioEffectManager]
     * @author xeinebiu
     */
    fun createView(): View {
        val rootView = layoutInflater.inflate(R.layout.view_bass_boost, parent, false) as ViewGroup
        val bassLinearLayout: LinearLayoutCompat = rootView.findViewById(R.id.view_bass_boost_ll_container_bass_boost)

        initBass(bassLinearLayout)
        initSwitch(rootView)
        return rootView
    }

    /**
     * Create UI Views on [bassBoostContainer] for [bassBoost]
     * @author xeinebiu
     */
    private fun initBass(bassBoostContainer: LinearLayoutCompat) {
        if (!bassBoost.strengthSupported) return
        /* More than 20, the audio becomes very quiet and low */
        val max = bassBoost.maxRecommendedStrength
        val currentStrength = bassBoost.roundedStrength

        /* Create the layout */
        val bassView = layoutInflater.inflate(R.layout.item_bass, bassBoostContainer, false)

        /* Set minimum progress status to "0" */
        val minView: AppCompatTextView = bassView.findViewById(R.id.item_bass_tv_min)
        minView.text = currentStrength.toString()

        /* Set maximum status to [max] */
        val maxView: AppCompatTextView = bassView.findViewById(R.id.item_bass_tv_max)
        maxView.text = max.toString()

        /* Find the [AppCompatSeekBar] from [bassView] */
        val seekBar: AppCompatSeekBar = bassView.findViewById(R.id.item_bass_sb_progress)
        seekBar.max = max
        seekBar.progress = currentStrength.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bassBoost.setStrength(progress.toShort())
                minView.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        /* Add created layout into the given [bassBoostContainer] */
        bassBoostContainer.addView(bassView)
    }

    /**
     * Find the [SwitchCompat] from given [view] and connect with [bassBoost]
     * @author xeinebiu
     */
    private fun initSwitch(view: View) {
        val switch: SwitchCompat = view.findViewById(R.id.view_bass_sc_enable)
        switch.isChecked = bassBoost.enabled
        switch.setOnCheckedChangeListener { _, isChecked ->
            bassBoost.enabled = isChecked
        }
        bassBoost.setEnableStatusListener { _, enabled ->
            switch.isChecked = enabled
        }
    }
}
