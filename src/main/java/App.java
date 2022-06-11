import Agent.Agent;
import AuctioneerUI.StartAuctionUI;
import CarrierUI.AdministrationUI;
import CarrierUI.CarrierLoginUI;
import StartUI.LoginUI;
import StartUI.RegisterUI;
import StartUI.WelcomeUI;
import CarrierUI.JoinAuctionUI;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.Utils;
import javax.swing.*;
import java.util.ArrayList;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static JoinAuctionUI joinAuctionUI;
    private static StartAuctionUI auctioneerUI;
    private static AdministrationUI adminUI;

    private static Agent user;


    static public void main(String[] args) {

        welcomeUI = new WelcomeUI();
        loginUI = new LoginUI();
        registerUI = new RegisterUI();
        joinAuctionUI = new JoinAuctionUI();
        auctioneerUI = new StartAuctionUI();

        welcomeUI = new WelcomeUI();
        welcomeUI.setVisible(true);

        JButton welcomeLoginBtn = welcomeUI.getLoginBtn();
        welcomeLoginBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            loginUI.setVisible(true);
        });

        JButton welcomeRegisterBtn = welcomeUI.getRegisterBtn();
        welcomeRegisterBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            registerUI.setVisible(true);
        });

        JButton loginBackBtn = loginUI.getBackBtn();
        loginBackBtn.addActionListener(e -> {
            loginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        JButton registerRegisterBtn = registerUI.getRegisterBtn();
        registerRegisterBtn.addActionListener(e -> {
            try {
                registerUI.setErrorLabel("");
                if (!registerUI.areAllFieldsFilled()) {
                    throw new Exception("Please fill out all fields.");
                }
                String name = registerUI.getNameText();
                String username = registerUI.getUsernameText();
                String password = registerUI.getPasswordText();
                boolean isAuctioneer = registerUI.isAuctioneer();
                if (!isAuctioneer) {
                    String transReq = registerUI.getTrText();
                    float depotX = registerUI.getDepotLatText();
                    float depotY = registerUI.getDepotLonText();
                    float pickupBaserate = registerUI.getBaseRateAText();
                    float externalTravelCost = registerUI.getBasePriceText();
                    float loadBaserate = registerUI.getBaseRateBText();
                    float internalTravelCost = registerUI.getBaseInRateText();
                    if(!registerUI.verifyTRInput()) {
                        throw new Exception("Transport requests are not entered correctly.");
                    }
                    if (!registerUI.verifyPriceInput()) {
                        throw new Exception("Price is not entered correctly.");
                    }
                    if (!registerUI.verifyDepotInput()) {
                        throw new Exception("Depot is not entered correctly.");
                    }
                    if (!HTTPRequests.registerCarrier(name, username, password, depotX, depotY, pickupBaserate, externalTravelCost, loadBaserate, internalTravelCost)) {
                        throw new Exception("Username " + username + " is already used.");
                    }
                    ArrayList<Float> tr = Utils.convertStringToTR(transReq);
                    Agent agent = HTTPRequests.login(username, password);
                    for (int i = 0; i<(tr.size()); i+=4) {
                        HTTPRequests.addTransportRequest(agent, tr.get(i), tr.get(i+1), tr.get(i+2), tr.get(i+3));
                    }
                } else {
                    if (!HTTPRequests.registerAuctioneer(name, username, password)) {
                        throw new Exception("Username " + username + " is already used.");
                    }
                }
                registerUI.setErrorLabel("");
                registerUI.showSuccessLabel();
                registerUI.deactivate();
            } catch (Exception ex) {
                registerUI.setErrorLabel(ex.getMessage());
            }
        });

        JButton registerBackBtn = registerUI.getBackBtn();
        registerBackBtn.addActionListener(e -> {
            registerUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        JButton loginLoginBtn = loginUI.getLoginBtn();
        loginUI.getRootPane().setDefaultButton(loginLoginBtn);
        loginLoginBtn.addActionListener(e -> {
            String username = loginUI.getNameText();
            String password = loginUI.getPasswordText();
            user = HTTPRequests.login(username, password);
            if (user == null) {
                loginUI.setErrorLabel("Incorrect username/password.");
                return;
            }
            loginUI.setVisible(false);
            loginUI.reset();
            if (user.isAuctioneer()) {
                auctioneerUI.setNameLabel(user.getDisplayname());
                auctioneerUI.setVisible(true);
            } else {
                adminUI = new AdministrationUI(user);
                joinAuctionUI.setNameLabel(user.getDisplayname());
                joinAuctionUI.setVisible(true);
            }
        });

        JButton carrierJoinAuctionMyTRBtn = joinAuctionUI.getMyTRBtn();
        carrierJoinAuctionMyTRBtn.addActionListener(e -> {
            adminUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();

        });

        JButton carrierJoinAuctionLogoutBtn = joinAuctionUI.getLogoutBtn();
        carrierJoinAuctionLogoutBtn.addActionListener(e -> {
            adminUI.setVisible(false);
            joinAuctionUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();
        });

        JButton auctioneerLogoutBtn = auctioneerUI.getLogoutBtn();
        auctioneerLogoutBtn.addActionListener(e -> {
            auctioneerUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();
        });

    }
}
