-- Add missing columns to project table
-- Execute this SQL directly in your PostgreSQL database

-- Add total_budget column
ALTER TABLE project ADD COLUMN IF NOT EXISTS total_budget DECIMAL(15,2);

-- Add start_date column
ALTER TABLE project ADD COLUMN IF NOT EXISTS start_date DATE;

-- Add location_id column (references city table)
ALTER TABLE project ADD COLUMN IF NOT EXISTS location_id BIGINT REFERENCES city(id);

-- Verify the columns were added
SELECT column_name, data_type, numeric_precision, numeric_scale 
FROM information_schema.columns 
WHERE table_name = 'project' 
  AND column_name IN ('total_budget', 'start_date', 'location_id')
ORDER BY column_name;

