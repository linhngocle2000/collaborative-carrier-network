package AuctioneerUI;

import Agent.CarrierAgent;
import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;

import javax.swing.*;

import Auction.Auction;
import Auction.TransportRequest;
import Auction.Bid;
import Auction.VickreyAuction;
import Utils.TourPlanning;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class StartAuctionUI extends JFrame {


    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    static final int MAX_T = Runtime.getRuntime().availableProcessors() + 1;
    private List<CarrierAgent> bidders;

    public StartAuctionUI() {

        bidders = HTTPRequests.getCarrierAgents();

///////////
// Frame
///////////

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setTitle("CCN");
        setSize(width,height);
        setMinimumSize(new Dimension(width,height));
        setLocationRelativeTo(null);

///////////
// Panels
///////////

        JPanel panel = new JPanel();
        panel.setBackground(background);
        panel.setLayout(new GridBagLayout());

        JLabel topLabel = new JLabel("Auction is running.");
        topLabel.setFont(font.deriveFont(Font.BOLD, 14));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        panel.add(topLabel, constraints);

        JLabel bottomLabel = new JLabel("Window closes automatically.");
        bottomLabel.setFont(font.deriveFont(Font.BOLD, 14));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panel.add(bottomLabel, constraints);

        getContentPane().add(panel);


        pack();

        setResizable(false);


    }

    public void checkCarrierList() {
        if (bidders==null || bidders.isEmpty()) {
            this.dispose();
            HTTPRequests.logout();
            System.exit(0);
        }
    }

    public void auctionOff() {
        checkCarrierList();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        for (CarrierAgent carrier : bidders) {
            Runnable r = new AuctionTask(carrier);
            pool.submit(r); // Use submit instead of execute
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not finish all auction tasks!
    }

    public void startAuctions() {
        checkCarrierList();
        HTTPRequests.resetCost();
        HTTPRequests.stashTransportRequests();
        for (int i = 0; i < 3; i++) 
        {
            List<Auction> auctions = HTTPRequests.getAllAuctions();
            if (auctions!=null && !auctions.isEmpty()) {
                for (Auction auction : auctions) {
                    auction.setAuctionStrategy(new VickreyAuction());
                    auction.start();
                    ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
                    for (CarrierAgent bidder : bidders) {
                        Runnable r = new BidTask(bidder, auction);
                        pool.submit(r); // Use submit instead of execute
                    }
                    pool.shutdown();
                    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not finish all bid tasks!
                    auction.end();
                    Bid winningBid = auction.getWinningBid();
                    if (winningBid == null) {
                        auction.notifyWinner();
                    }
                }
            }
        }
        HTTPRequests.resetAuction();
        this.dispose();
        HTTPRequests.logout();
    }


}

class BidTask implements Runnable
{
    private CarrierAgent carrier;
    private TransportRequest transReq;
    private Auction auction;

    public BidTask(CarrierAgent agent, Auction a)
    {
        this.carrier = agent;
        this.transReq = a.getDefaultTransportRequest();
        this.auction = a;
    }

    public void run()
    {
        TourPlanning tour = new TourPlanning(carrier);
        double price = tour.getProfit(transReq)-1000.00;
        if (price>=0) {
            Bid bid = HTTPRequests.addBid(auction, carrier, price);
            auction.addBid(bid);
        }
        System.out.println("Tour planning for carrier "+carrier.getUsername()+" and request " + transReq.getRouteString() +" completed");
    }
}

class AuctionTask implements Runnable
{
    private CarrierAgent carrier;

    public AuctionTask(CarrierAgent agent)
    {
        this.carrier = agent;
    }

    public void run()
    {
        TourPlanning tour = new TourPlanning(carrier);
        for (TransportRequest tr : tour.getRequests()) {
            if (tour.getProfit(tr)<=0) {
                Auction auction = HTTPRequests.addAuction();
                HTTPRequests.addTransportRequestToAuction(auction, tr);
                System.out.println("Request " + tr.getRouteString() + " sent to auction.");
            }
        }
        System.out.println("Requests of carrier "+carrier.getUsername()+" checked.");
    }
}
