package utils.account;

public enum LoginNames {

    VALID_LOGIN(UserLogins.USER_PORTAL.getUserLogin()),
    LOGIN_CASE_INSENSETIVE("AcCePtAnCe"),
    INVALID_LOGIN("1A1t1m1a1n1@t1e1s1t1.c1o1m"),;

    String loginName;

    LoginNames(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }
}
