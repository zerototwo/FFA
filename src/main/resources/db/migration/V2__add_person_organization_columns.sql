-- Migration: add organization tracking columns to person table
-- Enables registering users to choose an embassy or other organisation

ALTER TABLE person
    ADD COLUMN IF NOT EXISTS organization_type VARCHAR(50);

ALTER TABLE person
    ADD COLUMN IF NOT EXISTS organization_id BIGINT;

ALTER TABLE person
    ADD COLUMN IF NOT EXISTS organization_name VARCHAR(255);

