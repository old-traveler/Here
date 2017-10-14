package com.here.util;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.here.adapter.ImageAdapter;
import com.imnjh.imagepicker.control.BaseLoaderController;
import com.imnjh.imagepicker.loader.PhotoLoader;
import com.imnjh.imagepicker.model.Album;


/**
 * Created by Martin on 2017/1/17.
 */
public class PhotoController extends BaseLoaderController {

    private ImageAdapter photoAdapter;
    private static final String ARGS_ALBUM = "ARGS_ALBUM";



    public void onCreate(@NonNull Activity context, @NonNull RecyclerView recyclerView) {
      super.onCreate(context);
      photoAdapter = new ImageAdapter(context, null);
      recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
      recyclerView.setAdapter(photoAdapter);
    }

    @Override
    protected int getLoaderId() {
      return PHOTO_LOADER_ID;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      Album album = args.getParcelable(ARGS_ALBUM);
      if (album == null) {
        return null;
      }
      return PhotoLoader.newInstance(context, album);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
      photoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
      photoAdapter.swapCursor(null);
    }

    /**
     * @param album
     */
    public void load(Album album) {
      Bundle args = new Bundle();
      args.putParcelable(ARGS_ALBUM, album);
      loaderManager.initLoader(getLoaderId(), args, this);
    }


    /**
     *
     */
    public void loadAllPhoto(Context context) {
      Album album = new Album(Album.ALBUM_ID_ALL, -1,
          context.getString(Album.ALBUM_NAME_ALL_RES_ID), 0);
      load(album);
    }






}
