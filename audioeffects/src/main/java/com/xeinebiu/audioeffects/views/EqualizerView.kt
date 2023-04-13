package com.xeinebiu.audioeffects.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.*
import com.xeinebiu.audioeffects.AudioEffectManager
import com.xeinebiu.audioeffects.R
import com.xeinebiu.audioeffects.XEqualizer

/**
 * Equalizer View to work with given [audioEffectManager]
 * @author xeinebiu
 */
class EqualizerView(
    private val context: Context,
    private val parent: ViewGroup?,
    private val audioEffectManager: AudioEffectManager,
) {
    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val equalizer: XEqualizer
        get() = audioEffectManager.equalizer

    /* Contains all [SeekBar] representing a single band */
    private val seekBars: MutableMap<Short, BandLevel> = HashMap()

    /**
     * Create [View] from given [audioEffectManager]
     * @author xeinebiu
     */
    fun createView(): View {
        val rootView = layoutInflater.inflate(
            R.layout.view_equalizer,
            parent,
            false,
        ) as ViewGroup

        val bandsLinearLayout: LinearLayoutCompat = rootView.findViewById(
            R.id.view_equalizer_ll_container_bands,
        )

        val presetsLinearLayout: LinearLayoutCompat = rootView.findViewById(
            R.id.view_equalizer_ll_container_presets,
        )

        initSwitch(rootView)
        initBands(bandsLinearLayout)
        initPresets(presetsLinearLayout)

        return rootView
    }

    /**
     * Create UI Views on [bandsLinearLayout] for [equalizer]
     * @author xeinebiu
     */
    private fun initBands(bandsLinearLayout: LinearLayoutCompat) {
        val numberFrequencyBands: Short = equalizer.numberOfBands
        val lowerEqualizerBandLevelMilliBel: Short = equalizer.bandLevelRange[0]
        val upperEqualizerBandLevelMilliBel: Short = equalizer.bandLevelRange[1]

        for (i in 0 until numberFrequencyBands) {
            val equalizerBandIndex = i.toShort()

            val bandItemView = layoutInflater.inflate(R.layout.item_band, parent, false)

            val frequencyHeaderTextView: AppCompatTextView = bandItemView.findViewById(
                R.id.item_band_tv_frequency,
            )

            frequencyHeaderTextView.text = readableHertz(
                equalizer.getCenterFreq(equalizerBandIndex),
            )

            val lowerEqualizerBandLevelTextView: AppCompatTextView = bandItemView.findViewById(
                R.id.item_band_tv_min,
            )

            lowerEqualizerBandLevelTextView.text = readableDb(
                lowerEqualizerBandLevelMilliBel,
            )

            val upperEqualizerBandLevelTextView: AppCompatTextView = bandItemView.findViewById(
                R.id.item_band_tv_max,
            )

            upperEqualizerBandLevelTextView.text = readableDb(
                upperEqualizerBandLevelMilliBel,
            )

            val seekBar: AppCompatSeekBar = bandItemView.findViewById(R.id.item_band_sb_progress)
            seekBar.max = upperEqualizerBandLevelMilliBel - lowerEqualizerBandLevelMilliBel
            val bandLevel = BandLevel(
                seekBar,
                equalizerBandIndex,
                lowerEqualizerBandLevelMilliBel,
                upperEqualizerBandLevelMilliBel,
            )
            seekBars[equalizerBandIndex] = bandLevel
            setProgress(bandLevel)

            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    equalizer.setBandLevel(
                        equalizerBandIndex,
                        (progress + lowerEqualizerBandLevelMilliBel).toShort(),
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
            })

            bandsLinearLayout.addView(bandItemView)
        }
    }

    /**
     * Find the [SwitchCompat] from given [view] and connect with [equalizer]
     * @author xeinebiu
     */
    private fun initSwitch(view: View) {
        val switch: SwitchCompat = view.findViewById(R.id.view_equalizer_sc_enable)
        switch.isChecked = equalizer.enabled
        switch.setOnCheckedChangeListener { _, isChecked ->
            equalizer.enabled = isChecked
        }
        equalizer.addEnableStatusChangeListener {
            switch.isChecked = it
        }
    }

    /**
     * Create UI Views which represents [equalizer] presets
     * @author xeinebiu
     */
    private fun initPresets(presetsLinearLayout: LinearLayoutCompat) {
        val presetsCount = equalizer.numberOfPresets
        val currentPreset = equalizer.currPreset

        /* Reference to the [checkIcon] so it can be easily switched */
        var currentPresetCheckImage: AppCompatImageView? = null
        for (i in 0 until presetsCount) {
            val preset = i.toShort()

            /* Create [view] which represents single [preset] */
            val view = layoutInflater.inflate(R.layout.item_preset, presetsLinearLayout, false)

            /* Find [AppCompatTextView] inside [view] to show the [title] of [Preset] */
            val titleView: AppCompatTextView = view.findViewById(R.id.item_preset_tv_title)
            titleView.text = equalizer.getPresetName(preset)

            /* Find [AppCompatImageView] inside [view] to show the [check] drawable */
            val checkIcon: AppCompatImageView = view.findViewById(R.id.item_preset_iv_check)

            /* Show [checkIcon] only if the [preset] is [currentPreset] */
            checkIcon.visibility = if (currentPreset == preset) {
                currentPresetCheckImage = checkIcon
                View.VISIBLE
            } else {
                View.GONE
            }

            /* Listen for [preset] click */
            view.setOnClickListener {
                /* Hide the image of previous [preset] */
                currentPresetCheckImage?.visibility = View.GONE

                /* Set the image reference to new selected [preset] */
                currentPresetCheckImage = checkIcon
                checkIcon.visibility = View.VISIBLE

                /* Tell [equalizer] to use the new [preset] */
                equalizer.usePreset(preset)

                /* Update values of [seekbars] */
                updateSeekBars()
            }
            /* Add created [view] on container */
            presetsLinearLayout.addView(view)
        }
    }

    /**
     * Update [seekBars] with current state of [equalizer]
     * @author xeinebiu
     */
    private fun updateSeekBars() {
        val numberFrequencyBands: Short = equalizer.numberOfBands
        for (i in 0 until numberFrequencyBands) {
            val equalizerBandIndex = i.toShort()
            updateSeekBar(equalizerBandIndex)
        }
    }

    /**
     * Update [seekBars] for given [equalizerBandIndex]
     * @author xeinebiu
     */
    private fun updateSeekBar(equalizerBandIndex: Short) {
        val bandLevel = seekBars[equalizerBandIndex] ?: return
        setProgress(bandLevel)
    }

    /**
     * Set progress of [bandLevel] from given [equalizerBandIndex] of [equalizer]
     * @author xeinebiu
     */
    private fun setProgress(bandLevel: BandLevel) {
        val level = equalizer.getBandLevel(bandLevel.index).toInt()
        bandLevel.seekBar.progress = level + bandLevel.maxBandLevel
    }

    companion object {
        /**
         * Convert given [millihertz] to a readable Text
         */
        private fun readableHertz(millihertz: Int): String = "${millihertz / 1000}Hz"

        /**
         * Convert given [milliBel] to readable Text
         * @author xeinebiu
         */
        private fun readableDb(milliBel: Short): String = "${milliBel / 100}dB"
    }

    private data class BandLevel constructor(
        val seekBar: AppCompatSeekBar,
        val index: Short,
        val lowestBandLevel: Short,
        val maxBandLevel: Short,
    )
}
