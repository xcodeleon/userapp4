  -- liquibase formatted sql

    -- changeset author_xcodeleon:2

    -- precondition onFail:MARK_RAN

    -- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_indexes WHERE indexname = 'idx_users_login' AND tablename = 'users';

  -- comment: Создание индекса для поля login в таблице users, если он не существует

    CREATE INDEX idx_users_login ON users(login);



    -- rollback

    DROP INDEX IF EXISTS idx_users_login;