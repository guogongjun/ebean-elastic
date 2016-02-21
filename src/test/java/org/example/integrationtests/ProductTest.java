package org.example.integrationtests;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import org.example.EmbeddedElasticServer;
import org.example.domain.Country;
import org.example.domain.Order;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;


public class ProductTest {

  private void indexCountries() {

    Query<Country> query = Ebean.find(Country.class)
        .where().query();

    Ebean.getDefaultServer().docStore().indexByQuery(query);

  }

  @Test
  public void testDb() throws InterruptedException {

    EmbeddedElasticServer server = new EmbeddedElasticServer();


    ResetBasicData.reset();
    //indexCountries();

    //if (true) {
    //  return;
    //}

    Query<Order> query = Ebean.find(Order.class)
        .where().gt("id", 1)
        .query();

    Ebean.getDefaultServer().docStore().indexByQuery(query);


    Thread.sleep(2000);

    Order order = Ebean.getDefaultServer().docStore().getById(Order.class, 2);

    assertNotNull(order);
    assertThat(order.getId()).isEqualTo(2);
  }
}