package com.studenthackvii.dave;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "/peter.json")
@ConfigurationProperties
public class CartoonsFile {
  private CartoonItem[] cartoons;

  public CartoonItem[] getCartoons()
  {
      return cartoons;
  }

  public void setCartoons(CartoonItem[] cartoons)
  {
      this.cartoons = cartoons;
  }

  @Component
  public class CartoonItem {
      private String name;
      private String url;
      private int id;
      private String[] genre;
  }
}