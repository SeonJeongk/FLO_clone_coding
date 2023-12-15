package com.example.flo.ui.main.locker

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.flo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmDialog(private val savedSongFragment: SavedSongFragment) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_bottomsheet, container, false)

        // 레이아웃 배경 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val delete: ImageView = view.findViewById(R.id.dialog_delete_iv)
        delete.setOnClickListener {
            savedSongFragment.binding.itemSavedCheckIv.visibility = View.GONE
            savedSongFragment.binding.itemSavedCheckTv.visibility = View.GONE
            savedSongFragment.binding.itemSavedNocheckIv.visibility = View.VISIBLE
            savedSongFragment.binding.itemSavedNocheckTv.visibility = View.VISIBLE
            dismiss()
        }

        return view
    }
}