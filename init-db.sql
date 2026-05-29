-- Create all databases
CREATE DATABASE IF NOT EXISTS smartshop_user;
CREATE DATABASE IF NOT EXISTS smartshop_product;
CREATE DATABASE IF NOT EXISTS smartshop_order;
CREATE DATABASE IF NOT EXISTS smartshop_payment;

-- Give the 'smartshop' user full access to all databases
GRANT ALL PRIVILEGES ON smartshop_user.* TO 'smartshop'@'%';
GRANT ALL PRIVILEGES ON smartshop_product.* TO 'smartshop'@'%';
GRANT ALL PRIVILEGES ON smartshop_order.* TO 'smartshop'@'%';
GRANT ALL PRIVILEGES ON smartshop_payment.* TO 'smartshop'@'%';

-- Apply the changes immediately
FLUSH PRIVILEGES;