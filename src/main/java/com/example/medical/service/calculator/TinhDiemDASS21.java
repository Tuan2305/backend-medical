package com.example.medical.service.calculator;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Calculator cho thang đo DASS-21 (Depression Anxiety Stress Scales)
 * 21 câu hỏi, mỗi câu có điểm từ 0-3
 * Tổng điểm tối đa: 63
 */
@Component
public class TinhDiemDASS21 implements TinhDiemKhaoSat {

    @Override
    public int tinhDiem(Map<String, Object> cauTraLoi) {
        int tongDiem = 0;
        try {
            // DASS-21 có 21 câu hỏi, mỗi câu điểm 0-3
            for (int i = 1; i <= 21; i++) {
                Integer diem = chuyenDoiSoNguyen(cauTraLoi.get("q" + i));
                if (diem != null && diem >= 0 && diem <= 3) {
                    tongDiem += diem;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính điểm DASS-21: " + e.getMessage());
        }
        return Math.min(tongDiem, 63); // DASS-21 max score is 63 (21 * 3)
    }

    @Override
    public String dienGiaiDiem(int diem) {
        String dienGiai = "Điểm DASS-21 tổng: " + diem + "/63. ";
        if (diem <= 9) {
            return dienGiai + "Mức độ bình thường. Không có dấu hiệu đáng lo ngại về trầm cảm, lo âu hay stress.";
        } else if (diem <= 13) {
            return dienGiai + "Mức độ nhẹ. Có một số dấu hiệu cần theo dõi.";
        } else if (diem <= 20) {
            return dienGiai + "Mức độ vừa. Nên tìm kiếm sự trợ giúp và tư vấn chuyên nghiệp.";
        } else if (diem <= 27) {
            return dienGiai + "Mức độ nặng. Cần được điều trị và theo dõi bởi chuyên gia.";
        } else {
            return dienGiai + "Mức độ rất nặng. Cần được can thiệp điều trị ngay lập tức.";
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
