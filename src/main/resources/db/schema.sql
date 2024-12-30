-- Create the database
CREATE DATABASE simplified_crm;

-- Create the Client table
CREATE TABLE IF NOT EXISTS Client
(
    id           SERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    industry     VARCHAR(255),
    address      TEXT
);

-- Create the Contact table
CREATE TABLE IF NOT EXISTS Contact
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20),
    client_id  INT          NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Client (id)
);

-- Create the Task table
CREATE TABLE IF NOT EXISTS Task
(
    id          SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    status      VARCHAR(50) CHECK (status IN ('Open', 'In Progress', 'Completed')),
    due_date    DATE,
    contact_id  INT  NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES Contact (id)
);


-- Create the User table
CREATE TABLE IF NOT EXISTS Audit
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    enabled  boolean     NOT NULL default true,
    expired  boolean     NOT NULL default false,
    locked   boolean     NOT NULL default true
);