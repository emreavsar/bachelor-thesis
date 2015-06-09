-- This Script creates qualit database and user.
-- Database: qualit
-- User: qualit		Password: qualit

DROP DATABASE if exists qualit;
DROP ROLE if exists qualit;

CREATE ROLE qualit LOGIN
  ENCRYPTED PASSWORD 'md5e1d0cc129a529dd399d44152fbda2f22'
  SUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;

CREATE DATABASE qualit
  WITH OWNER = qualit
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       CONNECTION LIMIT = -1;