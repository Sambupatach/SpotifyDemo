package com.lowes.demoapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lowes.demoapp.R
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.domain.model.Artist

private const val TAG = "AlbumsRecyclerAdapter"
class AlbumsRecyclerAdapter(var albums : List<Album>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_layout, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var album = albums.get(position)
        (holder as AlbumViewHolder).title.text = album.name
        (holder as AlbumViewHolder).artists.text = getArtistsNames(album.artists)
        val requestOptions = RequestOptions
            .overrideOf(200, 200)
        Glide.with(holder.itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(album!!.images!!.get(0).url)
            .into((holder as AlbumViewHolder).image)

    }
    public fun updateAlbums(albums : List<Album>){
        Log.d(TAG,"setAlbums: $albums")
        this.albums = albums
    }
    override fun getItemCount(): Int {
        return albums!!.size
    }

    private fun getArtistsNames(artists : List<Artist>?) : String{
        Log.d(TAG,"getArtistsNames")
        var ret = ""
        artists?.forEach { ret += it.name+", " }
        Log.d(TAG,"ret:$ret")
        if(!ret.isEmpty()) ret = ret.dropLast(2)
        return ret
    }
    inner class AlbumViewHolder (var view : View) :RecyclerView.ViewHolder(view) {
        var image : ImageView
        lateinit var title : TextView
        lateinit var artists : TextView
        init{
            image = view.findViewById(R.id.album_image)
            title = view.findViewById(R.id.title)
            artists = view.findViewById(R.id.artists)
        }
    }
}