package com.here.scan;

/**
 * Created by hyc on 2017/6/23 09:24
 */

public interface ScanContract {

    void startScanCode();

    void showLoading();

    void stopLoading();

    void promptIsFollow();

    void followSuccess();

    void followFail(String error);

}
