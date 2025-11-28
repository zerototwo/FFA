-- Add total_budget column to project table
-- This column stores the total budget for the project

ALTER TABLE project ADD COLUMN IF NOT EXISTS total_budget DECIMAL(15,2);

