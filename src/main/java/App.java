import Agent.Agent;
import Agent.CarrierAgent;
import AuctioneerUI.StartAuctionUI;
import CarrierUI.AdministrationUI;
import CarrierUI.CalculatorUI;
import StartUI.LoginUI;
import StartUI.RegisterUI;
import StartUI.WelcomeUI;
import CarrierUI.JoinAuctionUI;
import CarrierUI.CarrierLoginUI;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.Utils;
import javax.swing.*;
import java.util.ArrayList;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static CarrierLoginUI carrierLoginUI;
    private static JoinAuctionUI joinAuctionUI;
    private static StartAuctionUI auctioneerUI;
    private static AdministrationUI adminUI;

    private static JButton welcomeLoginBtn, welcomeRegisterBtn,
            loginBackBtn, loginLoginBtn, registerBackBtn,
            carrierLoginLogoutBtn, carrierLoginJoinAuctionBtn,
            carrierJoinAuctionLogoutBtn, auctioneerLogoutBtn,
            registerRegisterBtn, carrierLoginAdminBtn;

    private static Agent user;


    static public void main(String[] args) {

        welcomeUI = new WelcomeUI();
        loginUI = new LoginUI();
        registerUI = new RegisterUI();
        carrierLoginUI = new CarrierLoginUI();
        joinAuctionUI = new JoinAuctionUI();
        auctioneerUI = new StartAuctionUI();

        welcomeUI.setVisible(true);

        welcomeLoginBtn = welcomeUI.getLoginBtn();
        welcomeLoginBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            loginUI.setVisible(true);
        });

        welcomeRegisterBtn = welcomeUI.getRegisterBtn();
        welcomeRegisterBtn.addActionListener(e -> {
            welcomeUI.setVisible(false);
            registerUI.setVisible(true);
        });

        loginBackBtn = loginUI.getBackBtn();
        loginBackBtn.addActionListener(e -> {
            loginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        registerRegisterBtn = registerUI.getRegisterBtn();
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
                    for (int i = 0; i<(tr.size()); i+=4) {
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

        registerBackBtn = registerUI.getBackBtn();
        registerBackBtn.addActionListener(e -> {
            registerUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        loginLoginBtn = loginUI.getLoginBtn();
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
                adminUI = new AdministrationUI(user);
            }
        });

        carrierLoginLogoutBtn = carrierLoginUI.getLogoutBtn();
        carrierLoginLogoutBtn.addActionListener(e -> {
            carrierLoginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        carrierLoginJoinAuctionBtn = carrierLoginUI.getJoinAuctionBtn();
        carrierLoginJoinAuctionBtn.addActionListener(e -> {
            joinAuctionUI.setNameLabel(user.getDisplayname());
            joinAuctionUI.setVisible(true);
        });

        carrierLoginAdminBtn = carrierLoginUI.getAdministrationBtn();
        carrierLoginAdminBtn.addActionListener(e -> {
            adminUI.setVisible(true);
        });

        carrierJoinAuctionLogoutBtn = joinAuctionUI.getLogoutBtn();
        carrierJoinAuctionLogoutBtn.addActionListener(e -> {
            joinAuctionUI.setVisible(false);
            joinAuctionUI.reset();
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();

        });

        auctioneerLogoutBtn = auctioneerUI.getLogoutBtn();
        auctioneerLogoutBtn.addActionListener(e -> {
            auctioneerUI.setVisible(false);
            welcomeUI.setVisible(true);
            // Reset data
            user = null;
            HTTPRequests.logout();
        });

    }
}
