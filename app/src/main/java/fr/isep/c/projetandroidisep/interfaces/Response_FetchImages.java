package fr.isep.c.projetandroidisep.interfaces;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;

public interface Response_FetchImages
{
    void processFinish_fetchImages(Bitmap img, String url);
}