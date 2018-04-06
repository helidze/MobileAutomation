package utils.account;


public enum LoginPasses {
    VALID_PASS("123456"),
    NON_EXISTENT_PASS("brahmaputra"),
    PASS_CASE_SENSETIVE("pasSwOrD1"),
    SSO_PASSWORD("5jXFGkup");

    String loginPass;

    LoginPasses(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getLoginPass() {
        return loginPass;
    }
}