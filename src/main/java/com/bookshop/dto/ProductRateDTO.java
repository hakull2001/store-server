package com.bookshop.dto;

import com.bookshop.constants.Common;
import com.bookshop.constants.ProductRateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRateDTO {
    @NotNull
    @Min(1)
    private Long productId;

    private Long userId;

    @NotNull(message = "Bạn phải chọn xếp hạng số sao")
    @Min(ProductRateEnum.MIN)
    @Max(ProductRateEnum.MAX)
    private Integer value;

    @NotBlank(message = "không được để trống")
    @Length(max = Common.STRING_LENGTH_LIMIT, message = "Quá số lượng ký tự cho phép")
    private String comment;
}
