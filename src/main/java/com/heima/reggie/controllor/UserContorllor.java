package com.heima.reggie.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.reggie.common.R;
import com.heima.reggie.entity.User;
import com.heima.reggie.service.UserService;
import com.heima.reggie.utils.SMSUtils;
import com.heima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserContorllor {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            // 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            // 调用阿里云的api完成短信发送
//            SMSUtils.sendMessage("我是你爹","SMS_283515633",phone,code);
            log.info("code:{}",code);

            // 存储生成的验证码
            session.setAttribute(phone,phone);



            return R.success("发送短信成功");

        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

        Object sessionObj = session.getAttribute(phone);



        if(sessionObj != null && sessionObj.equals(phone)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

            userLambdaQueryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(userLambdaQueryWrapper);

            // 判断是否等于新用户
            if(user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);

                userService.save(user);
            }

            session.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("登录失败");
    }
}
