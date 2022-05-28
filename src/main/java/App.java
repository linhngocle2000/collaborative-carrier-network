import javax.swing.*;
import java.io.IOException;
import java.util.Map.Entry;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static CarrierLoginUI carrierLoginUI;
    private static CarrierJoinAuctionUI carrierJoinAuctionUI;
    private static AuctioneerUI auctioneerUI;

    private static JButton welcomeLoginBtn, welcomeRegisterBtn,
            loginBackBtn, loginLoginBtn, registerBackBtn,
            carrierLoginLogoutBtn, carrierLoginJoinAuctionBtn,
            carrierJoinAuctionLogoutBtn, auctioneerLogoutBtn,
            registerRegisterBtn;

    private static String displayName;

    private static Boolean isAuctioneer;

    static public void main(String[] args) {


        welcomeUI = new WelcomeUI();
        loginUI = new LoginUI();
        registerUI = new RegisterUI();
        carrierLoginUI = new CarrierLoginUI();
        carrierJoinAuctionUI = new CarrierJoinAuctionUI();
        auctioneerUI = new AuctioneerUI();

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
                if (!registerUI.areAllFieldsFilled()) {
                    throw new Exception("Please fill out all fields.");
                }
                String name = registerUI.getNameText();
                String username = registerUI.getUsernameText();
                String password = registerUI.getPasswordText();
                boolean isAuctioneer = registerUI.isAuctioneer();
                if (!HTTPRequests.register(name, username, password, isAuctioneer)) {
                    throw new Exception("Username " + username + " is already used.");
                } else {
                    registerUI.setErrorLabel("");
                    registerUI.showSuccessLabel();
                }
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
            Boolean isAuctioneerAgent = null;
            String name = null;
            try {
                Entry<String, Boolean> response = HTTPRequests.login(username, password);
                if (response == null) {
                    throw new Exception("Incorrect username/password.");
                }
                name = response.getKey();
                isAuctioneerAgent = response.getValue();
                loginUI.setVisible(false);
                loginUI.reset();
                if (isAuctioneerAgent) {
                    auctioneerUI.setVisible(true);
                    auctioneerUI.setNameLabel(name);
                } else {
                    carrierLoginUI.setNameLabel(name);
                    carrierLoginUI.setVisible(true);
                }
            } catch (Exception exception) {
                loginUI.setErrorLabel(exception.getMessage());
            } finally {
                displayName = name;
                isAuctioneer = isAuctioneerAgent;
            }
        });

        carrierLoginLogoutBtn = CarrierLoginUI.getLogoutBtn();
        carrierLoginLogoutBtn.addActionListener(e -> {
            carrierLoginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        carrierLoginJoinAuctionBtn = CarrierLoginUI.getJoinAuctionBtn();
        carrierLoginJoinAuctionBtn.addActionListener(e -> {
            carrierJoinAuctionUI.setNameLabel(displayName);
            carrierLoginUI.setVisible(false);
            carrierJoinAuctionUI.setVisible(true);
        });

        carrierJoinAuctionLogoutBtn = CarrierJoinAuctionUI.getLogoutBtn();
        carrierJoinAuctionLogoutBtn.addActionListener(e -> {
            carrierJoinAuctionUI.setVisible(false);
            carrierJoinAuctionUI.reset();
            welcomeUI.setVisible(true);
        });

        auctioneerLogoutBtn = AuctioneerUI.getLogoutBtn();
        auctioneerLogoutBtn.addActionListener(e -> {
            auctioneerUI.setVisible(false);
            auctioneerUI.reset();
            welcomeUI.setVisible(true);
        });



    }
}
