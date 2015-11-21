package com.sku.archbiz2015.utils;


import java.util.HashMap;

/**
 * Created by hesk on 19/8/15.
 */
public class DataProvider {

    HashMap<String, String> file_maps;

    public DataProvider() {
        file_maps = new HashMap<>();
    }

    public HashMap<String, String> getFileSrcHorizontal() {
        return file_maps;
    }

    public void setHashFile(String fileNmae, String url) {

        file_maps.put(fileNmae, url);
    }
}
