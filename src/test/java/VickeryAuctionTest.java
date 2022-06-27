import static org.junit.jupiter.api.Assertions.assertEquals;

import Agent.CarrierAgent;
import Auction.Auction;
import Auction.Bid;
import Auction.VickreyAuction;
import org.junit.Before;
import org.junit.Test;

public class VickeryAuctionTest {

    Auction auction;
    CarrierAgent carrierTop;
    CarrierAgent carrierSecond;
    CarrierAgent carrierThird;
    Bid bidTop;
    Bid bidSecond;
    Bid bidThird;

    @Before
    public void setUp() {
        auction = new Auction(0,0);
        auction.setAuctionStrategy(new VickreyAuction());
        carrierTop = new CarrierAgent("test1", "test1", 0,0,0,0,0,0);
        carrierSecond = new CarrierAgent("test2", "test2", 0,0,0,0,0,0);
        carrierThird = new CarrierAgent("test3", "test3", 0,0,0,0,0,0);
        bidTop = new Bid(1,auction,carrierTop,100.0);
        bidSecond = new Bid(2,auction,carrierSecond,90.0);
        bidThird = new Bid(3,auction,carrierThird,80.0);
    }


    @Test
    public void testGetWinningBid() {
        auction.addBid(bidThird);
        auction.addBid(bidTop);
        auction.addBid(bidSecond);
        Bid winningBid = auction.getWinningBid();
        assertEquals(bidTop.getID(), winningBid.getID(), "ID of winning bid should be of highest bid");
        assertEquals(bidTop.getBidder().getUsername(), winningBid.getBidder().getUsername(), "Bidder of winning bid should be of highest bid");
        assertEquals(bidSecond.getPrice(), winningBid.getPrice(), "Price of winning bid should be of second highest bid");
    }
}
