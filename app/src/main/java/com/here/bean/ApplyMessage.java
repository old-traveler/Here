package com.here.bean;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by hyc on 2017/7/10 18:46
 */

public class ApplyMessage extends BmobIMExtraMessage {

    public void ApplyMessage(){
        setMsgType("apply");
    }

}
