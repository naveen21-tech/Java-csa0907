CREATE DATABASE IF NOT EXISTS campus_passes;
USE campus_passes;
CREATE TABLE IF NOT EXISTS bus_pass(
 pass_id VARCHAR(32) PRIMARY KEY,
 reg_no VARCHAR(32) NOT NULL,
 name VARCHAR(120) NOT NULL,
 route_no INT NOT NULL,
 amount DECIMAL(12,2) NOT NULL,
 INDEX idx_bus_pass_reg(reg_no),
 INDEX idx_bus_pass_route(route_no)
);
