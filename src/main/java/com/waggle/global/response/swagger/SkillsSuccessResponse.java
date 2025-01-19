package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.Skill;
import com.waggle.global.response.SuccessResponse;

public class SkillsSuccessResponse extends SuccessResponse<Skill[]> {
    public SkillsSuccessResponse(int code, String message, Skill[] payload) {
        super(code, message, payload);
    }
}
