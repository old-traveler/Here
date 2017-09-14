package com.here.bean;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by hyc on 2017/9/14 13:18
 */

public class CallMessage extends BmobIMExtraMessage {

    public CallMessage(){
        setMsgType("call");
    }

}
