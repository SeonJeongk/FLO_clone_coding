package com.example.flo.ui.main.album

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter(): RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {
    private var albums = ArrayList<Album>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    //클릭 리스너에 이벤트 저장
    private lateinit var mItemClickListener: MyItemClickListener

    //외부에서 실행할 클릭 이벤트 설정
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    //ViewHolder 생성시 호출 - item view 객체
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //item view 클릭 이벤트 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])

        // 더보기 누르면 삭제
        holder.binding.itemLockerAlbumBtnMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(albums[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = albums.size  //dataset 크기 알려줌

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>){
        this.albums.clear()
        this.albums.addAll(albums)
    }

    private fun removeSong(position: Int){  //item 삭제
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    //item view 객체를 담고있는 viewHolder
    inner class ViewHolder(val binding: ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.itemLockerAlbumTitleTv.text = album.title
            binding.itemLockerAlbumSingerTv.text = album.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}