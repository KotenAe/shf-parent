package com.atguigu.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;

public class QiNiuTest {
    @Test
    public void test() throws Exception {
        //构造一个带指定 Zone 对象的配置类
        //Zone.zone0() 华东
        //Zone.zone1() 华北
        //Zone.zone2() 华南
        //这些区域和新建空间的顺序依次类推
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "73aEfgnJaXPUpH08hEUWFHF-KAAgFnqPDqkyvnkT";
        String secretKey = "wNWbkSin5hdGDS_HDvGjuojIF7--FgUQn1fskgjZ";
        String bucket = "kotena";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:/Users/zhour/Pictures/Saved Pictures/1.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //String key = null;
        //设置自定义的名字
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }
}
