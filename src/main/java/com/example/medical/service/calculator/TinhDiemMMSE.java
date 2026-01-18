package com.example.medical.service.calculator;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Calculator cho thang đo MMSE (Mini-Mental State Examination)
 * 30 câu hỏi, mỗi câu có điểm 0 hoặc 1
 * Tổng điểm tối đa: 30
 */
@Component
public class TinhDiemMMSE implements TinhDiemKhaoSat {

    @Override
    public int tinhDiem(Map<String, Object> cauTraLoi) {
        int tongDiem = 0;
        try {
            // MMSE có 30 điểm tổng, các câu hỏi có điểm khác nhau
            for (int i = 1; i <= 30; i++) {
                Integer diem = chuyenDoiSoNguyen(cauTraLoi.get("q" + i));
                if (diem != null && diem >= 0 && diem <= 1) {
                    tongDiem += diem;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính điểm MMSE: " + e.getMessage());
        }
        return Math.min(tongDiem, 30); // MMSE max score is 30
    }

    @Override
    public String dienGiaiDiem(int diem) {
        String dienGiai = "Điểm MMSE: " + diem + "/30. ";
        if (diem >= 24) {
            return dienGiai + "Nhận thức bình thường. Không có dấu hiệu suy giảm nhận thức.";
        } else if (diem >= 18) {
            return dienGiai + "Suy giảm nhận thức nhẹ. Nên theo dõi và đánh giá thêm.";
        } else if (diem >= 10) {
            return dienGiai + "Suy giảm nhận thức vừa. Cần can thiệp và hỗ trợ.";
        } else {
            return dienGiai + "Suy giảm nhận thức nặng. Cần chăm sóc đặc biệt và điều trị tích cực.";
        }
    }

    /**
     * Parse integer từ response object
     */
    private Integer chuyenDoiSoNguyen(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof String) {
            try {
                String str = ((String) obj).trim();
                if (str.isEmpty()) return 0;
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        if (obj instanceof Double) return ((Double) obj).intValue();
        if (obj instanceof Float) return ((Float) obj).intValue();
        return 0;
    }
}
