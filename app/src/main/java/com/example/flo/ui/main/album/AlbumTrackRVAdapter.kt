package com.example.flo.ui.main.album

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.remote.TrackResponse
import com.example.flo.databinding.ItemAlbumTrackBinding

class AlbumTrackRVAdapter (val context: Context, val result: TrackResponse): RecyclerView.Adapter<AlbumTrackRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemAlbumTrackBinding =
            ItemAlbumTrackBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // 데이터 값 적용
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.songIdx.text = result.result[position].songIdx.toString()
        holder.title.text = result.result[position].title
        holder.singer.text = result.result[position].singer


        if (result.result[position].isTitleSong == "T") {
            holder.titleTextView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = result.result.size

    inner class ViewHolder(val binding: ItemAlbumTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val songIdx: TextView = binding.itemAlbumSongOrderTv
        val title: TextView = binding.itemAlbumTitleTv
        val singer: TextView = binding.itemAlbumSingerTv
        val titleTextView: TextView = binding.itemAlbumSongListTitleTv
    }


    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    //외부에서 리스너를 전달받아 어댑터에서 사용할 수 있게 mItemClickListener에 저장
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }
}