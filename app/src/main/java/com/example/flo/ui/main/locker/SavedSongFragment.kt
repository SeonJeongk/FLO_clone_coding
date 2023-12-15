package com.example.flo.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment(){

    lateinit var binding: FragmentSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!


        val bottomSheetFragment  = ConfirmDialog(this)
        binding.itemSavedNocheckTv.setOnClickListener{
            setSelectStatus(true)
            bottomSheetFragment .show(childFragmentManager, bottomSheetFragment .tag)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.savedsongSongListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        //like 상태 업데이트 : [...] 누르면 삭제
        songRVAdapter.setItemClickListener(object : SavedSongRVAdapter.OnItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })


        binding.savedsongSongListRv.adapter = songRVAdapter
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }

    private fun setSelectStatus(isSelecting : Boolean) {
        if(isSelecting) {   //전체선택
            binding.itemSavedCheckIv.visibility = View.VISIBLE
            binding.itemSavedCheckTv.visibility = View.VISIBLE
            binding.itemSavedNocheckIv.visibility = View.GONE
            binding.itemSavedNocheckTv.visibility = View.GONE
        }
        else {  //선택해제
            binding.itemSavedCheckIv.visibility = View.GONE
            binding.itemSavedCheckTv.visibility = View.GONE
            binding.itemSavedNocheckIv.visibility = View.VISIBLE
            binding.itemSavedNocheckTv.visibility = View.VISIBLE
        }
    }
}