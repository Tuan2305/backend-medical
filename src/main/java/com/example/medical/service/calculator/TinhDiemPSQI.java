package com.example.medical.service.calculator;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Calculator cho thang đo PSQI (Pittsburgh Sleep Quality Index)
 * Thang đo phức tạp với 7 components
 * Tổng điểm tối đa: 21
 */
@Component
public class TinhDiemPSQI implements TinhDiemKhaoSat {

    @Override
    public int tinhDiem(Map<String, Object> cauTraLoi) {
        int tongDiem = 0;
        try {
            // Component 1: Subjective sleep quality (question 9)
            Integer q9 = chuyenDoiSoNguyen(cauTraLoi.get("q9"));
            tongDiem += (q9 != null) ? q9 : 0;

            // Component 2: Sleep latency (questions 2 and 5a)
            Integer q2 = chuyenDoiSoNguyen(cauTraLoi.get("q2")); // Minutes to fall asleep
            Integer q5a = chuyenDoiSoNguyen(cauTraLoi.get("q5a")); // Cannot sleep within 30 min
            int diemDoTreNgu = tinhDiemThoiGianNguTre(q2, q5a);
            tongDiem += diemDoTreNgu;

            // Component 3: Sleep duration (question 4)
            Integer q4 = chuyenDoiSoNguyen(cauTraLoi.get("q4")); // Hours of sleep
            tongDiem += tinhDiemThoiLuongNgu(q4);

            // Component 4: Sleep efficiency
            String q1 = (cauTraLoi.get("q1") != null) ? cauTraLoi.get("q1").toString() : ""; // Bedtime
            String q3 = (cauTraLoi.get("q3") != null) ? cauTraLoi.get("q3").toString() : ""; // Wake time
            tongDiem += tinhDiemHieuQuaGiacNgu(q1, q3, q4);

            // Component 5: Sleep disturbances (questions 5b-5j)
            int diemRoiLoan = 0;
            for (char c = 'b'; c <= 'j'; c++) {
                Integer diem = chuyenDoiSoNguyen(cauTraLoi.get("q5" + c));
                diemRoiLoan += (diem != null) ? diem : 0;
            }
            tongDiem += calculateDisturbanceScore(diemRoiLoan);

            // Component 6: Use of sleep medication (question 6)
            Integer q6 = chuyenDoiSoNguyen(cauTraLoi.get("q6"));
            tongDiem += (q6 != null) ? q6 : 0;

            // Component 7: Daytime dysfunction (questions 7 and 8)
            Integer q7 = chuyenDoiSoNguyen(cauTraLoi.get("q7"));
            Integer q8 = chuyenDoiSoNguyen(cauTraLoi.get("q8"));
            tongDiem += tinhDiemRoiLoanChucNangBanNgay(q7, q8);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính điểm PSQI: " + e.getMessage());
        }
        return Math.min(tongDiem, 21); // PSQI max score is 21
    }

    @Override
    public String dienGiaiDiem(int diem) {
        if (diem <= 5) {
            return "Chất lượng giấc ngủ tốt (điểm PSQI: " + diem + "/21). " +
                    "Điểm số này cho thấy bạn có chất lượng giấc ngủ bình thường.";
        } else {
            return "Chất lượng giấc ngủ kém (điểm PSQI: " + diem + "/21). " +
                    "Điểm > 5 cho thấy có vấn đề về giấc ngủ cần được quan tâm và có thể cần tư vấn từ chuyên gia.";
        }
    }

    /**
     * Tính điểm sleep latency từ thời gian ngủ và tần suất khó ngủ
     */
    private int tinhDiemThoiGianNguTre(Integer phut, Integer tanSuat) {
        if (phut == null || tanSuat == null) return 0;

        int diemPhut = 0;
        if (phut <= 15) diemPhut = 0;
        else if (phut <= 30) diemPhut = 1;
        else if (phut <= 60) diemPhut = 2;
        else diemPhut = 3;

        int tong = diemPhut + tanSuat;
        if (tong == 0) return 0;
        else if (tong <= 2) return 1;
        else if (tong <= 4) return 2;
        else return 3;
    }

    /**
     * Tính điểm sleep duration từ số giờ ngủ
     */
    private int tinhDiemThoiLuongNgu(Integer soGio) {
        if (soGio == null) return 3;
        if (soGio >= 7) return 0;
        else if (soGio >= 6) return 1;
        else if (soGio >= 5) return 2;
        else return 3;
    }

    /**
     * Tính điểm sleep efficiency
     */
    private int tinhDiemHieuQuaGiacNgu(String gioNgu, String gioThuc, Integer soGioNgu) {

        if (soGioNgu == null) return 3;
        if (soGioNgu >= 7) return 0;
        else if (soGioNgu >= 6) return 1;
        else if (soGioNgu >= 5) return 2;
        else return 3;
    }

    /**
     * Tính điểm disturbance từ tổng điểm các câu hỏi về rối loạn giấc ngủ
     */
    private int calculateDisturbanceScore(int tongRoiLoan) {
        if (tongRoiLoan == 0) return 0;
        else if (tongRoiLoan <= 9) return 1;
        else if (tongRoiLoan <= 18) return 2;
        else return 3;
    }

    /**
     * Tính điểm daytime dysfunction
     */
    private int tinhDiemRoiLoanChucNangBanNgay(Integer q7, Integer q8) {
        if (q7 == null || q8 == null) return 0;
        int tong = q7 + q8;
        if (tong == 0) return 0;
        else if (tong <= 2) return 1;
        else if (tong <= 4) return 2;
        else return 3;
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
