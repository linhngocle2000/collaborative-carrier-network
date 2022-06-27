package Auction;

import java.util.ArrayList;
import java.util.List;

public class Bundle {
   private List<TransportRequest> requests;
   
   public Bundle(List<TransportRequest> requests) {
      this.requests = new ArrayList<>(requests);
   }

   public List<TransportRequest> getRequests() {
      return this.requests;
   }

}
