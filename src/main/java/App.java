import Agent.Agent;
import AuctioneerUI.StartAuctionUI;
import StartUI.LoginUI;
import StartUI.RegisterUI;
import StartUI.WelcomeUI;
import CarrierUI.JoinAuctionUI;
import CarrierUI.CarrierLoginUI;
import UIResource.HTTPResource.HTTPRequests;

import javax.swing.*;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static CarrierLoginUI carrierLoginUI;
    private static JoinAuctionUI joinAuctionUI;
    private static StartAuctionUI auctioneerUI;

    private static JButton welcomeLoginBtn, welcomeRegisterBtn,
            loginBackBtn, loginLoginBtn, registerBackBtn,
            carrierLoginLogoutBtn, carrierLoginJoinAuctionBtn,
            carrierJoinAuctionLogoutBtn, auctioneerLogoutBtn,
            registerRegisterBtn;

    private static Agent user;


    static public void main(String[] args) {

        welcomeUI = new WelcomeUI();
        loginUI = new LoginUI();
        registerUI = new RegisterUI();
        carrierLoginUI = new CarrierLoginUI();
        joinAuctionUI = new JoinAuctionUI();
        auctioneerUI = new StartAuctionUI();

        welcomeUI.setVisible(true);

        welcomeLoginBtn = WelcomeUI.getLoginBtn();
        welcomeLoginBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            loginUI.setVisible(true);
        });

        welcomeRegisterBtn = WelcomeUI.getRegisterBtn();
        welcomeRegisterBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            registerUI.setVisible(true);
        });

        loginBackBtn = LoginUI.getBackBtn();
        loginBackBtn.addActionListener(e -> {
            loginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        registerRegisterBtn = RegisterUI.getRegisterBtn();
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
                    double depotX = registerUI.getDepotLatText();
                    double depotY = registerUI.getDepotLonText();
                    if(!registerUI.verifyTRInput()) {
                        throw new Exception("Transport requests are not entered correctly.");
                    }
                    if (!registerUI.verifyPriceInput()) {
                        throw new Exception("Price is not entered correctly.");
                    }
                    if (!registerUI.verifyDepotInput()) {
                        throw new Exception("Depot is not entered correctly.");
                    }
                    if (!HTTPRequests.registerCarrier(name, username, password, false, depotX, depotY)) {
                        throw new Exception("Username " + username + " is already used.");
                    }
                } else {
//                    if (!HTTPRequests.registerAuctioneer(name, username, password, true)) {
//                        throw new Exception("Username " + username + " is already used.");
//                    }
                }
                registerUI.setErrorLabel("");
                registerUI.showSuccessLabel();
                registerUI.deactivate();
            } catch (Exception ex) {
                registerUI.setErrorLabel(ex.getMessage());
            }
        });

        registerBackBtn = RegisterUI.getBackBtn();
        registerBackBtn.addActionListener(e -> {
            registerUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        loginLoginBtn = LoginUI.getLoginBtn();
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
                carrierLoginUI.setNameLabel(user.getDisplayname());
                carrierLoginUI.setVisible(true);
            }
        });

        carrierLoginLogoutBtn = CarrierLoginUI.getLogoutBtn();
        carrierLoginLogoutBtn.addActionListener(e -> {
            carrierLoginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        carrierLoginJoinAuctionBtn = CarrierLoginUI.getJoinAuctionBtn();
        carrierLoginJoinAuctionBtn.addActionListener(e -> {
            joinAuctionUI.setNameLabel(user.getDisplayname());
            carrierLoginUI.setVisible(false);
            joinAuctionUI.setVisible(true);
        });

        carrierJoinAuctionLogoutBtn = JoinAuctionUI.getLogoutBtn();
        carrierJoinAuctionLogoutBtn.addActionListener(e -> {
            joinAuctionUI.setVisible(false);
            joinAuctionUI.reset();
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();

        });

        auctioneerLogoutBtn = StartAuctionUI.getLogoutBtn();
        auctioneerLogoutBtn.addActionListener(e -> {
            auctioneerUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();
        });

    }
}
