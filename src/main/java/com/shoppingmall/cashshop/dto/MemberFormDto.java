package com.shoppingmall.cashshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberFormDto {

    //이름을 NotBlank로 체크하는 이유: 빈 문자열 " "인 경우로 입력할 수도 있기에
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String memberName;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String memberEmail;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 생성해주세요.")
    private String memberPassword;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String memberAddress;
}
