package utils.mobile;


public enum DriverWindows {

    WEBVIEW("WEBVIEW"),
    NATIVE_APP("NATIVE_APP");


    private final String view;

    DriverWindows(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

}
