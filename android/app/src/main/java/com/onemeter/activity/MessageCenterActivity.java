package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.MessageInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.swipe.SimpleSwipeListener;
import com.onemeter.swipe.SwipeLayout;
import com.onemeter.swipe.adapters.BaseSwipeAdapter;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.CircleImageView;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：消息中心页面
 * 作者：angelyin
 * 时间：2016/1/28 10:28
 * 备注：
 */
public class MessageCenterActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView activity_message_center_listview;
    // 只是用来模拟异步获取数据
    private Handler handler;
    private MessageCenterAdapter messageCenterAdapter;
    /**
     * 数据为空的时候显示
     **/
    private ImageView activity_message_center_empty_img;
    private TextView activity_message_center_empty_text;
    private ProgressDialog progressDialog;
    private List<MessageInfo> messageInfos;
    int Uid = UserLoginActivity.userInfos.get(0).getUid();
    private ImageButton activity_message_center_img_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message_center_main);
        progressDialog = new ProgressDialog(this);
        messageInfos=new ArrayList<MessageInfo>();
        initData();
        initView();

    }

    /**
     * 初始化组件
     */
    private void initData() {
        messageInfos.clear();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        progressDialog.setMessage("加载中");
        progressDialog.show();
        String urlvalue = Constants.API_MY_MESSAGE_CENTER_LIST + "memberId=" + Uid;
        new NetUtil(this).sendGetToServer(urlvalue, MessageCenterActivity.this, "消息列表");

    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/28 10:35
     */
    private void initView() {
        activity_message_center_empty_img = (ImageView) findViewById(R.id.activity_message_center_empty_img);
        activity_message_center_empty_img.setVisibility(View.GONE);
        activity_message_center_empty_text = (TextView) findViewById(R.id.activity_message_center_empty_text);
        activity_message_center_empty_text.setVisibility(View.GONE);
        handler = new Handler();
        activity_message_center_img_return = (ImageButton) findViewById(R.id.activity_message_center_img_return);
        activity_message_center_img_return.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_message_center_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;

        }

    }

    @Override
    public void onRefresh() {
        //模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
//                mList.clear();
                if (messageInfos.size() == 0) {
                    activity_message_center_empty_img.setVisibility(View.VISIBLE);
                    activity_message_center_empty_text.setVisibility(View.VISIBLE);
                    activity_message_center_listview.setVisibility(View.GONE);
                }
                activity_message_center_listview.stopRefresh();
                toast("刷新成功");

            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                activity_message_center_listview.stopLoadMore();
                toast("加载成功");
            }
        }, 1000);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int  ii=messageInfos.get(position-1).getId();
        Intent intent = new Intent(MessageCenterActivity.this, MessageCenterDetailActivity.class);
        intent.putExtra("meassgeId", ii+"");
        startActivity(intent);
    }

    /**
     * 请求结束后此方法更新数据
     *
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            Log.i("Onemeter", "关闭prodialog");
        }
        if (!isSuccess) {// 请求不成功
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("content");
            /**********************************************************************************/
            if (action.equals("消息列表")) {
                if (statu) {//成功
                    if (jsonArray.length() == 0) {
                        toast("数据为空");
                    } else {
//                     toast("数据长度："+jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i).getJSONObject("message");
                            int id = jsonObject2.getInt("id");
                            String content = jsonObject2.getString("content");
                            Long createDate = jsonObject2.getLong("createDate");
                            String source = jsonObject2.getString("source");
                            String title=jsonObject2.getString("title");
                            boolean status = jsonArray.getJSONObject(i).getBoolean("status");
                            MessageInfo mif = new MessageInfo();
                            mif.setId(id);
                            mif.setMemberId(Uid);
                            mif.setCreateDate(createDate);
                            mif.setSource(source);
                            mif.setTitle(title);
                            mif.setContent(content);
                            messageInfos.add(mif);
                        }
                        messageCenterAdapter = new MessageCenterAdapter(this);
                        activity_message_center_listview = (XListView) findViewById(R.id.activity_message_center_listview);
                        // 设置xlistview可以加载、刷新
                        activity_message_center_listview.setPullLoadEnable(true);
                        activity_message_center_listview.setPullRefreshEnable(true);
                        // 设置回调函数
                        activity_message_center_listview.setXListViewListener(this);
                        // 设置适配器
                        activity_message_center_listview.setAdapter(messageCenterAdapter);
                        activity_message_center_listview.setOnItemClickListener(this);
                    }

                } else {
                    toast(message);
                }
            }

            /***************************************************************************/

            if(action.equals("消息删除")){
                if(statu){//成功
                    toast("删除成功");
                }else {
                    toast(message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * 消息中心的数据适配器
     */

    public class MessageCenterAdapter extends BaseSwipeAdapter {
        /**
         * 消息提醒图片
         **/
        CircleImageView message_center_look;
        /**
         * 消息描述内容
         **/
        TextView message_center_detail;
        /**
         * 消息标题
         **/
        TextView message_center_item_title;
        /**
         * 消息推送时间
         **/
        TextView message_center_item_time;
        // 上下文对象
        private Context mContext;

        // 构造函数
        public MessageCenterAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        // 对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
        @Override
        public void fillValues(int position, View convertView) {
            message_center_look = (CircleImageView) convertView.findViewById(R.id.message_center_look);
            message_center_item_title = (TextView)convertView.findViewById(R.id.message_center_item_title);
            message_center_detail = (TextView) convertView.findViewById(R.id.message_center_detail);
            message_center_item_time = (TextView) convertView.findViewById(R.id.message_center_item_time);
            message_center_detail.setText(messageInfos.get(position).getContent().toString());
            message_center_item_title.setText(messageInfos.get(position).getTitle().toString());
            String time=Utils.getFormatedDateTime("yyyy-MM-dd",messageInfos.get(position).getCreateDate().longValue());
            message_center_item_time.setText(time);
        }

        @Override
        public int getCount() {
            return messageInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return messageInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View generateView(final int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item,
                    parent, false);
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            // 当隐藏的删除menu被打开的时候的回调函数
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {

                }
            });
            // 双击的回调函数
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {

                }
            });
            // 添加删除布局的点击事件
            v.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 点击完成之后，关闭删除menu
                    //发送删除提交请求
                    getNetdelete(messageInfos.get(position).getId());
                    messageInfos.remove(position);
//                    toast(position+"");
                    messageCenterAdapter.notifyDataSetChanged();
                    swipeLayout.close();
                }
            });
            return v;
        }
    }

    /**
     * 删除提交数据
     */
    private void getNetdelete(int id) {
        if(!MyApplication.isNetAvailable){//没有网络的情况
            toast("网络为连接，请打开网络");
            return;
        }
        String urlvalue = Constants.API_MY_DELETE_MESSAGE + "memberId=" + Uid+"&messageId="+id;
        new NetUtil(this).sendGetToServer(urlvalue, MessageCenterActivity.this, "消息删除");


    }



}
