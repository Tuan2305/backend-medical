package com.example.medical.service.calculator;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Calculator cho thang đo Zung Self-Rating Depression Scale
 * 20 câu hỏi, mỗi câu có điểm từ 1-4
 * Tổng điểm tối đa: 80
 */
@Component
public class TinhDiemZung implements TinhDiemKhaoSat {

    @Override
    public int tinhDiem(Map<String, Object> cauTraLoi) {
        int tongDiem = 0;
        try {
            // Zung có 20 câu hỏi, mỗi câu điểm 1-4
            for (int i = 1; i <= 20; i++) {
                Integer diem = chuyenDoiSoNguyen(cauTraLoi.get("q" + i));
                if (diem != null && diem >= 1 && diem <= 4) {
                    tongDiem += diem;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính điểm Zung: " + e.getMessage());
        }
        return Math.min(tongDiem, 80); // Zung max score is 80 (20 * 4)
    }

    @Override
    public String dienGiaiDiem(int diem) {
        String dienGiai = "Điểm Zung Self-Rating Depression Scale: " + diem + "/80. ";
        if (diem < 50) {
            return dienGiai + "Không có trầm cảm đáng kể. Tình trạng tâm lý bình thường.";
        } else if (diem < 60) {
            return dienGiai + "Trầm cảm nhẹ đến vừa. Nên theo dõi và có thể cần tư vấn.";
        } else if (diem < 70) {
            return dienGiai + "Trầm cảm vừa đến nặng. Khuyến nghị tìm kiếm sự trợ giúp chuyên nghiệp.";
        } else {
            return dienGiai + "Trầm cảm nặng. Cần được điều trị ngay lập tức bởi chuyên gia tâm thần.";
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
