-- Add new fields to project table for enhanced project management
-- Migration: V3__add_project_fields.sql

-- Add project status field (DRAFT, PENDING_APPROVAL, PUBLISHED)
ALTER TABLE project ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'DRAFT';
COMMENT ON COLUMN project.status IS 'Project status: DRAFT, PENDING_APPROVAL, PUBLISHED';

-- Add total budget field
ALTER TABLE project ADD COLUMN IF NOT EXISTS total_budget DECIMAL(15,2);
COMMENT ON COLUMN project.total_budget IS 'Total budget for the project';

-- Add start date field
ALTER TABLE project ADD COLUMN IF NOT EXISTS start_date DATE;
COMMENT ON COLUMN project.start_date IS 'Project start date';

-- Add location field (using city_id to reference city table)
ALTER TABLE project ADD COLUMN IF NOT EXISTS location_id BIGINT REFERENCES city(id);
COMMENT ON COLUMN project.location_id IS 'Location of the project (references city table)';

-- Create index on status for faster filtering
CREATE INDEX IF NOT EXISTS idx_project_status ON project(status) WHERE is_deleted = false;

-- Create index on location_id for faster queries
CREATE INDEX IF NOT EXISTS idx_project_location ON project(location_id) WHERE is_deleted = false;

