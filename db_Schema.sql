-- Users Table
CREATE TABLE USERS (
    user_id NUMBER PRIMARY KEY,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('BUYER', 'SELLER', 'ADMIN')),
    name VARCHAR2(100),
    phone VARCHAR2(20),
    address VARCHAR2(255),
    security_question VARCHAR2(255),
    security_answer VARCHAR2(255),
    registration_date DATE DEFAULT SYSDATE
);

CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1;

-- Categories Table
CREATE TABLE CATEGORIES (
    category_id NUMBER PRIMARY KEY,
    category_name VARCHAR2(100) UNIQUE NOT NULL
);

CREATE SEQUENCE CATEGORY_SEQ START WITH 1 INCREMENT BY 1;

-- Products Table
CREATE TABLE PRODUCTS (
    product_id NUMBER PRIMARY KEY,
    seller_id NUMBER REFERENCES USERS(user_id),
    category_id NUMBER REFERENCES CATEGORIES(category_id),
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(500),
    price NUMBER(10, 2) NOT NULL,
    mrp NUMBER(10, 2), -- Maximum Retail Price
    stock_quantity NUMBER DEFAULT 0,
    image_url VARCHAR2(255),
    is_active NUMBER(1) DEFAULT 1, -- 1 for active, 0 for inactive
    created_at DATE DEFAULT SYSDATE
);

CREATE SEQUENCE PRODUCT_SEQ START WITH 1 INCREMENT BY 1;

-- Cart Table
CREATE TABLE CART (
    cart_id NUMBER PRIMARY KEY,
    buyer_id NUMBER REFERENCES USERS(user_id),
    product_id NUMBER REFERENCES PRODUCTS(product_id),
    quantity NUMBER DEFAULT 1,
    added_at DATE DEFAULT SYSDATE,
    CONSTRAINT unique_cart_item UNIQUE (buyer_id, product_id)
);

CREATE SEQUENCE CART_SEQ START WITH 1 INCREMENT BY 1;

-- Orders Table
CREATE TABLE ORDERS (
    order_id NUMBER PRIMARY KEY,
    buyer_id NUMBER REFERENCES USERS(user_id),
    total_amount NUMBER(10, 2),
    order_status VARCHAR2(20) DEFAULT 'PENDING' CHECK (order_status IN ('PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    order_date DATE DEFAULT SYSDATE,
    shipping_address VARCHAR2(255),
    payment_method VARCHAR2(50)
);

CREATE SEQUENCE ORDER_SEQ START WITH 1 INCREMENT BY 1;

-- Order Items Table
CREATE TABLE ORDER_ITEMS (
    order_item_id NUMBER PRIMARY KEY,
    order_id NUMBER REFERENCES ORDERS(order_id),
    product_id NUMBER REFERENCES PRODUCTS(product_id),
    quantity NUMBER,
    price_per_unit NUMBER(10, 2)
);

CREATE SEQUENCE ORDER_ITEM_SEQ START WITH 1 INCREMENT BY 1;

-- Reviews Table
CREATE TABLE REVIEWS (
    review_id NUMBER PRIMARY KEY,
    product_id NUMBER REFERENCES PRODUCTS(product_id),
    buyer_id NUMBER REFERENCES USERS(user_id),
    rating NUMBER(1) CHECK (rating BETWEEN 1 AND 5),
    review_text VARCHAR2(500),
    review_date DATE DEFAULT SYSDATE
);

CREATE SEQUENCE REVIEW_SEQ START WITH 1 INCREMENT BY 1;

-- Initial Categories
INSERT INTO CATEGORIES (category_id, category_name) VALUES (CATEGORY_SEQ.NEXTVAL, 'Electronics');
INSERT INTO CATEGORIES (category_id, category_name) VALUES (CATEGORY_SEQ.NEXTVAL, 'Fashion');
INSERT INTO CATEGORIES (category_id, category_name) VALUES (CATEGORY_SEQ.NEXTVAL, 'Books');
INSERT INTO CATEGORIES (category_id, category_name) VALUES (CATEGORY_SEQ.NEXTVAL, 'Home & Kitchen');

COMMIT;

select *from users;

-- 1. Create sequence first (safe even if table exists later)
CREATE SEQUENCE favorites_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 2. Create table (without auto-increment column syntax)
CREATE TABLE favorites (
    favorite_id     NUMBER(10)   PRIMARY KEY,
    user_id         NUMBER(10)   NOT NULL,
    product_id      NUMBER(10)   NOT NULL,
    added_at        TIMESTAMP    DEFAULT SYSTIMESTAMP  NOT NULL,
    CONSTRAINT uk_favorites_user_product UNIQUE (user_id, product_id),
    CONSTRAINT fk_favorites_user     FOREIGN KEY (user_id)    REFERENCES users(user_id)    ON DELETE CASCADE,
    CONSTRAINT fk_favorites_product   FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 3. Create trigger to make favorite_id auto-increment
CREATE OR REPLACE TRIGGER favorites_trg
BEFORE INSERT ON favorites
FOR EACH ROW
BEGIN
    :NEW.favorite_id := favorites_seq.NEXTVAL;
END;
/

INSERT INTO favorites (user_id, product_id) VALUES (1, 1);


SELECT * FROM favorites;
select *from users;

SELECT * FROM users ORDER BY user_id;
SELECT * FROM products ORDER BY product_id;

SELECT * FROM categories;
select *from products;
select *from orders;

select *from order_items;

ALTER TABLE order_items
ADD status VARCHAR2(20) DEFAULT 'PENDING';

CREATE SEQUENCE notifications_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE notifications (
    notification_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    message VARCHAR2(255),
    created_at DATE DEFAULT SYSDATE,
    is_read CHAR(1) DEFAULT 'N'
);

INSERT INTO notifications
(notification_id, user_id, message)
VALUES
(notifications_seq.NEXTVAL, 1, 'Test notification');


select *from notifications;


SELECT constraint_name, search_condition
FROM user_constraints
WHERE table_name = 'ORDERS'
AND constraint_type = 'C';

ALTER TABLE orders
DROP CONSTRAINT SYS_C007669;

ALTER TABLE orders
ADD CONSTRAINT chk_order_status
CHECK (order_status IN ('PENDING', 'SUCCESS', 'CANCELLED'));

SELECT DISTINCT user_id
FROM notifications
WHERE user_id NOT IN (SELECT user_id FROM users);

ALTER TABLE notifications
ADD CONSTRAINT fk_notifications_user
FOREIGN KEY (user_id)
REFERENCES users(user_id);




