package com.waggle.domain.user;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserDayOfWeek;
import com.waggle.domain.user.entity.UserIndustry;
import com.waggle.domain.user.entity.UserIntroduction;
import com.waggle.domain.user.entity.UserJobRole;
import com.waggle.domain.user.entity.UserPortfolio;
import com.waggle.domain.user.entity.UserSkill;
import java.util.List;

public record UserInfo(
    User user,
    List<UserJobRole> userJobRoles,
    List<UserIndustry> userIndustries,
    List<UserSkill> userSkills,
    List<UserDayOfWeek> userDaysOfWeek,
    List<UserIntroduction> userIntroductions,
    List<UserPortfolio> userPortfolios
) {

    public static UserInfo of(
        User user,
        List<UserJobRole> userJobRoles,
        List<UserIndustry> userIndustries,
        List<UserSkill> userSkills,
        List<UserDayOfWeek> userDaysOfWeek,
        List<UserIntroduction> userIntroductions,
        List<UserPortfolio> userPortfolios
    ) {
        return new UserInfo(
            user,
            userJobRoles,
            userIndustries,
            userSkills,
            userDaysOfWeek,
            userIntroductions,
            userPortfolios
        );
    }
}
