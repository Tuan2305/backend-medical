package com.example.medical.service.impl;

import com.example.medical.dto.request.SurveySubmissionRequest;
import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import com.example.medical.repository.PatientRepository;
import com.example.medical.repository.SurveyResponseRepository;
import com.example.medical.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SurveyResponse submitSurvey(SurveySubmissionRequest submissionRequest) {
        try {
            // Validate patient code
            if (!isValidPatientCode(submissionRequest.getPatientCode())) {
                throw new RuntimeException("Mã bệnh nhân không hợp lệ");
            }

            // Find or create patient
            Patient patient = findOrCreatePatient(submissionRequest);

            // Create survey response
            SurveyResponse surveyResponse = new SurveyResponse();
            surveyResponse.setPatient(patient);
            surveyResponse.setSurveyType(submissionRequest.getSurveyType());
            surveyResponse.setResponses(objectMapper.writeValueAsString(submissionRequest.getResponses()));

            // Calculate score and interpretation
            int totalScore = calculateScore(submissionRequest.getSurveyType(), submissionRequest.getResponses());
            String interpretation = generateInterpretation(submissionRequest.getSurveyType(), totalScore);

            surveyResponse.setTotalScore(totalScore);
            surveyResponse.setInterpretation(interpretation);

            SurveyResponse savedResponse = surveyResponseRepository.save(surveyResponse);

            // Update patient's updated_at timestamp
            patient.setUpdatedAt(LocalDateTime.now());
            patientRepository.save(patient);

            return savedResponse;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi khảo sát: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SurveyResponse> getAllSurveyResponses() {
        return surveyResponseRepository.findAll();
    }

    @Override
    public Page<SurveyResponse> getRecentSurveys(int hours, Pageable pageable) {
        try {
            LocalDateTime sinceTime = LocalDateTime.now().minusHours(hours);
            System.out.println("DEBUG: Looking for surveys since: " + sinceTime); // Debug log

            Page<SurveyResponse> result = surveyResponseRepository.findRecentResponses(sinceTime, pageable);
            System.out.println("DEBUG: Found " + result.getTotalElements() + " surveys"); // Debug log

            return result;
        } catch (Exception e) {
            System.err.println("ERROR in getRecentSurveys: " + e.getMessage());
            throw new RuntimeException("Error getting recent surveys: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<SurveyResponse> getAllSurveyResponses(Pageable pageable) {
        return surveyResponseRepository.findAll(pageable);
    }

    public List<SurveyResponse> getSurveyResponsesByPatient(Long patientId) {
        return surveyResponseRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    public Page<SurveyResponse> getSurveyResponsesByPatient(Long patientId, Pageable pageable) {
        return surveyResponseRepository.findByPatientIdOrderByCreatedAtDesc(patientId, pageable);
    }

    public List<SurveyResponse> getSurveyResponsesBySurveyType(SurveyType surveyType) {
        return surveyResponseRepository.findBySurveyType(surveyType);
    }

    @Override
    public Page<SurveyResponse> getSurveyResponsesBySurveyType(SurveyType surveyType, Pageable pageable) {
        return surveyResponseRepository.findBySurveyType(surveyType, pageable);
    }

    @Override
    public List<SurveyResponse> getRecentSurveys(int hours) {
        try {
            LocalDateTime sinceTime = LocalDateTime.now().minusHours(hours);
            System.out.println("DEBUG: Looking for surveys since: " + sinceTime); // Debug log

            List<SurveyResponse> result = surveyResponseRepository.findRecentResponses(sinceTime);
            System.out.println("DEBUG: Found " + result.size() + " surveys"); // Debug log

            return result;
        } catch (Exception e) {
            System.err.println("ERROR in getRecentSurveys: " + e.getMessage());
            throw new RuntimeException("Error getting recent surveys: " + e.getMessage(), e);
        }
    }

    // Additional utility methods
    public List<SurveyResponse> getSurveyResponsesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return surveyResponseRepository.findByDateRange(startDate, endDate);
    }

    public List<SurveyResponse> searchSurveysByPatientName(String patientName) {
        return surveyResponseRepository.findByPatientNameContaining(patientName);
    }

    private boolean isValidPatientCode(String patientCode) {
        return patientCode != null &&
                patientCode.matches("^BN\\d{6}$") &&
                patientCode.length() == 8;
    }

    private Patient findOrCreatePatient(SurveySubmissionRequest request) {
        Optional<Patient> existingPatient = patientRepository.findByPatientCode(request.getPatientCode());

        if (existingPatient.isPresent()) {
            // Update patient information
            Patient patient = existingPatient.get();
            updatePatientInfo(patient, request);
            return patientRepository.save(patient);
        } else {
            // Create new patient
            Patient patient = new Patient();
            patient.setPatientCode(request.getPatientCode());
            updatePatientInfo(patient, request);
            return patientRepository.save(patient);
        }
    }

    private void updatePatientInfo(Patient patient, SurveySubmissionRequest request) {
        patient.setFullName(request.getFullName());
        patient.setBirthYear(request.getBirthYear());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setOccupation(request.getOccupation());
        patient.setUpdatedAt(LocalDateTime.now());
    }

    // Scoring methods
    private int calculateScore(SurveyType surveyType, Map<String, Object> responses) {
        switch (surveyType) {
            case PSQI:
                return calculatePSQIScore(responses);
            case BECK:
                return calculateBeckScore(responses);
            case ZUNG:
                return calculateZungScore(responses);
            case DASS21:
                return calculateDASS21Score(responses);
            case MMSE:
                return calculateMMSEScore(responses);
            default:
                return 0;
        }
    }

    private int calculatePSQIScore(Map<String, Object> responses) {
        int totalScore = 0;
        try {
            // Component 1: Subjective sleep quality (question 9)
            Integer q9 = parseIntegerFromResponse(responses.get("q9"));
            totalScore += (q9 != null) ? q9 : 0;

            // Component 2: Sleep latency (questions 2 and 5a)
            Integer q2 = parseIntegerFromResponse(responses.get("q2")); // Minutes to fall asleep
            Integer q5a = parseIntegerFromResponse(responses.get("q5a")); // Cannot sleep within 30 min
            int sleepLatency = calculateSleepLatencyScore(q2, q5a);
            totalScore += sleepLatency;

            // Component 3: Sleep duration (question 4)
            Integer q4 = parseIntegerFromResponse(responses.get("q4")); // Hours of sleep
            totalScore += calculateSleepDurationScore(q4);

            // Component 4: Sleep efficiency
            String q1 = (responses.get("q1") != null) ? responses.get("q1").toString() : ""; // Bedtime
            String q3 = (responses.get("q3") != null) ? responses.get("q3").toString() : ""; // Wake time
            totalScore += calculateSleepEfficiencyScore(q1, q3, q4);

            // Component 5: Sleep disturbances (questions 5b-5j)
            int disturbanceScore = 0;
            for (char c = 'b'; c <= 'j'; c++) {
                Integer score = parseIntegerFromResponse(responses.get("q5" + c));
                disturbanceScore += (score != null) ? score : 0;
            }
            totalScore += calculateDisturbanceScore(disturbanceScore);

            // Component 6: Use of sleep medication (question 6)
            Integer q6 = parseIntegerFromResponse(responses.get("q6"));
            totalScore += (q6 != null) ? q6 : 0;

            // Component 7: Daytime dysfunction (questions 7 and 8)
            Integer q7 = parseIntegerFromResponse(responses.get("q7"));
            Integer q8 = parseIntegerFromResponse(responses.get("q8"));
            totalScore += calculateDaytimeDysfunctionScore(q7, q8);

        } catch (Exception e) {
            throw new RuntimeException("Error calculating PSQI score: " + e.getMessage());
        }
        return Math.min(totalScore, 21); // PSQI max score is 21
    }

    private int calculateSleepLatencyScore(Integer minutes, Integer frequency) {
        if (minutes == null || frequency == null) return 0;

        int minutesScore = 0;
        if (minutes <= 15) minutesScore = 0;
        else if (minutes <= 30) minutesScore = 1;
        else if (minutes <= 60) minutesScore = 2;
        else minutesScore = 3;

        int sum = minutesScore + frequency;
        if (sum == 0) return 0;
        else if (sum <= 2) return 1;
        else if (sum <= 4) return 2;
        else return 3;
    }

    private int calculateSleepDurationScore(Integer hours) {
        if (hours == null) return 3;
        if (hours >= 7) return 0;
        else if (hours >= 6) return 1;
        else if (hours >= 5) return 2;
        else return 3;
    }

    private int calculateSleepEfficiencyScore(String bedTime, String wakeTime, Integer sleepHours) {
        // This is simplified - actual calculation would need time parsing
        // For now, return a placeholder based on sleep hours
        if (sleepHours == null) return 3;
        if (sleepHours >= 7) return 0;
        else if (sleepHours >= 6) return 1;
        else if (sleepHours >= 5) return 2;
        else return 3;
    }

    private int calculateDisturbanceScore(int totalDisturbance) {
        if (totalDisturbance == 0) return 0;
        else if (totalDisturbance <= 9) return 1;
        else if (totalDisturbance <= 18) return 2;
        else return 3;
    }

    private int calculateDaytimeDysfunctionScore(Integer q7, Integer q8) {
        if (q7 == null || q8 == null) return 0;
        int sum = q7 + q8;
        if (sum == 0) return 0;
        else if (sum <= 2) return 1;
        else if (sum <= 4) return 2;
        else return 3;
    }

    private int calculateBeckScore(Map<String, Object> responses) {
        int totalScore = 0;
        try {
            for (int i = 1; i <= 21; i++) {
                Integer score = parseIntegerFromResponse(responses.get("q" + i));
                if (score != null && score >= 0 && score <= 3) {
                    totalScore += score;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calculating Beck score: " + e.getMessage());
        }
        return Math.min(totalScore, 63); // Beck max score is 63 (21 * 3)
    }

    private int calculateZungScore(Map<String, Object> responses) {
        int totalScore = 0;
        try {
            // Zung has 20 questions, each scored 1-4
            for (int i = 1; i <= 20; i++) {
                Integer score = parseIntegerFromResponse(responses.get("q" + i));
                if (score != null && score >= 1 && score <= 4) {
                    totalScore += score;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calculating Zung score: " + e.getMessage());
        }
        return Math.min(totalScore, 80); // Zung max score is 80 (20 * 4)
    }

    private int calculateDASS21Score(Map<String, Object> responses) {
        int totalScore = 0;
        try {
            // DASS-21 has 21 questions, each scored 0-3
            for (int i = 1; i <= 21; i++) {
                Integer score = parseIntegerFromResponse(responses.get("q" + i));
                if (score != null && score >= 0 && score <= 3) {
                    totalScore += score;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calculating DASS-21 score: " + e.getMessage());
        }
        return Math.min(totalScore, 63); // DASS-21 max score is 63 (21 * 3)
    }

    private Integer parseIntegerFromResponse(Object obj) {
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

    private int calculateMMSEScore(Map<String, Object> responses) {
        int totalScore = 0;
        try {
            // MMSE has 30 points total, questions vary in scoring
            for (int i = 1; i <= 30; i++) {
                Integer score = parseIntegerFromResponse(responses.get("q" + i));
                if (score != null && score >= 0 && score <= 1) {
                    totalScore += score;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calculating MMSE score: " + e.getMessage());
        }
        return Math.min(totalScore, 30); // MMSE max score is 30
    }

    private String interpretMMSE(int score) {
        String interpretation = "Điểm MMSE: " + score + "/30. ";
        if (score >= 24) {
            return interpretation + "Nhận thức bình thường. Không có dấu hiệu suy giảm nhận thức.";
        } else if (score >= 18) {
            return interpretation + "Suy giảm nhận thức nhẹ. Nên theo dõi và đánh giá thêm.";
        } else if (score >= 10) {
            return interpretation + "Suy giảm nhận thức vừa. Cần can thiệp và hỗ trợ.";
        } else {
            return interpretation + "Suy giảm nhận thức nặng. Cần chăm sóc đặc biệt và điều trị tích cực.";
        }
    }

    // Interpretation methods
    private String generateInterpretation(SurveyType surveyType, int totalScore) {
        switch (surveyType) {
            case PSQI:
                return interpretPSQI(totalScore);
            case BECK:
                return interpretBeck(totalScore);
            case ZUNG:
                return interpretZung(totalScore);
            case DASS21:
                return interpretDASS21(totalScore);
            case MMSE:
                return interpretMMSE(totalScore);
            default:
                return "Không có diễn giải cho loại khảo sát này";
        }
    }

    private String interpretPSQI(int score) {
        if (score <= 5) {
            return "Chất lượng giấc ngủ tốt (điểm PSQI: " + score + "/21). " +
                    "Điểm số này cho thấy bạn có chất lượng giấc ngủ bình thường.";
        } else {
            return "Chất lượng giấc ngủ kém (điểm PSQI: " + score + "/21). " +
                    "Điểm > 5 cho thấy có vấn đề về giấc ngủ cần được quan tâm và có thể cần tư vấn từ chuyên gia.";
        }
    }

    private String interpretBeck(int score) {
        String interpretation = "Điểm Beck Depression Inventory: " + score + "/63. ";
        if (score <= 13) {
            return interpretation + "Không có hoặc trầm cảm tối thiểu. Tình trạng tâm lý bình thường.";
        } else if (score <= 19) {
            return interpretation + "Trầm cảm nhẹ. Nên theo dõi và có thể cần tư vấn tâm lý.";
        } else if (score <= 28) {
            return interpretation + "Trầm cảm vừa. Khuyến nghị nên tìm kiếm sự trợ giúp chuyên nghiệp.";
        } else {
            return interpretation + "Trầm cảm nặng. Cần được điều trị và theo dõi bởi chuyên gia tâm thần ngay lập tức.";
        }
    }

    private String interpretZung(int score) {
        String interpretation = "Điểm Zung Self-Rating Depression Scale: " + score + "/80. ";
        if (score < 50) {
            return interpretation + "Không có trầm cảm đáng kể. Tình trạng tâm lý bình thường.";
        } else if (score < 60) {
            return interpretation + "Trầm cảm nhẹ đến vừa. Nên theo dõi và có thể cần tư vấn.";
        } else if (score < 70) {
            return interpretation + "Trầm cảm vừa đến nặng. Khuyến nghị tìm kiếm sự trợ giúp chuyên nghiệp.";
        } else {
            return interpretation + "Trầm cảm nặng. Cần được điều trị ngay lập tức bởi chuyên gia tâm thần.";
        }
    }

    private String interpretDASS21(int score) {
        String interpretation = "Điểm DASS-21 tổng: " + score + "/63. ";
        if (score <= 9) {
            return interpretation + "Mức độ bình thường. Không có dấu hiệu đáng lo ngại về trầm cảm, lo âu hay stress.";
        } else if (score <= 13) {
            return interpretation + "Mức độ nhẹ. Có một số dấu hiệu cần theo dõi.";
        } else if (score <= 20) {
            return interpretation + "Mức độ vừa. Nên tìm kiếm sự trợ giúp và tư vấn chuyên nghiệp.";
        } else if (score <= 27) {
            return interpretation + "Mức độ nặng. Cần được điều trị và theo dõi bởi chuyên gia.";
        } else {
            return interpretation + "Mức độ rất nặng. Cần được can thiệp điều trị ngay lập tức.";
        }
    }



}