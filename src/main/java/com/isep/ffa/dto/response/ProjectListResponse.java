package com.isep.ffa.dto.response;

import com.isep.ffa.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Project list response DTO
 * Contains project information optimized for list display
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project information for list display")
public class ProjectListResponse {

    @Schema(description = "Project ID", example = "1")
    private Long id;

    @Schema(description = "Project name", example = "Youth Entrepreneurship Program 2024")
    private String name;

    @Schema(description = "Project description", example = "Supporting young entrepreneurs in francophone communities")
    private String description;

    @Schema(description = "Total budget", example = "50000.00")
    private BigDecimal totalBudget;

    @Schema(description = "Start date", example = "2024-02-01")
    private LocalDate startDate;

    @Schema(description = "Location city ID", example = "1")
    private Long locationId;

    @Schema(description = "Location city name", example = "Ottawa")
    private String locationName;

    @Schema(description = "Location full address", example = "Ottawa, Ontario, Canada")
    private String location;

    @Schema(description = "Number of applications", example = "24")
    private Long applicationCount;

    @Schema(description = "Project status", example = "PUBLISHED", allowableValues = {"DRAFT", "PENDING_APPROVAL", "PUBLISHED"})
    private String status;

    @Schema(description = "Creation date", example = "2024-01-15")
    private LocalDate creationDate;

    /**
     * Convert Project entity to ProjectListResponse
     */
    public static ProjectListResponse fromProject(Project project, Long applicationCount) {
        if (project == null) {
            return null;
        }
        ProjectListResponse response = ProjectListResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .totalBudget(project.getTotalBudget())
            .startDate(project.getStartDate())
            .locationId(project.getLocationId())
            .applicationCount(applicationCount != null ? applicationCount : 0L)
            .status(project.getStatus() != null ? project.getStatus() : "DRAFT")
            .creationDate(project.getCreationDate())
            .build();
        
        // Set location name if available
        if (project.getLocation() != null) {
            response.setLocationName(project.getLocation().getName());
            // Build full location string if needed
            response.setLocation(project.getLocation().getName());
        }
        
        return response;
    }
}

