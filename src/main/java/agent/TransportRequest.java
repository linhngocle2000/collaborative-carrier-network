package main.java.agent;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.util.Coordinate;

import java.util.List;
import java.util.UUID;


public class TransportRequest {

   private static double pickup_X, pickup_Y;
   private static double deliver_X, deliver_Y;

   private static String ID = null;

   public static List<Shipment> generateRequest(List<Shipment> requests) {
      int count = 0;
      while (count < 2) {
         pickup_X = Math.random() * 30;
         pickup_Y = Math.random() * 30;
         deliver_X = Math.random() * 30;
         deliver_Y = Math.random() * 30;

         ID = UUID.randomUUID().toString().substring(28);

         requests.add(Shipment.Builder.newInstance(ID).setPickupLocation(loc(Coordinate.newInstance(pickup_X, pickup_Y))).setDeliveryLocation(loc(Coordinate.newInstance(deliver_X, deliver_Y))).build());
         
         count++;
      }

      return requests;
   }
		
   private static Location loc(Coordinate coordinate) {
      return Location.Builder.newInstance().setCoordinate(coordinate).build();
   }
}
