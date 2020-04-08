package pers.wesley.common.validation.validator;

import lombok.extern.slf4j.Slf4j;
import pers.wesley.common.validation.constraints.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @Description : 身份证格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 17:12
 * 中华人民共和国国家标准 GB 11643-1999
 * 6位地址码，8位出生日期码，3位顺序码，1位校验码
 * 参考网站 https://30ke.cn/doc/id-num-validaty
 */
@Slf4j
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    private static final char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private static final int[] WI = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final Map<String, String> PROVINCE_CODE = new ConcurrentHashMap<>();

    private static final int ID_LENGTH_15 = 15;
    private static final int ID_LENGTH_18 = 18;
    private static final int ID_LENGTH_17 = 17;
    private static final int MOD = 11;

    private Pattern pattern = Pattern.compile("[0-9]*");

    static {
        PROVINCE_CODE.put("11", "北京");
        PROVINCE_CODE.put("12", "天津");
        PROVINCE_CODE.put("13", "河北");
        PROVINCE_CODE.put("14", "山西");
        PROVINCE_CODE.put("15", "内蒙古");
        PROVINCE_CODE.put("21", "辽宁");
        PROVINCE_CODE.put("22", "吉林");
        PROVINCE_CODE.put("23", "黑龙江");
        PROVINCE_CODE.put("31", "上海");
        PROVINCE_CODE.put("32", "江苏");
        PROVINCE_CODE.put("33", "浙江");
        PROVINCE_CODE.put("34", "安徽");
        PROVINCE_CODE.put("35", "福建");
        PROVINCE_CODE.put("36", "江西");
        PROVINCE_CODE.put("37", "山东");
        PROVINCE_CODE.put("41", "河南");
        PROVINCE_CODE.put("42", "湖北");
        PROVINCE_CODE.put("43", "湖南");
        PROVINCE_CODE.put("44", "广东");
        PROVINCE_CODE.put("45", "广西");
        PROVINCE_CODE.put("46", "海南");
        PROVINCE_CODE.put("50", "重庆");
        PROVINCE_CODE.put("51", "四川");
        PROVINCE_CODE.put("52", "贵州");
        PROVINCE_CODE.put("53", "云南");
        PROVINCE_CODE.put("54", "西藏");
        PROVINCE_CODE.put("61", "陕西");
        PROVINCE_CODE.put("62", "甘肃");
        PROVINCE_CODE.put("63", "青海");
        PROVINCE_CODE.put("64", "宁夏");
        PROVINCE_CODE.put("65", "新疆");
        PROVINCE_CODE.put("71", "台湾");
        PROVINCE_CODE.put("81", "香港");
        PROVINCE_CODE.put("82", "澳门");
        PROVINCE_CODE.put("91", "国外");
    }

    @Override
    public void initialize(IdCard constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        if (value.length() == ID_LENGTH_15 || value.length() == ID_LENGTH_18) {
            final String idCard17 = value.length() == ID_LENGTH_18 ? value.substring(0, 17) : value.substring(0, 6) + "19" + value.substring(6, 16);
            if (!pattern.matcher(idCard17).matches()) {
                return false;
            }
            final LocalDate birthday;
            try {
                birthday = LocalDate.parse(idCard17.substring(6, 14), DateTimeFormatter.ofPattern("yyyyMMdd"));
            } catch (Exception e) {
                return false;
            }
            if (birthday.isAfter(LocalDate.now())) {
                return false;
            }
            final String provinceCode = idCard17.substring(0, 2);
            if (!PROVINCE_CODE.containsKey(provinceCode)) {
                return false;
            }
            if (value.length() == ID_LENGTH_18) {
                int totalMulAiWi = 0;
                for (int i = 0; i < ID_LENGTH_17; i++) {
                    totalMulAiWi += Integer.parseInt(String.valueOf(idCard17.charAt(i))) * WI[i];
                }
                if (value.charAt(ID_LENGTH_17) != VERIFY_CODE[totalMulAiWi % MOD]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
