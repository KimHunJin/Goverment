package com.sku.archbiz2015.utils;


import java.util.HashMap;

/**
 * Created by hesk on 19/8/15.
 */
public class DataProvider {
    public static HashMap<String, String> getFileSrcHorizontal() {
        HashMap<String, String> file_maps = new HashMap<>();

        file_maps.put("Test1","https://s3-us-west-2.amazonaws.com/valueupmos/1");
        file_maps.put("Test2","https://s3-us-west-2.amazonaws.com/valueupmos/2");
        file_maps.put("Test3","https://s3-us-west-2.amazonaws.com/valueupmos/3");
        file_maps.put("Test4","https://s3-us-west-2.amazonaws.com/valueupmos/4");
        return file_maps;
    }

    public static HashMap<String, Integer> getMapSource() {
        HashMap<String, Integer> map_maps = new HashMap<>();
        return map_maps;
    }
}
