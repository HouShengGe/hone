package com.mc.app.hotel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class PictureBean {
    /**
     * items : [{"picture_big_width":750,"picture_original_height":576,"picture_small_network_url":"uploadFiles/2015/7/4/20150704111915_813d6f42c62-809f-4469-b243-52747d167421_small.png","picture_big_height":493,"picture_big_network_url":"uploadFiles/2015/7/4/20150704111915_813d6f42c62-809f-4469-b243-52747d167421_big.png","picture_thumbnail_size":10929,"picture_thumbnail_height":157,"picture_id":319,"picture_big_size":60299,"picture_date":"2015-07-04 11:19:15","picture_original_network_url":"uploadFiles/2015/7/4/20150704111915_813d6f42c62-809f-4469-b243-52747d167421.png","picture_small_width":360,"picture_original_width":876,"picture_small_size":20399,"picture_thumbnail_network_url":"uploadFiles/2015/7/4/20150704111915_813d6f42c62-809f-4469-b243-52747d167421_thumbnail.png","picture_small_height":236,"picture_thumbnail_width":240,"picture_delete":0,"picture_original_size":398985},{"picture_big_width":440,"picture_original_height":440,"picture_small_network_url":"uploadFiles/2015/7/4/20150704111915_496e8613000-d14c-4910-9d4e-ff57ff733fe2_small.jpg","picture_big_height":440,"picture_big_network_url":"uploadFiles/2015/7/4/20150704111915_496e8613000-d14c-4910-9d4e-ff57ff733fe2_big.jpg","picture_thumbnail_size":12448,"picture_thumbnail_height":240,"picture_id":320,"picture_big_size":30199,"picture_date":"2015-07-04 11:19:15","picture_original_network_url":"uploadFiles/2015/7/4/20150704111915_496e8613000-d14c-4910-9d4e-ff57ff733fe2.jpg","picture_small_width":360,"picture_original_width":440,"picture_small_size":23634,"picture_thumbnail_network_url":"uploadFiles/2015/7/4/20150704111915_496e8613000-d14c-4910-9d4e-ff57ff733fe2_thumbnail.jpg","picture_small_height":360,"picture_thumbnail_width":240,"picture_delete":0,"picture_original_size":28658},{"picture_big_width":439,"picture_original_height":394,"picture_small_network_url":"uploadFiles/2015/7/4/20150704111915_253c7c13816-50c5-4a53-981f-c235e7050874_small.jpg","picture_big_height":394,"picture_big_network_url":"uploadFiles/2015/7/4/20150704111915_253c7c13816-50c5-4a53-981f-c235e7050874_big.jpg","picture_thumbnail_size":11691,"picture_thumbnail_height":215,"picture_id":321,"picture_big_size":25505,"picture_date":"2015-07-04 11:19:15","picture_original_network_url":"uploadFiles/2015/7/4/20150704111915_253c7c13816-50c5-4a53-981f-c235e7050874.jpg","picture_small_width":360,"picture_original_width":439,"picture_small_size":21182,"picture_thumbnail_network_url":"uploadFiles/2015/7/4/20150704111915_253c7c13816-50c5-4a53-981f-c235e7050874_thumbnail.jpg","picture_small_height":323,"picture_thumbnail_width":240,"picture_delete":0,"picture_original_size":26470},{"picture_big_width":75,"picture_original_height":4590,"picture_small_network_url":"uploadFiles/2015/7/4/20150704111915_2572b033d0b-6e2c-4bf4-ad25-a5d56057d2a3_small.jpg","picture_big_height":750,"picture_big_network_url":"uploadFiles/2015/7/4/20150704111915_2572b033d0b-6e2c-4bf4-ad25-a5d56057d2a3_big.jpg","picture_thumbnail_size":3030,"picture_thumbnail_height":240,"picture_id":322,"picture_big_size":15845,"picture_date":"2015-07-04 11:19:16","picture_original_network_url":"uploadFiles/2015/7/4/20150704111915_2572b033d0b-6e2c-4bf4-ad25-a5d56057d2a3.jpg","picture_small_width":36,"picture_original_width":460,"picture_small_size":5201,"picture_thumbnail_network_url":"uploadFiles/2015/7/4/20150704111915_2572b033d0b-6e2c-4bf4-ad25-a5d56057d2a3_thumbnail.jpg","picture_small_height":360,"picture_thumbnail_width":24,"picture_delete":0,"picture_original_size":250292},{"picture_big_width":115,"picture_original_height":2868,"picture_small_network_url":"uploadFiles/2015/7/4/20150704111916_316daea8af-a69c-43d2-897d-8bdf409252ae_small.jpg","picture_big_height":750,"picture_big_network_url":"uploadFiles/2015/7/4/20150704111916_316daea8af-a69c-43d2-897d-8bdf409252ae_big.jpg","picture_thumbnail_size":5087,"picture_thumbnail_height":240,"picture_id":323,"picture_big_size":29976,"picture_date":"2015-07-04 11:19:16","picture_original_network_url":"uploadFiles/2015/7/4/20150704111916_316daea8af-a69c-43d2-897d-8bdf409252ae.jpg","picture_small_width":55,"picture_original_width":440,"picture_small_size":9316,"picture_thumbnail_network_url":"uploadFiles/2015/7/4/20150704111916_316daea8af-a69c-43d2-897d-8bdf409252ae_thumbnail.jpg","picture_small_height":360,"picture_thumbnail_width":36,"picture_delete":0,"picture_original_size":196699}]
     * totalrecords : 5
     */
    private List<Pictures> items;
    private int totalrecords;

    public void setItems(List<Pictures> items) {
        this.items = items;
    }

    public void setTotalrecords(int totalrecords) {
        this.totalrecords = totalrecords;
    }

    public List<Pictures> getItems() {
        return items;
    }

    public int getTotalrecords() {
        return totalrecords;
    }

    public class Pictures {
        /**
         * picture_big_width : 562
         * picture_original_height : 1136
         * picture_small_network_url : uploadFiles/2015/7/3/20150703113410_34485b4fb91-fd27-43e0-ac17-4c3b0a4054e3_small.jpg
         * picture_pertain_type : 2
         * picture_big_network_url : uploadFiles/2015/7/3/20150703113410_34485b4fb91-fd27-43e0-ac17-4c3b0a4054e3_big.jpg
         * picture_big_height : 750
         * picture_thumbnail_size : 15396
         * picture_id : 287
         * picture_thumbnail_height : 240
         * picture_date : 2015-07-03 11:34:10
         * picture_big_size : 82828
         * picture_original_network_url : uploadFiles/2015/7/3/20150703113410_34485b4fb91-fd27-43e0-ac17-4c3b0a4054e3.jpg20150703113410_34485b4fb91-fd27-43e0-ac17-4c3b0a4054e3.jpg
         * picture_small_width : 270
         * picture_original_width : 852
         * picture_small_size : 28022
         * picture_pertain_id : 435
         * picture_thumbnail_network_url : uploadFiles/2015/7/3/20150703113410_34485b4fb91-fd27-43e0-ac17-4c3b0a4054e3_thumbnail.jpg
         * picture_small_height : 360
         * picture_thumbnail_width : 180
         * picture_delete : 0
         * picture_original_size : 147638
         */
        private int picture_big_width;
        private int picture_original_height;
        private String picture_small_network_url;
        private int picture_pertain_type;
        private String picture_big_network_url;
        private int picture_big_height;
        private int picture_thumbnail_size;
        private int picture_id;
        private int picture_thumbnail_height;
        private String picture_date;
        private int picture_big_size;
        private String picture_original_network_url;
        private int picture_small_width;
        private int picture_original_width;
        private int picture_small_size;
        private int picture_pertain_id;
        private String picture_thumbnail_network_url;
        private int picture_small_height;
        private int picture_thumbnail_width;
        private int picture_delete;
        private int picture_original_size;
        private String loca_picture;

        //新添字段 app资源文件中图片的id 并非服务器传递字段
        private int picture_resource_id;

        public int getPicture_resource_id() {
            return picture_resource_id;
        }

        public void setPicture_resource_id(int picture_resource_id) {
            this.picture_resource_id = picture_resource_id;
        }

        public String getLoca_picture() {
            return loca_picture;
        }

        public void setLoca_picture(String loca_picture) {
            this.loca_picture = loca_picture;
        }

        public void setPicture_big_width(int picture_big_width) {
            this.picture_big_width = picture_big_width;
        }

        public void setPicture_original_height(int picture_original_height) {
            this.picture_original_height = picture_original_height;
        }

        public void setPicture_small_network_url(String picture_small_network_url) {
            this.picture_small_network_url = picture_small_network_url;
        }

        public void setPicture_pertain_type(int picture_pertain_type) {
            this.picture_pertain_type = picture_pertain_type;
        }

        public void setPicture_big_network_url(String picture_big_network_url) {
            this.picture_big_network_url = picture_big_network_url;
        }

        public void setPicture_big_height(int picture_big_height) {
            this.picture_big_height = picture_big_height;
        }

        public void setPicture_thumbnail_size(int picture_thumbnail_size) {
            this.picture_thumbnail_size = picture_thumbnail_size;
        }

        public void setPicture_id(int picture_id) {
            this.picture_id = picture_id;
        }

        public void setPicture_thumbnail_height(int picture_thumbnail_height) {
            this.picture_thumbnail_height = picture_thumbnail_height;
        }

        public void setPicture_date(String picture_date) {
            this.picture_date = picture_date;
        }

        public void setPicture_big_size(int picture_big_size) {
            this.picture_big_size = picture_big_size;
        }

        public void setPicture_original_network_url(String picture_original_network_url) {
            this.picture_original_network_url = picture_original_network_url;
        }

        public void setPicture_small_width(int picture_small_width) {
            this.picture_small_width = picture_small_width;
        }

        public void setPicture_original_width(int picture_original_width) {
            this.picture_original_width = picture_original_width;
        }

        public void setPicture_small_size(int picture_small_size) {
            this.picture_small_size = picture_small_size;
        }

        public void setPicture_pertain_id(int picture_pertain_id) {
            this.picture_pertain_id = picture_pertain_id;
        }

        public void setPicture_thumbnail_network_url(String picture_thumbnail_network_url) {
            this.picture_thumbnail_network_url = picture_thumbnail_network_url;
        }

        public void setPicture_small_height(int picture_small_height) {
            this.picture_small_height = picture_small_height;
        }

        public void setPicture_thumbnail_width(int picture_thumbnail_width) {
            this.picture_thumbnail_width = picture_thumbnail_width;
        }

        public void setPicture_delete(int picture_delete) {
            this.picture_delete = picture_delete;
        }

        public void setPicture_original_size(int picture_original_size) {
            this.picture_original_size = picture_original_size;
        }

        public int getPicture_big_width() {
            return picture_big_width;
        }

        public int getPicture_original_height() {
            return picture_original_height;
        }

        public String getPicture_small_network_url() {
            return picture_small_network_url;
        }

        public int getPicture_pertain_type() {
            return picture_pertain_type;
        }

        public String getPicture_big_network_url() {
            return picture_big_network_url;
        }

        public int getPicture_big_height() {
            return picture_big_height;
        }

        public int getPicture_thumbnail_size() {
            return picture_thumbnail_size;
        }

        public int getPicture_id() {
            return picture_id;
        }

        public int getPicture_thumbnail_height() {
            return picture_thumbnail_height;
        }

        public String getPicture_date() {
            return picture_date;
        }

        public int getPicture_big_size() {
            return picture_big_size;
        }

        public String getPicture_original_network_url() {
            return picture_original_network_url;
        }

        public int getPicture_small_width() {
            return picture_small_width;
        }

        public int getPicture_original_width() {
            return picture_original_width;
        }

        public int getPicture_small_size() {
            return picture_small_size;
        }

        public int getPicture_pertain_id() {
            return picture_pertain_id;
        }

        public String getPicture_thumbnail_network_url() {
            return picture_thumbnail_network_url;
        }

        public int getPicture_small_height() {
            return picture_small_height;
        }

        public int getPicture_thumbnail_width() {
            return picture_thumbnail_width;
        }

        public int getPicture_delete() {
            return picture_delete;
        }

        public int getPicture_original_size() {
            return picture_original_size;
        }

        @Override
        public String toString() {
            return "Pictures{" +
                    "picture_big_width=" + picture_big_width +
                    ", picture_original_height=" + picture_original_height +
                    ", picture_small_network_url='" + picture_small_network_url + '\'' +
                    ", picture_pertain_type=" + picture_pertain_type +
                    ", picture_big_network_url='" + picture_big_network_url + '\'' +
                    ", picture_big_height=" + picture_big_height +
                    ", picture_thumbnail_size=" + picture_thumbnail_size +
                    ", picture_id=" + picture_id +
                    ", picture_thumbnail_height=" + picture_thumbnail_height +
                    ", picture_date='" + picture_date + '\'' +
                    ", picture_big_size=" + picture_big_size +
                    ", picture_original_network_url='" + picture_original_network_url + '\'' +
                    ", picture_small_width=" + picture_small_width +
                    ", picture_original_width=" + picture_original_width +
                    ", picture_small_size=" + picture_small_size +
                    ", picture_pertain_id=" + picture_pertain_id +
                    ", picture_thumbnail_network_url='" + picture_thumbnail_network_url + '\'' +
                    ", picture_small_height=" + picture_small_height +
                    ", picture_thumbnail_width=" + picture_thumbnail_width +
                    ", picture_delete=" + picture_delete +
                    ", picture_original_size=" + picture_original_size +
                    ", loca_picture='" + loca_picture + '\'' +
                    ", picture_resource_id=" + picture_resource_id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PictureBean{" +
                "items=" + items +
                ", totalrecords=" + totalrecords +
                '}';
    }
}
