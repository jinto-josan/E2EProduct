-- Schema for SubSystem
CREATE TABLE IF NOT EXISTS SUB_SYSTEM ( name VARCHAR(255) PRIMARY KEY, description VARCHAR(255));

-- Schema for Request
CREATE TABLE IF NOT EXISTS REQUEST (path VARCHAR(255) NOT NULL,method VARCHAR(10) NOT NULL,body CLOB,headers CLOB,sub_system_name VARCHAR(255),PRIMARY KEY (path, method),FOREIGN KEY (sub_system_name) REFERENCES SubSystem(name));

-- Schema for Response
CREATE TABLE IF NOT EXISTS RESPONSE (request_path VARCHAR(255) NOT NULL,request_http_method VARCHAR(10) NOT NULL,status_code INT NOT NULL,body CLOB,headers CLOB,PRIMARY KEY (request_path, request_httpMethod, status_code),FOREIGN KEY (request_path, request_httpMethod) REFERENCES Request(path, httpMethod));

-- Sample data for SubSystem
--INSERT INTO SUB_SYSTEM (name, description) VALUES ('Subsystem A', 'Description for Subsystem A');
--INSERT INTO SUB_SYSTEM (name, description) VALUES ('Subsystem B', 'Description for Subsystem B');
--
---- Sample data for Request
--INSERT INTO REQUEST (path, method, body, headers, sub_system_name) VALUES ('/api/v1/resource', 'GET', '{"key1": "value1", "key2": "value2"}', '{"header1": "value1"}', 'Subsystem A');
--INSERT INTO REQUEST (path, method, body, headers, sub_system_name) VALUES ('/api/v1/resource', 'POST', '{"key3": "value3", "key4": "value4"}', '{"header2": "value2"}', 'Subsystem A');
--INSERT INTO REQUEST (path, method, body, headers, sub_system_name) VALUES ('/api/v2/resource', 'GET', '{"key5": "value5", "key6": "value6"}', '{"header3": "value3"}', 'Subsystem B');
--
---- Sample data for Response
--INSERT INTO RESPONSE (request_path, request_http_method, status_code, body, headers) VALUES ('/api/v1/resource', 'GET', 200, '{"responseKey1": "responseValue1"}', '{"header1": "value1"}');
--INSERT INTO RESPONSE (request_path, request_http_method, status_code, body, headers) VALUES ('/api/v1/resource', 'GET', 404, '{"responseKey2": "responseValue2"}', '{"header2": "value2"}');
--INSERT INTO RESPONSE (request_path, request_http_method, status_code, body, headers) VALUES ('/api/v1/resource', 'POST', 201, '{"responseKey3": "responseValue3"}', '{"header3": "value3"}');
--INSERT INTO RESPONSE (request_path, request_http_method, status_code, body, headers) VALUES ('/api/v2/resource', 'GET', 500, '{"responseKey4": "responseValue4"}', '{"header4": "value4"}');