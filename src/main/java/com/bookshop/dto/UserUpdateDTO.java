package com.bookshop.dto;

import com.bookshop.constants.Common;
import com.bookshop.constants.RoleEnum;
import com.bookshop.validators.IsIn;
import com.bookshop.validators.NullOrNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String firstName;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String lastName;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String username;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String address;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,255}$", message = "Không hợp lệ")
    private String password;

    private Long amount;

    @IsIn(value = {RoleEnum.ADMIN, RoleEnum.MEMBER}, message = "Không hợp lệ")
    private String role;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String email;

    @NullOrNotEmpty(message = "Không hợp lệ")
    @Length(max = Common.STRING_LENGTH_LIMIT)
    private String phone;
}
