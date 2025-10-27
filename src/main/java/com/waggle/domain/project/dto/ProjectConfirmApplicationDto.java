package com.waggle.domain.project.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProjectConfirmApplicationDto(@NotNull UUID projectApplicantId) {

}
