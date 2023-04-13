package com.xeinebiu.audioeffects.app

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xeinebiu.audioeffects.AudioEffectManager
import com.xeinebiu.audioeffects.AudioEffectViewHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var audioSessionId = 0
    private lateinit var audioEffectManager: AudioEffectManager
    private lateinit var audioEffectViewHelper: AudioEffectViewHelper
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioSessionId = setupMediaPlayer()
        audioEffectManager = AudioEffectManager(audioSessionId)
        audioEffectViewHelper = AudioEffectViewHelper(
            this,
            supportFragmentManager,
            audioEffectManager,
        )

        audioEffectManager.equalizer.addPropertiesChangeListener {
            println()
        }
        audioEffectManager.equalizer.addEnableStatusChangeListener {
            println()
        }
    }

    private fun setupMediaPlayer(): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.demo_music)
        mediaPlayer.start()
        mediaPlayer.setVolume(.1F, .1F)
        return mediaPlayer.audioSessionId
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
        audioEffectManager.release()
    }

    /**
     * Display [com.xeinebiu.audioeffects.views.AudioEffectView]
     * as [com.xeinebiu.audioeffects.dialogs.AudioEffectViewDialogFragment]
     * @author xeinebiu
     */
    fun asBottomSheet(view: View) {
        audioEffectViewHelper.showAsBottomSheet()
    }

    /**
     * Display [com.xeinebiu.audioeffects.views.AudioEffectView]
     * as [com.xeinebiu.audioeffects.dialogs.AudioEffectViewDialogFragment]
     * @author xeinebiu
     */
    fun asDialog(view: View) {
        audioEffectViewHelper.showAsDialog()
    }

    /**
     * Display [com.xeinebiu.audioeffects.views.AudioEffectView] inside the Root Container
     * @author xeinebiu
     */
    fun asView(view: View) {
        val tag = "asView"
        val exists: View? = container.findViewWithTag(tag)
        if (exists != null) return

        val audioEffectView = audioEffectViewHelper.asView(container)
        audioEffectView.tag = tag
        container.addView(audioEffectView)
    }
}
