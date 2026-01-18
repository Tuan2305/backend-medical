package com.example.medical.dto.request;

import com.example.medical.entity.enu.GioiTinh;
import com.example.medical.entity.enu.LoaiKhaoSat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuiKhaoSatRequest {

    // Thông tin bệnh nhân
    @NotBlank(message = "Mã bệnh nhân là bắt buộc")
    private String maBenhNhan;          // patientCode

    @NotBlank(message = "Họ và tên là bắt buộc")
    private String hoTen;               // fullName

    @NotNull(message = "Năm sinh là bắt buộc")
    @Min(value = 1900, message = "Năm sinh phải từ 1900 trở lên")
    @Max(value = 2024, message = "Năm sinh không được vượt quá năm hiện tại")
    private Integer namSinh;            // birthYear

    @NotNull(message = "Giới tính là bắt buộc")
    private GioiTinh gioiTinh;            // gender

    private String diaChi;              // address
    private String ngheNghiep;          // occupation

    // Thông tin khảo sát
    @NotNull(message = "Loại khảo sát là bắt buộc")
    private LoaiKhaoSat loaiKhaoSat;     // surveyType

    @NotNull(message = "Câu trả lời khảo sát là bắt buộc")
    private Map<String, Object> cauTraLoi; // responses

}
