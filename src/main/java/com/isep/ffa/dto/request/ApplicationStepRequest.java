package com.isep.ffa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Application step request DTO
 * Used for multi-step application submission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for application step submission")
public class ApplicationStepRequest {

  @Schema(description = "Application ID (for updates, null for new applications)", example = "1", nullable = true)
  private Long applicationId;

  @Schema(description = "Step number (1-5)", example = "1", allowableValues = {"1", "2", "3", "4", "5"})
  @NotNull(message = "Step number is required")
  @Min(value = 1, message = "Step number must be between 1 and 5")
  private Integer step;

  @Schema(description = "Project ID", example = "1")
  @NotNull(message = "Project ID is required")
  private Long projectId;

  // Step 1: Basics
  @Schema(description = "Application title (Step 1)", example = "Youth Entrepreneurship Program 2024", nullable = true)
  private String title;

  @Schema(description = "Application description (Step 1)", example = "A comprehensive program...", nullable = true)
  private String description;

  @Schema(description = "Start date (Step 1)", example = "2024-02-01", nullable = true)
  private LocalDate startDate;

  @Schema(description = "End date (Step 1)", example = "2024-12-31", nullable = true)
  private LocalDate endDate;

  @Schema(description = "Location city ID (Step 1)", example = "1", nullable = true)
  private Long locationId;

  // Step 2: Scope
  @Schema(description = "Project scope (Step 2)", example = "The project aims to support...", nullable = true)
  private String scope;

  // Step 3: Budget
  @Schema(description = "Total budget (Step 3)", example = "50000.00", nullable = true)
  private BigDecimal budget;

  // Step 4: Documents (handled separately via document upload endpoints)
  @Schema(description = "Document requirements (Step 4) - list of document type IDs", nullable = true)
  private List<Long> documentTypeIds;

  // Step 5: Review and submit
  @Schema(description = "Final submission flag (Step 5)", example = "false", nullable = true)
  private Boolean submit;

  @Schema(description = "Save as draft", example = "true", nullable = true)
  private Boolean saveDraft;
}

