-- Remove alternate_names column from cities table as it's excess information not being used

ALTER TABLE cities DROP COLUMN IF EXISTS alternate_names;

