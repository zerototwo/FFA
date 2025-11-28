package com.isep.ffa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Create Project Request DTO
 * Used for creating a new project
 */
@Data
@Schema(description = "Request payload for creating a new project")
public class CreateProjectRequest {

  @Schema(description = "Project name", example = "Youth Entrepreneurship Program 2024", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Project name is required")
  private String name;

  @Schema(description = "Project description", example = "A comprehensive program to support young entrepreneurs in developing their business ideas.", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Project description is required")
  private String description;

  @Schema(description = "Project status (DRAFT, PENDING_APPROVAL, PUBLISHED). Defaults to DRAFT if not provided.", example = "DRAFT", allowableValues = {
      "DRAFT", "PENDING_APPROVAL", "PUBLISHED" })
  private String status;

  @Schema(description = "Total budget for the project", example = "50000.00")
  private BigDecimal totalBudget;

  @Schema(description = "Project start date", example = "2024-02-01")
  private LocalDate startDate;

  @Schema(description = "Submission date", example = "2024-01-15")
  private LocalDate submissionDate;

  @Schema(description = "Location city ID", example = "1")
  private Long locationId;
}
