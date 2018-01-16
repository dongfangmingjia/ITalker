package com.warner.italker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.warner.factory.Factory;
import com.warner.factory.data.helper.AccountHelper;
import com.warner.factory.persistence.Account;

/**
 * Created by warner on 2018/1/16.
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA:
                // 常规额消息送达
                byte[] payloads = bundle.getByteArray("payload");
                if (payloads != null) {
                    String message = new String(payloads);
                    onMessageArrived(message);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化id
     * @param cid
     */
    private void onClientInit(String cid) {
        Account.setPushId(cid);
        if (Account.isLogin()) {
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息送达
     * @param message
     */
    private void onMessageArrived(String message) {
        Factory.dispatchPush(message);
    }
}
