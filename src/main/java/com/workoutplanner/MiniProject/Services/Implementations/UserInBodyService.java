package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.UserInbody;
import com.workoutplanner.MiniProject.Payload.Request.UserInBodyRequest;
import com.workoutplanner.MiniProject.Payload.Response.UserInbodyResponse;
import com.workoutplanner.MiniProject.Repositories.UserInbodyRepository;
import com.workoutplanner.MiniProject.Repositories.UserRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutScheduleRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IUserInBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInBodyService implements IUserInBodyService {
    @Autowired
    private UserInbodyRepository userInbodyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkoutScheduleRepository workoutScheduleRepository;

    public UserInBodyService(UserInbodyRepository userInbodyRepository, UserRepository userRepository) {
        this.userInbodyRepository = userInbodyRepository;
        this.userRepository = userRepository;
    }

    public int calculateAge(LocalDate birthDate) {
        if(birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Male: (1.20 * BMI) + (0.23 * age) - 16.2
    // Female: (1.20 * BMI) + (0.23 * age) - 5.4
    // activityFactor: điều chỉnh nếu người dùng tập nhiều lần trong tuần (giảm body fat 1-2%)

    private BigDecimal calculateBodyFatPercentage(BigDecimal weight, BigDecimal height, int age, String gender, int frequencyPerWeek) {
        if (weight == null || height == null || gender == null) return null;

        double h = height.doubleValue(); // cm
        double w = weight.doubleValue(); // kg
        double bmi = w / Math.pow(h / 100, 2);

        double activityFactor = 0.0;
        if (frequencyPerWeek >= 5) activityFactor = -2.0;
        else if (frequencyPerWeek >= 3) activityFactor = -1.0;

        double bodyFat;
        if ("Male".equalsIgnoreCase(gender) || "M".equalsIgnoreCase(gender)) {
            bodyFat = (1.20 * bmi) + (0.23 * age) - 16.2 + activityFactor;
        } else {
            bodyFat = (1.20 * bmi) + (0.23 * age) - 5.4 + activityFactor;
        }

        return BigDecimal.valueOf(bodyFat).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    //    double intensityFactor = frequencyPerWeek * 0.02;
    //    double muscleMass = lbm * 0.7 * (1 + intensityFactor);

    private BigDecimal calculateMuscleMass(BigDecimal weight, BigDecimal bodyFatPercentage, int frequencyPerWeek) {
        if (weight == null || bodyFatPercentage == null) return null;

        double w = weight.doubleValue();
        double fat = bodyFatPercentage.doubleValue();
        // LBM = Lean Body Mass = trọng lượng cơ thể không tính mỡ.
        double lbm = w * (1 - fat / 100);

        double intensityFactor = frequencyPerWeek * 0.02;
        // muscleMass ≈ 70% * LBM, điều chỉnh dựa trên số buổi tập (intensityFactor).
        double muscleMass = lbm * 0.7 * (1 + intensityFactor);

        return BigDecimal.valueOf(muscleMass).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public List<UserInbodyResponse> getMyInBody() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách InBody và sort theo ngày
        List<UserInbody> userInbodyList = userInbodyRepository.findByUser(user);
        userInbodyList.sort((a, b) -> a.getMeasuredAt().compareTo(b.getMeasuredAt()));

        List<UserInbodyResponse> userInbodyResponses = new ArrayList<>();
        // previousBodyFat và previousMuscleMass dùng để so sánh trend tăng/giảm.
        BigDecimal previousBodyFat = null;
        BigDecimal previousMuscleMass = null;

        // Vòng lặp từng record InBody
        for (UserInbody userInbody : userInbodyList) {
            UserInbodyResponse response = new UserInbodyResponse();
            response.setId(userInbody.getId());
            response.setFullName(userInbody.getUser().getFullName());
            response.setMeasuredAt(userInbody.getMeasuredAt());
            response.setHeight(userInbody.getHeight());
            response.setWeight(userInbody.getWeight());

            int age = calculateAge(userInbody.getUser().getBirthday());
            userInbody.setAge(age);
            String gender = userInbody.getUser().getGender();

            // Số buổi tập trong 7 ngày gần nhất
            int frequencyPerWeek = workoutScheduleRepository.countByUserIdInLastWeek(userInbody.getUser().getId(), LocalDate.now().minusDays(7));

            // Tính bodyFat và muscleMass
            BigDecimal bodyFat = calculateBodyFatPercentage(userInbody.getWeight(), userInbody.getHeight(), age, gender, frequencyPerWeek);
            BigDecimal muscleMass = calculateMuscleMass(userInbody.getWeight(), bodyFat, frequencyPerWeek);

            // Cập nhật vào DB
            userInbody.setBodyFatPercentage(bodyFat);
            userInbody.setMuscleMass(muscleMass);
            userInbodyRepository.save(userInbody);
            response.setBodyFatPercentage(bodyFat);
            response.setMuscleMass(muscleMass);

            // Tính BMI & Lean Body Mass
            double height = userInbody.getHeight().doubleValue() / 100.0;
            double weight = userInbody.getWeight().doubleValue();

            // BMI = weight / height^2
            BigDecimal bmi = BigDecimal.valueOf(weight / (height * height)).setScale(2, BigDecimal.ROUND_HALF_UP);

            // Lean Body Mass = weight * (1 - bodyFat%)
            BigDecimal leanBodyMass = BigDecimal.valueOf(weight * (1 - bodyFat.doubleValue()/100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            response.setBmi(bmi);
            response.setLeanBodyMass(leanBodyMass);

            // Trend tăng/giảm Body Fat & Muscle Mass
            if (previousBodyFat != null) {
                // So sánh với lần đo trước
                response.setBodyFatTrend(bodyFat.compareTo(previousBodyFat) > 0 ? "up" :
                        bodyFat.compareTo(previousBodyFat) < 0 ? "down" : "stable");
                // Lần đo đầu tiên → trend "stable".
            } else response.setBodyFatTrend("stable");

            if (previousMuscleMass != null) {
                response.setMuscleMassTrend(muscleMass.compareTo(previousMuscleMass) > 0 ? "up" :
                        muscleMass.compareTo(previousMuscleMass) < 0 ? "down" : "stable");
            } else response.setMuscleMassTrend("stable");

            previousBodyFat = bodyFat;
            previousMuscleMass = muscleMass;

            response.setNotes(userInbody.getNotes());
            userInbodyResponses.add(response);
        }

        return userInbodyResponses;
    }

    @Override
    public UserInbodyResponse createUserInBody(UserInBodyRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        int age = calculateAge(user.getBirthday());
        String gender = user.getGender();

        // Tính số buổi tập trong tuần trước
        LocalDate measuredDate = request.getMeasuredAt().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate oneWeekAgo = measuredDate.minusDays(7);
        int frequencyPerWeek = workoutScheduleRepository.countByUserIdInLastWeek(user.getId(), oneWeekAgo);

        // Tính BodyFat và MuscleMass
        BigDecimal bodyFat = calculateBodyFatPercentage(request.getWeight(), request.getHeight(), age, gender, frequencyPerWeek);
        BigDecimal muscleMass = calculateMuscleMass(request.getWeight(), bodyFat, frequencyPerWeek);

        // Tính BMI và LBM
        double height = request.getHeight().doubleValue() / 100.0;
        double weight = request.getWeight().doubleValue();
        BigDecimal bmi = BigDecimal.valueOf(weight / (height * height)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal leanBodyMass = BigDecimal.valueOf(weight * (1 - bodyFat.doubleValue() / 100)).setScale(2, BigDecimal.ROUND_HALF_UP);

        // DTO request -> Entity
        UserInbody userInbody = new UserInbody();
        userInbody.setUser(user);
        userInbody.setAge(age);
        userInbody.setWeight(request.getWeight());
        userInbody.setHeight(request.getHeight());
        userInbody.setMeasuredAt(request.getMeasuredAt());
        userInbody.setNotes(request.getNotes());
        userInbody.setBodyFatPercentage(bodyFat);
        userInbody.setMuscleMass(muscleMass);

        userInbodyRepository.save(userInbody);

        // Entity -> DTO response
        UserInbodyResponse response = new UserInbodyResponse();
        response.setId(userInbody.getId());
        response.setFullName(user.getFullName());
        response.setMeasuredAt(request.getMeasuredAt());
        response.setHeight(request.getHeight());
        response.setWeight(request.getWeight());
        response.setBodyFatPercentage(bodyFat);
        response.setMuscleMass(muscleMass);
        response.setBmi(bmi);
        response.setLeanBodyMass(leanBodyMass);
        response.setNotes(request.getNotes());
        response.setBodyFatTrend("stable");
        response.setMuscleMassTrend("stable");

        return response;
    }
}
