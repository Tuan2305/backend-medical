package com.example.medical.service.calculator;

import java.util.Map;

/**
 * Interface chung cho tất cả các calculator tính điểm thang đo
 */
public interface TinhDiemKhaoSat {
    
    /**
     * Tính điểm tổng từ các câu trả lời
     * @param cauTraLoi Map chứa câu trả lời của người dùng
     * @return Điểm tổng
     */
    int tinhDiem(Map<String, Object> cauTraLoi);
    
    /**
     * Diễn giải kết quả dựa trên điểm số
     * @param diem Điểm số cần diễn giải
     * @return Chuỗi diễn giải kết quả
     */
    String dienGiaiDiem(int diem);
}
