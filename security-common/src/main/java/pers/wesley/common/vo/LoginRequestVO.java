package pers.wesley.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @Description : 登录请求报文
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 13:51
 */
@Setter
@Getter
@ToString
@ApiModel(description = "登录请求报文")
public class LoginRequestVO {
    @NotEmpty
    @ApiModelProperty(name = "用户名", required = true)
    private String username;

    @NotEmpty
    @ApiModelProperty(name = "密码", required = true)
    @ToString.Exclude
    private String password;

    @ApiModelProperty(name = "验证码唯一ID")
    private String captchaKey;

    @ApiModelProperty(name = "验证码")
    private String captchaValue;
}
