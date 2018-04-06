package utils.account;

public enum UserLogins {

    MAIN_ACCOUNT("autotest1"),
    AUTO_QA("autoqa@i.ua"),
    TASK_ADMIN("automation@test.com"),
    USER_PORTAL("admin"),;

    UserLogins(String userLogin) {
        this.userLogin = userLogin;
    }

    public  String getUserLogin() {
        return userLogin;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    private String userLogin;

    private final static String userPassword = LoginPasses.VALID_PASS.getLoginPass();
}
