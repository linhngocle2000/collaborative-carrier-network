import javax.swing.*;

public class App {

    private static WelcomeUI welcomeUI;
    private static LoginUI loginUI;
    private static RegisterUI registerUI;
    private static CarrierLoginUI carrierLoginUI;
    private static CarrierJoinAuctionUI carrierJoinAuctionUI;
    private static AuctioneerUI auctioneerUI;

    private static JButton welcomeLoginBtn;
    private static JButton welcomeRegisterBtn;
    private static JButton loginBackBtn;
    private static JButton loginLoginBtn;
    private static JButton registerBackBtn;
    private static JButton carrierLoginLogoutBtn;
    private static JButton carrierLoginJoinAuctionBtn;
    private static JButton carrierJoinAuctionLogoutBtn;
    private static JButton auctioneerLogoutBtn;

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

        registerBackBtn = RegisterUI.getBackBtn();
        registerBackBtn.addActionListener(e -> {
            registerUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        loginLoginBtn = LoginUI.getLoginBtn();
        loginLoginBtn.addActionListener(e -> {
            loginUI.setVisible(false);
            loginUI.reset();
            carrierLoginUI.setVisible(true);
            auctioneerUI.setVisible(true);
        });

        carrierLoginLogoutBtn = CarrierLoginUI.getLogoutBtn();
        carrierLoginLogoutBtn.addActionListener(e -> {
            carrierLoginUI.setVisible(false);
            welcomeUI.setVisible(true);
        });

        carrierLoginJoinAuctionBtn = CarrierLoginUI.getJoinAuctionBtn();
        carrierLoginJoinAuctionBtn.addActionListener(e -> {
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
