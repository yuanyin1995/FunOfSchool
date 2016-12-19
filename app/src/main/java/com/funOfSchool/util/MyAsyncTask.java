package com.funOfSchool.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.funOfSchool.ui.PersonInfoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;

import static com.funOfSchool.ui.PersonInfoActivity.imgUrl;
import static com.funOfSchool.ui.PersonInfoActivity.urlpath;

/**
 * Created by asus1 on 2016/10/25.
 */
public class MyAsyncTask extends AsyncTask<Integer,Integer,String> {
    private Context context; //上下文环境

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    /**
     * 必须实现的方法，该方法中执行具体的任务
     */
    @Override
    protected String doInBackground(Integer... integers) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        File file = new File(urlpath);
        try {
            params.put("profile_picture",file);
            System.out.println("文件存在！！！！！！！！！！！！！！！");
        }catch (Exception e){
            System.out.println("文件不存在");
        }
        Looper.prepare();
        client.post(imgUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                if(i == 200){
                    Toast.makeText(context,"上传成功",Toast.LENGTH_LONG).show();
                    Log.i("Tag",new String(responseBody));
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable error) {
                error.printStackTrace();
            }
        });
        Looper.loop();
        return null;
    }

    /**
     * 运行在UI线程中，调用 doInBackground 方法之前执行，可以不实现
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
    }

    /**
     * 运行在UI线程中，doInBackground 方法执行完毕后被调用，可以不实现
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
    }

    /**
     * 在异步任务执行过程中，当手动调用了 publishProgress 方法时，会回调该方法
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        PersonInfoActivity activity = (PersonInfoActivity) context;
        //activity.tv.setText(values[0]+""); 这里写回调时需要执行的方法
    }
}
