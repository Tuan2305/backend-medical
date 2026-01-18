package com.example.medical.service.calculator;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Calculator cho thang đo Beck Depression Inventory
 * 21 câu hỏi, mỗi câu có điểm từ 0-3
 * Tổng điểm tối đa: 63
 */
@Component
public class TinhDiemBeck implements TinhDiemKhaoSat {

    @Override
    public int tinhDiem(Map<String, Object> cauTraLoi) {
        int tongDiem = 0;
        try {
            for (int i = 1; i <= 21; i++) {
                Integer diem = chuyenDoiSoNguyen(cauTraLoi.get("q" + i));
                if (diem != null && diem >= 0 && diem <= 3) {
                    tongDiem += diem;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính điểm Beck: " + e.getMessage());
        }
        return Math.min(tongDiem, 63); // Beck max score is 63 (21 * 3)
    }

    @Override
    public String dienGiaiDiem(int diem) {
        String dienGiai = "Điểm Beck Depression Inventory: " + diem + "/63. ";
        if (diem <= 13) {
            return dienGiai + "Không có hoặc trầm cảm tối thiểu. Tình trạng tâm lý bình thường.";
        } else if (diem <= 19) {
            return dienGiai + "Trầm cảm nhẹ. Nên theo dõi và có thể cần tư vấn tâm lý.";
        } else if (diem <= 28) {
            return dienGiai + "Trầm cảm vừa. Khuyến nghị nên tìm kiếm sự trợ giúp chuyên nghiệp.";
        } else {
            return dienGiai + "Trầm cảm nặng. Cần được điều trị và theo dõi bởi chuyên gia tâm thần ngay lập tức.";
        }
    }

    /**
     * Parse integer từ response object
     */
    private Integer chuyenDoiSoNguyen(Object giaTri) {
        if (giaTri == null) return 0;
        if (giaTri instanceof Integer) return (Integer) giaTri;
        if (giaTri instanceof String) {
            try {
                String str = ((String) giaTri).trim();
                if (str.isEmpty()) return 0;
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        if (giaTri instanceof Double) return ((Double) giaTri).intValue();
        if (giaTri instanceof Float) return ((Float) giaTri).intValue();
        return 0;
    }
}
