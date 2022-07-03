import Agent.Agent;
import Agent.CarrierAgent;
import Agent.AuctioneerAgent;
import AuctioneerUI.StartAuctionUI;
import CarrierUI.AdministrationUI;
import StartUI.LoginUI;
import StartUI.RegisterUI;
import StartUI.WelcomeUI;
import UIResource.HTTPResource.HTTPRequests;
import Utils.Converter;

import javax.swing.*;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static StartAuctionUI auctioneerUI;
    private static AdministrationUI adminUI;

    private static AuctioneerAgent auctioneer;
    private static CarrierAgent carrier;

    private static JButton carrierLogoutBtn;
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public App() {

        loginUI = new LoginUI();
        registerUI = new RegisterUI();

        welcomeUI = new WelcomeUI();
        welcomeUI.setVisible(true);

        JButton welcomeLoginBtn = welcomeUI.getLoginBtn();
        welcomeLoginBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            loginUI.setVisible(true);
            LOGGER.info("Login screen");
        });

        JButton welcomeRegisterBtn = welcomeUI.getRegisterBtn();
        welcomeRegisterBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            registerUI.setVisible(true);
            LOGGER.info("Register screen");
        });

        JButton loginBackBtn = loginUI.getBackBtn();
        loginBackBtn.addActionListener(e -> {
            loginUI.setVisible(false);
            welcomeUI.setVisible(true);
            LOGGER.info("Welcome screen");
        });

        JButton registerRegisterBtn = registerUI.getRegisterBtn();
        registerRegisterBtn.addActionListener(e -> {
            try {
                registerUI.hideSuccessLabel();
                registerUI.setErrorLabel("");
                if (!registerUI.areAllFieldsFilled()) {
                    throw new Exception("Please fill out all fields.");
                }
                if (Converter.checkPriceFormat(registerUI.getBaseRateAText()) ||
                        Converter.checkPriceFormat(registerUI.getBasePriceText()) ||
                        Converter.checkPriceFormat(registerUI.getBaseRateBText()) ||
                        Converter.checkPriceFormat(registerUI.getBaseInRateText())) {
                    throw new Exception("Price is not entered correctly.");
                }
                if (Converter.checkDepotFormat(registerUI.getDepotLonText()) ||
                        Converter.checkDepotFormat(registerUI.getDepotLatText())) {
                    throw new Exception("Depot is not entered correctly.");
                }
                String name = registerUI.getNameText();
                String username = registerUI.getUsernameText();
                String password = registerUI.getPasswordText();
                String transReq = registerUI.getTrText();
                float depotX = Float.parseFloat(registerUI.getDepotLatText());
                float depotY = Float.parseFloat(registerUI.getDepotLonText());
                float pickupBaserate = Float.parseFloat(registerUI.getBaseRateAText());
                float externalTravelCost = Float.parseFloat(registerUI.getBasePriceText());
                float loadBaserate = Float.parseFloat(registerUI.getBaseRateBText());
                float internalTravelCost = Float.parseFloat(registerUI.getBaseInRateText());
                if(Converter.checkTRFormat(registerUI.getTrText())) {
                    throw new Exception("Transport requests are not entered correctly.");
                }

                if (!HTTPRequests.registerCarrier(name, username, password, depotX, depotY, pickupBaserate, externalTravelCost, loadBaserate, internalTravelCost)) {
                    throw new Exception("Username " + username + " is already used.");
                }
                ArrayList<Float> tr = Converter.convertStringToTR(transReq);
                carrier = (CarrierAgent)HTTPRequests.login(username, password);
                for (int i = 0; i<(tr.size()); i+=4) {
                    assert carrier != null;
                    HTTPRequests.addTransportRequest(carrier, tr.get(i), tr.get(i+1), tr.get(i+2), tr.get(i+3));
                }
                registerUI.setErrorLabel("");
                registerUI.showSuccessLabel();
                registerUI.reset();
                LOGGER.info("Carrier " + carrier.getUsername() + " registered");
            } catch (Exception ex) {
                registerUI.setErrorLabel(ex.getMessage());
                LOGGER.warn(ex.getMessage());
            }
        });

        JButton registerBackBtn = registerUI.getBackBtn();
        registerBackBtn.addActionListener(e -> {
            registerUI.setVisible(false);
            welcomeUI.setVisible(true);
            LOGGER.info("Welcome screen");
        });

        JButton loginLoginBtn = loginUI.getLoginBtn();
        loginUI.getRootPane().setDefaultButton(loginLoginBtn);
        loginLoginBtn.addActionListener(e -> {
            loginUI.setErrorLabel("");
            String username = loginUI.getNameText();
            String password = loginUI.getPasswordText();
            Agent user = null;
            try {
                user = HTTPRequests.login(username, password);
            } catch (Exception ex) {
                LOGGER.warn(ex.getMessage());
            }
            if (user == null) {
                loginUI.setErrorLabel("Incorrect username/password.");
                return;
            }
            if (user.isAuctioneer()) {
                LOGGER.info("Auctioneer logged in");
                carrier = null;
                auctioneerUI = new StartAuctionUI();
                auctioneerUI.setVisible(true);
                loginUI.setVisible(false);
                auctioneer = (AuctioneerAgent)user;
            } else {
                auctioneer = null;
                carrier = (CarrierAgent)user;
                LOGGER.info("Carrier " + carrier.getUsername() + " logged in");
                adminUI = new AdministrationUI(carrier);
                adminUI.setVisible(true);
                adminUI.getVisUI().setVisible(true);
                loginUI.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        new App();
    }
}
