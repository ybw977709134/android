/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.onemeter.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.onemeter.utils.Constants;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		String result ;
		         switch (resp.errCode) {
			        case BaseResp.ErrCode.ERR_OK:
				             result = "分享成功";
				             break;
			       case BaseResp.ErrCode.ERR_USER_CANCEL:
				             result = "取消分享";
				             break;
			         case BaseResp.ErrCode.ERR_AUTH_DENIED:
				             result = "分享失败";
				             break;
			         default:
				             result = "位置错误";
				             break;
			         }

		         Toast.makeText(this, result, Toast.LENGTH_LONG).show();

		         // TODO 微信分享 成功之后调用接口
		         this.finish();
	}
	}

