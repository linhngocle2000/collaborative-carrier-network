import Agent.Agent;
import Agent.CarrierAgent;
import Agent.AuctioneerAgent;
import Agent.AgentFactory;
import AuctioneerUI.StartAuctionUI;
import CarrierUI.AdministrationUI;
import StartUI.LoginUI;
import StartUI.RegisterUI;
import StartUI.WelcomeUI;
import CarrierUI.JoinAuctionUI;
import UIResource.HTTPResource.HTTPRequests;
import Utils.Converter;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static JoinAuctionUI joinAuctionUI;
    private static StartAuctionUI auctioneerUI;
    private static AdministrationUI adminUI;

    private static AuctioneerAgent auctioneer;
    private static CarrierAgent carrier;


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
                    ArrayList<Float> tr = Converter.convertStringToTR(transReq);
                    carrier = AgentFactory.carrierFromJSON(Objects.requireNonNull(HTTPRequests.login(username, password)));
                    for (int i = 0; i<(tr.size()); i+=4) {
                        HTTPRequests.addTransportRequest(carrier, tr.get(i), tr.get(i+1), tr.get(i+2), tr.get(i+3));
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
            loginUI.setErrorLabel("");
            String username = loginUI.getNameText();
            String password = loginUI.getPasswordText();
            JSONObject data = HTTPRequests.login(username, password);
            if (data == null) {
                loginUI.setErrorLabel("Incorrect username/password.");
                return;
            }
            if (data.getBoolean("IsAuctioneer")) {
                auctioneer = AgentFactory.auctioneerFromJSON(data);
                auctioneerUI.setNameLabel(auctioneer.getDisplayname());
                auctioneerUI.setVisible(true);
            } else {
                carrier = AgentFactory.carrierFromJSON(data);
                adminUI = new AdministrationUI(carrier);
                joinAuctionUI.setNameLabel(carrier.getDisplayname());
                joinAuctionUI.setVisible(true);
            }
            loginUI.setVisible(false);
            loginUI.reset();
        });

        JButton carrierJoinAuctionMyTRBtn = joinAuctionUI.getMyTRBtn();
        carrierJoinAuctionMyTRBtn.addActionListener(e -> adminUI.setVisible(true));

        JButton carrierJoinAuctionLogoutBtn = joinAuctionUI.getLogoutBtn();
        carrierJoinAuctionLogoutBtn.addActionListener(e -> {
            adminUI.setVisible(false);
            joinAuctionUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            carrier = null;
            HTTPRequests.logout();
        });

        JButton auctioneerLogoutBtn = auctioneerUI.getLogoutBtn();
        auctioneerLogoutBtn.addActionListener(e -> {
            auctioneerUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            auctioneer = null;
            HTTPRequests.logout();
        });

    }
}
