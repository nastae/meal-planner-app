-- Drop all tables in public schema
DO $$ DECLARE
    r RECORD;
BEGIN
    -- Drop all tables
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;

-- Drop all enums in public schema
DO $$ DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT n.nspname AS schema, t.typname AS enum_name
              FROM pg_type t
              JOIN pg_enum e ON t.oid = e.enumtypid
              JOIN pg_catalog.pg_namespace n ON n.oid = t.typnamespace
              GROUP BY schema, enum_name) LOOP
        EXECUTE 'DROP TYPE IF EXISTS ' || quote_ident(r.enum_name) || ' CASCADE';
    END LOOP;
END $$;