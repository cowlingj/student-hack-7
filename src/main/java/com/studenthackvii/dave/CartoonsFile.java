package com.studenthackvii.dave;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class CartoonsFile {
  private List<CartoonItem> cartoons = new ArrayList<>();

  public List<CartoonItem> getCartoons()
  {
      return cartoons;
  }

  public void setCartoons(List<CartoonItem> cartoons)
  {
      this.cartoons = cartoons;
  }

  public static class CartoonItem {
      private String name;
        private String url;
        private int id;
        private String[] genre;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the genre
         */
        public String[] getGenre() {
            return genre;
        }

        /**
         * @param genre the genre to set
         */
        public void setGenre(String[] genre) {
            this.genre = genre;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }


  }
}