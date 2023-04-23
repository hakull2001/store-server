package com.bookshop.dto;

import com.bookshop.constants.Common;
import com.bookshop.constants.RoleEnum;
import com.bookshop.validators.IsIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "Họ đệm không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String lastName;

    @NotBlank(message = "Tài khoản không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String username;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String address;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,255}$", message = " Không hợp lệ")
    private String password;

    private Long amount;

    @IsIn(value = {RoleEnum.ADMIN, RoleEnum.MEMBER}, message = "Không hợp lệ")
    private String role;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String phone;
}
