package com.example.flo.ui.main.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemAlbumBinding

//datalist를 매개변수로 받아오고, Adapter class를 상속받음
class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    //인터페이스
    //어댑터 외부의 HomeFragment에서 클릭 이벤트 처리가 가능
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onBtnClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    //외부에서 리스너를 전달받아 어댑터에서 사용할 수 있게 mItemClickListener에 저장
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album){  //item 추가
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){  //item 삭제
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    //ViewHolder 생성시 호출 - item view 객체
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)  //item view 객체 return
    }

    //item view 객체 뷰홀더와 연결 및 클릭 이벤트 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])    //viewHolder로 item view 객체 넘겨줘
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(albumList[position]) }  //클릭리스너 호출
        holder.binding.itemAlbumPlayingImgIv.setOnClickListener { mItemClickListener.onBtnClick(albumList[position]) } //앨범 재생 클릭
    }

    //dataset 크기 알려줌
    override fun getItemCount(): Int = albumList.size

    //item view 객체를 담고있는 viewHolder
    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }

}