package com.xeinebiu.audioeffects

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.xeinebiu.audioeffects.dialogs.AudioEffectViewBottomSheetDialogFragment
import com.xeinebiu.audioeffects.dialogs.AudioEffectViewDialogFragment
import com.xeinebiu.audioeffects.views.AudioEffectView

/**
 * Helper class to display the [AudioEffectView] on different ways
 * @author xeinebiu
 */
class AudioEffectViewHelper constructor(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val audioEffectManager: AudioEffectManager
) {
    /**
     * Create an instance of [AudioEffectView] from given [parent]
     * @author xeinebiu
     */
    fun asView(parent: ViewGroup? = null): View =
        createView(fragmentManager, parent).createView()

    /**
     * Display [AudioEffectView] on a [AudioEffectViewBottomSheetDialogFragment]
     * @author xeinebiu
     */
    fun showAsBottomSheet(): DialogFragment {
        val dialog = AudioEffectViewBottomSheetDialogFragment()
        dialog.onCreateViewListener = { _, viewGroup ->
            createView(dialog.childFragmentManager, viewGroup).createView()
        }
        dialog.show(fragmentManager, null)
        return dialog
    }

    /**
     * Display [AudioEffectView] on a [AudioEffectViewDialogFragment]
     */
    fun showAsDialog(): DialogFragment {
        val dialog = AudioEffectViewDialogFragment()
        dialog.onCreateViewListener = { _, viewGroup ->
            createView(dialog.childFragmentManager, viewGroup).createView()
        }
        dialog.show(fragmentManager, null)
        return dialog
    }

    private fun createView(
        fragmentManager: FragmentManager,
        parent: ViewGroup?
    ): AudioEffectView =
        AudioEffectView(fragmentManager, context, parent, audioEffectManager)
}
