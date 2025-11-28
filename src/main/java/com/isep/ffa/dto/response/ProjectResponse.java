package com.isep.ffa.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Project Response DTO
 * Simplified project information for API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project information response")
public class ProjectResponse {

  @Schema(description = "Project ID", example = "1")
  private Long id;

  @Schema(description = "Project name", example = "Youth Entrepreneurship Program 2024")
  private String name;

  @Schema(description = "Project description", example = "A comprehensive program to support young entrepreneurs in developing their business ideas.")
  private String description;

  @Schema(description = "Project status", example = "DRAFT", allowableValues = { "DRAFT", "PENDING_APPROVAL",
      "PUBLISHED" })
  private String status;

  @Schema(description = "Total budget for the project", example = "50000.00")
  private BigDecimal totalBudget;

  @Schema(description = "Project start date", example = "2024-02-01")
  private LocalDate startDate;

  @Schema(description = "Submission date", example = "2024-01-15")
  private LocalDate submissionDate;

  @Schema(description = "Location city ID", example = "1")
  private Long locationId;

  @Schema(description = "Intervener ID (creator of the project)", example = "3")
  private Long intervenerId;

  @Schema(description = "Winner user ID (if project has a winner)", example = "5", nullable = true)
  private Long winnerUserId;

  @Schema(description = "Creation date", example = "2024-01-15")
  private LocalDate creationDate;

  @Schema(description = "Last modification date", example = "2024-01-15")
  private LocalDate lastModificationDate;
}
