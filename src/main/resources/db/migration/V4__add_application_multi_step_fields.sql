-- Add multi-step application fields to support the 5-step application process
-- Step 1: Basics (title, description, start_date, end_date)
-- Step 2: Scope
-- Step 3: Budget
-- Step 4: Documents (handled via documents_submitted table)
-- Step 5: Review (status change)

ALTER TABLE application ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'DRAFT';
ALTER TABLE application ADD COLUMN IF NOT EXISTS title VARCHAR(200);
ALTER TABLE application ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE application ADD COLUMN IF NOT EXISTS scope TEXT;
ALTER TABLE application ADD COLUMN IF NOT EXISTS budget DECIMAL(15,2);
ALTER TABLE application ADD COLUMN IF NOT EXISTS start_date DATE;
ALTER TABLE application ADD COLUMN IF NOT EXISTS end_date DATE;
ALTER TABLE application ADD COLUMN IF NOT EXISTS location_id BIGINT REFERENCES city(id);
ALTER TABLE application ADD COLUMN IF NOT EXISTS current_step INTEGER DEFAULT 1;

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_application_status ON application (status);
CREATE INDEX IF NOT EXISTS idx_application_user_status ON application (user_id, status);
CREATE INDEX IF NOT EXISTS idx_application_location_id ON application (location_id);

