package com.xeinebiu.audioeffects.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AudioEffectViewBottomSheetDialogFragment : BottomSheetDialogFragment(), AudioEffectDialog {

    override var onCreateViewListener: ((LayoutInflater, ViewGroup?) -> View)? = null

    /**
     * Return passed view as content for Dialog
     * @author xeinebiu
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = onCreateViewListener?.invoke(inflater, container)

    /**
     * Make [Dialog] HALF EXPANDED by default
     * @author xeinebiu
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia: DialogInterface ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet: ViewGroup? = dialog.findViewById(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from<ViewGroup>(bottomSheet).state =
                    BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        return bottomSheetDialog
    }

    /**
     * Close [BottomSheetDialogFragment]
     */
    override fun close(): Unit = dismiss()
}
