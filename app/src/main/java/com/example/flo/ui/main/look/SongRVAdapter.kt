package com.example.flo.ui.main.look

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.FloChartResult
import com.example.flo.databinding.ItemSongBinding

class SongRVAdapter(val context: Context, val result: FloChartResult): RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false )

        return ViewHolder(binding)
    }

    // 데이터 값 적용
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       if(result.songs[position].coverImgUrl == "" || result.songs[position].coverImgUrl == null){
            Log.d("image", "No data")
       }else{
           Log.d("image", result.songs[position].coverImgUrl)
           Glide.with(context).load(result.songs[position].coverImgUrl).into(holder.coverImg)
       }
        holder.title.text = result.songs[position].title
        holder.singer.text = result.songs[position].singer
    }

    override fun getItemCount(): Int = result.songs.size

    inner class ViewHolder(val binding: ItemSongBinding):RecyclerView.ViewHolder(binding.root){
        val coverImg : ImageView = binding.itemSongCoverImgIv
        val title : TextView = binding.itemSongTitleTv
        val singer: TextView = binding.itemSongSingerTv
    }


    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    //외부에서 리스너를 전달받아 어댑터에서 사용할 수 있게 mItemClickListener에 저장
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}