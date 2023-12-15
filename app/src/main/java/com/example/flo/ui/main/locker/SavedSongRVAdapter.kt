package com.example.flo.ui.main.locker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemSavedsongBinding

class SavedSongRVAdapter(): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()
    //리스너 인터페이스
    interface OnItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    //클릭 리스너에 이벤트 저장
    private lateinit var songItemClickListener: OnItemClickListener

    //외부에서 실행할 클릭 이벤트 설정
    fun setItemClickListener(itemClickListener: OnItemClickListener){
        songItemClickListener = itemClickListener
    }


    //ViewHolder 생성시 호출 - item view 객체
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedsongBinding = ItemSavedsongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)  //item view 객체 return
    }

    //item view 클릭 이벤트 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])

        // 더보기 누르면 삭제
        holder.binding.itemSavedBtnMoreIv.setOnClickListener{
            songItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }

//        holder.binding.itemSavedBtnMoreIv.setOnClickListener{songItemClickListener.onRemoveSong(position)} //더보기 누르면 삭제

        holder.binding.itemSavedBtnPlayIv.setOnClickListener{   //노래 재생
            songs[position].isPlaying = true
            notifyDataSetChanged()
        }
        holder.binding.itemSavedBtnStopIv.setOnClickListener{   //노래 일시정지
            songs[position].isPlaying = false
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = songs.size   //dataset 크기 알려줌

    fun addSongs(songs: ArrayList<Song>){   //item 추가
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }
    private fun removeSong(position: Int){  //item 삭제
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    //item view 객체를 담고있는 viewHolder
    inner class ViewHolder(val binding: ItemSavedsongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song) {
            binding.itemSavedTitleTv.text = song.title
            binding.itemSavedSingerTv.text = song.singer
            binding.itemSavedCoverImgIv.setImageResource(song.coverImg!!)

            if (song.isPlaying) {
                binding.itemSavedBtnPlayIv.visibility = View.GONE
                binding.itemSavedBtnStopIv.visibility = View.VISIBLE
            } else {
                binding.itemSavedBtnPlayIv.visibility = View.VISIBLE
                binding.itemSavedBtnStopIv.visibility = View.GONE
            }
        }
    }
}