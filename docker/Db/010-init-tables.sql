SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'circolari';

DROP DATABASE IF EXISTS circolari;

/*
 * Create the Database
 */
CREATE DATABASE circolari;

/*
 * Select the database
 */
\c circolari;

/*
 * BASE TABLES
 */

DROP TABLE IF EXISTS html CASCADE;

CREATE TABLE HTML (
	htlm_ts         TIMESTAMP NOT NULL PRIMARY KEY,
	html            TEXT        DEFAULT ''	NOT NULL
);