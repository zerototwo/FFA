-- Add missing columns to project table
-- This migration adds start_date and location_id columns

-- Add start_date column
ALTER TABLE project ADD COLUMN IF NOT EXISTS start_date DATE;

-- Add location_id column (references city table)
ALTER TABLE project ADD COLUMN IF NOT EXISTS location_id BIGINT REFERENCES city(id);

