package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    @Reference
    private HouseImageService houseImageService;

    //去上传图片的页面
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String goLoadPage(Model model, @PathVariable("houseId") Long houseId, @PathVariable("type") Integer type) {
        //将房源的id和图片的类型放到request域里面
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return "house/upload";
    }

    //上传房源图片  @RequestParam("file")file表单里面的name属性值
    @ResponseBody
    @RequestMapping("/upload/{houseId}/{type}")
    public Result goLoadPage(Model model, @PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type,
                             @RequestParam("file") MultipartFile[] files) {
        //做判断
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    //获取字节数组
                    byte[] bytes = file.getBytes();
                    //获取图片的名字
                    //String originalFilename = file.getOriginalFilename();
                    //通过UUID随机生成一个字符串上传到七牛云图片的名字
                    String newFileName = UUID.randomUUID().toString();
                    //通过QiniuUtils工具类上传图片到七牛云
                    QiniuUtil.upload2Qiniu(bytes, newFileName);
                    //创建houseImage对象
                    HouseImage houseImage = new HouseImage();
                    houseImage.setHouseId(houseId);
                    houseImage.setType(type);
                    houseImage.setImageName(newFileName);
                    //设置图片的路径，路径的格式是七牛云对象存储的绑定的域名
                    houseImage.setImageUrl("https://photo.sneoo.com/" + newFileName);
                    //调用houseImageService里面的方法
                    houseImageService.insert(houseImage);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    /**
     * 删除图片
     *
     * @param model
     * @param houseId
     * @param id
     * @return
     */
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(ModelMap model, @PathVariable Long houseId, @PathVariable Long id) {

        HouseImage houseImage = houseImageService.getById(id);
        QiniuUtil.deleteFileFromQiniu(houseImage.getImageName());
        houseImageService.delete(id);
        return "redirect:/house/" + houseId;
    }
}
