package pers.wesley.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description : 登录应答报文
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 13:51
 */
@Setter
@Getter
@ToString
@ApiModel(description = "登录应答报文")
public class LoginResponseVO {
    @ApiModelProperty(name = "令牌", required = true)
    private String token;
}
