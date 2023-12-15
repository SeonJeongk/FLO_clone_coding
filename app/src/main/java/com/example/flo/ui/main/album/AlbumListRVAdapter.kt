package com.example.flo.ui.main.album

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.FloAlbumResult
import com.example.flo.data.remote.FloAlbumSongs
import com.example.flo.databinding.ItemAlbumBinding

class AlbumListRVAdapter(val context: Context, val result: FloAlbumResult): RecyclerView.Adapter<AlbumListRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false )

        return ViewHolder(binding)
    }

    // 데이터 값 적용
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null){

        }else{
            Log.d("image", result.albums[position].coverImgUrl)
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(result.albums[position]) }  //클릭리스너 호출
    }

    override fun getItemCount(): Int = result.albums.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        val coverImg : ImageView = binding.itemAlbumCoverImgIv
        val title : TextView = binding.itemAlbumTitleTv
        val singer: TextView = binding.itemAlbumSingerTv
    }


    interface MyItemClickListener{
        fun onItemClick(albums: FloAlbumSongs)
    }

    //외부에서 리스너를 전달받아 어댑터에서 사용할 수 있게 mItemClickListener에 저장
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}