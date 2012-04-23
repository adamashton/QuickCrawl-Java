--
DROP DATABASE IF EXISTS quick_crawl;

--
CREATE DATABASE quick_crawl;

-- Specify the db to use for all this
USE quick_crawl;

-- create customer table
CREATE TABLE IF NOT EXISTS customer(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(32) NOT NULL,
    created TIMESTAMP DEFAULT NOW()) ENGINE=innodb;
    
-- create crawl table
CREATE TABLE IF NOT EXISTS crawl(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    name VARCHAR(32),
    start_point VARCHAR(64),
    end_point VARCHAR(64),
    created TIMESTAMP DEFAULT NOW()) ENGINE=innodb;
    
-- create aggregate_place table
CREATE TABLE IF NOT EXISTS aggregate_place(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    qype_ids VARCHAR(100),
    yelp_ids VARCHAR(100)) ENGINE=innodb;
    
-- create crawl_places table
CREATE TABLE IF NOT EXISTS crawl_places(
    crawl_id INT NOT NULL,
    aggregate_place_id INT NOT NULL,
    place_index INT NOT NULL) ENGINE=innodb;
